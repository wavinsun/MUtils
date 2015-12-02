package cn.mutils.app.pay;

import cn.mutils.app.core.event.IListener;

public interface AppPayListener extends IListener {

	/**
	 * Pay success
	 * 
	 * @param task
	 */
	public void onComplete(AppPayTask task);

	/**
	 * Pay error
	 * 
	 * @param task
	 * @param e
	 *            It is not null when exception happens
	 */
	public void onError(AppPayTask task, Exception e);

}
