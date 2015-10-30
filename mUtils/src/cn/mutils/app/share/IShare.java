package cn.mutils.app.share;

import cn.mutils.app.os.IContextOwner;

public interface IShare extends IContextOwner {

	public static interface IShareListener {

		public void onComplete(IShare share);

		public void onError(IShare share);

		public void onCancel(IShare share);

	}

	public static final String PACKAGE_QQ = "com.tencent.mobileqq";
	public static final String PACKAGE_QZONE = "com.qzone";
	public static final String PACKAGE_WECHAT = "com.tencent.mm";
	public static final String PACKAGE_WEIBO = "com.sina.weibo";
	public static final String PACKAGE_TENCENT_WEIBO = "com.tencent.WBlog";

	public static final int PLATFORM_UNKNOWN = 0;
	public static final int PLATFORM_QQ = 1;
	public static final int PLATFORM_QZONE = 2;
	public static final int PLATFORM_WECHAT = 3;
	public static final int PLATFORM_WECHAT_MOMENTS = 4;
	public static final int PLATFORM_WEIBO = 5;
	public static final int PLATFORM_TENCENT_WEIBO = 6;

	public static final int METHOD_UNKNOWN = 0;
	public static final int METHOD_INTENT = 1;
	public static final int METHOD_API = 2;
	public static final int METHOD_SHARE_SDK = 3;

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

	public int getMethod();

	public void setMethod(int method);

	public void share();

	public IShareListener getListener();

	public void setListener(IShareListener listener);

}
