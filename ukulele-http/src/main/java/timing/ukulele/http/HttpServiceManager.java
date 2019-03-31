package timing.ukulele.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpServiceManager {

    private static final Gson gsonFormat = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    protected static HttpServiceManager instance;

    public static HttpServiceManager getInstance(){
        if(instance==null){
            synchronized (HttpServiceManager.class){
                if(instance==null){
                    instance=new HttpServiceManager();
                }
            }
        }
        return instance;
    }

    protected static String sBaseUrl;

    protected Retrofit retrofit;

    public HttpServiceManager(){
    }

    public HttpServiceManager setBaseUrl(String url){
        sBaseUrl=url;
        return instance;
    }

    public void build(OkHttpClient okHttpClient){
        if(sBaseUrl==null)
            new IllegalAccessException("need setBaseUrl");
        retrofit=new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(sBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gsonFormat))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        if(retrofit==null)
            new IllegalAccessException("Please build");
        return retrofit;
    }

}
