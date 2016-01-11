package cn.mutils.app.pay;

import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.mutils.app.App;
import cn.mutils.app.core.ILockable;
import cn.mutils.app.core.err.ErrorCodeException;
import cn.mutils.app.core.task.IStoppableManager;
import cn.mutils.app.io.AppBroadcast;

/**
 * WeChat pay task
 */
@SuppressWarnings({"unused", "ConstantConditions"})
public class WXPayTask extends AppPayTask implements ILockable {

    protected String mAppId;

    protected String mPartnerId;

    protected String mPrepayId;

    protected String mNonceStr;

    protected String mTimeStamp;

    protected String mPackageValue;

    protected String mSign;

    protected String mExtData;

    @Override
    public boolean isLocked() {
        return true;
    }

    @Override
    public void setLocked(boolean locked) {

    }

    public String getAppId() {
        return mAppId;
    }

    public void setAppId(String value) {
        mAppId = value;
    }

    public String getPartnerId() {
        return mPartnerId;
    }

    public void setPartnerId(String value) {
        mPartnerId = value;
    }

    public String getPrepayId() {
        return mPrepayId;
    }

    public void setPrepayId(String value) {
        mPrepayId = value;
    }

    public String getNonceStr() {
        return mNonceStr;
    }

    public void setNonceStr(String value) {
        mNonceStr = value;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(String value) {
        mTimeStamp = value;
    }

    public String getPackageValue() {
        return mPackageValue;
    }

    public void setPackageValue(String value) {
        mPackageValue = value;
    }

    public String getSign() {
        return mSign;
    }

    public void setSign(String value) {
        mSign = value;
    }

    public String getExtData() {
        return mExtData;
    }

    public void setExtData(String value) {
        mExtData = value;
    }

    @Override
    protected void onStart() {
        if (App.getWechatAppId() == null) {
            dispatchException();
            return;
        }
        IWXAPI api = WXAPIFactory.createWXAPI(mContext, mAppId);
        if (api == null) {
            dispatchException();
            return;
        }
        if (!api.isWXAppInstalled()) {
            dispatchException();
            return;
        }
        if (api.getWXAppSupportAPI() < Build.PAY_SUPPORTED_SDK_INT) {
            dispatchException();
            return;
        }
        api.registerApp(mAppId);
        PayReq req = new PayReq();
        req.appId = mAppId;
        req.partnerId = mPartnerId;
        req.prepayId = mPrepayId;
        req.nonceStr = mNonceStr;
        req.timeStamp = mTimeStamp;
        req.packageValue = mPackageValue;
        req.sign = mSign;
        if (mExtData != null) {
            req.extData = mExtData;
        }
        api.sendReq(req);
        WXPayBroadcast broadcast = new WXPayBroadcast(mContext);
        broadcast.setOnReceiveListener(new WXPayBroadcastReceiverListener());
        if (mContext instanceof IStoppableManager) {
            ((IStoppableManager) mContext).bind(this);
        }
        broadcast.start();
    }

    protected void dispatchException() {
        for (AppPayListener listener : getListeners(AppPayListener.class)) {
            listener.onError(this, new Exception());
        }
        stop();
    }

    class WXPayBroadcastReceiverListener implements AppBroadcast.OnReceiveListener<WXPayBroadcast.WXPayExtra> {

        @Override
        public void onReceive(AppBroadcast<WXPayBroadcast.WXPayExtra> broadcast, WXPayBroadcast.WXPayExtra extra) {
            broadcast.stop();
            if (extra.errCode == 0) {
                for (AppPayListener listener : getListeners(AppPayListener.class)) {
                    listener.onComplete(WXPayTask.this);
                }
            } else {
                for (AppPayListener listener : getListeners(AppPayListener.class)) {
                    ErrorCodeException e = new ErrorCodeException(extra.errCode, extra.errStr);
                    listener.onError(WXPayTask.this, e);
                }
            }
            stop();
        }

    }

}
