package cn.alauwahios.daemon.site;

import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.alauwahios.daemon.Constants;
import cn.alauwahios.daemon.core.HttpRequest;
import cn.alauwahios.daemon.dao.AlauwahiosDao;
import cn.alauwahios.daemon.util.JsoupUtil;
import cn.alauwahios.daemon.util.SleepUtil;
import cn.alauwahios.daemon.util.StringUtil;
import cn.alauwahios.daemon.vo.FxZiyuanVO;

public class GetBtbtt05 implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(GetBtbtt05.class);
	private static final String HOME_URL = "http://www.3btbtt.com"; // btbtt05
	// 第一页 https://www.btbtt05.com/forum-index-fid-1.htm
	// 3btbtt
	// 详情 https://www.btbtt05.com/thread-index-fid-1-tid-18740.htm
	// 起始页
	private static final int startPage = 22789;

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
		int maxPid = getLastPage();
		try {
			for (int i = maxPid + 1; i < maxPid + 130; i++) {
				String url = HOME_URL + "/thread-index-fid-1-tid-" + i + ".htm";
				System.out.println(url);
				Document doc = JsoupUtil.getDocByConnect(url);
				String title = doc.select("title").text();
				// 将过滤某些字样
				
				if(StringUtil.isNullOrBlank(title)){
					continue;
				}
				
				title = title.replace("[BT下载]", "");
				title = title.replace("电影下载 ", "");
				
				System.out.println(title);
				Elements eles = doc.getElementsByClass("attachlist").select("td").select("a");
				String urlTor = "";
				for (Element e : eles) {
					String href = e.attr("href");
					if (!StringUtil.isNullOrBlank(href) && href.startsWith("http")) {
						//System.out.println(href);
						href = href.replace("dialog", "download");
						href = href.replace("-ajax-1", "");
						urlTor = HttpRequest.getLinkAfterRediect(href);
					}
				}
				// System.out.println(urlTor);
				String key = "";
				key = urlTor.replace(".torrent", "");
				String[] keyStr = key.split("/");
				key = keyStr[keyStr.length - 1];
				// System.out.println(key);

				// fx资源表
				FxZiyuanVO vo = new FxZiyuanVO();
				vo.setFxKW(key);
				vo.setFxName(title);
				vo.setFxLink(urlTor);
				vo.setShortLink(Constants.DEFAULT_SHORT_LINK);
				vo.setPostTime(new Date());
				vo.setType(Constants.BTBTT_VIDEO_TYPE);
				vo.setRemark(i + "");
				//System.out.println(JsonUtil.toJson(vo));
				AlauwahiosDao.saveFxZiyuan(vo);

				SleepUtil.sleepBySecond(2, 4);
			}
		} catch (Exception e) {
			logger.error("[btbtt资源抓取出错了]，将重新抓取", e);
			SleepUtil.sleepBySecond(120, 150);
			getBtZiyuan();
		}
	}

	public static void main(String[] args) {
		GetBtbtt05 fx = new GetBtbtt05();
		fx.getBtZiyuan();
	}
}