package com.cn.example.app;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.cn.example.utils.LogUtils;


/**
 * Created by Administrator on 2017/8/16.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = CrashHandler.class.getSimpleName();

    // 系统默认的UncaughtException处理类
    //private Thread.UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    // 程序的Context对象
    private Context mContext;

    private CrashHandler(){}

    /** 获取CrashHandler实例 ,单例模式 */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        //mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
            handleException(ex);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LogUtils.e(TAG, "error : ", e);
            }

            // 结束所有Activity
            AppManager.getAppManager().AppExit();

//            // 退出程序
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);

    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG)
                        .show();
                Looper.loop();
            }
        }.start();

        return true;
    }
}
