package cn.o.app.event;

import android.content.Context;
import cn.o.app.context.IContextOwner;
import cn.o.app.core.event.Dispatcher;

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
