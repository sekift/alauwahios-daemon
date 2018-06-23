package cn.alauwahios.daemon.dao.impl;

import java.sql.Connection;

/**
 * 数据库连接资料类
 */
public interface DbConnectProfile {

	/**
	 * 获取数据库连接池别名
	 * @return -- 连接池别名
	 */
	String getDbPoolAlias();
	
	/**
	 * 获取数据库连接
	 * @return -- 数据库连接
	 */
	Connection getConnection();
	
	/**
	 * 初始化数据库连接上下文时间(主要是申请数据库连接的时间),单位: 毫秒
	 * @return -- 花费时间
	 */
	long initializedContextTime();
}
