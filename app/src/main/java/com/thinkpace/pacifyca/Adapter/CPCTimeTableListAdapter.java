package com.thinkpace.pacifyca.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.entity.timetable.CPCTimeTableItem;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sapna on 19-03-2017.
 */

public class CPCTimeTableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<CPCTimeTableItem> mListItems;
    int mGridUnit;

    public CPCTimeTableListAdapter(Context context, ArrayList<CPCTimeTableItem> mArrayList) {
        this.mContext = context;
        this.mListItems = mArrayList;
        mGridUnit = CPCAppCommonUtility.getScreenGridUnitBy16(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_table_content_layout, parent, false);
        return new TimeTableItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TimeTableItemViewHolder mItemViewHolder = (TimeTableItemViewHolder) holder;
        final CPCTimeTableItem timeTableItem = mListItems.get(position);
        if (timeTableItem != null) {
            mItemViewHolder.mSubName.setText(timeTableItem.getSubName());
            mItemViewHolder.mTimeText.setText(timeTableItem.getFrom_time() + " - " + timeTableItem.getEnd_time());
        }

    }

    @Override
    public int getItemCount() {
        if (mListItems != null)
            return mListItems.size();
        else
            return 0;
    }


    public void updateList(ArrayList<CPCTimeTableItem> assignmentList) {
        this.mListItems = assignmentList;
        notifyDataSetChanged();
    }

    class TimeTableItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.subject_name)
        TextView mSubName;

        @BindView(R.id.time_text)
        TextView mTimeText;

        @BindView(R.id.time_table_item_card)
        CardView mTimeTableCard;

        public TimeTableItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mTimeTableCard.getLayoutParams().height = mGridUnit * 4;
        }
    }
}
