package net.studiesmadesimple.nayaks.fcm;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import net.studiesmadesimple.nayaks.data.NewSegmentsData;
import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sagar on 12/6/2016.
 */

public class NayaksFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private String refreshedToken;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(Constants.LOG_TAG, "Refreshed token: " + refreshedToken);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.APP_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.IS_TOKEN_STORED,false);
        editor.commit();


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        // TODO: Implement this method to send token to your app server.

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

                        Log.d(Constants.LOG_TAG,"Something went wrong. Try again later. Notifications ");
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
