package com.mob.studio.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jianwl
 * Date: 2016/3/23
 * Time: 11:28
 */
public class ListTest {
    @Test
    public void listTest(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        System.out.println(JSON.toJSONString(list.subList(1,2)));
    }
}
