package net.studiesmadesimple.nayaks.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

/**
 * Created by sagar on 12/10/2016.
 */

public class OpenFileFragment extends Fragment {

    private View v;
    private Bundle bundle;
    private String contentType, url;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        contentType = bundle.getString(Constants.CONTENT_TYPE, "");
        if (contentType.equalsIgnoreCase("pdf")) {

            url = "http://docs.google.com/gview?embedded=true&url=" + bundle.getString(Constants.URL, "");
        } else {

            url = bundle.getString(Constants.URL, "");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.row_open_file, container, false);

        WebView mWebView = (WebView) v.findViewById(R.id.web);
        mWebView.setWebViewClient(new MyBrowser());
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.loadUrl(url);


        return v;

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            HelperMethods.showProgressDialog(getActivity(), Constants.APP_NAME, "Loading");
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            HelperMethods.dismissProgressDialog();
        }
    }
}
