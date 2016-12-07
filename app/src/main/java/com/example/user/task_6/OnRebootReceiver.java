package com.example.user.task_6;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

import com.google.gson.Gson;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.user.task_6.MainActivity.EXTRA_CALENDAR;
import static com.example.user.task_6.MainActivity.EXTRA_DESCRIPTION;
import static com.example.user.task_6.MainActivity.EXTRA_TITLE;
import static com.example.user.task_6.MainActivity.START_SERVICE_REQUEST_CODE;

public class OnRebootReceiver extends BroadcastReceiver {
    public OnRebootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences(MainActivity.PREF_NAME, MODE_PRIVATE);

        Log.i("onreloadReceiver", "started");

        String title = preferences.getString(EXTRA_TITLE, null);
        String description = preferences.getString(EXTRA_DESCRIPTION, null);
        String savedCalendarJson = preferences.getString(EXTRA_CALENDAR, null);

        Gson gson = new Gson();
        Calendar calendar = gson.fromJson(savedCalendarJson, java.util.Calendar.class);

        Intent broadcastIntent = new Intent(context.getApplicationContext(), NotificationReceiver.class);
        broadcastIntent.putExtra(EXTRA_CALENDAR, calendar);
        broadcastIntent.putExtra(EXTRA_TITLE, title);
        broadcastIntent.putExtra(EXTRA_DESCRIPTION, description);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), START_SERVICE_REQUEST_CODE, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
