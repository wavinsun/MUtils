package cn.o.app.pay;

import cn.o.app.task.ContextOwnerTask;

public class OPayTask extends ContextOwnerTask {

	/** Unknown */
	public static final int STATUS_UNKNOWN = 0;
	/** AliPay success */
	public static final int STATUS_ALI_SUCCESS = 9000;
	/** UnionPay success */
	public static final int STATUS_UP_SUCCESS = 1;
	/** UnionPay failure */
	public static final int STATUS_UP_FAIL = 2;
	/** UnionPay cancel */
	public static final int STATUS_UP_CANCEL = 3;

	protected int mStatus = STATUS_UNKNOWN;

	public int getStatus() {
		return mStatus;
	}

	public void addListener(OPayListener listener) {
		super.addListener(listener);
	}

}
