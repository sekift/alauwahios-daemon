package cn.alauwahios.daemon.tieba;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.alauwahios.daemon.Constants;
import cn.alauwahios.daemon.dao.AlauwahiosDao;
import cn.alauwahios.daemon.util.DateUtil;
import cn.alauwahios.daemon.util.JsoupUtil;
import cn.alauwahios.daemon.util.RandomUtil;
import cn.alauwahios.daemon.util.SleepUtil;
import cn.alauwahios.daemon.util.UrlUtil;
import cn.alauwahios.daemon.vo.BaiduTiebaVO;
import cn.alauwahios.daemon.vo.BaiduWangpanVO;

public class BaiduTiebaDB implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(BaiduTiebaDB.class);
	private static final String HOME_URL = "http://tieba.baidu.com";
	private static final String HOME_PAGE_URL = "https://pan.baidu.com/mbox/homepage";
	private static final String HTTP = "http://";
	private static final int HTTP_INDEX = 48;
	private static final String HTTPS = "https://";
	private static final int HTTPS_INDEX = 49;
	private static final String SHORT_IDX_UNMARK = "short=";
	private static final String QUESTION_MARK= "?";
	private static final String SHORT_IDX = QUESTION_MARK + SHORT_IDX_UNMARK;
	private static final int SHORT_INDEX = 14;
	//private static final List<String> denyList = new ArrayList<String>();
	
	BaiduYunDB baiduYun = new BaiduYunDB();
	
	public void run() {
		getBaiduWangpan();
	}
	
	public void getBaiduWangpan() {
		String keyWord = "short";
		String urlPage1 = "http://tieba.baidu.com/f/search/res?isnew=1&kw=&qw=" + keyWord
				+ "&rn="+RandomUtil.randomInt(10, 50)+"&un=&only_thread=0&sm=1&sd=&ed=&pn=1&ie=utf-8";
		//String urlPage1 = "http://tieba.baidu.com/f/search/res?ie=utf-8&qw=" + keyWord;
		String className = "s_post_list";
		try {
			Elements eles = JsoupUtil.getByAttrClass(urlPage1, className);
			Element ele = eles.get(0);
			int length = ele.getElementsByTag("span").size();
			//List<String> deleteList = new ArrayList<String>();
			//List<String> resultList = new ArrayList<String>();
			for (int i = 0; i < length; i++) {
				String panLink = ele.getElementsByClass("p_content").get(i).text();
				if ((!panLink.contains("失效")) && (!panLink.contains("复制"))) {
					Element eLink = ele.getElementsByTag("span").get(i);
					String replyLink = HOME_URL + eLink.select("a").attr("href");
					String replyName = eLink.select("a").text();

					Element bLink = ele.getElementsByClass("p_forum").get(i);
					String tiebaLink = HOME_URL + bLink.select("a").attr("href");
					String tiebaName = bLink.select("a").text();

					String postTime = ele.getElementsByClass("p_date").get(i).text();

					try{
					if (panLink.startsWith(HTTP)) {
						panLink = panLink.substring(0, HTTP_INDEX);
					} else if (panLink.startsWith(HTTPS)) {
						panLink = panLink.substring(0, HTTPS_INDEX);
					} else if (panLink.indexOf(HTTP) > -1) {
						int start = panLink.indexOf(HTTP);
						panLink = panLink.substring(start, start + HTTP_INDEX);
					} else if (panLink.indexOf(HTTPS) > -1) {
						int start = panLink.indexOf(HTTPS);
						panLink = panLink.substring(start, start + HTTPS_INDEX);
					} else if (panLink.indexOf(SHORT_IDX) > -1) {
						int start = panLink.indexOf(SHORT_IDX);
						panLink = HOME_PAGE_URL + panLink.substring(start, start + SHORT_INDEX);
					}
					}catch(Exception e){
						logger.error("截取出错了，请检查：panLink="+panLink+";错误信息为：", e);
					}
					if (panLink.endsWith(".")) {
						panLink = panLink.replace(".", "");
					}
					if (panLink.contains("panbaiducom")) {
						panLink = panLink.replace("panbaiducom", "pan.baidu.com");
					}

					//String resultLink = "<a href=\"" + replyLink + "\" target=\"_blank\">" + replyName + "</a>"
							//+ " 贴吧：<a href=\"" + tiebaLink + "\" target=\"_blank\">" + tiebaName + "</a>" + " 发布时间："
							//+ postTime;
					//String spe = "<br />";
					//String resultpanLink = "<a href=\"" + panLink + "\" target=\"_blank\">" + panLink + "</a>";
					String panShortLink = UrlUtil.getUrlParamterValue(panLink, "short");
					// 优化
					//System.out.println(panLink);
					if(panLink.startsWith(SHORT_IDX_UNMARK)){
						panLink = HOME_PAGE_URL + QUESTION_MARK + panLink;
						panShortLink = UrlUtil.getUrlParamterValue(panLink, "short");
					}
					if ((panLink.contains("pan.baidu.com"))
							&& null != panShortLink 
							&& panShortLink.matches("^[a-zA-Z0-9]{6,8}+$")) {
						//panShortLink 网盘短链接
						//panLink 网盘链接
						//replyLink 回复链接
						//replyName 回复名字
						//tiebaLink 贴吧链接
						//tiebaName 贴吧名字
						//postTime 发布时间
						
						//网盘表
						BaiduWangpanVO bwvo = new BaiduWangpanVO();
						bwvo.setPanShortLink(panShortLink);
						bwvo.setPanLink(panLink);
						bwvo.setReplyName(replyName.replace("回复:", ""));
						bwvo.setReplyLink(replyLink);
						bwvo.setTiebaLink(tiebaLink);
						bwvo.setTiebaName(tiebaName);
						bwvo.setShortLink(Constants.DEFAULT_SHORT_LINK);
						bwvo.setPostTime(DateUtil.convertStrToDate(postTime+":00:00",DateUtil.DEFAULT_LONG_DATE_FORMAT));
						bwvo.setType(Constants.DEFAULT_TYPE);
						bwvo.setRemark(Constants.DEFAULT_REMARK);
						AlauwahiosDao.saveBaiduWangpan(bwvo);
						
						//贴吧表
						String tiebaKw = UrlUtil.getUrlParamterValue(tiebaLink, "kw");
						BaiduTiebaVO btvo = new BaiduTiebaVO();
						btvo.setTiebaKw(tiebaKw);
						btvo.setTiebaName(tiebaName);
						btvo.setTiebaLink(tiebaLink);
						btvo.setShortLink(Constants.DEFAULT_SHORT_LINK);
						btvo.setType(Constants.DEFAULT_TYPE);
						btvo.setRemark(Constants.DEFAULT_REMARK);
						AlauwahiosDao.saveBaiduTieba(btvo);
						
						/*String requestQuery = UrlUtil.getUrlParamter(panLink);
						if(!deleteList.contains(panLink)
								&& !denyList.contains(requestQuery)){
							deleteList.add(panLink);
						    resultList.add(resultLink + spe + resultpanLink);
						}*/
					}
				}
			}
			
			// 插入baiduyun.xyz的抓取
			baiduYun.getContent();
			/**
			 * 不再写文件
			 */
			//List<String> baiduYunList = baiduYun.getContent();
			//resultList.add("<br />以下的时间相对较前，在上面的不够用时可以尝试加群！<br />");
			//resultList.addAll(baiduYunList);
			//FileOperate.saveToFile(resultList);
		} catch (Exception e) {
			logger.error("[tieba抓取出错了]", e);
			SleepUtil.sleepBySecond(30, 30);
			getBaiduWangpan();
		}
	}
}