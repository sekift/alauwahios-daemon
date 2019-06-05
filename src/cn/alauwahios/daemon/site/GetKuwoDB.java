package cn.alauwahios.daemon.site;

import java.util.ArrayList;
import java.util.HashMap;
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
import cn.alauwahios.daemon.vo.KuwoAlbumVO;
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
						vo.setArtistId(Integer.valueOf(singerIdStr));
						vo.setArtistName(singerName);
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
					vo.setArtistId(Integer.valueOf(map.get("id").toString()));
					vo.setArtistName(map.get("name").toString());
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
				vo.setArtistId(Integer.valueOf(map.get("id").toString()));
				vo.setArtistName(map.get("name").toString());
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
	 * 根据歌手获取专辑信息
	 * 
	 * {
	"code": 200,
	"curTime": 1559744736501,
	"data": {
		"total": "16",
		"albumList": [{
			"albuminfo": "我也应该是被疼爱的灰姑娘&lt;br&gt;可你不愿用一生陪我赌一个过往&lt;br&gt;也会有一个人与我互诉衷肠&lt;br&gt;丢掉过往的伤&lt;br&gt;也会有一个人与我互诉衷肠&lt;br&gt;会有吗...",
			"artist": "陈雪凝",
			"releaseDate": "2019-05-09",
			"album": "灰姑娘",
			"albumid": 9909242,
			"pay": 0,
			"artistid": 1486611,
			"pic": "http://img1.kwcdn.kuwo.cn/star/albumcover/300/17/43/1340289947.jpg",
			"isstar": 0,
			"lang": "国语"
		}, {
			"albuminfo": "陈雪凝二专第五支单曲《你的酒馆对我打了烊》&lt;br&gt;词曲：陈雪凝&lt;br&gt;和声：李美灵芝&lt;br&gt;混音：鍾澤鑫&lt;br&gt;希望能在夜晚触碰到你最真实的情感",
			"artist": "陈雪凝",
			"releaseDate": "2019-02-19",
			"album": "你的酒馆对我打了烊",
			"albumid": 9041133,
			"pay": 0,
			"artistid": 1486611,
			"pic": "http://img1.kwcdn.kuwo.cn/star/albumcover/300/57/5/2300106887.jpg",
			"isstar": 0,
			"lang": "国语"
		}]
	},
	"msg": "success",
	"profileId": "site",
	"reqId": "7eb245de-ecbe-4a0d-899a-8daac6d2c45d"
}

http://www.kuwo.cn/api/www/artist/artistAlbum?artistid=1486611&pn=1&rn=28
	 */
	public void getKuwoAlbum(int id, int endId) {
		String singerUrl = KUWO_URL + "/api/www/artist/artistAlbum?artistid=";
		String singerComUrl = "";
		int curId = 0;
		try {
			List<Object> singerIdList = KuwoDao.getKuwoSingerId();
			for (Object obj : singerIdList) {
				curId = Integer.valueOf(obj.toString());
				if (curId < id) {
					continue;
				}
				singerComUrl = singerUrl + obj + "&pn=1&rn=1";
				System.out.println(singerComUrl);
				if (curId >= endId) {
					break;
				}

				// 先获取总专辑数
				Document doc = JsoupUtil.getDocByConnectIgnoreContent(singerComUrl);
				String body = doc.getElementsByTag("body").text();
				// 由于json可能出错，使用截取获取total
				body = body.substring(0, body.indexOf("\",\"albumList\""));
				body = body.substring(body.indexOf("\"total\":\"")+9);
				int total = Integer.valueOf(body);
				System.out.println(total);

				// 分页
				int pageSize = 2;
				int pageCount = total / pageSize + 1;
				for (int page = 1; page <= pageCount; page++) {
					singerComUrl = singerUrl + obj + "&pn=" + page + "&rn="+pageSize;
					doc = JsoupUtil.getDocByConnectIgnoreContent(singerComUrl);
					System.out.println(singerComUrl);
					body = doc.getElementsByTag("body").text();
					
					body = body.substring(0, body.indexOf("}]},\"msg\""));
					body = body.substring(body.indexOf("\"albumList\":[{")+14);
					body = body.replace("},", "====");
					body = body.replace("===={", "====");
					System.out.println(body);
					String[] bodyArray = body.split("====");
					for (String arr : bodyArray) {
						System.out.println(arr);
						
						// 处理albuminfo
						String albumInfo = "";
						if (arr.contains("\"albuminfo\":\"")) {
							int infoBegin = arr.indexOf("\"albuminfo\":\"") + 13;
							int infoEnd = arr.indexOf("\",\"artist\"");
							albumInfo = arr.substring(infoBegin, infoEnd);
							if (albumInfo.length() > 15120) {
								albumInfo = albumInfo.substring(0, 15000) + "...";
							}

							arr = arr.substring(0, infoBegin) + arr.substring(infoEnd, arr.length());
						}
						
						System.out.println(albumInfo+" ============= "+arr);
						arr = "{" + arr +"}";
						Map<String, Object> map = JsonUtil.toBean(arr, Map.class);
						KuwoAlbumVO vo = new KuwoAlbumVO();
						vo.setAlbumId(Integer.valueOf(map.get("albumid").toString()));
						vo.setAlbumName(map.get("album").toString());
						int artistId = Integer.valueOf(map.get("artistid").toString());
						if(artistId!=curId){//排除不是本歌手的专辑
							continue;
						}
						
						vo.setArtistId(artistId);
						vo.setArtistName(map.get("artist").toString());
						vo.setIsStar(Integer.valueOf(map.get("isstar").toString()));
						vo.setPay(Integer.valueOf(map.get("pay").toString()));
						if (null != map.get("pic")) {
							vo.setPic(map.get("pic").toString());
						}
						if (null != map.get("releaseDate")) {
							vo.setReleaseDate(map.get("releaseDate").toString());
						}
						if (null != map.get("lang")) {
							vo.setLanguage(map.get("lang").toString());
						}
						
						vo.setAlbumInfo(albumInfo);
						vo.setCurUrl(singerComUrl);
						vo.setRemark("");

						KuwoDao.saveKuwoAlbum(vo);
						SleepUtil.sleepBySecond(10, 20);
					}
					singerComUrl = "";
				}
			}
		} catch (Exception e) {
			logger.error("[kuwo资源抓取出错了]，将重新抓取", e);
			System.out.println(e);
			SleepUtil.sleepBySecond(10, 20);
			getKuwoAlbum(curId, 2);
		}
	}
	
	/**
	 * 根据歌手获取歌曲信息
	 * http://www.kuwo.cn/api/www/artist/artistMusic?artistid=1&pn=1&rn=2
	 * {
	"code": 200,
	"curTime": 1559744683660,
	"data": {
		"total": "436",
		"list": [{
			"musicrid": "MUSIC_56628058",
			"hasmv": 1,
			"artist": "蔡妍[채연]",
			"releaseDate": "2018-11-12",
			"album": "Bazzaya",
			"albumid": 7457174,
			"pay": "16515324",
			"artistid": 1,
			"albumpic": "http://img3.kuwo.cn/star/albumcover/500/98/24/899648361.jpg",
			"songTimeMinutes": "03:31",
			"pic": "http://img3.kuwo.cn/star/albumcover/300/98/24/899648361.jpg",
			"isstar": 0,
			"rid": 56628058,
			"isListenFee": false,
			"duration": 211,
			"pic120": "http://img3.kuwo.cn/star/albumcover/120/98/24/899648361.jpg",
			"name": "最美的期待",
			"online": 1,
			"track": 4,
			"hasLossless": true
		}, {
			"musicrid": "MUSIC_252018",
			"hasmv": 1,
			"artist": "蔡妍[채연]",
			"releaseDate": "1970-01-01",
			"album": "",
			"albumid": 0,
			"nationid": "0",
			"pay": "16515324",
			"artistid": 1,
			"albumpic": "http://img2.sycdn.kuwo.cn/star/starheads/120/86/40/4158031293.jpg",
			"songTimeMinutes": "03:05",
			"pic": "http://img2.sycdn.kuwo.cn/star/starheads/300/86/40/4158031293.jpg",
			"isstar": 0,
			"rid": 252018,
			"isListenFee": false,
			"duration": 185,
			"pic120": "http://img2.sycdn.kuwo.cn/star/starheads/120/86/40/4158031293.jpg",
			"name": "둘이서-[两个人]",
			"online": 1,
			"track": 1,
			"hasLossless": true
		}]
	},
	"msg": "success",
	"profileId": "site",
	"reqId": "78f3e939-a605-4d8d-9efa-c19b653e61e0"
}
	 * 
	 */
	public void getKuwoMusic(int id, int endId) {
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
				SleepUtil.sleepBySecond(110,110);
			}
		} catch (Exception e) {
			logger.error("[kuwo资源抓取出错了]，将重新抓取", e);
			SleepUtil.sleepBySecond(10, 20);
			getKuwoSingerInfo(curId, 183100);
		}
	}
	
	/**
	 * 根据歌曲id获取歌词
	 * http://m.kuwo.cn/newh5/singles/songinfoandlrc?musicId=6749207
	 * 
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
//		fx.getKuwoSingerInfo(149657,183100);
		fx.getKuwoAlbum(0, 2);
//		fx.getGeciLyric();
	}
}
