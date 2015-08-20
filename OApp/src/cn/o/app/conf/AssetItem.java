package cn.o.app.conf;

import android.content.Context;
import cn.o.app.OUtil;
import cn.o.app.core.annotation.Ignore;
import cn.o.app.core.json.JsonUtil;
import cn.o.app.core.runtime.BeanCache;
import cn.o.app.core.xml.XmlUtil;

/**
 * JSON or XML serializer for assets file
 * 
 * @see JsonUtil
 * @see XmlUtil
 */
@SuppressWarnings("serial")
public class AssetItem implements IAssetItem {

	/** Assets file type of XML */
	public static final int TYPE_ASSET_XML = 0;
	/** Assets file type of JSON */
	public static final int TYPE_ASSET_JSON = 1;

	/** Assets file type */
	protected int mAssetType = TYPE_ASSET_XML;

	protected String mAssetFileName;

	/** Bean cache of assets file */
	protected BeanCache mAssetCache;

	@Ignore
	public int getAssetType() {
		return mAssetType;
	}

	public void setAssetType(int assetType) {
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
				XmlUtil.convert(OUtil.getAssetString(context, mAssetFileName), this);
				mAssetCache.fromTarget();
				return true;
			} catch (Exception e) {
				return false;
			}
		} else if (mAssetType == TYPE_ASSET_JSON) {
			try {
				JsonUtil.convert(OUtil.getAssetString(context, mAssetFileName), this);
				mAssetCache.fromTarget();
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	public void clearCache() {
		if (mAssetCache != null) {
			mAssetCache.clear();
		}
	}
}
