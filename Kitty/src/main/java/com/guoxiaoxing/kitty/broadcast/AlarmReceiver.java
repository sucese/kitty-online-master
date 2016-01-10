package com.guoxiaoxing.kitty.broadcast;

import com.guoxiaoxing.kitty.service.NoticeUtils;
import com.guoxiaoxing.kitty.util.TLog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		TLog.log("onReceive ->net.oschina.app收到定时获取消息");
		NoticeUtils.requestNotice(context);
	}
}
