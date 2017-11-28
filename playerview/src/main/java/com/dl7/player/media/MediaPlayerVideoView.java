package com.dl7.player.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dl7.player.R;

import java.io.IOException;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;

import static com.dl7.player.utils.StringUtils.generateTime;

/**
 * TV播放器
 * Created by Administrator on 2017/6/9.
 */

public class MediaPlayerVideoView extends FrameLayout implements View.OnClickListener,View.OnTouchListener{
    private static final String TAG = "MediaPlayerVideoView";

    /**
     * 判断是否是直播，根据视频源的长度判断
     */
    private static boolean IS_LIVE = false;

    // 进度条最大值
    private static final long MAX_VIDEO_SEEK = 1000;
    // 默认隐藏控制栏时间
    private static final int DEFAULT_HIDE_TIMEOUT = 5000;
    // 更新进度消息
    private static final int MSG_UPDATE_SEEK = 10086;

    private static final int SEEK_TIME = 10000;
    // 目标进度
    private long mTargetPosition = INVALID_VALUE;
    // 当前进度
    private long mCurPosition = INVALID_VALUE;
    // 总长度
    private long mTotalPosition = INVALID_VALUE;

    // 无效变量
    private static final int INVALID_VALUE = -1;
    // 达到文件时长的允许误差值，用来判断是否播放完成
    private static final int INTERVAL_TIME = 1000;

    //是否正在播放
    private boolean isPlaying = false;

    private boolean isPauseing = true;
    //是否已经开始播放
    private boolean isStartPlaying = false;


    //IJK播放器
    //private IjkVideoView videoView;

    private MediaPlayer mediaPlayer;

    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private int mediaPlayerPercent;

    //加载等待界面
    private RelativeLayout mLoadView;
    //头部bar
    private RelativeLayout topBar;
    //video标题
    private TextView tvTitle;
    //底部bar
    private LinearLayout bottomBar;
    //播放/暂停icon
    private ImageView ivCtrl;
    //当前时间
    private TextView tvCurTime;
    //结束时间
    private TextView tvEndTime;
    //进度条
    private SeekBar playSeekBar;
    //中间控制图标view
    private FrameLayout flCtrlView;
    //快进
    private TextView tvForward;
    //快退
    private TextView tvRewind;
    //中间播放icon
    private ImageView ivPlayIcon;

    private TextView tvProgress;

    //播放错误监听
    private OnErrorListener mListener;

    private OnCompletionListener onCompletionListener;


    private OnPreparedListener onPreparedListener;

    private Context mContext;

    private GestureDetector mGestureDetector;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_UPDATE_SEEK){
                final long pos = _setProgress(mTargetPosition);
                mCurPosition = pos;
                if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
                    // 这里会重复发送MSG，已达到实时更新 Seek 的效果
                    msg = obtainMessage(MSG_UPDATE_SEEK);
                    sendMessageDelayed(msg, 1000 - (pos % 1000));
                }
            }
        }
    };

    private boolean isTVLive;

    public MediaPlayerVideoView(@NonNull Context context){
        this(context,null);
        initView(context);
    }


    public MediaPlayerVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 初始化View
     * @param context
     */
    private void initView(Context context) {
        View.inflate(context, R.layout.layout_media_tv_player_view, this);
        mContext = context;
        //videoView = (IjkVideoView) findViewById(R.id.ijk_video_view);
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mLoadView = (RelativeLayout) findViewById(R.id.rl_wait);
        topBar = (RelativeLayout) findViewById(R.id.rl_top_bar);
        tvTitle = (TextView) findViewById(R.id.tv_video_title);
        bottomBar = (LinearLayout) findViewById(R.id.ll_bottom_bar);
        ivCtrl = (ImageView) findViewById(R.id.iv_play);
        tvCurTime = (TextView) findViewById(R.id.tv_cur_time);
        tvEndTime = (TextView) findViewById(R.id.tv_end_time);
        playSeekBar = (SeekBar) findViewById(R.id.player_seek);
        flCtrlView = (FrameLayout) findViewById(R.id.fl_touch_layout);
        tvForward = (TextView) findViewById(R.id.tv_fast_forward);
        tvRewind = (TextView) findViewById(R.id.tv_fast_rewind);
        ivPlayIcon = (ImageView) findViewById(R.id.iv_play_circle);
        tvProgress = (TextView) findViewById(R.id.tv_progress);

        super.setOnTouchListener(this);
        super.setClickable(true);
        super.setLongClickable(true);
        super.setFocusable(true);

        mGestureDetector = new GestureDetector(mContext,new MyGestureListener());
        mGestureDetector.setOnDoubleTapListener(new MyGestureListener());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (mGestureDetector.onTouchEvent(motionEvent)) {
            return true;
        }
        if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_UP) {
            _endGesture();
        }
        return false;
    }

    private class MyCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    /**
     * 初始化，必须要先调用
     *
     * @return
     */
    public MediaPlayerVideoView init() {
        _initMediaPlayer();
        return this;
    }

    public MediaPlayerVideoView setTitle(String title){
        tvTitle.setText(title);
        return this;
    }


    /**
     * 初始化IJK播放器
     */
    private void _initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        holder = surfaceView.getHolder();
        holder.addCallback(new MyCallBack());
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                mediaPlayerPercent = percent;
            }
        });

        // 进度
        playSeekBar.setMax((int)MAX_VIDEO_SEEK);
        playSeekBar.setOnSeekBarChangeListener(mSeekListener);
    }

    /**
     * 设置播放资源
     *, Map<String, String> headers
     * @param uri
     * @return
     */
    public MediaPlayerVideoView setVideoPath(Uri uri) throws IOException {
        mediaPlayer.setDataSource(getContext(),uri);
        mediaPlayer.prepareAsync();
        return this;
    }

    public MediaPlayerVideoView setVideoPath(Uri uri, Map<String, String> headers) throws Exception {
        mediaPlayer.setDataSource(getContext(),uri,headers);
        mediaPlayer.prepareAsync();
        return this;
    }



    /**
     * 开始播放
     */
    public void start(){
        mLoadView.setVisibility(VISIBLE);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mLoadView.setVisibility(GONE);
                isStartPlaying = true;
                mp.start();
                showBar();
                ivCtrl.setSelected(true);
                isPlaying = true;
                isPauseing = false;
                mHandler.sendEmptyMessage(MSG_UPDATE_SEEK);
                if(mp.getDuration() <= 0){
                    IS_LIVE = true;
                }else{
                    IS_LIVE = false;
                }
                onPreparedListener.onPrepared(mp);
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mListener.onError(mp,what,extra);
                return false;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onCompletionListener.onCompletion(mp);
            }
        });
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });

    }

    public interface OnPreparedListener{
        boolean onPrepared(MediaPlayer iMediaPlayer);
    }

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }

    public interface OnCompletionListener{
        boolean onCompletion(MediaPlayer iMediaPlayer);
    }

    public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    public interface OnErrorListener{
        boolean onError(MediaPlayer iMediaPlayer, int i, int i1);
    }

    public MediaPlayerVideoView setOnErrorListener(OnErrorListener listener){
        this.mListener = listener;
        return this;
    }

    /**
     * 是否在播放
     * @return
     */
    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    /**
     * 播放
     */
    public void play(){
        mediaPlayer.start();
        ivCtrl.setSelected(true);
        isPlaying = true;
        isPauseing = false;
        mHandler.sendEmptyMessage(MSG_UPDATE_SEEK);
    }


    /**
     * 暂停
     */
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            ivCtrl.setSelected(false);
            isPlaying = false;
            isPauseing = true;
            mediaPlayer.pause();
        }
    }

    /**
     * 停止
     */
    public void stop() {
        if(mediaPlayer != null) {
            pause();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    /**
     * 更新进度条
     *
     * @return
     */
    private long _setProgress(long targetPosition) {
        if (mediaPlayer == null) {
            return 0;
        }
        // 视频播放的当前进度
        //int position = Math.max(videoView.getCurrentPosition(), mInterruptPosition);

        long position;

        if(targetPosition != INVALID_VALUE){
            position = targetPosition;
        }else{
            position = mediaPlayer.getCurrentPosition();
        }

        // 视频总的时长
        long duration = mediaPlayer.getDuration();
        //Log.i("duration",duration+"###");

        mTotalPosition = mediaPlayer.getDuration();

        if (duration > 0) {
            // 转换为 Seek 显示的进度值
            long pos = (long) MAX_VIDEO_SEEK * position / duration;
            playSeekBar.setProgress((int) pos);

        }
        // 获取缓冲的进度百分比，并显示在 Seek 的次进度
        int percent = mediaPlayerPercent;
        playSeekBar.setSecondaryProgress(percent * 10);

        // 更新播放时间
        tvCurTime.setText(generateTime(position));
        tvEndTime.setText(generateTime(duration));
        // 返回当前播放进度
        return position;
    }

    /**
     * SeekBar监听
     */
    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        long curPosition;
        long duration;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }
            long progressL = progress;

            // 计算目标位置
            mTargetPosition = (duration * progressL) / MAX_VIDEO_SEEK;
            long deltaTime = (mTargetPosition - curPosition) / MAX_VIDEO_SEEK;

            String desc;
            // 对比当前位置来显示快进或后退
            if (mTargetPosition > curPosition) {
                desc = generateTime(mTargetPosition) + "/" + generateTime(duration) + "\n" + "+" + deltaTime + "秒";
                _setFastForward(desc);
            } else {
                desc = generateTime(mTargetPosition) + "/" + generateTime(duration) + "\n" + deltaTime + "秒";
                _setFastRewind(desc);
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            curPosition = mediaPlayer.getCurrentPosition();
            duration = mediaPlayer.getDuration();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // 视频跳转
            seekTo(mTargetPosition);
            _setProgress(mTargetPosition);
            mTargetPosition = INVALID_VALUE;

        }
    };


    @Override
    public void onClick(View view) {

    }

    /**
     * 显示上下状态栏
     */
    private void showBar(){
        if(topBar.getVisibility() == GONE){
            topBar.setVisibility(VISIBLE);
        }
        if(bottomBar.getVisibility() == GONE){
            bottomBar.setVisibility(VISIBLE);
        }
        mHandler.removeCallbacks(mHideTouchViewRunnable);
        mHandler.postDelayed(mHideTouchViewRunnable, DEFAULT_HIDE_TIMEOUT);
    }

    /**
     * 隐藏视图Runnable
     */
    private Runnable mHideTouchViewRunnable = new Runnable() {
        @Override
        public void run() {
            _hideTouchView();
        }
    };

    /**
     * 隐藏触摸视图
     */
    private void _hideTouchView() {
        if (flCtrlView.getVisibility() == View.VISIBLE) {
            tvForward.setVisibility(View.GONE);
            tvRewind.setVisibility(GONE);
            flCtrlView.setVisibility(View.GONE);
        }
        if(topBar.getVisibility() == VISIBLE){
            topBar.setVisibility(GONE);
        }
        if(bottomBar.getVisibility() == VISIBLE){
            bottomBar.setVisibility(GONE);
        }
    }

    /**
     * 设置快进
     *
     * @param time
     */
    private void _setFastForward(String time) {
        tvRewind.setVisibility(GONE);
        if(flCtrlView.getVisibility() == GONE) {
            flCtrlView.setVisibility(VISIBLE);
        }
        tvForward.setText(time);
        tvForward.setVisibility(VISIBLE);
        mHandler.removeCallbacks(mHideTouchViewRunnable);
        mHandler.postDelayed(mHideTouchViewRunnable, 2000);
    }

    /**
     * 设置快退
     *
     * @param time
     */
    private void _setFastRewind(String time) {
        tvForward.setVisibility(GONE);
        if (flCtrlView.getVisibility() == GONE) {
            flCtrlView.setVisibility(VISIBLE);
        }
        tvRewind.setText(time);
        tvRewind.setVisibility(VISIBLE);
        mHandler.removeCallbacks(mHideTouchViewRunnable);
        mHandler.postDelayed(mHideTouchViewRunnable, 2000);
    }

    /**
     * 跳转
     * @param position 位置
     */
    public void seekTo(long position) {
        mediaPlayer.seekTo((int)position);
    }

    public long getRecordPosition(){
        return mCurPosition > mTotalPosition - 1000*60*5 ? 0 : mCurPosition;
    }

    public void seekToRecord(long position){
        seekTo(position);
        _setProgress(position);
    }

    public void setTVLive(){
        this.isTVLive = true;
    }


    public void reSetPlayer(){
        //videoView.stopPlayback();
        //videoView.release(true);
        //videoView.stopBackgroundPlay();
        //videoView.setRender(1);

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(isStartPlaying && !isTVLive) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        if(!IS_LIVE) {
                            pause();
                            mTargetPosition = mCurPosition -= SEEK_TIME;
                            if(mTargetPosition <= 0){
                                mTargetPosition = 0;
                            }
                            _setFastRewind(generateTime(mTargetPosition));
                            _setProgress(mTargetPosition);
                            showBar();
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        if(!IS_LIVE) {
                            pause();
                            mTargetPosition = mCurPosition += SEEK_TIME;
                            //
                            if(mTargetPosition >= mTotalPosition){
                                mTargetPosition = mTotalPosition;
                            }
                            Log.i("key_action","down"+mTargetPosition);
                            _setFastForward(generateTime(mTargetPosition));
                            _setProgress(mTargetPosition);
                            showBar();
                        }
                        break;
                    case KeyEvent.KEYCODE_ENTER:
                        showBar();
                        if (isPlaying) {
                            pause();
                            ivPlayIcon.setVisibility(VISIBLE);
                        } else {
                            play();
                            ivPlayIcon.setVisibility(GONE);
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                        showBar();
                        if (isPlaying) {
                            pause();
                            ivPlayIcon.setVisibility(VISIBLE);
                        } else {
                            play();
                            ivPlayIcon.setVisibility(GONE);
                        }
                        break;
                }
            }

            if(event.getAction() == KeyEvent.ACTION_UP){
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        if (!IS_LIVE) {
                            play();
                            seekTo(mTargetPosition);
                            mTargetPosition = INVALID_VALUE;
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        if(!IS_LIVE) {
                            play();
                            Log.i("key_action","up"+mTargetPosition);
                            seekTo(mTargetPosition);
                            mTargetPosition = INVALID_VALUE;
                        }
                        break;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }



    /**
     * 快进或者快退滑动改变进度，这里处理触摸滑动不是拉动 SeekBar
     *
     * @param percent 拖拽百分比
     */
    private void _onProgressSlide(float percent) {
        int position = mediaPlayer.getCurrentPosition();
        long duration = mediaPlayer.getDuration();
        // 单次拖拽最大时间差为100秒或播放时长的1/2
        long deltaMax = Math.min(100 * 1000, duration / 2);
        // 计算滑动时间
        long delta = (long) (deltaMax * percent);
        // 目标位置
        mTargetPosition = delta + position;
        if (mTargetPosition > duration) {
            mTargetPosition = duration;
        } else if (mTargetPosition <= 0) {
            mTargetPosition = 0;
        }
        int deltaTime = (int) ((mTargetPosition - position) / 1000);
        String desc;
        // 对比当前位置来显示快进或后退
        if (mTargetPosition > position) {
            desc = generateTime(mTargetPosition) + "/" + generateTime(duration) + "\n" + "+" + deltaTime + "秒";
        } else {
            desc = generateTime(mTargetPosition) + "/" + generateTime(duration) + "\n" + deltaTime + "秒";
        }
        _setFastForward(desc);
    }

    /**
     * 手势结束调用
     */
    private void _endGesture() {
        if (mTargetPosition >= 0 && mTargetPosition != mediaPlayer.getCurrentPosition()) {
            // 更新视频播放进度
            seekTo(mTargetPosition);

            mTargetPosition = INVALID_VALUE;
        }
    }

    /**
     * 手势监听类
     * */
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener{

        // 是否是按下的标识，默认为其他动作，true为按下标识，false为其他动作
        private boolean isDownTouch;
        // 是否声音控制,默认为亮度控制，true为声音控制，false为亮度控制
        private boolean isVolume;
        // 是否横向滑动，默认为纵向滑动，true为横向滑动，false为纵向滑动
        private boolean isLandscape;

        public MyGestureListener() {
            super();
        }


        @Override
        public boolean onDown(MotionEvent e) {
            isDownTouch = true;
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(!isTVLive) {
                float oldx = e1.getX(), oldy = e1.getY();
                float dx = oldx - e2.getX();
                float dy = oldy - e2.getY();

                if(isDownTouch){
                    isDownTouch = false;
                    // 判断左右或上下滑动
                    isLandscape = Math.abs(distanceX) >= Math.abs(distanceY);
                    // 判断是声音或亮度控制
                    isVolume = oldx > getResources().getDisplayMetrics().widthPixels * 0.5f;

                }

                if(isLandscape){
                    _onProgressSlide(-dx / surfaceView.getWidth());
                }else{
                    float percent = dy / surfaceView.getHeight();
                    Log.d("isVolume",isVolume+","+percent);

                }

            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

    }

}
