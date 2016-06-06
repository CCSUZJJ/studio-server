package com.mob.studio.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.List;

/**
 * @author: Zhang.Min
 * @since: 2016/3/21
 * @version: 1.7
 */
public class JsonTest {
    @Test
    public void ArrayTest(){
        String ids = "[12000001,12000003,12000005]";
        List<Long> idList = JSON.parseArray(ids, Long.TYPE);
        for (Long id : idList){
            System.out.println("id:" + id);
        }
    }
}
