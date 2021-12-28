package com.udit.umbrellanotify.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final String APP_ID="33d10f977aaf52544f78bb9d89fd7b7b";
    public static Location current_location=null;

    public static String convertUnixToDate(long dt){
        Date date=new Date(dt*1000L);
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm  dd-MM-yyyy(EEE)");
        String formatted=sdf.format(date);
        return formatted;
    }
    public static String UnixToHour(long dt){
        Date date=new Date(dt*1000L);
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        String formatted=sdf.format(date);
        return formatted;
    }
}
