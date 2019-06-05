package cn.alauwahios.daemon.vo;

import java.util.Date;

public class KuwoSingerInfoVO {

	private int artistId;// `id` int(11) NOT NULL,
	private String aartist;// `aartist` varchar(100) DEFAULT NULL,
	private String artistName;// `name` varchar(100) DEFAULT NULL,
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
	
	private String birthday;//	  `birthday` varchar(16) DEFAULT NULL,
	private String country;//	  `country` varchar(16) DEFAULT NULL,
	private String gener;//	  `gener` varchar(4) DEFAULT NULL,
	private String weight;//	  `weight` varchar(4) DEFAULT NULL,
	private String language;//	  `language` varchar(10) DEFAULT NULL,
	private String upPcUrl;//	  `upPcUrl` varchar(256) DEFAULT NULL,
	private String birthplace;//	  `birthplace` varchar(256) DEFAULT NULL,
	private String constellation;//	  `constellation` varchar(10) DEFAULT NULL,
	private String tall;//	  `tall` varchar(128) DEFAULT NULL,
	private String info;//	  `info` varchar(5120) DEFAULT NULL,
	
	
	@Override
	public String toString() {
		return "KuwoSingerInfoVO [artistId="+artistId
				+",aartist="+aartist
				+",artistName="+artistName
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
				+",birthday="+birthday
				+",country="+country
				+",gener="+gener
				+",weight="+weight
				+",language="+language
				+",upPcUrl="+upPcUrl
				+",birthplace="+birthplace
				+",constellation="+constellation
				+",tall="+tall
				+",info="+info
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

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getGener() {
		return gener;
	}

	public void setGener(String gener) {
		this.gener = gener;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getUpPcUrl() {
		return upPcUrl;
	}

	public void setUpPcUrl(String upPcUrl) {
		this.upPcUrl = upPcUrl;
	}

	public String getBirthplace() {
		return birthplace;
	}

	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getTall() {
		return tall;
	}

	public void setTall(String tall) {
		this.tall = tall;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
