package net.studiesmadesimple.nayaks.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

import org.json.JSONObject;

/**
 * Created by sagar on 2/13/2017.
 */

public class RegisterFCMTokenService extends IntentService {


    public RegisterFCMTokenService() {
        super("RegisterFCMTokenService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle bundle = intent.getExtras();
        final String token = bundle.getString(Constants.TOKEN);

        try{

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("device_id", HelperMethods.getDeviceDetails(getApplicationContext()));
            jsonObject.put("token",token);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.notificationsUrl, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {

                        String responsCode = response.getString("status");
                        if (responsCode.equalsIgnoreCase("200")) {

                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.APP_NAME,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(Constants.IS_TOKEN_STORED,true);
                            editor.commit();

                        } else {

                            Log.d(Constants.LOG_TAG,"Something went wrong. Try again later. Notifications ");


                        }


                    } catch (Exception e) {

                        e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d(Constants.LOG_TAG,"Something went wrong. Try again later. Notifications "+error);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(jsonObjectRequest);
        }
        catch (Exception e){

            e.printStackTrace();
        }


    }
}
