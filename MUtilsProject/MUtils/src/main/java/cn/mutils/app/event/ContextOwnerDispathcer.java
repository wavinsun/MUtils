package cn.mutils.app.event;

import android.content.Context;

import cn.mutils.app.os.IContextOwner;
import cn.mutils.core.event.Dispatcher;

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
