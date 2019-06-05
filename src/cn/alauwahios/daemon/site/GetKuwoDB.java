package cn.alauwahios.daemon.site;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.alauwahios.daemon.dao.KuwoDao;
import cn.alauwahios.daemon.util.HtmlUtil;
import cn.alauwahios.daemon.util.JsonUtil;
import cn.alauwahios.daemon.util.JsoupUtil;
import cn.alauwahios.daemon.util.SleepUtil;
import cn.alauwahios.daemon.util.StringUtil;
import cn.alauwahios.daemon.vo.KuwoSingerBaseVO;
import cn.alauwahios.daemon.vo.KuwoSingerInfoVO;
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
				System.out.println(singerTotalUrl+path);
				Document doc = JsoupUtil.getDocByConnectIgnoreContent(singerTotalUrl+path);
				String text = doc.getElementsByTag("body").text();
				Map<String, Object> map = JsonUtil.toBean(text, Map.class);
				Map<String, Object> data = (Map<String, Object>) map.get("data");
//				KuwoDao.saveKuwoSingerTotal(String.valueOf(arr), data.get("total"), "");
				System.out.println(data);
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
	 * 获取歌手基本信息
	 * http://www.kuwo.cn/api/www/artist/artistInfo?category=0&prefix=%2523&pn=1&rn=1
	 * 返回
	 * 
	 * {
	"code": 200,
	"curTime": 1559701173398,
	"data": {
		"total": "317",
		"artistList": [{
			"artistFans": 94654,
			"albumNum": 143,
			"mvNum": 0,
			"pic": "http://img3.sycdn.kuwo.cn/star/starheads/300/92/66/1775174282.jpg",
			"musicNum": 1736,
			"pic120": "http://img3.sycdn.kuwo.cn/star/starheads/120/92/66/1775174282.jpg",
			"isStar": 0,
			"aartist": "",
			"name": "7妹",
			"pic70": "http://img3.sycdn.kuwo.cn/star/starheads/70/92/66/1775174282.jpg",
			"id": 215882,
			"pic300": "http://img3.sycdn.kuwo.cn/star/starheads/300/92/66/1775174282.jpg"
		}, {
			"artistFans": 14015,
			"albumNum": 16,
			"mvNum": 10,
			"pic": "http://img2.sycdn.kuwo.cn/star/starheads/300/5/44/1535995348.jpg",
			"musicNum": 87,
			"pic120": "http://img2.sycdn.kuwo.cn/star/starheads/120/5/44/1535995348.jpg",
			"isStar": 0,
			"aartist": "Lambsey",
			"name": "ラムジ",
			"pic70": "http://img2.sycdn.kuwo.cn/star/starheads/70/5/44/1535995348.jpg",
			"id": 19365,
			"pic300": "http://img2.sycdn.kuwo.cn/star/starheads/300/5/44/1535995348.jpg"
		}]
	},
	"msg": "success",
	"profileId": "site",
	"reqId": "02d161c4-2066-4df0-925b-0128a5012654"
}
	 */
	public void getKuwoSingerBase() {
		String singerUrl = KUWO_URL + "/api/www/artist/artistInfo?category=0&rn=2000&pn=1&prefix=";
		String singerComUrl = "";
		try {
			for (char arr : SINGER_PREFIX) {
				String path = "";
				if (String.valueOf(arr).equals("#")) {
					path = "%2523";
				} else {
					path = String.valueOf(arr);
				}
				singerComUrl = singerUrl + path;
				System.out.println(singerComUrl);
				Document doc = JsoupUtil.getDocByConnectIgnoreContent(singerComUrl);
				String body = doc.getElementsByTag("body").text();
				Map<String, Object> json = JsonUtil.toBean(body, Map.class);
				Map<String, Object> data = (Map<String, Object>) json.get("data");
				List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("artistList");
//				 System.out.println(json);

				for (Map<String, Object> map : list) {
					System.out.println(map);
					KuwoSingerBaseVO vo = new KuwoSingerBaseVO();
					vo.setId(Integer.valueOf(map.get("id").toString()));
					vo.setName(map.get("name").toString());
					vo.setAartist(map.get("aartist").toString());
					vo.setPrefix(arr + "");
					vo.setIsStar(Integer.valueOf(map.get("isStar").toString()));
					vo.setAlbumNum(Integer.valueOf(map.get("albumNum").toString()));
					vo.setMvNum(Integer.valueOf(map.get("mvNum").toString()));
					vo.setMusicNum(Integer.valueOf(map.get("musicNum").toString()));
					vo.setArtistFans(Integer.valueOf(map.get("artistFans").toString()));
					if(null != map.get("pic")){
					vo.setPic(map.get("pic").toString());
					}
					if(null != map.get("pic70")){
					vo.setPic70(map.get("pic70").toString());
					}
					if(null != map.get("pic120")){
					vo.setPic120(map.get("pic120").toString());
					}
					if(null != map.get("pic300")){
					vo.setPic300(map.get("pic300").toString());
					}
					vo.setCurUrl(singerComUrl);
					vo.setRemark("");

					KuwoDao.saveKuwoSingerBase(vo);
				}
				SleepUtil.sleepBySecond(10, 20);
			}
		} catch (Exception e) {
			logger.error("[kuwo资源抓取出错了]，将重新抓取", e);
		}
	}
	
	/**
	 * 获取歌手详细信息
	 * http://www.kuwo.cn/api/www/artist/artist?artistid=1
	 * 返回
	 */
	public void getKuwoSingerInfo(int id, int endId) {
		String singerUrl = KUWO_URL + "/api/www/artist/artist?artistid=";
		String singerComUrl = "";
		int curId= 0;
		try {
			List<Object> singerIdList = KuwoDao.getKuwoSingerId();
			for (Object obj : singerIdList) { // 149657,183100 未爬
				curId=Integer.valueOf(obj.toString());
				if(curId<id){
					continue;
				}
				singerComUrl = singerUrl + obj;
				System.out.println(singerComUrl);
				if(curId>=endId){
					break;
				}
				
				Document doc = JsoupUtil.getDocByConnectIgnoreContent(singerComUrl);
				String body = doc.getElementsByTag("body").text();
				System.out.println(body);
				// 先提取info避免后面json出问题
				String info = "";
				if (body.contains("\"info\":\"")) {
					int infoBegin = body.indexOf("\"info\":\"") + 8;
					int infoEnd = body.indexOf("\"},\"msg\"");
					info = body.substring(infoBegin, infoEnd);
					if (info.length() > 15120) {
						info = info.substring(0, 15000) + "...";
					}

					body = body.substring(0, infoBegin) + body.substring(infoEnd, body.length());
				}
				Map<String, Object> json = JsonUtil.toBean(body, Map.class);
				Map<String, Object> map = (Map<String, Object>) json.get("data");
//				System.out.println(map);

				KuwoSingerInfoVO vo = new KuwoSingerInfoVO();
				vo.setId(Integer.valueOf(map.get("id").toString()));
				vo.setName(map.get("name").toString());
				vo.setAartist(map.get("aartist").toString());
				vo.setIsStar(Integer.valueOf(map.get("isStar").toString()));
				vo.setAlbumNum(Integer.valueOf(map.get("albumNum").toString()));
				vo.setMvNum(Integer.valueOf(map.get("mvNum").toString()));
				vo.setMusicNum(Integer.valueOf(map.get("musicNum").toString()));
				vo.setArtistFans(Integer.valueOf(map.get("artistFans").toString()));
				if (null != map.get("pic")) {
					vo.setPic(map.get("pic").toString());
				}
				if (null != map.get("pic70")) {
					vo.setPic70(map.get("pic70").toString());
				}
				if (null != map.get("pic120")) {
					vo.setPic120(map.get("pic120").toString());
				}
				if (null != map.get("pic300")) {
					vo.setPic300(map.get("pic300").toString());
				}
				vo.setCurUrl(singerComUrl);
				vo.setRemark("");
				
				if(null != map.get("birthday")){
					vo.setBirthday(map.get("birthday").toString());
				}
				if(null != map.get("country")){
					vo.setCountry(map.get("country").toString());
				}
				if(null != map.get("gener")){
					vo.setGener(map.get("gener").toString());
				}
				if(null != map.get("weight")){
					vo.setWeight(map.get("weight").toString());
				}
				if(null != map.get("language")){
					vo.setLanguage(map.get("language").toString());
				}
				if(null != map.get("upPcUrl")){
					vo.setUpPcUrl(map.get("upPcUrl").toString());
				}
				if(null != map.get("birthplace")){
					vo.setBirthplace(map.get("birthplace").toString());
				}
				if(null != map.get("constellation")){
					vo.setConstellation(map.get("constellation").toString());
				}
				if(null != map.get("tall")){
					vo.setTall(map.get("tall").toString());
				}
				vo.setInfo(info);

				KuwoDao.saveKuwoSingerInfo(vo);
				SleepUtil.sleepBySecond(0, 0);
			}
		} catch (Exception e) {
			logger.error("[kuwo资源抓取出错了]，将重新抓取", e);
			SleepUtil.sleepBySecond(10, 20);
			getKuwoSingerInfo(curId, 183100);
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
	
	/**
	 * geci网的http://geci.me/song/1581878
	 * 
	 * @param args
	 */
	public void getGeciLyric() {
		String url = "http://geci.me/song/";
		try {
			for(int i=416;i<1200;i++){
			Document doc = JsoupUtil.getDocByConnectIgnoreContent(url+i);
			if(doc==null){
				continue;
			}
			System.out.println(url);
			
			System.out.println("歌曲名:"+doc.getElementsByTag("td").get(2));
			System.out.println("艺术家:"+doc.getElementsByTag("td").get(5));
			System.out.println("专辑:"+doc.getElementsByTag("td").get(7));
			System.out.println("语种:"+doc.getElementsByTag("td").get(9));
			System.out.println("发行时间:"+doc.getElementsByTag("td").get(11));
			System.out.println("唱片公司:"+doc.getElementsByTag("td").get(13));
			
			System.out.println("歌词:"+doc.getElementsByTag("p"));
			SleepUtil.sleepBySecond(111, 311);
			}
		} catch (Exception e) {
			logger.error("[kuwo资源抓取出错了]，将重新抓取", e);
		}
	}
	

	public static void main(String[] args) {
		GetKuwoDB fx = new GetKuwoDB();
		fx.getKuwoSingerInfo(149657,183100);
//		fx.getGeciLyric();
	}
}
