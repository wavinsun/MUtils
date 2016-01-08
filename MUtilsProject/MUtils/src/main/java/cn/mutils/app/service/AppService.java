package cn.mutils.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.LinkedList;
import java.util.List;

import cn.mutils.app.core.task.IStoppable;
import cn.mutils.app.core.task.IStoppableManager;
import cn.mutils.app.net.INetQueue;
import cn.mutils.app.net.INetQueueOwner;
import cn.mutils.app.net.INetTask;
import cn.mutils.app.net.NetQueue;
import cn.mutils.app.ui.core.UICore;

@SuppressWarnings("deprecation")
public class AppService extends Service implements INetQueueOwner,
        IStoppableManager {

    protected NetQueue mNetQueue;

    protected List<IStoppable> mBindStopables;

    public void bind(IStoppable stoppable) {
        UICore.bind(this, stoppable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNetQueue = new NetQueue();
        mNetQueue.setContext(this);
    }

    @Override
    public void onDestroy() {
        stopAll(true);
        mNetQueue.clear();
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        stopAll();
    }

    @Override
    public INetQueue getNetQueue() {
        return mNetQueue;
    }

    public void add(INetTask<?, ?> task) {
        this.bind(task);
        mNetQueue.add(task);
    }

    @Override
    public void stopAll() {
        UICore.stopAll(this);
    }

    public void stopAll(boolean includeLockable) {
        UICore.stopAll(this, includeLockable);
    }

    @Override
    public List<IStoppable> getBindStoppables() {
        if (mBindStopables == null) {
            mBindStopables = new LinkedList<IStoppable>();
        }
        return mBindStopables;
    }

}
