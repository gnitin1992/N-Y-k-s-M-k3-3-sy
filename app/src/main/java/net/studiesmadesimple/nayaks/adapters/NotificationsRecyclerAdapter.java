package net.studiesmadesimple.nayaks.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.data.AssessmentData;
import net.studiesmadesimple.nayaks.data.NotificationData;
import net.studiesmadesimple.nayaks.utils.Constants;

import java.util.List;

/**
 * Created by sagar on 12/11/2016.
 */

public class NotificationsRecyclerAdapter extends RecyclerView.Adapter<NotificationsRecyclerAdapter.ViewHolder> implements View.OnClickListener{

    private Context context;

    private List<NotificationData> notificationDatas;
    private TextView a;

    private NotificationsCallback callback;


    public interface NotificationsCallback {

        public void position(int position);
    }

    public NotificationsRecyclerAdapter(Context context, List<NotificationData> notificationDatas, NotificationsCallback callback) {
        this.context = context;
        this.notificationDatas = notificationDatas;
        this.callback = callback;
        Log.d(Constants.LOG_TAG,Constants.NOTIFICATIONS_RECYCLER_ADAPTER);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View v;
        public ViewHolder(View v) {
            super(v);

            this.v = v;
        }
    }

    @Override
    public NotificationsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_test_video_notifications, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void findViews(ViewHolder holder){

        a = (TextView) holder.v.findViewById(R.id.a);

    }

    public void setViews(int position){

        a.setText(notificationDatas.get(position).getMessage());
        a.setOnClickListener(this);

    }

    @Override
    public void onBindViewHolder(NotificationsRecyclerAdapter.ViewHolder holder, int position) {

        findViews(holder);
        setViews(position);

    }

    @Override
    public int getItemCount() {
        return notificationDatas.size();
    }

    @Override
    public void onClick(View view) {

        String tagDetails[] = view.getTag().toString().split("_");
        int position = Integer.parseInt(tagDetails[1]);

        callback.position(position);

    }


}
