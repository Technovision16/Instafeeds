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

public class ReScheduleAppointments extends AppCompatActivity {

    TextView location,dateOfAppointment,labname;
    Button home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_schedule_appointments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        location=(TextView)findViewById(R.id.street1);
        dateOfAppointment=(TextView) findViewById(R.id.date);
        labname=(TextView) findViewById(R.id.labname1);

        location.setText(getIntent().getStringExtra("area").toString()+","+getIntent().getStringExtra("city").toString());
        dateOfAppointment.setText(getIntent().getStringExtra("date"));
        labname.setText(getIntent().getStringExtra("lab"));

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReScheduleAppointments.this,DashBoard.class);
                startActivity(intent);
            }
        });





    }

}
