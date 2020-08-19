package com.duomai.new_custom_base.api.product.core.temp;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MD5Utils {
	public static String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
				'E', 'F' };
		try {
			MessageDigest md = MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public static Map<String, String> convertBase64StringtoMap(String str)
			throws UnsupportedEncodingException {
		if (str == null)
			return null;
		String keyvalues = null;
		try {
			keyvalues = new String(Base64.decode(str), "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String[] keyvalueArray = keyvalues.split("\\&");
		Map<String, String> map = new HashMap<String, String>();
		for (String keyvalue : keyvalueArray) {
			String[] s = keyvalue.split("\\=");
			if (s == null || s.length != 2)
				return null;
			map.put(s[0], s[1]);
		}
		return map;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String md5 = MD5Utils.getMD5("832071012".getBytes("UTF-8"));
		System.out.println(md5);
		String regex = "\\d";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(md5); 
		String integer = "";
		int i = 0;
		while(m.find()){
			integer +=m.group();
			System.out.println(m.group());
			i++;
			if(i>=3){
				break;
			}
		}
		System.out.println(integer);
		int newIndex = Integer.parseInt(integer);
		if(newIndex > 499){
			newIndex = newIndex - 500;
		}
		System.out.println(newIndex);
//		System.out.println(MD5Utils.getMD5("832071012".getBytes("UTF-8")));
	}
	
	/*** 
     * MD5加码 生成32位md5码 
     */  
    public static String string2MD5(String inStr){  
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");  
        }catch (Exception e){  
            System.out.println(e.toString());  
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = inStr.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
  
    }  
  
    /** 
     * 加密解密算法 执行一次加密，两次解密 
     */   
    public static String convertMD5(String inStr){  
        char[] a = inStr.toCharArray();  
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ '+');  
        }  
        String s = new String(a);  
        return s;  
     } 
}
