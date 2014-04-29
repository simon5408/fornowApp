package com.fornow.app.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * @author Simon Lv
 * @since 2012-6-10
 */
public class StringUtils {

	public static final String COMMA = ",";
	public static final String POINT = "\\.";
	public static final String UNDERLINE = "_";

	public static List<String> stringTokenizer(String value) {
		List<String> results = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(value);
		while (st.hasMoreElements()) {
			String str = st.nextElement().toString();
			if (org.apache.commons.lang.StringUtils.isNotBlank(str)) {
				results.add(str);
			}
		}
		return results;
	}

	public static String collectionToString(Collection<Object> collection) {
		if (collection.isEmpty()) {
			return "";
		} else {
			StringBuilder builder = new StringBuilder();
			for (Object obj : collection) {
				if (builder.length() != 0) {
					builder.append(COMMA).append(String.valueOf(obj));
				} else {
					builder.append(String.valueOf(obj));
				}
			}
			return builder.toString();
		}
	}

	public static Set<Long> stringToSet(String str) {
		Set<Long> result = new HashSet<Long>();
		if (org.apache.commons.lang.StringUtils.isBlank(str)) {
			return result;
		}
		for (String value : str.split(COMMA)) {
			result.add(Long.valueOf(value));
		}
		return result;
	}

	public static Set<Long> stringToSet(String[] values) {
		Set<Long> result = new HashSet<Long>();
		if (values != null) {
			for (String value : values) {
				result.add(Long.valueOf(value));
			}
		}
		return result;
	}

	/**
	 * 
	 * @param content
	 * @return
	 */
	public static int getByteSize(String content) {
		int size = 0;
		if (isNoEmpty(content)) {
			try {
				size = content.getBytes("utf-8").length;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return size;
	}

	/**
	 * String to List with special character
	 * 
	 * @param arrayStr
	 * @param special
	 * @return
	 */
	public static List<String> getListWithSpecial(String arrayStr,
			String special) {
		List<String> array = new ArrayList<String>();
		if (isNoEmpty(arrayStr)) {
			String[] arrays = arrayStr.split(special);
			array = Arrays.asList(arrays);
		}

		return array;
	}

	public static boolean isNoEmpty(String str) {
		if (str != null && !"".equals(str)) {
			return true;
		}

		return false;
	}

	public static String getFileExt(String fileName) {
		if (isNoEmpty(fileName)) {
			List<String> array = getListWithSpecial(fileName, POINT);
			if (array.size() > 0) {
				return "." + array.get(array.size() - 1);
			}
		}
		return null;
	}
}
