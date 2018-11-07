package cn.alauwahios.daemon.tieba;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.alauwahios.daemon.Constants;
import cn.alauwahios.daemon.dao.AlauwahiosDao;
import cn.alauwahios.daemon.util.DateUtil;
import cn.alauwahios.daemon.util.JsoupUtil;
import cn.alauwahios.daemon.util.SleepUtil;
import cn.alauwahios.daemon.util.UrlUtil;
import cn.alauwahios.daemon.vo.BaiduTiebaVO;
import cn.alauwahios.daemon.vo.BaiduWangpanVO;

public class BaiduTiebaDB implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(BaiduTiebaDB.class);
	private static final String HOME_URL = "http://tieba.baidu.com";
	private static final String HOME_PAGE_URL = "https://pan.baidu.com/mbox/homepage";
	private static final String SHORT_IDX_UNMARK = "short=";
	private static final String QUESTION_MARK = "?";

	BaiduYunDB baiduYun = new BaiduYunDB();

	public void run() {
		getBaiduWangpan();
	}

	public void getBaiduWangpan() {
		String keyWord = "short";
		String urlPage1 = "http://tieba.baidu.com/f/search/res?isnew=1&kw=&qw=" + keyWord + "&rn="
				+ 10 + "&un=&only_thread=0&sm=1&sd=&ed=&pn=1&ie=utf-8";
		//String urlPage1 = "http://tieba.baidu.com/f/search/res?ie=utf-8&qw=" + keyWord;
		String className = "s_post_list";
		try {
			Elements eles = JsoupUtil.getByAttrClass(urlPage1, className);
			Element ele = eles.get(0);
			int length = ele.getElementsByTag("span").size();
			for (int i = 0; i < length; i++) {
				String panLink = ele.getElementsByClass("p_content").get(i).text();
				try {
					if (panLink.contains("short=")) {
						panLink = panLink.substring(panLink.indexOf("short=") + 6, panLink.length());
						panLink = panLink.substring(0, 7);
					}
				} catch (Exception e) {
					logger.error("截取出错了，请检查：panLink为：" + 
				               ele.getElementsByClass("p_content").get(i).text() + ";错误信息为：", e);
					continue;
				}
				if (panLink.matches("^[a-zA-Z0-9]{6,8}+$")) {
					panLink = HOME_PAGE_URL + QUESTION_MARK + SHORT_IDX_UNMARK + panLink;
					Element eLink = ele.getElementsByTag("span").get(i);
					String replyLink = HOME_URL + eLink.select("a").attr("href");
					String replyName = eLink.select("a").text();

					Element bLink = ele.getElementsByClass("p_forum").get(i);
					String tiebaLink = HOME_URL + bLink.select("a").attr("href");
					String tiebaName = bLink.select("a").text();

					String postTime = ele.getElementsByClass("p_date").get(i).text();

					String panShortLink = UrlUtil.getUrlParamterValue(panLink, "short");

					// 网盘表
					BaiduWangpanVO bwvo = new BaiduWangpanVO();
					bwvo.setPanShortLink(panShortLink);
					bwvo.setPanLink(panLink);
					bwvo.setReplyName(replyName.replace("回复:", ""));
					bwvo.setReplyLink(replyLink);
					bwvo.setTiebaLink(tiebaLink);
					bwvo.setTiebaName(tiebaName);
					bwvo.setShortLink(Constants.DEFAULT_SHORT_LINK);
					bwvo.setPostTime(DateUtil.convertStrToDate(postTime + ":00:00", DateUtil.DEFAULT_LONG_DATE_FORMAT));
					bwvo.setType(Constants.DEFAULT_TYPE);
					bwvo.setRemark(Constants.DEFAULT_REMARK);
					AlauwahiosDao.saveBaiduWangpan(bwvo);

					// 贴吧表
					String tiebaKw = UrlUtil.getUrlParamterValue(tiebaLink, "kw");
					BaiduTiebaVO btvo = new BaiduTiebaVO();
					btvo.setTiebaKw(tiebaKw);
					btvo.setTiebaName(tiebaName);
					btvo.setTiebaLink(tiebaLink);
					btvo.setShortLink(Constants.DEFAULT_SHORT_LINK);
					btvo.setType(Constants.DEFAULT_TYPE);
					btvo.setRemark(Constants.DEFAULT_REMARK);
					AlauwahiosDao.saveBaiduTieba(btvo);
				}
			}

			// 插入baiduyun.xyz的抓取
			baiduYun.getContent();
			// 插入01dyzy.com的抓取
			YunqunzuDB.getYunqunzu();
		} catch (Exception e) {
			logger.error("[tieba抓取出错了]", e);
			SleepUtil.sleepBySecond(100, 100);
			getBaiduWangpan();
		}
	}
}