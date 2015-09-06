package cn.o.app.core.sort;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.o.app.core.sort.comparator.CreationItemComparator;
import cn.o.app.core.sort.comparator.FileLengthComparator;
import cn.o.app.core.sort.comparator.FileModifiedComparator;
import cn.o.app.core.sort.comparator.LastAccessItemComparator;
import cn.o.app.core.sort.comparator.LastAccessItemEntryComparator;
import cn.o.app.core.sort.comparator.PreSortedComparator;

/**
 * Sort utility of framework
 */
public class SortUtil {

	/**
	 * Sort file list by last modified time
	 * 
	 * @param list
	 * @return
	 */
	public static List<File> sortByModified(List<File> list) {
		return sortByModified(list, Order.ASC);
	}

	/**
	 * Sort file list by last modified time for order
	 * 
	 * @param list
	 * @param order
	 * @return
	 */
	public static List<File> sortByModified(List<File> list, Order order) {
		FileModifiedComparator comparator = new FileModifiedComparator();
		comparator.setOrder(order);
		Collections.sort(list, comparator);
		return list;
	}

	/**
	 * Sort file list by file length
	 * 
	 * @param list
	 * @return
	 */
	public static List<File> sortByLength(List<File> list) {
		return sortByLength(list, Order.ASC);
	}

	/**
	 * Sort file list by file length for order
	 * 
	 * @param list
	 * @param order
	 * @return
	 */
	public static List<File> sortByLength(List<File> list, Order order) {
		FileLengthComparator comparator = new FileLengthComparator();
		comparator.setOrder(order);
		Collections.sort(list, comparator);
		return list;
	}

	/**
	 * Sort list by last access time
	 * 
	 * @param list
	 * @return
	 */
	public static List<ILastAccessItem> sortByLastAccess(List<ILastAccessItem> list) {
		return sortByLastAccess(list, Order.ASC);
	}

	/**
	 * Sort list by last access time for order
	 * 
	 * @param list
	 * @param order
	 * @return
	 */
	public static List<ILastAccessItem> sortByLastAccess(List<ILastAccessItem> list, Order order) {
		LastAccessItemComparator comparator = new LastAccessItemComparator();
		comparator.setOrder(order);
		Collections.sort(list, comparator);
		return list;
	}

	/**
	 * Sort list by creation time
	 * 
	 * @param list
	 * @return
	 */
	public static List<ICreationItem> sortByCreation(List<ICreationItem> list) {
		return sortByCreation(list, Order.ASC);
	}

	/**
	 * Sort list by creation time for order
	 * 
	 * @param list
	 * @param order
	 * @return
	 */
	public static List<ICreationItem> sortByCreation(List<ICreationItem> list, Order order) {
		CreationItemComparator comparator = new CreationItemComparator();
		comparator.setOrder(order);
		Collections.sort(list, comparator);
		return list;
	}

	/**
	 * Sort map by LRU algorithm
	 * 
	 * @param map
	 * @return
	 */
	public static <K> Map<K, ILastAccessItem> sortByLru(Map<K, ILastAccessItem> map) {
		return sortByLru(map, Integer.MAX_VALUE);
	}

	/**
	 * Sort map by LRU algorithm for size
	 * 
	 * @param map
	 * @param lruSize
	 * @return
	 */
	public static <K> Map<K, ILastAccessItem> sortByLru(Map<K, ILastAccessItem> map, int lruSize) {
		List<Map.Entry<K, ILastAccessItem>> entryList = new ArrayList<Map.Entry<K, ILastAccessItem>>(map.entrySet());
		LastAccessItemEntryComparator<K> comparator = new LastAccessItemEntryComparator<K>();
		comparator.setOrder(Order.DESC);
		Collections.sort(entryList, comparator);
		HashMap<K, ILastAccessItem> sortedMap = new HashMap<K, ILastAccessItem>();
		for (int i = 0; i < lruSize; i++) {
			Map.Entry<K, ILastAccessItem> entry = entryList.get(i);
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		entryList.clear();
		return sortedMap;
	}

	/**
	 * Sort list by presorted list
	 * 
	 * @param list
	 * @param preSorted
	 * @return
	 */
	public static <E> List<E> sortByPreSorted(List<E> list, List<E> preSorted) {
		return sortByPreSorted(list, preSorted, Order.ASC);
	}

	/**
	 * Sort list by presorted list for order
	 * 
	 * @param list
	 * @param preSorted
	 * @param order
	 * @return
	 */
	public static <E> List<E> sortByPreSorted(List<E> list, List<E> preSorted, Order order) {
		PreSortedComparator<E> comparator = new PreSortedComparator<E>();
		comparator.setOrder(order);
		comparator.setPreSorted(preSorted);
		Collections.sort(list, comparator);
		comparator.setPreSorted(null);
		return list;
	}

}
