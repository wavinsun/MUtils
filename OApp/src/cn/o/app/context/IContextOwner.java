package cn.o.app.context;

import android.content.Context;

public interface IContextOwner extends IContextProvider {

	public void setContext(Context context);

}
