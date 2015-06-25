package cn.o.app.pay;

import cn.o.app.event.Listener;

public interface OPayListener extends Listener {

	// 支付成功
	public void onComplete(OPayTask task);

	// 如果触发异常e不为空
	public void onError(OPayTask task, Exception e);

}
