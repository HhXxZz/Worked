package com.cn.example.ui.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.cn.example.R;
import com.cn.example.utils.ToastUtils;
import com.dl7.player.media.MediaPlayerVideoView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/28.
 */

public class PlayerActivity extends AppCompatActivity {


    @BindView(R.id.media_player)
    MediaPlayerVideoView mediaPlayerVideoView;

    private Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        //uri = Uri.parse("http://cdxf-yun-ftn.weiyun.com/?ver=5087&rkey=bcbb052977077e9548373231faeaa73a67ea68bbe7fe55c9694f01903ae7dbcedda0efe335dfcfc36604ff28b1b1d2b2e5b5ceeff4bacbe287b543c88ec0e109");

        uri = Uri.parse("http://v1.tywj.vip/ppvod/514F1EEA6FE446F501CEC4CEEEC1404E.m3u8");


        try {

            //调用系统自带的播放器
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "video/mp4");
            startActivity(intent);

//            Map<String,String>headers = new HashMap<>();
//            mediaPlayerVideoView.init()
//                    .setTitle("test")
//                    .setVideoPath(uri, headers)
//                    .setOnErrorListener(new MediaPlayerVideoView.OnErrorListener() {
//                        @Override
//                        public boolean onError(MediaPlayer iMediaPlayer, int i, int i1) {
//                            ToastUtils.showLong("player error11");
//                            PlayerActivity.this.finish();
//                            return false;
//                        }
//                    }).start();
//
//            mediaPlayerVideoView.setOnPreparedListener(new MediaPlayerVideoView.OnPreparedListener() {
//                @Override
//                public boolean onPrepared(MediaPlayer iMediaPlayer) {
//                    return false;
//                }
//            });
        }catch (Exception e){
            ToastUtils.showLong("player error22");
            PlayerActivity.this.finish();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }
}
