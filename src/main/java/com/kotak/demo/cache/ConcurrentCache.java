package com.kotak.demo.cache;

import com.kotak.demo.cache.policy.EvictionPolicy;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentCache<K, V> implements Cache<K, V> {

    private final Map<K, V> dataStore;
    private final EvictionPolicy<K> evictionPolicy;
    private final ReentrantLock policyLock;

    public ConcurrentCache(EvictionPolicy<K> evictionPolicy) {
        this.evictionPolicy = evictionPolicy;
        this.dataStore = new ConcurrentHashMap<>();
        this.policyLock = new ReentrantLock();
    }

    @Override
    public V get(K key) {
        V v = dataStore.get(key);
        if (Objects.nonNull(v)) {
            policyLock.lock();
            try {
                evictionPolicy.onAccess(key);
                checkAndEvict();
            } finally {
                policyLock.unlock();
            }
        }
        return v;
    }

    @Override
    public void put(K key, V value) {
        // null check
        policyLock.lock();
        try {
            if (dataStore.containsKey(key)) {
                dataStore.put(key, value);
                evictionPolicy.onAccess(key);
                return;
            }
            checkAndEvict();
            dataStore.put(key, value);
            evictionPolicy.onInsert(key);
        } finally {
            policyLock.unlock();
        }
    }

    @Override
    public V delete(K key) {
        V removed = dataStore.remove(key);
        if (Objects.nonNull(removed)) {
            policyLock.lock();
            try {
                evictionPolicy.onDelete(key);
            } finally {
                policyLock.unlock();
            }
        }
        return removed;
    }

    private void checkAndEvict() {
        if (evictionPolicy.shouldEvict()) {
            K keyToEvict = evictionPolicy.evict();
            if (Objects.nonNull(keyToEvict)) {
                dataStore.remove(keyToEvict);
            }
        }
    }
}
