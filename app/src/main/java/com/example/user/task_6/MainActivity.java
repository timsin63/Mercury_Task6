package com.example.user.task_6;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private Calendar calendar;
    TextView textDate, textTime;
    EditText descriptionInput, titleInput;
    public static final String EXTRA_CALENDAR = "CALENDAR_EXTRA";
    public static final String EXTRA_TITLE = "TITLE_EXTRA";
    public static final String EXTRA_DESCRIPTION = "DESCRIPTION_EXTRA";
    public static final String PREF_NAME = "PREF_NAME";
    public static final int START_SERVICE_REQUEST_CODE = 123;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        String title = preferences.getString(EXTRA_TITLE, null);
        String description = preferences.getString(EXTRA_DESCRIPTION, null);
        String savedCalendarJson = preferences.getString(EXTRA_CALENDAR, null);

        titleInput = (EditText) findViewById(R.id.title_input);
        descriptionInput = (EditText) findViewById(R.id.description_input);
        textDate = (TextView) findViewById(R.id.text_date);
        textTime = (TextView) findViewById(R.id.text_time);

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(savedCalendarJson)){
            titleInput.setText(title);
            descriptionInput.setText(description);
            Gson gson = new Gson();
            calendar = gson.fromJson(savedCalendarJson, Calendar.class);
        } else {
            calendar = new GregorianCalendar(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
            calendar.set(Calendar.SECOND, 0);
        }

        textDate.setText(getDate(calendar));

        textTime.setText(getTime(calendar));

        final Button saveBtn = (Button) findViewById(R.id.button_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(titleInput.getText())){
                    Toast.makeText(getApplicationContext(), R.string.error_title, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (calendar.before(new GregorianCalendar(TimeZone.getTimeZone(getResources().getString(R.string.timezone))))){
                    Toast.makeText(getApplicationContext(), R.string.error_date, Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent broadcastIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
                broadcastIntent.putExtra(EXTRA_CALENDAR, calendar);
                broadcastIntent.putExtra(EXTRA_TITLE, titleInput.getText().toString());
                broadcastIntent.putExtra(EXTRA_DESCRIPTION, descriptionInput.getText().toString());

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), START_SERVICE_REQUEST_CODE, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                savePref();
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_set) + ": " + getDate(calendar) + " " + getTime(calendar), Toast.LENGTH_LONG).show();

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
            textDate.setText(getDate(calendar));
        }
    };

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker timePicker, int chosenHour, int chosenMinute) {
            calendar.set(Calendar.HOUR_OF_DAY, chosenHour);
            calendar.set(Calendar.MINUTE,chosenMinute);
            textTime.setText(getTime(calendar));
        }
    };


    public String getDate(Calendar calendar){
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH)+1) + "." + calendar.get(Calendar.YEAR);
    }

    public String getTime(Calendar calendar){
        return calendar.get(Calendar.MINUTE) < 10? calendar.get(Calendar.HOUR_OF_DAY) +
                ":0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        savePref();
        super.onSaveInstanceState(outState);
    }


    private void savePref(){
        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EXTRA_TITLE, titleInput.getText().toString());
        editor.putString(EXTRA_DESCRIPTION, descriptionInput.getText().toString());
        Gson gson = new Gson();
        String json = gson.toJson(calendar);
        editor.putString(EXTRA_CALENDAR, json);
        editor.commit();
    }
}
