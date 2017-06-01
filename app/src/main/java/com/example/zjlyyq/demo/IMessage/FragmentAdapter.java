package com.example.zjlyyq.demo.IMessage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by jialuzhang on 2017/6/1.
 */

public class FragmentAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragments;
    private String[] titles = new String[]{"私信","评论","@我","通知"};
    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
