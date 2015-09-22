package cn.o.app.os;

import android.content.Context;

public interface IContextOwner extends IContextProvider {

	public void setContext(Context context);

}
