package cn.mutils.app.demo.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.mutils.app.bluetooth.BluetoothConnection;
import cn.mutils.app.demo.R;
import cn.mutils.app.demo.ShellActivity;
import cn.mutils.app.event.listener.OnActivityResultListener;
import cn.mutils.app.ui.StateView;
import cn.mutils.core.annotation.event.Click;
import cn.mutils.core.annotation.res.FindViewById;
import cn.mutils.core.annotation.res.SetContentView;
import cn.mutils.core.log.Logs;


@SetContentView(R.layout.view_bluetooth_client)
public class BluetoothClientDemoView extends StateView {

    public static final int REQUEST_BLUETOOTH_DEVICE = 1000;

    protected BluetoothConnection mConnection;

    @FindViewById(R.id.discovery)
    protected TextView mDiscoveryButton;

    @FindViewById(R.id.message)
    protected EditText mMessageText;

    public BluetoothClientDemoView(Context context) {
        super(context);
    }

    public BluetoothClientDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BluetoothClientDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.addOnActivityResultListener(new OnActivityResultListener() {

            @Override
            public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
                if (requestCode == REQUEST_BLUETOOTH_DEVICE) {
                    if (resultCode == Activity.RESULT_OK) {
                        String address = data.getStringExtra("address");
                        mDiscoveryButton.setVisibility(View.INVISIBLE);
                        mConnection.connect(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address));
                    }
                }
            }

        });
        mConnection = new BluetoothConnection(BluetoothServerDemoView.SERVER_NAME,
                BluetoothServerDemoView.SERVER_UUID);
        mConnection.setListener(new BluetoothConnection.BluetoothConnectionListener() {

            @Override
            public void onStateChange(int state) {
                switch (state) {
                    case BluetoothConnection.STATE_CONNECTED:
                        Logs.i("onStateChange:CONNECTED");
                        break;
                    case BluetoothConnection.STATE_CONNECTING:
                        Logs.i("onStateChange:CONNECTING");
                        break;
                    case BluetoothConnection.STATE_LISTEN:
                        Logs.i("onStateChange:LISTEN");
                        break;
                    case BluetoothConnection.STATE_NONE:
                        Logs.i("onStateChange:NONE");
                        break;
                }
            }

            @Override
            public void onWrite(byte[] bytes) {
                Logs.i("onWrite:" + bytes);
            }

            @Override
            public void onRead(byte[] bytes) {
                Logs.i("onRead:" + bytes);
            }

            @Override
            public void onConnectDevice(BluetoothDevice device) {
                Logs.i("onConnectDevice:" + device);
            }

        });
    }

    @Click(R.id.discovery)
    protected void onClickDiscovery(View v) {
        ShellActivity.ShellExtra extra = new ShellActivity.ShellExtra();
        extra.setTitle(((TextView) v).getText().toString());
        extra.setViewName(BluetoothDiscoveryDemoView.class.getName());
        Intent intent = new Intent(getContext(), ShellActivity.class);
        extra.putTo(intent);
        startActivityForResult(intent, REQUEST_BLUETOOTH_DEVICE);
    }

    @Click(R.id.send)
    protected void onClickSend() {
        if (mConnection.getState() != BluetoothConnection.STATE_CONNECTED) {
            toast("Not Connected");
            return;
        }
        mConnection.write(mMessageText.getText().toString().getBytes());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mConnection.stop();
    }
}
