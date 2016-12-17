package net.studiesmadesimple.nayaks.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.data.PDFData;
import net.studiesmadesimple.nayaks.utils.Constants;

import java.util.List;

/**
 * Created by sagar on 12/11/2016.
 */

public class PdfRecyclerAdapter extends RecyclerView.Adapter<PdfRecyclerAdapter.ViewHolder> implements View.OnClickListener{

    private Context context;
    private List<PDFData> pdfDatas;

    private TextView a;

    private PdfCallback callback;

    public PdfRecyclerAdapter(Context context, List<PDFData> pdfDatas, PdfCallback callback) {
        this.context = context;
        this.pdfDatas = pdfDatas;
        this.callback = callback;
        Log.d(Constants.LOG_TAG,Constants.PDF_RECYCLER_ADAPTER);
    }

    public interface PdfCallback {

        public void position(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        View v;
        public ViewHolder(View v) {
            super(v);
            this.v = v;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_test_video_notifications, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    public void findViews(ViewHolder holder) {

        a = (TextView) holder.v.findViewById(R.id.a);
    }

    public void setViews(int position) {

        a.setText(pdfDatas.get(position).getTitle());
        a.setTag("online_" + position);
        a.setOnClickListener(this);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        findViews(holder);
        setViews(position);


    }

    @Override
    public int getItemCount() {
        return pdfDatas.size();
    }


    @Override
    public void onClick(View v) {

        String tagDetails[] = v.getTag().toString().split("_");
        int position = Integer.parseInt(tagDetails[1]);

        callback.position(position);


    }

}
