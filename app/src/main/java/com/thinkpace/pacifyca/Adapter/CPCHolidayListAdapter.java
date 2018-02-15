package com.thinkpace.pacifyca.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.calander.APCInstituteCalendarActivity;
import com.thinkpace.pacifyca.entity.CPCInstituteCalenderModel;

/**
 * Created by Krishna on 3/19/2017.
 */
public class CPCHolidayListAdapter extends RecyclerView.Adapter<CPCHolidayListAdapter.ViewHolder> {
    Context mContext;
    private CPCInstituteCalenderModel mInstituteCalenderModel;

    public CPCHolidayListAdapter(Context context, CPCInstituteCalenderModel mMonthData) {
        mContext = context;
        mInstituteCalenderModel = mMonthData;
    }

    @Override
    public CPCHolidayListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holiday_list_item, parent, false);
        return new CPCHolidayListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        mInstituteCalenderModel.getInstituteData().get(position).getDate();

        holder.mHolidayName.setText(mInstituteCalenderModel.getInstituteData().get(position).getTitle() + " - " +
                mInstituteCalenderModel.getInstituteData().get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mInstituteCalenderModel.getInstituteData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mHolidayName;

        public ViewHolder(View itemView) {
            super(itemView);
            mHolidayName = (TextView) itemView.findViewById(R.id.holiday_name);
        }
    }
}
