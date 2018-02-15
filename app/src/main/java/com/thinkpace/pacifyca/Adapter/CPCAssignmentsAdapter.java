package com.thinkpace.pacifyca.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.entity.CPCAssignmentList;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sapna on 15-03-2017.
 */

public class CPCAssignmentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<CPCAssignmentList> mListItems;
    int mGridUnit;

    public CPCAssignmentsAdapter(Context context, ArrayList<CPCAssignmentList> mArrayList) {
        this.mContext = context;
        this.mListItems = mArrayList;
        mGridUnit = CPCAppCommonUtility.getScreenGridUnitBy16(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignments_content_layout, parent, false);
        return new CPCAssignmentsAdapter.AssignmentsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CPCAssignmentsAdapter.AssignmentsItemViewHolder mItemViewHolder = (CPCAssignmentsAdapter.AssignmentsItemViewHolder) holder;
        final CPCAssignmentList assignmentDetail = mListItems.get(position);
        if (assignmentDetail != null) {



            mItemViewHolder.mAssignmentDate.setText(assignmentDetail.getAssigneddate());
            mItemViewHolder.mAssignmentTitle.setText(assignmentDetail.getTitle());
            mItemViewHolder.mAssignmentDesc.setText(assignmentDetail.getAssigneeName());
            mItemViewHolder.mItemsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assignmentClickListener.onAssignmentItemClick(position, assignmentDetail);
                }
            });

            if (assignmentDetail.isRead()) {
                mItemViewHolder.mAssignmentTitle.setTypeface(null, Typeface.NORMAL);
                mItemViewHolder.mAssignmentDesc.setTypeface(null, Typeface.NORMAL);
            } else {
                mItemViewHolder.mAssignmentTitle.setTypeface(null, Typeface.BOLD);
                mItemViewHolder.mAssignmentDesc.setTypeface(null, Typeface.BOLD);
            }
        }

    }

    @Override
    public int getItemCount() {
        if (mListItems != null)
            return mListItems.size();
        else
            return 0;
    }


    // Define listener member variable
    private OnAssignmentClickListener assignmentClickListener;


    // Define the listener interface
    public interface OnAssignmentClickListener {
        void onAssignmentItemClick(int position, CPCAssignmentList assignmentDetail);

    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnAssignmentClickListener(OnAssignmentClickListener listener) {
        this.assignmentClickListener = listener;
    }

    public void updateList(ArrayList<CPCAssignmentList> assignmentList) {
        this.mListItems = assignmentList;
        notifyDataSetChanged();
    }

    class AssignmentsItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.assignment_name)
        TextView mAssignmentTitle;

        @BindView(R.id.assignment_description)
        TextView mAssignmentDesc;

        @BindView(R.id.assignment_date)
        TextView mAssignmentDate;

       /* @BindView(R.id.message_time)
        TextView mMessageTime;*/

        @BindView(R.id.assignment_item_card)
        CardView mAssignmentCard;

        @BindView(R.id.assignment_list_item_lyt)
        RelativeLayout mItemsLayout;


        public AssignmentsItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mAssignmentCard.getLayoutParams().height = mGridUnit * 4;
            mItemsLayout.getLayoutParams().height = mGridUnit * 4;
        }
    }
}
