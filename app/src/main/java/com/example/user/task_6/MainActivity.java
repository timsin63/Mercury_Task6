package com.example.user.task_6;

import android.app.AlarmManager;
import android.app.DatePickerDialog;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.content.Intent;

import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Calendar calendar;
    TextView textDate, textTime;
    EditText descriptionInput, titleInput;
    public static final String EXTRA_CALENDAR = "CALENDAR_EXTRA";
    public static final String EXTRA_TITLE = "TITLE_EXTRA";
    public static final String EXTRA_DESCRIPTION = "DESCRIPTION_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = new GregorianCalendar(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));

        titleInput = (EditText) findViewById(R.id.title_input);
        descriptionInput = (EditText) findViewById(R.id.description_input);

        textDate = (TextView) findViewById(R.id.text_date);
        textDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH)+1) + "." + calendar.get(Calendar.YEAR));

        textTime = (TextView) findViewById(R.id.text_time);
        textTime.setText(calendar.get(Calendar.MINUTE) < 10? calendar.get(Calendar.HOUR_OF_DAY) +
                ":0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));



        Button saveBtn = (Button) findViewById(R.id.button_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleInput.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), R.string.error_title, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (calendar.before(new GregorianCalendar(TimeZone.getTimeZone(getResources().getString(R.string.timezone))))){
                    Toast.makeText(getApplicationContext(), R.string.error_date, Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent serviceIntent = new Intent(getApplicationContext(), NotificationService.class);
                serviceIntent.putExtra(EXTRA_CALENDAR, calendar);
                serviceIntent.putExtra(EXTRA_TITLE, titleInput.getText());
                serviceIntent.putExtra(EXTRA_DESCRIPTION, descriptionInput.getText());

//                stopService(serviceIntent);
//                startService(serviceIntent);

                NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(titleInput.getText())
                        .setContentText(descriptionInput.getText());
                Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(activityIntent);
                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(1, builder.build());

            //    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

             //   alarmManager.set(AlarmManager.ELAPSED_REALTIME, System.currentTimeMillis() + 5000, pendingIntent);

            }
        });
    }


    public void showDatePicker(View view){
        new DatePickerDialog(this, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void showTimePicker(View view){
        new TimePickerDialog(this, onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }


    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int chosenYear, int chosenMonth, int chosenDay) {
            calendar.set(Calendar.YEAR, chosenYear);
            calendar.set(Calendar.MONTH, chosenMonth);
            calendar.set(Calendar.DAY_OF_MONTH, chosenDay);
            textDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR));
        }
    };

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker timePicker, int chosenHour, int chosenMinute) {
            calendar.set(Calendar.HOUR_OF_DAY, chosenHour);
            calendar.set(Calendar.MINUTE,chosenMinute);
            textTime.setText(calendar.get(Calendar.MINUTE) < 10? calendar.get(Calendar.HOUR_OF_DAY) +
                    ":0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        }
    };
}
