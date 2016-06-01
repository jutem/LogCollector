package com.jutem.log.collector.pusher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 把log推到redis 
 * @author jutem 
 */
public class RedisPusher implements Pusher{
	
	private static final Logger logger = Logger.getLogger(RedisPusher.class);

	private RedisTemplate<String, String> logRedisTemplate; //redis
	private ExecutorService logPool; //异步线程池 
	private String redisKey;
	
	public RedisPusher(RedisTemplate<String, String> redis, String redisKey) {
		this(redis, Executors.newFixedThreadPool(30), redisKey);
	}
	
	public RedisPusher(RedisTemplate<String, String> redis, ExecutorService logPool, String redisKey) {
		logger.info("redis pusher init success");
		
		this.logRedisTemplate = redis;
		this.logPool = logPool; 
		this.redisKey = redisKey;
	}

	@Override
	public void push(final String log) {
		logPool.submit(new Runnable() {
			@Override
			public void run() {
				logger.info("<<<<<<<< push log : " + log); //TODO 改成debug
				logRedisTemplate.boundListOps(redisKey).rightPush(log);
			}
		});
	}
	
}
