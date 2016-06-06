package com.mob.studio.test;

import com.alibaba.fastjson.JSON;
import com.mob.studio.service.SearchService;
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
 * Time: 18:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
public class SearchServiceTest {
    @Autowired
    private SearchService searchService;

    @Test
    public void searchTest(){
        List<Map<String,Object>> list = searchService.search(1L, "林", 1, 10);
        for(Map map: list){
//			System.out.println(player.getId() + " " + player.getNick());
            System.out.println(JSON.toJSONString(map));
        }
    }
}
