package com.cn.example.http;

import android.content.Context;

import com.cn.example.app.App;
import com.cn.example.utils.LogUtils;
import com.cn.example.utils.NetUtil;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/3/7.
 *
 * 网络请求工具类
 */

public class HttpUtils {

    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 ; //second


    private static HttpUtils instance;
    private Context context;
    private Gson gson;
    private boolean debug;
    private Object httpServer;
    /**
     * 获取单例
     * @return
     */
    public static HttpUtils getInstance(){
        if(instance == null){
            synchronized (HttpUtils.class){
                if(instance == null) {
                    instance = new HttpUtils();
                    return instance;
                }
            }
        }
        return instance;
    }

    /**
     * 获取单例网络访问对象
     * @param a
     * @param <T>
     * @return
     */
    public <T> T getHttpServer(Class<T> a) {
        if (httpServer == null) {
            synchronized (HttpUtils.class) {
                if (httpServer == null) {
                    httpServer = getBuilder(ApiContants.TEST_URL).build().create(a);
                }
            }
        }
        return (T) httpServer;
    }

    /**
     * 构建retrofit
     * @param apiUrl
     * @return
     */
    private Retrofit.Builder getBuilder(String apiUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(getUnsafeOkHttpClient());
        builder.baseUrl(apiUrl);//设置远程地址
        builder.addConverterFactory(new NullOnEmptyConverterFactory());
        builder.addConverterFactory(GsonConverterFactory.create(getGson()));
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return builder;
    }

    public void init(Context context, boolean debug) {
        this.context = context;
        this.debug = debug;
        HttpHead.init(context);
    }

    public OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }};
            Cache cache = new Cache(new File(App.getInstance().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);
            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder okBuilder = new OkHttpClient.Builder().cache(cache)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    //.addInterceptor(mRewriteCacheControlInterceptor)
                    //.addNetworkInterceptor(mRewriteCacheControlInterceptor)
                    .addInterceptor(getInterceptor());

            //okBuilder.sslSocketFactory(sslSocketFactory);
            okBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
//                    Log.d("HttpUtils", "==come");
                    return true;
                }
            });

            return okBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    class HttpHeadInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

           // builder.addHeader("Accept", "application/json");

//            if (!NetUtil.isNetworkAvailable()) {
//                request = request.newBuilder()
//                        .cacheControl(CacheControl.FORCE_CACHE)
//                        .build();
//                LogUtils.d("no network");
//            }

            //Request.Builder builder = request.newBuilder();

//            if (CheckNetwork.isNetworkConnected(context)) {
//                int maxAge = 60;
//                builder.addHeader("Cache-Control", "public, max-age=" + maxAge);
//            } else {
//                int maxStale = 60 * 60 * 24 * 28;
//                builder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
//            }

            // 可添加token
//            if (listener != null) {
//                builder.addHeader("token", listener.getToken());
//            }
//            String peerid;
//            if(App.getAppInfo() == null){
//                peerid = "";
//            }else{
//                peerid = App.getAppInfo().getPeerid();
//            }
//
//            // 添加新的参数
//            HttpUrl.Builder queryParameterBulider = request.url()
//                    .newBuilder()
//                    .scheme(request.url().scheme())
//                    .host(request.url().host())
//                    .addQueryParameter(PeeridBean.KEY_PEERID, peerid);

//             如有需要，添加请求头
            //builder.addHeader("Origin",ApiContants.HEAD_URL);
//            builder.method(request.method(),request.body())
//                    .url(queryParameterBulider.build());

            Response originalResponse = chain.proceed(request);
            if (NetUtil.isNetworkAvailable()) {
                //有网的时候读接口上的@Headers里的配置，可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                LogUtils.i("cacheControl",cacheControl);
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }

           // return chain.proceed(builder.build());
        }
    }


    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtil.isNetworkAvailable()) {

                //设置缓存时间为60秒
                CacheControl cacheControl = new CacheControl.Builder()
                        .onlyIfCached()
                        .maxAge(60, TimeUnit.SECONDS)
                        .maxStale(60,TimeUnit.SECONDS)
                        .build();
                //无限缓存  CacheControl.FORCE_CACHE
                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
                LogUtils.d("no network");
            }
            Response originalResponse = chain.proceed(request);
            if (NetUtil.isNetworkAvailable()) {
                //有网的时候读接口上的@Headers里的配置，可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                LogUtils.i("cacheControl",cacheControl+"666");
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };


    class NullOnEmptyConverterFactory extends Converter.Factory{
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);

            return new Converter<ResponseBody, Object>() {
                @Override
                public Object convert(ResponseBody value) throws IOException {
                    if (value.contentLength() == 0)
                        return null;
                    return delegate.convert(value);
                }
            };
        }
    }

    private HttpLoggingInterceptor getInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        if (debug) {
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 测试
//        } else {
//            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE); // 打包
//        }
        //日志显示级别  basic body
        HttpLoggingInterceptor.Level level= HttpLoggingInterceptor.Level.BASIC;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.i("OkHttp====:"+message);
            }

        });
        loggingInterceptor.setLevel(level);

        return loggingInterceptor;
    }

    private Gson getGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.setLenient();
            builder.setFieldNamingStrategy(new AnnotateNaming());
            builder.serializeNulls();
            gson = builder.create();
        }
        return gson;
    }

    private static class AnnotateNaming  implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            ParamNames a = field.getAnnotation(ParamNames.class);
            return a != null ? a.value() : FieldNamingPolicy.IDENTITY.translateName(field);
        }
    }

}
