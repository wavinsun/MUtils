package cn.mutils.app.share.api;


import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import cn.mutils.app.share.Share;
import cn.mutils.app.share.ShareBase;
import cn.mutils.app.util.AppUtil;

public class ShareQQDelegate extends ShareBase {

    @Override
    public void share() {
        if (Share.getTencentAppId() == null) {
            if (mListener != null) {
                mListener.onError(this);
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, mTitle);
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, mText);
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mUrl);
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mImageUrl);
        Tencent.createInstance(Share.getTencentAppId(), mContext).shareToQQ(AppUtil.toActivity(mContext), bundle, new IUiListener() {

            @Override
            public void onError(UiError err) {
                if (mListener != null) {
                    mListener.onError(ShareQQDelegate.this);
                }
            }

            @Override
            public void onComplete(Object obj) {
                if (mListener != null) {
                    mListener.onComplete(ShareQQDelegate.this);
                }
            }

            @Override
            public void onCancel() {
                if (mListener != null) {
                    mListener.onCancel(ShareQQDelegate.this);
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
