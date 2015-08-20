package cn.o.app.pay;

import cn.o.app.core.event.Listener;

public interface OPayListener extends Listener {

	/**
	 * Pay success
	 * 
	 * @param task
	 */
	public void onComplete(OPayTask task);

	/**
	 * Pay error
	 * 
	 * @param task
	 * @param e
	 *            It is not null when exception happens
	 */
	public void onError(OPayTask task, Exception e);

}
