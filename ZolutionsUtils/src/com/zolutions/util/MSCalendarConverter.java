package com.zolutions.util;

/**
 * @author S J RILEY - Zolutions Ltd.
 * @version 1.0
 */
import java.util.*;
import java.text.*;

public final class MSCalendarConverter {
  private static Date d1900 = null; // the date 1/1/1900 00:00:00:00
  private static long milliSecInDay = 1000 * 24 * 60 * 60;

  /**
   * Needs to be called once to setup the 1900 00:00:00:00 date.
   */
  private static void init() {
    Calendar c1900 = Calendar.getInstance();
    c1900.set(1900,0,0);
    c1900.set(Calendar.HOUR_OF_DAY,12);
    c1900.set(Calendar.MINUTE,0);
    c1900.set(Calendar.SECOND,0);
    c1900.set(Calendar.MILLISECOND,0);
    d1900 = c1900.getTime();
  }

  /**
   * Given the number of days since the start of 1900, this method returns
   * the equivalent Calenday object.
   */
  public static Date getDateFromMS(int numDaysSince1900) {
    if(d1900 == null) init();
    Date d = new Date(d1900.getTime() + (numDaysSince1900-1)*milliSecInDay);
    return d;
  }

  /**
   * Given a Date object, this method returns the number of <B>whole</B> days passed
   * since the start of 1900.
   */
  public static int getDaysSince1900(Date d) {
    if(d1900 == null) init();
    // get time difference (in millisecs.)
    long delta = d.getTime() - d1900.getTime(); // millisec
    long days = delta/milliSecInDay;
    return (int)days+2; // + one as MS day 1 is 1/1/1900.
  }

  public static void main(String[] args) {
   // int nDays = 37698; // todays (18th March 2003) date number, from Excel
    int nDays = 1; // todays (18th March 2003) date number, from Excel

    Date d = getDateFromMS(nDays);
    System.out.println("1900 + "+nDays+" day(s) = "+d); // 1 day after 1/1/1900 is the 2/1/1900

    // create a Date formatter...
    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
    // apply Date formatter...
    String dateStr = df.format(d);
    // print result....
    System.out.println("1900 + "+nDays+" day(s) = "+dateStr);

    try {
      Date d3 = df.parse(dateStr);
      System.out.println("parsed version = " + d3 + " or in 'yyyy/mm/dd' format = "+df.format(d3));
      System.out.println("days since 1900 = "+getDaysSince1900(d3));
    }
    catch (java.text.ParseException pe) {
      System.out.println(pe.getMessage());
    }

  }

}

