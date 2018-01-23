package com.thirdparty.alioss.util;

import org.springframework.format.datetime.DateFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/*
 * 日期工具类
 * 
 */
public class DateUtils extends DateFormatter {

	private static String getFormatStr(int formatType) {
		String formatStr = null;
		switch (formatType) {
		case 1:
			formatStr = "yyyy-MM-dd";
			break;
		case 2:
			formatStr = "yyyy-MM-dd HH:mm:ss";
			break;
		case 3:
			formatStr = "MM-dd";
			break;
		case 4:
			formatStr = "yyyyMMdd";
			break;
		case 5:
			formatStr = "yyyyMMddHHmmss";
			break;
		case 6:
			formatStr = "HH:mm";
			break;
		case 7:
			formatStr = "yyyyMM";
			break;
		case 8:
			formatStr = "HH:mm:ss";
			break;
		case 9:
			formatStr = "HHmmss";
			break;
		case 10:
			formatStr = "yyyyMMdd/HHmmss";
			break;
		case 11:
			formatStr = "yy-MM-dd-HH-mm-ss";
			break;
		case 12:
			formatStr = "yyMMddHHmmss";
			break;
		case 13:
			formatStr = "yyyy年MM月dd日";
			break;
		case 23:
			formatStr = "yy-MM";
			break;
		default:
			formatStr = "yyyy-MM-dd HH:mm:ss";
		}
		return formatStr;
	}
	public static String getStringTime(long timeLong, int formatType) {
		String time = "";
		if (timeLong > 0) {
			String formatStr = getFormatStr(formatType);
			DateFormat df = new SimpleDateFormat(formatStr);
			time = df.format(timeLong);
		}
		return time;
	}
	public static String getStrDate(Date date, int formatType) {
		String time = "";
		if (date != null) {
			String formatStr = getFormatStr(formatType);
			DateFormat df = new SimpleDateFormat(formatStr);
			time = df.format(date);
		}
		return time;
	}
	public static Date formatStringTime(String stime, int formatType) throws ParseException{
		Date date = null;
		DateFormat df = new SimpleDateFormat(getFormatStr(formatType));
		try {
			date = df.parse(stime);
		} catch (ParseException e) {
			throw new ParseException(stime, formatType);
		}
		return date;
	}
	/**
	 * 当天时间
	 * @return
	 * @author Louis
	 * @throws ParseException 
	 */
	public static Date getCurrentTime(int formatType) throws ParseException {
		String today = getStringTime(new Date().getTime(),formatType);
		return formatStringTime(today,formatType);
	}
	
	/**
	 * 时间处理格式
	 * @return
	 * @author Louis
	 * @throws ParseException 
	 */
	public static Date dateChangeType(Date date, int formatType) throws ParseException {
		String dateStr = getStringTime(date.getTime(),formatType);
		return formatStringTime(dateStr,formatType);
	}
	
	/**
	 * 模板时间
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 * @author Louis
	 */
	public static Date getTemplateTime(int hour, int minute, int second, int millisecond) {
		Calendar currentDate = new GregorianCalendar();   
		currentDate.set(Calendar.HOUR_OF_DAY, hour);  
		currentDate.set(Calendar.MINUTE, minute);  
		currentDate.set(Calendar.SECOND, second);  
		currentDate.set(Calendar.MILLISECOND, millisecond);
		return currentDate.getTime();
	}
	
	/**
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param second
	 * @param millisecond
	 * @return
	 * @author Louis
	 */
	public static Date getCustomTime(int year, int month, int day, int hour, int minute, int second, int millisecond) {
		Calendar currentDate = new GregorianCalendar();   
		currentDate.set(Calendar.YEAR, year);
		currentDate.set(Calendar.MONTH, month);
		currentDate.set(Calendar.DATE, day);
		currentDate.set(Calendar.HOUR_OF_DAY, hour);  
		currentDate.set(Calendar.MINUTE, minute);  
		currentDate.set(Calendar.SECOND, second);  
		currentDate.set(Calendar.MILLISECOND, millisecond);
		return currentDate.getTime();
	}
	
	/**
	 * 开始时间 
	 * @param date yyyy-mm-dd
	 * @return
	 */
	public static Date getBeginTime(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 0);  
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 结束时间
	 * @param date yyyy-mm-dd
	 * @return
	 */
	public static Date getEndTime(Date date) {
		Calendar calendar = new GregorianCalendar();  
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);  
		calendar.set(Calendar.MINUTE, 59);  
		calendar.set(Calendar.SECOND, 59);  
		return calendar.getTime();
	}
	
	public static void main(String[] args) {
		//System.out.println(getBeginTime(new Date()));
		//System.out.println(getEndTime(new Date()));
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(new Date());
//		System.out.println(calendar.get(Calendar.YEAR));
//		System.out.println(calendar.get(Calendar.MONTH)+1);
//		System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
//		System.out.println(calendar.get(Calendar.HOUR));
//		System.out.println(calendar.get(Calendar.MINUTE));
//		System.out.println(calendar.get(Calendar.SECOND));
//		System.out.println(getStringTime(calendar.getTime().getTime(),1));
		System.out.println(getStrDate(new Date(),0));
	}
	
	public static String date2Str() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date now = new Date();
		return sdf.format(now);
	}
	
	public static String date2Str(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date now = new Date();
		return sdf.format(now);
	}
	
	/**  
     * 计算两个日期之间相差的小时数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差小时数 
     * @throws ParseException  
     */    
    public static int daysBetween_hms(Date smdate,Date bdate) throws ParseException    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    } 
    
    
	
	
	public static Date dateAdd(Date date, Integer months){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date1 = null;
		try {
			date1 = sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calender = Calendar.getInstance();
        calender.setTime(date1);
        calender.add(Calendar.MONTH, months);
        return calender.getTime();
	}
	
	public static Integer getTotalMonth(Integer type, Integer num){
		int totalMonth = 0;
		if(type == 801){
			totalMonth = 1*num;
		}else if(type == 802){
			totalMonth = 3*num;
		}else if(type == 803){
			totalMonth = 12*num;
		}else{
			totalMonth = 0;
		}
		
		return totalMonth;
	}
	
	/**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public static int daysBetween(Date smdate,Date bdate) throws ParseException    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    } 
	
    /*
     * 根据小时数计算预约数量
     * 0-10天  563+hour*9+lasts
     * 11-20天 563+240*9+(hour-240)*17+lasts
     * 21-40天 563+240*9+240*17+(hour-480)*23+lasts
     * 40天后 563+240*9+240*17+480*23+(hour-960)*1+lasts
     */
    public static int computeDays(Date start, Date end) throws ParseException{
    	int lasts = Integer.parseInt(getDated());
    	
    	int num = 0;
    	int hour = daysBetween_hms(start, end);
    	int basic_num = 563;
    	int num10 = 9;
    	int num20 = 17;
    	int num40 = 23;
    	int other_num = 1;
    	
    	if(hour >= 0 && hour < 240){
    		num = basic_num + hour * num10 + lasts + getRandom();
    	}else if(hour >= 240 && hour < 480){
    		num = basic_num + 240*9 + (hour-240)*17 + lasts + getRandom();
    	}else if(hour >= 480 && hour < 960){
    		num = basic_num + 240*9 + 240*17 + (hour-480)*23 + lasts + getRandom();
    	}else if(hour >= 960){
    		num = basic_num + 240*9 + 240*17 + 480*23 + (hour-960)*1 + lasts + getRandom();
    	}
    	
    	return num;
    }
    public static String getDated(){
    	return date2Str(new Date(),"HH");
    }
    
    public static int getRandom(){
   	 int max=133;
        int min=13;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        //System.out.println(s);
        return s;
   }
    
    public static Date str2Date(String strDate, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("str2Date(String, String)");
		}
		return null;
	}
	
    /**
     * 计算两个日期之间相差的月份
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int getMonthBetweenDate(Date smdate, Date bdate) throws ParseException {
		int months = 0;// 相差月份
		int daysBetween = daysBetween(smdate,bdate);
		if(daysBetween <= 0){
			months = 0;
		}else{
//			//大于等于25天前进一位
			if(daysBetween%30 >= 15){
				months = daysBetween/30 + 1;
			}else{
				months = daysBetween/30;
			}
		}
		
		return months;
	}
    
    public static int getEvalMonth(Integer month) throws ParseException {
		int months = 0;
		if(month <= 0){
			months = 0;
		}else{
//			//大于等于3个月前进一位
			if(month%6 >= 3){
				months = month/6 + 1;
			}else{
				months = month/6;
			}
		}
		
		return months * 6;
	}
}
