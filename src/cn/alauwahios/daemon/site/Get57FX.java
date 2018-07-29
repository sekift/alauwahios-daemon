package cn.alauwahios.daemon.site;

import org.jsoup.nodes.Document;

import cn.alauwahios.daemon.util.JsoupUtil;

public class Get57FX {

	public static void main(String[] args) {
		Document doc = JsoupUtil.getDocByConnect("https://www.57fx.com/file-new/");
		System.out.println(doc);

	}

}
