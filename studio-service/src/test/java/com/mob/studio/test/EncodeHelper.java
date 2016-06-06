package com.mob.studio.test;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.Security;

public class EncodeHelper {
	public static final String ALGORITHM_MD5 = "MD5";
	public static final String ALGORITHM_SHA1 = "SHA1";
	public static final String ALGORITHM_AES = "AES";

	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS7Padding";

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * encode string
	 * @param algorithm
	 * @param str
	 * @return String
	 */
	public static String encode(String algorithm, String str) {
		if (str == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(str.getBytes());
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * encode By MD5
	 * @param str
	 * @return String
	 */
	public static String encodeByMD5(String str) {
		if (str == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM_MD5);
			messageDigest.update(str.getBytes());
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Takes the raw bytes from the digest and formats them correct.
	 * @param bytes
	 *            the raw bytes from the digest.
	 * @return the formatted bytes.
	 */
	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		// 把密文转换成十六进制的字符串形式
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}


	/**
	 * do AES encode
	 * */
	public static byte[] AES128Encode(String key, String text) {
		if (key ==null || text == null) {
			return null;
		}
		try {
			byte[] data = text.getBytes("UTF-8");
			byte[] keyArr = key.toUpperCase().getBytes("UTF-8");
			byte[] newArr = new byte[16];
			System.arraycopy(keyArr, 0, newArr, 0, Math.min(keyArr.length,16));

			SecretKey secretKey = new SecretKeySpec(newArr, ALGORITHM_AES);
			Security.addProvider(new BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM,"BC");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] cipherTest = new byte[cipher.getOutputSize(data.length)];
			int ctLength = cipher.update(data, 0, data.length, cipherTest, 0);
			cipher.doFinal(cipherTest, ctLength);
			return cipherTest;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}

	}



	/**
	 * do AES decrypt
	 * */
	public static byte[] AES128Decrypt(String key, byte[] data) {
		if (key ==null || data == null) {
			return null;
		}
		try {
			byte[] keyArr = key.toUpperCase().getBytes("UTF-8");
			byte[] newKeyArr = new byte[16];
			System.arraycopy(keyArr, 0, newKeyArr, 0, Math.min(keyArr.length,16));
			SecretKey secretKey = new SecretKeySpec(newKeyArr, ALGORITHM_AES);
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM,"BC");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}


	private static String  showByteArray(byte[] data){
		if(null == data){
			return null;
		}
		StringBuilder sb = new StringBuilder("{");
		for(byte b:data){
			sb.append(b).append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("}");
		return sb.toString();
	}

	public static String Base64Encode(final byte[] data) {
		return Base64.encodeBase64String(data);
	}

	public static byte[] Base64Decode(String text) {
		return Base64.decodeBase64(text);
	}




	public static void main(String[] args) {

		System.out.println("111111 MD5  :"
		+ EncodeHelper.encodeByMD5("111111"));
		System.out.println("111111 MD5  :"
		+ EncodeHelper.encode("MD5", "111111"));
		System.out.println("111111 SHA1 :"
		+ EncodeHelper.encode("SHA1", "111111"));

		try {
			System.out.println("111111 AES :"
                            + showByteArray(EncodeHelper.AES128Encode("mobLive", "111111"))
                            + "\n==="
                            + new String(EncodeHelper.AES128Decrypt("mobLive", AES128Encode("mobLive", "111111")),"UTF-8")
            );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

//		System.out.println("[B@3aa0eb51 AES :"
//				+ com.mob.studio.test.EncodeHelper.AES128Decrypt("mobLive", "[B@3aa0eb51".getBytes()));

	}

}
