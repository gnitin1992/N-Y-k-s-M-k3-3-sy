package net.studiesmadesimple.nayaks.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.utils.Constants;

/**
 * Created by studiesmadesimple on 10/17/2016.
 */

public class CenterListRecyclerAdapter extends RecyclerView.Adapter<CenterListRecyclerAdapter.ViewHolder> implements View.OnClickListener{

    private Context context;
    private TextView address;
    private CenterListCallback callback;


    public interface CenterListCallback{

        public void selectedPosition(String phoneNumber);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CenterListRecyclerAdapter(Context context,CenterListCallback callback) {
        this.context = context;
        this.callback = callback;

        Log.d(Constants.LOG_TAG,Constants.CENTER_LIST_RECYCLER_ADAPTER);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public CenterListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_center_list, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void findViews(ViewHolder holder){

        address = (TextView)holder.view.findViewById(R.id.address_text_center_list_fragment);

    }

    public void setViews(int position){

        address.setText("Address "+position);
        address.setTag("address_"+position);
        address.setOnClickListener(this);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position]);

        findViews(holder);
        setViews(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public void onClick(View v) {

        String tagDetails[] = v.getTag().toString().split("_");
        int position = Integer.parseInt(tagDetails[1]);
        callback.selectedPosition("932983293");
    }

}