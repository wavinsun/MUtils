package cn.mutils.app.core.sort.comparator;

import java.util.Comparator;

import cn.mutils.app.core.sort.ILastAccessItem;
import cn.mutils.app.core.sort.Order;
import cn.mutils.app.core.sort.OrderItem;

/**
 * Comparator for {@link ILastAccessItem}
 */
public class LastAccessItemComparator extends OrderItem implements Comparator<ILastAccessItem> {

	@Override
	public int compare(ILastAccessItem lhs, ILastAccessItem rhs) {
		long diff = lhs.lastAccess() - rhs.lastAccess();
		if (diff > 0) {
			return mOrder == Order.ASC ? 1 : -1;
		} else if (diff < 0) {
			return mOrder == Order.ASC ? -1 : 1;
		} else {
			return 0;
		}
	}

}
