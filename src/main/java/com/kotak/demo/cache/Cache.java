package com.kotak.demo.cache;

public interface Cache<K, V> {

    V get(K key);

    void put(K key, V value);

    V delete(K key);

}
