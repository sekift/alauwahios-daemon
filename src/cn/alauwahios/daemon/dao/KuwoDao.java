package cn.alauwahios.daemon.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.alauwahios.daemon.Constants;
import cn.alauwahios.daemon.dao.impl.DBOperate;
import cn.alauwahios.daemon.vo.KuwoAlbumVO;
import cn.alauwahios.daemon.vo.KuwoLyricVO;
import cn.alauwahios.daemon.vo.KuwoMusicVO;
import cn.alauwahios.daemon.vo.KuwoSingerBaseVO;
import cn.alauwahios.daemon.vo.KuwoSingerInfoVO;
import cn.alauwahios.daemon.vo.KuwoSingerNameVO;

/**
 * 操作数据库DAO层
 * 
 * @author:sekift
 * @time:2016-7-26 下午04:15:30
 */
public class KuwoDao {
	private static Logger logger = LoggerFactory.getLogger(KuwoDao.class);
	
	/**
	 * kuwo歌手总数
	 * @param prefix
	 * @param total
	 * @return
	 */
	public static boolean saveKuwoSingerTotal(String prefix, Object total, String remark) {
		boolean result = false;
		String sql = "INSERT INTO kuwo_singer_total(prefix,total,createTime,updateTime,remark)"
				+ " VALUES(?,?,now(),now(),?) ON DUPLICATE KEY UPDATE updateTime=now(),total=?";
		List<Object> params = new ArrayList<Object>();
		params.add(prefix);
		params.add(total);
		params.add(remark);
		
		params.add(total);
		
		try {
			result = DBOperate.update(Constants.ALIAS_MASTER, sql, params.toArray()) > 0;
		} catch (Exception e) {
			logger.error("[酷我信息]插入歌手总数出错", e);
		}
		return result;
	}
	
	/**
	 * 获取kuwo歌手总数
	 * @param args
	 */
	public static int getKuwoSingerTotal(String prefix){
		String sql = "SELECT total FROM kuwo_singer_total WHERE prefix=?";
		int total = (Integer) DBOperate.query4ObjectQuietly(Constants.ALIAS_SLAVE, sql, prefix);
		return total;
	}
	
	/**
	 * 获取kuwo的第几个歌手
	 * @param args
	 */
	public static int getKuwoSingerNum(int artistId){
		String sql = "select count(*) from kuwo_singer_info where artistId<?;";
		Long total = (Long) DBOperate.query4ObjectQuietly(Constants.ALIAS_SLAVE, sql, artistId);
		return total.intValue();
	}
	
	/**
	 * 获取kuwo歌手Id
	 * @param args
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<KuwoSingerInfoVO> getKuwoSingerInfo(int beginId, int endId){
		String sql = "SELECT artistId,artistName,albumNum,musicNum FROM kuwo_singer_info ORDER BY artistId asc LIMIT ?,?";
		List<KuwoSingerInfoVO> list = (List<KuwoSingerInfoVO>) DBOperate.queryQuietly(Constants.ALIAS_SLAVE, sql,
				new BeanListHandler(KuwoSingerInfoVO.class), beginId, endId);
		return list;
	}
	/**
	 * 
	 * 获取kuwo歌手Id和
	 * @param args
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Object> getKuwoSingerId(){
		String sql = "SELECT artistId FROM kuwo_singer_base";
		List<Object> list = (List<Object>) DBOperate.queryQuietly(Constants.ALIAS_SLAVE, sql,
				new ColumnListHandler());
		return list;
	}
	
	/**
	 *  歌手kuwoid和name
	 * @param vo
	 * @return
	 */
	public static boolean saveKuwoSingerName(KuwoSingerNameVO vo) {
		boolean result = false;
		if (null == vo) {
			return result;
		}
		String sql = "INSERT INTO kuwo_singer_name(artistId,artistName,"
				+ " prefix,curUrl,preUrl,createTime,updateTime,remark)"
				+ " VALUES(?,?,?,?,?,now(),now(),?)";
		List<Object> params = new ArrayList<Object>();
		params.add(vo.getArtistId());
		params.add(vo.getArtistName());
		params.add(vo.getPrefix());
		params.add(vo.getCurUrl());
		params.add(vo.getPreUrl());
		params.add(vo.getRemark());
		
		try {
			result = DBOperate.update(Constants.ALIAS_MASTER, sql, params.toArray()) > 0;
		} catch (Exception e) {
			result = false;
			logger.error("[酷我信息]插入数据出错", e);
		}
		return result;
	}
	
	/**
	 *  kuwo歌手基本信息
	 * @param vo
	 * @return
	 */
	public static boolean saveKuwoSingerBase(KuwoSingerBaseVO vo) {
		boolean result = false;
		if (null == vo) {
			return result;
		}
		String sql = "INSERT INTO kuwo_singer_base(artistId,aartist,"
				+ " artistName,prefix,isStar,albumNum,mvNum,musicNum,artistFans,"
				+ " pic,pic70,pic120,pic300,curUrl,createTime,updateTime,remark)"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?)"
				+ "  ON DUPLICATE KEY UPDATE updateTime=now(),albumNum=?,mvNum=?,musicNum=?,artistFans=?";
		List<Object> params = new ArrayList<Object>();
		params.add(vo.getArtistId());
		params.add(vo.getAartist());
		params.add(vo.getArtistName());
		params.add(vo.getPrefix());
		params.add(vo.getIsStar());
		params.add(vo.getAlbumNum());
		params.add(vo.getMvNum());
		params.add(vo.getMusicNum());
		params.add(vo.getArtistFans());
		params.add(vo.getPic());
		params.add(vo.getPic70());
		params.add(vo.getPic120());
		params.add(vo.getPic300());
		params.add(vo.getCurUrl());
		params.add(vo.getRemark());
		
		params.add(vo.getAlbumNum());
		params.add(vo.getMvNum());
		params.add(vo.getMusicNum());
		params.add(vo.getArtistFans());
		
		try {
			result = DBOperate.update(Constants.ALIAS_MASTER, sql, params.toArray()) > 0;
		} catch (Exception e) {
			result = false;
			logger.error("[酷我信息]插入数据出错", e);
		}
		return result;
	}
	
	/**
	 *  kuwo歌手详细信息
	 * @param vo
	 * @return
	 */
	public static boolean saveKuwoSingerInfo(KuwoSingerInfoVO vo) {
		boolean result = false;
		if (null == vo) {
			return result;
		}
		String sql = "INSERT INTO kuwo_singer_info(artistId,aartist,"
				+ " artistName,isStar,albumNum,mvNum,musicNum,artistFans,"
				+ " pic,pic70,pic120,pic300,curUrl,createTime,updateTime,remark,"
				+ " birthday,country,gener,weight,language,upPcUrl,birthplace,constellation,tall,info)"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?,?,?,?,?,?,?,?,?,?,?)"
				+ " ON DUPLICATE KEY UPDATE updateTime=now(),albumNum=?,mvNum=?,musicNum=?,artistFans=?,info=?";
		List<Object> params = new ArrayList<Object>();
		params.add(vo.getArtistId());
		params.add(vo.getAartist());
		params.add(vo.getArtistName());
		params.add(vo.getIsStar());
		params.add(vo.getAlbumNum());
		params.add(vo.getMvNum());
		params.add(vo.getMusicNum());
		params.add(vo.getArtistFans());
		params.add(vo.getPic());
		params.add(vo.getPic70());
		params.add(vo.getPic120());
		params.add(vo.getPic300());
		params.add(vo.getCurUrl());
		params.add(vo.getRemark());

		params.add(vo.getBirthday());
		params.add(vo.getCountry());
		params.add(vo.getGener());
		params.add(vo.getWeight());
		params.add(vo.getLanguage());
		params.add(vo.getUpPcUrl());
		params.add(vo.getBirthplace());
		params.add(vo.getConstellation());
		params.add(vo.getTall());
		params.add(vo.getInfo());
		
		params.add(vo.getAlbumNum());
		params.add(vo.getMvNum());
		params.add(vo.getMusicNum());
		params.add(vo.getArtistFans());
		params.add(vo.getInfo());
		
		try {
			result = DBOperate.update(Constants.ALIAS_MASTER, sql, params.toArray()) > 0;
		} catch (Exception e) {
			result = false;
			logger.error("[酷我信息]插入数据出错", e);
		}
		return result;
	}
	
	/**
	 *  kuwo歌曲基本信息
	 * @param vo
	 * @return
	 */
	public static boolean saveKuwoMusic(KuwoMusicVO vo) {
		boolean result = false;
		if (null == vo) {
			return result;
		}
		String sql = "INSERT INTO kuwo_music("
				+ " musicId,musicRid,musicName,albumId,albumName,artistId,artistName,"
				+ " hasMv,isStar,isListenFee,online,pay,nationId,track,albumPic,"
				+ " pic,pic120,hasLossless,songTimeMinutes,releaseDate,duration,"
				+ " curUrl,createTime,updateTime,remark)"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?)"
				+ "  ON DUPLICATE KEY UPDATE updateTime=now()";
		List<Object> params = new ArrayList<Object>();
		params.add(vo.getMusicId());
		params.add(vo.getMusicRid());
		params.add(vo.getMusicName());
		params.add(vo.getAlbumId());
		params.add(vo.getAlbumName());
		params.add(vo.getArtistId());
		params.add(vo.getArtistName());
		params.add(vo.getHasMv());
		params.add(vo.getIsStar());
		params.add(vo.getIsListenFee());
		params.add(vo.getOnline());
		params.add(vo.getPay());
		params.add(vo.getNationId());
		params.add(vo.getTrack());
		params.add(vo.getAlbumPic());
		params.add(vo.getPic());
		params.add(vo.getPic120());
		params.add(vo.getHasLossless());
		params.add(vo.getSongTimeMinutes());
		params.add(vo.getReleaseDate());
		params.add(vo.getDuration());
		params.add(vo.getCurUrl());
		params.add(vo.getRemark());
		try {
			result = DBOperate.update(Constants.ALIAS_MASTER, sql, params.toArray()) > 0;
		} catch (Exception e) {
			result = false;
			logger.error("[酷我信息]插入数据出错", e);
		}
		return result;
	}
	
	// 歌词数据库名称
	public static final String KUWO_LYRIC_NAME = "kuwo_lyric_2";
	
	
	/**
	 * 获取kuwo歌词是否已经下载
	 * @param args
	 */
	public static boolean getKuwoLyricExists(int musicId){
		String sql = "SELECT * FROM "+KUWO_LYRIC_NAME+" WHERE musicId=?";
		KuwoLyricVO vo = (KuwoLyricVO) DBOperate.queryQuietly(Constants.ALIAS_SLAVE, sql,new BeanHandler(KuwoLyricVO.class), musicId);
		boolean flag = false;
		if(null!=vo){
			flag = true;
		}
		return flag;
	}
	
	/**
	 *  kuwo歌曲基本信息
	 * @param vo
	 * @return
	 */
	public static boolean saveKuwoLyric(KuwoLyricVO vo) {
		boolean result = false;
		if (null == vo) {
			return result;
		}
		String sql = "INSERT INTO "+KUWO_LYRIC_NAME+"("
				+ " musicId,musicName,albumId,albumName,artistId,artistName,"
				+ " lrcList,nsig1,nsig2,score100,playCnt,"
				+ " curUrl,createTime,updateTime,remark)"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?)"
				+ "  ON DUPLICATE KEY UPDATE updateTime=now()";
		List<Object> params = new ArrayList<Object>();
		params.add(vo.getMusicId());
		params.add(vo.getMusicName());
		params.add(vo.getAlbumId());
		params.add(vo.getAlbumName());
		params.add(vo.getArtistId());
		params.add(vo.getArtistName());
		params.add(vo.getLrcList());
		params.add(vo.getNsig1());
		params.add(vo.getNsig2());
		params.add(vo.getScore100());
		params.add(vo.getPlayCnt());
		params.add(vo.getCurUrl());
		params.add(vo.getRemark());
		try {
			result = DBOperate.update(Constants.ALIAS_MASTER, sql, params.toArray()) > 0;
		} catch (Exception e) {
			logger.error("[酷我信息]插入数据出错", e);
		}
		return result;
	}
	
	/**
	 *  kuwo专辑信息
	 * @param vo
	 * @return
	 */
	public static boolean saveKuwoAlbum(KuwoAlbumVO vo) {
		boolean result = false;
		if (null == vo) {
			return result;
		}
		String sql = "INSERT INTO kuwo_album(albumId,albumName,artistId,"
				+ " artistName,isStar,pay,pic,language,releaseDate,"
				+ " albumInfo,curUrl,createTime,updateTime,remark)"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,now(),now(),?)"
				+ " ON DUPLICATE KEY UPDATE updateTime=now()";
		List<Object> params = new ArrayList<Object>();
		params.add(vo.getAlbumId());
		params.add(vo.getAlbumName());
		params.add(vo.getArtistId());
		params.add(vo.getArtistName());
		params.add(vo.getIsStar());
		params.add(vo.getPay());
		params.add(vo.getPic());
		params.add(vo.getLanguage());
		params.add(vo.getReleaseDate());
		params.add(vo.getAlbumInfo());
		params.add(vo.getCurUrl());
		params.add(vo.getRemark());
		try {
			result = DBOperate.update(Constants.ALIAS_MASTER, sql, params.toArray()) > 0;
		} catch (Exception e) {
			result = false;
			logger.error("[酷我信息]插入数据出错", e);
		}
		return result;
	}

	public static void main(String args[]) {
//		System.out.println(saveKuwoSingerTotal("A", 100, ""));
		System.out.println(getKuwoLyricExists(54316));
		
	}
}
