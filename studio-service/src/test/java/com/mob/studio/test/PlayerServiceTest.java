package com.mob.studio.test;

import com.alibaba.fastjson.JSON;
import com.mob.studio.domain.Player;
import com.mob.studio.service.LiveService;
import com.mob.studio.service.PlayerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: jianwl
 * Date: 2016/3/22
 * Time: 18:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
public class PlayerServiceTest {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private LiveService liveService;

    @Test
    public void loginTest(){
        Player p = new Player("琅琊榜01", "南宁", 0, "http://CDN.img.youzu.com/zhibo_145586400254837_18182315.jpg","open_3453445439933",1);
        Player player = playerService.login(p);
    }

    @Test
    public void registTest(){
        Player player = new Player("暗黑时代01","北京",0,"http://CDN.img.youzu.com/zhibo_1455864202837_18182315.jpg","open_123334562278910",1);
        playerService.regist(player);
    }

    @Test
    public void getPlayerByAliasId(){
        Player player = playerService.getPlayerByAliasId("open_123456789");
        System.out.println(JSON.toJSONString(player));
    }


    @Test
    public void findPlayerByFuzzyNickTest(){
        String nick = "";
        Integer pageNo = 1;
        Integer pageSize = 10;
        List<Player> list = playerService.findPlayerByFuzzyNick(nick, pageNo, pageSize);
        for(Player player : list){
            System.out.println(JSON.toJSONString(player));
        }
    }

    @Test
    public void findPlayerInfoById(){
        Player player = playerService.findPlayerInfoById(7L);
        System.out.println("player = " + JSON.toJSONString(player));
    }

    @Test
    public void followerLive(){
        liveService.followerLive(7L, 1, 10);
    }

    @Test
    public void doFollowTest(){
        Long playerId = 7L;
        Long followId = 12L;
        playerService.doFollow(playerId, followId);
    }

    @Test
    public void doBlack(){
        Long playerId = 7L;
        Long backId = 12L;
        playerService.doBlack(playerId, backId);
    }

    @Test
    public void doUnfollow(){
        Long playerId = 7L;
        Long followId = 12L;
        playerService.doUnfollow(playerId, followId);
    }

    @Test
    public void doUnblack(){
        Long playerId = 7L;
        Long backId = 12L;
        playerService.doUnblack(playerId, backId);
    }

    /**
     * 原来的方法是错的
     *  ids.constain();
     */
    @Test
    public void isFollower(){
        Long playId = 7L;
        Long targetId = 12L;
        boolean exist = playerService.isFollower(playId, targetId);
        System.out.println(exist);
    }

    @Test
    public void getMyProfileDetail(){
        Long playerId = 7L;
        Map map = playerService.getMyProfileDetail(playerId);
        System.out.println(JSON.toJSONString(map));
    }

    @Test
    public void findFollowerByPlayerId(){
        Long playerId = 7L;
        Integer pageNo = 1;
        Integer pageSize = 10;
        List<Map<String,Object>> list = playerService.findFollowerByPlayerId(playerId,pageNo,pageSize);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void findFollowerByPlayerId2(){
        Long playerId = 7L;
        Long currentPlayerId = 12L;
        Integer pageNo = 1;
        Integer pageSize = 10;
        List<Map<String,Object>> list = playerService.findFollowerByPlayerId(playerId,currentPlayerId,pageNo,pageSize);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void findFansByPlayerId(){
        Long playerId = 7L;
        Integer pageNo = 1;
        Integer pageSize = 10;
        List<Map<String,Object>> list = playerService.findFansByPlayerId(playerId,pageNo,pageSize);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void findFansByPlayerId2(){
        Long playerId = 7L;
        Long currentPlayerId = 12L;
        Integer pageNo = 1;
        Integer pageSize = 10;
        List<Map<String,Object>> list = playerService.findFansByPlayerId(playerId,currentPlayerId,pageNo,pageSize);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void findBlackListByPlayerId(){
        Long playerId = 7L;
        List<Player> list = playerService.findBlackListByPlayerId(playerId);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void getPortrait(){
        Long playerId = 12L;
        Long currentPlayerId = 7L;
        Map map = playerService.getPortrait(playerId, currentPlayerId);
        System.out.println(JSON.toJSONString(map));
    }

    @Test
    public void getProfileDetail(){
        Long playerId = 12L;
        Long currentPlayerId = 7L;
        Map map = playerService.getProfileDetail(playerId, currentPlayerId);
        System.out.println(JSON.toJSONString(map));
    }
//    List<Long> pageList(List<Long> list,Integer pageNo,Integer pageSize);
    @Test
    public void pageList(){
        List<Long> list = new ArrayList<>();
        for(long i = 0;i < 105; i++){
            list.add(i);
        }
        System.out.println(playerService.pageList(list,11,10));

    }













    @Test
    public void insertPlayerTest(){
        Player p = new Player("游龙", "上海", 0, "http://CDN.img.youzu.com/zhibo_145586400254837_18182315.jpg","open_37534455",1);
        Player player = playerService.regist(p);
        System.out.print("player id:" + player.getId());
    }





}
