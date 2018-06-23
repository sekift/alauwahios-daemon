package cn.alauwahios.daemon.vo;

/**
 * 分页
 */
public class PageInfo {
	private long pageSize = 50; // 分页数
	private long totalCount = -1; // 总记录数
	private long pageNum = 1; // 当前页

	@Override
	public String toString() {
		return "PageInfo [pageNum=" + pageNum + ", pageSize=" + pageSize
				+ ", totalCount=" + totalCount + "]";
	}

	public long getPageCount() {
		if (totalCount < 1)
			return 0;
		return (totalCount + pageSize - 1) / pageSize;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getPageNum() {
		return pageNum;
	}

	public void setPageNum(long pageNum) {
		this.pageNum = pageNum;
	}
}
