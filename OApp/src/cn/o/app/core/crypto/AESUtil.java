package cn.o.app.core.crypto;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import cn.o.app.core.text.StringUtil;

/**
 * AES encrypt and decrypt
 */
public class AESUtil {

	public static final String ALGORITHM = "AES";
	public static final String CIPHER_ECB = "AES/ECB/PKCS5Padding";
	public static final String CIPHER_CBC = "AES/CBC/PKCS5Padding";
	public static final String CIPHER_CBC_NO_PADDING = "AES/CBC/NoPadding";

	public static String encrypt(String text, String pwd) {
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ECB);
			cipher.init(Cipher.ENCRYPT_MODE, generateKey(pwd));
			byte[] encrypt = cipher.doFinal(text.getBytes("UTF-8"));
			return StringUtil.toHex(encrypt);
		} catch (Exception e) {
			return null;
		}
	}

	public static String decrypt(String text, String pwd) {
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ECB);
			cipher.init(Cipher.DECRYPT_MODE, generateKey(pwd));
			byte[] decrypt = cipher.doFinal(StringUtil.toBytes(text));
			return new String(decrypt, "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}

	protected static Key generateKey(String pwd) throws Exception {
		return new SecretKeySpec(StringUtil.md5(pwd.getBytes("UTF-8")), ALGORITHM);
	}
}
