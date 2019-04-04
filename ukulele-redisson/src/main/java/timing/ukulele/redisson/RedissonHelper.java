package timing.ukulele.redisson;

import org.redisson.api.RBucket;
import org.redisson.api.RType;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import timing.ukulele.redisson.cache.CacheManager;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存辅助类
 */
public class RedissonHelper implements CacheManager, ApplicationContextAware {

    private RedissonClient redisTemplate = null;
    private Integer EXPIRE = 60;

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取连接
     */
    private RedissonClient getRedis() {
        if (redisTemplate == null) {
            synchronized (RedissonHelper.class) {
                if (redisTemplate == null) {
                    redisTemplate = applicationContext.getBean(RedissonClient.class);
                }
            }
        }
        return redisTemplate;
    }

    /**
     * 根据键值获取对象桶
     *
     * @param key 键值
     * @return 对象桶
     */
    private RBucket<Object> getRedisBucket(String key) {
        return getRedis().getBucket(key);
    }

    /**
     * 根据键值获取对象
     *
     * @param key 键值
     * @return 对象
     */
    public final Object get(final String key) {
        RBucket<Object> temp = getRedisBucket(key);
//		get时暂时不设置过期时间 吕自聪 2017/06/15
//		expire(temp, EXPIRE);
        return temp.get();
    }

    /**
     * 设置某个键值，默认的过期时间
     *
     * @param key   键
     * @param value 值
     */
    public final void set(final String key, final Serializable value) {
        RBucket<Object> temp = getRedisBucket(key);
        temp.set(value);
        expire(temp, EXPIRE);
    }

    /**
     * 设置某个键值，并指定过期时间
     *
     * @param key     键
     * @param value   值
     * @param seconds 过期时间
     */
    public final void set(final String key, final Serializable value, int seconds) {
        RBucket<Object> temp = getRedisBucket(key);
        temp.set(value);
        expire(temp, seconds);
    }

    /**
     * 批量设置键值
     *
     * @param temps 键值对map
     */
    public final void multiSet(final Map<String, Object> temps) {
        getRedis().getBuckets().set(temps);
    }

    /**
     * 判断某个键值是否存在
     *
     * @param key 键值
     * @return 是否存在
     */
    public final Boolean exists(final String key) {
        RBucket<Object> temp = getRedisBucket(key);
        return temp.isExists();
    }

    /**
     * 同步删除某个键及对应的值
     *
     * @param key 键
     */
    public final void del(final String key) {
        getRedis().getKeys().deleteAsync(key);
    }

    /**
     * 删除匹配表达式的所有键及对应的值
     *
     * @param pattern 表达式
     */
    public final void delAll(final String pattern) {
        getRedis().getKeys().deleteByPattern(pattern);
    }

    /**
     * 获取某个键对应的值的类型
     *
     * @param key 键
     * @return 类型字符串
     */
    public final String type(final String key) {
        RType type = getRedis().getKeys().getType(key);
        if (type == null) {
            return null;
        }
        return type.getClass().getName();
    }

    /**
     * 在某段时间后失效
     *
     * @param seconds 过期时间
     * @param bucket  bucket
     */
    private void expire(final RBucket<Object> bucket, final int seconds) {
        bucket.expire(seconds, TimeUnit.SECONDS);
    }

    /**
     * 在某个时间点失效
     *
     * @param key      键值
     * @param unixTime 时间
     * @return 是否设置时间
     */
    public final Boolean expireAt(final String key, final long unixTime) {
        return getRedis().getBucket(key).expireAt(new Date(unixTime));
    }

    public final Long ttl(final String key) {
        RBucket<Object> rBucket = getRedisBucket(key);
        return rBucket.remainTimeToLive();
    }

    public final Object getSet(final String key, final Serializable value) {
        RBucket<Object> rBucket = getRedisBucket(key);
        return rBucket.getAndSet(value);
    }

    public Set<Object> getAll(String pattern) {
        Set<Object> set = new HashSet<>();
        Iterable<String> keys = getRedis().getKeys().getKeysByPattern(pattern);
        for (String key : keys) {
            set.add(getRedisBucket(key).get());
        }
        return set;
    }

    public Boolean expire(String key, int seconds) {
        RBucket<Object> bucket = getRedisBucket(key);
        expire(bucket, seconds);
        return true;
    }

    public boolean setnx(String key, Serializable value) {
        return getRedis().getLock(key).tryLock();
    }

    public void unlock(String key) {
        getRedis().getLock(key).unlock();
    }

    public void hset(String key, String field, String value) {
        getRedis().getMap(key).put(field, value);
    }

    public Object hget(String key, String field) {
        return getRedis().getMap(key).get(field);
    }

    public void hdel(String key, String field) {
        getRedis().getMap(key).remove(field);
    }
}
