package com.healthpatient.patientapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LabActivity extends AppCompatActivity {

    CardView lab1,lab2;
    TextView street1,street2;
    TextView labname1,labname2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        labname1 = (TextView) findViewById(R.id.labname1);
        labname2=(TextView) findViewById(R.id.labname);

        lab1=(CardView) findViewById(R.id.lab1);
        lab2=(CardView) findViewById(R.id.lab2);
        lab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LabActivity.this, ReScheduleAppointments.class).putExtra("lab",labname1.getText())
                        .putExtra("city",getIntent().getStringExtra("city"))
                        .putExtra("area",getIntent().getStringExtra("area"))
                        .putExtra("date",getIntent().getStringExtra("date"));
                startActivity(intent);
            }
        });
        lab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LabActivity.this, ReScheduleAppointments.class).putExtra("lab",labname2.getText())
                        .putExtra("city",getIntent().getStringExtra("city"))
                        .putExtra("area",getIntent().getStringExtra("area"));
                startActivity(intent);

            }
        });
        street1=(TextView) findViewById(R.id.street1);
        street2=(TextView) findViewById(R.id.street2);

        street1.setText(getIntent().getStringExtra("area")+","+getIntent().getStringExtra("city"));
        street2.setText(getIntent().getStringExtra("area")+","+getIntent().getStringExtra("city"));

    }

}
