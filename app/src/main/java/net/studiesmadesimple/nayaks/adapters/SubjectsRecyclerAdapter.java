package net.studiesmadesimple.nayaks.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.data.AssessmentData;
import net.studiesmadesimple.nayaks.data.PDFData;
import net.studiesmadesimple.nayaks.data.SubjectData;
import net.studiesmadesimple.nayaks.utils.Constants;

import java.util.List;

/**
 * Created by studiesmadesimple on 10/17/2016.
 */

public class SubjectsRecyclerAdapter extends RecyclerView.Adapter<SubjectsRecyclerAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<SubjectData> subjectDatas;
    private TextView subject,online,offline;

    private SubjectsRecyclerAdapterCallback callback;
    private List<AssessmentData> assessmentDatas;
    private List<PDFData> pdfDatas;

    public interface SubjectsRecyclerAdapterCallback{

        public void onStreamSelected(String whichStream,int position);

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SubjectsRecyclerAdapter(Context context, List<SubjectData> subjectDatas,SubjectsRecyclerAdapterCallback callback) {
        this.context = context;
        this.subjectDatas = subjectDatas;
        this.callback = callback;

        Log.d(Constants.LOG_TAG,Constants.SUBJECTS_RECYCLER_ADAPTER);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public SubjectsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_subjects, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void findViews(ViewHolder holder){

        subject = (TextView)holder.view.findViewById(R.id.subject_text_subjects_fragment);
        online = (TextView)holder.view.findViewById(R.id.online_text_subjects_fragment);
        offline = (TextView)holder.view.findViewById(R.id.offline_text_subjects_fragment);
    }

    public void setViews(int position){


        subject.setText(subjectDatas.get(position).getSubjectName());

        online.setTag("online_"+position);
        online.setOnClickListener(this);

        offline.setTag("offline_"+position);
        offline.setOnClickListener(this);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position]);

        findViews(holder);
        setViews(position);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return subjectDatas.size();
    }


    public void watchOnline(int pos){


        callback.onStreamSelected("online",pos);

    }


    public void watchOffline(int pos){


        callback.onStreamSelected("offline",pos);
    }

    @Override
    public void onClick(View v) {

        String tagDetails[] = v.getTag().toString().split("_");
        int position = Integer.parseInt(tagDetails[1]);

        switch (v.getId()){

            case R.id.online_text_subjects_fragment: watchOnline(position);
                break;

            case R.id.offline_text_subjects_fragment: watchOffline(position);
                break;

        }


    }
}