package net.dongjian.imooc_voice.api;

import net.dongjian.imooc_voice.model.user.User;
import net.dongjian.lib_network.okhttp.CommonOkHttpClient;
import net.dongjian.lib_network.okhttp.listener.DisposeDataHandle;
import net.dongjian.lib_network.okhttp.listener.DisposeDataListener;
import net.dongjian.lib_network.okhttp.request.CommonRequest;
import net.dongjian.lib_network.okhttp.request.RequestParams;

/**
 * 项目中所有关于网络请求方面的请求中心
 */
public class RequestCenter {

    /**
     * 相关的请求地址
     */
    static class HttpConstats {
//        private static final String ROOT_URL = "http://imooc.com/api";
        //服务器网络地址
        private static final String ROOT_URL = "http://39.97.122.129";


        public static String LOGIN = ROOT_URL + "/module_voice/login_phone";
    }

    /**
     * 发送post类型的请求
     * @param url  地址
     * @param params 参数
     * @param listener 回调
     * @param clazz
     */
    public static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, params),
                new DisposeDataHandle(listener, clazz));
    }

    /**
     * 用户登陆请求
     */
    public static void login(DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("mb" , "15521216459");
        params.put("pwd","999999q");
        RequestCenter.postRequest(HttpConstats.LOGIN,params,listener, User.class);

    }

}
