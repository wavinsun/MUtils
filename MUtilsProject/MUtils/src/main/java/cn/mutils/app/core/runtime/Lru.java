package cn.mutils.app.core.runtime;

import android.support.v4.util.LruCache;

/**
 * LRU memory cache of framework
 */
@SuppressWarnings("unused")
public class Lru<K, V> extends LruCache<K, V> {

    public Lru(int maxSize) {
        super(maxSize);
    }

}
