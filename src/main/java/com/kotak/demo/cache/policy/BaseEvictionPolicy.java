package com.kotak.demo.cache.policy;

public abstract class BaseEvictionPolicy<K> implements EvictionPolicy<K> {

    private final CacheOptions cacheOptions;

    public BaseEvictionPolicy(CacheOptions options) {
        this.cacheOptions = options;
    }
}
