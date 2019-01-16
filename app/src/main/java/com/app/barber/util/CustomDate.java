package com.app.barber.util;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.app.barber.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by harish on 20/12/18.
 */

public class CustomDate {
    private static final String APPOINTMENT_FORMAT = "EEEE,MMMM YY";
    static String DEFAULT_FORMAT_DATE = "MM/dd/yyyy";
    private static String TAG = CustomDate.class.getSimpleName();

    /**
     * Format date as required
     */
    public static String formatThis(String format, String dateStr) {
        String inputPattern = format;
        String outputPattern = APPOINTMENT_FORMAT;
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateStr);
            String updaetedFormat = outputFormat.format(date);
            Log.d(" formatThis :::::", updaetedFormat);
            return updaetedFormat;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * Format time as required
     */
    public static String formatTimeRemainig(Activity serviceListActivity, String timeRemaining) {

        try {
            String[] timeR = timeRemaining.split(":");
            Log.d(" formatTimeRemainig ", timeR[0] + ":" + timeR[1]);
            timeRemaining = timeR[0] + ":" + timeR[1] + " " + serviceListActivity.getString(R.string.str_hrs_left);
        } catch (Exception e) {
            return serviceListActivity.getString(R.string.str_expired);
        }
        return timeRemaining;
    }

    /**
     * get required current date format.
     */
    public static String getCurrentMonth(Activity activity, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        Log.d("Month :: ", dateFormat.format(date));
        return dateFormat.format(date);
    }

    /**
     * get required current date format.
     */
    public static String getCurrentTimeUTC(Activity activity, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
        Date date = new Date();
        Log.d("getCurrentTimeUTC ::  ", dateFormat.format(date));
        return dateFormat.format(date);
    }

    /**
     * convert UTC to Local
     */
    public static String convertUTCtoLocal(String time) {

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());
        String formattedDate = df.format(date);
        return formattedDate;
    }

    /**
     * get required current date format.
     */
    public static String getCurrentFormat(Activity activity, String dateText, String inFormat, String outFormat) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inFormat);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outFormat);

        Date date = new Date();
        String str = null;

        try {
            Log.d(" in date :::::", dateText);
            date = inputFormat.parse(dateText);
            dateText = outputFormat.format(date);

            Log.d(" out date :::::", dateText);
            return dateText;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateText;
    }

    public static String getdateStatus(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        Date strDate = null;
        try {
            strDate = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int catalog_outdated;
        if (System.currentTimeMillis() > strDate.getTime()) {
            catalog_outdated = 1;
            Log.d(TAG, "" + dateString + " past " + catalog_outdated);
            return "Past Appointments";
        } else {
            catalog_outdated = 0;
            Log.d(TAG, "" + dateString + " upcoming " + catalog_outdated);
            return "Upcoming Appointments";
        }


    }

    public static long getValidTimestamp(long dateSent) {
        return dateSent * 1000;
    }
}
