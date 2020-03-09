#### Guava Cache Overview  
Guava cache is excellent simple cache library developed by Google. It is simple in-memory cache implementations.
Note the word simple. It is not cluster aware cache and not suitable for distributed cache systems.
If you are looking for any such cache system which is cluster aware and scale across multiple clusters
then perhaps you can look to Ehcache or Infinispan Distributed Cache. Both are very powerful distrubuted cache
and configurable plus scalable.  
Guava stores the cache in in-memory. In this tutorials we will show how Guava Cache can be implemented.

In **Basic.java**, we have implemented all necessary set of methods, to implement getter and setters of 
cache.  
Code snippet for basic code snippets how to create cache and access it
```
       CacheLoader<String, String> cacheLoader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                // When values not found, create this default value
                   return key.toUpperCase();
            }
        };

       LoadingCache<String, String> cache = CacheBuilder
                .newBuilder()
                .build(cacheLoader);
       // If key doesnt exists, then populate key and default value.
       cache.getUnchecked(key);
       cache.get(key);
       cache.put("key","value")
       cache.getIfPresent(key);
       cache.size();  
```
***
There are various eviction policy:
1. Size based eviction policy: If cache reaches maximum size, then evict oldest items in cache.
2. Weight based eviction policy: If cache reaches maximum weight, then evict oldest items in cache.
3. Expiry after last access time: Key will expire in specified time from last access time.
4. Expiry after last write time: Key will expire after sometime from last write time.

Code snippet for various eviction policy:
```
// Size based eviction policy
LoadingCache<String, String> cache = CacheBuilder
                .newBuilder()
                .maximumSize(3)
                .weigher(weigher)
                .build(loader);

// Weight based eviction policy:
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

// Expire based on access time.
LoadingCache<String, String> cache = CacheBuilder
                .newBuilder()
                .expireAfterAccess(1, TimeUnit.SECONDS)
                .build(cacheLoader);

// Expiry from last write time.
LoadingCache<String, String> cache = CacheBuilder
                .newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .build(cacheLoader);
```

We can also record cache stats and get all necessary metrics like hit-ratio, miss-ratio, loadtime etc.
```
LoadingCache<String, String> cache = CacheBuilder
                .newBuilder()
                .maximumSize(3)
                .recordStats()
                .build(loader);

System.out.println(cache.stats());
```

We implement notification when any entry is evicted.
```
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
``` 








    
