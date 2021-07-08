package net.dongjian.imooc_voice.view.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import net.dongjian.imooc_voice.R;
import net.dongjian.imooc_voice.model.user.CHANNEL;
import net.dongjian.imooc_voice.view.home.adpater.HomePagerAdapter;
import net.dongjian.lib_common_ui.base.BaseActivity;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    //首页出现的卡片，一方面表明长度，一方面表明内容
    private static final CHANNEL[] CHANNELS = new CHANNEL[]{CHANNEL.MY,CHANNEL.DISCORY,CHANNEL.FRIEND};

    private DrawerLayout mDrawerLayout;
    private View mToggleView;
    private View mSearchView;

    private ViewPager mViewPager;
    private HomePagerAdapter mHomePagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initData();
    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggleView = findViewById(R.id.toggle_view);
        mSearchView = findViewById(R.id.search_view);
        mViewPager = findViewById(R.id.view_pager);
        mHomePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(),CHANNELS);
        mViewPager.setAdapter(mHomePagerAdapter);

        mToggleView.setOnClickListener(this);
        
        initMagicIndicator();
    }

    private void initData() {

    }

    //初始化指示器 、 实现缩放等效果
    private void initMagicIndicator() {
        MagicIndicator magicIndicator = findViewById(R.id.magic_indicator);
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                //返回数量，这里判断卡片数量
                return CHANNELS == null ? 0 : CHANNELS.length;
            }

            @Override
            //初始化每个titleView的具体效果
            public IPagerTitleView getTitleView(Context context, int index) {
                //使用简单样式,自定义内容、大小、字体、颜色、选中后颜色、监听器
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(CHANNELS[index].getKey());
                simplePagerTitleView.setTextSize(19);
                simplePagerTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                simplePagerTitleView.setNormalColor(Color.parseColor("#999999"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#333333"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //选中后ViewPager设置选中的index为当前item
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        //进行绑定
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator,mViewPager);
    }


    @Override
    public void onClick(View v) {

    }
}
