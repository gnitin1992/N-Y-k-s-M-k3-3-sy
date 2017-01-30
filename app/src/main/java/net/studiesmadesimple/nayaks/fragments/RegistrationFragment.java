package net.studiesmadesimple.nayaks.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.data.NewStreamData;
import net.studiesmadesimple.nayaks.database.MyDatabaseHelper;
import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.GlobalVariables;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by studiesmadesimple on 10/16/2016.
 */

public class  RegistrationFragment extends Fragment implements View.OnClickListener,
        Response.Listener<JSONObject>, Response.ErrorListener, AdapterView.OnItemSelectedListener {

    private View v;
    private EditText firstName, lastName;
    private Spinner streams;
    private Button submit;
    private JSONObject jsonObject;

    private boolean isInternetAvailable, isStreamsLoaded;
    private MyDatabaseHelper databaseHelper;
    private List<NewStreamData> streamsData;
    private List<String> streamsDataForSpinner = new ArrayList<String>();;
    private String selectedStreamId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isInternetAvailable = HelperMethods.isInternetAvailable(getActivity());
        isStreamsLoaded = GlobalVariables.getInstance().isStreamsLoaded();
        databaseHelper = new MyDatabaseHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_registration, container, false);

        Log.d(Constants.LOG_TAG, Constants.REGISTRATION_FRAGMENT);

        if (isStreamsLoaded) {

            fetchStreamsFromDatabase();
        } else {

            fetchStreams();
        }

        findViews();
        setViews();
        setCustomAdapter();

        return v;
    }

    public void fetchStreamsFromDatabase() {

        streamsData = databaseHelper.getAllStreams();
        for (int i = 0; i < streamsData.size(); i++) {

            streamsDataForSpinner.add(streamsData.get(i).getStreamName());
        }
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
                    streamsDataForSpinner = new ArrayList<String>();
                    String responsCode = response.getString("status");
                    if (responsCode.equalsIgnoreCase("200")) {

                        JSONArray jsonArray = response.getJSONArray("stream");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String streamId = jsonObject.getString("id");
                            String streamName = jsonObject.getString("name");

                            streamsData.add(new NewStreamData(streamId, streamName));
                            streamsDataForSpinner.add(streamName);
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

    public void findViews() {

        firstName = (EditText) v.findViewById(R.id.first_name_edit_registration_fragment);
        lastName = (EditText) v.findViewById(R.id.last_name_edit_registration_fragment);
        streams = (Spinner) v.findViewById(R.id.stream_spinner_registration_fragment);
        submit = (Button) v.findViewById(R.id.submit_button_registration_fragment);

    }

    public void setViews() {

        submit.setOnClickListener(this);
    }

    public void setCustomAdapter() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, streamsDataForSpinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        streams.setAdapter(adapter);
        streams.setOnItemSelectedListener(this);
    }

    public void submit() {

        HelperMethods.showProgressDialog(getActivity(), Constants.APP_NAME, "Loading...");

        try {

            jsonObject = new JSONObject();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
            String mobileNumber = sharedPreferences.getString(Constants.MOBILE_NUMBER, "NA");
            String deviceId = sharedPreferences.getString(Constants.DEVICE_ID, "NA");
            String deviceName = sharedPreferences.getString(Constants.DEVICE_NAME, "NA");

            if (!mobileNumber.equalsIgnoreCase("NA")) {


                jsonObject.put("phone",mobileNumber);
                jsonObject.put("device_id", deviceId);
                jsonObject.put("first_name", firstName.getText().toString().trim());
                jsonObject.put("last_name", lastName.getText().toString().trim());
                jsonObject.put("middle_name","");
                jsonObject.put("stream_id", selectedStreamId);

            } else {

                Toast.makeText(getActivity(),"Mobile number not verified",5000).show();
                return;
            }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.registerUrl, jsonObject, this, this);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(jsonObjectRequest);

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.submit_button_registration_fragment:
                submit();
                break;

        }
    }


    @Override
    public void onResponse(JSONObject response) {

        HelperMethods.dismissProgressDialog();

        try {

            String responseCode = response.getString("status");
            if (responseCode.equalsIgnoreCase("200")) {


                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Constants.USER_REGISTERED, true);
                editor.putString(Constants.STREAM_ID,selectedStreamId);
                editor.commit();

                HelperMethods.showFragment(getActivity(), new StreamsFragment(),false);

            } else if(responseCode.equalsIgnoreCase("201")) {

                String message = response.getString("message");

                Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
            }
            else{

                Toast.makeText(getActivity(),"Something went wrong.Try again later.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        HelperMethods.dismissProgressDialog();
        Toast.makeText(getActivity(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        selectedStreamId = streamsData.get(i).getStreamId();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
