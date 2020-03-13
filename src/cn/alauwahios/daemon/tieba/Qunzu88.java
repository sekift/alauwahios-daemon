package cn.alauwahios.daemon.tieba;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.alauwahios.daemon.core.HttpRequest;
import cn.alauwahios.daemon.dao.AlauwahiosDao;
import cn.alauwahios.daemon.util.HttpUtil;
import cn.alauwahios.daemon.util.JsonUtil;
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
public class Qunzu88 {
	private static final Logger logger = LoggerFactory.getLogger(Qunzu88.class);
	private static final String YunqunzuUrl = "http://www.qunzu88.com";//http://www.01yungroup.com
	
	//www.qunzu88.com/jump-1508289.html?id=1508289&sign=-1274588090

	public static void getYunqunzu() {
		String doc = null;
		try {
			Map<String, String> headers= new HashMap<String, String>();
			headers.put("Host", "www.qunzu88.com");
			headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:70.0) Gecko/20100101 Firefox/70.0");
			headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			headers.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			//headers.put("Accept-Encoding", "gzip, deflate");
			headers.put("Referer",
					"https://www.baidu.com/link?url=NRjPmDYEeDU7whNWUxhVTdRIPkbWTePYp9_he4bmWgm&wd=&eqid=cab0f72d0000789a000000065de5237e");
			headers.put("Connection", "keep-alive");
			headers.put("Cookie",
					"JSESSIONID=804189252942F58B52B7DAA4C0B4F02C; Hm_lvt_1e618fac27df2db7cebe4f30842447b8=1575297939,1575298093,1575298262,1575298286; Hm_lpvt_1e618fac27df2db7cebe4f30842447b8=1575298286");
			headers.put("Upgrade-Insecure-Requests", "1");
			headers.put("Pragma", "no-cache");
			headers.put("Cache-Control", "no-cache");
			Map<String, String> params= new HashMap<String, String>();
			doc = HttpUtil.get(YunqunzuUrl, params, headers, 1*60*60, 1*60*60, "utf-8");
			System.out.println(doc);
			
			Elements eles = null;
			System.out.println(eles);
			for(Element ele : eles){
				String originUrl = ele.select("a").attr("onclick");
				originUrl = originUrl.replace("ondj_(", "");
				originUrl = originUrl.split(",")[0].replaceAll("\'","");
				System.out.println("originUrl="+originUrl);
			    if(originUrl.startsWith("/")){
			    	String url = HttpRequest.getLinkAfterRediect(YunqunzuUrl+originUrl);//YunqunzuUrl + 
			    	// 写数据库
			    	System.out.println("url="+url);
					BaiduYunVO byvo = new BaiduYunVO();
					String panShortLink = UrlUtil.getUrlParamterValue(url, "short");
					byvo.setPanShortLink(panShortLink);
					byvo.setPanLink(url);
					byvo.setShortLink("");
					byvo.setType(0);
					byvo.setRemark("");
					System.out.println(JsonUtil.toJson(byvo));
					AlauwahiosDao.saveBaiduYun(byvo);
			    	
			    	SleepUtil.sleepBySecond(1, 5);
			    }
			}
		} catch (Exception e) {
			logger.error("[baiduyun]抓取出错了，", e);
		}
	}

	public static final void main(String args[]) {
		Qunzu88.getYunqunzu();
	}

}
