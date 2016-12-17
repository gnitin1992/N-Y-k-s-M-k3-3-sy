package net.studiesmadesimple.nayaks.fragments;

import android.content.Intent;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.adapters.CenterListRecyclerAdapter;
import net.studiesmadesimple.nayaks.utils.Constants;

import org.json.JSONObject;

/**
 * Created by studiesmadesimple on 10/16/2016.
 */

public class CenterListFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {

    private View v;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_center_list,container,false);

        Log.d(Constants.LOG_TAG,Constants.CENTER_LIST_FRAGMENT);

        findViews();
        measuringTheLayout();
        fetchingCenters();

        return v;
    }

    public void findViews(){

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_center_list_fragment);

    }

    public void measuringTheLayout(){

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        adapter = new CenterListRecyclerAdapter(getActivity(), new CenterListRecyclerAdapter.CenterListCallback() {
            @Override
            public void selectedPosition(String number) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel: "+number));//change the number
                startActivity(callIntent);

            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void fetchingCenters(){


    }


    @Override
    public void onResponse(JSONObject response) {


    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}
