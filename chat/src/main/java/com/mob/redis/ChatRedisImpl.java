package com.mob.redis;

import com.mob.utils.RedisKeyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created with Intellij IDEA
 * User: panal
 * Date: 2016/3/25
 * Time: 11:04
 */
public class ChatRedisImpl implements ChatRedis{

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void initRedisTemplate() {
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void lPush(String key, String value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public String blPop(String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.opsForList().leftPop(key, timeout, timeUnit);
    }

    @Override
    public boolean hexists(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public void hset(String key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public void hIncrement(String key, String hashKey, Double value) {
        redisTemplate.opsForHash().increment(key, hashKey, value);
    }

    @Override
    public String hget(String key, String hashKey) {
        return (String)redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Map<Object, Object> hgetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public void hdelete(String key, String hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    @Override
    public void zSet(String key, String value, long score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public void zIncrement(String key, String object, Double incre) {
        redisTemplate.opsForZSet().incrementScore(key, object, incre);
    }

    @Override
    public Double zGetScore(String key, String object) {
        return redisTemplate.opsForZSet().score(key, object);
    }

    @Override
    public Set<String> zRange(String key, int start, int end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    @Override
    public Set<String> zRevRange(String key, int start, int end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    @Override
    public void zRemove(String key, Object[] objects) {
        redisTemplate.opsForZSet().remove(key, objects);
    }

    @Override
    public void zRemove(String key, Object object) {
        redisTemplate.opsForZSet().remove(key, object);
    }

    @Override
    public List<String> multiGetByKeys(Set<String> keySet) {
        return redisTemplate.opsForValue().multiGet(keySet);
    }

    @Override
    public void sendGift(final String fromKey, final String toKey, final Long coin, final Long send, final Long fromPoint,
                         final Long toPoint, final String giftRankKey, final String fromId, final Long giftPoint) {
        SessionCallback<String> sessionCallback = new SessionCallback<String>() {
            @Override
            public <K, V> String execute(RedisOperations<K, V> operation) throws DataAccessException{
                operation.multi();
                redisTemplate.opsForHash().put(fromKey, RedisKeyConstants.PLAYER_COIN_KEY, coin.toString());
                redisTemplate.opsForHash().put(fromKey, RedisKeyConstants.PLAYER_SEND_KEY, send.toString());
                redisTemplate.opsForHash().put(fromKey, RedisKeyConstants.PLAYER_POINT_KEY, fromPoint.toString());
                redisTemplate.opsForHash().put(toKey, RedisKeyConstants.PLAYER_POINT_KEY, toPoint.toString());
                redisTemplate.opsForZSet().incrementScore(giftRankKey, fromId, giftPoint);
                operation.exec();
                return null;
            }
        };
        redisTemplate.execute(sessionCallback);
    }

    @Override
    public void updateViewers(final String hotRankKey, final String roomInfoKey, final String roomInfoViewersKey,
                                final String roomId, final  Double status){
        SessionCallback<String> sessionCallback = new SessionCallback<String>() {
            @Override
            public <K, V> String execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                redisOperations.multi();
                redisTemplate.opsForZSet().incrementScore(hotRankKey, roomId, status);
                redisTemplate.opsForHash().increment(roomInfoKey, roomInfoViewersKey, status);
                redisOperations.exec();
                return null;
            }
        };
        redisTemplate.execute(sessionCallback);
    }

    @Override
    public void updateFansRoomList(final List<Long> playerIds, final String roomId, final long createAt){
        RedisCallback<String> redisCallback = new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                final byte[] value = roomId.getBytes();
                for (Long playerId : playerIds) {
                   final byte[] key = redisTemplate.getStringSerializer().serialize(String.format(RedisKeyConstants.PLAYER_FOLLOWER_RANK_KEY, playerId));
                    redisConnection.zAdd(key, createAt, value);
                }
                return null;
            }
        };
        redisTemplate.execute(redisCallback, false, true);
    }

    @Override
    public void removeFansRoomList(final List<Long> playerIds, final String roomId){
        RedisCallback<String> redisCallback = new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                final byte[] value = roomId.getBytes();
                for (Long playerId : playerIds) {
                    final byte[] key = String.format(RedisKeyConstants.PLAYER_FOLLOWER_RANK_KEY, playerId).getBytes();
                    redisConnection.zRem(key, value);
                }
                return null;
            }
        };
        redisTemplate.execute(redisCallback, false, true);
    }

    @Override
    public void removeRoom(final String onlineKey, final String roomInfoKey, final String hotRankKey,
                            final String anchorId, final String roomId){
        SessionCallback<String> sessionCallback = new SessionCallback<String>() {
            @Override
            public <K, V> String execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                redisOperations.multi();
                redisTemplate.opsForHash().delete(onlineKey, anchorId);  //删除房间号-主播的对应关系
                redisTemplate.delete(String.format(roomInfoKey, roomId));//清除直播间的信息
                redisTemplate.opsForZSet().remove(hotRankKey, roomId);   //清除热门列表中的roomId
                redisOperations.exec();
                return null;
            }
        };
        redisTemplate.execute(sessionCallback);
    }

    @Override
    public void removeAll(){
        final Set<String> sets = redisTemplate.keys("studio:*");
        RedisCallback<String> redisCallback = new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {;
                for (String key : sets) {
                    System.out.println(key);
                    final byte[] bytes = key.getBytes();
                    redisConnection.del(bytes);
                }
                return null;
            }
        };
        redisTemplate.execute(redisCallback, false, true);
    }
}
