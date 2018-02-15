package com.thinkpace.pacifyca.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;


import com.thinkpace.pacifyca.entity.timetable.CPCTimeTableData;
import com.thinkpace.pacifyca.fragment.FPCTimetable;
import com.thinkpace.pacifyca.utils.CPCAppConstants;

import java.util.ArrayList;

/**
 * Created by Krishna Upadhya on 20-03-2016.
 */
public class CPCTImeTablePagerAdapter extends FragmentPagerAdapter {
    private ArrayList<CPCTimeTableData> mTabMenuList;
    private SparseArray<Fragment> mFragmentReferences;

    public CPCTImeTablePagerAdapter(FragmentManager fm, ArrayList<CPCTimeTableData> tabMenuList) {
        super(fm);
        mTabMenuList = tabMenuList;
        mFragmentReferences = new SparseArray<Fragment>();
    }

    @Override
    public Fragment getItem(int position) {
        FPCTimetable fragment = FPCTimetable.newInstance();
        if (mTabMenuList != null && mTabMenuList.get(position) != null) {
            Bundle bundle = new Bundle();
            bundle.putString(CPCAppConstants.WEEK_NAME, mTabMenuList.get(position).getWeek_name());
            if (mTabMenuList != null && mTabMenuList.size() > 0 && mTabMenuList.get(position) != null)
                bundle.putSerializable(CPCAppConstants.TIME_TABLE_LIST, mTabMenuList.get(position).getItem());
            fragment.setArguments(bundle);
           // fragment.setList(mTabMenuList.get(position).getItem());

        }
        mFragmentReferences.put(position, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        if (mTabMenuList != null) {
            return mTabMenuList.size();
        }
        return 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        /*Fragment fragment = (Fragment) object;
        View view = (View) fragment.getView();
        ((ViewPager) container).removeView(view);
        mFragmentReferences.remove(position);
        super.destroyItem(container, position, object);*/
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabMenuList.get(position).getWeek_name();
    }

    public Fragment getFragmentInstance(int position) {
        return mFragmentReferences.get(position);
    }
}
