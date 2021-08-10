package net.dongjian.lib_audio.mediaplayer.core;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;

/**
 * 功能：
 * 1、播放音频
 * 2、对外发送各种类型的事件（所以需要实现各种事件的监听器）
 */
public class AudioPlayer implements MediaPlayer.OnCompletionListener,
                                    MediaPlayer.OnBufferingUpdateListener,
                                    MediaPlayer.OnPreparedListener,
                                    MediaPlayer.OnErrorListener,
                                    AudioManager.OnAudioFocusChangeListener {


    //增强mediaPlayer的后台保活能力
    private WifiManager.WifiLock mWifiLock;
    //AudioFocusManager 监听播放器的焦点的状态（得到或失去）



    public AudioPlayer(){

    }


    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}
