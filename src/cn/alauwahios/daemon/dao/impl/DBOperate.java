package cn.alauwahios.daemon.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DBUtil操作类
 * 
 * @author sekift
 * @teme 2016-7-29 下午02:13:23
 * @memo 使用proxool数据库连接池实现
 */
public class DBOperate {
	private static Logger logger = LoggerFactory.getLogger(DBOperate.class);

	private static QueryRunner queryRunner = new QueryRunner();

	public DBOperate() {
	}

	public static <T> T queryQuietly(String alias, String sql, ResultSetHandler<T> rsh, Object... params) {
		Connection conn = ProxoolConnection.getConnectProfile(alias).getConnection();
		if (conn == null)
			return null;
		try {
			return queryRunner.query(conn, sql, rsh, params);
		} catch (Exception e) {
			logger.error("DB读出错,sql=" + sql + ",params=" + getParamsStr(params), e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return null;
	}

	public static <T> T query(String alias, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
		Connection conn = ProxoolConnection.getConnectProfile(alias).getConnection();
		if (conn == null)
			return null;
		try {
			return queryRunner.query(conn, sql, rsh, params);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object query4ObjectQuietly(String alias, String sql, Object... params) {
		Connection conn = ProxoolConnection.getConnectProfile(alias).getConnection();
		if (conn == null)
			return null;
		try {
			return queryRunner.query(conn, sql, new ScalarHandler(), params);
		} catch (Exception e) {
			logger.error("DB读出错,sql=" + sql + ",params=" + getParamsStr(params), e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object query4Object(String alias, String sql, Object... params) throws SQLException {
		Connection conn = ProxoolConnection.getConnectProfile(alias).getConnection();
		if (conn == null)
			return null;
		try {
			return queryRunner.query(conn, sql, new ScalarHandler(), params);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	public static int updateQuietly(String alias, String sql, Object... params) {
		Connection conn = ProxoolConnection.getConnectProfile(alias).getConnection();
		try {
			return queryRunner.update(conn, sql, params);
		} catch (Exception e) {
			logger.error("DB写出错,sql=" + sql + ",params=" + getParamsStr(params), e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return -1;
	}

	public static int update(String alias, String sql, Object... params) throws SQLException {
		Connection conn = ProxoolConnection.getConnectProfile(alias).getConnection();
		try {
			return queryRunner.update(conn, sql, params);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	private static String getParamsStr(Object... params) {
		if (params == null)
			return "";
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < params.length; i++) {
			if (i > 0)
				buff.append(",");
			buff.append(params[i]);
		}
		return buff.toString();
	}

}
