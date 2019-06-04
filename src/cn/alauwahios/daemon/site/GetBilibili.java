package cn.alauwahios.daemon.site;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.javascript.host.Map;

import cn.alauwahios.daemon.dao.AlauwahiosDao;
import cn.alauwahios.daemon.util.HtmlUtil;
import cn.alauwahios.daemon.util.JsonUtil;
import cn.alauwahios.daemon.util.JsoupUtil;
import cn.alauwahios.daemon.util.SleepUtil;
import cn.alauwahios.daemon.util.StringUtil;
import cn.alauwahios.daemon.util.TransformEncode;

public class GetBilibili implements Runnable {
	/**
	 *  https://space.bilibili.com/ajax/member/getSubmitVideos?mid=304176852&pagesize=30&tid=0&page=2&keyword=&order=pubdate
	 */
	private static final Logger logger = LoggerFactory.getLogger(GetBilibili.class);
	private static final String HOME_URL = "https://space.bilibili.com";
	private static final String URL_PATH = "/ajax/member/getSubmitVideos?";
	private static final String UP_NAME = "mid=1315101";
	private static final String URL_PARAM = "&pagesize=30&tid=0&keyword=&order=pubdate&page=";
	// https://space.bilibili.com/1315101/video?tid=0&keyword=&order=pubdate&page=156
	// 起始页
	private static final int startPage = 19645;

	public void run() {
		getBtZiyuan();
	}

	private int getLastPage() {
		int result = startPage;
		// 判断爬取到什么地方
		String pid = AlauwahiosDao.getBtbttMaxPid();
		if (!StringUtil.isNullOrBlank(pid)) {
			result = Integer.valueOf(pid);
		}
		 System.out.println(result);
		return result;
	}

	public void getBtZiyuan() {
		//int maxPid = getLastPage();
		try {
			for (int i = 156; i > 155; i--) {
				String url = HOME_URL + URL_PATH + UP_NAME + URL_PARAM + i;
				
				System.out.println(url);
				Document doc = JsoupUtil.getDocByConnectIgnoreContent(url);
				String json = doc.select("body").text();
				json = TransformEncode.unicode2utf8(json);
				int first = json.indexOf("[");
				int last = json.lastIndexOf("]");
				String substr = json.substring(first+1, last-1)+"}";
				System.out.println(substr);
				System.out.println(JsonUtil.toBean(substr, Map.class));
				
				
//				Elements eles = doc.getElementsByClass("attachlist").select("td").select("a");
//				String urlTor = "";
//				for (Element e : eles) {
//					String href = e.attr("href");
//					if (!StringUtil.isNullOrBlank(href) && href.startsWith("http")) {
//						//System.out.println(href);
//						href = href.replace("dialog", "download");
//						href = href.replace("-ajax-1", "");
//						urlTor = HttpRequest.getLinkAfterRediect(href);
//					}
//				}
//				// System.out.println(urlTor);
//				String key = "";
//				key = urlTor.replace(".torrent", "");
//				String[] keyStr = key.split("/");
//				key = keyStr[keyStr.length - 1];
//				// System.out.println(key);
//
//				// fx资源表
//				FxZiyuanVO vo = new FxZiyuanVO();
//				vo.setFxKW(key);
//				vo.setFxName(title);
//				vo.setFxLink(urlTor);
//				vo.setShortLink(Constants.DEFAULT_SHORT_LINK);
//				vo.setPostTime(new Date());
//				vo.setType(Constants.BTBTT_VIDEO_TYPE);
//				vo.setRemark(i + "");
//				System.out.println(JsonUtil.toJson(vo));
//				//AlauwahiosDao.saveFxZiyuan(vo);
//
//				SleepUtil.sleepBySecond(2, 4);
			}
		} catch (Exception e) {
			logger.error("[btbtt资源抓取出错了]，将重新抓取", e);
			System.out.println(e);
			SleepUtil.sleepBySecond(120, 150);
			getBtZiyuan();
		}
	}

	public static void main(String[] args) {
		GetBilibili fx = new GetBilibili();
		fx.getBtZiyuan();
	}
}