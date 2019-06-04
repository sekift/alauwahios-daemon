package cn.alauwahios.daemon.site;

import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.alauwahios.daemon.dao.AlauwahiosDao;
import cn.alauwahios.daemon.dao.KuwoDao;
import cn.alauwahios.daemon.util.JsonUtil;
import cn.alauwahios.daemon.util.JsoupUtil;
import cn.alauwahios.daemon.util.SleepUtil;
import cn.alauwahios.daemon.vo.KuwoSingerNameVO;

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
	private static final String KUWO_URL = "http://www.kuwo.cn";

	// 歌手常数
	private static final char[] SINGER_PREFIX = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#".toCharArray();//ABCDEFGHIJKLMNOPQRSTUVWXYZ#

	/**
	 * 1 歌手按字母分类总数
	 */
	public void getKuwoSingerTotal() {
		String singerTotalUrl = KUWO_URL + "/api/www/artist/artistInfo?category=0";
		try {
			String path = "";
			for (char arr : SINGER_PREFIX) {
				if(String.valueOf(arr).equals("#")){
					path = "&prefix=%2523&pn=1&rn=1";
				}else{
					path = "&prefix="+arr+"&pn=1&rn=1";
				}
				Document doc = JsoupUtil.getDocByConnectIgnoreContent(singerTotalUrl+path);
				String text = doc.getElementsByTag("body").text();
				Map<String, Object> map = JsonUtil.toBean(text, Map.class);
				Map<String, Object> data = (Map<String, Object>) map.get("data");
				KuwoDao.saveKuwoSingerTotal(String.valueOf(arr), data.get("total"), "");
			}
		} catch (Exception e) {
			logger.error("[kuwo资源抓取出错了]，将重新抓取", e);
		}
	}
	
	/**
	 * 1 歌手kuwoid和name
	 */
	public void getKuwoSingerName() {
		String singerUrl = KUWO_URL + "/geci/artist_";
		String singerComUrl = "";
		try {
			for (char arr : SINGER_PREFIX) {
				String path = "";
				if (String.valueOf(arr).equals("#")) {
					path = "qita";
				} else {
					path = String.valueOf(arr);
				}
				int singerTotal = KuwoDao.getKuwoSingerTotal(arr + "");
				int pageCount = singerTotal / 200 + 1; // 200一页
				for (int page = 1; page <= pageCount; page++) {
					singerComUrl = singerUrl + path + "_" + page + ".htm";
					System.out.println(singerComUrl);
					Document doc = JsoupUtil.getDocByConnectIgnoreContent(singerComUrl);
					Elements eles = doc.getElementsByClass("songer_list").select("li");
					for (Element ele : eles) {
						KuwoSingerNameVO vo = new KuwoSingerNameVO();
						String curUrl = ele.select("a").attr("href");
						String singerName = ele.text();
						String singerIdStr = curUrl.replace(KUWO_URL + "/geci/a_", "").replace("/", "");
						vo.setSingerId(Integer.valueOf(singerIdStr));
						vo.setSingerName(singerName);
						vo.setCurUrl(curUrl);
						vo.setPreUrl(singerComUrl);
						vo.setPrefix(arr + "");
						vo.setRemark("");

						KuwoDao.saveKuwoSingerName(vo);
					}
					SleepUtil.sleepBySecond(1, 3);
				}
			}
		} catch (Exception e) {
			logger.error("[kuwo资源抓取出错了]，将重新抓取", e);
		}
	}
	
	/**
	 * 1 下载歌词
	 */
	public void getKuwoLyric() {
		String url = "http://m.kuwo.cn/newh5/singles/songinfoandlrc?musicId=6749207";
		try {
			Document doc = JsoupUtil.getDocByConnectIgnoreContent(url);
			System.out.println(doc.getElementsByTag("body").text());
			SleepUtil.sleepBySecond(1, 3);
		} catch (Exception e) {
			logger.error("[kuwo资源抓取出错了]，将重新抓取", e);
		}

	}

	public static void main(String[] args) {
		GetKuwoDB fx = new GetKuwoDB();
		fx.getKuwoLyric();
	}
}
