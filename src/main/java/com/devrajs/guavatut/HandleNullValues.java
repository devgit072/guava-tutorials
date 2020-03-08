package com.devrajs.guavatut;

// We cant add null values in the guava cache, else it will throw exceptions.
// However using Optional class we can achieve something similar, if null value mean something to you.

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class HandleNullValues {
    void handleNullValue() {
        CacheLoader<String, Optional<String>> loader = new CacheLoader<String, Optional<String>>() {
            @Override
            public Optional<String> load(String key) throws Exception {
                return Optional.fromNullable(calculateValue(key));
            }
        };

        LoadingCache<String, Optional<String>> cache = CacheBuilder.newBuilder().build(loader);

        cache.getUnchecked("apple1");
        cache.getUnchecked("apple2");
        cache.getUnchecked("apple3");
        cache.getUnchecked("apple4");

        // This will handle the null value.
        cache.getUnchecked("");

        System.out.println(cache.size());

        Optional<String> value1 = cache.getIfPresent("apple1");
        System.out.println(value1.orNull());

        // This will print null.
        System.out.println(cache.getIfPresent("").orNull());
    }
    private String calculateValue(String key) {
        if (key.equals("")) {
            return null;
        }
        return key.toUpperCase();
    }

    public static void main(String[] args) {
        HandleNullValues handleNullValues = new HandleNullValues();
        handleNullValues.handleNullValue();
    }
}
