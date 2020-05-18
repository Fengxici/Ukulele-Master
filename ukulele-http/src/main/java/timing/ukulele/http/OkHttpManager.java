package timing.ukulele.http;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * okHttp3 管理类，封装了okHttp的网络连接方法
 *
 * @author fengxici
 */
public enum OkHttpManager {
    /**
     * 实例
     */
    INSTANCE;

    public OkHttpManager getInstance() {
        return INSTANCE;
    }

    protected OkHttpClient okHttpClient;
    protected OkHttpClient.Builder builder;
    protected int mTimeOut = 60;
    protected String headerKey, headerValue;
    protected boolean isLog = true;

    /**
     * 设置header
     * 暂时不支持多个header
     *
     * @param headerKey   键
     * @param headerValue 值
     * @return
     */
    public OkHttpManager setHeader(String headerKey, String headerValue) {
        this.headerKey = headerKey;
        this.headerValue = headerValue;
        return INSTANCE;
    }

    /**
     * 设置超时时间
     *
     * @param timeOut 数值
     * @return
     */
    public OkHttpManager setTimeOut(int timeOut) {
        mTimeOut = timeOut;
        return INSTANCE;
    }

    /**
     * 是否显示Log 默认为true
     *
     * @param isLog true 显示
     * @return
     */
    public OkHttpManager setLog(boolean isLog) {
        this.isLog = isLog;
        return INSTANCE;
    }

    public OkHttpManager build(List<Interceptor> interceptors) {
        if (okHttpClient == null) {
            builder = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor()
                            .setLevel(isLog ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                    .retryOnConnectionFailure(true)
                    .connectTimeout(mTimeOut, TimeUnit.SECONDS)
                    .readTimeout(mTimeOut, TimeUnit.SECONDS)
                    .writeTimeout(mTimeOut, TimeUnit.SECONDS)
                    .addInterceptor(chain -> {
                        Request response = chain.request();
                        if (headerKey != null) {
                            response = chain.request()
                                    .newBuilder()
                                    .addHeader(headerKey, headerValue)
                                    .build();
                        }
                        return chain.proceed(response);
                    });

            okHttpClient = builder.build();
        }
        builder.interceptors().clear();
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        return INSTANCE;
    }

    /**
     * @return OkHttp3 Client
     */
    public OkHttpClient getClient() {
        return okHttpClient;
    }

    public OkHttpClient.Builder getBuilder() {
        return builder;
    }
}