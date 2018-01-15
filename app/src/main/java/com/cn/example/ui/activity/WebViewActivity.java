package com.cn.example.ui.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ClientCertRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.VideoView;

import com.cn.example.R;

/**
 * Created by Administrator on 2017/9/23.
 */

public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWebView = (WebView) findViewById(R.id.web_view);

        WebSettings webSettings = mWebView.getSettings();


        webSettings.setJavaScriptEnabled(true);

        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        webSettings.setUseWideViewPort(true);
        webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");

        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                if (view instanceof FrameLayout){
                    FrameLayout frame = (FrameLayout) view;
                    if (frame.getFocusedChild() instanceof VideoView){
                        VideoView video = (VideoView) frame.getFocusedChild();
                        frame.removeView(video);
                        setContentView(video);
                        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.stop();
                            }
                        });
                        video.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                return false;
                            }
                        });
                        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                            }
                        });
                    }
                }
            }
        });

        mWebView.loadUrl("https://www.bilibili.com/");

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if(mWebView.canGoBack()){
                mWebView.goBack();
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
