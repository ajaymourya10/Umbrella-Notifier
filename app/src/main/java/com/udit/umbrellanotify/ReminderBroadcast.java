package com.udit.umbrellanotify;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.udit.umbrellanotify.Model.Weather;
import com.udit.umbrellanotify.Model.WeatherForecastResult;
import com.udit.umbrellanotify.Model.WeatherResult;

public class ReminderBroadcast extends BroadcastReceiver {
    WeatherForecastResult weatherForecastResult;
    WeatherResult weatherResult;
    Weather weather;
    String x;
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeating_intent= new Intent(context,TodayWeatherFragment.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,100,repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);
//            if(((weather.getDescription()).equals("heavy rain"))||((weather.getDescription()).equals("light rain"))){
//                x="There is chances of heavy rain, please take your Umbrella with you";
//            }
//            else if((weather.getDescription()).equals("clear sky"))
//            {
//                x="The sky seems to be clear,no need to take Umbrella";
//            }
//            else
//            {
//                x="There is very less chances of rain,please take your Umbrella ";
//            }
        x="hello";
            NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"Reminder")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_add_alert_24)
                    .setContentTitle("Weather Alert")
                    .setContentText(x)
                    .setSound(alarmSound).setPriority(NotificationCompat.PRIORITY_DEFAULT);

            notificationManager.notify(100,builder.build());
//        }
//        else {
//            NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"Reminder")
//                    .setContentIntent(pendingIntent)
//                    .setSmallIcon(R.drawable.ic_add_alert_24)
//                    .setContentTitle("Weather Alert")
//                    .setContentText("Today weather has "+new StringBuilder(weatherForecastResult.list.get(0).weather.get(0).getDescription()))
//                    .setSound(alarmSound);
//
//            notificationManager.notify(100,builder.build());
//        }
//        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);




    }
}
