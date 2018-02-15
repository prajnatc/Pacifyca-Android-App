package com.thinkpace.pacifyca.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.entity.CPCStudents;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sapna on 15-05-2016.
 */
public class CPCStudentsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<CPCStudents> mListItems;
    int mGridUnit;

    public CPCStudentsListAdapter(Context context, ArrayList<CPCStudents> mArrayList) {
        this.mContext = context;
        this.mListItems = mArrayList;
        mGridUnit = CPCAppCommonUtility.getScreenGridUnitBy16(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.students_item_lyt, parent, false);
        return new StudentsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final StudentsItemViewHolder mItemViewHolder = (StudentsItemViewHolder) holder;
        final CPCStudents students = mListItems.get(position);
        /*if (students != null)
            mItemViewHolder.mAssignmentTitle.setText(students.getName());

        mItemViewHolder.mItemsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, students.getName(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private CPCStudents getItem(int position) {
        return mListItems.get(position);
    }

    @Override
    public int getItemCount() {
        if (mListItems != null) {
            return mListItems.size();
        }
        return 0;
    }

    // Define listener member variable
    private OnStudentItemClickListener listener;

    // Define the listener interface
    public interface OnStudentItemClickListener {
        void onHeaderItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnStudentItemClickListener(OnStudentItemClickListener listener) {
        this.listener = listener;
    }

    class StudentsItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.student_name_text)
        TextView mNameTextView;
        @BindView(R.id.student_item)
        RelativeLayout mItemsLayout;

        public StudentsItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mItemsLayout.getLayoutParams().height = mGridUnit * 3;
        }
    }
}
