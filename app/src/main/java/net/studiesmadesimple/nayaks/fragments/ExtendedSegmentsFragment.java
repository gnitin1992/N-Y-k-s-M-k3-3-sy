package net.studiesmadesimple.nayaks.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.adapters.AssessmentRecyclerAdapter;
import net.studiesmadesimple.nayaks.adapters.PdfRecyclerAdapter;
import net.studiesmadesimple.nayaks.adapters.SubjectsRecyclerAdapter;
import net.studiesmadesimple.nayaks.data.AssessmentData;
import net.studiesmadesimple.nayaks.data.PDFData;
import net.studiesmadesimple.nayaks.data.SubjectData;
import net.studiesmadesimple.nayaks.database.MyDatabaseHelper;
import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.GlobalVariables;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by studiesmadesimple on 10/16/2016.
 */

public class ExtendedSegmentsFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    private View v;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isInternetAvailable = false;
    private Bundle bundle;
    private String segmentId, contentType;
    private MyDatabaseHelper databaseHelper;


    private List<SubjectData> subjectsDatas;
    private List<PDFData> pdfDatas;
    private List<AssessmentData> assessmentDatas;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check internet connection
        isInternetAvailable = HelperMethods.isInternetAvailable(getActivity());
        bundle = this.getArguments();
        segmentId = bundle.getString(Constants.SEGMENT_ID, "");
        contentType = bundle.getString(Constants.CONTENT_TYPE, "");
        databaseHelper = new MyDatabaseHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_subjects, container, false);

        Log.d(Constants.LOG_TAG, Constants.SUBJECTS_FRAGMENT);

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        findViews();
        measuringTheLayout();

        fetchSubjects();

        return v;
    }

    public void findViews() {

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_subjects_fragment);

    }

    public void measuringTheLayout() {

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

    }


    public void fetchSubjects() {

        try {

            if (!segmentId.equalsIgnoreCase("000")) {


                HelperMethods.showProgressDialog(getActivity(), Constants.APP_NAME, "Loading...");

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
                String streamId = sharedPreferences.getString(Constants.STREAM_ID, "");
                String uniqueId = sharedPreferences.getString(Constants.UNIQUE_ID, "");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("segment_id", segmentId);
                jsonObject.put("stream_id", streamId);
                jsonObject.put("unique_id", uniqueId);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.segmentSelectionUrl, jsonObject,
                        this, this);
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(jsonObjectRequest);
            }


        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    public void addSubjects() {

        for (int i = 0; i < subjectsDatas.size(); i++) {

            databaseHelper.addSubject(subjectsDatas.get(i));
            GlobalVariables.getInstance().setSubjectsLoaded(true);

        }
    }

    public void addPDFs() {

        for (int i = 0; i < pdfDatas.size(); i++) {

            databaseHelper.addPDF(pdfDatas.get(i));
            GlobalVariables.getInstance().setPDFsLoaded(true);

        }

    }

    public void addTests() {


        for (int i = 0; i < assessmentDatas.size(); i++) {

            databaseHelper.addAssessment(assessmentDatas.get(i));
            GlobalVariables.getInstance().setAssessmentsLoaded(true);

        }

    }

    public void setCustomAdapter() {

//        if (contentType.equalsIgnoreCase("video")) {

            // specify an adapter (see also next example)
            adapter = new SubjectsRecyclerAdapter(getActivity(), subjectsDatas, new SubjectsRecyclerAdapter.SubjectsRecyclerAdapterCallback() {
                @Override
                public void onStreamSelected(String whichStream, int position) {

                    Bundle bundle = new Bundle();
                    bundle.putString("subjectId", subjectsDatas.get(position).getSubjectId());
                    bundle.putString("segmentId",segmentId);
                    bundle.putString("contentType",contentType);
                    bundle.putString("whichStream", whichStream);

                    if (whichStream.equalsIgnoreCase("online") && (isInternetAvailable == false)) {

                        Toast.makeText(getActivity(), "Please check the internet connection.", Toast.LENGTH_SHORT).show();

                    }
                    else if (whichStream.equalsIgnoreCase("offline") && ((contentType.equalsIgnoreCase("pdf"))||(contentType.equalsIgnoreCase("test")))){

                        Toast.makeText(getActivity(),"This content is not available offline", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Fragment fragment = new ChaptersFragment();
                        fragment.setArguments(bundle);
                        HelperMethods.showFragment(getActivity(), fragment, true);

                    }

                }
            });

//        } else if (contentType.equalsIgnoreCase("pdf")) {
//
//            // specify an adapter (see also next example)
//            adapter = new PdfRecyclerAdapter(getActivity(), pdfDatas, new PdfRecyclerAdapter.PdfCallback() {
//                @Override
//                public void position(int position) {
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants.CONTENT_TYPE, "pdf");
//                    bundle.putString(Constants.URL, pdfDatas.get(position).getUrl());
//
//                    Fragment fragment = new OpenFileFragment();
//                    fragment.setArguments(bundle);
//
//                    HelperMethods.showFragment(getActivity(), fragment, true);
//                }
//            });
//
//        } else if (contentType.equalsIgnoreCase("test")) {
//
//            // specify an adapter (see also next example)
//            adapter = new AssessmentRecyclerAdapter(getActivity(), assessmentDatas, new AssessmentRecyclerAdapter.AssessmentCallback() {
//                @Override
//                public void position(int position) {
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants.CONTENT_TYPE, "test");
//                    bundle.putString(Constants.URL, assessmentDatas.get(position).getUrl());
//
//                    Fragment fragment = new OpenFileFragment();
//                    fragment.setArguments(bundle);
//
//                    HelperMethods.showFragment(getActivity(), fragment, true);
//
//                }
//            });
//
//        }


        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResponse(JSONObject response) {

        HelperMethods.dismissProgressDialog();
        subjectsDatas = new ArrayList<SubjectData>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        String streamId = sharedPreferences.getString(Constants.STREAM_ID, "");

        try {

            String responsCode = response.getString("status");
            if (responsCode.equalsIgnoreCase("200")) {

//                if (contentType.equalsIgnoreCase("video")) {

                    JSONArray jsonArray = response.getJSONArray("subject");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String subjectId = jsonObject.getString("id");
                        String subjectName = jsonObject.getString("name");

                        subjectsDatas.add(new SubjectData(subjectId, subjectName, streamId, segmentId));
                    }
                    addSubjects();
//                }

//                } else if (contentType.equalsIgnoreCase("pdf")) {
//
//                    pdfDatas = new ArrayList<PDFData>();
//                    String baseUrl = response.getString("content_path");
//                    JSONArray jsonArray = response.getJSONArray("pdf");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        String pdfId = jsonObject.getString("id");
//                        String pdfName = jsonObject.getString("title");
//                        String pdfFileName = jsonObject.getString("file_name");
//
//                        String url = baseUrl + contentType + "/" + pdfFileName;
//
//                        pdfDatas.add(new PDFData(pdfId, segmentId, pdfName, url));
//                    }
//                    addPDFs();
//
//                } else if (contentType.equalsIgnoreCase("test")) {
//
//                    assessmentDatas = new ArrayList<AssessmentData>();
//                    JSONArray jsonArray = response.getJSONArray("test");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        String testId = jsonObject.getString("id");
//                        String testName = jsonObject.getString("title");
//                        String testUrl = jsonObject.getString("link");
//
//                        assessmentDatas.add(new AssessmentData(testId, "", testName, testUrl));
//                    }
//                    addTests();
//
//                }

                setCustomAdapter();

            } else {

                Toast.makeText(getActivity(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();

            }


        } catch (Exception e) {

            Toast.makeText(getActivity(), "Something went wrong.Try again later.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        HelperMethods.dismissProgressDialog();
        Toast.makeText(getActivity(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();

    }
}
