package com.healthpatient.patientapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.healthpatient.patientapp.Utils.URL1;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    EditText firstname,lastname,email,phone,dob;
    RadioGroup gender;
    String genders;
    RadioButton male,female;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firstname=(EditText)findViewById(R.id.firstname);
        lastname=(EditText) findViewById(R.id.LastName);
        email=(EditText) findViewById(R.id.emailid);
        phone=(EditText) findViewById(R.id.mobile);
        dob=(EditText) findViewById(R.id.dob);
        gender=(RadioGroup) findViewById(R.id.gendergroup);
        male=(RadioButton) findViewById(R.id.male);
        female=(RadioButton) findViewById(R.id.female);
        if(male.isSelected())
            genders="male";
        else
            genders="female";

        register=(Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerwork();
            }
        });
    }
    void registerwork()
    {
        SignUpTask signUpTask=new SignUpTask(firstname.getText().toString(),lastname.getText().toString()
                ,phone.getText().toString(),dob.getText().toString(),email.getText().toString(),genders);
        signUpTask.execute();
    }

    void parsejson(String success)
    {
        try
        {
            JSONObject jsonObject=new JSONObject(success);
            String message=jsonObject.getString("message");
            if(message.equals(" "))
            {
                Toast.makeText(Signup.this,"Successful Register",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Signup.this, LoginActivity.class);
                startActivity(intent);
            }
                else
            Toast.makeText(Signup.this,"Sorry..Please Try Later..",Toast.LENGTH_LONG).show();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public class SignUpTask extends AsyncTask<Void, Void, String>
    {

         String first,last,dob,mob,email,mlink,gender;
        String error=null;
        HttpURLConnection conn;
        BufferedReader bufferedReader;

        SignUpTask(String first,String last,String mob,String dob,String email,String gender)
        {
            mlink = URL1.getRegisterURL();
            this.first=first;
            this.last=last;
            this.mob=mob;
            this.dob=dob;
            this.email=email;
            this.gender=gender;
        }
        @Override
        protected String doInBackground(Void... params) {

            try
            {
                URL url=new URL(mlink);
                Map<String,Object> param=new LinkedHashMap<String, Object>();
                StringBuilder postData=new StringBuilder();

                if(postData.length()!=0)
                    postData.append('&');
                postData.append(URLEncoder.encode("name", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode( first+" "+last,"UTF-8"));
                if(postData.length()!=0)
                    postData.append('&');
                postData.append(URLEncoder.encode("email", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode( email , "UTF-8"));
                if(postData.length()!=0)
                    postData.append('&');
                postData.append(URLEncoder.encode("phone", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode( mob , "UTF-8"));
                if(postData.length()!=0)
                    postData.append('&');
                postData.append(URLEncoder.encode("gender", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode( gender, "UTF-8"));
                if(postData.length()!=0)
                    postData.append('&');
                postData.append(URLEncoder.encode("birthday", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode( dob , "UTF-8"));
                if(postData.length()!=0)
                    postData.append('&');
                postData.append(URLEncoder.encode("action", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode( "patient","UTF-8"));

                byte[] postDataBytes=postData.toString().getBytes("UTF-8");


                conn=(HttpURLConnection)url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("X-Patient-App", "true");
                conn.getOutputStream().write(postDataBytes);

                InputStream inputStream = conn.getInputStream();

                StringBuffer buffer = new StringBuffer();
                if(inputStream==null){
                    return "null_inputstream";
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line ;

                while ( (line=bufferedReader.readLine())!=null ){
                    buffer.append(line + '\n');
                }

                if (buffer.length() == 0) {
                    return "null_inputstream";
                }

                String stringJSON = buffer.toString();
                Log.v("MyApp", "JSON retured in Attendance" + stringJSON);

                return stringJSON;

            } catch (UnknownHostException | ConnectException e) {
                error = "null_internet" ;
                e.printStackTrace();
            } catch (IOException e) {
                error= "null_file";
                e.printStackTrace();
            } finally {
                if ( conn!= null) {
                    conn.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
//                        Log.e(LOG_CAT, "ErrorClosingStream", e);
                    }
                }
            }

            return error;
        }
        @Override
        protected void onPostExecute(final String success) {
            parsejson(success);

        }
    }

}
