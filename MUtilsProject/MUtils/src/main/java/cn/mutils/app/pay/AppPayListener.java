package cn.mutils.app.pay;

import cn.mutils.app.core.event.IListener;

public interface AppPayListener extends IListener {

    /**
     * Pay success
     */
    void onComplete(AppPayTask task);

    /**
     * Pay error
     */
    void onError(AppPayTask task, Exception e);

}
