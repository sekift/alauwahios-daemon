package cn.alauwahios.daemon.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.alauwahios.daemon.Constants;
import cn.alauwahios.daemon.dao.impl.DBOperate;
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

	public static void main(String args[]) {
//		System.out.println(saveKuwoSingerTotal("A", 100, ""));
		System.out.println(getKuwoSingerTotal("A"));
		
	}
}
