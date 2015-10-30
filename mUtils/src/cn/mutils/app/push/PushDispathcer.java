package cn.mutils.app.push;

import android.content.Context;
import cn.mutils.app.core.annotation.Ignore;

/**
 * Implements {@link IPushDispatcher}
 */
@SuppressWarnings("serial")
public abstract class PushDispathcer<MESSAGE> implements IPushDispatcher<MESSAGE> {

	protected IPushManager mManager;

	@Override
	public Context getContext() {
		return mManager == null ? null : mManager.getContext();
	}

	@Ignore
	@Override
	public IPushManager getManager() {
		return mManager;
	}

	@Override
	public void setManager(IPushManager manager) {
		mManager = manager;
	}

}
