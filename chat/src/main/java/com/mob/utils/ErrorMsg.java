package com.mob.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误信息
 * User: panal
 * Date: 2016/3/30
 * Time: 11:50
 */
public class ErrorMsg {

    private static final Map<Integer, String> msgMap = new HashMap<Integer, String>();

    /**
     * 向不存在的直播间发送消息
     */
    public static final int s401 = 401;

    /**
     * 不存在该礼物
     */
    public static final int s402 = 402;

    /**
     * 没有足够的金币送礼
     */
    public static final int s403 = 403;

    /**
     * 主播已离开房间，房间不存在
     */
    public static final int s405 = 405;

    /**
     *同一用户不能同时进入两个设备
     */
    public static final int s406 = 406;

    /**
     *不能重复进入房间
     */
    public static final int s407 = 407;

    static {
        msgMap.put(s401, "Group [%s] is not found, send message failed.");
        msgMap.put(s402, "It does not exist the item : [%s]");
        msgMap.put(s403, "Member [%s] doesn't have enough gold to give the gift ");
        msgMap.put(s405, "Anchor has left the room, the room [%s] does not exist.");
        msgMap.put(s406, "You can not enter two rooms at the same time.");
        msgMap.put(s407, "Refuse to repeat join the group[%s].");
    }

    public static String getMsg(int status) {
        return msgMap.get(status);
    }
}
