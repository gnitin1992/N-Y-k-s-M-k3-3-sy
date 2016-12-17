package net.studiesmadesimple.nayaks.fragments;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.custom.CustomMediaController;
import net.studiesmadesimple.nayaks.data.TopicData;
import net.studiesmadesimple.nayaks.database.MyDatabaseHelper;
import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

import java.io.File;

import static android.content.Context.DOWNLOAD_SERVICE;
import static net.studiesmadesimple.nayaks.R.id.videoView;

/**
 * Created by studiesmadesimple on 12/2/2016.
 */

public class FullScreenVideoFragment extends Fragment implements MediaPlayer.OnPreparedListener {

    private Uri uri;
    private String comingFrom, topicName, topicId;
    private VideoView vd;
    private String filePath;

    private DownloadManager downloadManager;
    private long downloadId;

    private CustomMediaController mediaController;
    private MediaController mediaController1;
    private MyDatabaseHelper databaseHelper;
    private TopicData topic;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        comingFrom = bundle.getString("comingFrom", "");
        uri = Uri.parse(bundle.getString("videoPath"));
        topic = (TopicData) bundle.getSerializable("topic");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_full_screen_video, container, false);

        Log.d(Constants.LOG_TAG, Constants.FULL_SCREEN_VIDEO_FRAGMENT);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        vd = (VideoView) v.findViewById(videoView);

        HelperMethods.showProgressDialog(getActivity(), Constants.APP_NAME, "Loading");

        if (comingFrom.equalsIgnoreCase("demo")|| comingFrom.equalsIgnoreCase("offline")) {

            mediaController1 = new MediaController(getActivity());
            vd.setMediaController(mediaController1);
        } else {

            mediaController = new CustomMediaController(getActivity(), new CustomMediaController.CustomCallback() {
                @Override
                public void clicked(boolean isClicked) {

                    if (isClicked) {

                        Toast.makeText(getActivity(), "Downloading...", Toast.LENGTH_SHORT).show();
                        downloadVideo();
                    }

                }
            });


            vd.setMediaController(mediaController);
        }


        vd.setVideoURI(uri);
        vd.start();
        vd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getActivity(), "Video completed", Toast.LENGTH_LONG).show();
            }
        });

        vd.requestFocus();
        vd.setOnPreparedListener(this);


        return v;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        HelperMethods.dismissProgressDialog();
        vd.start();
    }

    public long downloadVideo() {

        long downloadReference;

        Uri music_uri = uri;

        // Create request for android download manager
        downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        DownloadManager.Request request = new DownloadManager.Request(music_uri);
        request.setVisibleInDownloadsUi(false);

        String topicName = topic.getTopicName();
        if (!topicName.isEmpty()) {

            //Setting title of request
            request.setTitle(topicName);
            //Setting description of request
            request.setDescription("Downloading Video");
            File file = new File(getActivity().getExternalFilesDir(
                    Environment.DIRECTORY_MOVIES), topicName);
            filePath = file.getAbsolutePath();
            request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_MOVIES,topicName);

        }

        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);

        downloadId = downloadReference;

        return downloadReference;

    }


    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            // your code

            Toast.makeText(ctxt,"Download complete", Toast.LENGTH_SHORT).show();
            databaseHelper = new MyDatabaseHelper(ctxt);
            databaseHelper.addOfflineVideo(topic.getTopicId(),filePath);
        }
    };


}
