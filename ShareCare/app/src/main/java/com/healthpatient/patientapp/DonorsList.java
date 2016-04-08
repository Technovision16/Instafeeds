package com.healthpatient.patientapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class DonorsList extends AppCompatActivity {

    TextView street1,street2,blood1,blood2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        street1=(TextView) findViewById(R.id.street1);
        street2=(TextView) findViewById(R.id.street2);
        blood1=(TextView)findViewById(R.id.blood1);
        blood2=(TextView) findViewById(R.id.blood2);

        street1.setText(getIntent().getStringExtra("area")+","+getIntent().getStringExtra("city"));
        street2.setText(getIntent().getStringExtra("area")+","+getIntent().getStringExtra("city"));
        blood1.setText(getIntent().getStringExtra("blood"));
        blood2.setText(getIntent().getStringExtra("blood"));


    }

}
