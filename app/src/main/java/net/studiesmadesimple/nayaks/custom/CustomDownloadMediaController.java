package net.studiesmadesimple.nayaks.custom;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by sagar on 12/11/2016.
 */

public class CustomDownloadMediaController extends MediaController {

    private Context context;
    private ImageButton customButton;
    private DownloadManager downloadManager;

    private CustomCallback customCallback;

    public interface CustomCallback{

        public void clicked(boolean isClicked);
    }

    public CustomDownloadMediaController(Context context, CustomCallback customCallback) {
        super(context);
        this.context = context;
        this.customCallback = customCallback;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        frameParams.gravity = Gravity.RIGHT | Gravity.TOP;

        View v = makeCCView();
        addView(v, frameParams);

    }


    private View makeCCView() {

        customButton = new ImageButton(context);
        customButton.setImageResource(R.drawable.download);

        customButton.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {


                customCallback.clicked(true);


            }
        });

        return customButton;
    }
}
