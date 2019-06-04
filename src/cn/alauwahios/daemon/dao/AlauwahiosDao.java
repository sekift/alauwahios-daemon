package cn.alauwahios.daemon.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.alauwahios.daemon.Constants;
import cn.alauwahios.daemon.dao.impl.DBOperate;
import cn.alauwahios.daemon.vo.BaiduTiebaVO;
import cn.alauwahios.daemon.vo.BaiduTieziVO;
import cn.alauwahios.daemon.vo.BaiduWangpanVO;
import cn.alauwahios.daemon.vo.BaiduYunVO;
import cn.alauwahios.daemon.vo.FxZiyuanVO;

/**
 * 操作数据库DAO层
 * 
 * @author:sekift
 * @time:2016-7-26 下午04:15:30
 */
public class AlauwahiosDao {
	private static Logger logger = LoggerFactory.getLogger(AlauwahiosDao.class);

	/**
	 * 网盘群
	 * @param vo
	 * @return
	 */
	public static boolean saveBaiduWangpan(BaiduWangpanVO vo) {
		boolean result = false;
		if (null == vo) {
			return result;
		}
		String sql = "INSERT INTO baidu_wangpan(panShortLink,panLink,replyName,replyLink,tiebaName,"
				+ " tiebaLink,shortLink,postTime,createTime,updateTime,type,status,star,sort,hot,visits,remark)"
				+ " VALUES(?,?,?,?,?,?,?,?,now(),now(),?,1,0,1,1,0,?) ON DUPLICATE KEY UPDATE updateTime=now(),hot=hot+1";
		List<Object> params = new ArrayList<Object>();
		params.add(vo.getPanShortLink());
		params.add(vo.getPanLink());
		params.add(vo.getReplyName());
		params.add(vo.getReplyLink());
		params.add(vo.getTiebaName());
		params.add(vo.getTiebaLink());
		params.add(vo.getShortLink());
		params.add(vo.getPostTime());
		params.add(vo.getType());
		params.add(vo.getRemark());
		
		try {
			result = DBOperate.update(Constants.ALIAS_MASTER, sql, params.toArray()) > 0;
		} catch (Exception e) {
			result = false;
			logger.error("[贴吧链接]插入数据出错", e);
		}
		return result;
	}
	
	/**
	 * 贴吧信息
	 * @param vo
	 * @return
	 */
	public static boolean saveBaiduTieba(BaiduTiebaVO vo) {
		boolean result = false;
		if (null == vo) {
			return result;
		}
		String sql = "INSERT INTO baidu_tieba(tiebaKw,tiebaName,"
				+ " tiebaLink,shortLink,createTime,updateTime,type,status,star,sort,hot,visits,remark)"
				+ " VALUES(?,?,?,?,now(),now(),?,1,0,1,1,0,?) ON DUPLICATE KEY UPDATE updateTime=now(),hot=hot+1";
		List<Object> params = new ArrayList<Object>();
		params.add(vo.getTiebaKw());
		params.add(vo.getTiebaName());
		params.add(vo.getTiebaLink());
		params.add(vo.getShortLink());
		params.add(vo.getType());
		params.add(vo.getRemark());
		
		try {
			result = DBOperate.update(Constants.ALIAS_MASTER, sql, params.toArray()) > 0;
		} catch (Exception e) {
			result = false;
			logger.error("[贴吧链接]插入数据出错", e);
		}
		return result;
	}
	
	/**
	 * 帖子
	 * @param vo
	 * @return
	 */
	public static boolean saveBaiduTiezi(BaiduTieziVO vo) {
		boolean result = false;
		if (null == vo) {
			return result;
		}
		String sql = "INSERT INTO baidu_tiezi(tieziId,tieziName,tieziLink,tiebaName,"
				+ " tiebaLink,shortLink,postTime,createTime,updateTime,type,status,star,sort,hot,visits,remark)"
				+ " VALUES(?,?,?,?,?,?,?,now(),now(),?,1,0,1,1,0,?) ON DUPLICATE KEY UPDATE updateTime=now(),hot=hot+1";
		List<Object> params = new ArrayList<Object>();
		params.add(vo.getTieziId());
		params.add(vo.getTieziName());
		params.add(vo.getTieziLink());
		params.add(vo.getTiebaName());
		params.add(vo.getTiebaLink());
		params.add(vo.getShortLink());
		params.add(vo.getPostTime());
		params.add(vo.getType());
		params.add(vo.getRemark());
		
		try {
			result = DBOperate.update(Constants.ALIAS_MASTER, sql, params.toArray()) > 0;
		} catch (Exception e) {
			result = false;
			logger.error("[贴吧链接]插入数据出错", e);
		}
		return result;
	}
	
	/**
	 * baiduyun.xyz 过来的数据
	 * @param vo
	 * @return
	 */
	public static boolean saveBaiduYun(BaiduYunVO vo) {
		boolean result = false;
		if (null == vo) {
			return result;
		}
		String sql = "INSERT INTO baidu_yun(panShortLink,panLink,"
				+ " shortLink,createTime,updateTime,type,status,star,sort,hot,visits,remark)"
				+ " VALUES(?,?,?,now(),now(),?,1,0,1,1,0,?) ON DUPLICATE KEY UPDATE updateTime=now(),hot=hot+1";
		List<Object> params = new ArrayList<Object>();
		params.add(vo.getPanShortLink());
		params.add(vo.getPanLink());
		params.add(vo.getShortLink());
		params.add(vo.getType());
		params.add(vo.getRemark());
		
		try {
			result = DBOperate.update(Constants.ALIAS_MASTER, sql, params.toArray()) > 0;
		} catch (Exception e) {
			result = false;
			logger.error("[贴吧链接]插入数据出错", e);
		}
		return result;
	}
	
	/**
	 * 57fx信息
	 * @param vo
	 * @return
	 */
	public static boolean saveFxZiyuan(FxZiyuanVO vo) {
		boolean result = false;
		if (null == vo) {
			return result;
		}
		String sql = "INSERT INTO fx_ziyuan(fxKW,fxName,"
				+ " fxLink,shortLink,postTime,createTime,updateTime,type,status,star,sort,hot,visits,remark)"
				+ " VALUES(?,?,?,?,?,now(),now(),?,1,0,1,1,0,?) ON DUPLICATE KEY UPDATE updateTime=now(),hot=hot+1";
		List<Object> params = new ArrayList<Object>();
		params.add(vo.getFxKW());
		params.add(vo.getFxName());
		params.add(vo.getFxLink());
		params.add(vo.getShortLink());
		params.add(vo.getPostTime());
		params.add(vo.getType());
		params.add(vo.getRemark());
		
		try {
			result = DBOperate.update(Constants.ALIAS_MASTER, sql, params.toArray()) > 0;
		} catch (Exception e) {
			result = false;
			logger.error("[fx资源]插入数据出错", e);
		}
		return result;
	}
	
	/**
	 * friok信息
	 * @param vo
	 * @return
	 */
	public static boolean saveFriok(FxZiyuanVO vo) {
		boolean result = false;
		if (null == vo) {
			return result;
		}
		String sql = "INSERT INTO friok_ziyuan(fxKW,fxName,"
				+ " fxLink,shortLink,postTime,createTime,updateTime,type,status,star,sort,hot,visits,remark)"
				+ " VALUES(?,?,?,?,?,now(),now(),?,1,0,1,1,0,?) ON DUPLICATE KEY UPDATE updateTime=now(),hot=hot+1";
		List<Object> params = new ArrayList<Object>();
		params.add(vo.getFxKW());
		params.add(vo.getFxName());
		params.add(vo.getFxLink());
		params.add(vo.getShortLink());
		params.add(vo.getPostTime());
		params.add(vo.getType());
		params.add(vo.getRemark());
		
		try {
			result = DBOperate.update(Constants.ALIAS_MASTER, sql, params.toArray()) > 0;
		} catch (Exception e) {
			result = false;
			logger.error("[friok资源]插入数据出错", e);
		}
		return result;
	}
	
	/**
	 * 获取最后一条的提交时间
	 * @param args
	 */
	public static Date getFxMaxPostTime(){
		String sql = "SELECT MAX(postTime) FROM fx_ziyuan LIMIT 1;";
		Date postTime = (Date) DBOperate.query4ObjectQuietly(Constants.ALIAS_SLAVE, sql);
		return postTime;
	}
	
	/**
	 * 获取btbtt最大id的那条，目的是为了拿爬取的最大pid
	 * @param args
	 */
	public static String getBtbttMaxPid(){
		String sql = "SELECT remark FROM fx_ziyuan WHERE type = 100401 ORDER BY id desc LIMIT 1;";
		String pid = (String) DBOperate.query4ObjectQuietly(Constants.ALIAS_SLAVE, sql);
		return pid;
	}

	public static void main(String args[]) {
//		BaiduWangpanVO bwvo = new BaiduWangpanVO();
//		bwvo.setPanShortLink("bo2acX5");
//		bwvo.setPanLink("https://pan.baidu.com/mbox/homepage?short=bo2acX5");
//		bwvo.setReplyName("http://pan.baid");
//		bwvo.setReplyLink("http://tieba.baidu.com/p/5269184604?pid=120170990029&cid=0#120170990029");
//		bwvo.setTiebaName("蓝燕");
//		bwvo.setTiebaLink("http://tieba.baidu.com/f?kw=%C0%B6%D1%E0");
//		bwvo.setShortLink("");
//		bwvo.setPostTime(DateUtil.convertStrToDate("2018-06-08 15:10:00:00", DateUtil.DEFAULT_LONG_DATE_FORMAT));
//		bwvo.setType(1);
//		bwvo.setRemark("");
//		System.out.println(saveBaiduWangpan(bwvo));
//		
//		BaiduTiebaVO btvo = new BaiduTiebaVO();
//		btvo.setTiebaKw("%C0%B6%D1%E0");
//		btvo.setTiebaName("蓝燕");
//		btvo.setTiebaLink("http://tieba.baidu.com/f?kw=%C0%B6%D1%E0");
//		btvo.setShortLink("");
//		btvo.setType(1);
//		btvo.setRemark("");
//		System.out.println(saveBaiduTieba(btvo));
//		
//		BaiduTieziVO bzvo = new BaiduTieziVO();
//		bzvo.setTieziId(5487025563L);
//		bzvo.setTieziName("谁有缘之空的百度云，不要压缩包。急求");
//		bzvo.setTieziLink("http://tieba.baidu.com/p/5269184604");
//		bzvo.setTiebaName("浅色不过");
//		bzvo.setTiebaLink("http://tieba.baidu.com/f?kw=%C0%B6%D1%E0");
//		bzvo.setShortLink("");
//		bzvo.setPostTime(DateUtil.convertStrToDate("2018-06-08 15:10:00:00", DateUtil.DEFAULT_LONG_DATE_FORMAT));
//		bzvo.setType(1);
//		bzvo.setRemark("");
//		System.out.println(saveBaiduTiezi(bzvo));
//		
//		BaiduYunVO byvo = new BaiduYunVO();
//		byvo.setPanShortLink("bo2acX5");
//		byvo.setPanLink("https://pan.baidu.com/mbox/homepage?short=bo2acX5");
//		byvo.setShortLink("");
//		byvo.setType(0);
//		byvo.setRemark("");
//		System.out.println(saveBaiduYun(byvo));
	}
}
