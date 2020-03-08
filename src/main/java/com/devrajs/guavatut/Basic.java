package com.devrajs.guavatut;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/*
Basically two steps to create a systems:
1) Create a Cacheloader and then create a LoadingCache.
2) Put and Get the cache.
 */
public class Basic {
    // Create cache loader for string, string.
    private CacheLoader<String, String> loader;
    private LoadingCache<String, String> loadingCache;

    private static Basic basic;
    private Basic() {
        loader = new CacheLoader<String, String>() {

            // If key does not exists in cache, then use this function to create value corresponding to key, when cache
            // is queried.
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };
        loadingCache = CacheBuilder.newBuilder().build(loader);
    }

    // We are creating singleton class because in case of multi-threaded environment, multiple threads
    // can cause race condition in single cache.
    synchronized public static Basic getInstance() {
        if (basic == null) {
            basic = new Basic();
        }
        return basic;
    }

    private String fetchSomeValue(String key) {
        return key.toUpperCase() + "_value";
    }

    public long getCacheSize() {
        return loadingCache.size();
    }

    public String getCacheValueOrPutIfKeyDoesNotExistsUnChecked(String key) {
        // If value doesnt exists, then create a new one. If exists return it.
        return loadingCache.getUnchecked(key);
    }

    public Map<String, String> getCacheValuesOfMultipleKeys(List<String> keys) throws ExecutionException {
        return loadingCache.getAll(keys);
    }

    public String getCacheValueOrPutIfKeyDoesNotExists(String key) throws ExecutionException {
        return loadingCache.get(key);
    }

    public void putKeyInCache(String key) {
        loadingCache.put(key, fetchSomeValue(key));
    }

    public void putManyKeysInCacheAtATime(Map<String, String> manyKeys) {
        loadingCache.putAll(manyKeys);
    }

    public String getValueIfPresent(String keydoesntexists) {
        return loadingCache.getIfPresent(keydoesntexists);
    }

    public static void main(String[] args) throws ExecutionException {
        Basic basic = Basic.getInstance();
        // It should print 0.
        System.out.println("Cache size" + basic.getCacheSize());
        basic.putKeyInCache("apple");
        basic.putKeyInCache("apple1");
        basic.putKeyInCache("apple2");
        basic.putKeyInCache("apple3");
        basic.putKeyInCache("apple4");
        basic.putKeyInCache("apple6");

        // It should print 6.
        System.out.println("Current cache size" + basic.getCacheSize());

        System.out.println(basic.getCacheValueOrPutIfKeyDoesNotExists("apple"));
        System.out.println(basic.getCacheValueOrPutIfKeyDoesNotExists("apple7"));
        System.out.println(basic.getCacheValueOrPutIfKeyDoesNotExistsUnChecked("apple8"));
        System.out.println(basic.getValueIfPresent("bla"));
    }
}
