package com.thinkpace.pacifyca.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.thinkpace.pacifyca.Adapter.CPCTimeTableListAdapter;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.entity.timetable.CPCTimeTableItem;
import com.thinkpace.pacifyca.utils.CPCAppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FPCTimetable extends Fragment {

    @BindView(R.id.time_table_list_recycler)
    RecyclerView mTimeTableRecycler;

    @BindView(R.id.no_messages_lyt)
    LinearLayout mNoMessagesLyt;

    private String mWeekName;
    private ArrayList<CPCTimeTableItem> mTimeTableList;
    private Unbinder unbinder;
    private CPCTimeTableListAdapter mTimeTableListAdapter;

    public FPCTimetable() {
        // Required empty public constructor
    }

    public static FPCTimetable newInstance() {
        FPCTimetable fragment = new FPCTimetable();
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        unbinder = ButterKnife.bind(this, view);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        if (getActivity() != null)
            mTimeTableRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTimeTableRecycler.setHasFixedSize(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        mWeekName = bundle.getString(CPCAppConstants.WEEK_NAME);
        mTimeTableList = (ArrayList<CPCTimeTableItem>) bundle.getSerializable(CPCAppConstants.TIME_TABLE_LIST);
    }

    public void setList(ArrayList<CPCTimeTableItem> timeList) {
        this.mTimeTableList = timeList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAssignmentAdapter();
    }

    private void setAssignmentAdapter() {

        if (mTimeTableList != null && mTimeTableList.size() > 0) {
            mNoMessagesLyt.setVisibility(View.GONE);
            mTimeTableRecycler.setVisibility(View.VISIBLE);
            mTimeTableListAdapter = new CPCTimeTableListAdapter(getActivity(), mTimeTableList);
            mTimeTableRecycler.setAdapter(mTimeTableListAdapter);
        } else {
            mNoMessagesLyt.setVisibility(View.VISIBLE);
            mTimeTableRecycler.setVisibility(View.GONE);
        }
    }

}
