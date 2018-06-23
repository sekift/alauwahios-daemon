package cn.alauwahios.daemon.util;

/**
 * @author: sekift
 * @Create On: 2015-11-3 下午05:19:44
 * @Description: XSS过滤：<>()'
 */

public class XssUtil {
	
	public static String cleanXSS(String value) {
		if(value == null){
			return null;
		}
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		value = value.replaceAll("'", "&#39;");
		return value;
	}

}