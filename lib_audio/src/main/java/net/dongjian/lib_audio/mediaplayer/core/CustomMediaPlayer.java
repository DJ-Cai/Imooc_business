package net.dongjian.lib_audio.mediaplayer.core;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * 带状态的MediaPlayer （通过mediaplayer获取的状态不准确）
 */
public class CustomMediaPlayer extends MediaPlayer implements MediaPlayer.OnCompletionListener {

    //当前状态
    private Status mState;
    private OnCompletionListener mCompletionListener;

    //通过枚举来判断状态
    public enum Status{
        //状态:空闲、初始化、启动、暂停、停止、完成；
        IDEL , INITALIZED , STARTED , PAUSED , STOPPTED , COMPLETED;
    }

    public CustomMediaPlayer(){
        super();
        mState = Status.IDEL;
        //在播放期间到达媒体源末尾时要调用的回调
        super.setOnCompletionListener(this);
    }

    @Override
    public void reset() {
        super.reset();
        mState = Status.IDEL;
    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, IllegalStateException, SecurityException {
        super.setDataSource(path);
        mState = Status.INITALIZED;
    }

    @Override
    public void start() throws IllegalStateException {
        super.start();
        mState = Status.STARTED;
    }

    @Override
    public void pause() throws IllegalStateException {
        super.pause();
        mState = Status.PAUSED;
    }

    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        mState = Status.STOPPTED;
    }

    /**
     * 完成
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        mState = Status.COMPLETED;
    }

    public Status getState(){
        return mState;
    }

    /**
     * 是否播放完成
     * @return
     */
    public boolean isComplete(){
        return mState == Status.COMPLETED;
    }

    public void setCompleteListener(OnCompletionListener listener){
        mCompletionListener = listener;
    }
}
