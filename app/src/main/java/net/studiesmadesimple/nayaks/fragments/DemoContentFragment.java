package net.studiesmadesimple.nayaks.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.adapters.DemoContentRecyclerAdapter;
import net.studiesmadesimple.nayaks.data.TopicData;
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

public class DemoContentFragment extends Fragment implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    private View v;
    private Button unlockContent;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<TopicData> demoData;
    private boolean isInternetAvailable, isDemoContentLoaded;
    private MyDatabaseHelper databaseHelper;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isInternetAvailable = HelperMethods.isInternetAvailable(getActivity());
        isDemoContentLoaded = GlobalVariables.getInstance().isDemoContentLoaded();
        databaseHelper = new MyDatabaseHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_demo_content, container, false);

        Log.d(Constants.LOG_TAG, Constants.DEMO_CONTENT_FRAGMENT);

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        findViews();
        measuringTheLayout();

//        if (isDemoContentLoaded){
//
//            fetchDemosFromDatabase();
//
//        }
//        else{

        fetchingDemoContents();
//        }


        return v;
    }

    public void findViews() {

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_demo_content_fragment);
        unlockContent = (Button) v.findViewById(R.id.unlock_content_button_demo_content_fragment);

    }

    public void measuringTheLayout() {

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        unlockContent.setOnClickListener(this);
    }

    public void fetchingDemoContents() {

        HelperMethods.showProgressDialog(getActivity(), Constants.APP_NAME, "Loading...");

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        String streamId = sharedPreferences.getString(Constants.STREAM_ID, "NA");

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("stream_id", streamId);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.demoContentUrl, jsonObject, this, this);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(jsonObjectRequest);


        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    public void unlockContent() {

        HelperMethods.showFragment(getActivity(), new EnterCouponCodeFragment(),false);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.unlock_content_button_demo_content_fragment:
                unlockContent();
                break;

        }

    }

    public void addDemos() {

        for (int i = 0; i < demoData.size(); i++) {

            databaseHelper.addTopic(demoData.get(i));
            GlobalVariables.getInstance().setStreamsLoaded(true);

        }
    }

    public void settingAdapter() {

        // specify an adapter (see also next example)
        adapter = new DemoContentRecyclerAdapter(getActivity(), demoData, new DemoContentRecyclerAdapter.DemoContentCallback() {
            @Override
            public void selectedPositionUri(Uri videoUri) {

                Bundle bundle = new Bundle();
                bundle.putString("videoPath", videoUri.toString());
                bundle.putString("comingFrom","demo");

                Fragment fragment = new FullScreenVideoFragment();
                fragment.setArguments(bundle);
                HelperMethods.showFragment(getActivity(), fragment,false);

            }
        });
        recyclerView.setAdapter(adapter);



    }

    @Override
    public void onResponse(JSONObject response) {

        HelperMethods.dismissProgressDialog();
        try {

            demoData = new ArrayList<TopicData>();
            String status = response.getString("status");
            if (status.equalsIgnoreCase("200")) {

                JSONArray jsonArray = response.getJSONArray("content");
                String baseUrl = response.getString("content_path");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String title = jsonObject.getString("title");
                    String fileType = jsonObject.getString("file_type");
                    String fileName = jsonObject.getString("file_name");

                    String topicUrl = baseUrl+fileType+"/"+fileName;

                    demoData.add(new TopicData(id, title,"0","",topicUrl,"",1));

                }
                addDemos();
                settingAdapter();

            } else {

                Toast.makeText(getActivity(), "Something went wrong.Try again later.", Toast.LENGTH_SHORT).show();

            }


        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    @Override
    public void onErrorResponse(VolleyError error) {

        HelperMethods.dismissProgressDialog();
        Toast.makeText(getActivity(), "Something went wrong. Try again later. " + error, Toast.LENGTH_SHORT).show();

    }
}
