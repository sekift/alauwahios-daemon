package cn.alauwahios.daemon.dao.impl;

import java.sql.Connection;

/**
 * 数据库连接资料(profile)
 */
public final class DbConnectProfileImpl implements DbConnectProfile {
	
	private Connection conn = null;
	
	private String poolAlias = null;
	
	private long contextTime = -1;
	
	public DbConnectProfileImpl(Connection conn, String alias, long contextTime) {
		this.conn = conn;
		this.poolAlias = alias;
		this.contextTime = contextTime;
	}

	public Connection getConnection() { 
		return conn;
	}

	public String getDbPoolAlias() { 
		return poolAlias;
	}

	public long initializedContextTime() { 
		return contextTime;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(128);
		sb.append("DbConnectProfileImpl{");
		sb.append(" conn:").append(conn).append(",\n");
		sb.append(" poolAlias:").append(poolAlias).append(",\n");
		sb.append(" contextTime:").append(contextTime);
		sb.append("}");
		return sb.toString();
	}
}
