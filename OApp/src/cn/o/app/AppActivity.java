package cn.o.app;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.o.app.core.event.listener.VersionUpdateListener;
import cn.o.app.ui.Activitier;
import cn.o.app.ui.StatusBox;

@SuppressLint("InflateParams")
public class AppActivity extends Activitier {

	private static final int NEW_VERSION_STATE_UNKNOWN = -1;
	private static final int NEW_VERSION_STATE_NO = 0;
	private static final int NEW_VERSION_STATE_YES = 1;

	protected static Object sSync = new Object();
	protected static int sNewVersionState = NEW_VERSION_STATE_UNKNOWN;

	protected FeedbackAgent mFeedbackAgent;
	protected UmengUpdateListener mUmengUpdateListener;
	protected UmengDialogButtonListener mUmengDialogButtonListener;
	protected List<VersionUpdateListener> mVersionUpdateListeners;

	protected StatusBox mStatusBox;
	protected RelativeLayout mTitleBox;
	protected TextView mTitleBoxName;
	protected ImageView mTitleBoxBackButton;

	protected void onClickTitleBoxBackBtn() {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mStatusBox == null) {
			mStatusBox = findViewById(R.id.status_box, StatusBox.class);
		}
		if (mTitleBox == null) {
			mTitleBox = findViewById(R.id.title_box, RelativeLayout.class);
		}
		if (mTitleBoxName == null) {
			mTitleBoxName = findViewById(R.id.title_box_name, TextView.class);
		}
		if (mTitleBoxBackButton == null) {
			mTitleBoxBackButton = findViewById(R.id.title_box_back, ImageView.class);
			if (mTitleBoxBackButton != null) {
				mTitleBoxBackButton.setOnClickListener(new OnClickTitleBoxBackButtonListener());
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (App.getApp() != null) {
			if (App.getApp().isUmengEnabled()) {
				MobclickAgent.onResume(this);
			}
			if (App.getApp().isJPushEneabled()) {
				JPushInterface.onResume(this);
			}
		}
	}

	@Override
	protected void onPause() {
		if (App.getApp() != null) {
			if (App.getApp().isUmengEnabled()) {
				MobclickAgent.onPause(this);
			}
			if (App.getApp().isJPushEneabled()) {
				JPushInterface.onPause(this);
			}
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (!mFinished) {
			if (mVersionUpdateListeners != null) {
				mVersionUpdateListeners.clear();
			}
		}
		super.onDestroy();
	}

	@Override
	public void finish() {
		if (mVersionUpdateListeners != null) {
			mVersionUpdateListeners.clear();
		}
		super.finish();
	}

	public boolean hasNewVersion() {
		if (sNewVersionState == NEW_VERSION_STATE_UNKNOWN) {
			checkNewVersion(null);
		}
		return sNewVersionState == NEW_VERSION_STATE_YES;
	}

	public void checkNewVersion(VersionUpdateListener listener) {
		if (App.getApp() == null || !App.getApp().isUmengEnabled()) {
			return;
		}
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
					synchronized (sSync) {
						if (hasNewVersion) {
							sNewVersionState = NEW_VERSION_STATE_YES;
						} else {
							sNewVersionState = NEW_VERSION_STATE_NO;
						}
					}
					if (mFinished) {
						return;
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
								UmengUpdateAgent.showUpdateDialog(AppActivity.this, updateInfo);
							}
						}
					}
				}
			};
		}
		UmengUpdateAgent.setUpdateListener(this.mUmengUpdateListener);
		UmengUpdateAgent.forceUpdate(this);
	}

	public void feedback() {
		if (App.getApp() == null || !App.getApp().isUmengEnabled()) {
			return;
		}
		if (mFeedbackAgent == null) {
			mFeedbackAgent = new FeedbackAgent(this);
			mFeedbackAgent.sync();
		}
		mFeedbackAgent.startFeedbackActivity();
	}

	class OnClickTitleBoxBackButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			onClickTitleBoxBackBtn();
		}

	}

}
