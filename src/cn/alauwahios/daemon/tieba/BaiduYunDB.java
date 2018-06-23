package cn.alauwahios.daemon.tieba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.alauwahios.daemon.dao.AlauwahiosDao;
import cn.alauwahios.daemon.util.JsonUtil;
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
public class BaiduYunDB {
	private static final Logger logger = LoggerFactory.getLogger(BaiduYunDB.class);
	private static final String baiduYunUrl = "http://baiduyun.xyz/getQunZuLink?shareId=";

	@SuppressWarnings("unchecked")
	public List<String> getContent() {
		String result = "";
		List<String> resultList = new ArrayList<String>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			for (int i = 1; i <= 30; i+=1) {
				result = JsoupUtil.getDocByConnectIgnoreContent(baiduYunUrl + i).text();
				resultMap = JsonUtil.toBean(result, Map.class);
				if (null != resultMap && resultMap.containsKey("success")) {
					if ((Boolean) (resultMap.get("success"))) {
						String content = (String) resultMap.get("qunZuLink");
						String resultLink = " 贴吧：小哥哥，快来玩阿！";
						String spe = "<br />";
						String resultContent = "<a href=\"" + content + "\" target=\"_blank\">" + content + "</a>";
						resultList.add(resultLink + spe + resultContent);
						
						// 写数据库
						BaiduYunVO byvo = new BaiduYunVO();
						String panShortLink = UrlUtil.getUrlParamterValue(content, "short");
						byvo.setPanShortLink(panShortLink);
						byvo.setPanLink(content);
						byvo.setShortLink("");
						byvo.setType(0);
						byvo.setRemark("");
						AlauwahiosDao.saveBaiduYun(byvo);
					}
				}
				// 每一个休息1-3秒
				SleepUtil.sleepBySecond(1, 3);
			}
		} catch (Exception e) {
			logger.error("[baiduyun]抓取出错了，", e);
			return resultList;
		}
		return resultList;

	}

	public static final void main(String args[]) {
		BaiduYunDB by = new BaiduYunDB();
		System.out.println(by.getContent());
	}

}
