package cn.mutils.app.event;

import android.content.Context;
import cn.mutils.app.core.event.Dispatcher;
import cn.mutils.app.os.IContextOwner;

public class ContextOwnerDispathcer extends Dispatcher implements IContextOwner {

	protected Context mContext;

	@Override
	public Context getContext() {
		return mContext;
	}

	@Override
	public void setContext(Context context) {
		mContext = context;
	}

}
