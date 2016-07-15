package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;

import cn.mutils.app.demo.R;
import cn.mutils.app.task.AnimTask;
import cn.mutils.app.task.AnimTask.AnimTaskListener;
import cn.mutils.app.ui.ProgressView;
import cn.mutils.app.ui.StateView;
import cn.mutils.core.annotation.event.Click;
import cn.mutils.core.annotation.res.FindViewById;
import cn.mutils.core.annotation.res.SetContentView;

@SetContentView(R.layout.view_progress_view)
public class ProgressViewDemoView extends StateView {

    @FindViewById(R.id.progress_view)
    protected ProgressView mProgressView;

    protected AnimTask mTask;

    public ProgressViewDemoView(Context context) {
        super(context);
    }

    public ProgressViewDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressViewDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mTask = new AnimTask();
        mTask.setStepMillis(300);
        mTask.setSteps(Long.MAX_VALUE);
        mTask.addListener(new AnimTaskListener() {

            @Override
            public void onUpdate(AnimTask task, double progress) {
                mProgressView.setProgress((mProgressView.getProgress() + 1) % 101);
            }
        });
        mTask.start();
    }

    @Override
    public void onDestroy() {
        mTask.stop();
        super.onDestroy();
    }

    @Click(R.id.shape_ring)
    protected void onClickShapeRing() {
        mProgressView.setShape(ProgressView.SHAPE_RING);
    }

    @Click(R.id.shape_line)
    protected void onClickShapeLine() {
        mProgressView.setShape(ProgressView.SHAPE_LINE);
    }

}
