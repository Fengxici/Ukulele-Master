package timing.ukulele.http.callback;

/**
 * 普通返回的数据回调
 */
public interface ResponseCallBack<T> {

    void onResponse(T response);

    void onFailure(int httpCode, String msg);
}