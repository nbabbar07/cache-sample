package com.kotak.demo.cache.policy;

import com.kotak.demo.cache.EvictionPolicyType;

public interface EvictionPolicy<K> {

    EvictionPolicyType getCacheType();

    void onAccess(K key);

    void onDelete(K key);

    void onInsert(K key);

    K evict();

    boolean shouldEvict();
}
