package net.studiesmadesimple.nayaks.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.data.ActionData;
import net.studiesmadesimple.nayaks.data.NewSegmentsData;
import net.studiesmadesimple.nayaks.data.NewStreamData;
import net.studiesmadesimple.nayaks.database.MyDatabaseHelper;
import net.studiesmadesimple.nayaks.service.RegisterFCMTokenService;
import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.GlobalVariables;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by studiesmadesimple on 10/16/2016.
 */

public class SplashScreenFragment extends Fragment {

    private View v;
    private ImageView logo;
    private TextView appName;

    private ProgressDialog progressDialog;
    private MyDatabaseHelper databaseHelper;
    private List<NewSegmentsData> segmentsData;
    private List<NewStreamData> streamsData;
    private List<ActionData> actionsData;
    private String[] actions = {"online", "offline", "downloadable"};

    private boolean isStreamsLoaded, isSegmentsLoaded, isTokenRegistered;

    public void clearSharePreference(){

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.TOKEN);
        editor.remove(Constants.IS_TOKEN_STORED);
        editor.commit();

    }

    public boolean checkIfTokenRegistered() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, MODE_PRIVATE);
        boolean token = sharedPreferences.getBoolean(Constants.IS_TOKEN_STORED,false);

        return token;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isStreamsLoaded = GlobalVariables.getInstance().isStreamsLoaded();
        isSegmentsLoaded = GlobalVariables.getInstance().isSegmentsLoaded();
        databaseHelper = new MyDatabaseHelper(getActivity());

        isTokenRegistered = checkIfTokenRegistered();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_splash_screen, container, false);

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Log.d(Constants.LOG_TAG, Constants.SPLASH_SCREEN_FRAGMENT);

        // we are doing this to get everyone on the same page
        clearSharePreference();

        registerDevice();

        getDeviceDetails();

        findViews();

        createActions();

        if (!isSegmentsLoaded) {

            fetchStreams();
        }
        if (!isStreamsLoaded) {

            fetchSegments();
        }

        if (isSegmentsLoaded && isStreamsLoaded) {

            changeFragment();
        }


        animateViews();
        HelperMethods.isExternalStorageWritable();
        HelperMethods.isExternalStorageReadable();


        return v;
    }

    public void registerDevice() {

        if (!isTokenRegistered) {

            String newToken = FirebaseInstanceId.getInstance().getToken();

            if(newToken != null){

                Intent registerFCMTokenService = new Intent(getActivity(), RegisterFCMTokenService.class);
                registerFCMTokenService.putExtra(Constants.TOKEN, newToken);

                getActivity().startService(registerFCMTokenService);

            }


        }

    }

    public void getDeviceDetails() {

        String androidId = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String str = android.os.Build.MODEL;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.DEVICE_ID, androidId);
        editor.putString(Constants.DEVICE_NAME, str);
        editor.commit();


    }

    public void findViews() {

        logo = (ImageView) v.findViewById(R.id.logo_image_splash_screen_fragment);
        appName = (TextView) v.findViewById(R.id.name_text_splash_screen_fragment);

    }

    public void createActions() {

        actionsData = new ArrayList<ActionData>();
        for (int i = 0; i < 3; i++) {

            actionsData.add(new ActionData(actions[i]));

        }
        addActions(actionsData);

    }

    public void addActions(List<ActionData> actionData) {

        for (int i = 0; i < 3; i++) {

            databaseHelper.addAction(actionData.get(i));
        }
    }

    public void addSegments(List<NewSegmentsData> segmentsData) {

        for (int i = 0; i < segmentsData.size(); i++) {

            databaseHelper.addSegment(segmentsData.get(i));
            GlobalVariables.getInstance().setSegmentsLoaded(true);

        }

        databaseHelper.addSegment(new NewSegmentsData("000", "Notifications", "notification"));

        changeFragment();

    }

    public void fetchSegments() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(Constants.APP_NAME);
        progressDialog.setMessage("Loading...Please Wait...");
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.segmentUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                segmentsData = new ArrayList<NewSegmentsData>();

                progressDialog.dismiss();
                try {

                    String responsCode = response.getString("status");
                    if (responsCode.equalsIgnoreCase("200")) {

                        JSONArray jsonArray = response.getJSONArray("segment");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String segmentId = jsonObject.getString("id");
                            String segmentName = jsonObject.getString("name");
                            String contentType = jsonObject.getString("content_type");

                            segmentsData.add(new NewSegmentsData(segmentId, segmentName, contentType));
                        }
                        addSegments(segmentsData);

                    } else {

                        Toast.makeText(getActivity(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {

                    Toast.makeText(getActivity(), "Something went wrong.Try again later.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);

    }


    public void addStreams(List<NewStreamData> streamsData) {

        for (int i = 0; i < streamsData.size(); i++) {

            databaseHelper.addStream(streamsData.get(i));
            GlobalVariables.getInstance().setStreamsLoaded(true);

        }

    }

    public void fetchStreams() {

        HelperMethods.showProgressDialog(getActivity(), Constants.APP_NAME, "Loading...");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.streamUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                HelperMethods.dismissProgressDialog();
                try {

                    streamsData = new ArrayList<NewStreamData>();
                    String responsCode = response.getString("status");
                    if (responsCode.equalsIgnoreCase("200")) {

                        JSONArray jsonArray = response.getJSONArray("stream");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String streamId = jsonObject.getString("id");
                            String streamName = jsonObject.getString("name");

                            streamsData.add(new NewStreamData(streamId, streamName));
                        }
                        addStreams(streamsData);

                    } else {

                        Toast.makeText(getActivity(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {

                    Toast.makeText(getActivity(), "Something went wrong.Try again later.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                HelperMethods.dismissProgressDialog();
                Toast.makeText(getActivity(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);


    }


    public void animateViews() {

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_out);
        logo.startAnimation(animation);
        appName.startAnimation(animation);


    }


    public void changeFragment() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(Constants.APP_NAME, MODE_PRIVATE);
                if (sharedPreferences.getBoolean(Constants.MOBILE_NUMBER_VERIFIED, false)) {

                    if (sharedPreferences.getBoolean(Constants.UNIQUE_ID_ENTERED, false)) {

                        if (sharedPreferences.getBoolean(Constants.COUPON_VERIFIED, false)) {

                            HelperMethods.showFragment(getActivity(), new SegmentsFragment(), false);

                        } else {

                            HelperMethods.showFragment(getActivity(), new StreamsFragment(), false);
                        }
//                        else if(!sharedPreferences.getBoolean(Constants.IS_FIRST_TIME_USER, false)){
//
//                            HelperMethods.showFragment(getActivity(), new SegmentsFragment());
//                        }


                    } else if (sharedPreferences.getBoolean(Constants.USER_REGISTERED, false)) {


                        if (sharedPreferences.getBoolean(Constants.COUPON_VERIFIED, false)) {

                            HelperMethods.showFragment(getActivity(), new SegmentsFragment(), false);

                        } else {

                            HelperMethods.showFragment(getActivity(), new StreamsFragment(), false);
                        }

                    } else {

                        HelperMethods.showFragment(getActivity(), new EnterUniqueIdFragment(), false);
                    }

                } else {

                    HelperMethods.showFragment(getActivity(), new EnterMobileNumberFragment(), false);

                }


            }
        }, 3000);

    }


}

