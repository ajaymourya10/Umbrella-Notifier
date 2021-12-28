package com.udit.umbrellanotify;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.DialogCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.udit.umbrellanotify.Adapter.WeatherForecastAdapter;
import com.udit.umbrellanotify.Common.Common;
import com.udit.umbrellanotify.Model.Weather;
import com.udit.umbrellanotify.Model.WeatherForecastResult;
import com.udit.umbrellanotify.Model.WeatherResult;

import java.text.DateFormat;
import java.util.Calendar;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ForecastFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {

   static ForecastFragment instance;
   CompositeDisposable compositeDisposable;
   IOpenWeatherMap mService;
   TextView txt_city_name,txt_get_coord;
   RecyclerView recycler_forecast;
   Button timepicker;
   int hr,min;
    Button alarm;
    Weather weather;
    WeatherForecastResult weatherForecastResult;

//    public ForecastFragment(WeatherForecastResult weatherForecastResult) {
//        this.weatherForecastResult = weatherForecastResult;
//    }

    public  static ForecastFragment getInstance() {
        if (instance==null)
            instance=new ForecastFragment();
       return instance;
    }

    public ForecastFragment() {
        compositeDisposable=new CompositeDisposable();
        Retrofit retrofit=RetroFitClient.getInstance();
        mService=retrofit.create(IOpenWeatherMap.class);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView=inflater.inflate(R.layout.fragment_forecast,container,false);
        txt_city_name=itemView.findViewById(R.id.txt_city_name);
        txt_get_coord=itemView.findViewById(R.id.txt_geo_coord);
        recycler_forecast=itemView.findViewById(R.id.recycler_forecast);
        recycler_forecast.setHasFixedSize(true);
        recycler_forecast.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        timepicker=itemView.findViewById(R.id.Timepicker);
        timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               DialogFragment tp= new TimePickerFrag();
//                tp.show(getActivity().getSupportFragmentManager(),"time picker");
                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                         hr=hourOfDay;
                         min=minute;
                        Calendar calendar=Calendar.getInstance();
                        calendar.set(0,0,0,hr,min);
                        timepicker.setText(hr+":"+min);
                    }
                },24,0,true);
                timePickerDialog.updateTime(hr,min);
                timePickerDialog.show();
            }
        });
        alarm=itemView.findViewById(R.id.alarm);
        createNotificationchannel();
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Reminder Set!",Toast.LENGTH_SHORT).show();
                Calendar calendar=Calendar.getInstance();
                Intent intent=new Intent(getContext(),ReminderBroadcast.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager=(AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
                calendar.set(Calendar.HOUR_OF_DAY,hr);
                calendar.set(Calendar.MINUTE,min);
                calendar.set(Calendar.SECOND,00);
//                long timeButtonClick=System.currentTimeMillis();
//                long tenSecondsInMillis=1000*10;
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
            }
        });

        getForecastWeatherInformation();

        // Inflate the layout for this fragment
        return itemView;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    private void getForecastWeatherInformation() {
        compositeDisposable.add(mService.getForecastWeatherByLating(
                String.valueOf(Common.current_location.getLatitude()),String.valueOf(Common.current_location.getLongitude()),Common.APP_ID,
                "metric").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                        displayForecastWeather(weatherForecastResult);
                    }
                    }
                , new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("Error", "" + throwable.getMessage());
                    }
                })
        );
    }

    private void displayForecastWeather(WeatherForecastResult weatherForecastResult) {
        txt_city_name.setText(new StringBuilder(weatherForecastResult.city.name));
        txt_get_coord.setText(new StringBuilder(weatherForecastResult.city.coord.toString()));

        WeatherForecastAdapter adapter=new WeatherForecastAdapter(getContext(),weatherForecastResult);
        recycler_forecast.setAdapter(adapter);

    }



    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {

//        Calendar c=Calendar.getInstance();
//        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
//        c.set(Calendar.MINUTE,minute);
//        c.set(Calendar.SECOND,0);
//        updateTimenext(c);
//        startAlarm(c);


    }

//    private void startAlarm(Calendar c) {
//        AlarmManager alarmManager=(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
//        Intent intent=new Intent(getContext(),AlertReciever.class);
//        PendingIntent pendingIntent=PendingIntent.getBroadcast(getContext(),1,intent,0);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
//    }
//
//    private void updateTimenext(Calendar c) {
//        String timeText="Alarm Set For: ";
//        timeText+= DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
//        timepicker.setText(timeText);
//    }

    private  void createNotificationchannel()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            CharSequence name = "ReminderChannel";
            String description = "Channel for weather alert";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Reminder", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager=getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}