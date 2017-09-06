package com.ice.bike.common.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * 
 * 创建时间： 2017年7月16日 下午3:19:43
 * 
 * @author ice
 *
 */
@Service("redisCacheUtil")
public class RedisCacheUtil<T> {

	@Autowired
	protected RedisTemplate<String, T> redisTemplate;

	/** 获取ValueOperations. */
	public ValueOperations<String, T> getValueOperations() {
		return redisTemplate.opsForValue();
	}

	/** 获取ListOperations. */
	public ListOperations<String, T> getListOperations() {
		return redisTemplate.opsForList();
	}

	/** 设置缓存值. */
	public ValueOperations<String, T> setCacheObject(String key, T value) throws Exception {
		if (key == null || value == null) {
			return null;
		}
		ValueOperations<String, T> operation = redisTemplate.opsForValue();
		operation.set(key, value);
		return operation;
	}

	/** 设置缓存值. */
	public ValueOperations<String, T> setCacheObject(String key, T value, long time) throws Exception {
		if (key == null || value == null) {
			return null;
		}
		ValueOperations<String, T> operation = redisTemplate.opsForValue();
		operation.set(key, value, time, TimeUnit.SECONDS);
		return operation;
	}

	/** 获取缓存值. */
	public T getCacheObject(String key) throws Exception {
		if (key == null) {
			return null;
		}
		ValueOperations<String, T> operation = redisTemplate.opsForValue();
		return operation.get(key);
	}

	/** 删除key值. */
	public boolean deleteKey(String key) throws Exception {
		if (key == null) {
			return false;
		}
		redisTemplate.delete(key);
		return true;
	}

	/** 缓存list数据. */
	public ListOperations<String, T> setCacheList(String key, List<T> list) throws Exception {
		ListOperations<String, T> listOperation = redisTemplate.opsForList();
		if (key == null || list.isEmpty()) {
			return null;
		}
		for (T object : list) {
			listOperation.rightPush(key, object);
		}
		return listOperation;
	}

	/** 获取list缓存的值. */
	public List<T> getCacheList(String key) throws Exception {
		if (key == null) {
			return null;
		}
		List<T> list = new ArrayList<>();
		ListOperations<String, T> listOperation = redisTemplate.opsForList();
		Long size = listOperation.size(key);
		if (size.intValue() < 1) {
			return null;
		}
		for (int i = 0; i < size.intValue(); i++) {
			list.add(listOperation.leftPop(key));
		}
		return list;
	}

	/** 缓存Map对象. */
	public HashOperations<String, String, T> setCacheMap(String key, Map<String, T> dataMap) {
		if (key == null || dataMap.isEmpty()) {
			return null;
		}
		HashOperations<String, String, T> hashOperation = redisTemplate.opsForHash();
		for (Map.Entry<String, T> entry : dataMap.entrySet()) {
			hashOperation.put(key, entry.getKey(), entry.getValue());
		}
		return hashOperation;
	}

	/** 获取map对象. */
	public Map<String, T> getCacheMap(String key) {
		HashOperations<String, String, T> hashOperation = redisTemplate.opsForHash();
		Map<String, T> entries = hashOperation.entries(key);
		return entries;
	}

	/** 缓存Map对象. */
	public HashOperations<String, Integer, T> setCacheIntegerMap(String key, Map<Integer, T> dataMap) {
		if (key == null || dataMap.isEmpty()) {
			return null;
		}
		HashOperations<String, Integer, T> hashOperation = redisTemplate.opsForHash();
		for (Map.Entry<Integer, T> entry : dataMap.entrySet()) {
			hashOperation.put(key, entry.getKey(), entry.getValue());
		}
		return hashOperation;
	}

	/** 获取map对象. */
	public Map<Integer, T> getCacheIntegerMap(String key) {
		HashOperations<String, Integer, T> hashOperation = redisTemplate.opsForHash();
		Map<Integer, T> entries = hashOperation.entries(key);
		if (entries.isEmpty()) {
			return null;
		}
		return entries;
	}

	public boolean deleteMap(String redisKey, String... mapKeys) {
		if (redisKey == null || mapKeys == null) {
			return false;
		}
		try {
			redisTemplate.opsForHash().delete(redisKey, mapKeys);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
