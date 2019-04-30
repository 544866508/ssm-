package util;


import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeUtil {
	
	
	
	//采用joda-time包
	
	//str->Date
	//Date->str
	
	//采用定义好的STANDARD_FORMAT日期格式
	public static final String STANDARD_FORMAT ="yyyy-MM-dd HH:mm:ss";
	//将前端传入的STANDARD_FORMAT格式的String数据转换成Date类型的数据，然后存入持久化类中
	public static Date strToDate(String dateTimeStr) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
		DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
		return dateTime.toDate();
	}
	//把持久化类中的Date类型的数据转换成STANDARD_FORMAT格式的String数据，然后在传给前端
	public static String DateToStr(Date date) {
		if(date == null){
			return StringUtils.EMPTY;
		}
		DateTime dateTime = new DateTime(date);
		return dateTime.toString(STANDARD_FORMAT);
	}

	//自定义日期格式
	//将前端传入的自定义格式的String数据转换成Date类型的数据，然后存入持久化类中
	public static Date strToDate(String dateTimeStr,String formatStr) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
		DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
		return dateTime.toDate();
	}
	//把持久化类中的Date类型的数据转换成自定义格式的String数据，然后在传给前端
	public static String DateToStr(Date date,String formatStr) {
		if(date == null){
			return StringUtils.EMPTY;
		}
		DateTime dateTime = new DateTime(date);
		return dateTime.toString(formatStr);
	}
	
	




}
