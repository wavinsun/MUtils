package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import cn.mutils.app.demo.R;
import cn.mutils.app.task.AnimTask;
import cn.mutils.app.task.AnimTask.AnimTaskListener;
import cn.mutils.app.ui.StateView;
import cn.mutils.core.annotation.event.Click;
import cn.mutils.core.annotation.res.FindViewById;
import cn.mutils.core.annotation.res.SetContentView;

@SetContentView(R.layout.view_anim_task)
public class AnimTaskDemoView extends StateView {

    @FindViewById(R.id.go)
    protected TextView mGo;

    protected AnimTask mTask;

    public AnimTaskDemoView(Context context) {
        super(context);
    }

    public AnimTaskDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimTaskDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mTask = new AnimTask();
        mTask.setStepMillis(1000);
        mTask.setSteps(10);
        mTask.addListener(new AnimTaskListener() {

            @Override
            public void onUpdate(AnimTask task, double progress) {
                if (progress < 1) {
                    mGo.setText((task.getSteps() - task.getStep()) + "");
                    mGo.setEnabled(false);
                } else {
                    mGo.setText("go");
                    mGo.setEnabled(true);
                }
            }
        });
    }

    @Click(R.id.go)
    protected void onClickGo() {
        mTask.start();
    }

}
