package cn.alauwahios.daemon.dao.impl;

import java.net.URL;
import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认连接
 * @author:sekift
 * @time:2016-7-26 下午02:19:29
 * @version:
 */
public class ProxoolConnection {
	private static Logger logger = LoggerFactory.getLogger(ProxoolConnection.class);

	static {
		// 同步所有插件初始化过程(初始化串行化)
		synchronized (logger) {
			logger.debug("初始化全局配置");
			String proxoolConfig = "/config/proxool.xml";
			if (null != proxoolConfig && !"".equals(proxoolConfig)) {
				DbConnectionProvider.registerPoolFromXml(proxoolConfig);
			} else {
				try {
					String pf = "/proxool.xml";
					URL u = ProxoolConnection.class.getResource(pf);
					if (null != u) {
						DbConnectionProvider.registerPoolFromXml(pf);
					}
				} catch (Exception ex) {
					logger.info("使用默认的配置文件[/proxool.xml]配置数据库连接池失败.");
				}
			}
		}
	}

	/*
	 * 根据数据库连接池别名获取数据库连接资料(profile)
	 */
	public static DbConnectProfile getConnectProfile(String alias) {
		// 获取DbConnectProfile
		try {
			long start = System.currentTimeMillis();
			Connection conn = DbConnectionProvider.getConnection(alias);
			long end = System.currentTimeMillis();
			DbConnectProfile profile = new DbConnectProfileImpl(conn, alias, (end - start));
			return profile;
		} catch (Exception e) {
			logger.error("getConnectProfile获取数据库连接池[" + alias + "]的连接异常", e);
		}
		return null;
	}
}
