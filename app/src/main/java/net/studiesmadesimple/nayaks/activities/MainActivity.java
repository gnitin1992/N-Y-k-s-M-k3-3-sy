package net.studiesmadesimple.nayaks.activities;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.studiesmadesimple.nayaks.R;

import net.studiesmadesimple.nayaks.fragments.ChaptersFragment;
import net.studiesmadesimple.nayaks.fragments.EnterMobileNumberFragment;
import net.studiesmadesimple.nayaks.fragments.EnterUniqueIdFragment;
import net.studiesmadesimple.nayaks.fragments.ExtendedSegmentsFragment;
import net.studiesmadesimple.nayaks.fragments.FullScreenVideoFragment;
import net.studiesmadesimple.nayaks.fragments.OpenFileFragment;
import net.studiesmadesimple.nayaks.fragments.RegistrationFragment;
import net.studiesmadesimple.nayaks.fragments.SegmentsFragment;
import net.studiesmadesimple.nayaks.fragments.SplashScreenFragment;
import net.studiesmadesimple.nayaks.fragments.StreamsFragment;
import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.HelperMethods;
import net.studiesmadesimple.nayaks.utils.Variables;

import static android.R.attr.dial;
import static android.R.attr.id;

public class MainActivity extends AppCompatActivity{

    private boolean isRegistered;
    private AlertDialog dialog;
    private int counter =0;
    public static int backCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(Constants.LOG_TAG,Constants.MAIN_ACTIVITY);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        HelperMethods.setScreenDimensions(getApplicationContext());

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);


        HelperMethods.showFragment(MainActivity.this,new SplashScreenFragment(),false);

    }

    public void showDialog(){


        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builder.setMessage("Do you want to exit");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                backCounter=0;
//                finish();
                System.exit(0);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                backCounter = 0;
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onBackPressed() {

        Fragment f = getSupportFragmentManager().findFragmentById(android.R.id.content);

        if(f instanceof SplashScreenFragment){

            showDialog();
        }
        else if(f instanceof EnterMobileNumberFragment){

            showDialog();

        }else if(f instanceof EnterUniqueIdFragment){

            showDialog();

        }else if(f instanceof RegistrationFragment){

            super.onBackPressed();

        }else if(f instanceof StreamsFragment){

            showDialog();
        }else if(f instanceof SegmentsFragment){

            showDialog();
        }else if(f instanceof ExtendedSegmentsFragment){

            super.onBackPressed();

        }else if(f instanceof ChaptersFragment){

            super.onBackPressed();
        }else if(f instanceof FullScreenVideoFragment){

            super.onBackPressed();
        }else if(f instanceof OpenFileFragment){

            super.onBackPressed();
        }


    }
}
