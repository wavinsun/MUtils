package cn.mutils.app.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import cn.mutils.app.core.event.IListener;
import cn.mutils.app.event.listener.OnActivityResultListener;
import cn.mutils.app.ui.core.IActivityExecutor;

/**
 * UnionPay
 */
public class UPPayTask extends AppPayTask {

    /**
     * UnionPay trade number
     */
    protected String mTradeNo;

    protected OnActivityResultListener mOnActivityResultListener;

    protected boolean mDebug;

    public boolean isDebug() {
        return mDebug;
    }

    public void setDebug(boolean debug) {
        if (mStarted || mStopped) {
            return;
        }
        mDebug = debug;
    }

    public String getTradeNo() {
        return mTradeNo;
    }

    public void setTradeNo(String tradeNo) {
        if (mStarted || mStopped) {
            return;
        }
        mTradeNo = tradeNo;
    }

    @Override
    protected void onStart() {
        String mode = mDebug ? "01" : "00";
        UPPayAssistEx.startPayByJAR((Activity) mContext, PayActivity.class, null, null, mTradeNo, mode);
    }

    @Override
    public void addListener(IListener listener) {
        super.addListener(listener);
        attachToContext();
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        attachToContext();
    }

    protected void attachToContext() {
        if (mContext == null) {
            return;
        }
        if (!(mContext instanceof IActivityExecutor)) {
            return;
        }
        if (mOnActivityResultListener == null) {
            mOnActivityResultListener = new UPPayResultListener();
        }
        IActivityExecutor executor = (IActivityExecutor) mContext;
        executor.addOnActivityResultListener(mOnActivityResultListener);
    }

    class UPPayResultListener implements OnActivityResultListener {

        @Override
        public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
            if (data == null) {
                return;
            }
            Bundle extras = data.getExtras();
            if (extras == null) {
                return;
            }
            String payResult = extras.getString("pay_result");
            if (payResult == null) {
                return;
            }
            if (payResult.equalsIgnoreCase("success")) {
                mStatus = STATUS_UP_SUCCESS;
                for (AppPayListener listener : getListeners(AppPayListener.class)) {
                    listener.onComplete(UPPayTask.this);
                }
            } else {
                if (payResult.equalsIgnoreCase("fail")) {
                    mStatus = STATUS_UP_FAIL;
                } else if (payResult.equalsIgnoreCase("cancel")) {
                    mStatus = STATUS_UP_CANCEL;
                }
                for (AppPayListener listener : getListeners(AppPayListener.class)) {
                    listener.onError(UPPayTask.this, null);
                }
            }
            ((IActivityExecutor) context).removeOnActivityResultListener(mOnActivityResultListener);
        }

    }
}
