package cn.mutils.app.demo.ui;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.mutils.app.bluetooth.DiscoveryTask;
import cn.mutils.app.core.annotation.event.OnClick;
import cn.mutils.app.core.annotation.res.FindViewById;
import cn.mutils.app.core.annotation.res.SetContentView;
import cn.mutils.app.demo.R;
import cn.mutils.app.ui.AppActivity;
import cn.mutils.app.ui.StateView;
import cn.mutils.app.ui.adapter.IItemView;
import cn.mutils.app.ui.adapter.ItemView;
import cn.mutils.app.ui.adapter.UIAdapter;

@SetContentView(R.layout.view_bluetooth_discovery)
public class BluetoothDiscoveryDemoView extends StateView {

    @FindViewById(R.id.list)
    protected ListView mListView;

    protected DiscoveryAdapter mAdapter;

    public BluetoothDiscoveryDemoView(Context context) {
        super(context);
    }

    public BluetoothDiscoveryDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BluetoothDiscoveryDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAdapter = new DiscoveryAdapter();
        mListView.setAdapter(mAdapter);
        Context context = getContext();
        DiscoveryTask task = new DiscoveryTask(context);
        task.setListener(new DiscoveryTask.DiscoveryListener() {

            @Override
            public void onUpdate(List<BluetoothDevice> devices) {
                mAdapter.setDataProvider(devices);
            }

            @Override
            public void onComplete(List<BluetoothDevice> devices) {
                Context context = getContext();
                if (context instanceof AppActivity) {
                    ((AppActivity) context).setBusy(false);
                }
            }
        });
        task.start();
        if (context instanceof AppActivity) {
            ((AppActivity) context).setBusy(true);
        }
    }

    class DiscoveryAdapter extends UIAdapter<BluetoothDevice> {

        @Override
        public IItemView<BluetoothDevice> getItemView() {
            return new DiscoveryItemView(getContext());
        }
    }

    @SetContentView(R.layout.item_bluetooth_device)
    class DiscoveryItemView extends ItemView<BluetoothDevice> {

        @FindViewById(R.id.name)
        protected TextView mNameText;

        @FindViewById(R.id.address)
        protected TextView mAddressText;

        public DiscoveryItemView(Context context) {
            super(context);
        }

        @Override
        public void onResume() {
            mNameText.setText("Name: " + mDataProvider.getName());
            mAddressText.setText("Address: " + mDataProvider.getAddress());
        }

        @OnClick
        protected void onClick() {
            Context context = getContext();
            if (!(context instanceof Activity)) {
                return;
            }
            Activity activity = (Activity) context;
            Intent intent = new Intent();
            intent.putExtra("address", mDataProvider.getAddress());
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }

    }

}
