package net.dongjian.lib_network.okhttp.response;

import android.os.Handler;

import com.google.gson.Gson;

import net.dongjian.lib_network.okhttp.exception.OkHttpException;
import net.dongjian.lib_network.okhttp.listener.DisposeDataListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 处理json类型的响应
 */
public class CommonJsonCallback implements Callback {

    protected final String EMPTY_MSG = "";
    /**
     * 具体异常的类型
     */
    protected final int NET_ERROR = -1; //网络层产生异常
    protected final int JSON_ERROR = -2; //json的相关异常
    protected final int OTHER_ERROR = -3; //未知异常

    /**
     * 处理json response需要
     * 1、listener回调：给业务层处理具体逻辑
     * 2、handler：能够切换到主线程（拿到数据的线程非UI线程）
     * 3、object：表明json数据需要转化为的具体对象
     */
    private DisposeDataListener mListener;
    private Handler mDeliveryHandler;
    private Class<?> mClass;

    @Override
    public void onFailure(Call call, IOException e) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NET_ERROR,e));
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        //1、在response里获取数据
        final String result = response.body().string();
        //2、对数据进行处理
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    private void handleResponse(String result) {
        //如果数据为空，直接失败
        if(result == null){
            mListener.onFailure(new OkHttpException(NET_ERROR,EMPTY_MSG));
            return;
        }
        try{
            //将数据转换为业务层需要的对象
            Object obj = new Gson().fromJson(result,mClass);
            //转换成功则返回成功，失败则失败
            if(obj != null){
                mListener.onSuccess(obj);
            }else{
                mListener.onFailure(new OkHttpException(JSON_ERROR,EMPTY_MSG));
            }
        }catch (Exception e){
            mListener.onFailure(new OkHttpException(OTHER_ERROR,e.getMessage()));
            e.printStackTrace();
        }
    }
}
