package cn.o.app.ui.web;

import android.content.Context;
import cn.o.app.core.annotation.Ignore;
import cn.o.app.core.json.JsonUtil;

/**
 * Implements {@link IWebMessageDispatcher}
 *
 * @param <MESSAGE>
 */
@SuppressWarnings("serial")
public abstract class WebMessageDispatcher<MESSAGE> implements IWebMessageDispatcher<MESSAGE> {

	protected IWebMessageManager mManager;

	@Override
	public Context getContext() {
		return mManager == null ? null : mManager.getContext();
	}

	@Ignore
	@Override
	public IWebMessageManager getManager() {
		return mManager;
	}

	@Override
	public void setManager(IWebMessageManager manager) {
		mManager = manager;
	}

	@Override
	public void notifyManager(MESSAGE message) {
		try {
			mManager.onMessage(JsonUtil.convert(message));
		} catch (Exception e) {

		}
	}

}