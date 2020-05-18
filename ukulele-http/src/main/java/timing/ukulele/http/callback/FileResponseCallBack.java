package timing.ukulele.http.callback;

/**
 * 下载的文件返回的数据回调
 *
 * @author fengxici
 */
public interface FileResponseCallBack {

    /**
     * 下载完成
     */
    void onDownloadComplete();

    /**
     * 下载失败
     */
    void onFailure(Throwable t);

    /**
     * 下载进度
     */
    void onProgress(int currentProgress);
}