package cn.mutils.app.os;

import android.content.Context;

public interface IContextOwner extends IContextProvider {

    void setContext(Context context);

}
