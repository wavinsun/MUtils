package cn.o.app.core.sort.comparator;

import java.util.Comparator;
import java.util.Map.Entry;

import cn.o.app.core.sort.ILastAccessItem;
import cn.o.app.core.sort.Order;
import cn.o.app.core.sort.OrderItem;

/**
 * Comparator for {@link ILastAccessItem} in map
 *
 * @param <K>
 */
public class LastAccessItemEntryComparator<K> extends OrderItem implements Comparator<Entry<K, ILastAccessItem>> {

	@Override
	public int compare(Entry<K, ILastAccessItem> lhs, Entry<K, ILastAccessItem> rhs) {
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
