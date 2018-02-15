package com.thinkpace.pacifyca.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.entity.CPCPieChartItems;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Krishna Upadhya on 12/27/2016.
 */

public class CPCAttendanceChartAdapter extends RecyclerView.Adapter<CPCAttendanceChartAdapter.AuthorViewHolder> {
    public static final int[] CHART_TWO_COLORS = {
            Color.rgb(97, 169, 220), Color.rgb(204, 204, 204)
    };
    public static final int[] CHART_THREE_COLORS = {
            Color.rgb(97, 169, 220), Color.rgb(255, 242, 0), Color.rgb(204, 204, 204)
    };
    private Context mContext;
    private ArrayList<CPCPieChartItems> mListItems;
    private int mGridUnit;
    // Define listener member variable
    private OnChartClickListener onChartClickListener;

    // Define the listener interface
    public interface OnChartClickListener {
        void onChartItemClick(CPCPieChartItems item, int position, Boolean isDailyTwice);
    }

    public CPCAttendanceChartAdapter(Context context, ArrayList<CPCPieChartItems> mArrayList, OnChartClickListener listener) {
        this.mContext = context;
        this.mListItems = mArrayList;
        onChartClickListener = listener;
    }

    @Override
    public AuthorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_pie_chart, parent, false);
        return new AuthorViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final AuthorViewHolder holder, final int position) {
        final CPCPieChartItems chartItemData = mListItems.get(position);
        Boolean isDailyTwice = false;
        if (chartItemData == null) return;

        if (!TextUtils.isEmpty(chartItemData.getType()) && chartItemData.getType().equals(CPCAppConstants.KEY_GROUP_DAILY_ONCE)) {
            setDailyOnceData(holder, chartItemData);
        } else if (!TextUtils.isEmpty(chartItemData.getType()) && chartItemData.getType().equals(CPCAppConstants.KEY_GROUP_DAILY_TWICE)) {
            setDailyTwiceData(holder, chartItemData);
            isDailyTwice = true;
        } else if (!TextUtils.isEmpty(chartItemData.getType()) && chartItemData.getType().equals(CPCAppConstants.KEY_GROUP_SUBJECT)) {
            setDailyOnceData(holder, chartItemData);
        }

        final Boolean finalIsDailyTwice = isDailyTwice;
        holder.moreDetailsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChartClickListener.onChartItemClick(chartItemData, position, finalIsDailyTwice);
            }
        });
    }

    private void setSubjectWiseData(AuthorViewHolder holder, CPCPieChartItems chartItemData) {
        holder.pieChart.setVisibility(View.GONE);
        holder.subjectCircleLyt.setVisibility(View.VISIBLE);
        holder.titleText.setText(chartItemData.getSubject());
        holder.presentText.setText("Present - " + chartItemData.getPresent());
        holder.presentPmLyt.setVisibility(View.GONE);
        holder.totalText.setText("Total - " + chartItemData.getTotalClasses());
        holder.absentText.setText("Absent - " + chartItemData.getAbsent());
        int presentCount = 0;
        try {
            presentCount = Integer.parseInt(chartItemData.getPresent());
        } catch (NumberFormatException nfe) {
            CPCAppCommonUtility.log("Error", nfe.getMessage());
        } catch (Exception ex) {
            CPCAppCommonUtility.log("Error", ex.getMessage());
        }
        float totalPercent = ((float) presentCount / chartItemData.getTotalClasses()) * 100;
        holder.subPresentText.setText((int) totalPercent + "%");
    }

    private void setDailyTwiceData(AuthorViewHolder holder, CPCPieChartItems chartItemData) {
        holder.pieChart.setVisibility(View.VISIBLE);
        holder.subjectCircleLyt.setVisibility(View.GONE);
        holder.presentPmLyt.setVisibility(View.VISIBLE);
        holder.absentPmLyt.setVisibility(View.VISIBLE);
        holder.presentText.setText("Present AM - " + (chartItemData.getmPresentAMCount()));
        holder.presentPMText.setText("Present PM - " + (chartItemData.getmPresentPMCount()));
        holder.totalText.setText("Total - " + ((float) chartItemData.getmTotalAmPmCount() / 2) + " days");
        holder.absentText.setText("Absent AM - " + (chartItemData.getmAbsentAMCount()));
        holder.mAbsentPmText.setText("Absent PM - " + (chartItemData.getmAbsentPMCount()));
        setChartTwo(holder.pieChart, chartItemData);
    }

    private void setDailyOnceData(AuthorViewHolder holder, CPCPieChartItems chartItemData) {
        holder.pieChart.setVisibility(View.VISIBLE);
        holder.subjectCircleLyt.setVisibility(View.GONE);
        holder.presentText.setText("Present - " + chartItemData.getmPresentCount());
        holder.presentPmLyt.setVisibility(View.GONE);
        holder.absentPmLyt.setVisibility(View.GONE);
        holder.totalText.setText("Total - " + chartItemData.getTotalClasses() + " days");
        holder.absentText.setText("Absent - " + chartItemData.getmAbsentCount());
        setDailyOnceChart(holder.pieChart, chartItemData);
        if (chartItemData.getSubject() != null && chartItemData.getSubject().trim().length() > 0) {
            holder.mSubjectName.setVisibility(View.VISIBLE);
            holder.mSubjectName.setText(chartItemData.getSubject());
        } else {
            holder.mSubjectName.setVisibility(View.GONE);
        }

    }

    private void setDailyOnceChart(PieChart pieChart, CPCPieChartItems chartData) {
        float attendedClass;
        float absentClasses;

        absentClasses = chartData.getmAbsentCount();
        ArrayList<PieEntry> entries = new ArrayList<>();
        attendedClass = ((float) chartData.getmPresentCount() / chartData.getTotalClasses()) * 100;
        absentClasses = ((float) absentClasses / chartData.getTotalClasses()) * 100;
        entries.add(new PieEntry(attendedClass, 0));
        entries.add(new PieEntry(absentClasses, 1));

        PieDataSet dataSet = new PieDataSet(entries, "# of Calls");
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("present");
        labels.add("absent");


        PieData data = new PieData(dataSet);
        dataSet.setColors(CHART_TWO_COLORS); //
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setData(data);
        pieChart.setHoleRadius(60f);
        if (chartData != null && chartData.getTotalClasses() != 0) {
            pieChart.setCenterText((int) attendedClass + "%");

        }
        pieChart.setCenterTextSize(13f);
        data.setDrawValues(false);
        pieChart.setHoleColor(mContext.getResources().getColor(android.R.color.white));

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        pieChart.animateY(2000);
    }

    private void setChartTwo(PieChart pieChart, CPCPieChartItems chartData) {
        float absent;
        float presentAM;
        float presentPM;
        ArrayList<PieEntry> entries = new ArrayList<>();
        presentAM = ((float) chartData.getmPresentAMCount() / chartData.getmTotalAmPmCount()) * 100;
        presentPM = ((float) chartData.getmPresentPMCount() / chartData.getmTotalAmPmCount()) * 100;
        absent = ((float) chartData.getmAbsentAmPmCount() / chartData.getmTotalAmPmCount()) * 100;
        entries.add(new PieEntry(presentAM, 0));
        entries.add(new PieEntry(presentPM, 1));
        entries.add(new PieEntry(absent, 2));

        pieChart.setEntryLabelColor(mContext.getResources().getColor(R.color.white));

        PieDataSet dataSet = new PieDataSet(entries, "");
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("present(AM)");
        labels.add("present(PM)");
        labels.add("absent");

        PieData data = new PieData(dataSet);
        dataSet.setColors(CHART_THREE_COLORS); //
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setData(data);
        pieChart.setHoleRadius(60f);
        if (chartData != null && chartData.getTotalClasses() != 0) {
            pieChart.setCenterText((int) (presentAM + presentPM) + "%");
        }

        pieChart.setCenterTextSize(13f);
        data.setDrawValues(false);

        pieChart.setHoleColor(mContext.getResources().getColor(android.R.color.white));
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
       /* List<LegendEntry> lentries = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = CHART_THREE_COLORS[i];
            entry.label = labels.get(i);
            lentries.add(entry);
        }

        legend.setCustom(lentries);*/

        pieChart.animateY(2000);
        // pieChart.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image
    }

    @Override
    public int getItemCount() {
        if (mListItems != null) {
            return mListItems.size();
        }
        return 0;
    }

    class AuthorViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.present_text)
        TextView presentText;
        @BindView(R.id.title_text)
        TextView titleText;
        @BindView(R.id.subject_percent)
        TextView subPresentText;
        @BindView(R.id.present_PM_text)
        TextView presentPMText;
        @BindView(R.id.absent_text)
        TextView absentText;
        @BindView(R.id.total_text_text)
        TextView totalText;
        @BindView(R.id.more_details)
        TextView moreDetailsText;
        @BindView(R.id.pie_chart)
        PieChart pieChart;
        @BindView(R.id.subject_type_lyt)
        RelativeLayout subjectCircleLyt;
        @BindView(R.id.root_chart_lyt)
        RelativeLayout rootLyt;
        @BindView(R.id.present_pm_lyt)
        LinearLayout presentPmLyt;
        @BindView(R.id.absent_pm_lyt)
        LinearLayout absentPmLyt;
        @BindView(R.id.subject_name)
        TextView mSubjectName;
        @BindView(R.id.absent_pm_text)
        TextView mAbsentPmText;

        public AuthorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

