package cn.alauwahios.daemon.site;

import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.alauwahios.daemon.dao.KuwoDao;
import cn.alauwahios.daemon.util.JsonUtil;
import cn.alauwahios.daemon.util.JsoupUtil;
import cn.alauwahios.daemon.util.SleepUtil;
import cn.alauwahios.daemon.util.StringUtil;
import cn.alauwahios.daemon.vo.KuwoAlbumVO;
import cn.alauwahios.daemon.vo.KuwoLyricVO;
import cn.alauwahios.daemon.vo.KuwoMusicVO;
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
	 * 根据歌手获取歌曲信息
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
	
	http://www.kuwo.cn/api/www/artist/artistMusic?artistid=1&pn=1&rn=2
}
	 * 
	 */
	private static final String[] signalArray = {"{",":",",","}"};
	
	@SuppressWarnings("unchecked")
	public void getKuwoMusic(int singerBenId, int singerEndid,int pageNo, int pageSize) {//int id, int endId, 
		String singerUrl = KUWO_URL + "/api/www/artist/artistMusic?artistid=";
		String singerComUrl = "";
		int curId = 0;
		
		// 先去数据库查询是第几个
		singerBenId = KuwoDao.getKuwoSingerNum(singerBenId);
		try {
			// 歌手总数大概是23485个,每次取100个
			for (int beg = singerBenId; beg < 23485; beg += 100) {
				List<KuwoSingerInfoVO> singerIdList = KuwoDao.getKuwoSingerInfo(beg, 100);
				for (KuwoSingerInfoVO infoVo : singerIdList) {
					curId = infoVo.getArtistId();
//					if (curId < id) {
//						continue;
//					}
					if (curId >= singerEndid) {
						break;
					}
					int total = infoVo.getMusicNum();
					System.out.println(total);

					// 分页
//					int pageSize = 200;
					int pageCount = total / pageSize + 1;
					for (int page = pageNo; page <= pageCount; page++) {
						singerComUrl = singerUrl + curId + "&pn=" + page + "&rn=" + pageSize;
						Document doc = JsoupUtil.getDocByConnectIgnoreContent(singerComUrl);
						System.out.println(singerComUrl);
						String body = doc.getElementsByTag("body").text();
						
						// 去掉转义字符
						String bodyout = body.replaceAll("\\{\"", "{~");
						bodyout = bodyout.replaceAll("\"\\}", "~}");
						bodyout = bodyout.replaceAll("\",\"", "~,~");
						bodyout = bodyout.replaceAll("\":\"", "~:~");
						bodyout = bodyout.replaceAll("\":", "~:");
						bodyout = bodyout.replaceAll(":\"", ":~");
						bodyout = bodyout.replaceAll("\",", "~,");
						bodyout = bodyout.replaceAll(",\"", ",~");
						
						bodyout = bodyout.replaceAll("\"", "'");
						
						bodyout = bodyout.replaceAll("\\{~", "{\"");
						bodyout = bodyout.replaceAll("~\\}", "\"}");
						bodyout = bodyout.replaceAll("~,~", "\",\"");
						bodyout = bodyout.replaceAll("~:~", "\":\"");
						bodyout = bodyout.replaceAll("~:", "\":");
						bodyout = bodyout.replaceAll(":~", ":\"");
						bodyout = bodyout.replaceAll("~,", "\",");
						body = bodyout.replaceAll(",~", ",\"");
						
//						System.out.println(body);
						Map<String, Object> json = JsonUtil.toBean(body, Map.class);
						Map<String, Object> data = (Map<String, Object>) json.get("data");
						
						//修改页数
						String totalStr = (String) data.get("total");
						if(Integer.valueOf(totalStr) > total){							
							total = Integer.valueOf(totalStr);
							pageCount = total / pageSize + 1;
						}
						
						List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("list");
						for (Map<String, Object> map : list) {
//							System.out.println(map);
							KuwoMusicVO vo = new KuwoMusicVO();

							int musicId = Integer.valueOf(map.get("rid").toString());
							vo.setMusicId(Integer.valueOf(map.get("rid").toString()));
							vo.setMusicName(map.get("name").toString());
							vo.setMusicRid(map.get("musicrid").toString());
							vo.setArtistId(Integer.valueOf(map.get("artistid").toString()));
							vo.setArtistName(map.get("artist").toString());
							vo.setIsStar(Integer.valueOf(map.get("isstar").toString()));
							vo.setPay(Integer.valueOf(map.get("pay").toString()));

							vo.setHasMv(Integer.valueOf(map.get("hasmv").toString()));
							vo.setAlbumId(Integer.valueOf(map.get("albumid").toString()));
							vo.setPay(Integer.valueOf(map.get("pay").toString()));
							vo.setDuration(Integer.valueOf(map.get("duration").toString()));
							vo.setOnline(Integer.valueOf(map.get("online").toString()));
							vo.setTrack(Integer.valueOf(map.get("track").toString()));

							if (null != map.get("album")) {
								vo.setAlbumName(map.get("album").toString());
							}
							if (null != map.get("nationid")) {
								vo.setNationId(map.get("nationid").toString());
							}
							if (null != map.get("albumpic")) {
								vo.setAlbumPic(map.get("albumpic").toString());
							}
							if (null != map.get("songTimeMinutes")) {
								vo.setSongTimeMinutes(map.get("songTimeMinutes").toString());
							}
							if (null != map.get("pic120")) {
								vo.setPic120(map.get("pic120").toString());
							}
							if (null != map.get("isListenFee")) {
								boolean flag = Boolean.valueOf(map.get("isListenFee").toString());
								vo.setIsListenFee(flag ? 1 : 0);
							}
							if (null != map.get("hasLossless")) {
								boolean flag = Boolean.valueOf(map.get("hasLossless").toString());
								vo.setHasLossless(flag ? 1 : 0);
							}
							if (null != map.get("pic")) {
								vo.setPic(map.get("pic").toString());
							}
							if (null != map.get("releaseDate")) {
								vo.setReleaseDate(map.get("releaseDate").toString());
							}

							vo.setCurUrl(singerComUrl);
							vo.setRemark("");

//							System.out.println("VO= "+vo.toString());
							if(!KuwoDao.getKuwoMusicExists(musicId)){
							    KuwoDao.saveKuwoMusic(vo);
							}
							System.out.println("artistId="+vo.getArtistId()+";"+musicId);//+";artistName="+vo.getArtistName()
							
							// 先查这首歌是否已经有歌词，没有才去下
							if(!KuwoDao.getKuwoLyricExists(musicId)
									&& !KuwoDao.getKuwoLyricNoneExists(musicId)){
							    getKuwoLyric(musicId);
							}else{
								System.out.println("artistId="+vo.getArtistId()+";"+musicId+"; 歌词已下载");//+";artistName="+vo.getArtistName()
							}
//							SleepUtil.sleepBySecond(100, 20);
						}
						singerComUrl = "";
					}
				}
//				SleepUtil.sleepBySecond(2, 4);
			}
		} catch (Exception e) {
			logger.error("[kuwo资源抓取出错了]，将重新抓取; curUrl = " + singerComUrl, e);
			System.out.println(e);
			// SleepUtil.sleepBySecond(10, 20);
			// getKuwoAlbum(curId, 2);
		}
	}
	
	/**
	 * 根据歌曲id获取歌词
	 * {
	"data": {
		"lrclist": [{
			"time": "0.0",
			"lineLyric": "너 땜에 (因为你) - 채연 (蔡妍)"
		}, {
			"time": "175.95999",
			"lineLyric": "你看着我微笑 我还是会很担心"
		}],
		"songinfo": {
			"album": "只有你",
			"albumId": "20405",
			"artist": "蔡妍[]",
			"artistId": "1",
			"coopFormats": ["320kmp3", "192kmp3", "128kmp3"],
			"copyRight": "0",
			"duration": "190",
			"formats": "WMA96|WMA128|MP3128|MP3192|MP3H|AAC48",
			"hasEcho": null,
			"hasMv": "0",
			"id": "120782",
			"isExt": null,
			"isNew": null,
			"isPoint": "0",
			"isbatch": null,
			"isdownload": "0",
			"isstar": "0",
			"mkvNsig1": "0",
			"mkvNsig2": "0",
			"mkvRid": "MV_0",
			"mp3Nsig1": "286317765",
			"mp3Nsig2": "3759541851",
			"mp3Rid": "MP3_120782",
			"mp3Size": "",
			"mp4sig1": "",
			"mp4sig2": "",
			"musicrId": "MUSIC_120782",
			"mutiVer": "0",
			"mvpic": null,
			"nsig1": "2416845212",
			"nsig2": "2513287007",
			"online": "1",
			"params": null,
			"pay": "0",
			"pic": "http://img1.kwcdn.kuwo.cn/star/albumcover/240/64/53/3764338614.jpg",
			"playCnt": "277",
			"rankChange": null,
			"reason": null,
			"score": null,
			"score100": "41",
			"songName": "因为你",
			"songTimeMinutes": "03:10",
			"tpay": null,
			"trend": null,
			"upTime": "",
			"uploader": ""
		}
	},
	"msg": "成功",
	"msgs": null,
	"profileid": "site",
	"reqid": "c316d0a2X1d8bX4821Xab68X9fa8e2940b60",
	"status": 200
}
	 * http://m.kuwo.cn/newh5/singles/songinfoandlrc?musicId=6749207
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void getKuwoLyric(int musicId) {
		String url = "http://m.kuwo.cn/newh5/singles/songinfoandlrc?musicId="+musicId;
		try {
			
			Document doc = JsoupUtil.getDocByConnectIgnoreContent(url);
			String body = doc.getElementsByTag("body").text();
			
			// 先去掉无用的部分
			body = body.substring(0, body.indexOf(",\"simpl\":"));
			body = body + "}}";
			
			// 去掉转义字符
			String bodyout = body.replaceAll("\\{\"", "{~");
			bodyout = bodyout.replaceAll("\"\\}", "~}");
			bodyout = bodyout.replaceAll("\",\"", "~,~");
			bodyout = bodyout.replaceAll("\":\"", "~:~");
			bodyout = bodyout.replaceAll("\":", "~:");
			bodyout = bodyout.replaceAll(":\"", ":~");
			bodyout = bodyout.replaceAll("\",", "~,");
			bodyout = bodyout.replaceAll(",\"", ",~");
			
			bodyout = bodyout.replaceAll("\"", "'");
			
			bodyout = bodyout.replaceAll("\\{~", "{\"");
			bodyout = bodyout.replaceAll("~\\}", "\"}");
			bodyout = bodyout.replaceAll("~,~", "\",\"");
			bodyout = bodyout.replaceAll("~:~", "\":\"");
			bodyout = bodyout.replaceAll("~:", "\":");
			bodyout = bodyout.replaceAll(":~", ":\"");
			bodyout = bodyout.replaceAll("~,", "\",");
			body = bodyout.replaceAll(",~", ",\"");
			
//			System.out.println(body);
			Map<String, Object> json = JsonUtil.toBean(body, Map.class);
			Map<String, Object> data = (Map<String, Object>) json.get("data");
			// 歌词
			String lrcList = ""; 
			if(null != data.get("lrclist")){
				lrcList = data.get("lrclist").toString();
			}
			
			Map<String, Object> map = (Map<String, Object>) data.get("songinfo");
			// 其他
			KuwoLyricVO vo = new KuwoLyricVO();
			
			vo.setLrcList(lrcList);
			vo.setMusicId(Integer.valueOf(map.get("id").toString()));
			vo.setMusicName(map.get("songName").toString());
			vo.setArtistId(Integer.valueOf(map.get("artistId").toString()));
			vo.setArtistName(map.get("artist").toString());
			vo.setAlbumId(Integer.valueOf(map.get("albumId").toString()));
			vo.setAlbumName(map.get("album").toString());
			vo.setNsig1(map.get("nsig1").toString());
			vo.setNsig2(map.get("nsig2").toString());
			int playCnt = 0, score100 = 0;
			try{
				playCnt = Integer.valueOf(map.get("playCnt").toString());
			}catch(Exception e){}			
			vo.setPlayCnt(playCnt);
			
			try{
				score100 = Integer.valueOf(map.get("score100").toString());
			}catch(Exception e){}			
			vo.setScore100(score100);
			vo.setCurUrl(url);
			vo.setRemark("");
//			System.out.println(vo);

			boolean flag = false;
			if(StringUtil.isNullOrBlank(lrcList)){
				flag = KuwoDao.saveKuwoLyricNone(vo);
			}else{
				flag = KuwoDao.saveKuwoLyric(vo);
			}
			System.out.println("artistId="+vo.getArtistId()+";"+vo.getMusicId()+  " : " +flag);//+";artistName="+vo.getArtistName()
			SleepUtil.sleepBySecond(0, 1);
		} catch (Exception e) {
			logger.error("[kuwo资源抓取出错了]，将重新抓取,url="+url, e);
			
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
	public void getKuwoAlbum(int singerBenId, int endId, int currPage, int pageSize, int time1, int time2) {
		String singerUrl = KUWO_URL + "/api/www/artist/artistAlbum?artistid=";
		String singerComUrl = "";
		int curId = 0;
		try {
			singerBenId = KuwoDao.getKuwoSingerNum(singerBenId);
			List<KuwoSingerInfoVO> singerIdList = KuwoDao.getKuwoSingerInfo(singerBenId, endId);
			for (KuwoSingerInfoVO infoVo : singerIdList) {
				curId = infoVo.getArtistId();
				
				singerComUrl = singerUrl + curId + "&pn=1&rn=1";
				//System.out.println(singerComUrl);

				// 先获取总专辑数
				int total = infoVo.getAlbumNum();
				System.out.println(curId +";"+infoVo.getArtistName()+ " 的专辑总数为： " + total);

				// 分页
				int pageCount = total / pageSize + 1;
				for (int page = currPage; page <= pageCount; page++) {
					singerComUrl = singerUrl + curId + "&pn=" + page + "&rn="+pageSize;
					SleepUtil.sleepBySecond(time1, time2);
					Document doc = JsoupUtil.getDocByConnectIgnoreContent(singerComUrl);
					System.out.println(singerComUrl);
					String body = doc.getElementsByTag("body").text();
					
					if(body.contains("\"total\":\"0\"")){
						continue;
					}
//					System.out.println(body);
					body = body.substring(0, body.indexOf("}]},\"msg\""));
					body = body.substring(body.indexOf("\"albumList\":[{")+14);
					body = body.replace("},", ">=>>=>");
					body = body.replace(">=>>=>{", ">=>>=>");
					String[] bodyArray = body.split(">=>>=>");
					for (String arr : bodyArray) {
						// 处理albuminfo
						String albumInfo = "";
						if (arr.contains("\"albuminfo\":\"")) {
							int infoBegin = arr.indexOf("\"albuminfo\":\"") + 13;
							int infoEnd = arr.indexOf("\",\"artist\"");
//							System.out.println(arr);
							albumInfo = arr.substring(infoBegin, infoEnd);
							if (albumInfo.length() >= 15120) {
								albumInfo = albumInfo.substring(0, 15000) + "...";
							}

							arr = arr.substring(0, infoBegin) + arr.substring(infoEnd, arr.length());
						}
						
						String album = "";
						if (arr.contains("\"album\":\"")) {
							int infoBegin = arr.indexOf("\"album\":\"") + 9;
							int infoEnd = arr.indexOf("\",\"albumid\"");
//							System.out.println(arr);
							album = arr.substring(infoBegin, infoEnd);
							if (album.contains("\"")) {
								album = album.replace("\"", "'");
							}

							arr = arr.substring(0, infoBegin) + arr.substring(infoEnd, arr.length());
						}
						
						String artist = "";
						if (arr.contains("\"artist\":\"")) {
							int infoBegin = arr.indexOf("\"artist\":\"") + 10;
							int infoEnd = arr.indexOf("\",\"releaseDate\"");
//							System.out.println(arr);
							artist = arr.substring(infoBegin, infoEnd);
							if (artist.contains("\"")) {
								artist = artist.replace("\"", "'");
							}
							if(artist.length() >= 250){
								artist = artist.substring(0, 200) + "...";
							}

							arr = arr.substring(0, infoBegin) + arr.substring(infoEnd, arr.length());
						}
						
//						System.out.println(albumInfo+" ============= "+arr);
						arr = "{" + arr +"}";
						@SuppressWarnings("unchecked")
						Map<String, Object> map = null;
						try{
						    map = JsonUtil.toBean(arr, Map.class);
						}catch(Exception e){
							logger.error("[kuwo资源抓取出错了]:\t" + arr.replace("albuminfo\":\"", "albuminfo\":\""+albumInfo)
							                             .replace("album\":\"", "album\":\""+album) +"\t"+singerComUrl, e);
							continue;
						}
						KuwoAlbumVO vo = new KuwoAlbumVO();
						int albumId = Integer.valueOf(map.get("albumid").toString());
						vo.setAlbumId(albumId);
						vo.setAlbumName(album); //map.get("album").toString()
						int artistId = Integer.valueOf(map.get("artistid").toString());
//						if(artistId!=curId){//排除不是本歌手的专辑
//							continue;
//						}
						
						// 没有才去下
						if(KuwoDao.getKuwoAlbumExists(albumId)){
							System.out.println("albumId="+vo.getAlbumId()+";"+albumId+";albumName"+map.get("album").toString()+"; 专辑已下载");//+";artistName="+vo.getArtistName()
							continue;
						}
						
						vo.setArtistId(artistId);
						vo.setArtistName(artist);//map.get("artist").toString()
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

						boolean flag = KuwoDao.saveKuwoAlbum(vo);
						System.out.println("albumId=" + vo.getAlbumId()+";albumName="+ vo.getAlbumName() + "; "+flag);
					}
					singerComUrl = "";
				}
			}
		} catch (Exception e) {
			logger.error("[kuwo资源抓取出错了]，将重新抓取", e);
			e.printStackTrace();
//			SleepUtil.sleepBySecond(10, 20);
//			getKuwoAlbum(curId, 2, pageSize);
		}
	}
	
	// 播放音乐
	// http://m.kuwo.cn/h5app/api/music/play/15213376（musicId）

	public static void main(String[] args) {
		GetKuwoDB fx = new GetKuwoDB();
//		fx.getKuwoSingerInfo(149657,183106);
		
		/**
		 * 0 1 - 9113
		 * 1 9114 - 86206
		 * 2 86206 - 3773716
		 */
		fx.getKuwoMusic(3773716, 13773716, 1, 100);//歌手id起(排序)，歌手id尽（不是排序23486），页数，208586 1
//		fx.getGeciLyric();
//		fx.getKuwoLyric(5037080);
		
//		fx.getKuwoAlbum(1, 9113, 1, 100 ,0,0);//23486
	}
}
