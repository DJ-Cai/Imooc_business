package net.dongjian.lib_common_ui.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


import net.dongjian.lib_common_ui.utils.StatusBarUtil;

/**
 * Actitity的基础样式， 可以在这里实现沉浸式效果和通用东西
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.statusBarLightMode(this);
    }
}
