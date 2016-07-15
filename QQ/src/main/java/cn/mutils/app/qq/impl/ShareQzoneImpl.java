package cn.mutils.app.qq.impl;

import android.os.Bundle;

import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import cn.mutils.app.App;
import cn.mutils.app.qq.IShareQzone;
import cn.mutils.app.share.ShareBase;
import cn.mutils.app.util.AppUtil;

public class ShareQzoneImpl extends ShareBase implements IShareQzone {

    @Override
    public void share() {
        if (App.getTencentAppId() == null) {
            if (mListener != null) {
                mListener.onError(this);
            }
        }
        Bundle bundle = new Bundle();
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, mTitle);
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, mText);
        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, mUrl);
        bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, AppUtil.asArrayList(new String[]{mImageUrl}));
        Tencent.createInstance(App.getTencentAppId(), mContext).shareToQzone(AppUtil.toActivity(mContext), bundle, new IUiListener() {

            @Override
            public void onError(UiError err) {
                if (mListener != null) {
                    mListener.onError(ShareQzoneImpl.this);
                }
            }

            @Override
            public void onComplete(Object obj) {
                if (mListener != null) {
                    mListener.onComplete(ShareQzoneImpl.this);
                }
            }

            @Override
            public void onCancel() {
                if (mListener != null) {
                    mListener.onCancel(ShareQzoneImpl.this);
                }
            }
        });
    }

    @Override
    public int getPlatform() {
        return PLATFORM_QZONE;
    }

    @Override
    public int getMethod() {
        return METHOD_API;
    }

}
