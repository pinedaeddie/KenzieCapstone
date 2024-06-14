package com.kenzie.appserver.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kenzie.appserver.repositories.model.AppointmentRecord;
import java.util.concurrent.TimeUnit;

public class CacheStore {

    private final Cache<String, AppointmentRecord> cache;

    public CacheStore(int expiry, TimeUnit timeUnit) {
        // Initializing the cache with a default expiry time of 120 seconds
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public AppointmentRecord get(String key) {
        return cache.getIfPresent(key);
    }

    public void evict(String key) {
        cache.invalidate(key);
    }

    public void add(String key, AppointmentRecord value) {
        cache.put(key, value);
    }
}