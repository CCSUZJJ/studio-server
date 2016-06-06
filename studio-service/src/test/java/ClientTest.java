/**
 * User: jianwl
 * Date: 2016/3/23
 * Time: 19:44
 */
import com.alibaba.fastjson.JSON;
import com.mob.studio.test.EncodeHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author: Zhang.Min
 * @since: 2016/3/16
 * @version: 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
public class ClientTest {

    @Test
    public void test() {
        String seed = "mob";
        String nonce = "dfaerE3";
        String timestamp = "1458115556";
        String[] str = { seed, timestamp, nonce };
        Arrays.sort(str); // 字典序排序
        String inputStr = str[0] + str[1] + str[2];
        String signature = EncodeHelper.encode(EncodeHelper.ALGORITHM_SHA1, inputStr);

        String aliasId = "open9187655";
        Integer aliasType = 1;
        String avatar = "http://avatar.com/1.jpg";
        String nick = "阿龙";
        Integer sex = 1;
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("seed",seed);
        bodyMap.put("nonce", nonce);
        bodyMap.put("timestamp", timestamp);
        bodyMap.put("aliasId",aliasId);
        bodyMap.put("aliasType", aliasType);
        bodyMap.put("avatar", avatar);
        bodyMap.put("nick", nick);
        bodyMap.put("sex", sex);
        String text = JSON.toJSONString(bodyMap);
        System.out.println("signature >>> " + signature);
        System.out.println("body >>> " + text);
        System.out.println(System.currentTimeMillis());
        try {
            System.out.println("encode body >>>" + EncodeHelper.Base64Encode(EncodeHelper.AES128Encode("mobLive", text)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    # Home page
//    POST    /home/hot                   controllers.Home.hot()
//    POST    /home/follower              controllers.Home.follower()
//    POST    /home/talent                controllers.Home.talent()
//    POST    /home/search                controllers.Home.search()
//
//            # my actions
//    POST    /my/follow/:targetId        controllers.My.follow(targetId : Long)
//    POST    /my/unfollow/:targetId      controllers.My.unfollow(targetId : Long)
//    POST    /my/black/:targetId         controllers.My.black(targetId : Long)
//    POST    /my/unblack/:targetId       controllers.My.unblack(targetId : Long)
//
//            # my home page
//    POST    /my/home                     controllers.My.home()
//    POST    /my/followers/list           controllers.My.follower_list()
//    POST    /my/fans/list                controllers.My.fans_list()
//    POST    /my/blacklist                controllers.My.black_list()
//    POST    /my/giftrank                 controllers.My.gift_rank_list()
//    POST    /my/update                   controllers.My.update()
//
//            # other player page
//    POST    /player/:playerId/portrait   controllers.Player.portrait(playerId : Long)
//    POST    /player/:playerId/home       controllers.Player.home(playerId : Long)
//    POST    /player/:playerId/follow     controllers.Player.follow(playerId : Long)
//    POST    /player/:playerId/fans       controllers.Player.fans(playerId : Long)

    //    # Studio
//    POST    /studio/open                controllers.Studio.open()
    @Test
    public void openTest(){
        String seed = "mob";
        String nonce = "dfaerE3";
        String timestamp = "1458115556";
        String[] str = { seed, timestamp, nonce };
        Arrays.sort(str); // 字典序排序
        String inputStr = str[0] + str[1] + str[2];
        String signature = EncodeHelper.encode(EncodeHelper.ALGORITHM_SHA1, inputStr);

        Long playerId = 21L ;
        String title = "猴子请来的逗逼";
        Integer pageNo = 1;
        Integer pageSize = 10;
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("seed",seed);
        bodyMap.put("nonce", nonce);
        bodyMap.put("timestamp", timestamp);
        bodyMap.put("playerId",playerId);
        bodyMap.put("title", title);
//        bodyMap.put("pageNo", pageNo);
//        bodyMap.put("pageSize", pageSize);
        String text = JSON.toJSONString(bodyMap);
        System.out.println("signature >>> " + signature);
        System.out.println("body >>> " + text);
        System.out.println(System.currentTimeMillis());
        try {
            System.out.println("encode body >>>" + EncodeHelper.Base64Encode(EncodeHelper.AES128Encode("mobLive", text)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void randomTest(){
        Random random = new Random(5);
        for(int i=0 ;i< 100;i++){
            System.out.println(random.nextInt(5^10000));
        }
    }

}

