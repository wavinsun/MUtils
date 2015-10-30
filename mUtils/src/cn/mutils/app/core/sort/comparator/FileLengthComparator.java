package cn.mutils.app.core.sort.comparator;

import java.io.File;
import java.util.Comparator;

import cn.mutils.app.core.sort.Order;
import cn.mutils.app.core.sort.OrderItem;

/**
 * Comparator for {@link File} uses file length
 */
public class FileLengthComparator extends OrderItem implements Comparator<File> {

	@Override
	public int compare(File lhs, File rhs) {
		long diff = lhs.length() - rhs.length();
		if (diff > 0) {
			return mOrder == Order.ASC ? 1 : -1;
		} else if (diff < 0) {
			return mOrder == Order.ASC ? -1 : 1;
		} else {
			return 0;
		}
	}

}
