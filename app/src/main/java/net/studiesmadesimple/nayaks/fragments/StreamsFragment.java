package net.studiesmadesimple.nayaks.fragments;

import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.adapters.StreamsGridAdapter;
import net.studiesmadesimple.nayaks.data.NewStreamData;
import net.studiesmadesimple.nayaks.database.MyDatabaseHelper;
import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.GlobalVariables;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static net.studiesmadesimple.nayaks.utils.Constants.streamUrl;

/**
 * Created by studiesmadesimple on 10/16/2016.
 */

public class StreamsFragment extends Fragment implements AdapterView.OnItemClickListener{

    private View v;
    private GridView gridView;

    private ProgressDialog progressDialog;
    private boolean isInternetAvailable,isStreamsLoaded;
    private MyDatabaseHelper databaseHelper;
    private List<NewStreamData> streamsData;
    private String enabledStreamId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isInternetAvailable = HelperMethods.isInternetAvailable(getActivity());
        isStreamsLoaded = GlobalVariables.getInstance().isStreamsLoaded();
        databaseHelper = new MyDatabaseHelper(getActivity());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME,Context.MODE_PRIVATE);
        enabledStreamId = sharedPreferences.getString(Constants.STREAM_ID,"");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_streams, container, false);

        Log.d(Constants.LOG_TAG, Constants.STREAMS_FRAGMENT);

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        findViews();

        fetchStreamsFromDatabase();

//        if (isStreamsLoaded){
//
//            fetchStreamsFromDatabase();
//
//        }
//        else{
//
//            fetchStreams();
//        }
        setAdapters();
        setViews();

        return v;
    }

    public void findViews() {

        gridView = (GridView) v.findViewById(R.id.grid_view_streams_fragment);

    }

    public void fetchStreamsFromDatabase(){

        streamsData = databaseHelper.getAllStreams();
    }


    public void setAdapters() {

        gridView.setAdapter(new StreamsGridAdapter(getActivity(),streamsData,enabledStreamId));

    }

    public void setViews() {

        gridView.setOnItemClickListener(this);
    }

    public void addStreams() {

        for (int i = 0; i < streamsData.size(); i++) {

            databaseHelper.addStream(streamsData.get(i));
            GlobalVariables.getInstance().setStreamsLoaded(true);

        }

    }

    public void fetchStreams() {

        HelperMethods.showProgressDialog(getActivity(),Constants.APP_NAME,"Loading...");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.streamUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(Constants.LOG_TAG," the reponse is here ");

                HelperMethods.dismissProgressDialog();
                try {

                    streamsData = new ArrayList<NewStreamData>();
                    String responsCode = response.getString("status_code");
                    if (responsCode.equalsIgnoreCase("200")) {

                        JSONArray jsonArray = response.getJSONArray("streams");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String streamId = jsonObject.getString("stream_id");
                            String streamName = jsonObject.getString("stream_name");

                            streamsData.add(new NewStreamData(streamId, streamName));
                        }
                        addStreams();

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

                Log.d(Constants.LOG_TAG," the error is received ");

                HelperMethods.dismissProgressDialog();
                Toast.makeText(getActivity(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (enabledStreamId.equalsIgnoreCase(streamsData.get(position).getStreamId())){

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.STREAM_ID,streamsData.get(position).getStreamId());
            editor.commit();

//            HelperMethods.showFragment(getActivity(), new DemoContentFragment());
            HelperMethods.showFragment(getActivity(), new SegmentsFragment(),false);

        }



    }

}
