package cn.alauwahios.daemon.site;

import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.alauwahios.daemon.Constants;
import cn.alauwahios.daemon.dao.AlauwahiosDao;
import cn.alauwahios.daemon.util.JsoupUtil;
import cn.alauwahios.daemon.util.StringUtil;
import cn.alauwahios.daemon.util.UrlUtil;
import cn.alauwahios.daemon.vo.FxZiyuanVO;

public class GetFriokDBBatch {
	private static final Logger logger = LoggerFactory.getLogger(GetFriokDBBatch.class);
	private static final String HOME_URL = "http://www.friok.com"; // http://www.friok.com/download.php?id=105709
	private static final String PATH_URL = "/download.php?id=";

	public void getFriokZiyuan(int id) {
		String url = HOME_URL + PATH_URL + id;
		Document doc = JsoupUtil.getDocByConnect(url);
        System.out.println(url);
		
        try {
			String elesTip = doc.getElementsByClass("list").select("a").attr("href");
			if (!StringUtil.isNullOrBlank(elesTip)) {

				Elements elesDesc = doc.getElementsByClass("desc").get(0).select("p");
				String fxName = elesDesc.get(0).text().replace("文件名称：", "").trim();
				String fxPassword = elesDesc.get(1).text().replace("百度网盘密码：", "").replace("     ", "").trim();
				String fxLink = elesTip.trim();

				FxZiyuanVO vo = new FxZiyuanVO();
				//magnet:?xt=urn:btih:4B153574AC12C152E4A6721BA965283130A7418F
				//https://pan.baidu.com/share/init?surl=ZfXCokkbReqwsV9yilImvg
				//ed2k://|file|%E5%BC%BA%E5%B2%9B.720p.HD%E4%B8%AD%E8%8B%B1%E5%8F%8C%E5%AD%97.mp4|1267940683|0A599C20427201B0E3A4CFBFEA9D39BD|h=NSM7TLXWALRZ4AGB3HFNS5QMW2Z3SETI|/
				String fxKW = "";
				if(fxLink.startsWith("magnet")){
					fxKW = fxLink.replace("magnet:?xt=urn:btih:", "");
				}else if(fxLink.startsWith("http")){
					fxKW = UrlUtil.getUrlParamterValue(fxLink, "surl");
				}else if(fxLink.startsWith("ed2k")){
					String[] link = fxLink.split("\\|");
					fxKW = link[link.length - 2].replace("h=", "");
			    }else{
					fxKW = fxLink;
				}
				vo.setFxKW(fxKW);
				vo.setFxName(fxName);
				vo.setFxLink(fxLink);
				vo.setShortLink(Constants.DEFAULT_SHORT_LINK);
				vo.setPostTime(new Date());
				vo.setType(Constants.FRIOK_VIDEO_TYPE);
				vo.setRemark(fxPassword);
				AlauwahiosDao.saveFriok(vo);
			}
		} catch (Exception e) {
			logger.error("[friok抓取出错了]，1小时后会重新抓取", e);
		}
	}

	/**
	 * 修改文件名 策略：1 什么微信 公众号全部去掉 2 最后加上自己的域名（www.pan00.com）
	 */
	private static final String PAN00 = "(www.pan00.com)";

	public static String changeFileName(String fxName) {
		fxName = fxName + PAN00;
		return fxName;
	}

	public static void main(String[] args) {
		GetFriokDBBatch fx = new GetFriokDBBatch();
		for (int i = 105719; i < 105830; i++) {
			fx.getFriokZiyuan(i);
		}
	}
}
