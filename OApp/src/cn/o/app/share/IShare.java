package cn.o.app.share;

import cn.o.app.context.IContextOwner;

public interface IShare extends IContextOwner {

	public static interface IShareListener {
		public void onComplete(IShare share);

		public void onError(IShare share);

		public void onCancel(IShare share);
	}

	public static final String PACKAGE_QQ = "com.tencent.mobileqq";
	public static final String PACKAGE_QZONE = "com.qzone";
	public static final String PACKAGE_WECHAT = "com.tencent.mm";
	public static final String PACKAGE_SINA_WEIBO = "com.sina.weibo";

	public static final int PLATFORM_QQ = 1;
	public static final int PLATFORM_QZONE = 2;
	public static final int PLATFORM_WECHAT = 3;
	public static final int PLATFORM_WECHAT_MOMENTS = 4;
	public static final int PLATFORM_WEIBO = 5;

	public String getTitle();

	public void setTitle(String title);

	public String getText();

	public void setText(String text);

	public String getUrl();

	public void setUrl(String url);

	public String getImageUrl();

	public void setImageUrl(String imageUrl);

	public int getPlatform();

	public void setPlatform(int platform);

	public void share();

	public IShareListener getListener();

	public void setListener(IShareListener listener);

}
