package net.studiesmadesimple.nayaks.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

import java.io.File;

/**
 * Created by studiesmadesimple on 10/30/2016.
 */

public class OfflineStreamContentFragment extends Fragment implements MediaPlayer.OnPreparedListener {

    private View v;
    private VideoView videoView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_offline_stream, container, false);

        findViews();

        HelperMethods.showProgressDialog(getActivity(), "HI", "hello");

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    getActivity());
            mediacontroller.setAnchorView(videoView);

//            File file = new File(HelperMethods.getAppVideoStorageDir(getActivity(),""), "sati.mp4");
//            Log.d(Constants.APP_NAME, " the path is " + file.getAbsolutePath());
//
//            String base_url = "file:///storage/emulated/0/Android/data/net.studiesmadesimple.nayaks/files/";
//
//            String url = base_url.concat("storage/emulated/0/Android/data/net.studiesmadesimple.nayaks/files/Movies/Nayaks/ratnesh.mp4");

            String url = HelperMethods.getAppVideoStorageDir(getActivity(),"SAKSHI").getAbsolutePath();

            videoView.setMediaController(mediacontroller);
            videoView.setVideoPath(url);
            videoView.start();

        } catch (Exception e) {
            Log.e("Error", e.getMessage());

            Toast.makeText(getActivity(), " the message is " + e.getMessage(), Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(this);

        return v;
    }

    public void findViews() {


        videoView = (VideoView) v.findViewById(R.id.video_view);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        HelperMethods.dismissProgressDialog();
        videoView.start();
    }
}



