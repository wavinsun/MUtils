package cn.o.app.core.sort.comparator;

import java.util.Comparator;

import cn.o.app.core.sort.IIndexItem;
import cn.o.app.core.sort.Order;
import cn.o.app.core.sort.OrderItem;

/**
 * Comparator for {@link IIndexItem}
 */
public class IndexItemComparator extends OrderItem implements Comparator<IIndexItem> {

	@Override
	public int compare(IIndexItem lhs, IIndexItem rhs) {
		int diff = lhs.index() - rhs.index();
		if (diff > 0) {
			return mOrder == Order.ASC ? 1 : -1;
		} else if (diff < 0) {
			return mOrder == Order.ASC ? -1 : 1;
		} else {
			return 0;
		}
	}

}
