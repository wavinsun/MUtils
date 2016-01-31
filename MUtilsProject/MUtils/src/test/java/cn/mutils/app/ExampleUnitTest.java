package cn.mutils.app;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cn.mutils.app.core.beans.BeanField;
import cn.mutils.app.core.sort.SortUtil;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    public static class P<T> {
        public T t;
    }

    public static class C extends P<ArrayList<Integer>> {
        public HashMap<String, Long> l;
    }

    public static class D extends C {

    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testBeanField() throws Exception {
        BeanField fl = BeanField.getField(D.class, "l");
        Type tl = fl.getRawType();
        System.out.println(tl);
        BeanField ft = BeanField.getField(D.class, "t");
        Type tt = ft.getRawType();
        System.out.println(tt);
    }

    @Test
    public void testListSizeComparator() {
        List<List<Integer>> list = new ArrayList<List<Integer>>();
        for (int i = 0; i < 5; i++) {
            int size = (int) (Math.random() * 10);
            List<Integer> itemList = new LinkedList<Integer>();
            for (int j = size; j >= 0; j--) {
                itemList.add(0);
            }
            list.add(itemList);
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i).size() + " ");
        }
        System.out.println();
        List<List<Integer>> sortedList = SortUtil.sortByListSize(list);
        for (int i = 0; i < sortedList.size(); i++) {
            System.out.print(sortedList.get(i).size() + " ");
        }
        System.out.println();
    }

}