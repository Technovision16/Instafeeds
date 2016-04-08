package com.healthpatient.patientapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthpatient.patientapp.Utils.Constants;

public class AppointmentFragment extends Fragment {

    String meta_key;

    public AppointmentFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        SharedPreferences sharedpref = getActivity().getSharedPreferences(Constants.PREFERENCE_KEY, Context.MODE_PRIVATE);
        meta_key = sharedpref.getString(Constants.METAKEY, "null");



        return view;
    }

}
