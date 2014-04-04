package com.fourtyblocksunder.abstention.util;

public class Cache<T> {
	private T cachedType;

	private String cachedKey = "";

	public boolean keysMatch(String key) {
		return key.equals(cachedKey);
	}

	public void setCache(T type, String newKey) {
		this.cachedType = type;
		setKey(newKey);
	}

	public T getCache() {
		return this.cachedType;
	}

	public void setKey(String newKey) {
		this.cachedKey = newKey;
	}

	public String getKey() {
		return this.cachedKey;
	}
}
