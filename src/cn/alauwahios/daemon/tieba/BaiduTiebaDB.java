package cn.alauwahios.daemon.tieba;

import java.util.ArrayList;
import java.util.List;

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
	private static final String SHORT_IDX = "?short=";
	private static final int SHORT_INDEX = 14;
	private static final List<String> denyList = new ArrayList<String>();
	
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
			List<String> deleteList = new ArrayList<String>();
			List<String> resultList = new ArrayList<String>();
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

					String resultLink = "<a href=\"" + replyLink + "\" target=\"_blank\">" + replyName + "</a>"
							+ " 贴吧：<a href=\"" + tiebaLink + "\" target=\"_blank\">" + tiebaName + "</a>" + " 发布时间："
							+ postTime;
					String spe = "<br />";
					String resultpanLink = "<a href=\"" + panLink + "\" target=\"_blank\">" + panLink + "</a>";
					String panShortLink = UrlUtil.getUrlParamterValue(panLink, "short");
					
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
						
						String requestQuery = UrlUtil.getUrlParamter(panLink);
						if(!deleteList.contains(panLink)
								&& !denyList.contains(requestQuery)){
							deleteList.add(panLink);
						    resultList.add(resultLink + spe + resultpanLink);
						}
					}
				}
			}
			
			// 插入baiduyun.xyz的抓取
			List<String> baiduYunList = baiduYun.getContent();
			resultList.add("<br />以下的时间相对较前，在上面的不够用时可以尝试加群！<br />");
			resultList.addAll(baiduYunList);
			FileOperate.saveToFile(resultList);
		} catch (Exception e) {
			logger.error("[tieba抓取出错了]", e);
			SleepUtil.sleepBySecond(30, 30);
			getBaiduWangpan();
		}
	}
	
	static {
	    denyList.add("short=1fDVGW");
	    denyList.add("short=A0ISm");
	    denyList.add("short=b2ccVW");
	    denyList.add("short=bpGUkv9");
	    denyList.add("short=boJBfPX");
	    denyList.add("short=bo1uG0n");
	    denyList.add("short=boCA5wb");
	    denyList.add("short=bpCkIUb");
	    denyList.add("short=bp6x1nx");
	    denyList.add("short=bpI9FVT");
	    denyList.add("short=bN1J8i");
	    denyList.add("short=bnwPLpl");
	    denyList.add("short=c0Xt1V6");
	    denyList.add("short=c1F5iJ");
	    denyList.add("short=c1F5iJQ");
	    denyList.add("short=c1JtWmW");
	    denyList.add("short=c18K9rU");
	    denyList.add("short=c2bAWWo");
	    denyList.add("short=c2D4zYC");
	    denyList.add("short=c22FdgW");
	    denyList.add("short=c28KXtY");
	    denyList.add("short=c3rImAW");
	    denyList.add("short=c10gv4G");
	    denyList.add("short=c1El0Gg");
	    denyList.add("short=c5bfbC");
	    denyList.add("short=cgDqpC");
	    denyList.add("short=dFb8OBB");
	    denyList.add("short=b66ME6");
	    denyList.add("short=dDTt3UT");
	    denyList.add("short=dEDSxqh");
	    denyList.add("short=dFLO8hF");
	    denyList.add("short=dELZPsP");
	    denyList.add("short=dE0Aa7B");
	    denyList.add("short=dEXMiYl");
	    denyList.add("short=dF1Xobf");
	    denyList.add("short=dFodC1j");
	    denyList.add("short=dFmygs5");
	    denyList.add("short=dF2WCzJ");
	    denyList.add("short=dE5KRxJ");
	    denyList.add("short=dENv8f3");
	    denyList.add("short=dEXMiY");
	    denyList.add("short=dKTt6UT");
	    denyList.add("short=eSByAhO");
	    denyList.add("short=eSHbRYM");
	    denyList.add("short=eSD0DDC");
	    denyList.add("short=eR6hSBK");
	    denyList.add("short=eR7Zwdk");
	    denyList.add("short=eRNctiU");
	    denyList.add("short=eRQv8Ro");
	    denyList.add("short=eSgq7xo");
	    denyList.add("short=eStNE2e");
	    denyList.add("short=eT7Zwdk");
	    denyList.add("short=eSZwWH8");
	    denyList.add("short=gfMmbE7");
	    denyList.add("short=gfvNzLh");
	    denyList.add("short=gfN7Quf");
	    denyList.add("short=geTxoVT");
	    denyList.add("short=ge7I5ft");
	    denyList.add("short=geZIDhx");
	    denyList.add("short=geZci8V");
	    denyList.add("short=ge4uTX5");
	    denyList.add("short=geE4kOr");
	    denyList.add("short=gfNig7p");
	    denyList.add("short=i43RBEH");
	    denyList.add("short=i5IKdgx");
	    denyList.add("short=i4LRDLr");
	    denyList.add("short=i4AwxuP");
	    denyList.add("short=i4BpQmx");
	    denyList.add("short=i4uKw9v");
	    denyList.add("short=i51PgP3");
	    denyList.add("short=i55081B");
	    denyList.add("short=i519xg5");
	    denyList.add("short=i58AEyx");
	    denyList.add("short=i4Rrx3Z");
	    denyList.add("short=i4ZyGZj");
	    denyList.add("short=jI66UwA");
	    denyList.add("short=jI3OYSe");
	    denyList.add("short=jICREdw");
	    denyList.add("short=jIpXBs6");
	    denyList.add("short=jIpmbLs");
	    denyList.add("short=jIkmbsU");
	    denyList.add("short=jHAu0zc");
	    denyList.add("short=jH7mVR4");
	    denyList.add("short=jHVtaya");
	    denyList.add("short=jHTXm8Q");
	    denyList.add("short=jHOUklg");
	    denyList.add("short=jHRVx90");
	    denyList.add("short=kUAYv47");
	    denyList.add("short=kV1KZif");
	    denyList.add("short=kVRIO3H");
	    denyList.add("short=kV9mHmB");
	    denyList.add("short=kUC9e7t");
	    denyList.add("short=hsgSxms");
	    denyList.add("short=hsk5A4W");
	    denyList.add("short=hsIfW7i");
	    denyList.add("short=hsd4wa4");
	    denyList.add("short=hqB26BM");
	    denyList.add("short=hr9uhBQ");
	    denyList.add("short=hrQSyTe");
	    denyList.add("short=hrPr2xY");
	    denyList.add("short=hsiT0t2");
	    denyList.add("short=miAf2HE");
	    denyList.add("short=micUUl6");
	    denyList.add("short=mhPj2Q0");
	    denyList.add("short=mhWARFy");
	    denyList.add("short=mh78vMC");
	    denyList.add("short=mh831hQ");
	    denyList.add("short=nuVS1ML");
	    denyList.add("short=nuDngoh");
	    denyList.add("short=nvOH7nz");
	    denyList.add("short=pKOxnYj");
	    denyList.add("short=pKHBDX5");
	    denyList.add("short=pKS0Qp1");
	    denyList.add("short=pJQXfz9");
	    denyList.add("short=pLvNFqb");
	    denyList.add("short=pL7csQJ");
	    denyList.add("short=pLG5Z0R");
	    denyList.add("short=pLHlSoF");
	    denyList.add("short=pLPBNwJ");
	    denyList.add("short=o7JTo5W");
	    denyList.add("short=o7VkWem");
	    denyList.add("short=o7RwcOi");
	    denyList.add("short=o7TOXf0");
	    denyList.add("short=o8091sI");
	    denyList.add("short=o8oNJo2");
	    denyList.add("short=o8n0Rii");
	    denyList.add("short=o8TRyp8");
	    denyList.add("short=o8xQwVg");
	    denyList.add("short=o8Hojyy");
	    denyList.add("short=qYdFJZ2");
	    denyList.add("short=qYlUHWC");
	    denyList.add("short=qYKMPZY");
	    denyList.add("short=qYkUC1m");
	    denyList.add("short=qYbfGXQ");
	    denyList.add("short=qYH55Kw");
	    denyList.add("short=qYTvjv2");
	    denyList.add("short=qY0oU1A");
	    denyList.add("short=qXZk0qw");
	    denyList.add("short=skCNVYX");
	    denyList.add("short=sl8u3z3");
	    denyList.add("short=sladgD3");
	    denyList.add("short=slWaWLR");
	    denyList.add("short=slOjcrN");
	    denyList.add("short=skGRCdB");
	    denyList.add("short=skIT9pj");
	    denyList.add("short=skWH2UT");
	    denyList.add("short=sk90gCP");

	    denyList.add("short=?short=");
	    denyList.add("userShort=dFb");
	    denyList.add("userShort=dFb");
	    denyList.add("short=dE5ZP**");

	    denyList.add("?short=jICREdw");
	    denyList.add("?short=gfN7Quf");
	    denyList.add("?short=micUUl6");

	    denyList.add("&short=sl");
	    denyList.add("&short=c1");
	    denyList.add("&short=d1");
	    denyList.add("&short=gf");
	    denyList.add(" short=i5Ielg");
	  }

}