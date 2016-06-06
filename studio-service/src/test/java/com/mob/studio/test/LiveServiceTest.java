package com.mob.studio.test;

import com.alibaba.fastjson.JSON;
import com.mob.studio.domain.Room;
import com.mob.studio.service.LiveService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * User: jianwl
 * Date: 2016/3/22
 * Time: 18:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
public class LiveServiceTest {
    @Autowired
    private LiveService liveService;

    @Test
    public void openRoomTest(){
        Long playId = 8L;
        String title = "东北大叔002";
        Integer roomId = liveService.openRoom(playId, title);
        System.out.println("ROOMId = " + roomId);
    }

    @Test
    public void hotRankTest(){
        List<Room> list = liveService.hotRank(1,10);
        for(Room room : list){
            System.out.println(JSON.toJSONString(room));
        }
    }
}
