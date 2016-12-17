package net.studiesmadesimple.nayaks.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.data.NewStreamData;
import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

import java.util.List;

/**
 * Created by studiesmadesimple on 10/17/2016.
 */

public class StreamsGridAdapter extends BaseAdapter {

    private Context context;
    private int[] screenDimensions;
    private int width,height;
    private int[] colors;
    private List<NewStreamData> streamsData;
    private String enabledStreamId;

    public StreamsGridAdapter(Context context,List<NewStreamData> streamsData,String enabledStreamId) {
        this.context = context;
        screenDimensions = HelperMethods.getScreenDimensions(context);
        width = screenDimensions[1]/3;
        height = width;
        TypedArray ta = context.getResources().obtainTypedArray(R.array.subjectColors);
        colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
        ta.recycle();
        this.streamsData = streamsData;
        this.enabledStreamId = enabledStreamId;
        Log.d(Constants.LOG_TAG,Constants.STREAMS_GRID_ADAPTER);
    }

    public int getCount() {
        return streamsData.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(context);
            grid = inflater.inflate(R.layout.view_streams, null);
            TextView textView = (TextView) grid.findViewById(R.id.stream_text_streams_framgent);
            ImageView lock = (ImageView) grid.findViewById(R.id.lock_image_streams_fragment);
            textView.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
            textView.setBackgroundColor(colors[position%8]);
            textView.setText(streamsData.get(position).getStreamName());

            if(streamsData.get(position).getStreamId().equalsIgnoreCase(enabledStreamId)){

                lock.setVisibility(View.GONE);
            }
            else{

                lock.setVisibility(View.VISIBLE);
            }


        } else {
            grid = (View) convertView;
        }

        return grid;

    }

}