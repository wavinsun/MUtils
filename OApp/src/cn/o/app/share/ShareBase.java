package cn.o.app.share;

import android.content.Context;

public abstract class ShareBase implements IShare {

	protected String mTitle;

	protected String mText;

	protected String mUrl;

	protected String mImageUrl;

	protected int mPlatform = IShare.PLATFORM_QQ;

	protected IShareListener mListener;

	protected Context mContext;

	@Override
	public String getTitle() {
		return mTitle;
	}

	@Override
	public void setTitle(String title) {
		mTitle = title;
	}

	@Override
	public String getText() {
		return mText;
	}

	@Override
	public void setText(String text) {
		mText = text;
	}

	@Override
	public String getUrl() {
		return mUrl;
	}

	@Override
	public void setUrl(String url) {
		mUrl = url;
	}

	@Override
	public String getImageUrl() {
		return mImageUrl;
	}

	@Override
	public void setImageUrl(String imageUrl) {
		mImageUrl = imageUrl;
	}

	@Override
	public int getPlatform() {
		return mPlatform;
	}

	@Override
	public void setPlatform(int platform) {
		mPlatform = platform;
	}

	@Override
	public IShareListener getListener() {
		return mListener;
	}

	@Override
	public void setListener(IShareListener listener) {
		mListener = listener;
	}

	@Override
	public void setContext(Context context) {
		mContext = context;
	}

	@Override
	public Context getContext() {
		return mContext;
	}

}
