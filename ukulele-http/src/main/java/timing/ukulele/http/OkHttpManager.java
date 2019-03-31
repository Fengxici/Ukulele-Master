package timing.ukulele.http;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * okHttp3 管理类，封装了okHttp的网络连接方法
 * OkHttp3 开启Stetho听诊器
 */
public class OkHttpManager {
    protected static OkHttpManager instance;

    public static OkHttpManager getInstance() {
        if (instance == null) {
            synchronized (OkHttpManager.class) {
                if (instance == null) {
                    instance = new OkHttpManager();
                }
            }
        }
        return instance;
    }

    protected static OkHttpClient okHttpClient;
    protected static OkHttpClient.Builder builder;
    protected int mTimeOut=60;
    protected String headerKey,headerValue;
    protected boolean isLog=true;

    public OkHttpManager() {
    }

    /**
     * 设置header
     * 暂时不支持多个header
     * @param headerKey
     * @param headerValue
     * @return
     */
    public OkHttpManager setHeader(String headerKey,String headerValue) {
        this.headerKey = headerKey;
        this.headerValue = headerValue;
        return instance;
    }

    /**
     * 设置超时时间
     * @param timeOut 数值
     * @return
     */
    public OkHttpManager setTimeOut(int timeOut) {
        mTimeOut = timeOut;
        return instance;
    }

    /**
     * 是否显示Log 默认为true
     * @param isLog true 显示
     * @return
     */
    public OkHttpManager setLog(boolean isLog){
        this.isLog=isLog;
        return instance;
    }

    public OkHttpManager build(List<Interceptor> interceptors){
        if (okHttpClient == null) {
            builder = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor()
                            .setLevel(isLog? HttpLoggingInterceptor.Level.BODY: HttpLoggingInterceptor.Level.NONE))
                    .retryOnConnectionFailure(true)
                    .connectTimeout(mTimeOut, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request response = chain.request();
                            if (headerKey != null) {
                                response = chain.request()
                                    .newBuilder()
                                    .addHeader(headerKey, headerValue)
                                    .build();
                            }
                            return chain.proceed(response);
                        }
                    });
            if(interceptors!=null){
                for(Interceptor interceptor:interceptors){
                    builder.addInterceptor(interceptor);
                }
            }
            okHttpClient = builder.build();
        }
        return instance;
    }

    /**
     * @return OkHttp3 Client
     */
    public OkHttpClient getClient() {
        return okHttpClient;
    }

    public OkHttpClient.Builder getBuilder(){
        return builder;
    }
}
