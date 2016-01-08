package net.oschina.app.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import net.oschina.app.AppContext;
import net.oschina.app.bean.Tweet;

public class ServerTaskUtils {

    public static void pubTweet(Context context, Tweet tweet) {
        Intent intent = new Intent(ServerTaskService.ACTION_PUB_TWEET);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ServerTaskService.BUNDLE_PUB_TWEET_TASK, tweet);
        intent.putExtras(bundle);
        intent.setPackage(AppContext.getInstance().getPackageName());
        context.startService(intent);
    }

    public static void pubSoftWareTweet(Context context, Tweet tweet, int softid) {
        Intent intent = new Intent(ServerTaskService.ACTION_PUB_SOFTWARE_TWEET);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ServerTaskService.BUNDLE_PUB_SOFTWARE_TWEET_TASK,
                tweet);
        bundle.putInt(ServerTaskService.KEY_SOFTID, softid);
        intent.putExtras(bundle);
        intent.setPackage(AppContext.getInstance().getPackageName());
        context.startService(intent);
    }
}
