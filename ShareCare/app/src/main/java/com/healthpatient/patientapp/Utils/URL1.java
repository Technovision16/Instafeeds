package com.healthpatient.patientapp.Utils;

/**
 * Created by mrsinghania on 18/2/16.
 */
public class URL1 {
    public static String getLoginURL() {
        String url = "auth/login.json";
        return Constants.BASE_URL+url;
    }
    public static String getCityURL() {
        String url = "home/city.json";
        return Constants.BASE_URL+url;
    }
    public static String getAreaURL(String city_id) {
        String url = "home/area/";
        return Constants.BASE_URL+url + city_id+".json";
    }
    public static String getTestURL() {
        String url = "medicaltest/search.json";
        return Constants.BASE_URL+url ;
    }
}
