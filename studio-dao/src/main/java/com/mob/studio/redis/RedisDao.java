package com.mob.studio.redis;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collection;
import java.util.Set;

/**
 * Redis DAO interface
 * @author: Zhang.Min
 * @since: 2016/3/14
 * @version: 1.7
 */
public interface RedisDao {
    boolean exists(String key);

    void set(String key, String value, long expire);

    void set(String key, String value);

    String get(String key);

    void delete(String key);

    boolean hexists(String key, String hKey);

    String hget(String key, String hKey);

    void hset(String key, String hkey, String hValue);

    void hdelete(String key, String hKey);

    Collection<Object> hgetAllKeys(String key);

    Set<String> fuzzySearch(String pattern);

    void zAdd(String key, String value, long score);

    void zRemove(String key, Object object);

    Set<String> zRange(String key,int start, int end);

    Set<String> zRevRange(String key,int start, int end);

    Set<ZSetOperations.TypedTuple<String>> zRangeWithScore(String key, int start, int end);

    Set<ZSetOperations.TypedTuple<String>> zRevRangeWithScore(String key,int start, int end);

    Number zScore(String key, String value);

    void sAdd(String key,String...values);

    Set sMembers(String key);
}
