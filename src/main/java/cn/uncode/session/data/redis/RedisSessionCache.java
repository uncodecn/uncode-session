package cn.uncode.session.data.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uncode.session.data.SerializeUtil;
import cn.uncode.session.data.SessionCache;
import cn.uncode.session.data.SessionMap;
import redis.clients.jedis.Jedis;

public class RedisSessionCache implements SessionCache{
	
	private static final Logger LOG =LoggerFactory.getLogger(RedisSessionCache.class);
	
	@Override
	public void put(String sessionId, SessionMap sessionMap, int timeout) {
		RedisSentinelPool jedisPool = null;
		Jedis jedis = null;
		try {
			jedisPool = RedisSentinelPool.getPool();
			jedis = jedisPool.getResource();
            jedis.set(sessionId.getBytes(), SerializeUtil.serialize(sessionMap));
            jedis.expire(sessionId, timeout);
		} catch (Exception e) {
			LOG.error("Put session to redis error", e);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public SessionMap get(String sessionId) {
		RedisSentinelPool jedisPool = null;
		Jedis jedis = null;
		SessionMap sessionMap = null;
		byte[] reslut = null;
        try {
        	jedisPool = RedisSentinelPool.getPool();
            jedis = jedisPool.getResource();
            if (jedis.exists(sessionId)) {
                reslut = jedis.get(sessionId.getBytes());
                sessionMap = (SessionMap) SerializeUtil.unserialize(reslut);
            }
        } catch (Exception e) {
        	LOG.error("Read session from redis error", e);
            return null;
        } finally {
            jedisPool.returnResource(jedis);
        }
        return sessionMap;
	}

	@Override
	public void setMaxInactiveInterval(String sessionId, int interval) {
		RedisSentinelPool jedisPool = null;
		Jedis jedis = null;
        try {
        	jedisPool = RedisSentinelPool.getPool();
            jedis = jedisPool.getResource();
            if (jedis.exists(sessionId)) {
            	jedis.expire(sessionId, interval);
            }
        } catch (Exception e) {
        	LOG.error("Set session max inactive interval to redis error", e);
        } finally {
            jedisPool.returnResource(jedis);
        }
	}

	@Override
	public void destroy(String sessionId) {
		RedisSentinelPool jedisPool = null;
		Jedis jedis = null;
        try {
        	jedisPool = RedisSentinelPool.getPool();
            jedis = jedisPool.getResource();
            if (jedis.exists(sessionId)) {
            	jedis.expire(sessionId, 0);
            }
        } catch (Exception e) {
        	LOG.error("Destroy session from redis error", e);
        } finally {
            jedisPool.returnResource(jedis);
        }

	}



}
