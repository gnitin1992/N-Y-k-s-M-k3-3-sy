package net.studiesmadesimple.nayaks.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.adapters.NotificationsRecyclerAdapter;
import net.studiesmadesimple.nayaks.data.NotificationData;
import net.studiesmadesimple.nayaks.database.MyDatabaseHelper;
import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

import java.util.List;

/**
 * Created by studiesmadesimple on 10/16/2016.
 */

public class NotificationsFragment extends Fragment {

    private View v;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isInternetAvailable = false;
    private Bundle bundle;
    private String segmentId, contentType;
    private MyDatabaseHelper databaseHelper;


    private List<NotificationData> notificationsData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check internet connection
        isInternetAvailable = HelperMethods.isInternetAvailable(getActivity());
        bundle = this.getArguments();
        segmentId = bundle.getString(Constants.SEGMENT_ID, "");
        contentType = bundle.getString(Constants.CONTENT_TYPE, "");
        databaseHelper = new MyDatabaseHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_notifications, container, false);

        Log.d(Constants.LOG_TAG, Constants.NOTIFICATIONS_FRAGMENT);

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        findViews();
        measuringTheLayout();

        fetchNotifications();

        return v;
    }

    public void findViews() {

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_notifications_fragment);

    }

    public void measuringTheLayout() {

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

    }

    public void setCustomAdapter() {

        adapter = new NotificationsRecyclerAdapter(getActivity(), notificationsData);
        recyclerView.setAdapter(adapter);

    }

    public void fetchNotifications() {

        databaseHelper = new MyDatabaseHelper(getActivity());

        int count  = databaseHelper.getAllNotificationsCount();

        notificationsData = databaseHelper.getAllNotifications();
        if (notificationsData.size() == 0) {

            Toast.makeText(getActivity(), "No notifications", Toast.LENGTH_SHORT).show();
        } else {

            setCustomAdapter();
        }
    }

}
