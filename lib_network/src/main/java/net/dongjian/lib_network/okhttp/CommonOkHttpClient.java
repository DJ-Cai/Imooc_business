package net.dongjian.lib_network.okhttp;

import net.dongjian.lib_network.okhttp.listener.DisposeDataHandle;
import net.dongjian.lib_network.okhttp.response.CommonFileCallback;
import net.dongjian.lib_network.okhttp.response.CommonJsonCallback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * okHttpClient主要用来连接request和response
 * 所以这里是用来发送get、post请求的工具类
 */
public class CommonOkHttpClient {

    //超时时间
    private static final int TIME_OUT = 30;
    //单例设计模式
    private static OkHttpClient mOkHttpClient;

    //对okHttpClient进行初始化操作
    static{
        OkHttpClient.Builder okHttpClientBuidler = new OkHttpClient.Builder();
        //设置对域名 默认信任
        okHttpClientBuidler.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        //设置公共请求头
        okHttpClientBuidler.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().
                        addHeader("User-Agent","Immoc-Mobia").build();
                return chain.proceed(request);
            }
        });
        //设置超时时间
        okHttpClientBuidler.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuidler.readTimeout(TIME_OUT,TimeUnit.SECONDS);
        okHttpClientBuidler.writeTimeout(TIME_OUT,TimeUnit.SECONDS);
        //设置允许重定向
        okHttpClientBuidler.followRedirects(true);

        mOkHttpClient =okHttpClientBuidler.build();
    }

    public static OkHttpClient getOkHttpClinet(){
        return mOkHttpClient;
    }

    public static Call get(Request request , DisposeDataHandle handle){
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    public static Call post(Request request , DisposeDataHandle handle){
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    public static Call downloadFile(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonFileCallback(handle));
        return call;
    }

}
