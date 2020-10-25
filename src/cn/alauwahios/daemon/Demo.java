package cn.alauwahios.daemon;

import java.util.Date;

public class Demo {

	public static void main(String[] args) {
		//bbs_sid=5f0818974500e70a; bbs_page=1; cck_lasttime=1596263445117; cck_count=1; bbs_lastonlineupdate=1596286362; bbs_lastday=1596286362
//		Cookie: bbs_sid=5f0818974500e70a; bbs_page=1; timeoffset=%2B08; cck_lasttime=1597214675540; cck_count=0
		System.out.println(System.currentTimeMillis());
		Long timestamp1 = 1597303445081L;
		Long timestamp2 = 1597304684*1000L;//1596806269883 //1596811985
		try{
			Date date = new Date(timestamp1);
			System.out.println(date);
			date = new Date(timestamp2);
			System.out.println(date);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
