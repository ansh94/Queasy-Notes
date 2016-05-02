package com.anshdeep.simplenotes;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class AddReminderActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton reminderFab;

    EditText etTitle, etDesc;

    String title, desc,myDate,myTime;

    ImageView btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    final static int RQS_1 = 1;
    private int mYear, mMonth, mDay, mHour, mMinute;

    boolean editingReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.addreminder_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_clear_24dp);

        getSupportActionBar().setTitle("Add new reminder");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        etTitle = (EditText) findViewById(R.id.addreminder_title);
        etDesc = (EditText) findViewById(R.id.addreminder_desc);

        FloatingActionButton reminderFab = (FloatingActionButton) findViewById(R.id.addreminder_fab);

        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);
        btnDatePicker = (ImageView) findViewById(R.id.btn_date);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddReminderActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnTimePicker = (ImageView) findViewById(R.id.btn_time);
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddReminderActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });




        editingReminder = getIntent().getBooleanExtra("isEditing", false);
        if (editingReminder) {
            title = getIntent().getStringExtra("reminder_title");
            desc = getIntent().getStringExtra("reminder_body");
            myDate = getIntent().getStringExtra("reminder_date");
            myTime = getIntent().getStringExtra("reminder_time");


            etTitle.setText(title);
            etDesc.setText(desc);
            txtDate.setText(myDate);
            txtTime.setText(myTime);

        }


        reminderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtDate.getText().toString().length()>0 && txtTime.getText().toString().length()>0) {

                    // Add reminder to DB

                    String newTitle = etTitle.getText().toString();
                    String newDesc = etDesc.getText().toString();
                    String newDate = txtDate.getText().toString();
                    String newTime = txtTime.getText().toString();


                    if (!editingReminder) {
                        Log.d("Reminder", "saving or creating");
                        Reminder reminder = new Reminder(newTitle, newDesc,newDate,newTime);
                        reminder.save();
                    } else {
                        Log.d("Note", "updating");

//                    List<Note> notes = Note.findWithQuery(Note.class, "where title = ?", title);
                        List<Reminder> reminders = Reminder.find(Reminder.class, "title = ?", title);
                        if (reminders.size() > 0) {

                            Reminder reminder = reminders.get(0);
                            Log.d("got reminder", "reminder: " + reminder.title);
                            reminder.title = newTitle;
                            reminder.body = newDesc;
                            reminder.date = newDate;
                            reminder.time = newTime;
                            reminder.save();

                        }

                    }


                    String date = txtDate.getText().toString();
                    String time = txtTime.getText().toString();


                    String[] dateArray = date.split("-");
                    int day = Integer.parseInt(dateArray[0]);
                    int month = Integer.parseInt(dateArray[1]) - 1;
                    int year = Integer.parseInt(dateArray[2]);

                    String[] timeArray = time.split(":");
                    int hour = Integer.parseInt(timeArray[0]);
                    int minutes = Integer.parseInt(timeArray[1]);


                    Calendar current = Calendar.getInstance();

                    Calendar cal = Calendar.getInstance();
                    cal.set(year, month, day, hour, minutes, 00);

                    if (cal.compareTo(current) <= 0) {
                        //The set Date/Time already passed
                        Toast.makeText(getApplicationContext(),
                                "Invalid Date/Time",
                                Toast.LENGTH_LONG).show();
                    } else {
                        setAlarm(cal, newTitle, newDesc);
                    }


                    finish();
                }

                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddReminderActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Date or Time cannot be empty!");

                    String positiveText = getString(android.R.string.ok);
                    builder.setPositiveButton(positiveText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // positive button logic
                                }
                            });



                    AlertDialog dialog = builder.create();
                    // display dialog
                    dialog.show();
                }

            }
        });

    }

    // Used to convert 24hr format to 12hr format with AM/PM values


    private void setAlarm(Calendar targetCal, String title, String desc) {
        Toast.makeText(getApplicationContext(), "Reminder set for " + targetCal.getTime(), Toast.LENGTH_SHORT).show();
        Intent notifyIntent = new Intent(getBaseContext(), MyReceiver.class);
        notifyIntent.putExtra("reminder_title", title);
        notifyIntent.putExtra("reminder_desc", desc);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (getBaseContext(), RQS_1, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }


}
