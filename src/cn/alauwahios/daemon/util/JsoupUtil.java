package cn.alauwahios.daemon.util;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 提供jsoup获取内容的类
 * 
 * @author:sekift
 * @time:2014-8-11 上午11:10:46
 * @version:
 */
public class JsoupUtil {
	// 连接时间
	public static final int connectTime = 5 * 60 * 60;// 5分钟
	// 默认的html模板
	public static final String html = "<html><head></head><bode></body></html>";

	public static Elements getByTag(String url, String tagName) {
		return getDocByConnect(url).getElementsByTag(tagName);
	}

	public static Elements getByAttrClass(String url, String className) {
		return getDocByConnect(url).getElementsByClass(className);
	}

	public static Element getByAttrId(String url, String idName) {
		return getDocByConnect(url).getElementById(idName);
	}

	// 获取doc connect方式
	public static Document getDocByConnect(String url) {
		Document doc = null;
		try {
			//doc = Jsoup.connect(url).timeout(connectTime).get();
			long updateTime = System.currentTimeMillis();
			Connection connect = Jsoup.connect(url);
	        connect.header("Host", "www.3btjia.com");
	        connect.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:78.0) Gecko/20100101 Firefox/78.0");
	        connect.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	        connect.header("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
	        connect.header("Accept-Encoding", "gzip, deflate");
	        connect.header("Cache-Control", "max-age=0");
	        connect.header("Connection", "keep-alive");
	        connect.header("Pragma", "no-cache");
	        connect.header("Referer", "http://www.3btjia.com/forum-index-fid-1-page-1.htm");
	        connect.header("Upgrade-Insecure-Requests", "1");
	        connect.header("Cookie", "Cookie: bbs_page=1; bbs_sid=90b92f3261616b1c; cck_lasttime=1597502094884; cck_count=1; bbs_lastonlineupdate=1597434105; bbs_lastday=1597429633; timeoffset=%2B08");
	        doc = connect.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	// 获取doc connect ignore content方式
	public static Document getDocByConnectIgnoreContent(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).timeout(connectTime).ignoreContentType(true).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	// 获取doc parse方式
	public static Document getDocByParse(String url) {
		Document doc = null;
		try {
			doc = Jsoup.parse(new URL(url), connectTime);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

}
