package com.mob.studio.test;

import com.mob.studio.service.PlayerBankService;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * User: jianwl
 * Date: 2016/3/22
 * Time: 18:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
public class PlayerBankServiceTest {
    private static final Logger logger = Logger.getLogger(PlayerBankServiceTest.class);

    @Autowired
    private PlayerBankService playerBankService;

    /**
     * PlayerMapper.xml 有问题,parameterMap 该为parameterType
     */

//    public void findGiftRankByPlayerIdTest(){
//        Long playerId = 7L;
//        Integer pageNo = 1;
//        Integer pageSize = 10;
//        List<Player> list = playerBankService.findGiftRankByPlayerId(playerId,pageNo,pageSize);
//        for(Player player : list){
//            System.out.println(JSON.toJSONString(player));
//        }
//    }


}
