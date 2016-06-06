package com.mob.studio.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DateUtil  {
	
	public static Date getDateWithHourAndMinute(Date date, Integer hour, Integer minute) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static Date add(Date date, int i) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, i);
		return c.getTime();
	}
	
	public static Long add(Long date, int i) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			Date d = sdf.parse(date.toString());
			Date r = add(d, i);
			return new Long(sdf.format(r));
		} catch (Exception e) {
			return null;
		}
		
	}
	
	
	public static Date addMonth(Date date, int i) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, i);
		return c.getTime();
	}
	
	public static Long addMonth(Long date, int i) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		try {
			Date d = sdf.parse(date.toString());
			Date r = add(d, i);
			return new Long(sdf.format(r));
		} catch (Exception e) {
			return null;
		}
		
	}
	
	public static List<Long> generateDateList(String begin, String end, String pattern) {
		List<Long> r = new ArrayList<Long>();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date b = null;
		Date e = null;
		try {
			b = sdf.parse(begin);
			e = sdf.parse(end);
		} catch (Exception ex) {
			return r;
		}
		if (b.after(e)) {
			return r;
		}
		int count = 0;
		int max = 500;
		
		SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
		while (!b.after(e)) {
			Long date = new Long(s.format(b));
			r.add(date);
			b = add(b, 1);
			count = count + 1;
			if (count > max) {
				return r;
			}
		}
		return r;
	}
	
	
	public static List<Long> generateMonthList(String begin, String end, String pattern) {
		List<Long> r = new ArrayList<Long>();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date b = null;
		Date e = null;
		try {
			b = sdf.parse(begin);
			e = sdf.parse(end);
		} catch (Exception ex) {
			return r;
		}
		if (b.after(e)) {
			return r;
		}
		int count = 0;
		int max = 500;
		
		SimpleDateFormat s = new SimpleDateFormat("yyyyMM");
		while (!b.after(e)) {
			Long date = new Long(s.format(b));
			r.add(date);
			b = addMonth(b, 1);
			count = count + 1;
			if (count > max) {
				return r;
			}
		}
		return r;
	}
	
	
	
	public static Date StringToDate(String dateStr,String formatStr){
		DateFormat sdf=new SimpleDateFormat(formatStr);
		Date date=null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	
	public static String DateToString(Date date,String formatStr){
		DateFormat sdf=new SimpleDateFormat(formatStr);
		String dateStr=null;
		
		dateStr = sdf.format(date);
		
		return dateStr;
	}
	
	
	public static Long DateToLong(Date date,String formatStr){
		String str = DateToString(date, formatStr);
		
		return Long.parseLong(str);
	}



	/**
	 * get current time as pattern 24h(yyyyMMddHHmmss)
	 * */
	public static String returnCurrentDateMode24() {
		try {
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			return sdf.format(now);
		} catch (Exception ex) {
			return null;
		}
	}

	public static Long returnTimeStamp(){
		return (System.currentTimeMillis());
	}

	public static void main(String[] args) {
//		String d = returnCurrentDateMode24();
//		System.out.println(d);
//		String y3 = String.valueOf(d.charAt(2));
//		System.out.println(y3);
//		String y1 = String.valueOf(d.charAt(0));
//		String business = String.format("%01d",0);
//		String date_other = d.substring(3,12);
//		System.out.println(y3+y1+business+date_other);

		System.out.println();
		String date = DateUtil.returnTimeStamp().toString();
		System.out.println("date:\t" + date);
		String dateLast3 = date.substring(10, 13);
		System.out.println("dateLast3:\t" + dateLast3);
		String dateFirst = String.valueOf(date.charAt(0));
		System.out.println("dateFirst:\t" + dateFirst);
		String dateOther = date.substring(1,10);
		System.out.println("dateOther:\t" + dateOther);
		long playerId = 1255789;
		String business = "0";
		String uidStr = String.valueOf(playerId);
		uidStr = (uidStr.length() < 5)? String.format("%05d",playerId): uidStr.substring(uidStr.length() - 5, uidStr.length());
		System.out.println("uidStr:\t" + uidStr);
		StringBuilder sb = new StringBuilder();
		sb.append(dateFirst).append(dateLast3).append(business).append(uidStr).append(dateOther);
		System.out.println(sb.toString());

	}
}