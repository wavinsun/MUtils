package cn.o.app.core.sort.comparator;

import java.io.File;
import java.util.Comparator;

import cn.o.app.core.sort.Order;
import cn.o.app.core.sort.OrderItem;

/**
 * Comparator for {@link File} uses file last modified time
 */
public class FileModifiedComparator extends OrderItem implements Comparator<File> {

	@Override
	public int compare(File lhs, File rhs) {
		long diff = lhs.lastModified() - rhs.lastModified();
		if (diff > 0) {
			return mOrder == Order.ASC ? 1 : -1;
		} else if (diff < 0) {
			return mOrder == Order.ASC ? -1 : 1;
		} else {
			return 0;
		}
	}

}
