package cn.alauwahios.daemon.util;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.Logger;

import cn.alauwahios.daemon.dao.impl.DBOperate;
import cn.alauwahios.daemon.vo.PageInfo;

/**
 * @fileName: DbPageUtil
 */
public class Data2PageUtil {

	public static Logger log = Logger.getLogger(Data2PageUtil.class);

	/**
	 * @Description: TODO (分页数据查询)
	 * @param dbId
	 * @param pageInfo
	 * @param sql
	 * @param rsh
	 * @param params
	 * @return
	 */
	public static <T> T queryQuietly(String dbId, PageInfo pageInfo,
			String sql, ResultSetHandler<T> rsh, Object... params) {

		if (pageInfo != null) {
			if (sql.toLowerCase().indexOf(" limit ") > -1) {
				log.error("[分页查询]含有关键词 limit");
				return null;
			}
			String pageSql = sql;
			int o = sql.toLowerCase().indexOf(" order by ");
			if (o > -1) {
				pageSql = sql.substring(0, o);
			}
			if (pageInfo.getTotalCount() < 0) {
				String sumSql = "SELECT COUNT(*) FROM (" + pageSql + ") T";
				Number totalCounts = (Number) DBOperate.query4ObjectQuietly(
						dbId, sumSql, params);
				if (totalCounts == null) {
					log.error("[分页查询]查询不到记录：sql=" + pageSql + ",parmas="
							+ params);
					return null;
				}
				pageInfo.setTotalCount(totalCounts.longValue());
			}
			long begRowNum = (pageInfo.getPageNum() - 1)
					* pageInfo.getPageSize();
			sql += " LIMIT " + begRowNum + "," + pageInfo.getPageSize();
		}
		return DBOperate.queryQuietly(dbId, sql, rsh, params);
	}

}
