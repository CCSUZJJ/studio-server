package com.mob.services;


import com.lamfire.json.JSON;
import com.lamfire.utils.StringUtils;
import com.mob.config.AppContextConfig;
import com.mob.redis.ChatRedis;
import com.mob.utils.RedisKeyConstants;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Chat业务
 * User: panal
 * Date: 2016/3/14
 * Time: 11:13
 */
public class ChatService {

    private static Logger LOGGER = Logger.getLogger(ChatService.class);
    private static Map<String, Long> itemMap = new HashMap<String, Long>();
    private static ChatRedis chatRedis = AppContextConfig.getChatRedis();
//    private static ChatRedis chatRedis;

    /**
     * 初始化，加载配置文件
     */
    public static void init() {
        ApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext.xml");
        chatRedis = (ChatRedis)context.getBean("chatRedis");
    }

    /**
     * 创建直播间,更新关注主播的用户列表
     * @param roomId
     * @param playerId
     */
    public static void initRoom(String roomId, String playerId) {
        try{
            List<Long> ids = getFollowers(playerId);
            if (ids != null){
                long createAt = System.currentTimeMillis();
                chatRedis.updateFansRoomList(ids, roomId, createAt);
                /*for (String id : ids) {
                    String key = String.format(PLAYER_FOLLOWER_RANK_KEY, id);
                    chatRedis.zSet(key, roomId, createAt);
                }*/
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    /**
     * 是否存在该房间
     * @param roomId
     * @return
     */
    public static boolean existRoom(String roomId) {
        return chatRedis.hexists(String.format(RedisKeyConstants.ROOM_INFO_KEY, roomId), RedisKeyConstants.ROOM_PLAYER_KEY);
    }

    public static String getRoomviewers(String roomId) {
        return  chatRedis.hget(String.format(RedisKeyConstants.ROOM_INFO_KEY, roomId), RedisKeyConstants.ROOM_INFO_VIEWERS_KEY);
    }

    /**
     * 更新在线观看人数
     * @param roomId 直播间id
     * @param status (1-在线 -1-离开)
     */
    public static void updateRoomViewers(String roomId, Double status) {
//        chatRedis.zIncrement(HOT_RANK_KEY, roomId, status);
//        chatRedis.hIncrement(String.format(ROOM_INFO_KEY, roomId), ROOM_INFO_VIEWERS_KEY, status);
        chatRedis.updateViewers(RedisKeyConstants.HOT_RANK_KEY, String.format(RedisKeyConstants.ROOM_INFO_KEY, roomId), RedisKeyConstants.ROOM_INFO_VIEWERS_KEY, roomId, status);
    }


    /**
     * 获取关注列表
     * @param playerId
     * @return
     */
    public static List<Long> getFollowers(String playerId) {
        String ids = chatRedis.hget(String.format(RedisKeyConstants.PLAYER_KEY, playerId), RedisKeyConstants.PLAYER_FANS_KEY);
        if (StringUtils.isNotBlank(ids)){
            return JSON.toJavaObjectArray(ids, Long.class);
        }
        return null;
    }


    /**
     * 获取礼物的值
     * @param itemId
     * @return
     */
    public static Long getItemPoint(String itemId){
        if (!itemMap.containsKey(itemId)){
            String key = String.format(RedisKeyConstants.ITEM_KEY, itemId);
            if (!chatRedis.hexists(key, RedisKeyConstants.ITEM_POINT_KEY)){
                return null;
            }
            String value = chatRedis.hget(key, RedisKeyConstants.ITEM_POINT_KEY);
            itemMap.put(itemId, Long.valueOf(value));
        }
        return itemMap.get(itemId);
    }


    /**
     * 送礼物
     * @param fromId 送礼物的用户id
     * @param toId   收礼物的用户id
     * @param giftPoint 收礼物用户总的礼物值
     * @return
     */
    public static Long sendGift(String fromId, String toId, Long giftPoint) {
        if (giftPoint == null || giftPoint == 0) {
            return -1l;
        }
        long fromCoin = Long.valueOf(chatRedis.hget(String.format(RedisKeyConstants.PLAYER_KEY, fromId), RedisKeyConstants.PLAYER_COIN_KEY));
        if(fromCoin >= giftPoint) {
            return updateGiftPoint(fromId, toId, giftPoint);
        }
        return -2l;
    }


    /**
     * 获取用户映票数
     * @param playerId
     * @return
     */
    public static Long getPlayerGift(String playerId) {
        if (StringUtils.isNotBlank(playerId)) {
            String key = String.format(RedisKeyConstants.PLAYER_KEY, playerId);
            String point = chatRedis.hget(key, RedisKeyConstants.PLAYER_POINT_KEY);
            return Long.valueOf(point);
        }
        return 0l;
    }


    public static Map<Object, Object> getRoomOnlineList() {
        return chatRedis.hgetAll(RedisKeyConstants.ROOM_ONLINE_KEY);
    }


    /**
     * 更新送礼物和收礼物的用户的对应积分
     * @param fromId
     * @param toId
     * @param giftPoint
     * @return
     */
    public static Long updateGiftPoint(String fromId, String toId, long giftPoint) {
        String fromKey = String.format(RedisKeyConstants.PLAYER_KEY, fromId);
        String toKey =  String.format(RedisKeyConstants.PLAYER_KEY, toId);
        String giftRankKey = String.format(RedisKeyConstants.GIFT_RANK_KEY, toId);
        long fromCoin = Long.valueOf(chatRedis.hget(fromKey, RedisKeyConstants.PLAYER_COIN_KEY)) - giftPoint;
        long fromPoint = Long.valueOf(chatRedis.hget(fromKey, RedisKeyConstants.PLAYER_POINT_KEY)) - giftPoint;
        long fromSend = Long.valueOf(chatRedis.hget(fromKey, RedisKeyConstants.PLAYER_SEND_KEY)) + giftPoint;
        long toPoint = Long.valueOf(chatRedis.hget(toKey, RedisKeyConstants.PLAYER_POINT_KEY)) + giftPoint;

        /*String id = fromId + "_" + toId;
        PlayerGiftChange playerGiftChange = new PlayerGiftChange(id, giftPoint, System.currentTimeMillis());
        String json = JSON.fromJavaObject(playerGiftChange).toJSONString();
        chatRedis.lPush(GIFT_KEY, json);*/

        chatRedis.sendGift(fromKey, toKey, fromCoin, fromSend, fromPoint, toPoint, giftRankKey, fromId, giftPoint);
        return toPoint;
    }


    /**
     * 主播离开房间,房间关闭后同步redis中与直播间相关的信息
     * @param anchorId 主播id
     * @param roomId   房间id
     */
    public static void syncOnAnchorLeave(String anchorId, String roomId) {
        /*
        //删除房间号-主播的对应关系
        chatRedis.hdelete(RedisKeyConstants.ROOM_ONLINE_KEY, anchorId);

        //清除直播间的信息
        chatRedis.delete(String.format(RedisKeyConstants.ROOM_INFO_KEY, roomId));

        //清除热门列表中的roomId
        chatRedis.zRemove(RedisKeyConstants.HOT_RANK_KEY, roomId);*/
        chatRedis.removeRoom(RedisKeyConstants.ROOM_ONLINE_KEY, RedisKeyConstants.ROOM_INFO_KEY, RedisKeyConstants.HOT_RANK_KEY, anchorId, roomId);
        LOGGER.info("[syncOnAnchorLeave] : anchorId = " + anchorId + ", roomId = " + roomId);
        //清除各用户关注列表中的roomId
        List<Long> fans = getFollowers(anchorId);
        if (fans != null){
            /*for(String playerId : fans) {
                String key = String.format(PLAYER_FOLLOWER_RANK_KEY, playerId);
                chatRedis.zRemove(key, roomId);
            }*/
            chatRedis.removeFansRoomList(fans, roomId);
        }
    }


    public static void main(String[] args) {
        ChatService.init();
//        AppContextConfig.init();
        String key = "studio:player:%s";
//        for (int i = 7; i < 50; i++) {
//            String k = String.format(key, i);
//            chatRedis.delete(k);
//        }
        chatRedis.removeAll();
    }

}
