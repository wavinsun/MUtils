package cn.o.app.ui.video;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import cn.o.app.io.Extra;
import cn.o.app.ui.OActivity;
import cn.o.app.ui.OView;

@SuppressWarnings("serial")
public class VideoActivity extends OActivity {

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

	static class VideoContainer extends OView {

		protected VideoView mVideoView;

		protected MediaController mMediaController;

		public VideoContainer(Context context) {
			super(context);
		}

		protected void init() {
			Context context = getContext();
			mMediaController = new MediaController(context);
			RelativeLayout.LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			mVideoView = new VideoView(context);
			mVideoView.setMediaController(mMediaController);
			mVideoView.setLayoutParams(params);
			this.setContentView(mVideoView);
			this.setBackgroundColor(0xFF000000);
			super.init();
		}

		public VideoView getVideoView() {
			return mVideoView;
		}

	}

}
