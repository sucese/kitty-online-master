package net.oschina.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;

import net.oschina.app.util.UIHelper;

import org.kymjs.kjframe.utils.FileUtils;
import org.kymjs.kjframe.utils.SystemTool;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import cz.msebera.android.httpclient.HttpException;

/**
 * 应用程序异常：用于捕获异常和提示错误信息
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @author kymjs (kymjs123@gmali.com)
 * @created 2014年9月25日 下午5:34:05
 * 
 */
@SuppressWarnings("serial")
public class AppException extends Exception implements UncaughtExceptionHandler {

    /** 定义异常类型 */
    public final static byte TYPE_NETWORK = 0x01;
    public final static byte TYPE_SOCKET = 0x02;
    public final static byte TYPE_HTTP_CODE = 0x03;
    public final static byte TYPE_HTTP_ERROR = 0x04;
    public final static byte TYPE_XML = 0x05;
    public final static byte TYPE_IO = 0x06;
    public final static byte TYPE_RUN = 0x07;
    public final static byte TYPE_JSON = 0x08;
    public final static byte TYPE_FILENOTFOUND = 0x09;

    private byte type;// 异常的类型
    // 异常的状态码，这里一般是网络请求的状态码
    private int code;

    /** 系统默认的UncaughtException处理类 */
    private AppContext mContext;

    private AppException(Context context) {
        this.mContext = (AppContext) context;
    }

    private AppException(byte type, int code, Exception excp) {
        super(excp);
        this.type = type;
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public int getType() {
        return this.type;
    }

    public static AppException http(int code) {
        return new AppException(TYPE_HTTP_CODE, code, null);
    }

    public static AppException http(Exception e) {
        return new AppException(TYPE_HTTP_ERROR, 0, e);
    }

    public static AppException socket(Exception e) {
        return new AppException(TYPE_SOCKET, 0, e);
    }

    public static AppException file(Exception e) {
        return new AppException(TYPE_FILENOTFOUND, 0, e);
    }

    // io异常
    public static AppException io(Exception e) {
        return io(e, 0);
    }

    // io异常
    public static AppException io(Exception e, int code) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            return new AppException(TYPE_NETWORK, code, e);
        } else if (e instanceof IOException) {
            return new AppException(TYPE_IO, code, e);
        }
        return run(e);
    }

    public static AppException xml(Exception e) {
        return new AppException(TYPE_XML, 0, e);
    }

    public static AppException json(Exception e) {
        return new AppException(TYPE_JSON, 0, e);
    }

    // 网络请求异常
    public static AppException network(Exception e) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            return new AppException(TYPE_NETWORK, 0, e);
        } else if (e instanceof HttpException) {
            return http(e);
        } else if (e instanceof SocketException) {
            return socket(e);
        }
        return http(e);
    }

    public static AppException run(Exception e) {
        return new AppException(TYPE_RUN, 0, e);
    }

    /**
     * 获取APP异常崩溃处理对象
     * 
     * @param context
     * @return
     */
    public static AppException getAppExceptionHandler(Context context) {
        return new AppException(context.getApplicationContext());
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex)) {
            System.exit(0);
        }
    }

    /**
     * 自定义异常处理:收集错误信息&发送错误报告
     * 
     * @param ex
     * @return true:处理了该异常信息;否则返回false
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null || mContext == null) {
            return false;
        }
        boolean success = true;
        try {
            success = saveToSDCard(ex);
        } catch (Exception e) {
        } finally {
            if (!success) {
                return false;
            } else {
                final Context context = AppManager.getAppManager()
                        .currentActivity();
                // 显示异常信息&发送报告
                new Thread() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        // 拿到未捕获的异常，
                        UIHelper.sendAppCrashReport(context);
                        Looper.loop();
                    }
                }.start();
            }
        }
        return true;
    }

    private boolean saveToSDCard(Throwable ex) throws Exception {
        boolean append = false;
        File file = FileUtils.getSaveFile("OSChina", "OSCLog.log");
        if (System.currentTimeMillis() - file.lastModified() > 5000) {
            append = true;
        }
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
                file, append)));
        // 导出发生异常的时间
        pw.println(SystemTool.getDataTime("yyyy-MM-dd-HH-mm-ss"));
        // 导出手机信息
        dumpPhoneInfo(pw);
        pw.println();
        // 导出异常的调用栈信息
        ex.printStackTrace(pw);
        pw.println();
        pw.close();
        return append;
    }

    private void dumpPhoneInfo(PrintWriter pw) throws NameNotFoundException {
        // 应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
                PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);
        pw.println();

        // android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        pw.println();

        // 手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        pw.println();

        // 手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        pw.println();

        // cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
        pw.println();
    }
}
