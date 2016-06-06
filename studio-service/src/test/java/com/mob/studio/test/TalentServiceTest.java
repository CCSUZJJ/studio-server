package com.mob.studio.test;

import com.mob.studio.service.PlayerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * User: jianwl
 * Date: 2016/3/22
 * Time: 18:07
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
public class TalentServiceTest {
    @Autowired
    private PlayerService playerService;

    @Test
    public void talentRank(){
        List<Map<String,Object>> players = playerService.talentRank(1L, 1, 10);
        for(Map map : players){
            System.out.println(map);
        }
    }
}
