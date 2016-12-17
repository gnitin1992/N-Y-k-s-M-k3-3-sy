package net.studiesmadesimple.nayaks.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.data.ChapterData;
import net.studiesmadesimple.nayaks.data.TopicData;
import net.studiesmadesimple.nayaks.utils.Constants;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sagar on 12/11/2016.
 */

public class ChapterExpandableListNew extends BaseExpandableListAdapter {

    private Context context;
    private List<ChapterData> chapterDatas;
    private List<TopicData> topicDatas;

    private HashMap<String,List<TopicData>> expandableData;

    public ChapterExpandableListNew(Context context,List<ChapterData> chapterDatas,HashMap<String,List<TopicData>> expandableData) {
        this.context = context;
        this.chapterDatas = chapterDatas;
        this.expandableData = expandableData;
//        this.topicDatas = topicDatas;
        Log.d(Constants.LOG_TAG,Constants.CHAPTERS_EXPANDABLE_LIST_ADAPTER);
    }

    @Override
    public int getGroupCount() {
        return expandableData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return expandableData.get(expandableData.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return expandableData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return expandableData.get(expandableData.get(groupPosition)).get(childPosition);


    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_headers, null);

            TextView chapterName = (TextView) convertView.findViewById(R.id.chapter_name_text_chapters_fragment);
            chapterName.setText(chapterDatas.get(groupPosition).getChapterName());
        }


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_child, null);

            TextView topicName = (TextView) convertView.findViewById(R.id.topic_name_text_chapters_fragment);
            topicName.setText(topicDatas.get(childPosition).getTopicName());
        }



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
