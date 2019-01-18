package cn.alauwahios.daemon.tieba;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.alauwahios.daemon.core.HttpRequest;
import cn.alauwahios.daemon.dao.AlauwahiosDao;
import cn.alauwahios.daemon.util.JsoupUtil;
import cn.alauwahios.daemon.util.SleepUtil;
import cn.alauwahios.daemon.util.UrlUtil;
import cn.alauwahios.daemon.vo.BaiduYunVO;

/**
 * 获取baiduyun的链接
 * 
 * @author:luyz
 * @time:2018-3-26 上午11:02:03
 * @version:
 */
public class YunqunzuDB {
	private static final Logger logger = LoggerFactory.getLogger(YunqunzuDB.class);
	private static final String YunqunzuUrl = "http://www.yunqunzuquan.com/";//http://www.01yungroup.com

	public static void getYunqunzu() {
		Document doc = null;
		try {
			doc = JsoupUtil.getDocByConnect(YunqunzuUrl);
			Elements eles = doc.getElementsByClass("group-name");
			for(Element ele : eles){
				String originUrl = ele.select("a").attr("href");
			    if(originUrl.startsWith("/")){
			    	String url = HttpRequest.getLinkAfterRediect(YunqunzuUrl + originUrl);
			    	// 写数据库
			    	System.out.println(url);
					BaiduYunVO byvo = new BaiduYunVO();
					String panShortLink = UrlUtil.getUrlParamterValue(url, "short");
					byvo.setPanShortLink(panShortLink);
					byvo.setPanLink(url);
					byvo.setShortLink("");
					byvo.setType(0);
					byvo.setRemark("");
					AlauwahiosDao.saveBaiduYun(byvo);
			    	
			    	SleepUtil.sleepBySecond(1, 5);
			    }
			}
		} catch (Exception e) {
			logger.error("[baiduyun]抓取出错了，", e);
		}
	}

	public static final void main(String args[]) {
		YunqunzuDB.getYunqunzu();
	}

}
