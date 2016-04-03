package com.github.mobile.smarttasks.android.ui.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.github.mobile.smarttasks.android.fragment.DayFragment;
import com.github.mobile.smarttasks.android.fragment.DayFragment_;
import com.github.mobile.smarttasks.core.content.ContentController;

import org.joda.time.LocalDate;

import java.util.HashMap;


public class PagerAdapter extends FragmentStatePagerAdapter {
    private static final Integer DAYS_BEFORE = 1000;
    private static final Integer DAYS_AFTER  = 1000;

    private HashMap<Integer, Fragment> fragments = new HashMap<>();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int i) {
        if (i < fragments.size() && fragments.get(i) != null)
            return fragments.get(i);

        Integer daysDiff = DAYS_BEFORE - i;

        LocalDate itemDate = LocalDate.now().minusDays(daysDiff);

        DayFragment fragment = DayFragment_.builder().build();
        fragment.setDateKeyString(itemDate.toDateTimeAtStartOfDay().toDate());

        fragments.put(i, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return (DAYS_BEFORE + DAYS_AFTER + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        LocalDate now = LocalDate.now();
        Integer daysDiff = DAYS_BEFORE - position;
        LocalDate itemDate = now.minusDays(daysDiff);

        return itemDate.toString(ContentController.DATE_FORMAT);
    }

    public int getCurrentDatePosition() {
        return DAYS_BEFORE;
    }
}