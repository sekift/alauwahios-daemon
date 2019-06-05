package cn.alauwahios.daemon.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.alauwahios.daemon.Constants;
import cn.alauwahios.daemon.dao.impl.DBOperate;
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
	 * 获取kuwo歌手Id
	 * @param args
	 */
	public static List<Object> getKuwoSingerId(){
		String sql = "SELECT id FROM kuwo_singer_base";
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
		String sql = "INSERT INTO kuwo_singer_name(singerId,singerName,"
				+ " prefix,curUrl,preUrl,createTime,updateTime,remark)"
				+ " VALUES(?,?,?,?,?,now(),now(),?)";
		List<Object> params = new ArrayList<Object>();
		params.add(vo.getSingerId());
		params.add(vo.getSingerName());
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
		String sql = "INSERT INTO kuwo_singer_base(id,aartist,"
				+ " name,prefix,isStar,albumNum,mvNum,musicNum,artistFans,"
				+ " pic,pic70,pic120,pic300,curUrl,createTime,updateTime,remark)"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?)"
				+ "  ON DUPLICATE KEY UPDATE updateTime=now(),albumNum=?,mvNum=?,musicNum=?,artistFans=?";
		List<Object> params = new ArrayList<Object>();
		params.add(vo.getId());
		params.add(vo.getAartist());
		params.add(vo.getName());
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
		String sql = "INSERT INTO kuwo_singer_info(id,aartist,"
				+ " name,isStar,albumNum,mvNum,musicNum,artistFans,"
				+ " pic,pic70,pic120,pic300,curUrl,createTime,updateTime,remark,"
				+ " birthday,country,gener,weight,language,upPcUrl,birthplace,constellation,tall,info)"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?,?,?,?,?,?,?,?,?,?,?)"
				+ "  ON DUPLICATE KEY UPDATE updateTime=now(),albumNum=?,mvNum=?,musicNum=?,artistFans=?,info=?";
		List<Object> params = new ArrayList<Object>();
		params.add(vo.getId());
		params.add(vo.getAartist());
		params.add(vo.getName());
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

	public static void main(String args[]) {
//		System.out.println(saveKuwoSingerTotal("A", 100, ""));
		System.out.println(getKuwoSingerId());
		
	}
}
