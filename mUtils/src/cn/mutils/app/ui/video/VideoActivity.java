package cn.mutils.app.ui.video;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import cn.mutils.app.io.Extra;
import cn.mutils.app.ui.Activitier;
import cn.mutils.app.ui.Viewer;

@SuppressWarnings("serial")
public class VideoActivity extends Activitier {

	public static class VideoExtra extends Extra {

		protected String mUrl;

		protected boolean mAutoPlay = true;

		public String getUrl() {
			return mUrl;
		}

		public void setUrl(String url) {
			mUrl = url;
		}

		public boolean isAutoPlay() {
			return mAutoPlay;
		}

		public void setAutoPlay(boolean autoPlay) {
			mAutoPlay = autoPlay;
		}

	}

	protected VideoExtra mExtra;

	protected VideoContainer mVideoContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mVideoContainer = new VideoContainer(this);
		this.setContentView(mVideoContainer);

		VideoView videoView = mVideoContainer.getVideoView();
		mExtra = new VideoExtra();
		if (mExtra.getFrom(getIntent())) {
			videoView.setVideoURI(Uri.parse(mExtra.mUrl));
			if (mExtra.mAutoPlay) {
				videoView.start();
			}
		}
		videoView.requestFocus();
	}

	static class VideoContainer extends Viewer {

		protected VideoView mVideoView;

		protected MediaController mMediaController;

		public VideoContainer(Context context) {
			super(context);
		}

		protected void init(Context context, AttributeSet attrs) {
			mMediaController = new MediaController(context);
			RelativeLayout.LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			mVideoView = new VideoView(context);
			mVideoView.setMediaController(mMediaController);
			mVideoView.setLayoutParams(params);
			this.setContentView(mVideoView);
			this.setBackgroundColor(0xFF000000);
			super.init(context, attrs);
		}

		public VideoView getVideoView() {
			return mVideoView;
		}

	}

}
