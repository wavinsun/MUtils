package cn.mutils.app.core.beans;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.mutils.app.core.codec.ByteUtil;
import cn.mutils.app.core.text.StringUtil;

/**
 * MD5 for object
 */
@SuppressWarnings("unused")
public class ObjectMD5 {

    /**
     * MD5 binary data
     */
    protected byte[] mMD5;

    public ObjectMD5(Object obj) {
        mMD5 = getMD5(obj);
    }

    public byte[] getMD5() {
        return mMD5;
    }

    public String toString() {
        return StringUtil.toHex(mMD5);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (mMD5 == null) {
            return false;
        }
        if (o instanceof ObjectMD5) {
            return Arrays.equals(mMD5, ((ObjectMD5) o).mMD5);
        } else {
            return mMD5.equals(o);
        }
    }

    /**
     * Get MD5 for object
     *
     * @param obj Object
     * @return Object binary data
     */
    protected static byte[] getMD5(Object obj) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            updateMD5(obj, md5);
            return md5.digest();
        } catch (Exception e) {
            return new byte[0];
        }
    }

    /**
     * Update MD5 info data for object
     *
     * @param obj Object
     * @param md5 MD5
     */
    protected static void updateMD5(Object obj, MessageDigest md5) {
        if (obj == null) {
            md5.update(ByteUtil.toBytes(0));
            return;
        }
        if (obj instanceof String || obj instanceof Integer || obj instanceof Long || obj instanceof Double
                || obj instanceof Boolean || obj instanceof Float || obj instanceof Character || obj instanceof Byte
                || obj instanceof Short) {
            md5.update(ByteUtil.toBytes(obj.hashCode()));
            return;
        }
        if (obj instanceof List) {
            for (Object e : (List<?>) obj) {
                updateMD5(e, md5);
            }
        } else if (obj instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
                updateMD5(entry.getValue(), md5);
            }
        } else {
            for (BeanField f : BeanField.getFields(obj.getClass())) {
                try {
                    updateMD5(f.get(obj), md5);
                } catch (Exception e) {
                    updateMD5(null, md5);
                }
            }
        }
    }

}
