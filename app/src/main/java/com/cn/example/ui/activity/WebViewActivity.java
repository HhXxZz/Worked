package com.cn.example.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.cn.example.R;
import com.cn.example.utils.LogUtils;

/**
 * Created by Administrator on 2017/9/23.
 */

public class WebViewActivity extends AppCompatActivity {

    private String matchVideo = ".*\\.baidupcs\\.com.*mp4.*,.*vip\\.niubaitu\\.com.*m3u8.*,^http[s]?:((?!(mp4pa\\.com)|(ktkkt\\.com)|(api\\.svip\\.baiyug\\.cn)).)*.(mp4|m3u8)$,^http[s]?:\\/\\/[^=]+!.*mp4pa\\.com.*\\.(mp4|m3u8)[?&\\/].*,^http[s]?:\\/\\/[^=]+!.*quankan\\.tv\\/js\\/player.*[\\/\\_]+m3u8.*?,^http[s]?:\\/\\/.*video\\.duoduotv\\.bid.*,^http[s]?:\\/\\/.*\\.lecloud.*,^http[s]?:\\/\\/.*pl-ali\\.youku.*,^http[s]?:\\/\\/vhoth.dnion\\.videocdn\\.qq\\.com.*,^http[s]?:\\/\\/player\\.lsmmr\\.com\\/api\\/url\\.php\\?.*,^http[s]?:\\/\\/disp\\.titan\\.mgtv\\.com\\/vod\\.do.*,^http[s]?:\\/\\/open\\.zhenxinge.*,^http[s]?:\\/\\/vod2\\.m3u8\\.cc\\/files.*,^http[s]?:\\/\\/api\\.662820\\.com\\/iqywap\\.php.*,^http[s]?:\\/\\/vod\\.playm3u8\\.com\\/files\\/.*,^http[s]?:\\/\\/m3u8\\.30pan\\.com\\/yk1\\/.*,^http[s]?:\\/\\/ys\\.seohaochen\\.com:886\\/ykmc\\.php.*,^http[s]?:\\/\\/download\\.cloud\\.189\\.cn\\/file\\/.*,^http[s]?:\\/\\/data\\.vod\\.itc\\.cn\\/.*,^http[s]?:\\/\\/vod6\\.playm3u8\\.com\\/files\\/.*,^http[s]?:\\/\\/simple\\.m3u8\\.cc\\/files\\/.*,^http[s]?:\\/\\/boba\\.52kuyun\\.com\\/ppvod\\/.*,^http[s]?:\\/\\/www\\.yylep\\.com\\/f-17-d\\/.*,^http[s]?:\\/\\/\\d+\\.\\d+\\.\\d+\\.\\d+.*\\.(mp4|m3u8)$,^http[s]?:\\/\\/apiv2\\.dachengway\\.cn\\/.*,^https[s]?:\\/\\/api\\.47ks\\.com\\/config\\/.*,^http[s]?:\\/\\/\\d+\\.\\d+\\.\\d+\\.\\d+.*\\.(mp4|m3u8).*,^http[s]?:\\/\\/yun\\.zhaiyou\\.tv:82\\/.*,^http[s]?:\\/\\/api8\\.co.*[^js|html],^http[s]?:\\/\\/mp4\\.512wx\\.com:80\\/\\/20170912\\/KrTZPpcb\\/.*,^http[s]?:\\/\\/ksctcdn\\.inter\\.iqiyi\\.com\\/videos\\/.*,^http[s]?:\\/\\/nj02all02\\.baidupcs\\.com\\/.*,^http[s]?:\\/\\/17\\.maoyun\\.tv\\/.*,^http[s]?:\\/\\/cache\\.m\\.iqiyi\\.com\\/.*,.*bd-dy\\.com.*mp4.*,.*opentv\\.duapp\\.com\\/url\\.php\\?xml\\=.*,.*bfs\\.ri-so\\.cn\\/files\\/.*,.*lmbsy\\.qq\\.com/.*\\.mp4.*,.*newplayers\\.dongyaodx\\.com\\/Yunflv\\/parse\\.php\\?url.*,.*xiaokanba\\.com\\/webplay\\/.*,.*kan\\.dy\\.wlbgt\\.com\\/mdparse\\/url\\.php\\?xml\\=.*,.*68ss67\\.duapp\\.com\\/mdparse\\/url\\.php\\?xml\\=.*,.*p\\.wp018\\.net:88\\/mgtv\\.php\\?url\\=.*,.*www\\.kkflv\\.com\\/.*,.*vlive\\.qqvideo\\.tc\\.qq\\.com\\/.*,.*1179075\\.sqnet\\.cn\\/temp.*,.*asp\\.cntv\\.cdnpe\\.com\\/asp.*,.*gslb\\.miaopai\\.com\\/.*";

    private String fullVideoJS = "javascript:(function(){var site_regs={bilibili:/\\.bilibili\\.com/,acfun:/\\.acfun\\.cn/,youku:/\\.youku\\.com/,zjstv:/\\.zjstv\\.com/,sohu:/tv\\.sohu\\.com/,qqlive:/live\\.qq\\.com/,};function findVideo(doc){var video=null;var videos=doc.getElementsByTagName(\"video\");if(videos.length>0){video=videos[0];return[doc,video]}try{var frames=doc.getElementsByTagName(\"iframe\");if(frames.length>0){for(var i=0;i<frames.length;i++){doc=frames[i].contentWindow.document;video=findVideo(doc);if(video!=null){return[doc,video]}}}}catch(e){console.error(e);return null}return null}function FFVideo(){var ff_doc=null;var ff_video=null;var ff_this=this;var __init=function(){var data=findVideo(window.document);if(data!=null){ff_doc=data[0];ff_video=data[1];__compatSite();ff_this.fullscreen();ff_this.play();window.firefilm.webPlaySuccess()}else{if(/www\\.ri003\\.com/.test(location.host)){document.querySelector(\"#kt_player .fp-ui\").click();var data=findVideo(window.document);if(data!=null){ff_doc=data[0];ff_video=data[1];ff_this.fullscreen();ff_this.play();window.firefilm.webPlaySuccess()}else{window.firefilm.webPlayError()}}else{window.firefilm.webPlayError()}}};var __compatSite=function(){if(ff_video){try{var btn=null;if(site_regs.bilibili.test(location.host)){ff_doc.querySelector(\".player-icon.icon-preview\").click()}else{if(site_regs.acfun.test(location.host)){ff_doc.querySelector(\".play-icon\").click()}else{if(site_regs.youku.test(location.host)){ff_doc.querySelector(\".x-video-button\").click()}else{if(site_regs.zjstv.test(location.host)){ff_doc.querySelector(\"#player canvas\").click()}else{if(site_regs.sohu.test(location.host)){ff_video.removeAttribute(\"webkit-playsinline\");ff_video.removeAttribute(\"playsinline\");ff_video.removeAttribute(\"x-webkit-airplay\")}else{if(site_regs.qqlive.test(location.host)){ff_doc.querySelector(\"#player bigplay\").click();ff_video.removeAttribute(\"webkit-playsinline\");ff_video.removeAttribute(\"playsinline\");ff_video.removeAttribute(\"x-webkit-airplay\")}}}}}}}catch(e){console.error(e)}}};this.playOrPause=function(){if(ff_video){ff_video.paused?ff_video.play():ff_video.pause()}};this.play=function(){ff_video.play()};this.pause=function(){ff_video.pause()};this.forward=function(){if(ff_video){ff_video.currentTime+=5}};this.back=function(){if(ff_video){ff_video.currentTime-=5}};this.fullscreen=function(){if(ff_video){ff_video.requestFullscreen&&ff_video.requestFullscreen();ff_video.webkitRequestFullScreen&&ff_video.webkitRequestFullScreen()}};__init()}window.ffvideo=new FFVideo()})();";

    private WebView mWebView;
    private String []matchVideoRes;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWebView = (WebView) findViewById(R.id.web_view);

        matchVideoRes = matchVideo.split(",");

        WebSettings webSettings = mWebView.getSettings();


        webSettings.setJavaScriptEnabled(true);


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


        mWebView.loadUrl("http://bd-dy.com/play/10746-0.htm");


        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                LogUtils.i("onShowCustomView", " onShowCustomView");
                if(view instanceof FrameLayout) {
                    LogUtils.i("onShowCustomView", " FrameLayout");
                    final FrameLayout frame = (FrameLayout) view;

                    for (int i = 0; i < frame.getChildCount(); i++) {
                        LogUtils.i("onShowCustomView", " FocusedChild:" + frame.getChildAt(i));
                    }
                }
            }

            @Override
            public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
                super.onShowCustomView(view, requestedOrientation, callback);
                LogUtils.i("onShowCustomView", " onShowCustomView");
                if(view instanceof FrameLayout) {
                    LogUtils.i("onShowCustomView", " FrameLayout");
                    final FrameLayout frame = (FrameLayout) view;

                    for (int i = 0; i < frame.getChildCount(); i++) {
                        LogUtils.i("onShowCustomView", " FocusedChild:" + frame.getChildAt(i));
                    }
                }
            }
        });




        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }


            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String tempUrl = request.getUrl().toString();
                if(isMatchedUrl(matchVideoRes,tempUrl)){
                    LogUtils.i("shouldInterceptRequest",tempUrl);
                }
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl(fullVideoJS);
            }

        });


    }

    public static boolean isMatchedUrl(String []regex,String url){
        if(url != null && !url.isEmpty()){
            for(int i=0; i<regex.length; i++){
                if(url.matches(regex[i])){
                    return true;
                }
            }
        }
        return false;
    }
}
