package com.healthpatient.patientapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.healthpatient.patientapp.Model.Loggedin;
import com.healthpatient.patientapp.Utils.Constants;
import com.healthpatient.patientapp.Utils.URL1;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

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
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class SearchLabs extends AppCompatActivity {

    AutoCompleteTextView cityview,areaview,searchview,testview,newView;
    List<AutoCompleteTextView> testlist;
    int city_id,hint=0;
    List<String[]> loginList;
    CardView testcard;
    LinearLayout linear,outerlinear;
    ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_labs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        cityview=(AutoCompleteTextView) findViewById(R.id.city);
        areaview=(AutoCompleteTextView) findViewById(R.id.area);
        searchview=(AutoCompleteTextView) findViewById(R.id.searchlabs);
        linear=(LinearLayout) findViewById(R.id.test);
        outerlinear=(LinearLayout) findViewById(R.id.search_linear);
        imageButton=(ImageButton) findViewById(R.id.imageButton);
        testcard=(CardView) findViewById(R.id.testcard);
        testlist=new ArrayList<AutoCompleteTextView>();

        cityview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SearchTask citytask=new SearchTask();
                citytask.execute();

            }
        });
        /*cityview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city_id = position;
                Log.v("MyApp", "Area correct");
                AreaTask areatask = new AreaTask(Integer.toString(city_id));
                areatask.execute();
                Log.v("MyApp", "Area correct");
            }
        });*/
        areaview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityview.getText().toString();
                for(int i=0;i<loginList.size();i++) {
                    if(loginList.get(i)[1].equals(cityview.getText().toString()))
                    {
                        city_id=Integer.parseInt(loginList.get(i)[0]);
                    }
                }
                AreaTask areatask = new AreaTask(Integer.toString(city_id));
                areatask.execute();
            }
        });
        searchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestTask testtask = new TestTask(0);
                testtask.execute();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEditTextView();
            }
        });

        for(int i=1;i<testlist.size();i++)
        {
            final int t=i;
            testlist.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TestTask testTask=new TestTask(t);
                    testTask.execute();
                }
            });
        }

    }
    protected void createEditTextView() {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams (
                LinearLayout.LayoutParams.MATCH_PARENT    ,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(0, 10, 0, 10);
        AutoCompleteTextView edittTxt = new AutoCompleteTextView(this);

        edittTxt.setHint("Type Another TestName");
        edittTxt.setLayoutParams(params);
        edittTxt.setInputType(InputType.TYPE_CLASS_TEXT);
        edittTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        edittTxt.setId(hint);
        hint++;
        testlist.add(edittTxt);
        linear.addView(edittTxt);


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
                new ArrayAdapter<>(SearchLabs.this,
                        android.R.layout.simple_dropdown_item_1line,list);


        cityview.setAdapter(adapter);
    }

    public class AreaTask extends AsyncTask<Void, Void, String>
    {

        final String mlink;
        String error=null;
        HttpURLConnection conn;
        BufferedReader bufferedReader;

        AreaTask(String city_id)
        {
            mlink = URL1.getAreaURL(city_id);

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
        List<String[]> loginList = new ArrayList<String[]>();
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
                    loginList.add(string);
                }



            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return loginList;
    }
    private void addareaToAutoComplete(List<String[]> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        List<String> list=new ArrayList<String>();
        for(int i=0;i<emailAddressCollection.size();i++) {
            list.add(emailAddressCollection.get(i)[1]);
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SearchLabs.this,
                        android.R.layout.simple_dropdown_item_1line, list);

        areaview.setAdapter(adapter);
    }
    public class TestTask extends AsyncTask<Void, Void, String>
    {

        final String mlink;
        String error=null;
        int position;
        HttpURLConnection conn;
        BufferedReader bufferedReader;

        TestTask(int position)
        {
            mlink = URL1.getTestURL();
            this.position=position;
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
            addtestToAutoComplete(parsejson3(success),position);

        }
    }
    public List<String[]> parsejson3(String jsonResponse)
    {
        List<String[]> loginList = new ArrayList<String[]>();
        if (jsonResponse != null) {

            try {
                JSONObject alljson=new JSONObject(jsonResponse);

                // Creating JSONObject from String
                JSONArray jsonObjMain = alljson.getJSONArray(Constants.ALLTESTS);
                for(int i=0;i<=jsonObjMain.length();i++)
                {
                    String[] string=new String[2];
                    JSONObject city=jsonObjMain.getJSONObject(i);
                    string[0]=city.getString(Constants.ID);
                    string[1]=city.getString(Constants.TESTNAME);
                    loginList.add(string);
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return loginList;
    }
    private void addtestToAutoComplete(List<String[]> emailAddressCollection,int position) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        List<String> list=new ArrayList<String>();
        for(int i=0;i<emailAddressCollection.size();i++) {
            list.add(emailAddressCollection.get(i)[1]);
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SearchLabs.this,
                        android.R.layout.simple_dropdown_item_1line, list);

        if(position==0)
        searchview.setAdapter(adapter);
        else
        testlist.get(position).setAdapter(adapter);
    }

}
