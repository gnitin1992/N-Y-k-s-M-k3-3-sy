package net.studiesmadesimple.nayaks.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.data.NewSegmentsData;
import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

import java.util.List;

/**
 * Created by studiesmadesimple on 10/17/2016.
 */

public class SegmentsGridAdapter extends BaseAdapter {

    private Context context;
    private int[] screenDimensions;
    private int width,height;
    private int[] colors;
//    private int[] images={R.drawable.segment_icon_concept,R.drawable.segment_icon_pratice,R.drawable.segment_icon_assesment,
//    R.drawable.segment_icon_study_material,R.drawable.segment_icon_announcment,R.drawable.segment_icon_paper_writing,R.drawable.segment_icon_concept,R.drawable.segment_icon_pratice,R.drawable.segment_icon_assesment,
//            R.drawable.segment_icon_study_material};
    private List<NewSegmentsData> segmentsData;

    private int[] images = {
       R.drawable.segment_icon_announcment,R.drawable.segment_icon_concept ,R.drawable.segment_icon_paper_writing ,
             R.drawable.segment_icon_study_material,R.drawable.segment_icon_assesment,R.drawable.segment_icon_study_material,
             R.drawable.segment_icon_pratice, R.drawable.segment_icon_announcment};

    public SegmentsGridAdapter(Context context, List<NewSegmentsData> segmentsData) {
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
        this.segmentsData = segmentsData;
        Log.d(Constants.LOG_TAG,Constants.SEGMENTS_GRID_ADAPTER);
    }

    public int getCount() {
        return segmentsData.size();
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
            grid = inflater.inflate(R.layout.view_segments, null);
            ImageView segmentImage = (ImageView) grid.findViewById(R.id.segment_image_segments_fragment);
            TextView segmentName = (TextView) grid.findViewById(R.id.segment_name_text_segments_fragment);

            segmentName.setText(segmentsData.get(position).getSegmentName());

            segmentImage.setLayoutParams(new LinearLayout.LayoutParams(width,height));
            segmentImage.setBackgroundColor(colors[position%8]);
            segmentImage.setImageResource(images[position]);

        } else {
            grid = (View) convertView;
        }

        return grid;

    }


}
