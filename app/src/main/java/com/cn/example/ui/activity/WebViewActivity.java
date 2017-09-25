package com.cn.example.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cn.example.R;
import com.cn.example.utils.LogUtils;

/**
 * Created by Administrator on 2017/9/23.
 */

public class WebViewActivity extends AppCompatActivity {

    private String matchVideo = ".*\\.baidupcs\\.com.*mp4.*,.*vip\\.niubaitu\\.com.*m3u8.*,^http[s]?:((?!(mp4pa\\.com)|(ktkkt\\.com)|(api\\.svip\\.baiyug\\.cn)).)*.(mp4|m3u8)$,^http[s]?:\\/\\/[^=]+!.*mp4pa\\.com.*\\.(mp4|m3u8)[?&\\/].*,^http[s]?:\\/\\/[^=]+!.*quankan\\.tv\\/js\\/player.*[\\/\\_]+m3u8.*?,^http[s]?:\\/\\/.*video\\.duoduotv\\.bid.*,^http[s]?:\\/\\/.*\\.lecloud.*,^http[s]?:\\/\\/.*pl-ali\\.youku.*,^http[s]?:\\/\\/vhoth.dnion\\.videocdn\\.qq\\.com.*,^http[s]?:\\/\\/player\\.lsmmr\\.com\\/api\\/url\\.php\\?.*,^http[s]?:\\/\\/disp\\.titan\\.mgtv\\.com\\/vod\\.do.*,^http[s]?:\\/\\/open\\.zhenxinge.*,^http[s]?:\\/\\/vod2\\.m3u8\\.cc\\/files.*,^http[s]?:\\/\\/api\\.662820\\.com\\/iqywap\\.php.*,^http[s]?:\\/\\/vod\\.playm3u8\\.com\\/files\\/.*,^http[s]?:\\/\\/m3u8\\.30pan\\.com\\/yk1\\/.*,^http[s]?:\\/\\/ys\\.seohaochen\\.com:886\\/ykmc\\.php.*,^http[s]?:\\/\\/download\\.cloud\\.189\\.cn\\/file\\/.*,^http[s]?:\\/\\/data\\.vod\\.itc\\.cn\\/.*,^http[s]?:\\/\\/vod6\\.playm3u8\\.com\\/files\\/.*,^http[s]?:\\/\\/simple\\.m3u8\\.cc\\/files\\/.*,^http[s]?:\\/\\/boba\\.52kuyun\\.com\\/ppvod\\/.*,^http[s]?:\\/\\/www\\.yylep\\.com\\/f-17-d\\/.*,^http[s]?:\\/\\/\\d+\\.\\d+\\.\\d+\\.\\d+.*\\.(mp4|m3u8)$,^http[s]?:\\/\\/apiv2\\.dachengway\\.cn\\/.*,^https[s]?:\\/\\/api\\.47ks\\.com\\/config\\/.*,^http[s]?:\\/\\/\\d+\\.\\d+\\.\\d+\\.\\d+.*\\.(mp4|m3u8).*,^http[s]?:\\/\\/yun\\.zhaiyou\\.tv:82\\/.*,^http[s]?:\\/\\/api8\\.co.*[^js|html],^http[s]?:\\/\\/mp4\\.512wx\\.com:80\\/\\/20170912\\/KrTZPpcb\\/.*,^http[s]?:\\/\\/ksctcdn\\.inter\\.iqiyi\\.com\\/videos\\/.*,^http[s]?:\\/\\/nj02all02\\.baidupcs\\.com\\/.*,^http[s]?:\\/\\/17\\.maoyun\\.tv\\/.*,^http[s]?:\\/\\/cache\\.m\\.iqiyi\\.com\\/.*,.*bd-dy\\.com.*mp4.*,.*opentv\\.duapp\\.com\\/url\\.php\\?xml\\=.*,.*bfs\\.ri-so\\.cn\\/files\\/.*,.*lmbsy\\.qq\\.com/.*\\.mp4.*,.*newplayers\\.dongyaodx\\.com\\/Yunflv\\/parse\\.php\\?url.*,.*xiaokanba\\.com\\/webplay\\/.*,.*kan\\.dy\\.wlbgt\\.com\\/mdparse\\/url\\.php\\?xml\\=.*,.*68ss67\\.duapp\\.com\\/mdparse\\/url\\.php\\?xml\\=.*,.*p\\.wp018\\.net:88\\/mgtv\\.php\\?url\\=.*,.*www\\.kkflv\\.com\\/.*,.*vlive\\.qqvideo\\.tc\\.qq\\.com\\/.*,.*1179075\\.sqnet\\.cn\\/temp.*,.*asp\\.cntv\\.cdnpe\\.com\\/asp.*,.*gslb\\.miaopai\\.com\\/.*";

    private WebView mWebView;
    private String []matchVideoRes;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWebView = (WebView) findViewById(R.id.web_view);

        matchVideoRes = matchVideo.split(",");
        mWebView.setWebChromeClient(new WebChromeClient(){

        });
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





        mWebView.loadUrl("http://bd-dy.com/play/8807-0.htm");

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
