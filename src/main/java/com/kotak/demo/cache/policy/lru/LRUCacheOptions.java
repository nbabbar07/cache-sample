package com.kotak.demo.cache.policy.lru;

import com.kotak.demo.cache.policy.CacheOptions;
import lombok.Getter;

@Getter
public class LRUCacheOptions implements CacheOptions {

    private int capacity;

    public LRUCacheOptions(int capacity) {
        this.capacity = capacity;
    }
}
