package cn.mutils.app.share;

import android.content.Context;

import cn.mutils.app.mm.IShareWechat;
import cn.mutils.app.mm.IShareWechatMoments;
import cn.mutils.app.qq.IShareQQ;
import cn.mutils.app.qq.IShareQzone;
import cn.mutils.app.share.intent.IntentShareQzone;
import cn.mutils.app.share.intent.IntentShareTencentWeibo;
import cn.mutils.app.share.intent.IntentShareWeibo;
import cn.mutils.app.ssdk.IMobShareTencentWeibo;
import cn.mutils.app.ssdk.IMobShareWeibo;
import cn.mutils.core.runtime.CC;

public class Share extends ShareBase {

    public Share(Context context) {
        setContext(context);
        mMethod = METHOD_INTENT;
    }

    @Override
    public void share() {
        IShare wrapper = null;
        switch (mPlatform) {
            case PLATFORM_QQ:
                switch (mMethod) {
                    case METHOD_API:
                        wrapper = CC.getService(IShareQQ.class);
                        break;
                    case METHOD_INTENT:
                        wrapper = null;
                        break;
                    case METHOD_SHARE_SDK:
                        wrapper = null;
                        break;
                    default:
                        break;
                }
                break;
            case PLATFORM_QZONE:
                switch (mMethod) {
                    case METHOD_API:
                        wrapper = CC.getService(IShareQzone.class);
                        break;
                    case METHOD_INTENT:
                        wrapper = new IntentShareQzone(mContext);
                        break;
                    case METHOD_SHARE_SDK:
                        wrapper = null;
                        break;
                    default:
                        break;
                }
                break;
            case PLATFORM_WEIBO:
                switch (mMethod) {
                    case METHOD_API:
                        wrapper = null;
                        break;
                    case METHOD_INTENT:
                        wrapper = new IntentShareWeibo(mContext);
                        break;
                    case METHOD_SHARE_SDK:
                        wrapper = CC.getService(IMobShareWeibo.class);
                        break;
                    default:
                        break;
                }
                break;
            case PLATFORM_WECHAT:
                switch (mMethod) {
                    case METHOD_API:
                        wrapper = CC.getService(IShareWechat.class);
                        break;
                    case METHOD_INTENT:
                        wrapper = null;
                        break;
                    case METHOD_SHARE_SDK:
                        wrapper = null;
                        break;
                    default:
                        break;
                }
                break;
            case PLATFORM_WECHAT_MOMENTS:
                switch (mMethod) {
                    case METHOD_API:
                        wrapper = CC.getService(IShareWechatMoments.class);
                        break;
                    case METHOD_INTENT:
                        wrapper = null;
                        break;
                    case METHOD_SHARE_SDK:
                        wrapper = null;
                        break;
                    default:
                        break;
                }
                break;
            case PLATFORM_TENCENT_WEIBO:
                switch (mMethod) {
                    case METHOD_API:
                        wrapper = null;
                        break;
                    case METHOD_INTENT:
                        wrapper = new IntentShareTencentWeibo(mContext);
                        break;
                    case METHOD_SHARE_SDK:
                        wrapper = CC.getService(IMobShareTencentWeibo.class);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        if (wrapper == null) {
            if (mListener != null) {
                mListener.onError(this);
            }
            return;
        }
        if (wrapper.getContext() == null) {
            wrapper.setContext(mContext);
        }
        wrapper.setTitle(mTitle);
        wrapper.setText(mText);
        wrapper.setImageUrl(mImageUrl);
        wrapper.setUrl(mUrl);
        wrapper.setListener(mListener);
        wrapper.share();
    }

}
