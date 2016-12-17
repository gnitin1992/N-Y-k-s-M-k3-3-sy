package net.studiesmadesimple.nayaks.fragments;

import android.app.ProgressDialog;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.data.CenterData;
import net.studiesmadesimple.nayaks.database.MyDatabaseHelper;
import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by studiesmadesimple on 10/16/2016.
 */

public class EnterCouponCodeFragment extends Fragment implements View.OnClickListener,
        Response.Listener<JSONObject>, Response.ErrorListener,
        AdapterView.OnItemSelectedListener{

    private View v;
    private Button submit, clickHere;
    private ProgressDialog progressDialog;
    private EditText couponCode;
    private Spinner centerList;
    private TextView centerDetails;

    private List<String> centerTitles;
    private List<CenterData> centerData;

    private MyDatabaseHelper databaseHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new MyDatabaseHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_enter_coupon_code, container, false);

        Log.d(Constants.LOG_TAG, Constants.ENTER_COUPON_CODE_FRAGMENT);

        findViews();
        setViews();

        return v;
    }

    public void findViews() {

        couponCode = (EditText) v.findViewById(R.id.coupon_edit_enter_coupon_code_fragment) ;
        centerList = (Spinner) v.findViewById(R.id.centers_spinner_enter_coupon_code_fragment);
        clickHere = (Button) v.findViewById(R.id.click_here_button_enter_coupon_code_fragment);
        submit = (Button) v.findViewById(R.id.submit_button_enter_coupon_code_fragment);
        centerDetails = (TextView) v.findViewById(R.id.center_details_text_enter_coupon_code_fragment);
    }

    public void setViews() {

        submit.setOnClickListener(this);
        clickHere.setOnClickListener(this);
    }


    public void submit() {


        HelperMethods.showProgressDialog(getActivity(),Constants.APP_NAME,"Loading...");

        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
            String getMobileNumber = sharedPreferences.getString(Constants.MOBILE_NUMBER,"");


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone",getMobileNumber);
            jsonObject.put("coupon_code",couponCode.getText().toString().trim());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.verifyCouponCodeUrl, jsonObject, this,this);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(jsonObjectRequest);

        }
        catch (Exception e){

            e.printStackTrace();
        }

    }

    public void setCustomAdapter(List<String> title) {


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, title);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        centerList.setAdapter(adapter);
        centerList.setOnItemSelectedListener(this);

    }

    public void getCenters() {

        HelperMethods.showProgressDialog(getActivity(),Constants.APP_NAME,"Loading...");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.fetchCentersUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{

                    HelperMethods.dismissProgressDialog();

                    centerTitles = new ArrayList<String>();
                    centerData = new ArrayList<CenterData>();

                    String statusCode = response.getString("status");
                    if(statusCode.equalsIgnoreCase("200")){

                        JSONArray jsonArray = response.getJSONArray("center");
                        for (int i =0;i<jsonArray.length();i++){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String name = jsonObject.getString("name");
                            String address = jsonObject.getString("address");
                            String phone = jsonObject.getString("phone");
                            String email = jsonObject.getString("email");

                            centerTitles.add(name);
                            centerData.add(new CenterData(id,name,address,phone,email));
                            databaseHelper.addCenter(new CenterData(id,name,address,phone,email));
                        }

                        setCustomAdapter(centerTitles);
                    }
                    else{

                        Toast.makeText(getActivity(),"Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();
                    }


                }
                catch (Exception e){

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                HelperMethods.dismissProgressDialog();
                Toast.makeText(getActivity()," The error occured", Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.submit_button_enter_coupon_code_fragment:
                submit();
                break;
            case R.id.click_here_button_enter_coupon_code_fragment:
                getCenters();
                break;

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        centerDetails.setVisibility(View.VISIBLE);
        String centerD = centerData.get(i).getCenterName()
                + System.getProperty("line.separator")+centerData.get(i).getCenterAddress()
                +System.getProperty("line.separator")+centerData.get(i).getCenterEmail()
                +System.getProperty("line.separator")+centerData.get(i).getCenterPhone();

        centerDetails.setText(centerD);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onResponse(JSONObject response) {

        HelperMethods.dismissProgressDialog();

        try{

            String statusCode = response.getString("status");
            if(statusCode.equalsIgnoreCase("200")){

                JSONObject jsonObject = response.getJSONObject("student");
                String id = jsonObject.getString("id");
                String uniqueId = jsonObject.getString("unique_id");


                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.UNIQUE_ID, uniqueId);
                editor.putBoolean(Constants.COUPON_VERIFIED, true);
                editor.commit();

                HelperMethods.showFragment(getActivity(), new SegmentsFragment(),false);

            }
            else if(statusCode.equalsIgnoreCase("201")){

                Toast.makeText(getActivity(),"Please check the coupon code", Toast.LENGTH_SHORT).show();
            }
            else{

                Toast.makeText(getActivity(),"Something went wrong.Try again later.", Toast.LENGTH_SHORT).show();
            }


        }
        catch (Exception e){

            e.printStackTrace();
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        HelperMethods.dismissProgressDialog();
        Toast.makeText(getActivity(),"Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();

    }


}
