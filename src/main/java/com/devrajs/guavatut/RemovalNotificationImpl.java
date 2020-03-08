package com.devrajs.guavatut;

// Implement notification when any items in cache in get removed.

import com.google.common.cache.*;

public class RemovalNotificationImpl {

    void implementNotification() {
        CacheLoader<String, String> cacheLoader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };

        RemovalListener<String, String> listener = new RemovalListener<String, String>() {
            @Override
            public void onRemoval(RemovalNotification<String, String> notification) {
                if (notification.wasEvicted()) {
                    System.out.println("Evicted, eviction policy: " + notification.getCause());
                }
            }
        };

        LoadingCache<String, String> cache = CacheBuilder
                .newBuilder()
                .maximumSize(3)
                .removalListener(listener)
                .build(cacheLoader);

        cache.getUnchecked("apple1");
        cache.getUnchecked("apple2");
        cache.getUnchecked("apple3");
        cache.getUnchecked("apple4");
        cache.getUnchecked("apple4");
        cache.getUnchecked("apple5");
        cache.getUnchecked("apple5");

        System.out.println(cache.stats());
    }

    public static void main(String[] args) {
        RemovalNotificationImpl obj = new RemovalNotificationImpl();
        obj.implementNotification();
    }
}
