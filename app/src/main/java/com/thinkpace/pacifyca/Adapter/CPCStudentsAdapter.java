/*
 * Copyright © 2016, National Media Group Ltd. All rights reserved.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

/**
 * Created by Krishna Upadhya on 8/8/2016.
 */

package com.thinkpace.pacifyca.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.entity.CPCStudentDetails;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;
import com.thinkpace.pacifyca.widget.RoboTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CPCStudentsAdapter extends RecyclerView.Adapter<CPCStudentsAdapter.StudentsViewHolder> {

    private Context mContext;
    private ArrayList<CPCStudentDetails> mListItems;
    private int mGridUnit;
    // Define listener member variable
    private OnStudentsClickListener onStudentsClickListener;

    // Define the listener interface
    public interface OnStudentsClickListener {
        void onStudentsItemClick(CPCStudentDetails item, int position);

        void onInfoClick(CPCStudentDetails item, int position);

    }

    public CPCStudentsAdapter(Context context, ArrayList<CPCStudentDetails> mArrayList, OnStudentsClickListener listener) {
        this.mContext = context;
        this.mListItems = mArrayList;
        mGridUnit = CPCAppCommonUtility.getScreenGridUnitBy16(mContext);
        onStudentsClickListener = listener;
    }

    @Override
    public StudentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_students, parent, false);
        return new StudentsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final StudentsViewHolder holder, final int position) {
        final CPCStudentDetails currentItem = getItem(position);
        holder.studentName.setText(currentItem.getFullName());
        String studentId = CPCAppCommonUtility.getStudentId(mContext, CPCAppConstants.KEY_STUDENT_ID);
        holder.mRadioButton.setOnCheckedChangeListener(null);
        if (!TextUtils.isEmpty(studentId) && studentId.equals(currentItem.getStudent_id()))
            holder.mRadioButton.setChecked(true);
        else
            holder.mRadioButton.setChecked(false);

        holder.mRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onStudentsClickListener != null && isChecked )
                    onStudentsClickListener.onStudentsItemClick(currentItem, position);
            }
        });

        holder.item_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onStudentsClickListener != null)
                    onStudentsClickListener.onStudentsItemClick(currentItem, position);
            }
        });
        holder.mInfoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onStudentsClickListener != null)
                    onStudentsClickListener.onInfoClick(currentItem, position);
            }
        });

        holder.mCourseName.setText(currentItem.getCourseName());
    }

    public void updateStudentList(ArrayList<CPCStudentDetails> listItems) {
        this.mListItems = listItems;
        notifyDataSetChanged();
    }


    private CPCStudentDetails getItem(int position) {
        return mListItems.get(position);
    }

    @Override
    public int getItemCount() {
        if (mListItems != null) {
            return mListItems.size();
        }
        return 0;
    }

    class StudentsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.student_name)
        TextView studentName;
        @BindView(R.id.students_item_lyt)
        RelativeLayout item_lyt;

        @BindView(R.id.student_radio_btn)
        RadioButton mRadioButton;

        @BindView(R.id.info_icon)
        ImageView mInfoIcon;

        @BindView(R.id.course_name)
        RoboTextView mCourseName;

        public StudentsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
