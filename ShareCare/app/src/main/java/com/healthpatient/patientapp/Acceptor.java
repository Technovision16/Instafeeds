package com.healthpatient.patientapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.healthpatient.patientapp.Utils.Constants;
import com.healthpatient.patientapp.Utils.URL1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Acceptor extends AppCompatActivity {

    AutoCompleteTextView cityview,areaview;
    EditText firstname,lastName,Dob,email,bloodgroup,phone;
    List<String[]> loginList;
    List<String[]> areaList;
    Button search;
    int city_id;
    String meta_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedpref= Acceptor.this.getSharedPreferences(Constants.PREFERENCE_KEY, Context.MODE_PRIVATE);
        meta_key=sharedpref.getString(Constants.METAKEY,"null");

        cityview=(AutoCompleteTextView) findViewById(R.id.city);
        areaview=(AutoCompleteTextView) findViewById(R.id.area);
        firstname=(EditText) findViewById(R.id.firstname);
        lastName=(EditText) findViewById(R.id.LastName);
        Dob=(EditText) findViewById(R.id.dob);
        email=(EditText) findViewById(R.id.emailid);
        phone=(EditText) findViewById(R.id.mobile);
        bloodgroup=(EditText) findViewById(R.id.blood);
        search=(Button) findViewById(R.id.register);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Acceptor.this,DonorsList.class).putExtra("blood",bloodgroup.getText().toString())
                        .putExtra("city",cityview.getText().toString())
                        .putExtra("area",areaview.getText().toString());
                startActivity(intent);
            }
        });

        AutofillTask autofillTask=new AutofillTask();
        autofillTask.execute();

        SearchTask searchTask=new SearchTask();
        searchTask.execute();
        cityview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AreaTask areatask = new AreaTask();
                areatask.execute();
            }
        });
    }
    public class SearchTask extends AsyncTask<Void, Void, String>
    {

        final String mlink;
        String error=null;
        HttpURLConnection conn;
        BufferedReader bufferedReader;

        SearchTask()
        {
            mlink = URL1.getCityURL();
        }
        @Override
        protected String doInBackground(Void... params) {

            try
            {
                URL url=new URL(mlink);
                Map<String,Object> param=new LinkedHashMap<String, Object>();



                conn=(HttpURLConnection)url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

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
            addcityToAutoComplete(parsejson(success));

        }
    }
    public List<String[]> parsejson(String jsonResponse)
    {
        loginList = new ArrayList<String[]>();
        if (jsonResponse != null) {

            try {
                // Creating JSONObject from String
                JSONArray jsonObjMain = new JSONArray(jsonResponse);
                for(int i=0;i<=150;i++)
                {
                    String[] string=new String[2];
                    JSONObject city=jsonObjMain.getJSONObject(i);
                    string[0]=city.getString(Constants.CITY_ID);
                    string[1]=city.getString(Constants.CITY_NAME);

                    loginList.add(string);
                }



            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return loginList;
    }
    private void addcityToAutoComplete(List<String[]> cityAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        List<String> list=new ArrayList<String>();
        for(int i=0;i<cityAddressCollection.size();i++) {
            list.add(cityAddressCollection.get(i)[1]);
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(Acceptor.this,
                        android.R.layout.simple_dropdown_item_1line,list);


        cityview.setAdapter(adapter);
    }

    public class AreaTask extends AsyncTask<Void, Void, String>
    {

        final String mlink;
        String error=null;
        HttpURLConnection conn;
        BufferedReader bufferedReader;

        AreaTask()
        {
            cityview.getText().toString();
            for (int i = 0; i < loginList.size(); i++) {
                if (loginList.get(i)[1].equals(cityview.getText().toString())) {
                    city_id = Integer.parseInt(loginList.get(i)[0]);
                }
            }
            mlink = URL1.getAreaURL(Integer.toString(city_id));

        }
        @Override
        protected String doInBackground(Void... params) {

            try
            {
                URL url=new URL(mlink);
                Map<String,Object> param=new LinkedHashMap<String, Object>();



                conn=(HttpURLConnection)url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

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
            addareaToAutoComplete(parsejson2(success));

        }
    }
    public List<String[]> parsejson2(String jsonResponse)
    {
        areaList = new ArrayList<String[]>();
        if (jsonResponse != null) {

            try {
                // Creating JSONObject from String
                JSONArray jsonObjMain = new JSONArray(jsonResponse);
                for(int i=0;i<=118;i++)
                {
                    String[] string=new String[2];
                    JSONObject area=jsonObjMain.getJSONObject(i);
                    string[0]=area.getString(Constants.CITY_ID);
                    string[1]=area.getString(Constants.CITY_NAME);
                    areaList.add(string);
                }



            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return areaList;
    }
    private void addareaToAutoComplete(List<String[]> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        List<String> list=new ArrayList<String>();
        for(int i=0;i<emailAddressCollection.size();i++) {
            list.add(emailAddressCollection.get(i)[1]);
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(Acceptor.this,
                        android.R.layout.simple_dropdown_item_1line, list);

        areaview.setAdapter(adapter);
    }
    public class AutofillTask extends AsyncTask<Void, Void, String>
    {

        final String mlink;
        String error=null;
        HttpURLConnection conn;
        BufferedReader bufferedReader;

        AutofillTask()
        {
            mlink = URL1.getProfileURL();
        }
        @Override
        protected String doInBackground(Void... params) {

            try
            {
                URL url=new URL(mlink);
                Map<String,Object> param=new LinkedHashMap<String, Object>();



                conn=(HttpURLConnection)url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("X-App", "patient");
                conn.setRequestProperty("X-Access-Token", meta_key);

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
            parsejson3(success);

        }
    }
    void parsejson3(String result)
    {
        try {
            JSONObject jsonObject=new JSONObject(result);
            JSONObject user=jsonObject.getJSONObject("user");
            String name_s=user.getString("_name");
            String phone_s=user.getString("_phone");
            String email_s=user.getString("_email");
            String gender_s=user.getString("_gender");
            String dob=user.getString("_birthday");
            String names[]=name_s.split(" ");
            firstname.setText(names[0]);
            lastName.setText(names[1]);
            Dob.setText(dob);
            phone.setText(phone_s);
            email.setText(email_s);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
