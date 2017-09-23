package com.cn.example.http.api;

import android.os.Environment;

import com.cn.example.bean.Subject;
import com.cn.example.http.HttpUtils;
import com.cn.example.utils.LogUtils;

import java.io.File;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * Created by Administrator on 2017/9/23.
 */

public class Repository {
    public static final String  CACHE_PATH = Environment.getExternalStorageDirectory().getPath()+"/example/cache/";


    private static Repository repository;

    public static Repository getRepository(){
        if(repository == null){
            synchronized (Repository.class) {
                if(repository == null) {
                    repository = new Repository();
                }
            }
        }
        return repository;
    }

    private CacheProviders cacheProviders;
    private ServerApi serverApi;

    private File cacheFile;

    public Repository(){
        cacheFile = new File(CACHE_PATH);
        if(!cacheFile.exists()){
            if(cacheFile.mkdirs()){
                //LogUtils.i("Repository","false");
            }
        }

        cacheProviders = new RxCache.Builder()
                .persistence(cacheFile, new GsonSpeaker())
                .using(CacheProviders.class);

        serverApi = HttpUtils.getInstance().getHttpServer(ServerApi.class);

    }

    public Observable<Subject> getGankio(){
        return cacheProviders.getGankList(serverApi.getGankList(), new DynamicKey("gank"),new EvictDynamicKey(false));
    }

}
