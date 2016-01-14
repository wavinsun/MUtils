package cn.mutils.app;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import cn.mutils.app.core.beans.BeanField;
import cn.mutils.app.core.json.JsonUtil;
import cn.mutils.app.core.reflect.ReflectUtil;

public class ExampleUnitTest extends ApplicationTestCase<Application> {

    public ExampleUnitTest() {
        super(Application.class);
    }

    public static class P<T> {
        public T t;

        public P() {
            try {
                t = (T) ReflectUtil.getParamRawType(this.getClass(), 0).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class C extends P<ArrayList<Integer>> {
        public HashMap<String, Long> l = new HashMap<String, Long>();
    }

    public static class D extends C {

    }

    public static class E extends ArrayList<D> {

    }

    public static class F extends E {

    }

    public void test() throws Exception {
        BeanField fl = BeanField.getField(D.class, "l");
        Type tl = fl.getRawType();
        System.out.println(tl);
        BeanField ft = BeanField.getField(D.class, "t");
        Type tt = ft.getRawType();
        System.out.println(tt);

        Class<?> rt = ReflectUtil.getCollectionElementRawType(F.class, null);
        Type gt = ReflectUtil.getCollectionElementGenericType(F.class, null);

        F f = new F();
        for (int i = 0; i < 3; i++) {
            f.add(new D());
        }
        String str = JsonUtil.convert(f);
        for (int i = 0; i < 3; i++) {
            JsonUtil.convert(str, F.class);
        }
        C c = new C();
        for (int i = 0; i < 3; i++) {
            c.t.add(i);
        }
        String sc = JsonUtil.convert(c);
        for (int i = 0; i < 3; i++) {
            P<ArrayList<Integer>> p = JsonUtil.convert(sc, P.class, c.getClass().getGenericSuperclass());
            String tStr = JsonUtil.convert(p.t);
            System.out.println(tStr);
        }
        System.out.println("OK");
    }

}