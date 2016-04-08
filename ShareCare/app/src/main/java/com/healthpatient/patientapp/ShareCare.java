package com.healthpatient.patientapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ShareCare extends AppCompatActivity {

    CardView diagnose, donate, about, acceptor, reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("ShareCare");

        diagnose = (CardView) findViewById(R.id.card_diagnose);
        donate = (CardView) findViewById(R.id.donate_card);
        acceptor = (CardView) findViewById(R.id.acceptor_card);
        about = (CardView) findViewById(R.id.about_card);
        reports = (CardView) findViewById(R.id.report_card);

        diagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareCare.this, DashBoard.class);
                startActivity(intent);
            }
        });
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareCare.this, Donation.class);
                startActivity(intent);
            }
        });
        acceptor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareCare.this, Acceptor.class);
                startActivity(intent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareCare.this, AboutUs.class);
                startActivity(intent);
            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareCare.this, Reports.class);
                startActivity(intent);
            }
        });

    }
}
