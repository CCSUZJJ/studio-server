package com.mob.studio.service.impl;

import com.alibaba.fastjson.JSON;
import com.mob.studio.dao.PlayerDao;
import com.mob.studio.dao.RoomDao;
import com.mob.studio.domain.Player;
import com.mob.studio.redis.RedisDao;
import com.mob.studio.service.PlayerBankService;
import com.mob.studio.service.PlayerService;
import com.mob.studio.util.MybatisUtil;
import com.mob.studio.util.RedisKey;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;

/**
 * @author: Zhang.Min
 * @since: 2016/3/14
 * @version: 1.7
 */
public class PlayerServiceImpl implements PlayerService {
    private static final Logger logger = Logger.getLogger(PlayerServiceImpl.class);

    private static final SqlSessionFactory sqlSessionFactory_Studio_W = MybatisUtil.getSqlSessionFactory_Studio_W();
    @Autowired
    @Qualifier("playerDao")
    private PlayerDao playerDao;

    @Autowired
    @Qualifier("roomDao")
    private RoomDao roomDao;

    @Autowired
    @Qualifier("redisDao")
    private RedisDao redisDao;

    @Autowired
    @Qualifier("playerBankService")
    private PlayerBankService playerBankService;

    @Override
    public Player login(Player player) {
        boolean isExists = redisDao.hexists(RedisKey.PLAYER + player.getId(), RedisKey.PLAYER_INFO);
        if (isExists) {
            String playerStr = redisDao.hget(RedisKey.PLAYER + player.getId(), RedisKey.PLAYER_INFO);
            if (playerStr != null && playerStr.length() > 0) {
                Player p = JSON.parseObject(playerStr, Player.class);
                //reinstall coin point followers fans data
                reInstallPlayerData(p.getId());
                return JSON.parseObject(playerStr, Player.class);
            }
        }
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        Player p = playerDao.getPlayerByAliasId(sqlSession, player.getAliasId());
        sqlSession.close();
        if (p == null) {
            //do regist
            p = regist(player);
        } else {
            reInstallPlayerData(p.getId());
        }
        return p;

    }

    @Override
    public Player regist(Player player) {
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        Player p = playerDao.insertPlayer(sqlSession, player);
        sqlSession.commit();
        sqlSession.close();
        playerBankService.initBank(p.getId());
        redisDao.hset(RedisKey.PLAYER + p.getId(), RedisKey.PLAYER_INFO, JSON.toJSONString(p));
        redisDao.hset(RedisKey.PLAYER + p.getId(), RedisKey.PLAYER_FANS, JSON.toJSONString(new ArrayList<Long>()));
        redisDao.hset(RedisKey.PLAYER + p.getId(), RedisKey.PLAYER_FOLLOWS, JSON.toJSONString(new ArrayList<Long>()));
        redisDao.hset(RedisKey.PLAYER + p.getId(), RedisKey.PLAYER_BLACKLIST, JSON.toJSONString(new ArrayList<Long>()));
        // 为了一开始也要显示粉丝为0的新用户，因此数据在此初始化
        updatePlayerFansRank(p.getId());
        return p;
    }

    @Override
    public Player update(Player player) {
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        playerDao.updatePlayer(sqlSession, player);
        sqlSession.commit();
        sqlSession.close();
        redisDao.hset(RedisKey.PLAYER + player.getId(), RedisKey.PLAYER_INFO, JSON.toJSONString(player));
        return findPlayerInfoById(player.getId());
    }

    @Override
    public Player getPlayerByAliasId(String aliasId) {
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        Player player = playerDao.getPlayerByAliasId(sqlSession, aliasId);
        sqlSession.close();
        return player;
    }

    @Override
    public Player findPlayerInfoById(Long id) {
        try {
            if (redisDao.hexists(RedisKey.PLAYER + id, RedisKey.PLAYER_INFO)) {
                String data = redisDao.hget(RedisKey.PLAYER + id, RedisKey.PLAYER_INFO);
                return JSON.parseObject(data, Player.class);
            } else {
                SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
                Player player = playerDao.getPlayerById(sqlSession, id);
                sqlSession.close();
                if (player != null) {
                    redisDao.hset(RedisKey.PLAYER + id, RedisKey.PLAYER_INFO, JSON.toJSONString(player));
                    return player;
                }
            }
        } catch (Exception e) {
            logger.error("error occurred:" + e);
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Player> findPlayerByFuzzyNick(String nick, Integer pageNo, Integer pageSize) {
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        List<Player> list = playerDao.findPlayerByFuzzyNick(sqlSession, nick, pageNo, pageSize);
        sqlSession.close();
        return list;
    }

    @Override
    public Boolean doFollow(Long playerId, Long followerId) {
        try {
            //关注者和被关注者不能是同一人
            if (playerId.equals(followerId)) {
                return false;
            }
            Boolean isBlack = isBlack(playerId, followerId);
            if (isBlack) {
                return false;
            }
            List<Long> followerIds = getFollowersByPlayerId(playerId);
            if (!followerIds.contains(followerId)) {
                SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
                playerDao.insertFollower(sqlSession, playerId, followerId);
                sqlSession.commit();
                sqlSession.close();
                followerIds.add(followerId);
                redisDao.hset(RedisKey.PLAYER + playerId, RedisKey.PLAYER_FOLLOWS, JSON.toJSONString(followerIds));

                List<Long> fansIds = getFansByPlayerId(followerId);
                fansIds.add(playerId);
                redisDao.hset(RedisKey.PLAYER + followerId, RedisKey.PLAYER_FANS, JSON.toJSONString(fansIds));

                //update talent rank
                updatePlayerFansRank(followerId);

                //update follower rank
                updatePlayerFollowerRank(playerId, true, followerId);
            } else {
                //已关注
                return false;
            }
        } catch (Exception e) {
            logger.error("error occurred : " + e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean doBlack(Long playerId, Long blackId) {
        try {
            //不允许自黑
            if (playerId.equals(blackId)) {
                return false;
            }
            List<Long> blackIds = getBlackListByPlayerId(playerId);
            if (!blackIds.contains(blackId)) {
                SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
                playerDao.insertBlackList(sqlSession, playerId, blackId);
                sqlSession.commit();
                sqlSession.close();
                blackIds.add(blackId);
                redisDao.hset(RedisKey.PLAYER + playerId, RedisKey.PLAYER_BLACKLIST, JSON.toJSONString(blackIds));
                //取消关注关系
                doUnfollow(playerId, blackId);
                //取消被关注关系
                doUnfollow(blackId, playerId);
            } else {
                //不可重复拉黑
                return false;
            }

        } catch (Exception e) {
            logger.error("error occurred : " + e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean doUnfollow(Long playerId, Long followerId) {
        try {
            SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
            playerDao.deleteFollower(sqlSession, playerId, followerId);
            sqlSession.commit();
            sqlSession.close();
            List<Long> followerIds = getFollowersByPlayerId(playerId);
            if (followerIds != null && followerIds.contains(followerId)) {
                followerIds.remove(followerId);
                redisDao.hset(RedisKey.PLAYER + playerId, RedisKey.PLAYER_FOLLOWS, JSON.toJSONString(followerIds));
            }

            List<Long> fansIds = getFansByPlayerId(followerId);
            if (fansIds != null && fansIds.contains(playerId)) {
                fansIds.remove(playerId);
                redisDao.hset(RedisKey.PLAYER + followerId, RedisKey.PLAYER_FANS, JSON.toJSONString(fansIds));
            }

            //update talent rank
            updatePlayerFansRank(followerId);

            //update follower rank
            updatePlayerFollowerRank(playerId, false, followerId);

        } catch (Exception e) {
            logger.error("error occurred : " + e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean doUnblack(Long playerId, Long blackId) {
        try {
            SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
            playerDao.deleteBlackList(sqlSession, playerId, blackId);
            sqlSession.commit();
            sqlSession.close();
            List<Long> blackIds = getBlackListByPlayerId(playerId);
            if (blackIds != null && blackIds.contains(blackId)) {
                blackIds.remove(blackId);
                redisDao.hset(RedisKey.PLAYER + playerId, RedisKey.PLAYER_BLACKLIST, JSON.toJSONString(blackIds));
            }
        } catch (Exception e) {
            logger.error("error occurred : " + e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean isFollower(Long playerId, Long targetId) {
        Boolean isFollower = false;
        List<Long> followerIds = getFollowersByPlayerId(playerId);
        if (followerIds != null && followerIds.contains(targetId)) {
            isFollower = true;
        }
        return isFollower;
    }

    @Override
    public Boolean isBlack(Long playerId, Long targetId) {
        Boolean isBlack = false;
        Boolean oppositeIsBlack = false;
        List<Long> blackIds = getBlackListByPlayerId(playerId);
        if (blackIds != null && blackIds.contains(targetId)) {
            isBlack = true;
        }
        List<Long> oppositeBlackIds = getBlackListByPlayerId(targetId);
        if (oppositeBlackIds != null && oppositeBlackIds.contains(playerId)) {
            oppositeIsBlack = true;
        }
        return (isBlack || oppositeIsBlack);
    }

    @Override
    public Map<String, Object> getMyProfileDetail(Long playerId) {
        //info
        Player player = findPlayerInfoById(playerId);
        //fans
        int fans = getFansByPlayerId(playerId).size();
        //followers
        int followers = getFollowersByPlayerId(playerId).size();
        //coin
        Long coins = playerBankService.getPlayerCoinById(playerId);
        // coin used
        Long send = playerBankService.getPlayerSendById(playerId);
        // gift top
        List<Player> giftTop = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<String>> dataTuple = redisDao.zRevRangeWithScore(RedisKey.GIFT_RANK + playerId, 0, 2);
        for (ZSetOperations.TypedTuple<String> tuple : dataTuple) {
            if (tuple.getValue() != null && tuple.getValue().length() > 0) {
                Player p = findPlayerInfoById(Long.valueOf(tuple.getValue()));
                giftTop.add(p);
            }
        }
        //point
//        Long point = playerBankService.getPlayerPointById(playerId);
        Long point = playerBankService.getPlayersFlowersById(playerId);


        Map<String, Object> detail = new HashMap<>();
        detail.put("player", player);
        detail.put("fans", fans);
        detail.put("followers", followers);
        detail.put("coins", coins);
        detail.put("send", send);
        detail.put("giftTop", giftTop);
        detail.put("point",point);
        return detail;
    }

    @Override
    public List<Map<String, Object>> findFollowerByPlayerId(Long playerId, Integer pageNo, Integer pageSize) {
        List<Map<String, Object>> followerMapList = new ArrayList<>();
        List<Long> followerIdsAll = getFollowersByPlayerId(playerId);
        List<Long> pageFollowerIdList = pageList(followerIdsAll, pageNo, pageSize);
        for (Long followerId : pageFollowerIdList) {
            Map<String, Object> map = new HashMap<>();
            Player player = findPlayerInfoById(followerId);
            map.put("player", player);
            map.put("isFollower", true);
            followerMapList.add(map);
        }
        return followerMapList;
    }

    @Override
    public List<Map<String, Object>> findFollowerByPlayerId(Long playerId) {
        List<Map<String, Object>> followerMapList = new ArrayList<>();
        List<Long> followerIdsAll = getFollowersByPlayerId(playerId);
        for (Long followerId : followerIdsAll) {
            Map<String, Object> map = new HashMap<>();
            Player player = findPlayerInfoById(followerId);
            map.put("player", player);
            map.put("isFollower", true);
            followerMapList.add(map);
        }
        return followerMapList;
    }

    @Override
    public List<Map<String, Object>> findFansByPlayerId(Long playerId, Integer pageNo, Integer pageSize) {
        List<Map<String, Object>> fansMapList = new ArrayList<>();
        List<Long> fansIdAll = getFansByPlayerId(playerId);
        List<Long> pagefansIdList = pageList(fansIdAll, pageNo, pageSize);
        for (Long fansId : pagefansIdList) {
            Map<String, Object> map = new HashMap<>();
            Player player = findPlayerInfoById(fansId);
            Boolean isFollower = isFollower(playerId, fansId);
            map.put("player", player);
            map.put("isFollower", isFollower);
            fansMapList.add(map);
        }
        return fansMapList;
    }

    @Override
    public List<Map<String, Object>> findFansByPlayerId(Long playerId) {
        List<Map<String, Object>> fansMapList = new ArrayList<>();
        List<Long> fansIdAll = getFansByPlayerId(playerId);
        for (Long fansId : fansIdAll) {
            Map<String, Object> map = new HashMap<>();
            Player player = findPlayerInfoById(fansId);
            Boolean isFollower = isFollower(playerId, fansId);
            map.put("player", player);
            map.put("isFollower", isFollower);
            fansMapList.add(map);
        }
        return fansMapList;
    }

    @Override
    public List<Player> findBlackListByPlayerId(Long playerId) {
        List<Player> blackList = new ArrayList<>();
        List<Long> blackIdList = getBlackListByPlayerId(playerId);
        for (Long blackId : blackIdList) {
            Player player = findPlayerInfoById(blackId);
            blackList.add(player);
        }
        return blackList;
    }

    @Override
    public List<Player> findBlackListByPlayerId(Long playerId, Integer pageNo, Integer pageSize) {
        List<Player> blackList = new ArrayList<>();
        List<Long> blackIdAll = getBlackListByPlayerId(playerId);
        List<Long> pageBlackIdList = pageList(blackIdAll, pageNo, pageSize);
        for (Long blackId : pageBlackIdList) {
            Player player = findPlayerInfoById(blackId);
            blackList.add(player);
        }
        return blackList;
    }

    @Override
    public Map<String, Object> getPortrait(Long playerId, Long currentPlayerId) {
        //info
        Player player = findPlayerInfoById(playerId);

        Boolean isFollower = isFollower(currentPlayerId, playerId);
        //fans
        int fans = getFansByPlayerId(playerId).size();
        //followers
        int followers = getFollowersByPlayerId(playerId).size();
        //point
//        Long point = playerBankService.getPlayerPointById(playerId);
        Long point = playerBankService.getPlayersFlowersById(playerId);
        // coin used
        Long send = playerBankService.getPlayerSendById(playerId);


        Map<String, Object> detail = new HashMap<>();
        detail.put("player", player);
        detail.put("isFollower", isFollower);
        detail.put("fans", fans);
        detail.put("followers", followers);
        detail.put("point", point);
        detail.put("send", send);
        return detail;
    }

    @Override
    public Map<String, Object> getProfileDetail(Long playerId, Long currentPlayerId) {
        //info
        Player player = findPlayerInfoById(playerId);

        Boolean isFollower = isFollower(currentPlayerId, playerId);

        Boolean isBlack = isBlack(currentPlayerId, playerId);
        //fans
        int fans = getFansByPlayerId(playerId).size();
        //followers
        int followers = getFollowersByPlayerId(playerId).size();
        // gift top
        List<Player> giftTop = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<String>> dataTuple = redisDao.zRevRangeWithScore(RedisKey.GIFT_RANK + playerId, 0, 2);
        for (ZSetOperations.TypedTuple<String> tuple : dataTuple) {
            if (tuple.getValue() != null && tuple.getValue().length() > 0) {
                Player p = findPlayerInfoById(Long.valueOf(tuple.getValue()));
                giftTop.add(p);
            }
        }

        Map<String, Object> detail = new HashMap<>();
        detail.put("player", player);
        detail.put("isFollower", isFollower);
        detail.put("isBlack", isBlack);
        detail.put("fans", fans);
        detail.put("followers", followers);
        detail.put("giftTop", giftTop);
        return detail;
    }

    @Override
    public List<Long> pageList(List<Long> list, Integer pageNo, Integer pageSize) {
        List<Long> pagingList = new ArrayList<>();
        int start = (pageNo - 1) * pageSize;
        int end = pageNo * pageSize;
        int length = list.size();
        if (start > length) {
            return pagingList;
        } else if (start <= length && end <= length) {
            return list.subList(start, end);
        } else if (start <= length && end > length) {
            return list.subList(start, length);
        }
        return pagingList;
    }

    @Override
    public List<Map<String, Object>> findFollowerByPlayerId(Long playerId, Long currentPlayerId, Integer pageNo, Integer pageSize) {
        List<Map<String, Object>> followerMapList = new ArrayList<>();
        List<Long> followerIdList = getFollowersByPlayerId(playerId);
        List<Long> pageFollowerIdList = pageList(followerIdList, pageNo, pageSize);
        for (Long followerId : pageFollowerIdList) {
            Map<String, Object> map = new HashMap<>();
            Player player = findPlayerInfoById(followerId); //我关注的人
            Boolean isFollower = isFollower(currentPlayerId, followerId); //我是否关注了，默人的关注对象；
            map.put("player", player);
            map.put("isFollower", isFollower);
            followerMapList.add(map);
        }
        return followerMapList;
    }

    @Override
    public List<Map<String, Object>> findFollowerByPlayerId(Long playerId, Long currentPlayerId) {
        List<Map<String, Object>> followerMapList = new ArrayList<>();
        List<Long> followerIdList = getFollowersByPlayerId(playerId);
        for (Long followerId : followerIdList) {
            Map<String, Object> map = new HashMap<>();
            Player player = findPlayerInfoById(followerId);
            Boolean isFollower = isFollower(currentPlayerId, followerId);
            map.put("player", player);
            map.put("isFollower", isFollower);
            followerMapList.add(map);
        }
        return followerMapList;
    }

    @Override
    public List<Map<String, Object>> findFansByPlayerId(Long playerId, Long currentPlayerId, Integer pageNo, Integer pageSize) {
        List<Map<String, Object>> fansMapList = new ArrayList<>();
        List<Long> fansIdList = getFansByPlayerId(playerId);
        List<Long> pagefansIdList = pageList(fansIdList, pageNo, pageSize);
        for (Long fansId : pagefansIdList) {
            Map<String, Object> map = new HashMap<>();
            Player player = findPlayerInfoById(fansId);
            Boolean isFollower = isFollower(currentPlayerId, fansId);
            map.put("player", player);
            map.put("isFollower", isFollower);
            fansMapList.add(map);
        }
        return fansMapList;
    }

    @Override
    public List<Map<String, Object>> findFansByPlayerId(Long playerId, Long currentPlayerId) {
        List<Map<String, Object>> fansMapList = new ArrayList<>();
        List<Long> fansIdList = getFansByPlayerId(playerId);
        for (Long fansId : fansIdList) {
            Map<String, Object> map = new HashMap<>();
            Player player = findPlayerInfoById(fansId);
            Boolean isFollower = isFollower(currentPlayerId, fansId);
            map.put("player", player);
            map.put("isFollower", isFollower);
            fansMapList.add(map);
        }
        return fansMapList;
    }

    @Override
    public List<Map<String, Object>> talentRank(Long playerId, Integer pageNo, Integer pageSize) {
        List<Long> blacklist = new ArrayList<>();
        List<Map<String, Object>> list = new ArrayList<>();
        int start = (pageNo - 1) * pageSize;
        int end = pageNo * pageSize - 1;
        List<Player> blackPlayer = findBlackListByPlayerId(playerId);
        if(blackPlayer != null){
            for(Player player : blackPlayer){
                blacklist.add(player.getId());
            }
        }
        Set<String> dataStr = redisDao.zRevRange(RedisKey.TALENT_RANK, start, end);
        for (String playId : dataStr) {
            Map<String, Object> map = new HashMap<>();
            Player player = findPlayerInfoById(Long.valueOf(playId));
            if (player != null) {
                Long id = player.getId();
                if(!blacklist.contains(id)) {
                    Boolean isFollower = isFollower(playerId, id);
                    map.put("player", player);
                    map.put("isFollower", isFollower);
                    list.add(map);
                }
            }
        }
        return list;
    }


    private List<Long> getFansByPlayerId(Long playerId) {
        List<Long> fansArr = new ArrayList<>();
        boolean isExists = redisDao.hexists(RedisKey.PLAYER + playerId, RedisKey.PLAYER_FANS);
        if (isExists) {
            String fansIdStr = redisDao.hget(RedisKey.PLAYER + playerId, RedisKey.PLAYER_FANS);
            try {
                if (fansIdStr != null && fansIdStr.length() > 0) {
                    fansArr = JSON.parseArray(fansIdStr, Long.TYPE);
                }
            } catch (Exception e) {
                logger.error("error occurred:" + e);
                e.printStackTrace();
            }
        } else {
            SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
            List<Long> list = playerDao.getFansIdById(sqlSession, playerId);
            sqlSession.close();
            if (list != null) {
                fansArr = list;
            }
            redisDao.hset(RedisKey.PLAYER + playerId, RedisKey.PLAYER_FANS, JSON.toJSONString(fansArr));

        }
        return fansArr;
    }

    private List<Long> getFollowersByPlayerId(Long playerId) {
        List<Long> followersArr = new ArrayList<>();
        boolean isExists = redisDao.hexists(RedisKey.PLAYER + playerId, RedisKey.PLAYER_FOLLOWS);
        if (isExists) {
            String followersIdStr = redisDao.hget(RedisKey.PLAYER + playerId, RedisKey.PLAYER_FOLLOWS);
            try {
                if (followersIdStr != null && followersIdStr.length() > 0) {
                    followersArr = JSON.parseArray(followersIdStr, Long.TYPE);
                }
            } catch (Exception e) {
                logger.error("error occurred:" + e);
                e.printStackTrace();
            }
        } else {
            SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
            List<Long> list = playerDao.getFollowersIdById(sqlSession, playerId);
            sqlSession.close();
            if (list != null) {
                followersArr = list;
            }
            redisDao.hset(RedisKey.PLAYER + playerId, RedisKey.PLAYER_FOLLOWS, JSON.toJSONString(followersArr));
        }
        return followersArr;
    }

    private List<Long> getBlackListByPlayerId(Long playerId) {
        List<Long> blackArr = new ArrayList<>();
        boolean isExists = redisDao.hexists(RedisKey.PLAYER + playerId, RedisKey.PLAYER_BLACKLIST);
        if (isExists) {
            String blackIdStr = redisDao.hget(RedisKey.PLAYER + playerId, RedisKey.PLAYER_BLACKLIST);
            try {
                if (blackIdStr != null && blackIdStr.length() > 0) {
                    blackArr = JSON.parseArray(blackIdStr, Long.TYPE);
                }
            } catch (Exception e) {
                logger.error("error occurred:" + e);
                e.printStackTrace();
            }
        } else {
            SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
            List<Long> list = playerDao.getBlackListById(sqlSession, playerId);
            sqlSession.close();
            if (list != null) {
                blackArr = list;
            }
            redisDao.hset(RedisKey.PLAYER + playerId, RedisKey.PLAYER_BLACKLIST, JSON.toJSONString(blackArr));
        }
        return blackArr;
    }

    private void reInstallPlayerData(Long playerId) {
        long point = playerBankService.getPlayerPointById(playerId);
        long coin = playerBankService.getPlayerCoinById(playerId);
        long send = playerBankService.getPlayerSendById(playerId);
        List<Long> fans = getFansByPlayerId(playerId);
        List<Long> follows = getFollowersByPlayerId(playerId);
        List<Long> blacklist = getBlackListByPlayerId(playerId);

        String fansStr = fans == null ? JSON.toJSONString(new ArrayList<Long>()) : JSON.toJSONString(fans);
        String followsStr = follows == null ? JSON.toJSONString(new ArrayList<Long>()) : JSON.toJSONString(follows);
        String blacklistStr = blacklist == null ? JSON.toJSONString(new ArrayList<Long>()) : JSON.toJSONString(blacklist);

        //cache reset
        redisDao.hset(RedisKey.PLAYER + playerId, RedisKey.PLAYER_FANS, fansStr);
        redisDao.hset(RedisKey.PLAYER + playerId, RedisKey.PLAYER_FOLLOWS, followsStr);
        redisDao.hset(RedisKey.PLAYER + playerId, RedisKey.PLAYER_BLACKLIST, blacklistStr);
        redisDao.hset(RedisKey.PLAYER + playerId, RedisKey.PLAYER_POINT, String.valueOf(point));
        redisDao.hset(RedisKey.PLAYER + playerId, RedisKey.PLAYER_SEND, String.valueOf(send));
        redisDao.hset(RedisKey.PLAYER + playerId, RedisKey.PLAYER_COIN, String.valueOf(coin));
    }

    private void updatePlayerFansRank(Long playerId) {
        List<Long> fansNum = getFansByPlayerId(playerId);
        if (fansNum != null && !fansNum.isEmpty()){
            redisDao.zAdd(RedisKey.TALENT_RANK, playerId.toString(), fansNum.size());
        } else {
            redisDao.zAdd(RedisKey.TALENT_RANK, playerId.toString(), 0);
        }

    }

    private void updatePlayerFollowerRank(Long playerId, boolean isFollow, Long followerId) {
        String roomId = redisDao.hget(RedisKey.ROOM_ONLINE, String.valueOf(followerId));
        logger.info("roomId = " + roomId);
        if (roomId != null) {
            String key = RedisKey.FOLLOWER_RANK + playerId;
            logger.info("follower_rank_key = " + key + ", playerId = " + playerId);
            if (isFollow) {
                redisDao.zAdd(key, roomId, System.currentTimeMillis());
            } else {
                redisDao.zRemove(key, roomId);
            }
        }
    }
}

