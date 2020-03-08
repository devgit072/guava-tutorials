package com.devrajs.guavatut;

// Using CacheStats we can record and store the stats, like hits, miss, etc.

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class CacheStatsImpl {

    void cacheStats() {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };

        LoadingCache<String, String> cache = CacheBuilder
                .newBuilder()
                .maximumSize(3)
                .recordStats()
                .build(loader);

        cache.getUnchecked("apple1");
        cache.getUnchecked("apple1");
        cache.getUnchecked("apple2");
        cache.getUnchecked("apple3");
        cache.getUnchecked("apple4");
        cache.getUnchecked("apple5");
        cache.getUnchecked("apple6");
        cache.getUnchecked("apple6");

        System.out.println(cache.stats());
    }

    public static void main(String[] args) {
        CacheStatsImpl stats = new CacheStatsImpl();
        stats.cacheStats();
    }
}
