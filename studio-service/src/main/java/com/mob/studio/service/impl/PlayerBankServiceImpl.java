package com.mob.studio.service.impl;

import com.alibaba.fastjson.JSON;
import com.mob.studio.dao.ItemDao;
import com.mob.studio.dao.PlayerBankDao;
import com.mob.studio.domain.Item;
import com.mob.studio.domain.Player;
import com.mob.studio.domain.PlayerBank;
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
 * @since: 2016/3/22
 * @version: 1.7
 */
public class PlayerBankServiceImpl implements PlayerBankService {
    private static final Logger logger = Logger.getLogger(PlayerBankServiceImpl.class);
    private static final SqlSessionFactory sqlSessionFactory_Studio_W = MybatisUtil.getSqlSessionFactory_Studio_W();

    @Autowired
    @Qualifier("redisDao")
    private RedisDao redisDao;

    @Autowired
    @Qualifier("playerBankDao")
    private PlayerBankDao playerBankDao;

    @Autowired
    @Qualifier("playerService")
    private PlayerService playerService;

    @Autowired
    @Qualifier("itemDao")
    private ItemDao itemDao;


    @Override
    public void initBank(Long id) {
        PlayerBank playerBank = new PlayerBank(id);
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        playerBankDao.insertOrUpdatePlayerCoin(sqlSession, playerBank);
        playerBankDao.insertOrUpdatePlayerPoint(sqlSession, playerBank);
        playerBankDao.insertOrUpdatePlayerBankStatistic(sqlSession, playerBank);
        sqlSession.commit();
        sqlSession.close();
        redisDao.hset(RedisKey.PLAYER + id, RedisKey.PLAYER_POINT, playerBank.getPoint().toString());
        redisDao.hset(RedisKey.PLAYER + id, RedisKey.PLAYER_SEND, playerBank.getCoinOut().toString());
        redisDao.hset(RedisKey.PLAYER + id, RedisKey.PLAYER_COIN, playerBank.getCoin().toString());
    }

    @Override
    public Long getPlayerPointById(Long id) {
        Long point = 0L;
        boolean isExist = redisDao.hexists(RedisKey.PLAYER + id, RedisKey.PLAYER_POINT);
        if (isExist) {
            String pointStr = redisDao.hget(RedisKey.PLAYER + id, RedisKey.PLAYER_POINT);
            if (pointStr != null && pointStr.length() > 0) {
                point = JSON.parseObject(pointStr, Long.TYPE);
                return point;
            }
        }
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        point = playerBankDao.getPlayerPointById(sqlSession, id);
        sqlSession.close();

        if (point == null) {
            point = 0L;
            logger.warn("[DB data not found]\t[player_point]\tplayer_id:" + id);
        }
        redisDao.hset(RedisKey.PLAYER + id, RedisKey.PLAYER_POINT, point.toString());
        return point;
    }

    @Override
    public Long getPlayerCoinById(Long id) {
        Long coin = 0L;
        boolean isExist = redisDao.hexists(RedisKey.PLAYER + id, RedisKey.PLAYER_COIN);
        if (isExist) {
            String coinStr = redisDao.hget(RedisKey.PLAYER + id, RedisKey.PLAYER_COIN);
            if (coinStr != null && coinStr.length() > 0) {
                coin = JSON.parseObject(coinStr, Long.TYPE);
                return coin;
            }
        }
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        coin = playerBankDao.getPlayerCoinById(sqlSession, id);
        sqlSession.close();
        if (coin == null) {
            coin = 0L;
            logger.warn("[DB data not found]\t[player_coin]\tplayer_id:" + id);
        }
        redisDao.hset(RedisKey.PLAYER + id, RedisKey.PLAYER_COIN, coin.toString());
        return coin;
    }

    @Override
    public Long getPlayerSendById(Long id) {
        Long send = 0L;
        boolean isExist = redisDao.hexists(RedisKey.PLAYER + id, RedisKey.PLAYER_SEND);
        if (isExist) {
            String sendStr = redisDao.hget(RedisKey.PLAYER + id, RedisKey.PLAYER_SEND);
            if (sendStr != null && sendStr.length() > 0) {
                send = JSON.parseObject(sendStr, Long.TYPE);
                return send;
            }
        }
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        send = playerBankDao.getPlayerCoinOutById(sqlSession, id);
        sqlSession.close();
        if (send == null) {
            send = 0L;
            logger.warn("[DB data not found]\t[player_bank_statistic]\tplayer_id:" + id);
        }
        redisDao.hset(RedisKey.PLAYER + id, RedisKey.PLAYER_SEND, send.toString());
        return send;
    }

    @Override
    public Long getPlayersFlowersById(Long id) {
        Long coin = getPlayerCoinById(id);
        Long point = getPlayerPointById(id);
        Long flowers = coin + point - (new PlayerBank(id)).getPoint();
        return flowers;
    }

    @Override
    public List<Map<String, Object>> findGiftRankByPlayerId(Long playerId, Integer pageNo, Integer pageSize) {
        List<Map<String, Object>> giftRank = new ArrayList<>();
        int start = (pageNo - 1) * pageSize;
        int end = pageNo * pageSize - 1;
        Set<ZSetOperations.TypedTuple<String>> dataTuple = redisDao.zRevRangeWithScore(RedisKey.GIFT_RANK + playerId, start, end);
        for (ZSetOperations.TypedTuple<String> tuple : dataTuple) {
            if (tuple.getValue() != null && tuple.getValue().length() > 0) {
                Map<String, Object> map = new HashMap<>();
                Player p = playerService.findPlayerInfoById(Long.valueOf(tuple.getValue()));
                map.put("player", p);
                map.put("point", tuple.getScore().longValue());
                giftRank.add(map);
            }
        }
        return giftRank;
    }

    @Override
    public void insertOrUpdatePlayerCoin(PlayerBank playerBank) {
        //update db
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        playerBankDao.insertOrUpdatePlayerCoin(sqlSession, playerBank);
        sqlSession.commit();
        sqlSession.close();
        //update cache
        redisDao.hset(RedisKey.PLAYER + playerBank.getPlayerId(), RedisKey.PLAYER_COIN, playerBank.getCoin().toString());
    }

    @Override
    public void insertOrUpdatePlayerPoint(PlayerBank playerBank) {
        //update db
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        playerBankDao.insertOrUpdatePlayerPoint(sqlSession, playerBank);
        sqlSession.commit();
        sqlSession.close();
        //update cache
        redisDao.hset(RedisKey.PLAYER + playerBank.getPlayerId(), RedisKey.PLAYER_POINT, playerBank.getPoint().toString());
    }

    @Override
    public void insertOrUpdatePlayerStatistic(PlayerBank playerBank) {
        //update db
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        playerBankDao.insertOrUpdatePlayerBankStatistic(sqlSession, playerBank);
        sqlSession.commit();
        sqlSession.close();
        //update cache
        redisDao.hset(RedisKey.PLAYER + playerBank.getPlayerId(), RedisKey.PLAYER_SEND, playerBank.getCoinOut().toString());
    }

    @Override
    public void useItem(Long playerId, Long targetId, Item item, Long quantity) {
        //player coin-; target point+; update player_bank_statistic(player,target),insert giftRank
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        Long priceAll = item.getPrice() * quantity;
        Long pointAll = item.getPoint() * quantity;
        PlayerBank playerBank = getData(playerId);
        Long coin_new = playerBank.getCoin() - priceAll;
        playerBank.setCoin(coin_new);
        playerBankDao.insertOrUpdatePlayerCoin(sqlSession, playerBank);
        Long coin_out_new = playerBank.getCoinOut() + priceAll;
        playerBank.setCoinOut(coin_out_new);
        playerBankDao.insertOrUpdatePlayerBankStatistic(sqlSession, playerBank);

        PlayerBank targetBank = getData(targetId);
        Long point_new = targetBank.getPoint() + pointAll;
        targetBank.setPoint(point_new);
        playerBankDao.insertOrUpdatePlayerPoint(sqlSession, targetBank);
        Long point_in_new = targetBank.getPointIn() + pointAll;
        targetBank.setPointIn(point_in_new);
        playerBankDao.insertOrUpdatePlayerBankStatistic(sqlSession, targetBank);
        sqlSession.commit();
        sqlSession.close();


        //cache player coin,send update
        redisDao.hset(RedisKey.PLAYER + playerBank.getPlayerId(), RedisKey.PLAYER_COIN, playerBank.getCoin().toString());
        redisDao.hset(RedisKey.PLAYER + playerBank.getPlayerId(), RedisKey.PLAYER_SEND, playerBank.getCoinOut().toString());
        //cache target point update
        redisDao.hset(RedisKey.PLAYER + targetBank.getPlayerId(), RedisKey.PLAYER_POINT, targetBank.getPoint().toString());
        //cache giftRank update
        Boolean isExists = redisDao.exists(RedisKey.GIFT_RANK + targetId);
        if (isExists) {
            Number score = redisDao.zScore(RedisKey.GIFT_RANK + targetId, playerId.toString());
            if (score != null) {
                Long scoreL = score.longValue() + pointAll;
                redisDao.zAdd(RedisKey.GIFT_RANK + targetId, playerId.toString(), scoreL);
            } else {
                //新送礼人第一条记录
                redisDao.zAdd(RedisKey.GIFT_RANK + targetId, playerId.toString(), pointAll);
            }
        } else {
            //收礼人第一条记录
            redisDao.zAdd(RedisKey.GIFT_RANK + targetId, playerId.toString(), pointAll);
        }


    }


    private PlayerBank getData(Long playerId) {
        PlayerBank playerBank = new PlayerBank();
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        playerBank = playerBankDao.getPlayerBankStatisticById(sqlSession, playerId);
        Long playerCoin = playerBankDao.getPlayerCoinById(sqlSession, playerId);
        playerBank.setCoin(playerCoin);
        Long point = playerBankDao.getPlayerPointById(sqlSession, playerId);
        playerBank.setPoint(point);
        sqlSession.commit();
        sqlSession.close();
        return playerBank;
    }
}
