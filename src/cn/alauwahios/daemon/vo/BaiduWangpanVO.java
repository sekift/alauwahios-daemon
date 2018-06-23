package cn.alauwahios.daemon.vo;

import java.util.Date;

/**
 * 贴吧链接表
 * @author Administrator
 *
 */
public class BaiduWangpanVO {
	private int id; // `id`
	private String panShortLink;// `panShortLink` varchar(50) NOT NULL DEFAULT '' COMMENT '网盘短链接',
	private String panLink;// `panLink` varchar(200) NOT NULL DEFAULT '' COMMENT '网盘链接',
	private String replyName;// `replyName` varchar(500) DEFAULT '' COMMENT '回复文字',
	private String replyLink;// `replyLink` varchar(500) DEFAULT '' COMMENT '回复链接',
	private String tiebaName;// `tiebaName` varchar(500) DEFAULT '' COMMENT '贴吧文字',
	private String tiebaLink;// `tiebaLink` varchar(500) DEFAULT '' COMMENT '贴吧链接',
	private String shortLink;// `shortLink` varchar(200) DEFAULT '' COMMENT '短链接',
	private Date postTime;// `postTime` datetime DEFAULT NULL COMMENT '发布时间',
	private Date createTime;// `createTime` datetime NOT NULL COMMENT '抓取时间',
	private Date updateTime;// `updateTime` datetime NOT NULL COMMENT '更新时间',
	private int type;// `type` int(11) NOT NULL DEFAULT '0' COMMENT '类别',
	private int status;// `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态：1 可用 0 不可用。',
	private int star;// `star` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
	private int sort;// `sort` int(11) NOT NULL DEFAULT '0' COMMENT '人工排序，默认为0',
	private int hot;// `hot` int(11) NOT NULL DEFAULT '0' COMMENT '热度',
	private int visits;// `visits` int(11) NOT NULL DEFAULT '0' COMMENT '访问次数',
	private String remark;// `remark` varchar(500) DEFAULT '' COMMENT '备用字段',
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPanShortLink() {
		return panShortLink;
	}
	public void setPanShortLink(String panShortLink) {
		this.panShortLink = panShortLink;
	}
	public String getPanLink() {
		return panLink;
	}
	public void setPanLink(String panLink) {
		this.panLink = panLink;
	}
	public String getReplyName() {
		return replyName;
	}
	public void setReplyName(String replyName) {
		this.replyName = replyName;
	}
	public String getReplyLink() {
		return replyLink;
	}
	public void setReplyLink(String replyLink) {
		this.replyLink = replyLink;
	}
	public String getTiebaName() {
		return tiebaName;
	}
	public void setTiebaName(String tiebaName) {
		this.tiebaName = tiebaName;
	}
	public String getTiebaLink() {
		return tiebaLink;
	}
	public void setTiebaLink(String tiebaLink) {
		this.tiebaLink = tiebaLink;
	}
	public String getShortLink() {
		return shortLink;
	}
	public void setShortLink(String shortLink) {
		this.shortLink = shortLink;
	}
	public Date getPostTime() {
		return postTime;
	}
	public void setPostTime(Date postTime) {
		this.postTime = postTime;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getHot() {
		return hot;
	}
	public void setHot(int hot) {
		this.hot = hot;
	}
	public int getVisits() {
		return visits;
	}
	public void setVisits(int visits) {
		this.visits = visits;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "BaiduWangpanVO [id="+id
				+",panShortLink="+panShortLink
				+",panLink="+panLink
				+",replyName="+replyName
				+",replyLink="+replyLink
				+",tiebaName="+tiebaName
				+",tiebaLink="+tiebaLink
				+",shortLink="+shortLink
				+",postTime="+postTime
				+",createTime="+createTime
				+",updateTime="+updateTime
				+",type="+type
				+",status="+status
				+",star="+star
				+",sort="+sort
				+",hot="+hot
				+",visits="+visits
				+",remark="+remark
				+"]";
	}
}
