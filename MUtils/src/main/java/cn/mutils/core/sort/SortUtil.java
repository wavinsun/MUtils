package cn.mutils.core.sort;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mutils.core.sort.comparator.CreationItemComparator;
import cn.mutils.core.sort.comparator.FileLengthComparator;
import cn.mutils.core.sort.comparator.FileModifiedComparator;
import cn.mutils.core.sort.comparator.IndexItemComparator;
import cn.mutils.core.sort.comparator.LastAccessItemComparator;
import cn.mutils.core.sort.comparator.LastAccessItemEntryComparator;
import cn.mutils.core.sort.comparator.ListSizeComparator;
import cn.mutils.core.sort.comparator.PreSortedComparator;

/**
 * Sort utility of framework
 */
@SuppressWarnings("unused")
public class SortUtil {

    /**
     * Sort file list by last modified time
     */
    public static List<File> sortByModified(List<File> list) {
        return sortByModified(list, Order.ASC);
    }

    /**
     * Sort file list by last modified time for order
     */
    public static List<File> sortByModified(List<File> list, Order order) {
        FileModifiedComparator comparator = new FileModifiedComparator();
        comparator.setOrder(order);
        Collections.sort(list, comparator);
        return list;
    }

    /**
     * Sort file list by file length
     */
    public static List<File> sortByLength(List<File> list) {
        return sortByLength(list, Order.ASC);
    }

    /**
     * Sort file list by file length for order
     */
    public static List<File> sortByLength(List<File> list, Order order) {
        FileLengthComparator comparator = new FileLengthComparator();
        comparator.setOrder(order);
        Collections.sort(list, comparator);
        return list;
    }

    /**
     * Sort list by last access time
     */
    public static <E extends ILastAccessItem> List<E> sortByLastAccess(List<E> list) {
        return sortByLastAccess(list, Order.ASC);
    }

    /**
     * Sort list by last access time for order
     */
    public static <E extends ILastAccessItem> List<E> sortByLastAccess(List<E> list, Order order) {
        LastAccessItemComparator comparator = new LastAccessItemComparator();
        comparator.setOrder(order);
        Collections.sort(list, comparator);
        return list;
    }

    /**
     * Sort list by creation time
     */
    public static <E extends ICreationItem> List<E> sortByCreation(List<E> list) {
        return sortByCreation(list, Order.ASC);
    }

    /**
     * Sort list by creation time for order
     */
    public static <E extends ICreationItem> List<E> sortByCreation(List<E> list, Order order) {
        CreationItemComparator comparator = new CreationItemComparator();
        comparator.setOrder(order);
        Collections.sort(list, comparator);
        return list;
    }

    /**
     * Sort map by LRU algorithm
     */
    public static <K, V extends ILastAccessItem> Map<K, V> sortByLru(Map<K, V> map) {
        return sortByLru(map, Integer.MAX_VALUE);
    }

    /**
     * Sort map by LRU algorithm for size
     */
    public static <K, V extends ILastAccessItem> Map<K, V> sortByLru(Map<K, V> map, int lruSize) {
        List<Map.Entry<K, V>> entryList = new ArrayList<Map.Entry<K, V>>(map.entrySet());
        LastAccessItemEntryComparator<K, V> comparator = new LastAccessItemEntryComparator<K, V>();
        comparator.setOrder(Order.DESC);
        Collections.sort(entryList, comparator);
        HashMap<K, V> sortedMap = new HashMap<K, V>();
        for (int i = 0, size = entryList.size(); i < size && i < lruSize; i++) {
            Map.Entry<K, V> entry = entryList.get(i);
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        entryList.clear();
        return sortedMap;
    }

    /**
     * Sort list by presorted list
     */
    public static <E> List<E> sortByPreSorted(List<E> list, List<?> preSorted) {
        return sortByPreSorted(list, preSorted, Order.ASC);
    }

    /**
     * Sort list by presorted list for order
     */
    public static <E> List<E> sortByPreSorted(List<E> list, List<?> preSorted, Order order) {
        PreSortedComparator<E> comparator = new PreSortedComparator<E>();
        comparator.setOrder(order);
        comparator.setPreSorted(preSorted);
        Collections.sort(list, comparator);
        comparator.clear();
        return list;
    }

    /**
     * Sort list by index
     */
    public static <E extends IIndexItem> List<E> sortByIndex(List<E> list) {
        return sortByIndex(list, Order.ASC);
    }

    /**
     * Sort list by index for order
     */
    public static <E extends IIndexItem> List<E> sortByIndex(List<E> list, Order order) {
        IndexItemComparator comparator = new IndexItemComparator();
        comparator.setOrder(order);
        Collections.sort(list, comparator);
        return list;
    }

    /**
     * Sort list by list item size
     */
    public static <E> List<List<E>> sortByListSize(List<List<E>> list) {
        return sortByListSize(list, Order.ASC);
    }

    /**
     * Sort list list item size for order
     */
    public static <E> List<List<E>> sortByListSize(List<List<E>> list, Order order) {
        ListSizeComparator comparator = new ListSizeComparator();
        comparator.setOrder(order);
        Collections.sort(list, comparator);
        return list;
    }

}
