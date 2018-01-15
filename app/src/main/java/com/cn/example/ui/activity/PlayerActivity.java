package com.cn.example.ui.activity;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.cn.example.R;
import com.cn.example.app.App;
import com.cn.example.utils.LogUtils;
import com.cn.example.utils.ToastUtils;
import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.player.media.IjkPlayerVideoView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by Administrator on 2017/11/28.
 */

public class PlayerActivity extends AppCompatActivity implements CacheListener {


    @BindView(R.id.media_player)
    IjkPlayerVideoView mediaPlayerVideoView;

    private Uri uri;
    private String url = "http://sz-ctfs.ftn.qq.com/rkey=7a1795b4381fc8768af37862c090adde6f93b82e3c98f90d5a3eccb9b369365359fec0b27fb7a9cc8b0bc16c9a0861048dddcd360c61e8494380e216acbdbdd0";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        //uri = Uri.parse("http://cdxf-yun-ftn.weiyun.com/?ver=5087&rkey=bcbb052977077e9548373231faeaa73a67ea68bbe7fe55c9694f01903ae7dbcedda0efe335dfcfc36604ff28b1b1d2b2e5b5ceeff4bacbe287b543c88ec0e109");



        try {

            //调用系统自带的播放器
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(uri, "video/mp4");
//            startActivity(intent);

            HttpProxyCacheServer proxy = App.getProxy(this);
            proxy.registerCacheListener(this, url);
            String proxyUrl = proxy.getProxyUrl(url);
            LogUtils.d("PlayerActivity", "Use proxy url " + proxyUrl + " instead of original url " + url);

            uri = Uri.parse(proxyUrl);

            Map<String,String>headers = new HashMap<>();
            mediaPlayerVideoView.init()
                    .setTitle("test")
                    .setVideoPath(uri, headers)
                    .setOnErrorListener(new IjkPlayerVideoView.OnErrorListener() {
                        @Override
                        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                            ToastUtils.showLong("player error11");
                            PlayerActivity.this.finish();
                            return false;
                        }
                    }).start();


        }catch (Exception e){
            ToastUtils.showLong("player error22");
            PlayerActivity.this.finish();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayerVideoView.stop();
        App.getProxy(this).unregisterCacheListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mediaPlayerVideoView.configurationChanged(newConfig);
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        LogUtils.i("PlayerActivity",percentsAvailable);
    }
}
