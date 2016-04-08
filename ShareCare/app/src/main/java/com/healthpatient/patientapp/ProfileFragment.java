package com.healthpatient.patientapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.healthpatient.patientapp.Utils.Constants;
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
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {

    TextView name,mob,email;
    String meta_key;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences sharedpref= getActivity().getSharedPreferences(Constants.PREFERENCE_KEY, Context.MODE_PRIVATE);
        meta_key=sharedpref.getString(Constants.METAKEY,"null");
        name=(TextView) view.findViewById(R.id.tv_user_details_name);
        email=(TextView) view.findViewById(R.id.text_userdetails_contactdetails_email);
        mob=(TextView) view.findViewById(R.id.text_userdetails_contactdetails_mobile);
        SearchLabTask searchLabTask=new SearchLabTask();
        searchLabTask.execute();
        return view;
    }
    public class SearchLabTask extends AsyncTask<Void, Void, String>
    {

        final String mlink;
        String error=null;
        int position;
        HttpURLConnection conn;
        BufferedReader bufferedReader;

        SearchLabTask()
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
                conn.setRequestMethod("GET");
                conn.setRequestProperty("X-App", "patient");
                conn.setRequestProperty("X-Access-Token",meta_key);

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
                Log.v("MyApp", "JSON retured in Profile" + stringJSON);

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
    void parsejson(String success)
    {
        try {
            JSONObject jsonObject=new JSONObject(success);
            JSONObject user=jsonObject.getJSONObject("user");
            String name_s=user.getString("_name");
            String phone_s=user.getString("_phone");
            String email_s=user.getString("_email");
            String gender_s=user.getString("_gender");
            name.setText(name_s);
            mob.setText(phone_s);
            email.setText(email_s);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
