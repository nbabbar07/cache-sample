package com.kotak.demo.cache.manager;

import com.kotak.demo.cache.Cache;
import com.kotak.demo.cache.ConcurrentCache;
import com.kotak.demo.cache.EvictionPolicyType;
import com.kotak.demo.cache.policy.CacheOptions;
import com.kotak.demo.cache.policy.EvictionPolicy;
import com.kotak.demo.cache.policy.lru.LRUCacheOptions;
import com.kotak.demo.cache.policy.lru.LRUPolicy;
import lombok.Getter;

public class CacheManager<K, V> {

    @Getter
    private final Cache<K, V> cache;

    public CacheManager(EvictionPolicyType evictionPolicy, CacheOptions cacheOptions) {
        EvictionPolicy<K> policy = getPolicy(evictionPolicy, cacheOptions);
        cache = new ConcurrentCache<>(policy);
    }

    private EvictionPolicy<K> getPolicy(EvictionPolicyType evictionPolicyType, CacheOptions options) {
        // Using reflections get all impl classes for Evictions policy
        return switch (evictionPolicyType) {
            case LRU -> new LRUPolicy<K>((LRUCacheOptions) options);
        };
    }


}
