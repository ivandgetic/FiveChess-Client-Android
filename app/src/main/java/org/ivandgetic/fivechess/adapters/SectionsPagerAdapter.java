package org.ivandgetic.fivechess.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.ivandgetic.fivechess.ChatFragment;
import org.ivandgetic.fivechess.FiveFragment;

/**
 * Created by ivandgetic on 2016/5/7 0007.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ChatFragment.newInstance(mContext);
        } else {
            return FiveFragment.newInstance(mContext);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "聊天";
            case 1:
                return "五子棋";
        }
        return null;
    }
}