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

public class NotificationsRecyclerAdapter extends RecyclerView.Adapter<NotificationsRecyclerAdapter.ViewHolder>{

    private Context context;

    private List<NotificationData> notificationDatas;
    private TextView title,message,date;

    public NotificationsRecyclerAdapter(Context context, List<NotificationData> notificationDatas) {
        this.context = context;
        this.notificationDatas = notificationDatas;
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
                .inflate(R.layout.row_notifications, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void findViews(ViewHolder holder){

        title = (TextView) holder.v.findViewById(R.id.title_text_notifications_fragment);
        message = (TextView) holder.v.findViewById(R.id.message_text_notifications_fragment);
        date = (TextView) holder.v.findViewById(R.id.date_text_notifications_fragment);

    }

    public void setViews(int position){

        title.setText(notificationDatas.get(position).getHeading());
        message.setText(notificationDatas.get(position).getMessage());
        date.setText(notificationDatas.get(position).getDeliveredDate());

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


}
