package net.dongjian.lib_network.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import net.dongjian.lib_network.okhttp.exception.OkHttpException;
import net.dongjian.lib_network.okhttp.listener.DisposeDataHandle;
import net.dongjian.lib_network.okhttp.listener.DisposeDownloadListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 处理文件下载类型的响应
 */
public class CommonFileCallback implements Callback {
    protected final String EMPTY_MSG = "";
    /**
     * 具体异常的类型
     */
    protected final int NET_ERROR = -1; //网络层产生异常
    protected final int IO_ERROR = -2; //io异常

    /**
     * 处理file response需要
     * 1、listener回调：给业务层处理具体逻辑
     * 2、handler：能够切换到主线程（拿到数据的线程非UI线程）
     * 3、一个表明间处理进度的时类型，这里用PROGRESS_MESSAGE，如果等于它，就回调进度
     */
    private static final int PROGRESS_MESSAGE = 0X01;
    private DisposeDownloadListener mListener;
    private Handler mDeliveryHandler;

    /**
     * 记录文件路径、当前进度
     */
    private String mFilePath;
    private int mProgress;


    /**
     * 初始化
     *
     * @param handle
     */
    public CommonFileCallback(DisposeDataHandle handle) {
        this.mListener = (DisposeDownloadListener) handle.mListener;
        this.mFilePath = handle.mSource;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper()) {
            //要处理任务进度
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PROGRESS_MESSAGE:
                        mListener.onProgress((int) msg.obj);
                        break;
                }
            }
        };
    }

    @Override
    public void onFailure(Call call, IOException e) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NET_ERROR, EMPTY_MSG));
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        //将response里的文件弄出来
        final File file = handleResponse(response);
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                if (file == null){
                    mListener.onFailure(new OkHttpException(IO_ERROR,EMPTY_MSG));
                }else{
                    mListener.onSuccess(file);
                }
            }
        });
    }

    /**
     * 将response携带的数据转换为file类型文件
     * @param response
     * @return
     */
    private File handleResponse(Response response) {
        if(response == null){
            return null;
        }

        /**
         * 需要一个输入流、输出流、file类型对象、读取时的buffer缓存、当前读写的位置
         */
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        File file = null;
        byte[] buffer = new byte[2048]; //buffer这个地方的赋值从read来
        int length;
        int currentLength = 0;
        double sumLength;

        try{
            //检查file
            checkLocalFilePath(mFilePath);
            //给各个变量赋值
            file = new File(mFilePath);
            fileOutputStream = new FileOutputStream(file);
            inputStream = response.body().byteStream();
            sumLength = (double)response.body().contentLength();

            //read读东西进buffer缓冲区内，write就嗷嗷写进file里
            while((length = inputStream.read(buffer) )!= -1){
                fileOutputStream.write(buffer,0,length);
                currentLength += length;
                mProgress = (int) (currentLength / sumLength * 100);
                //写的过程中对外发送进度
                mDeliveryHandler.obtainMessage(PROGRESS_MESSAGE,mProgress).sendToTarget();
            }
            //循环结束，关闭流
            fileOutputStream.flush();
        }catch (Exception e){
            //出错 file直接赋值为空
            file = null;
        }finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    /**
     * 检查传入的文件路径和内容是否存在，不存在则创建一个
     * @param localFilePath
     */
    private void checkLocalFilePath(String localFilePath){
        File path = new File(localFilePath.substring(0,localFilePath.lastIndexOf("/")+1));
        File file = new File(localFilePath);
        if(!path.exists()){
            path.mkdirs();
        }
        if(!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
