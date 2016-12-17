package net.studiesmadesimple.nayaks.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.adapters.ExpandableListAdapter;
import net.studiesmadesimple.nayaks.data.ChapterData;
import net.studiesmadesimple.nayaks.data.TopicData;
import net.studiesmadesimple.nayaks.database.MyDatabaseHelper;
import net.studiesmadesimple.nayaks.utils.Constants;
import net.studiesmadesimple.nayaks.utils.GlobalVariables;
import net.studiesmadesimple.nayaks.utils.HelperMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by studiesmadesimple on 10/26/2016.
 */

public class ChaptersFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener,
        ExpandableListView.OnChildClickListener {
//        ExpandableListView.OnGroupClickListener,ExpandableListView.OnChildClickListener {

    private View v;
    private ExpandableListView expandableListView;

    private Bundle bundle;
    private String whichStream, subjectId;
    private boolean isInternetAvailable, isChaptersLoaded;
    private MyDatabaseHelper databaseHelper;
    private List<ChapterData> chapterDatas;
    private List<TopicData> topicDatas;
    private String offlineTopicDetails[] = new String[2];

    private String topicUrl;
    private List<String> chapterNames;
    private List<String> topicNames;
    private LinkedHashMap<String, List<TopicData>> chapterMap;
    private HashMap<String, List<String>> topicMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isInternetAvailable = HelperMethods.isInternetAvailable(getActivity());
        isChaptersLoaded = GlobalVariables.getInstance().isChaptersLoaded();
        databaseHelper = new MyDatabaseHelper(getActivity());

        bundle = this.getArguments();
        whichStream = bundle.getString("whichStream");
        subjectId = bundle.getString("subjectId");

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_chapters, container, false);

        Log.d(Constants.LOG_TAG, Constants.CHAPTERS_FRAGMENT);

        findViews();

        if (isChaptersLoaded) {

            fetchChaptersFromDatabase();
        } else {

            fetchChapter();

        }

        setViews();

        return v;
    }

    public void findViews() {

        expandableListView = (ExpandableListView) v.findViewById(R.id.expandable_list);

    }

    public void fetchChaptersFromDatabase() {

        chapterDatas = databaseHelper.getAllChapters();
        topicDatas = databaseHelper.getAllTopics();

    }

    public void addChapters() {

        for (int i = 0; i < chapterDatas.size(); i++) {

            databaseHelper.addChapter(chapterDatas.get(i));
            GlobalVariables.getInstance().setStreamsLoaded(true);

        }

    }


    public void setCustomAdapter() {

        expandableListView.setAdapter(new ExpandableListAdapter(getActivity().getApplicationContext(),
                chapterNames, topicMap));

    }

    public void fetchChapter() {

        HelperMethods.showProgressDialog(getActivity(), Constants.APP_NAME, "Loading...");

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("subject_id", subjectId);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.chaptersUrl, jsonObject, this, this);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(jsonObjectRequest);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void setViews() {

        expandableListView.setOnChildClickListener(this);
    }

    @Override
    public void onResponse(JSONObject response) {

        HelperMethods.dismissProgressDialog();
        try {

            chapterDatas = new ArrayList<ChapterData>();
            chapterNames = new ArrayList<String>();
            topicMap = new HashMap<String, List<String>>();
            chapterMap = new LinkedHashMap<String, List<TopicData>>();
            String responsCode = response.getString("status");
            if (responsCode.equalsIgnoreCase("200")) {


                topicUrl = response.getString("content_path");
                JSONArray jsonArray = response.getJSONArray("chapter");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    topicDatas = new ArrayList<TopicData>();
                    topicNames = new ArrayList<String>();

                    String chapterId = jsonObject.getString("id");
                    String chapterName = jsonObject.getString("name");

                    // this is the base url for topic
                    JSONArray jsonArray1 = jsonObject.getJSONArray("topic");

                    for (int j = 0; j < jsonArray1.length(); j++) {

                        JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                        String topicId = jsonObject1.getString("id");
                        String topicName = jsonObject1.getString("title");
                        String fileType = jsonObject1.getString("file_type");
                        String fileName = jsonObject1.getString("file_name");
                        String isFileDownloadable = jsonObject1.getString("downloadable");
                        String actionId = "0";
                        if (isFileDownloadable.equalsIgnoreCase("yes")) {

                            actionId = "1";
                        } else if (isFileDownloadable.equalsIgnoreCase("no")) {

                            actionId = "0";
                        }

                        String finalPath = topicUrl + fileType + "/" + fileName;

                        TopicData topicData = new TopicData(topicId, topicName, actionId, chapterId, finalPath, "", 0);
                        topicNames.add(topicName);
                        topicDatas.add(topicData);
                        databaseHelper.addTopic(topicData);

                    }
                    topicMap.put(chapterName, topicNames);
                    chapterNames.add(chapterName);
                    chapterDatas.add(new ChapterData(chapterId, chapterName, subjectId));
                    chapterMap.put(chapterName, topicDatas);

                }
                addChapters();
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



    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {


        Log.d(Constants.LOG_TAG, " I am here ");
        List<String> a = new ArrayList<String>();

        for (String key : chapterMap.keySet()) {

            Log.d(Constants.LOG_TAG, key);
            a.add(key);
        }

        String getKey = a.get(groupPosition);
        TopicData topicData = chapterMap.get(getKey).get(childPosition);

        if (whichStream.equalsIgnoreCase("online")) {

            Bundle bundle = new Bundle();
            bundle.putString("videoPath", topicData.getTopicUrl());
            bundle.putSerializable("topic", topicData);
            bundle.putString("comingFrom", "online");

            Fragment fragment = new FullScreenVideoFragment();
            fragment.setArguments(bundle);
            HelperMethods.showFragment(getActivity(), fragment,true);

        } else if (whichStream.equalsIgnoreCase("offline")) {

            topicData.getTopicId();
            offlineTopicDetails = new MyDatabaseHelper(getActivity()).fetchOfflinePath(topicData.getTopicId());

            if (offlineTopicDetails != null) {

                if (offlineTopicDetails[1].isEmpty()) {

                    Toast.makeText(getActivity(), "No offline Video", Toast.LENGTH_SHORT).show();
                } else {

                    topicData.setTopicId(offlineTopicDetails[0]);
                    topicData.setOfflinePath(offlineTopicDetails[1]);

                    Bundle bundle = new Bundle();
                    bundle.putString("videoPath", offlineTopicDetails[1]);
                    bundle.putSerializable("topic", topicData);
                    bundle.putString("comingFrom", "offline");

                    Fragment fragment = new FullScreenVideoFragment();
                    fragment.setArguments(bundle);
                    HelperMethods.showFragment(getActivity(), fragment,true);

                }


            }
            else{

                Toast.makeText(getActivity(),"Offline video not available", Toast.LENGTH_SHORT).show();
            }


        }

        return true;
    }


}
