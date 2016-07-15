package cn.mutils.app.qq.impl;

import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import cn.mutils.app.App;
import cn.mutils.app.qq.IShareQQ;
import cn.mutils.app.share.ShareBase;
import cn.mutils.app.util.AppUtil;

public class ShareQQImpl extends ShareBase implements IShareQQ {

    @Override
    public void share() {
        if (App.getTencentAppId() == null) {
            if (mListener != null) {
                mListener.onError(this);
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, mTitle);
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, mText);
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mUrl);
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mImageUrl);
        Tencent.createInstance(App.getTencentAppId(), mContext).shareToQQ(AppUtil.toActivity(mContext), bundle, new IUiListener() {

            @Override
            public void onError(UiError err) {
                if (mListener != null) {
                    mListener.onError(ShareQQImpl.this);
                }
            }

            @Override
            public void onComplete(Object obj) {
                if (mListener != null) {
                    mListener.onComplete(ShareQQImpl.this);
                }
            }

            @Override
            public void onCancel() {
                if (mListener != null) {
                    mListener.onCancel(ShareQQImpl.this);
                }
            }
        });
    }

    @Override
    public int getPlatform() {
        return PLATFORM_QQ;
    }

    @Override
    public int getMethod() {
        return METHOD_API;
    }

}
