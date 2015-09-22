package cn.o.app.os;

import android.content.Context;

public class ContextOwner {

	protected Context mContext;

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context context) {
		mContext = context;
	}

}
