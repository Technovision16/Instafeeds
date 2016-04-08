package com.healthpatient.patientapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

public class ScheduleAppointments extends AppCompatActivity implements OnDateSelectedListener,OnMonthChangedListener{

    TextView dates;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_appointments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dates=(TextView) findViewById(R.id.dateselected);
        next=(Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ScheduleAppointments.this,LabActivity.class).putExtra("city",getIntent().getStringExtra("city"))
                        .putExtra("area", getIntent().getStringExtra("area")).putExtra("date", dates.getText());
                startActivity(intent);
            }
        });
    }


    @Override
    public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {

        dates.setText(date.toString());
        Toast.makeText(ScheduleAppointments.this,"You HAve Selected Date "+ date,Toast.LENGTH_SHORT);

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }
}
