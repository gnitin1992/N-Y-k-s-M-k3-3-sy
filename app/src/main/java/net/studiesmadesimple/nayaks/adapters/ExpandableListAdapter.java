package net.studiesmadesimple.nayaks.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import net.studiesmadesimple.nayaks.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sagar on 12/11/2016.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> chaptersName; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> topicsData;

    public ExpandableListAdapter(Context context, List<String> chaptersName,
                                 HashMap<String, List<String>> topicsData) {
        this.context = context;
        this.chaptersName = chaptersName;
        this.topicsData = topicsData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.topicsData.get(this.chaptersName.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_child, null);
        }

        TextView topicName = (TextView) convertView.findViewById(R.id.topic_name_text_chapters_fragment);
        topicName.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.topicsData.get(this.chaptersName.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.chaptersName.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.chaptersName.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_headers, null);
        }

        TextView chapterName = (TextView) convertView.findViewById(R.id.chapter_name_text_chapters_fragment);
        chapterName.setTypeface(null, Typeface.BOLD);
        chapterName.setText(chaptersName.get(groupPosition));


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
