package com.ceb.widge;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;



public class UtiEncrypt
{
	
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	public static final String KEY = "ygQwtg2nv83#579c";
	public static final String IV = "2389751023458692";
	private static final String mdk = "05cd251e05274276adbf10815f560cb6";
	
	private static String toHexString(byte[] b)
	{		
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++)
		{
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	public static String encryptMD5(String input)
	{
		try
		{			
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update((input + mdk).getBytes("utf-8"));
			byte messageDigest[] = digest.digest();
			
			return toHexString(messageDigest).toLowerCase();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	

	/**
	 * AES 加密
	 * 
	 * @param input
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptAES(String input) throws Exception
	{
		 byte[] KEYT = { 121, 103, 81, 119, 116, 103, 50, 110, 118, 56, 51, 35, 53, 55, 57, 99 };
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
		SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes("utf-8"), "AES");
		Log.i("keyyyyyy", new String(KEYT));
		IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("utf-8"));
		cipher.init(Cipher.ENCRYPT_MODE, keySpec,ivSpec);
		byte[] result = cipher.doFinal(input.getBytes("utf-8"));
		return Base64.encode(result);
	}
	
	/**
	 * AES 解密
	 * 
	 * @param input
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decryptAES(String input) throws Exception
	{
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
		SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes("utf-8"), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("utf-8"));
		cipher.init(Cipher.DECRYPT_MODE, keySpec,ivSpec);
		byte[] result = cipher.doFinal(Base64.decode(input));
		return new String(result, "utf-8");
	}
	
}