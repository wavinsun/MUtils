package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

import cn.mutils.app.demo.R;
import cn.mutils.app.ui.StateView;
import cn.mutils.core.annotation.event.Click;
import cn.mutils.core.annotation.res.SetContentView;
import cn.mutils.core.beans.BeanCache;
import cn.mutils.core.log.Logs;

@SetContentView(R.layout.view_test)
public class TestDemoView extends StateView {

    public TestDemoView(Context context) {
        super(context);
    }

    public TestDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Click(R.id.go)
    protected void onClickGo() {
        TestClass t = new TestClass();
        BeanCache cache = new BeanCache(t);
        List<String> changed = cache.fromTarget();
        t.intValue = 1;
        t.doubleValue = 2;
        t.stringValue = "123";
        List<String> changedList = cache.toTarget();
        Logs.d(changedList + "");
    }


    public static class TestClass {
        public int intValue;
        public String stringValue;
        public double doubleValue;
    }


}
