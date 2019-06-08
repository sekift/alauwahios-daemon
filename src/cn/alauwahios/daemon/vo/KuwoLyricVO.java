package cn.alauwahios.daemon.vo;

import java.util.Date;

/**
 * 歌词详细信息
 * 
 * @author sekift 2019-06-06
 */
public class KuwoLyricVO {
	private int musicId;
	private String musicName;
	private int albumId;
	private String albumName;
	private int artistId;
	private String artistName;
	private String lrcList;
	private String nsig1;
	private String nsig2;
	private int score100;
	private int playCnt;
	private String curUrl;
	private Date createTime;
	private Date updateTime;
	private String remark;
	
	@Override
	public String toString() {
		return "KuwoMusicVO [musicId="+musicId
				+",musicName="+musicName
				+",albumId="+albumId
				+",albumName="+albumName
				+",artistId="+artistId
				+",artistName="+artistName
				+",lrcList="+lrcList
				+",nsig1="+nsig1
				+",nsig2="+nsig2
				+",score100="+score100
				+",playCnt="+playCnt
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

	public String getLrcList() {
		return this.lrcList;
	};

	public void setLrcList(String lrcList) {
		this.lrcList = lrcList;
	}

	public String getNsig1() {
		return this.nsig1;
	};

	public void setNsig1(String nsig1) {
		this.nsig1 = nsig1;
	}

	public String getNsig2() {
		return this.nsig2;
	};

	public void setNsig2(String nsig2) {
		this.nsig2 = nsig2;
	}

	public int getScore100() {
		return this.score100;
	};

	public void setScore100(int score100) {
		this.score100 = score100;
	}

	public int getPlayCnt() {
		return this.playCnt;
	};

	public void setPlayCnt(int playCnt) {
		this.playCnt = playCnt;
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