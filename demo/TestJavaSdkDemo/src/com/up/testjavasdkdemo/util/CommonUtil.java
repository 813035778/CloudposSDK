package com.up.testjavasdkdemo.util;

import java.util.Locale;

import android.text.format.Time;

/**
 * ***********************************
 * 类描述： 工具类
 * 创建者： 瞿峰
 * 创建时间： 2016-8-29 下午4:40:30
 *************************************
 */
public class CommonUtil {
	/**
	* 将byte数组转化为Hex字符
	* 
	* @param byteArr
	*            待转换的数组
	* @return Hex字符
	*/
	public static String bytes2HexStr(byte[] byteArr) {
		if (byteArr == null || byteArr.length == 0)
			return "";
		StringBuffer strBufTemp = new StringBuffer("");
		for (int i = 0; i < byteArr.length; i++) {
			String stmp = Integer.toHexString(byteArr[i] & 0XFF);
			if (stmp.length() == 1)
				strBufTemp.append("0" + stmp);
			else
				strBufTemp.append(stmp);
		}
		return strBufTemp.toString().toUpperCase(Locale.getDefault());
	}

	/**
	* 获取系统时间，6位，例如：2012-11-1 6:11:21，返回的就是20130717144032
	* 
	* @return
	*/
	public static String getSysDate() {

		Time t = new Time();
		t.setToNow();
		int year = t.year;
		int month = t.month + 1;
		int day = t.monthDay;

		int hour = t.hour; // 0-23
		int minute = t.minute;
		int second = t.second;

		return String.format("%04d%02d%02d%02d%02d%02d", year, month, day, hour, minute, second, Locale.getDefault());
	}
}
