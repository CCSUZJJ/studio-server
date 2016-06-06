package com.mob.studio.redis.impl;

import com.mob.studio.redis.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class RedisDaoImpl implements RedisDao {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void initRedisTemplate(){
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
    }

    @Override
    public void hset(String key, String hKey, String hValue) {
        redisTemplate.opsForHash().put(key, hKey, hValue);
    }

    @Override
    public void set(final String key, final String value, final long expire) {
        SessionCallback<String> sessionCallback = new SessionCallback<String>() {
            @Override
            public <K, V> String execute(RedisOperations<K, V> operation) throws DataAccessException {
                operation.multi();
                redisTemplate.opsForValue().set(key, value);
                redisTemplate.expire(key,expire, TimeUnit.SECONDS);
                operation.exec();
                return null;
            }
        };

        redisTemplate.execute(sessionCallback);
    }

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String hget(String key, String hKey) {
        return (String)redisTemplate.opsForHash().get(key, hKey);
    }

    @Override
    public Collection<Object> hgetAllKeys(String key) {
       return redisTemplate.opsForHash().keys(key);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Set<String> fuzzySearch(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public void zAdd(String key, String value, long score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public void zRemove(String key, Object object) {
        redisTemplate.opsForZSet().remove(key, object);
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
    public Set<ZSetOperations.TypedTuple<String>> zRangeWithScore(String key, int start, int end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<String>> zRevRangeWithScore(String key, int start, int end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    @Override
    public Number zScore(String key, String value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    @Override
    public boolean hexists(String key, String hKey) {
        return redisTemplate.opsForHash().hasKey(key, hKey);
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void hdelete(String key, String hKey) {
        redisTemplate.opsForHash().delete(key, hKey);
    }

    @Override
    public void sAdd(String key,String...values) {
        redisTemplate.opsForSet().add(key,values);
    }

    @Override
    public Set sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }
}
