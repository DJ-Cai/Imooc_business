package net.dongjian.imooc_voice.api;

import net.dongjian.lib_network.okhttp.CommonOkHttpClient;
import net.dongjian.lib_network.okhttp.listener.DisposeDataHandle;
import net.dongjian.lib_network.okhttp.listener.DisposeDataListener;
import net.dongjian.lib_network.okhttp.request.CommonRequest;
import net.dongjian.lib_network.okhttp.request.RequestParams;

/**
 * 关于网络请求方面的请求中心
 */
public class RequestCenter {

    static class HttpConstats {
        private static final String ROOT_URL = "http://imooc.com/api";
    }

    public static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, params),
                new DisposeDataHandle(listener, clazz));
    }

}
