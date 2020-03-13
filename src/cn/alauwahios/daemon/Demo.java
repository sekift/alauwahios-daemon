package cn.alauwahios.daemon;

import java.io.File;

public class Demo {

	public static void main(String[] args) {
		String path = "K:\\微博福利姬";
		File file1 = new File(path);
		
		if(file1.isDirectory()){
			File[] files = file1.listFiles();
			if (files != null && files.length != 0) {
	            for (File file : files) {
	            	System.out.println(file+" "+file.length());
	            	if(file.length()<1){
	            	    //file.deleteOnExit();
	            	}
	            }
	        }
		}

	}

}
