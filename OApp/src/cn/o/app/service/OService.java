package cn.o.app.service;

import java.util.LinkedList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import cn.o.app.net.INetQueue;
import cn.o.app.net.INetQueueOwner;
import cn.o.app.net.INetTask;
import cn.o.app.net.NetQueue;
import cn.o.app.task.IStopable;
import cn.o.app.task.IStopableManager;
import cn.o.app.ui.core.UICore;

@SuppressWarnings("deprecation")
public class OService extends Service implements INetQueueOwner,
		IStopableManager {

	protected NetQueue mNetQueue;

	protected List<IStopable> mBindStopables;

	public void bind(IStopable stopable) {
		UICore.bind(this, stopable);
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
	public List<IStopable> getBindStopables() {
		if (mBindStopables == null) {
			mBindStopables = new LinkedList<IStopable>();
		}
		return mBindStopables;
	}

}
