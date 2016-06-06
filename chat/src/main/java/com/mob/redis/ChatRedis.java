package com.mob.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created with Intellij IDEA
 * User: panal
 * Date: 2016/3/28
 * Time: 17:18
 */
public interface ChatRedis {

    boolean exists(String key);

    void set(String key, String value);

    String get(String key);

    void delete(String key);

    boolean hexists(String key, String hashKey);

    void lPush(String key, String value);

    String blPop(String key, long timeout, TimeUnit timeUnit);

    void hset(String key, Object hashKey, Object value);

    void hIncrement(String key, String hashKey, Double value);

    String hget(String key, String hashKey);

    Map<Object, Object> hgetAll(String key);

    void hdelete(String key, String hashKey);

    void zSet(String key, String value, long score);

    void zIncrement(String key, String object, Double incre);

    Double zGetScore(String key, String object);

    Set<String> zRange(String key, int start, int end);

    Set<String> zRevRange(String key, int start, int end);

    void zRemove(String key, Object[] objects);

    void zRemove(String key, Object object);

    List<String> multiGetByKeys(Set<String> keySet);

    void sendGift(final String fromKey, final String toKey, final Long coin, final Long send, final Long fromPoint,
                  final Long toPoint, final String giftRankKey, final String fromId, final Long giftPoint);

    void updateViewers(final String hotRankKey, final String roomInfoKey, final String roomInfoViewersKey,
                       final String roomId, final  Double status);

    void updateFansRoomList(List<Long> ids, String roomId, long createAt);

    void removeFansRoomList(List<Long> ids, String roomId);

    void removeRoom(final String onlineKey, final String roomInfoKey, final String hotRankKey, final String anchorId, final String roomId);

    void removeAll();
}

