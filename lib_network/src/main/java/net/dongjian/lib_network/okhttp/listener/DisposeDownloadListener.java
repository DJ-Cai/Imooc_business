package net.dongjian.lib_network.okhttp.listener;

/**
 * 下载方面的监听有时候需要监听一下进度
 * @function 监听下载进度
 */
public interface DisposeDownloadListener extends DisposeDataListener {
    void onProgress(int progress);
}
