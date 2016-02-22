package cn.mutils.app.demo.ui;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import cn.mutils.app.demo.R;
import cn.mutils.app.demo.ShellActivity;
import cn.mutils.app.ui.StateView;
import cn.mutils.core.annotation.event.Click;
import cn.mutils.core.annotation.res.FindViewById;
import cn.mutils.core.annotation.res.SetContentView;

@SetContentView(R.layout.view_bluetooth)
public class BluetoothDemoView extends StateView {

    @FindViewById(R.id.log)
    protected TextView mLogText;

    public BluetoothDemoView(Context context) {
        super(context);
    }

    public BluetoothDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BluetoothDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        StringBuilder sb = new StringBuilder();
        sb.append("Name:");
        sb.append(adapter.getName());
        sb.append("\nAddress:");
        sb.append(adapter.getAddress());
        mLogText.setText(sb);
    }

    @Click(R.id.client)
    protected void onClickClient(View v) {
        ShellActivity.ShellExtra extra = new ShellActivity.ShellExtra();
        extra.setTitle(((TextView) v).getText().toString());
        extra.setViewName(BluetoothClientDemoView.class.getName());
        Intent intent = new Intent(getContext(), ShellActivity.class);
        extra.putTo(intent);
        startActivity(intent);
    }

    @Click(R.id.server)
    protected void onClickServer(View v) {
        ShellActivity.ShellExtra extra = new ShellActivity.ShellExtra();
        extra.setTitle(((TextView) v).getText().toString());
        extra.setViewName(BluetoothServerDemoView.class.getName());
        Intent intent = new Intent(getContext(), ShellActivity.class);
        extra.putTo(intent);
        startActivity(intent);
    }


}
