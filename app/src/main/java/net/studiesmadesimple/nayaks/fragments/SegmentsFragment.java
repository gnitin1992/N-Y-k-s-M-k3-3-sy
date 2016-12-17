package net.studiesmadesimple.nayaks.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.adapters.SegmentsGridAdapter;
import net.studiesmadesimple.nayaks.data.NewSegmentsData;
import net.studiesmadesimple.nayaks.database.AndroidDatabaseManager;
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

public class SegmentsFragment extends Fragment implements AdapterView.OnItemClickListener,Response.Listener<JSONObject>,Response.ErrorListener{

    private View v;
    private GridView gridView;

    private boolean isInternetAvailable,isSegmentsLoaded;
    private MyDatabaseHelper databaseHelper;
    private List<NewSegmentsData> segmentsData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isInternetAvailable = HelperMethods.isInternetAvailable(getActivity());
        isSegmentsLoaded = GlobalVariables.getInstance().isSegmentsLoaded();
        databaseHelper = new MyDatabaseHelper(getActivity());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_segments,container,false);

        Log.d(Constants.LOG_TAG,Constants.SEGMENTS_FRAGMENT);

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        findViews();
        if (isSegmentsLoaded){

            fetchSegmentsFromDatabase();

        }
        else{

            fetchSegments();
        }

        setViews();

        return v;
    }

    public void fetchSegmentsFromDatabase(){

        segmentsData = databaseHelper.getAllSegments();
        setAdapters();
    }

    public void findViews(){

        gridView = (GridView) v.findViewById(R.id.grid_view_segments_fragment);
//        Button button1 = (Button) v.findViewById(R.id.button_segments_fragment);
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getActivity(), AndroidDatabaseManager.class);
//                startActivity(i);
//            }
//        });
    }

    public void addSegments(List<NewSegmentsData> segmentsData) {

        for (int i = 0; i < segmentsData.size(); i++) {

            databaseHelper.addSegment(segmentsData.get(i));
            GlobalVariables.getInstance().setSegmentsLoaded(true);

        }

    }

    public void fetchSegments() {

        HelperMethods.showProgressDialog(getActivity(),Constants.APP_NAME,"Loading...");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.segmentUrl, null,this,this);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);

    }

    public void setAdapters(){


        if(segmentsData!= null){

            gridView.setAdapter(new SegmentsGridAdapter(getActivity(),segmentsData));
        }
        else{

            Toast.makeText(getActivity(),"Try again later.Something went wrong.", Toast.LENGTH_SHORT).show();
        }


    }

    public void setViews(){

        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.SEGMENT_ID,segmentsData.get(i).getSegmentId());
        bundle.putString(Constants.CONTENT_TYPE,segmentsData.get(i).getContentType());

        Fragment fragment = new ExtendedSegmentsFragment();
        fragment.setArguments(bundle);
        HelperMethods.showFragment(getActivity(),fragment,true);
    }

    @Override
    public void onResponse(JSONObject response) {


        segmentsData = new ArrayList<NewSegmentsData>();

        HelperMethods.dismissProgressDialog();
        try {

            String responsCode = response.getString("status_code");
            if (responsCode.equalsIgnoreCase("200")) {

                JSONArray jsonArray = response.getJSONArray("segments");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String segmentId = jsonObject.getString("segment_id");
                    String segmentName = jsonObject.getString("segment_name");
                    String contentType = jsonObject.getString("content_type");

                    segmentsData.add(new NewSegmentsData(segmentId, segmentName, contentType));
                }
                addSegments(segmentsData);

            } else {

                Toast.makeText(getActivity(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();

            }

            fetchSegmentsFromDatabase();

        } catch (Exception e) {

            Toast.makeText(getActivity(), "Something went wrong.Try again later.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        HelperMethods.dismissProgressDialog();
        Toast.makeText(getActivity(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();

        fetchSegmentsFromDatabase();
    }

}
