package com.devrajs.guavatut;

// Eviction policy is rules which cache follows to determine which key to remove when it is at capacity.

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;

import java.util.concurrent.TimeUnit;

public class Eviction {

    void evictionBySizeOfCache() {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                return fetchSomeValue(key);
            }
        };
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(3).build(loader);

        System.out.println("Expected cache size: 0, Actual cache size: " + cache.size());
        cache.put("apple0", fetchSomeValue("apple0"));
        cache.put("apple1", fetchSomeValue("apple1"));
        cache.put("apple2", fetchSomeValue("apple2"));
        cache.put("apple3", fetchSomeValue("apple3"));

        System.out.println("Cache size should be 3, actual size: " + cache.size());

        // apple0 should not exists, since it would be evicted by eviction policy.
        System.out.println(cache.getIfPresent("apple0"));
    }

    // Sometime we set the capacity of cache with some weight rather than size of cache.
    // Yeah, many times, rather than just the number of entries in cache may be not a good way to determine the
    // the capacity of cache. Like say a key of length 5000 may full the cache. So in such cases, weight will be better
    // way to set the capacity of cache.
    // When cache reaches its maximum weight allocated, it will evict oldest items.
    void evictionByWeight() {
        int len = 0;
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                return fetchSomeValue(key);
            }
        };

        // This determines the size of weigh of key and value pair.
        // Each insertion of key and value pair will increase weigh of cache.
        Weigher<String, String> weigher = new Weigher<String, String>() {
            @Override
            public int weigh(String key, String value) {
                return key.length() + value.length();
            }
        };

        LoadingCache<String, String> cache = CacheBuilder
                .newBuilder()
                .maximumWeight(30)
                .weigher(weigher)
                .build(loader);

        cache.put("apple", "appleV");
        cache.put("apple1", "appleV1");

        // Cache already reached maximum weight of 30, because each put is weight of length of key plus value.
        // After putting below elements, cache will evict the oldest items
        cache.put("apple2", "appleV2");

        System.out.println(cache.getIfPresent("apple"));
        System.out.println(cache.getIfPresent("apple2"));
    }

    // Apart from cache size and cache weight, keys can be evicted based on time expiry of keys.
    void evictionByTimeExpiryByAccess() throws InterruptedException {
        CacheLoader<String, String> cacheLoader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };

        LoadingCache<String, String> cache = CacheBuilder
                .newBuilder()
                .expireAfterAccess(1, TimeUnit.SECONDS)
                .build(cacheLoader);

        cache.put("apple", "apple");
        cache.getUnchecked("apple2");
        cache.getUnchecked("apple3");
        Thread.sleep(750);
        System.out.println(cache.getIfPresent("apple2"));
        Thread.sleep(250);

        // apple2 is recently accessed, so it doesnt expired yet.
        System.out.println(cache.getIfPresent("apple2"));
        // apple3 is expired since it was not accessed in 1 second window.
        System.out.println(cache.getIfPresent("apple3"));
        Thread.sleep(1000);
        System.out.println(cache.getIfPresent("apple2"));
    }

    void evictionByTimeExpiryByWrite() throws InterruptedException {
        CacheLoader<String, String> cacheLoader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };

        LoadingCache<String, String> cache = CacheBuilder
                .newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .build(cacheLoader);

        cache.getUnchecked("apple1");
        cache.getUnchecked("apple2");
        Thread.sleep(500);
        cache.getUnchecked("apple3");
        Thread.sleep(600);
        // apple1 is expired after 1100 ms.
        System.out.println(cache.getIfPresent("apple1"));
        System.out.println(cache.getIfPresent("apple3"));
    }

    private String fetchSomeValue(String key) {
        return key.toUpperCase() + "_value";
    }

    public static void main(String[] args) throws InterruptedException {
        Eviction eviction = new Eviction();
        //eviction.evictionBySizeOfCache();
        eviction.evictionByTimeExpiryByWrite();
    }
}
