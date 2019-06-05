package cn.alauwahios.daemon.vo;

import java.util.Date;

public class KuwoSingerBaseVO {

	private int artistId;// `id` int(11) NOT NULL,
	private String artistName;// `name` varchar(100) DEFAULT NULL,
	private String aartist;// `aartist` varchar(100) DEFAULT NULL,
	private String prefix;// `prefix` varchar(2) DEFAULT NULL COMMENT '歌手前缀',
	private int isStar;// `isStar` tinyint(2) DEFAULT NULL,
	private int albumNum;// `albumNum` int(11) DEFAULT NULL,
	private int mvNum;// `mvNum` int(11) DEFAULT NULL,
	private int musicNum;// `musicNum` int(11) DEFAULT NULL,
	private int artistFans;// `artistFans` int(11) DEFAULT NULL,
	private String pic;// `pic` varchar(64) DEFAULT NULL,
	private String pic70;// `pic70` varchar(64) DEFAULT NULL,
	private String pic120;// `pic120` varchar(64) DEFAULT NULL,
	private String pic300;// `pic300` varchar(64) DEFAULT NULL,
	private String curUrl;// `curUrl` varchar(128) DEFAULT NULL,
	private Date createTime;// `createTime` datetime DEFAULT NULL,
	private Date updateTime;// `updateTime` datetime DEFAULT NULL,
	private String remark;// `remark` varchar(128) DEFAULT NULL
	
	
	@Override
	public String toString() {
		return "KuwoSingerBaseVO [artistId="+artistId
				+",artistName="+artistName
				+",aartist="+aartist
				+",prefix="+prefix
				+",isStar="+isStar
				+",albumNum="+albumNum
				+",mvNum="+mvNum
				+",musicNum="+musicNum
				+",artistFans="+artistFans
				+",pic="+pic
				+",pic70="+pic70
				+",pic120="+pic120
				+",pic300="+pic300
				+",curUrl="+curUrl
				+",createTime="+createTime
				+",updateTime="+updateTime
				+",remark="+remark
				+"]";
	}
	
	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getAartist() {
		return aartist;
	}

	public void setAartist(String aartist) {
		this.aartist = aartist;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public int getIsStar() {
		return isStar;
	}

	public void setIsStar(int isStar) {
		this.isStar = isStar;
	}

	public int getAlbumNum() {
		return albumNum;
	}

	public void setAlbumNum(int albumNum) {
		this.albumNum = albumNum;
	}

	public int getMvNum() {
		return mvNum;
	}

	public void setMvNum(int mvNum) {
		this.mvNum = mvNum;
	}

	public int getMusicNum() {
		return musicNum;
	}

	public void setMusicNum(int musicNum) {
		this.musicNum = musicNum;
	}

	public int getArtistFans() {
		return artistFans;
	}

	public void setArtistFans(int artistFans) {
		this.artistFans = artistFans;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPic70() {
		return pic70;
	}

	public void setPic70(String pic70) {
		this.pic70 = pic70;
	}

	public String getPic120() {
		return pic120;
	}

	public void setPic120(String pic120) {
		this.pic120 = pic120;
	}

	public String getPic300() {
		return pic300;
	}

	public void setPic300(String pic300) {
		this.pic300 = pic300;
	}

	public String getCurUrl() {
		return curUrl;
	}

	public void setCurUrl(String curUrl) {
		this.curUrl = curUrl;
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

}
