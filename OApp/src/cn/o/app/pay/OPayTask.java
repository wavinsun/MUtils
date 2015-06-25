package cn.o.app.pay;

import cn.o.app.task.ContextOwnerTask;

public class OPayTask extends ContextOwnerTask {

	public static final int STATUS_UNKNOWN = 0;// 未知
	public static final int STATUS_ALI_SUCCESS = 9000;// 支付宝支付成功
	public static final int STATUS_UP_SUCCESS = 1;// 银联支付成功
	public static final int STATUS_UP_FAIL = 2;// 银联支付失败
	public static final int STATUS_UP_CANCEL = 3;// 银联支付取消

	protected int mStatus = STATUS_UNKNOWN;

	public int getStatus() {
		return mStatus;
	}

	public void addListener(OPayListener listener) {
		super.addListener(listener);
	}

}
