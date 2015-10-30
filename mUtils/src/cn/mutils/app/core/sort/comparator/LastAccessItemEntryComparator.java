package cn.mutils.app.core.sort.comparator;

import java.util.Comparator;
import java.util.Map.Entry;

import cn.mutils.app.core.sort.ILastAccessItem;
import cn.mutils.app.core.sort.Order;
import cn.mutils.app.core.sort.OrderItem;

/**
 * Comparator for {@link ILastAccessItem} in map
 *
 * @param <K>
 */
public class LastAccessItemEntryComparator<K, V extends ILastAccessItem> extends OrderItem
		implements Comparator<Entry<K, V>> {

	@Override
	public int compare(Entry<K, V> lhs, Entry<K, V> rhs) {
		long diff = lhs.getValue().lastAccess() - rhs.getValue().lastAccess();
		if (diff > 0) {
			return mOrder == Order.ASC ? 1 : -1;
		} else if (diff < 0) {
			return mOrder == Order.ASC ? -1 : 1;
		} else {
			return 0;
		}
	}

}
