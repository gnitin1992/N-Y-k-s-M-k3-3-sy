package net.studiesmadesimple.nayaks.fragments;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

/**
 * Created by studiesmadesimple on 10/16/2016.
 */

public class EnterMobileNumberFragment extends Fragment implements View.OnClickListener, VerificationListener {

    private View v;
    private EditText mobileNumber;
    private TextView relaxStatic, tillWeStatic;
    private Button next;
    private Verification verification;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_enter_mobile_number, container, false);

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Log.d(Constants.LOG_TAG, Constants.ENTER_OTP_FRAGMENT);

        findViews();
        setViews();

        return v;
    }

    public void findViews() {

        relaxStatic = (TextView) v.findViewById(R.id.static_text_relax);
        tillWeStatic = (TextView) v.findViewById(R.id.static_text_till_we);
        mobileNumber = (EditText) v.findViewById(R.id.mobile_edit_enter_mobile_number_fragment);
        next = (Button) v.findViewById(R.id.next_button_enter_mobile_number_fragment);

    }

    public void setViews() {

        next.setOnClickListener(this);
    }


    public void next() {

        if (relaxStatic.getVisibility() == View.GONE) {

            if(mobileNumber.getText().toString().length()==10){

                HelperMethods.showProgressDialog(getActivity(), Constants.APP_NAME, "Loading...");

                verification = SendOtpVerification.createSmsVerification(getActivity(), mobileNumber.getText().toString().trim(), this, "91");
                verification.initiate();
            }
            else{

                Toast.makeText(getActivity(),"Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
            }


        } else if (relaxStatic.getVisibility() == View.VISIBLE) {

            HelperMethods.showProgressDialog(getActivity(), Constants.APP_NAME, "Loading...");

            verification.verify(mobileNumber.getText().toString().trim());
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.next_button_enter_mobile_number_fragment:
                next();
                break;

        }

    }


    @Override
    public void onInitiated(String response) {

        HelperMethods.dismissProgressDialog();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.MOBILE_NUMBER, mobileNumber.getText().toString());
        editor.commit();


        relaxStatic.setVisibility(View.VISIBLE);
        tillWeStatic.setVisibility(View.GONE);
        mobileNumber.setText("");
        mobileNumber.setHint("OTP");
        next.setText("Submit");
    }

    @Override
    public void onInitiationFailed(Exception paramException) {

        HelperMethods.dismissProgressDialog();
        Toast.makeText(getActivity(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onVerified(String response) {

        HelperMethods.dismissProgressDialog();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.MOBILE_NUMBER_VERIFIED, true);
        editor.commit();

        for (int i =0 ; i<getActivity().getSupportFragmentManager().getBackStackEntryCount();i++){

            getActivity().getSupportFragmentManager().popBackStack();
        }



        HelperMethods.showFragment(getActivity(), new EnterUniqueIdFragment(),false);

    }

    @Override
    public void onVerificationFailed(Exception paramException) {

        HelperMethods.dismissProgressDialog();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.MOBILE_NUMBER_VERIFIED, false);
        editor.commit();

        Toast.makeText(getActivity(), "Sorry we could not verify. Try again later.", Toast.LENGTH_SHORT).show();

    }
}
