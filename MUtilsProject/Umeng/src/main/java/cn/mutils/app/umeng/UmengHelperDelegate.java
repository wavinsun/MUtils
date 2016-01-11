package cn.mutils.app.umeng;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateConfig;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import java.util.ArrayList;
import java.util.List;

import cn.mutils.app.App;
import cn.mutils.app.core.event.listener.VersionUpdateListener;
import cn.mutils.app.core.util.Edition;
import cn.mutils.app.ui.core.IActivity;
import cn.mutils.app.util.AppUtil;
import cn.mutils.app.util.UmengHelper;

@SuppressWarnings({"UnnecessaryUnboxing", "UnnecessaryBoxing", "unused", "SimplifiableConditionalExpression"})
public class UmengHelperDelegate extends UmengHelper {

    protected static Boolean sHasNewVersion;

    protected FeedbackAgent mFeedbackAgent;
    protected UmengUpdateListener mUmengUpdateListener;
    protected UmengDialogButtonListener mUmengDialogButtonListener;
    protected List<VersionUpdateListener> mVersionUpdateListeners;

    @Override
    public void onResume(Context context) {
        if (App.getApp() == null || !App.getApp().isUmengEnabled()) {
            return;
        }
        MobclickAgent.onResume(context);
    }

    @Override
    public void onPause(Context context) {
        if (App.getApp() == null || !App.getApp().isUmengEnabled()) {
            return;
        }
        MobclickAgent.onPause(context);
    }

    @Override
    public void onDestroy(Context context) {
        if (context instanceof IActivity) {
            if (((IActivity) context).isFinished()) {
                return;
            }
        }
        if (mVersionUpdateListeners != null) {
            mVersionUpdateListeners.clear();
        }
    }

    @Override
    public void finish(Context context) {
        if (mVersionUpdateListeners != null) {
            mVersionUpdateListeners.clear();
        }
    }

    @Override
    public boolean hasNewVersion(Context context) {
        if (sHasNewVersion == null) {
            checkNewVersion(context, null);
            return false;
        }
        return sHasNewVersion.booleanValue();
    }

    @Override
    public void checkNewVersion(Context context, VersionUpdateListener listener) {
        if (App.getApp() == null || !App.getApp().isUmengEnabled()) {
            return;
        }
        final Context c = context;
        if (listener != null) {
            if (mVersionUpdateListeners == null) {
                mVersionUpdateListeners = new ArrayList<VersionUpdateListener>();
            }
            mVersionUpdateListeners.add(listener);
        }
        UmengUpdateAgent.setDialogListener(null);
        if (this.mUmengUpdateListener == null) {
            this.mUmengUpdateListener = new UmengUpdateListener() {

                @Override
                public void onUpdateReturned(int statusCode, final UpdateResponse updateInfo) {
                    boolean hasNewVersion = statusCode == UpdateStatus.Yes;
                    synchronized (UmengHelperDelegate.class) {
                        sHasNewVersion = Boolean.valueOf(hasNewVersion);
                    }
                    if (c instanceof IActivity) {
                        if (((IActivity) c).isFinished()) {
                            return;
                        }
                    }
                    if (mVersionUpdateListeners != null) {
                        if (mVersionUpdateListeners.size() != 0) {
                            boolean interceptDialog = false;
                            for (VersionUpdateListener listener : mVersionUpdateListeners) {
                                if (hasNewVersion) {
                                    boolean intercept = listener.onYes(updateInfo.version);
                                    interceptDialog = interceptDialog ? true : intercept;
                                } else {
                                    listener.onNo();
                                }
                            }
                            if (hasNewVersion && !interceptDialog) {
                                if (mUmengDialogButtonListener == null) {
                                    mUmengDialogButtonListener = new UmengDialogButtonListener() {

                                        @Override
                                        public void onClick(int status) {
                                            if (mVersionUpdateListeners != null) {
                                                for (VersionUpdateListener listener : mVersionUpdateListeners) {
                                                    switch (status) {
                                                        case UpdateStatus.Update:
                                                            listener.onUpdate(updateInfo.version);
                                                            break;
                                                        case UpdateStatus.Ignore:
                                                        case UpdateStatus.NotNow:
                                                            listener.onUpdateCancel(updateInfo.version);
                                                            break;
                                                    }
                                                }
                                                mVersionUpdateListeners.clear();
                                            }
                                        }
                                    };
                                }
                                UmengUpdateAgent.setDialogListener(mUmengDialogButtonListener);
                                UmengUpdateAgent.showUpdateDialog(c, updateInfo);
                            }
                        }
                    }
                }
            };
        }
        UmengUpdateAgent.setUpdateListener(this.mUmengUpdateListener);
        UmengUpdateAgent.forceUpdate(c);
    }

    @Override
    public void feedback(Context context) {
        if (App.getApp() == null || !App.getApp().isUmengEnabled()) {
            return;
        }
        if (mFeedbackAgent == null) {
            mFeedbackAgent = new FeedbackAgent(context);
            mFeedbackAgent.sync();
        }
        mFeedbackAgent.startFeedbackActivity();
    }

    @Override
    public boolean isUmengEnabled(Context context) {
        return AppUtil.getAppMetaData(context, "UMENG_APPKEY") != null;
    }

    @Override
    public void initUmeng(Context context) {
        if (App.getApp() != null && App.getApp().getEdition() == Edition.DEBUG) {
            MobclickAgent.setDebugMode(true);
            MobclickAgent.setCatchUncaughtExceptions(false);
            UpdateConfig.setDebug(true);
        } else {
            UmengUpdateAgent.setUpdateCheckConfig(false);
        }
        // Fix bug for downloading always
        UmengUpdateAgent.setDeltaUpdate(false);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.setUpdateUIStyle(UpdateStatus.STYLE_DIALOG);
        UmengUpdateAgent.setUpdateAutoPopup(false);
    }

    @Override
    public void onKillProcess(Context context) {
        MobclickAgent.onKillProcess(context);
    }

}
