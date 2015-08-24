package cn.o.app.core.io;

import android.support.v4.util.LruCache;

/**
 * LRU memory cache of framework
 */
public class Lru<K, V> extends LruCache<K, V> {

	public Lru(int maxSize) {
		super(maxSize);
	}

}
