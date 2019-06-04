package cn.alauwahios.daemon.vo;

import java.util.Date;

public class KuwoSingerNameVO {

	private int singerId;// `singerId` int(11) NOT NULL COMMENT 'kuwo歌手id',
	private String singerName;// `singerName` varchar(50) NOT NULL COMMENT 'kuwo歌手名字',
	private String prefix;// `prefix` varchar(2) DEFAULT NULL COMMENT '歌手前缀',
	private String curUrl;// `curUrl` varchar(255) DEFAULT NULL COMMENT '当前的URL',
	private String preUrl;// `preUrl` varchar(255) DEFAULT NULL COMMENT '前一个URL',
	private Date createTime;// `createTime` datetime DEFAULT NULL COMMENT '创建时间',
	private Date updateTime;// `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
	private String remark;// `remark` varchar(255) DEFAULT NULL COMMENT '备用',

	public int getSingerId() {
		return singerId;
	}

	public void setSingerId(int singerId) {
		this.singerId = singerId;
	}

	public String getSingerName() {
		return singerName;
	}

	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getCurUrl() {
		return curUrl;
	}

	public void setCurUrl(String curUrl) {
		this.curUrl = curUrl;
	}

	public String getPreUrl() {
		return preUrl;
	}

	public void setPreUrl(String preUrl) {
		this.preUrl = preUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Override
	public String toString() {
		return "KuwoSingerNameVO [singerId="+singerId
				+",singerName="+singerName
				+",prefix="+prefix
				+",curUrl="+curUrl
				+",preUrl="+preUrl
				+",createTime="+createTime
				+",updateTime="+updateTime
				+",remark="+remark
				+"]";
	}

}
