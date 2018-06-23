package cn.alauwahios.daemon.tieba;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.alauwahios.daemon.util.JsoupUtil;

public class TestTiezi {
	
	public static void main(String args[]) {
		/*
		 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `tieziId` bigint(22) NOT NULL DEFAULT '0' COMMENT '帖子ID',
  `tieziName` varchar(500) DEFAULT '' COMMENT '帖子文字',
  `tieziLink` varchar(500) DEFAULT '' COMMENT '帖子链接',
  `tiebaName` varchar(500) DEFAULT '' COMMENT '贴吧文字',
  `tiebaLink` varchar(500) DEFAULT '' COMMENT '贴吧链接',
  `shortLink` varchar(200) DEFAULT '' COMMENT '短链接',
  `postTime` datetime DEFAULT NULL COMMENT '发布时间',
  `createTime` datetime NOT NULL COMMENT '抓取时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '类别',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态：1 可用 0 不可用。',
  `star` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '人工排序，默认为0',
  `hot` int(11) NOT NULL DEFAULT '0' COMMENT '热度',
  `visits` int(11) NOT NULL DEFAULT '0' COMMENT '访问次数',
  `remark` varchar(500) DEFAULT '' COMMENT '备用字段',
		 */
		String tieziUrl = "https://tieba.baidu.com/p/5761813673?abvef=fewfwe";
		Document result = JsoupUtil.getDocByConnect(tieziUrl);
		Element ele = result.head();
		String title = ele.getElementsByTag("title").html();
		Elements meta = ele.getElementsByTag("meta");
		//帖子ID
		String tieziLink = tieziUrl.split("\\?")[0];
		String[] tieziStr = tieziLink.split("\\/");
		String tieziId = tieziStr[tieziStr.length - 1];
		System.out.println(tieziId);
		
		//帖子文字
		String tieziName = title.split("【")[0];
		System.out.println(tieziName);
		//帖子链接
		System.out.println(tieziLink);
		
		String tiebaName = meta.attr("fname");
		String tiebaLink = "https://"+meta.attr("furl").replace("&ie=utf-8", "");
		
		System.out.println(tiebaName);
		System.out.println(tiebaLink);
		
		Element body = result.body();
		Elements scriptEle = body.getElementsByClass("d_name");//.first().getElementsByTag("script").first()
		System.out.println(scriptEle);
	}

}
