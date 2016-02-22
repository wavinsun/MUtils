package cn.mutils.app.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;

import cn.mutils.app.task.ContextOwnerTask;
import cn.mutils.core.ILockable;
import cn.mutils.core.event.IListener;
import cn.mutils.core.task.IStoppableManager;

@SuppressWarnings({"ResourceType", "unused", "UnnecessaryInterfaceModifier"})
public class DiscoveryTask extends ContextOwnerTask implements ILockable {

    public static interface DiscoveryListener extends IListener {

        void onUpdate(List<BluetoothDevice> devices);

        void onComplete(List<BluetoothDevice> devices);

    }

    protected List<BluetoothDevice> mDevices;
    protected BluetoothAdapter mAdapter;
    protected TaskReceiver mReceiver;

    public DiscoveryTask(Context context) {
        setContext(context);
    }

    public void setListener(DiscoveryListener listener) {
        super.setListener(listener);
    }

    @Override
    public boolean isLocked() {
        return true;
    }

    @Override
    public void setLocked(boolean locked) {

    }

    @Override
    protected void onStart() {
        mReceiver = new TaskReceiver();
        mContext.registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        mContext.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        if (mContext instanceof IStoppableManager) {
            ((IStoppableManager) mContext).bind(this);
        }
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mDevices = new ArrayList<BluetoothDevice>(mAdapter.getBondedDevices());
        if (mDevices.size() != 0) {
            DiscoveryListener listener = getListener(DiscoveryListener.class);
            if (listener != null) {
                listener.onUpdate(mDevices);
            }
        }
        if (mAdapter.isDiscovering()) {
            mAdapter.cancelDiscovery();
        }
        mAdapter.startDiscovery();
    }

    @Override
    protected void onStop() {
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }
        if (mAdapter != null && mAdapter.isDiscovering()) {
            mAdapter.cancelDiscovery();
        }
        mReceiver = null;
        mAdapter = null;
    }

    protected void onReceiver(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                mDevices.add(device);
            }
            DiscoveryListener listener = getListener(DiscoveryListener.class);
            if (listener != null) {
                listener.onUpdate(mDevices);
            }
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            DiscoveryListener listener = getListener(DiscoveryListener.class);
            if (listener != null) {
                listener.onComplete(mDevices);
            }
            stop();
        }
    }

    class TaskReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            DiscoveryTask.this.onReceiver(context, intent);
        }

    }


}
