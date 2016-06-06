package com.mob.studio.util;

/**
 * @author: Zhang.Min
 * @since: 2016/3/15
 * @version: 1.7
 */
public class RedisKey {
    public static final String TOP_NAME_SPACE = "studio:";
    public static final String PLAYER = TOP_NAME_SPACE + "player:";
    public static final String PLAYER_INFO = "info";
    public static final String PLAYER_FOLLOWS = "follows";
    public static final String PLAYER_FANS = "fans";
    public static final String PLAYER_POINT = "point";
    public static final String PLAYER_BLACKLIST = "blacklist";
    public static final String PLAYER_SEND = "send";
    public static final String PLAYER_COIN = "coin";

    public static final String ROOM = TOP_NAME_SPACE + "room:";
    public static final String ROOM_PLAYER = "player";
    public static final String ROOM_TITLE = "title";
    public static final String ROOM_VIEWERS = "viewers";

    public static final String ITEM_ID_LIST = TOP_NAME_SPACE + "item_list";
    public static final String ITEM = TOP_NAME_SPACE + "item:";
    public static final String ITEM_NAME = "name";
    public static final String ITEM_POINT = "point";
    public static final String ITEM_PRICE = "price";


    public static final String HOT_RANK = TOP_NAME_SPACE + "hot_rank";


    public static final String TALENT_RANK = TOP_NAME_SPACE + "talent_rank";


    public static final String FOLLOWER_RANK = TOP_NAME_SPACE + "follower_rank:";
    public static final String GIFT_RANK = TOP_NAME_SPACE + "gift_rank:";
    public static final String ROOM_ONLINE = TOP_NAME_SPACE + "room_online";

}
