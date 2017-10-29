package com.hezare.mmm.WebSide;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by amirhododi on 7/29/2017.
 */


public class Parser {
    public static JSONArray Parse(String root) {
        String prc=root.replaceAll("\\\\","").replaceAll("^\"|\"$", "");
        JSONArray jArray=null;
       try {
           JSONObject jObject = new JSONObject(prc);
            jArray = jObject.getJSONArray("Root");
       }catch (Exception e){

       }


        return jArray;
    }










}
