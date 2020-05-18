package timing.ukulele.http.callback;

/**
 * 普通返回的数据回调
 *
 * @author fengxici
 */
public interface ResponseCallBack<T> {

    /**
     * 请求成功
     */
    void onResponse(T response);

    /**
     * 请求失败
     */
    void onFailure(int httpCode, String msg);
}