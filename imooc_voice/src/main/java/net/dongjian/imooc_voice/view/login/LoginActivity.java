package net.dongjian.imooc_voice.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;


import net.dongjian.imooc_voice.R;
import net.dongjian.imooc_voice.api.RequestCenter;
import net.dongjian.imooc_voice.event.LoginEvent;
import net.dongjian.imooc_voice.model.user.User;
import net.dongjian.imooc_voice.utils.UserManager;
import net.dongjian.lib_common_ui.base.BaseActivity;
import net.dongjian.lib_network.okhttp.listener.DisposeDataListener;

import org.greenrobot.eventbus.EventBus;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {

    public static void start(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);

        findViewById(R.id.login_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从请求中心里发出登录请求，这里处理回调即可
                RequestCenter.login(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        User user = (User) responseObj;
                        UserManager.getInstance().saveUser(user);
                        //通知多个UI进行对应的更新
                        EventBus.getDefault().post(new LoginEvent());
                        finish();
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        //登陆失败
                        Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT);
                    }
                });
            }
        });
    }
}
