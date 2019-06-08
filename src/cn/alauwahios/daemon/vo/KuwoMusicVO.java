package cn.alauwahios.daemon.vo;

import java.util.Date;

/**
 * 歌曲详细信息
 * 
 * @author sekift 2019-06-06
 */
public class KuwoMusicVO {

	private int musicId;
	private String musicRid;
	private String musicName;
	private int albumId;
	private String albumName;
	private int artistId;
	private String artistName;
	private int hasMv;
	private int isStar;
	private int isListenFee;
	private int online;
	private int pay;
	private String nationId;
	private int track;
	private String albumPic;
	private String pic;
	private String pic120;
	private int hasLossless;
	private String songTimeMinutes;
	private String releaseDate;
	private int duration;
	private String curUrl;
	private Date createTime;
	private Date updateTime;
	private String remark;

	@Override
	public String toString() {
		return "KuwoMusicVO [musicId="+musicId
				+",musicRid="+musicRid
				+",musicName="+musicName
				+",albumId="+albumId
				+",albumName="+albumName
				+",artistId="+artistId
				+",artistName="+artistName
				+",hasMv="+hasMv
				+",isStar="+isStar
				+",isListenFee="+isListenFee
				+",online="+online
				+",pay="+pay
				+",nationId="+nationId
				+",track="+track
				+",albumPic="+albumPic
				+",pic="+pic
				+",pic120="+pic120
				+",hasLossless="+hasLossless
				+",releaseDate="+releaseDate
				+",songTimeMinutes="+songTimeMinutes
				+",duration="+duration
				+",curUrl="+curUrl
				+",createTime="+createTime
				+",updateTime="+updateTime
				+",remark="+remark
				+"]";
	}
	
	public int getMusicId() {
		return this.musicId;
	};

	public void setMusicId(int musicId) {
		this.musicId = musicId;
	}

	public String getMusicRid() {
		return this.musicRid;
	};

	public void setMusicRid(String musicRid) {
		this.musicRid = musicRid;
	}

	public String getMusicName() {
		return this.musicName;
	};

	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}

	public int getAlbumId() {
		return this.albumId;
	};

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public String getAlbumName() {
		return this.albumName;
	};

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public int getArtistId() {
		return this.artistId;
	};

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public String getArtistName() {
		return this.artistName;
	};

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public int getHasMv() {
		return this.hasMv;
	};

	public void setHasMv(int hasMv) {
		this.hasMv = hasMv;
	}

	public int getIsStar() {
		return this.isStar;
	};

	public void setIsStar(int isStar) {
		this.isStar = isStar;
	}

	public int getIsListenFee() {
		return this.isListenFee;
	};

	public void setIsListenFee(int isListenFee) {
		this.isListenFee = isListenFee;
	}

	public int getOnline() {
		return this.online;
	};

	public void setOnline(int online) {
		this.online = online;
	}

	public int getPay() {
		return this.pay;
	};

	public void setPay(int pay) {
		this.pay = pay;
	}

	public String getNationId() {
		return this.nationId;
	};

	public void setNationId(String nationId) {
		this.nationId = nationId;
	}

	public int getTrack() {
		return this.track;
	};

	public void setTrack(int track) {
		this.track = track;
	}

	public String getAlbumPic() {
		return this.albumPic;
	};

	public void setAlbumPic(String albumPic) {
		this.albumPic = albumPic;
	}

	public String getPic() {
		return this.pic;
	};

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPic120() {
		return this.pic120;
	};

	public void setPic120(String pic120) {
		this.pic120 = pic120;
	}

	public int getHasLossless() {
		return this.hasLossless;
	};

	public void setHasLossless(int hasLossless) {
		this.hasLossless = hasLossless;
	}

	public String getSongTimeMinutes() {
		return this.songTimeMinutes;
	};

	public void setSongTimeMinutes(String songTimeMinutes) {
		this.songTimeMinutes = songTimeMinutes;
	}

	public String getReleaseDate() {
		return this.releaseDate;
	};

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getDuration() {
		return this.duration;
	};

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getCurUrl() {
		return this.curUrl;
	};

	public void setCurUrl(String curUrl) {
		this.curUrl = curUrl;
	}

	public Date getCreateTime() {
		return this.createTime;
	};

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	};

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return this.remark;
	};

	public void setRemark(String remark) {
		this.remark = remark;
	}

}