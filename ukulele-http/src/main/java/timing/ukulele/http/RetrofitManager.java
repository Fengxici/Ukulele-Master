package timing.ukulele.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import timing.ukulele.http.converter.NullOnEmptyConverterFactory;
import timing.ukulele.http.converter.Utf8GsonConverterFactory;

/**
 * •Retrofit框架的管理类
 * •@className: RetrofitManager
 * •@author: 吕自聪
 * •@date: 2019/8/23
 */
public enum RetrofitManager {
    INSTANCE;

    public RetrofitManager getInstance() {
        return INSTANCE;
    }

    private String mBaseUrl;
    private Retrofit retrofit;

    /**
     * description: 设置网络请求基地址
     * date: 2019/8/23 10:31
     * author: 吕自聪
     *
     * @param url 基地址
     * @return timing.ukulele.http.RetrofitManager
     */
    public RetrofitManager setBaseUrl(String url) {
        mBaseUrl = url;
        return INSTANCE;
    }

    /**
     * description: 获取网络请求基地址
     * date: 2019/8/23 11:32
     * author: 吕自聪
     *
     * @return: java.lang.String 网络请求基地址字符串
     */
    public String getBaseUrl() {
        return mBaseUrl;
    }

    /**
     * description: 实例化retrofit
     *
     * @param okHttpClient OkHttp客户端
     *                     <p>
     *                     date: 2019/8/23 12:50
     *                     author: 吕自聪
     */
    public synchronized void buid(OkHttpClient okHttpClient) {
        if (mBaseUrl == null)
            new IllegalAccessError("没有设置网络请求基地址！");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
        retrofit = new Retrofit.Builder().client(okHttpClient).baseUrl(mBaseUrl)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(Utf8GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * description: 获取retrofit实例
     *
     * @return: retrofit2.Retrofit retrofit实例
     * <p>
     * date: 2019/8/23 12:30
     * author: 吕自聪
     */
    public Retrofit getRetrofit() {
        if (retrofit == null)
            new IllegalAccessException("retrofit没有实例化！");
        return retrofit;
    }
}
