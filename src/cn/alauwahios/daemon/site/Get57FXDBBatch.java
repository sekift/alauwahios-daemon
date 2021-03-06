package cn.alauwahios.daemon.site;

import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.alauwahios.daemon.Constants;
import cn.alauwahios.daemon.dao.AlauwahiosDao;
import cn.alauwahios.daemon.util.DateUtil;
import cn.alauwahios.daemon.util.JsoupUtil;
import cn.alauwahios.daemon.vo.FxZiyuanVO;

public class Get57FXDBBatch {
	private static final Logger logger = LoggerFactory.getLogger(Get57FXDBBatch.class);
	private static final String HOME_URL = "http://www.wwaaffxx.com"; // 57FX

	public void getFXZiyuan(String url) {
		Document doc = JsoupUtil.getDocByConnect(HOME_URL + url);
		Elements elesFn = doc.getElementsByClass("c_fn").select("a");

		try {
			for (int i = 0; i < elesFn.size(); i++) {//
				String fxUrl = HOME_URL + elesFn.get(i).attr("href");
				Elements contentEles = JsoupUtil.getDocByConnect(fxUrl).getElementsByClass("content");

				System.out.println("fxUrl=" + fxUrl);

				String fxLink = contentEles.select("a").get(0).attr("href");
				String fxName = contentEles.select("h1").text();
				// postTime 提交时间
				String fxKW = fxLink.split("/")[4];
				String postTimeString = contentEles.select("li").get(1).text();
				String postTimeStr = null;
				if (HOME_URL.contains("57fx")) {
					postTimeStr = postTimeString.replace("更新时间：", "") + ":00:00";
				} else if (HOME_URL.contains("wwaaffxx")) {
					postTimeStr = postTimeString.replace("分享时间：", "") + ":00:00";
				}
				Date postTime = DateUtil.convertStrToDate(postTimeStr, DateUtil.DEFAULT_LONG_DATE_FORMAT);

				fxName = changeFileName(fxName);
				// fx资源表
				FxZiyuanVO vo = new FxZiyuanVO();
				vo.setFxKW(fxKW);
				vo.setFxName(fxName);
				vo.setFxLink(fxLink);
				vo.setShortLink(Constants.DEFAULT_SHORT_LINK);
				vo.setPostTime(postTime);
				vo.setType(Constants.FW_VIDEO_TYPE);
				vo.setRemark(Constants.DEFAULT_REMARK);
				AlauwahiosDao.saveFxZiyuan(vo);
			}
		} catch (Exception e) {
			logger.error("[57fx抓取出错了]，1小时后会重新抓取", e);
		}
	}

	/**
	 * 修改文件名 策略：1 什么微信 公众号全部去掉 2 最后加上自己的域名（www.pan00.com）
	 */
	private static final String PAN00 = "(www.pan00.com)";
	// private static final String PAN00_HOME = "http://www.pan00.com";

	public static String changeFileName(String fxName) {
		fxName = fxName.replace("微信", "");
		fxName = fxName.replace("公众号", "");
		fxName = fxName + PAN00;
		return fxName;
	}

	public static void main(String[] args) {
		Get57FXDBBatch fx = new Get57FXDBBatch();
		for (int i = 1; i > 0; i--) {
			fx.getFXZiyuan("/file-sp-new-" + i + "/");
		}
	}
}
