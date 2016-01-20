package com.sunflower.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;

public class ConvertUtil {
	
	public static<T> T getValue(String value,String fieldName,Class<T> clazz){
	
		if (value == null) { // 濡傛灉鑾峰彇鍙傛暟鍊间负null,鍒欒繑鍥瀗ull
			return null;
		} else if (!value.equals("")) { // 濡傛灉鑾峰彇鍙傛暟鍊间笉涓?",鍒欓?杩嘽onvertGt鏂规硶杩涜绫诲瀷杞崲鍚庤繑鍥炵粨鏋?
			return convertGt(value, clazz);
		} else if (clazz.getName().equals(String.class.getName())) { // 濡傛灉鑾峰彇鍙傛暟鍊间负""
			return convertGt(value, clazz);
		} else {// 濡傛灉鑾峰彇鍙傛暟鍊间负"",骞朵笖clazz涓嶆槸鏄疭tring绫诲瀷,鍒欒繑鍥瀗ull
			return null;
		}
	}
	
	/**
	 * @param <T>
	 * @param value
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertGt(String value, Class<T> clazz) {
		if (value == null) { // 濡傛灉鍊间负null,鍒欒繑鍥瀗ull
			return null;
		} else if (value.equals("")
				&& !clazz.getName().equals(String.class.getName())) { // 濡傛灉value鍊间负"",鑰屼笖瑕佽浆涓虹殑绫诲瀷涓嶆槸string绫诲瀷锛岄偅涔堝氨缁熶竴杩斿洖null锛屼篃灏辨槸绌哄瓧绗︿覆涓嶈兘杞垚浠讳綍鍏朵粬绫诲瀷鐨勫疄浣擄紝鍙兘杩斿洖null
			return null;
		} else if (Date.class.getName().equalsIgnoreCase(clazz.getName())) { // 澧炲姞瀵逛粠String绫诲瀷鍒癉ate
			return (T) convertSTD(value);
		}
		return (T) ConvertUtils.convert(value, clazz);
	}

	//鏃ユ湡绫诲瀷鐨勮浆鎹?
	private static SimpleDateFormat simpleDateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static Date convertSTD(String date){
		try {
			return simpleDateFormate.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String convertDTS(Date date){
		return simpleDateFormate.format(date);
	}
	
}
