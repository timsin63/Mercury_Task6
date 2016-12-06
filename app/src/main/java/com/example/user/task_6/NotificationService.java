package com.example.user.task_6;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

public class NotificationService extends Service {
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("service", "started");
        Calendar calendar = (Calendar) intent.getSerializableExtra(MainActivity.EXTRA_CALENDAR);


//        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
//                        .setSmallIcon(R.drawable.icon)
//                        .setContentTitle(intent.getStringExtra(MainActivity.EXTRA_TITLE))
//                        .setContentText(intent.getStringExtra(MainActivity.EXTRA_DESCRIPTION));
//        Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(activityIntent);
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        builder.setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(1, builder.build());
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, pendingIntent);
//
//        startForeground(1, builder.build());


        return START_STICKY;
    }
}
