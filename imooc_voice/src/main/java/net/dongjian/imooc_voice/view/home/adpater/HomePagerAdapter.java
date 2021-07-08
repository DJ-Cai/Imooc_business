package net.dongjian.imooc_voice.view.home.adpater;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import net.dongjian.imooc_voice.model.user.CHANNEL;
import net.dongjian.imooc_voice.view.discovery.DiscoveryFragment;
import net.dongjian.imooc_voice.view.friend.FriendFragment;
import net.dongjian.imooc_voice.view.mine.MineFragment;

/**
 * 首页ViewPager的Adapter
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    private CHANNEL[] mList;

    public HomePagerAdapter(FragmentManager fm, CHANNEL[] datas) {
        super(fm);
        mList = datas;
    }

    //这种方式，避免一次性创建所有的framgent 来避免初次进入首页的卡顿
    @Override
    public Fragment getItem(int position) {
        int type = mList[position].getValue();
        switch (type) {
            case CHANNEL.MINE_ID:
                return MineFragment.newInstance();
            case CHANNEL.DISCORY_ID:
                return DiscoveryFragment.newInstance();
            case CHANNEL.FRIEND_ID:
                return FriendFragment.newInstance();
//            case CHANNEL.VIDEO_ID:
//                return VideoFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.length;
    }
}
