package com.kotak.demo.cache.policy.lru;

import com.kotak.demo.cache.EvictionPolicyType;
import com.kotak.demo.cache.policy.BaseEvictionPolicy;

import java.util.HashMap;
import java.util.Map;

public final class LRUPolicy<K> extends BaseEvictionPolicy<K> {

    public static class Node<K> {
        K key;
        Node<K> next;
        Node<K> prev;

        public Node(K key) {
            this.key = key;
        }
    }

    private final Map<K, Node<K>> keyStore;

    private Node<K> head;
    private Node<K> tail;
    private LRUCacheOptions cacheOptions;

    public LRUPolicy(LRUCacheOptions options) {
        super(options);
        this.keyStore = new HashMap<>();
        this.head = new Node<>(null); // dummy default
        this.tail = new Node<>(null); // dummy default

        head.next = tail;
        tail.prev = head;
        this.cacheOptions = options;
    }

    @Override
    public EvictionPolicyType getCacheType() {
        return EvictionPolicyType.LRU;
    }

    @Override
    public void onAccess(K key) {
        if (keyStore.containsKey(key)) {
            Node<K> node = keyStore.get(key);
            unlinkNode(node);
            addToHead(node);
        }
    }

    @Override
    public void onDelete(K key) {
        if (keyStore.containsKey(key)) {
            Node<K> node = keyStore.get(key);
            unlinkNode(node);
        }
    }

    @Override
    public void onInsert(K key) {
        Node<K> value = new Node<>(key);
        keyStore.put(key, value);
        addToHead(value);
    }

    @Override
    public K evict() {
        if (tail.prev == head) {
            return null;
        }
        Node<K> prev = tail.prev;
        unlinkNode(prev);
        return prev.key;
    }

    @Override
    public boolean shouldEvict() {
        return keyStore.size() >= cacheOptions.getCapacity();
    }

    private void unlinkNode(Node<K> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void addToHead(Node<K> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }
}
