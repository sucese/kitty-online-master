package net.oschina.app.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.api.OperationResponseHandler;
import net.oschina.app.api.remote.OSChinaApi;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.bean.Comment;
import net.oschina.app.bean.Result;
import net.oschina.app.bean.ResultBean;
import net.oschina.app.bean.Tweet;
import net.oschina.app.util.XmlUtils;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class ServerTaskService extends IntentService {
    private static final String SERVICE_NAME = "ServerTaskService";
    public static final String ACTION_PUB_BLOG_COMMENT = "net.oschina.app.ACTION_PUB_BLOG_COMMENT";
    public static final String ACTION_PUB_COMMENT = "net.oschina.app.ACTION_PUB_COMMENT";
    public static final String ACTION_PUB_POST = "net.oschina.app.ACTION_PUB_POST";
    public static final String ACTION_PUB_TWEET = "net.oschina.app.ACTION_PUB_TWEET";
    public static final String ACTION_PUB_SOFTWARE_TWEET = "net.oschina.app.ACTION_PUB_SOFTWARE_TWEET";

    public static final String KEY_ADAPTER = "adapter";

    public static final String BUNDLE_PUB_COMMENT_TASK = "BUNDLE_PUB_COMMENT_TASK";
    public static final String BUNDLE_PUB_POST_TASK = "BUNDLE_PUB_POST_TASK";
    public static final String BUNDLE_PUB_TWEET_TASK = "BUNDLE_PUB_TWEET_TASK";
    public static final String BUNDLE_PUB_SOFTWARE_TWEET_TASK = "BUNDLE_PUB_SOFTWARE_TWEET_TASK";
    public static final String KEY_SOFTID = "soft_id";

    private static final String KEY_COMMENT = "comment_";
    private static final String KEY_TWEET = "tweet_";
    private static final String KEY_SOFTWARE_TWEET = "software_tweet_";
    private static final String KEY_POST = "post_";

    public static List<String> penddingTasks = new ArrayList<String>();

    class PublicCommentResponseHandler extends OperationResponseHandler {

	public PublicCommentResponseHandler(Looper looper, Object... args) {
	    super(looper, args);
	}

	@Override
	public void onSuccess(int code, ByteArrayInputStream is, Object[] args)
		throws Exception {
	    PublicCommentTask task = (PublicCommentTask) args[0];
	    final int id = task.getId() * task.getUid();
	    ResultBean resB = XmlUtils.toBean(ResultBean.class, is);
	    Result res = resB.getResult();
	    if (res.OK()) {
		final Comment comment = resB.getComment();
		// UIHelper.sendBroadCastCommentChanged(ServerTaskService.this,
		// isBlog, task.getId(), task.getCatalog(),
		// Comment.OPT_ADD, comment);
		notifySimpleNotifycation(id,
			getString(R.string.comment_publish_success),
			getString(R.string.comment_blog),
			getString(R.string.comment_publish_success), false,
			true);
		removePenddingTask(KEY_COMMENT + id);
	    } else {
		onFailure(100, res.getErrorMessage(), args);
	    }
	}

	@Override
	public void onFailure(int code, String errorMessage, Object[] args) {
	    PublicCommentTask task = (PublicCommentTask) args[0];
	    int id = task.getId() * task.getUid();
	    notifySimpleNotifycation(id,
		    getString(R.string.comment_publish_faile),
		    getString(R.string.comment_blog),
		    code == 100 ? errorMessage
			    : getString(R.string.comment_publish_faile), false,
		    true);
	    removePenddingTask(KEY_COMMENT + id);
	}

	@Override
	public void onFinish() {
	    tryToStopServie();
	}
    }

    class PublicTweetResponseHandler extends OperationResponseHandler {

	String key = null;

	public PublicTweetResponseHandler(Looper looper, Object... args) {
	    super(looper, args);
	    key = (String) args[1];
	}

	@Override
	public void onSuccess(int code, ByteArrayInputStream is, Object[] args)
		throws Exception {
	    Tweet tweet = (Tweet) args[0];
	    final int id = tweet.getId();
	    Result res = XmlUtils.toBean(ResultBean.class, is).getResult();
	    if (res.OK()) {
		notifySimpleNotifycation(id,
			getString(R.string.tweet_publish_success),
			getString(R.string.tweet_public),
			getString(R.string.tweet_publish_success), false, true);
		new Handler().postDelayed(new Runnable() {
		    @Override
		    public void run() {
			cancellNotification(id);
		    }
		}, 3000);
		removePenddingTask(key + id);
		if (tweet.getImageFilePath() != null) {
		    File imgFile = new File(tweet.getImageFilePath());
		    if (imgFile.exists()) {
			imgFile.delete();
		    }
		}
	    } else {
		onFailure(100, res.getErrorMessage(), args);
	    }
	}

	@Override
	public void onFailure(int code, String errorMessage, Object[] args) {
	    Tweet tweet = (Tweet) args[0];
	    int id = tweet.getId();
	    notifySimpleNotifycation(id,
		    getString(R.string.tweet_publish_faile),
		    getString(R.string.tweet_public),
		    code == 100 ? errorMessage
			    : getString(R.string.tweet_publish_faile), false,
		    true);
	    removePenddingTask(key + id);
	}

	@Override
	public void onFinish() {
	    tryToStopServie();
	}
    }

    public ServerTaskService() {
	this(SERVICE_NAME);
    }

    private synchronized void tryToStopServie() {
	if (penddingTasks == null || penddingTasks.size() == 0) {
	    stopSelf();
	}
    }

    private synchronized void addPenddingTask(String key) {
	penddingTasks.add(key);
    }

    private synchronized void removePenddingTask(String key) {
	penddingTasks.remove(key);
    }

    public ServerTaskService(String name) {
	super(name);
    }

    @Override
    public void onCreate() {
	super.onCreate();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
	String action = intent.getAction();

	if (ACTION_PUB_BLOG_COMMENT.equals(action)) {
	    PublicCommentTask task = intent
		    .getParcelableExtra(BUNDLE_PUB_COMMENT_TASK);
	    if (task != null) {
		publicBlogComment(task);
	    }
	} else if (ACTION_PUB_COMMENT.equals(action)) {
	    PublicCommentTask task = intent
		    .getParcelableExtra(BUNDLE_PUB_COMMENT_TASK);
	    if (task != null) {
		publicComment(task);
	    }
	} else if (ACTION_PUB_POST.equals(action)) {
	    // Post post = intent.getParcelableExtra(BUNDLE_PUBLIC_POST_TASK);
	    // if (post != null) {
	    // publicPost(post);
	    // }
	} else if (ACTION_PUB_TWEET.equals(action)) {
	    Tweet tweet = intent.getParcelableExtra(BUNDLE_PUB_TWEET_TASK);
	    if (tweet != null) {
		pubTweet(tweet);
	    }
	} else if (ACTION_PUB_SOFTWARE_TWEET.equals(action)) {
	    Tweet tweet = intent
		    .getParcelableExtra(BUNDLE_PUB_SOFTWARE_TWEET_TASK);
	    int softid = intent.getIntExtra(KEY_SOFTID, -1);
	    if (tweet != null && softid != -1) {
		pubSoftWareTweet(tweet, softid);
	    }
	}
    }

    private void publicBlogComment(final PublicCommentTask task) {
	int id = task.getId() * task.getUid();
	addPenddingTask(KEY_COMMENT + id);

	notifySimpleNotifycation(id, getString(R.string.comment_publishing),
		getString(R.string.comment_blog),
		getString(R.string.comment_publishing), true, false);

	OSChinaApi.publicBlogComment(task.getId(), task.getUid(), task
		.getContent(), new PublicCommentResponseHandler(
		getMainLooper(), task, true));
    }

    private void publicComment(final PublicCommentTask task) {
	int id = task.getId() * task.getUid();
	addPenddingTask(KEY_COMMENT + id);

	notifySimpleNotifycation(id, getString(R.string.comment_publishing),
		getString(R.string.comment_blog),
		getString(R.string.comment_publishing), true, false);

	OSChinaApi.publicComment(task.getCatalog(), task.getId(),
		task.getUid(), task.getContent(), task.getIsPostToMyZone(),
		new PublicCommentResponseHandler(getMainLooper(), task, false));
    }

    // private void publicPost(Post post) {
    // post.setId((int) System.currentTimeMillis());
    // int id = post.getId();
    // addPenddingTask(KEY_POST + id);
    // notifySimpleNotifycation(id, getString(R.string.post_publishing),
    // getString(R.string.post_public),
    // getString(R.string.post_publishing), true, false);
    // OSChinaApi.publicPost(post, new
    // PublicPostResponseHandler(getMainLooper(),
    // post));
    // }
    //
    private void pubTweet(final Tweet tweet) {
	tweet.setId((int) System.currentTimeMillis());
	int id = tweet.getId();
	addPenddingTask(KEY_TWEET + id);
	notifySimpleNotifycation(id, getString(R.string.tweet_publishing),
		getString(R.string.tweet_public),
		getString(R.string.tweet_publishing), true, false);
	OSChinaApi.pubTweet(tweet, new PublicTweetResponseHandler(
		getMainLooper(), tweet, KEY_TWEET));
    }

    private void pubSoftWareTweet(final Tweet tweet, int softid) {
	tweet.setId((int) System.currentTimeMillis());
	int id = tweet.getId();
	addPenddingTask(KEY_SOFTWARE_TWEET + id);
	notifySimpleNotifycation(id, getString(R.string.tweet_publishing),
		getString(R.string.tweet_public),
		getString(R.string.tweet_publishing), true, false);
	OSChinaApi.pubSoftWareTweet(tweet, softid,
		new PublicTweetResponseHandler(getMainLooper(), tweet,
			KEY_SOFTWARE_TWEET));
    }

    private void notifySimpleNotifycation(int id, String ticker, String title,
	    String content, boolean ongoing, boolean autoCancel) {
	NotificationCompat.Builder builder = new NotificationCompat.Builder(
		this)
		.setTicker(ticker)
		.setContentTitle(title)
		.setContentText(content)
		.setAutoCancel(true)
		.setOngoing(false)
		.setOnlyAlertOnce(true)
		.setContentIntent(
			PendingIntent.getActivity(this, 0, new Intent(), 0))
		.setSmallIcon(R.drawable.ic_notification);

	// if (AppContext.isNotificationSoundEnable()) {
	// builder.setDefaults(Notification.DEFAULT_SOUND);
	// }

	Notification notification = builder.build();

	NotificationManagerCompat.from(this).notify(id, notification);
    }

    private void cancellNotification(int id) {
	NotificationManagerCompat.from(this).cancel(id);
    }
}
