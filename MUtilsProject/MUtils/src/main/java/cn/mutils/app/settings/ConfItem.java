package cn.mutils.app.settings;

import android.content.Context;

import cn.mutils.app.util.AppUtil;
import cn.mutils.core.annotation.Ignore;
import cn.mutils.core.beans.BeanCache;
import cn.mutils.core.json.JsonUtil;
import cn.mutils.core.xml.XmlUtil;

/**
 * JOSN or XML serializer for assets file and shared preferences
 */
@SuppressWarnings({"serial", "unused"})
public class ConfItem extends PrefItem implements IAssetItem {

    /**
     * Assets file type of XML
     */
    public static final int TYPE_ASSET_XML = 0;
    /**
     * Assets file type of JSON
     */
    public static final int TYPE_ASSET_JSON = 1;

    /**
     * Assets file type
     */
    protected int mAssetType = TYPE_ASSET_XML;

    protected String mAssetFileName;

    /**
     * Bean cache of assets file
     */
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
                XmlUtil.convert(AppUtil.getAssetString(context, mAssetFileName), this);
                mAssetCache.fromTarget();
                return true;
            } catch (Exception e) {
                return false;
            }
        } else if (mAssetType == TYPE_ASSET_JSON) {
            try {
                JsonUtil.convert(AppUtil.getAssetString(context, mAssetFileName), this);
                mAssetCache.fromTarget();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public void clear() {
        super.clear();
        if (mAssetCache != null) {
            mAssetCache.clear();
        }
    }

}
