package net.studiesmadesimple.nayaks.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.data.TopicData;
import net.studiesmadesimple.nayaks.utils.Constants;

import java.util.HashMap;
import java.util.List;

/**
 * Created by studiesmadesimple on 10/17/2016.
 */

public class DemoContentRecyclerAdapter extends RecyclerView.Adapter<DemoContentRecyclerAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private TextView address;
    private DemoContentCallback callback;

    private ImageView videoThumbnail;
    private TextView videoTitle;
    private List<TopicData> demoData;

    private Bitmap bm;

    private String videoTitles[] = {"Algebra Solving By Cramers Rule",
            "Biology Human Heart",
            "Geometry Trigonometry Question",
            "Physics Color Sky Appr Sun"
    };

    private ImageLoader imageLoader = ImageLoader.getInstance();

    public interface DemoContentCallback {

        public void selectedPositionUri(Uri videoUri);
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
    public DemoContentRecyclerAdapter(Context context, List<TopicData> demoData, DemoContentCallback callback) {
        this.context = context;
        this.demoData = demoData;
        this.callback = callback;

        Log.d(Constants.LOG_TAG, Constants.DEMO_CONTENT_RECYCLER_ADAPTER);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public DemoContentRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_demo_content, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void findViews(ViewHolder holder) {

        videoThumbnail = (ImageView) holder.view.findViewById(R.id.thumbnail_image_demo_content_fragment);
        videoTitle = (TextView) holder.view.findViewById(R.id.video_name_text_demo_content_fragment);

    }

    public void setViews(int position) {

//        imageLoader.displayImage(imageUri, imageView);

        Bitmap curThumb = retriveVideoFrameFromVideo(demoData.get(position).getTopicUrl());
        videoThumbnail.setImageBitmap(curThumb);

        videoTitle.setTag("video_"+position);
        videoTitle.setText(demoData.get(position).getTopicName());
        videoTitle.setOnClickListener(this);


    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        findViews(holder);
        setViews(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return demoData.size();
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoURL) {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoURL, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoURL);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    @Override
    public void onClick(View v) {

        String tagDetails[] = v.getTag().toString().split("_");
        int position = Integer.parseInt(tagDetails[1]);

        Uri videoURI = Uri.parse(demoData.get(position).getTopicUrl());
        callback.selectedPositionUri(videoURI);
    }

}