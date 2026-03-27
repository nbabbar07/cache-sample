package com.kotak.demo;

import com.kotak.demo.cache.Cache;
import com.kotak.demo.cache.EvictionPolicyType;
import com.kotak.demo.cache.manager.CacheManager;
import com.kotak.demo.cache.policy.lru.LRUCacheOptions;

//@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
//		SpringApplication.run(DemoApplication.class, args);

        CacheManager<Integer, String> cacheManager = new CacheManager<>(EvictionPolicyType.LRU, new LRUCacheOptions(3));

        Cache<Integer, String> cache = cacheManager.getCache();

        cache.put(1, "1");
        cache.put(2, "2");
        cache.put(3, "3");
        System.out.println(cache.get(1));
        cache.put(4, "4");
        cache.put(5, "5");
        System.out.println(cache.get(2));
        System.out.println(cache.get(3));
    }

}
