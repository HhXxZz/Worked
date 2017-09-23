package com.cn.example.http.api;

import com.cn.example.bean.Subject;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.LifeCache;

/**
 * RxCache
 *
 * Created by Administrator on 2017/9/23.
 */

public interface CacheProviders {

    @LifeCache(duration = 1,timeUnit = TimeUnit.MINUTES)
    Observable<Subject>getGankList(Observable<Subject> observable, DynamicKey dynamicKey, EvictDynamicKey evictDynamicKey);

}
