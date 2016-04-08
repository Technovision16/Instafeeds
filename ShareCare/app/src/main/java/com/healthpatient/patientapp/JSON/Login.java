package com.healthpatient.patientapp.JSON;

import android.util.Log;

import com.healthpatient.patientapp.Model.Loggedin;
import com.healthpatient.patientapp.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrsinghania on 18/2/16.
 */

import com.healthpatient.patientapp.Model.Loggedin;
import com.healthpatient.patientapp.Utils.Constants;
public class Login {
    String Username, Password;
    String TAG="MyApp - Login";

    public Login(String username, String password) {
        this.Username = username;
        this.Password = password;

    }

    public String buildjson() {
        try {
            final JSONObject jsonBody = new JSONObject("{\"username\":" + Username + ",\"pass\":" + Password + "}");
            Log.v("MyApp", jsonBody.toString());
            return jsonBody.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";

        }
    }

    /*public List<Loggedin> parsejson(String jsonResponse) {
        List<Loggedin> loginList = new ArrayList<Loggedin>();
        if (jsonResponse != null) {

            try {
                // Creating JSONObject from String
                JSONObject jsonObjMain = new JSONObject(jsonResponse);

                // Creating JSONArray from JSONObject
                JSONObject jsonobj = jsonObjMain.getJSONObject(Constants.DATA);

                Loggedin login = new Loggedin();

                login.setFirstname(jsonobj.getString(Constants.FIRSTNAME));
                login.setlastname(jsonobj.getString(Constants.LASTNAME));
                login.setUsername(jsonobj.getString(Constants.USERNAME));

                Log.v(TAG, "test = " + jsonobj.getString(Constants.FIRSTNAME));
                loginList.add(login);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return loginList;
    }
    */
}
