package cn.alauwahios.daemon.site;

import java.util.Map;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.platform.win32.WinDef.CHAR;

import cn.alauwahios.daemon.dao.AlauwahiosDao;
import cn.alauwahios.daemon.util.JsonUtil;
import cn.alauwahios.daemon.util.JsoupUtil;

/**
 * 获取kuwo的信息 
 * 1 先获取歌手信息（歌手按字母分类总数+歌手具体信息） 
 * 2 再获取唱片信息 
 * 3 再获取歌曲信息 
 * 4 再获取歌词信息
 * 
 * @author sekift
 *
 */
public class GetKuwoDB {

	private static final Logger logger = LoggerFactory.getLogger(GetKuwoDB.class);
	private static final String HOME_URL = "http://www.kuwo.cn";

	// 歌手常数
	private static final char[] SINGER_PREFIX = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#".toCharArray();//

	/**
	 * 1 歌手按字母分类总数
	 */
	public void getKuwoSingerTotal() {
		String singerUrl = HOME_URL + "/api/www/artist/artistInfo?category=0";
		try {
			String path = "";
			for (char arr : SINGER_PREFIX) {
				if(String.valueOf(arr).equals("#")){
					path = "&prefix=%2523&pn=1&rn=1";
				}else{
					path = "&prefix="+arr+"&pn=1&rn=1";
				}
				Document doc = JsoupUtil.getDocByConnectIgnoreContent(singerUrl+path);
				String text = doc.getElementsByTag("body").text();
				Map<String, Object> map = JsonUtil.toBean(text, Map.class);
				Map<String, Object> data = (Map<String, Object>) map.get("data");
				AlauwahiosDao.saveKuwoSingerTotal(String.valueOf(arr), data.get("total"), "");
			}
		} catch (Exception e) {
			logger.error("[kuwo资源抓取出错了]，将重新抓取", e);
		}
	}
	
	/**
	 * 1 歌手详细资料
	 */
	public void getKuwoSingerDetail() {
		String singerUrl = HOME_URL + "/api/www/artist/artistInfo?category=0";
		
		
		try {
			String path = "";
			for (char arr : SINGER_PREFIX) {
				if(String.valueOf(arr).equals("#")){
					path = "&prefix=%2523&pn=1&rn=1";
				}else{
					path = "&prefix="+arr+"&pn=1&rn=1";
				}
				Document doc = JsoupUtil.getDocByConnectIgnoreContent(singerUrl+path);
				String text = doc.getElementsByTag("body").text();
				Map<String, Object> map = JsonUtil.toBean(text, Map.class);
				Map<String, Object> data = (Map<String, Object>) map.get("data");
				AlauwahiosDao.saveKuwoSingerTotal(String.valueOf(arr), data.get("total"), "");
			}
		} catch (Exception e) {
			logger.error("[kuwo资源抓取出错了]，将重新抓取", e);
		}
	}

	public static void main(String[] args) {
		GetKuwoDB fx = new GetKuwoDB();
		fx.getKuwoSingerTotal();
	}
}
