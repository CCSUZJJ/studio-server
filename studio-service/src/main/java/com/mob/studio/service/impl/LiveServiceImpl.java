package com.mob.studio.service.impl;

import com.alibaba.fastjson.JSON;
import com.mob.studio.dao.PlayerDao;
import com.mob.studio.dao.RoomDao;
import com.mob.studio.domain.Player;
import com.mob.studio.domain.Room;
import com.mob.studio.redis.RedisDao;
import com.mob.studio.service.LiveService;
import com.mob.studio.service.PlayerService;
import com.mob.studio.util.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author: Zhang.Min
 * @since: 2016/3/17
 * @version: 1.7
 */
public class LiveServiceImpl implements LiveService {


    @Autowired
    @Qualifier("redisDao")
    private RedisDao redisDao;

    @Autowired
    @Qualifier("roomDao")
    private RoomDao roomDao;

    @Autowired
    @Qualifier("playerService")
    private PlayerService playerService;

    @Autowired
    @Qualifier("playerDao")
    private PlayerDao playerDao;

    @Override
    public Integer openRoom(Long playerId, String title) {
        Random random = new Random();
        int roomId = random.nextInt(1000000);
        //检查这个房间号是否已被占用,已占用重试一个新号
        while (redisDao.exists(RedisKey.ROOM + roomId)){
            roomId = random.nextInt(1000000);
        }
        Player player = playerService.findPlayerInfoById(playerId);
        redisDao.hset(RedisKey.ROOM + roomId, RedisKey.ROOM_PLAYER, JSON.toJSONString(player));
        redisDao.hset(RedisKey.ROOM + roomId, RedisKey.ROOM_TITLE, title);
        redisDao.hset(RedisKey.ROOM + roomId, RedisKey.ROOM_VIEWERS, "0");
        redisDao.hset(RedisKey.ROOM_ONLINE , playerId.toString(), "" + roomId);
        //初始化操作由socket来完成
//        //热榜数据初始化
//        redisDao.zAdd(RedisKey.HOT_RANK, "" + roomId, 0);
//        //关注榜数据初始化
//        List<Map<String,Object>> fansMapList = playerService.findFansByPlayerId(playerId);
//        if (fansMapList!=null) {
//            for (Map m : fansMapList) {
//                redisDao.zAdd(RedisKey.FOLLOWER_RANK + ((Player)m.get("player")).getId(), "" + roomId, System.currentTimeMillis());
//            }
//        }
        return roomId;
    }

    @Override
    public List<Room> hotRank(Integer pageNo, Integer pageSize) {
        // pageNo: 2, pageSize:10
        // start: 10, end:19
        List<Room> list = new ArrayList<>();
        int start = (pageNo - 1 ) * pageSize;
        int end = pageNo * pageSize -1;
        Set<String> dataStr = redisDao.zRevRange(RedisKey.HOT_RANK, start, end);
        for (String roomIdStr : dataStr) {
            // pkg moudle to list
            Room room = roomDao.getRoomById(Long.valueOf(roomIdStr));
            if (room != null) {
                list.add(room);
            }
        }
        return list;
    }

    @Override
    public List<Room> hotRank(Long playerId, Integer pageNo, Integer pageSize) {
        // pageNo: 2, pageSize:10
        // start: 10, end:19
        List<Room> list = new ArrayList<>();
        List<Long> blacklist = new ArrayList<>();
        int start = (pageNo - 1 ) * pageSize;
        int end = pageNo * pageSize -1;
        List<Player> blackPlayer = playerService.findBlackListByPlayerId(playerId);
        if(blackPlayer != null){
            for(Player p : blackPlayer){
                blacklist.add(p.getId());
            }
        }
        Set<String> dataStr = redisDao.zRevRange(RedisKey.HOT_RANK, start, end);
        for (String roomIdStr : dataStr) {
            // pkg moudle to list
            Room room = roomDao.getRoomById(Long.valueOf(roomIdStr));
            if (room != null) {
                Long id = room.getPlayer().getId();
                if(!blacklist.contains(id)) {
                    list.add(room);
                }
            }
        }
        return list;
    }

    @Override
    public List<Room> followerLive(Long playerId, Integer pageNo, Integer pageSize) {
        List<Room> list = new ArrayList<>();
        int start = (pageNo - 1) * pageSize;
        int end = pageNo * pageSize - 1;
        Set<String> dataStr = redisDao.zRevRange(RedisKey.FOLLOWER_RANK + playerId, start, end);
        for (String roomIdStr : dataStr) {
            // pkg module to list
            Room room = roomDao.getRoomById(Long.valueOf(roomIdStr));
            if (room != null) {
                list.add(room);
            }
        }
        return list;
    }

    @Override
    public Room findOnlineRoomByPlayerId(Long playerId) {
        if (redisDao.hexists(RedisKey.ROOM_ONLINE, playerId.toString())){
            String roomIdStr = redisDao.hget(RedisKey.ROOM_ONLINE, playerId.toString());
            if (roomIdStr != null){
                Room room = roomDao.getRoomById(Long.valueOf(roomIdStr));
                return room;
            }
        }
        return null;
    }
}
