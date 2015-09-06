package cn.o.app.core.sort.comparator;

import java.util.Comparator;

import cn.o.app.core.sort.ICreationItem;
import cn.o.app.core.sort.Order;
import cn.o.app.core.sort.OrderItem;

/**
 * Comparator for {@link ICreationItem}
 */
public class CreationItemComparator extends OrderItem implements Comparator<ICreationItem> {

	@Override
	public int compare(ICreationItem lhs, ICreationItem rhs) {
		long diff = lhs.creation() - rhs.creation();
		if (diff > 0) {
			return mOrder == Order.ASC ? 1 : -1;
		} else if (diff < 0) {
			return mOrder == Order.ASC ? -1 : 1;
		} else {
			return 0;
		}
	}

}
