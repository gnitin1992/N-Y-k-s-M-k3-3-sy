package net.studiesmadesimple.nayaks.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.service.RegisterFCMTokenService;
import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

import org.json.JSONObject;

/**
 * Created by studiesmadesimple on 10/16/2016.
 */

public class EnterUniqueIdFragment extends Fragment implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    private View v;
    private EditText uniqueId;
    private Button submit, skip;


    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_enter_unique_id, container, false);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Log.d(Constants.LOG_TAG, Constants.ENTER_UNIQUE_ID_FRAGMENT);

        findViews();
        setViews();

        return v;
    }

    public void findViews() {

        uniqueId = (EditText) v.findViewById(R.id.mobile_edit_enter_unique_id_fragment);
        submit = (Button) v.findViewById(R.id.submit_button_enter_unique_id_fragment);
        skip = (Button) v.findViewById(R.id.skip_button_enter_unique_id_fragment);


    }

    public void setViews() {

        submit.setOnClickListener(this);
        skip.setOnClickListener(this);
    }

    public void skip() {

        HelperMethods.showFragment(getActivity(), new RegistrationFragment(),true);
    }

    public void submit() {

        HelperMethods.showProgressDialog(getActivity(), Constants.APP_NAME, "Loading...");

        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
            String getMobileNumber = sharedPreferences.getString(Constants.MOBILE_NUMBER, "");
            String deviceId = sharedPreferences.getString(Constants.DEVICE_ID, "");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("unique_id", uniqueId.getText().toString().trim());
            jsonObject.put("phone", getMobileNumber);
            jsonObject.put("device_id", deviceId);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.checkUniqueCodeUrl, jsonObject, this, this);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.skip_button_enter_unique_id_fragment:
                skip();
                break;
            case R.id.submit_button_enter_unique_id_fragment:
                submit();
                break;

        }

    }

    @Override
    public void onResponse(JSONObject response) {

        HelperMethods.dismissProgressDialog();
        try {

            String responseCode = response.getString("status");
            //this patch coded by nitin
            String responseMessage = response.getString("message");
            if (responseCode.equalsIgnoreCase("201") && responseMessage.equalsIgnoreCase("Wrong Credentials")) {
                Toast.makeText(getActivity(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
            } else if (responseCode.equalsIgnoreCase("201") && responseMessage.equalsIgnoreCase("Device Id doesn't match")) {
                Toast.makeText(getActivity(), "Wrong Credentials.Contact admin", Toast.LENGTH_SHORT).show();
            } else if (responseCode.equalsIgnoreCase("201")) {
                String streamId = response.getString("stream");
                String message = response.getString("message");

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.UNIQUE_ID, uniqueId.getText().toString().trim());
                editor.putString(Constants.STREAM_ID, streamId);
                editor.putBoolean(Constants.UNIQUE_ID_ENTERED, true);
                editor.commit();

                Toast.makeText(getActivity(),message,5000).show();
                HelperMethods.showFragment(getActivity(), new StreamsFragment(),false);
            } else if (responseCode.equalsIgnoreCase("200")) {
                String streamId = response.getString("stream");
                String skip = response.getString("skip");

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.UNIQUE_ID, uniqueId.getText().toString().trim());
                editor.putString(Constants.STREAM_ID, streamId);
                editor.putBoolean(Constants.UNIQUE_ID_ENTERED, true);


                // since uniqueId will also be asked only once will only be done once
                String newToken = FirebaseInstanceId.getInstance().getToken();
                Intent registerFCMTokenService = new Intent(getActivity(), RegisterFCMTokenService.class);
                registerFCMTokenService.putExtra(Constants.TOKEN, newToken);

                getActivity().startService(registerFCMTokenService);

                if (skip.equalsIgnoreCase("yes")) {

                    editor.putBoolean(Constants.IS_FIRST_TIME_USER, false);
                    editor.commit();

                    HelperMethods.showFragment(getActivity(), new SegmentsFragment(),false);

                } else {

                    editor.putBoolean(Constants.IS_FIRST_TIME_USER, true);
                    editor.commit();

                    HelperMethods.showFragment(getActivity(), new StreamsFragment(),false);
                }
            } else {


                Toast.makeText(getActivity(),"Something went wrong.Try again later.", Toast.LENGTH_SHORT).show();
//                String streamId = response.getString("stream");
//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString(Constants.UNIQUE_ID, uniqueId.getText().toString().trim());
//                editor.putString(Constants.STREAM_ID, streamId);
//                editor.putBoolean(Constants.UNIQUE_ID_ENTERED, true);

//                if(skip.equalsIgnoreCase("yes")){
//
//                    editor.putBoolean(Constants.IS_FIRST_TIME_USER,false);
//                    editor.commit();
//
//                   Toast HelperMethods.showFragment(getActivity(),new SegmentsFragment());
//
//                }
//                else{
//
//                    editor.putBoolean(Constants.IS_FIRST_TIME_USER,true);
//                    editor.commit();

//                HelperMethods.showFragment(getActivity(), new StreamsFragment());

            }

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    @Override
    public void onErrorResponse(VolleyError error) {

        HelperMethods.dismissProgressDialog();
        Toast.makeText(getActivity(), " Sorry something went wrong", Toast.LENGTH_LONG).show();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.UNIQUE_ID_ENTERED, false);
        editor.commit();
    }
}
