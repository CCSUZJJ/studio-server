package com.mob.studio.test;

import com.mob.studio.redis.RedisDao;
import com.mob.studio.util.RedisKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jianwl
 * Date: 2016/3/22
 * Time: 18:14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
public class RedisTest {
    @Autowired
    private RedisDao redisDao;

    @Test
    public void zGiftRankAdd(){
        String mapName = RedisKey.GIFT_RANK + "7";
        String value = "15"; //playerId
        Integer score = 30200; //获得礼物的数量；
        redisDao.zAdd(mapName,value,score);
    }

    @Test
    public void hSetFans(){
        String mapName = RedisKey.PLAYER + 12;
        List<String> list = new ArrayList<>();
        list.add("");
//        list.add("10");
//        list.add("12");

        redisDao.hset(mapName, RedisKey.PLAYER_FANS, list.toString());
    }

    @Test
    public void hSetPlayerFollows(){
        String mapName = RedisKey.PLAYER + 12;
        List<String> list = new ArrayList<>();
        list.add("8");
//        list.add("12");

//        list.add("9");
        redisDao.hset(mapName, RedisKey.PLAYER_FOLLOWS, list.toString());
    }

    @Test
    public void hSetPlayerBackList(){
        String mapName = RedisKey.PLAYER + 9;
        List<String> list = new ArrayList<>();
        list.add("");
        redisDao.hset(mapName, RedisKey.PLAYER_BLACKLIST,list.toString());
    }

    @Test
    public void hSetCoin(){
        String mapName = RedisKey.PLAYER + 12;
        redisDao.hset(mapName,RedisKey.PLAYER_COIN,"1000");
    }

    @Test
    public void hSetGifRank(){
        String mapName = RedisKey.PLAYER + 12;
        List<String> list = new ArrayList<>();
        list.add("8");
        list.add("9");
        list.add("7");

        redisDao.hset(mapName,RedisKey.GIFT_RANK,list.toString());
    }

    @Test
    public void hSetSend(){
        String mapName = RedisKey.PLAYER +12;

        redisDao.hset(mapName,RedisKey.PLAYER_SEND,"500");
    }

    @Test
    public void hSetPoint(){
        String mapName = RedisKey.PLAYER + 4;

        redisDao.hset(mapName,RedisKey.PLAYER_POINT,"500");
    }

    @Test
    public void zHotRankSet(){
        String mapName = RedisKey.HOT_RANK;
        String value = "8"; //playerId
        Integer score = 40200; //获得礼物的数量；
        redisDao.zAdd(mapName, value, score);
    }

    @Test
    public void hSetViewer(){
        String mapName = RedisKey.ROOM + 4;

        redisDao.hset(mapName, RedisKey.ROOM_VIEWERS, "500");
    }
}
