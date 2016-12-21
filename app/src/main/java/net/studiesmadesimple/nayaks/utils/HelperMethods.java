package net.studiesmadesimple.nayaks.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import net.studiesmadesimple.nayaks.activities.MainActivity;

import java.io.File;
import java.util.HashMap;

/**
 * Created by studiesmadesimple on 10/16/2016.
 */

public class HelperMethods {

    private static ProgressDialog pDialog;

    public static void setSharedPreferenceVariable(){



    }

    public static String getDeviceDetails(Context context){

        String deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        return deviceId;
    }

    public static boolean isRegistered(Context context) {


        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constants.USER_REGISTERED, false);

    }

    public static void showFragment(Activity activity, Fragment fragment,boolean addToBackStack){

        // we are setting this because wheneve a fragment is changed we want the use to press twice to exit.
        MainActivity.backCounter = 0;

        FragmentManager fragmentManager = ((MainActivity)activity).getSupportFragmentManager();
        if (addToBackStack){

            fragmentManager.beginTransaction()
                    .replace(android.R.id.content,fragment)
                    .addToBackStack(fragment.getClass().getName())
                    .commit();

        }
        else{

            fragmentManager.beginTransaction()
                    .replace(android.R.id.content,fragment)
                    .commit();

        }


    }

    public static void setScreenDimensions(Context context){

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.APP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Variables.height,height);
        editor.putInt(Variables.width,width);
        editor.commit();

    }

    public static int[] getScreenDimensions(Context context){

        int[] dimensions = new int[2];

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.APP_NAME,Context.MODE_PRIVATE);
        dimensions[0] = sharedPreferences.getInt(Variables.height,0);
        dimensions[1] = sharedPreferences.getInt(Variables.width,0);

        return dimensions;
    }

    public static void showProgressDialog(Context context,String title,String message){

        // Create a progressbar
        pDialog = new ProgressDialog(context);
        // Set progressbar title
        pDialog.setTitle(title);
        // Set progressbar message
        pDialog.setMessage(message);
//        pDialog.setIndeterminate(false);
//        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

    }
    public static void dismissProgressDialog(){

        if (pDialog.isShowing()){

            pDialog.dismiss();
        }

    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    public static File getAppVideoStorageDir(Context context, String albumName) {
        // Get the directory for the app's private videos directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_MOVIES), albumName);

        Log.d("sagar",Environment.getExternalStorageDirectory().getAbsolutePath());
//                Environment.DIRECTORY_MOVIES), albumName);
        if (!file.mkdirs()) {
            Log.d(Constants.LOG_TAG, "Directory not created");


        }
        return file;
    }

    public static boolean isInternetAvailable(Context context){

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
