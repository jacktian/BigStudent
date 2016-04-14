package com.jov.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Environment;

public class StringUtil {
	public static final String IMAGE_PATH = Environment.getExternalStorageDirectory().toString() + "/perTest/images";
	public static boolean isEmpty(String str){
		return str==null||"".equals(str.trim());
	}
	@SuppressLint("SimpleDateFormat")
	public static String dateTimeFormatNow(){
		SimpleDateFormat formatter =  new SimpleDateFormat("yyyyMMddhhmmss");     
		Date date = new Date();
		synchronized(formatter) {
			return formatter.format(date);
		}
		
	}
}
