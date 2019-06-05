package cn.alauwahios.daemon.vo;

import java.util.Date;

public class KuwoAlbumVO {
	
	private int albumId;// `albumId` int(11) NOT NULL,
	private String albumName;// `albumName` varchar(100) DEFAULT '',
	private int artistId;// `artistId` int(11) DEFAULT '0',
	private String artistName;// `artistName` varchar(100) DEFAULT '',
	private int isStar;// `isStar` tinyint(2) DEFAULT NULL,
	private int pay;// `pay` int(11) DEFAULT NULL,
	private String pic;// `pic` varchar(128) DEFAULT NULL,
	private String releaseDate;// `releaseDate` varchar(64) DEFAULT NULL,
	private String language;// `language` varchar(64) DEFAULT '',
	private String albumInfo;// `albumInfo` varchar(15120) DEFAULT '',
	private String curUrl;// `curUrl` varchar(128) DEFAULT NULL,
	private Date createTime;// `createTime` datetime DEFAULT NULL,
	private Date updateTime;// `updateTime` datetime DEFAULT NULL,
	private String remark;// `remark` varchar(128) DEFAULT NULL
	
	@Override
	public String toString() {
		return "KuwoAlbumVO [albumId="+albumId
				+",albumName="+albumName
				+",artistId="+artistId
				+",artistName="+artistName
				+",isStar="+isStar
				+",pay="+pay
				+",pic="+pic
				+",releaseDate="+releaseDate
				+",language="+language
				+",albumInfo="+albumInfo
				+",curUrl="+curUrl
				+",createTime="+createTime
				+",updateTime="+updateTime
				+",remark="+remark
				+"]";
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
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

	public int getPay() {
		return pay;
	}

	public void setPay(int pay) {
		this.pay = pay;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getIsStar() {
		return isStar;
	}

	public void setIsStar(int isStar) {
		this.isStar = isStar;
	}

	public String getAlbumInfo() {
		return albumInfo;
	}

	public void setAlbumInfo(String albumInfo) {
		this.albumInfo = albumInfo;
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
