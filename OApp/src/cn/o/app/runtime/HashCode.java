package cn.o.app.runtime;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HashCode {

	protected static final long HASH_CODE_NULL = 1L;

	public static long hashCode(Object obj) {
		return hashCode(obj, 0);
	}

	protected static long hashCode(Object obj, long hash) {
		if (obj == null) {
			return hash + HASH_CODE_NULL;
		}
		hash += obj.hashCode();
		if (obj instanceof String || obj instanceof Integer
				|| obj instanceof Long || obj instanceof Double
				|| obj instanceof Boolean || obj instanceof Float
				|| obj instanceof Character || obj instanceof Byte
				|| obj instanceof Short) {
			return hash;
		}
		if (obj instanceof List) {
			for (Object e : (List<?>) obj) {
				hash = hashCode(e, hash);
			}
		} else if (obj instanceof Map) {
			for (Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
				hash = hashCode(entry.getValue(), hash);
			}
		} else {
			for (OField f : OField.getFields(obj.getClass())) {
				try {
					hash = hashCode(f.get(obj), hash);
				} catch (Exception e) {
					hash += HASH_CODE_NULL;
				}
			}
		}
		return hash;
	}

}
