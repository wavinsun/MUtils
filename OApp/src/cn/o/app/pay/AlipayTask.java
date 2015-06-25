package cn.o.app.pay;

import java.net.URLEncoder;

import android.app.Activity;
import android.os.AsyncTask;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.SignUtils;

//支付宝支付
public class AlipayTask extends OPayTask {
	protected String mPartner;

	protected String mSeller;

	protected String mRsaPrivate;

	protected String mOutTradeNo;

	protected String mSubject;

	protected String mBody;

	protected String mTotalFee;

	protected String mNotifyUrl;

	protected AlipayAsyncTask mTask;

	public String getPartner() {
		return mPartner;
	}

	public void setPartner(String partner) {
		if (mStarted || mStoped) {
			return;
		}
		mPartner = partner;
	}

	public String getSeller() {
		return mSeller;
	}

	public void setSeller(String seller) {
		if (mStarted || mStoped) {
			return;
		}
		mSeller = seller;
	}

	public String getRsaPrivate() {
		return mRsaPrivate;
	}

	public void setRsaPrivate(String rsaPrivate) {
		if (mStarted || mStoped) {
			return;
		}
		mRsaPrivate = rsaPrivate;
	}

	public String getOutTradeNo() {
		return mOutTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		if (mStarted || mStoped) {
			return;
		}
		mOutTradeNo = outTradeNo;
	}

	public String getSubject() {
		return mSubject;
	}

	public void setSubject(String subject) {
		if (mStarted || mStoped) {
			return;
		}
		mSubject = subject;
	}

	public String getBody() {
		return mBody;
	}

	public void setBody(String body) {
		if (mStarted || mStoped) {
			return;
		}
		mBody = body;
	}

	public String getTotalFee() {
		return mTotalFee;
	}

	public void setTotalFee(String totalFee) {
		if (mStarted || mStoped) {
			return;
		}
		mTotalFee = totalFee;
	}

	public String getNotifyUrl() {
		return mNotifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		if (mStarted || mStoped) {
			return;
		}
		mNotifyUrl = notifyUrl;
	}

	@Override
	protected void onStart() {
		this.mTask = new AlipayAsyncTask();
		this.mTask.execute();
	}

	protected void onStop() {
		if (this.mTask != null) {
			this.mTask.cancel(true);
		}
		mTask = null;
	}

	/**
	 * create the order info. 创建订单信息
	 */
	protected static String getOrderInfo(String partner, String seller,
			String outTradeNo, String subject, String body, String totalFee,
			String notifyUrl) {
		// 合作者身份ID
		String orderInfo = "partner=" + "\"" + partner + "\"";

		// 卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + seller + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + outTradeNo + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + totalFee + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + notifyUrl + "\"";

		// 接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	class AlipayAsyncTask extends AsyncTask<String, Integer, Object> {

		@Override
		protected Object doInBackground(String... params) {
			try {
				if (mContext == null) {
					throw new NullPointerException();
				}
				if (mPartner == null) {
					throw new NullPointerException();
				}
				if (mSeller == null) {
					throw new NullPointerException();
				}
				if (mRsaPrivate == null) {
					throw new NullPointerException();
				}
				if (mOutTradeNo == null) {
					throw new NullPointerException();
				}
				if (mSubject == null) {
					throw new NullPointerException();
				}
				if (mBody == null) {
					throw new NullPointerException();
				}
				if (mTotalFee == null) {
					throw new NullPointerException();
				}
				if (mNotifyUrl == null) {
					throw new NullPointerException();
				}
				String orderInfo = getOrderInfo(mPartner, mSeller, mOutTradeNo,
						mSubject, mBody, mTotalFee, mNotifyUrl);
				String sign = SignUtils.sign(orderInfo, mRsaPrivate);
				sign = URLEncoder.encode(sign, "UTF-8");
				String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
						+ "sign_type=\"RSA\"";
				PayTask aliPay = new PayTask((Activity) mContext);
				String result = aliPay.pay(payInfo);
				PayResult payResult = new PayResult(result);
				return Integer.parseInt(payResult.getResultStatus());
			} catch (Exception e) {
				return e;
			}
		}

		@Override
		protected void onPostExecute(Object result) {
			for (OPayListener listener : getListeners(OPayListener.class)) {
				if (result instanceof Exception) {
					listener.onError(AlipayTask.this, (Exception) result);
				} else {
					mStatus = ((Integer) result).intValue();
					if (mStatus == 9000) {
						listener.onComplete(AlipayTask.this);
					} else {
						listener.onError(AlipayTask.this, null);
					}
				}
			}
			stop();
		}

	}
}
