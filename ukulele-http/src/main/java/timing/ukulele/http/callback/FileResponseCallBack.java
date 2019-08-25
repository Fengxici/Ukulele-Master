package timing.ukulele.http.callback;

/**
 *  下载的文件返回的数据回调
 */
public interface FileResponseCallBack {

    void onDownloadComplete();


    void onFailure(Throwable t);

    void onProgress(int currentProgress);
}