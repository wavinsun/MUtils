package cn.o.app.core.sort.comparator;

import java.util.Comparator;
import java.util.List;

import cn.o.app.core.sort.Order;
import cn.o.app.core.sort.OrderItem;

/**
 * Comparator for presorted list
 *
 * @param <E>
 */
public class PreSortedComparator<E> extends OrderItem implements Comparator<E> {

	protected List<E> mPreSorted;

	public List<E> getPreSorted() {
		return mPreSorted;
	}

	public void setPreSorted(List<E> preSorted) {
		mPreSorted = preSorted;
	}

	@Override
	public int compare(E lhs, E rhs) {
		int diff = mPreSorted.indexOf(lhs) - mPreSorted.indexOf(rhs);
		if (diff > 0) {
			return mOrder == Order.ASC ? 1 : -1;
		} else if (diff < 0) {
			return mOrder == Order.ASC ? -1 : 1;
		} else {
			return 0;
		}
	}

}
