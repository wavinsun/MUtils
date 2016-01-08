package cn.mutils.app.pay;

import cn.mutils.app.core.event.IListener;

public interface AppPayListener extends IListener {

    /**
     * Pay success
     */
    public void onComplete(AppPayTask task);

    /**
     * Pay error
     */
    public void onError(AppPayTask task, Exception e);

}
