package cn.mutils.app.core.sort.comparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mutils.app.core.IClearable;
import cn.mutils.app.core.sort.Order;
import cn.mutils.app.core.sort.OrderItem;

/**
 * Comparator for presorted list
 *
 * @param <E>
 */
@SuppressWarnings("UnnecessaryUnboxing")
public class PreSortedComparator<E> extends OrderItem implements Comparator<E>, IClearable {

    protected List<?> mPreSorted;

    protected Map<E, Integer> mPreSortedMap;

    public List<?> getPreSorted() {
        return mPreSorted;
    }

    public void setPreSorted(List<?> preSorted) {
        mPreSorted = preSorted;
        if (mPreSortedMap == null) {
            mPreSortedMap = new HashMap<E, Integer>();
        }
    }

    @Override
    public int compare(E lhs, E rhs) {
        int diff = getPreSortedIndexOf(lhs) - getPreSortedIndexOf(rhs);
        if (diff > 0) {
            return mOrder == Order.ASC ? 1 : -1;
        } else if (diff < 0) {
            return mOrder == Order.ASC ? -1 : 1;
        } else {
            return 0;
        }
    }

    /**
     * Get index of e in presorted list
     */
    protected int getPreSortedIndexOf(E e) {
        int index = -1;
        Integer indexInteger = mPreSortedMap.get(e);
        if (indexInteger == null) {
            index = mPreSorted.indexOf(e);
            mPreSortedMap.put(e, index);
        } else {
            index = indexInteger.intValue();
        }
        return index;
    }

    @Override
    public void clear() {
        if (mPreSortedMap != null) {
            mPreSortedMap.clear();
            mPreSortedMap = null;
        }
        mPreSorted = null;
    }

}
