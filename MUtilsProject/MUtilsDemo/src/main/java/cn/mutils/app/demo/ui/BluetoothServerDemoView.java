package cn.mutils.app.demo.ui;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.UUID;

import cn.mutils.app.bluetooth.BluetoothConnection;
import cn.mutils.app.demo.R;
import cn.mutils.app.ui.StateView;
import cn.mutils.core.annotation.res.FindViewById;
import cn.mutils.core.annotation.res.SetContentView;
import cn.mutils.core.log.Logs;
import cn.mutils.core.time.DateTime;

@SetContentView(R.layout.view_bluetooth_server)
public class BluetoothServerDemoView extends StateView {

    public static final String SERVER_NAME = "wavinsun";
    public static final UUID SERVER_UUID = UUID.fromString("BC99CD38-2376-4DB9-B8E2-859D611B8411");

    protected BluetoothConnection mConnection;

    @FindViewById(R.id.log)
    protected TextView mLogText;

    protected StringBuilder mLogTextBuffer;

    public BluetoothServerDemoView(Context context) {
        super(context);
    }

    public BluetoothServerDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BluetoothServerDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mConnection = new BluetoothConnection(SERVER_NAME, SERVER_UUID);
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
                String message = new String(bytes);
                mLogTextBuffer.insert(0, "\n");
                mLogTextBuffer.insert(0, message);
                mLogTextBuffer.insert(0, new DateTime().toString() + ":");
                mLogText.setText(mLogTextBuffer);
            }

            @Override
            public void onConnectDevice(BluetoothDevice device) {
                Logs.i("onConnectDevice:" + device);
            }

        });
        mLogTextBuffer = new StringBuilder();
        mConnection.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mConnection.stop();
    }
}
