package cn.o.app.conf;

import android.content.Context;
import cn.o.app.OUtil;
import cn.o.app.annotation.Ignore;
import cn.o.app.json.JsonUtil;
import cn.o.app.runtime.BeanCache;
import cn.o.app.xml.XmlUtil;

@SuppressWarnings("serial")
public class ConfItem extends PrefItem implements IAssetItem {

	public static final int TYPE_ASSET_XML = 0;
	public static final int TYPE_ASSET_JSON = 1;

	protected int mAssetType = TYPE_ASSET_XML;

	protected String mAssetFileName;

	protected BeanCache mAssetCache;

	@Ignore
	public int getAssetType() {
		return mAssetType;
	}

	public void setPrefType(int assetType) {
		mAssetType = assetType;
	}

	@Ignore
	public String getAssetFileName() {
		return mAssetFileName;
	}

	public void setAssetFileName(String assetFileName) {
		mAssetFileName = assetFileName;
	}

	public synchronized boolean getFromAsset(Context context) {
		if (mAssetFileName == null) {
			return false;
		}
		if (mAssetCache == null) {
			mAssetCache = new BeanCache(this);
		} else {
			if (mAssetCache.toTarget() == null) {
				return true;
			}
		}
		if (mAssetType == TYPE_ASSET_XML) {
			try {
				XmlUtil.convert(OUtil.getAssetString(context, mAssetFileName),
						this);
				mAssetCache.fromTarget();
				return true;
			} catch (Exception e) {
				return false;
			}
		} else if (mAssetType == TYPE_ASSET_JSON) {
			try {
				JsonUtil.convert(OUtil.getAssetString(context, mAssetFileName),
						this);
				mAssetCache.fromTarget();
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	public void clearCache() {
		super.clearCache();
		if (mAssetCache != null) {
			mAssetCache.clear();
		}
	}

}
