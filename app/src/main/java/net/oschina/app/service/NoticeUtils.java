package net.oschina.app.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.util.Log;

import net.oschina.app.AppConfig;
import net.oschina.app.AppContext;
import net.oschina.app.util.TLog;

import java.util.HashMap;

public class NoticeUtils {

    public static INoticeService sService = null;
    private static HashMap<Context, ServiceBinder> sConnectionMap = new HashMap<Context, ServiceBinder>();

    public static boolean bindToService(Context context) {
        return bindToService(context, null);
    }

    public static boolean bindToService(Context context,
            ServiceConnection callback) {
        context.startService(new Intent(context, NoticeService.class));
        ServiceBinder sb = new ServiceBinder(callback);
        sConnectionMap.put(context, sb);
        return context.bindService(
                (new Intent()).setClass(context, NoticeService.class), sb, 0);
    }

    public static void unbindFromService(Context context) {
        ServiceBinder sb = sConnectionMap.remove(context);
        if (sb == null) {
            Log.e("MusicUtils", "Trying to unbind for unknown Context");
            return;
        }
        context.unbindService(sb);
        if (sConnectionMap.isEmpty()) {
            // presumably there is nobody interested in the service at this
            // point,
            // so don't hang on to the ServiceConnection
            sService = null;
        }
    }

    public static void clearNotice(int type) {
        if (sService != null) {
            try {
                sService.clearNotice(AppContext.getInstance().getLoginUid(),
                        type);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void requestNotice(Context context) {
        if (sService != null) {
            try {
                TLog.log("requestNotice...");
                sService.requestNotice();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            context.sendBroadcast(new Intent(
                    NoticeService.INTENT_ACTION_REQUEST));
            TLog.log("requestNotice,service is null");
        }
    }

    public static void scheduleNotice() {
        if (sService != null) {
            try {
                sService.scheduleNotice();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ServiceBinder implements ServiceConnection {
        ServiceConnection mCallback;

        ServiceBinder(ServiceConnection callback) {
            mCallback = callback;
        }

        @Override
        public void onServiceConnected(ComponentName className,
                android.os.IBinder service) {
            sService = INoticeService.Stub.asInterface(service);
            if (mCallback != null) {
                mCallback.onServiceConnected(className, service);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            if (mCallback != null) {
                mCallback.onServiceDisconnected(className);
            }
            sService = null;
        }
    }

    public static void tryToShutDown(Context context) {
        if (AppContext.get(AppConfig.KEY_NOTIFICATION_DISABLE_WHEN_EXIT, true)) {
            context.sendBroadcast(new Intent(
                    NoticeService.INTENT_ACTION_SHUTDOWN));
        }
    }
}
