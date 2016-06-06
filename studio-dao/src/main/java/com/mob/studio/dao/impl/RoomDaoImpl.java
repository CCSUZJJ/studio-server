package com.mob.studio.dao.impl;

import com.alibaba.fastjson.JSON;
import com.mob.studio.dao.RoomDao;
import com.mob.studio.domain.Player;
import com.mob.studio.domain.Room;
import com.mob.studio.redis.RedisDao;
import com.mob.studio.util.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author: Zhang.Min
 * @since: 2016/3/21
 * @version: 1.7
 */
public class RoomDaoImpl implements RoomDao {

    @Autowired
    @Qualifier("redisDao")
    private RedisDao redisDao;


    @Override
    public Room getRoomById(Long id) {
        Boolean isExists = redisDao.exists(RedisKey.ROOM + id);
        if (isExists) {
            Room room = new Room();
            String playerStr = redisDao.hget(RedisKey.ROOM + id, RedisKey.ROOM_PLAYER);
            room.setPlayer(JSON.parseObject(playerStr, Player.class));
            String title = redisDao.hget(RedisKey.ROOM + id, RedisKey.ROOM_TITLE);
            room.setTitle(title);
            String viewers = redisDao.hget(RedisKey.ROOM + id, RedisKey.ROOM_VIEWERS);
            room.setViewers(Long.valueOf(viewers));
            room.setId(id);
            return room;
        }
        return null;
    }
}
