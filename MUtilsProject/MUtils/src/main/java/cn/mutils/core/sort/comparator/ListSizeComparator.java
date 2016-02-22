package cn.mutils.core.sort.comparator;

import java.util.Comparator;
import java.util.List;

import cn.mutils.core.sort.Order;
import cn.mutils.core.sort.OrderItem;

/**
 * Comparator for List item by size of List
 */
public class ListSizeComparator extends OrderItem implements Comparator<List<?>> {

    @Override
    public int compare(List<?> lhs, List<?> rhs) {
        int diff = lhs.size() - rhs.size();
        if (diff > 0) {
            return mOrder == Order.ASC ? 1 : -1;
        } else if (diff < 0) {
            return mOrder == Order.ASC ? -1 : 1;
        } else {
            return 0;
        }
    }

}
