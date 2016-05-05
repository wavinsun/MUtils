package cn.mutils.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.test.ApplicationTestCase;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import cn.mutils.app.util.AppUtil;
import cn.mutils.core.beans.BeanField;
import cn.mutils.core.json.JsonUtil;
import cn.mutils.core.reflect.ReflectUtil;

public class AndroidUnitTest extends ApplicationTestCase<Application> {

    public AndroidUnitTest() {
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
        testBeanField();
    }

    protected void testAppUtil() throws Exception {
        Thread sub = new Thread() {
            @Override
            public void run() {
                boolean isMain = AppUtil.isMainThread();
                System.out.println("Sub thread:" + isMain);
            }
        };
        sub.start();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                boolean isMain = AppUtil.isMainThread();
                System.out.println("Main thread:" + isMain);
            }
        });
        Thread.sleep(100000);
    }

    protected void testBeanField() throws Exception {
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
        String str = JsonUtil.toString(f);
        for (int i = 0; i < 3; i++) {
            JsonUtil.fromString(str, F.class);
        }
        C c = new C();
        for (int i = 0; i < 3; i++) {
            c.t.add(i);
        }
        String sc = JsonUtil.toString(c);
        for (int i = 0; i < 3; i++) {
            P<ArrayList<Integer>> p = JsonUtil.fromString(sc, P.class, c.getClass().getGenericSuperclass());
            String tStr = JsonUtil.toString(p.t);
            System.out.println(tStr);
        }
    }

}