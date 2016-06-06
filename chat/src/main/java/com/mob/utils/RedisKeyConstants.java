package com.mob.utils;

/**
 * Redis中的key常量
 * User: panal
 * Date: 2016/4/13
 * Time: 16:18
 */
public class RedisKeyConstants {

    public static final String ROOM_ONLINE_KEY = "studio:room_online"; //在线房间
    public static final String ROOM_INFO_KEY = "studio:room:%s";     //房间信息
    public static final String ROOM_INFO_VIEWERS_KEY = "viewers";     //房间信息
    public static final String HOT_RANK_KEY = "studio:hot_rank";     //热门
    public static final String GIFT_RANK_KEY = "studio:gift_rank:%s";  //鲜花贡献榜
    public static final String PLAYER_FOLLOWER_RANK_KEY = "studio:follower_rank:%s";  //关注列表
    public static final String PLAYER_KEY = "studio:player:%s";      //用户key
    public static final String PLAYER_FANS_KEY = "fans";             //用户粉丝列表
    public static final String PLAYER_POINT_KEY = "point";           //收到的礼物数量
    public static final String PLAYER_SEND_KEY = "send";             //送出的礼物数量
    public static final String PLAYER_COIN_KEY = "coin";             //剩下的礼物数量
    public static final String ITEM_KEY = "studio:item:%s";          //礼物列表
    public static final String ITEM_POINT_KEY = "point";             //礼物列表
    public static final String GIFT_KEY = "studio:gift";             //送礼列表
    public static final String ROOM_PLAYER_KEY = "player";
}
