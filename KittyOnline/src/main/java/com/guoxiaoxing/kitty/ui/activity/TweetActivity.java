package com.guoxiaoxing.kitty.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.guoxiaoxing.kitty.bean.SimpleBackPage;
import com.guoxiaoxing.kitty.ui.fragment.TweetPubFragment;

import java.util.ArrayList;

/**
 * 对动弹界面的一个封装，用于相应系统分享
 *
 * @author guoxiaoxing
 */
public class TweetActivity extends SimpleBackActivity {
    private TweetPubFragment currentFragment;

    public static String FROM_KEY = "image_shared_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent.getIntExtra(FROM_KEY, -1) != 1) {
            mPageValue = SimpleBackPage.TWEET_PUB.getValue();
        }
        super.onCreate(savedInstanceState);
        respondExternal(intent);
    }

    /**
     * 响应从图片分享进入的事件
     *
     * @param intent
     */
    private void respondExternal(Intent intent) {
        currentFragment = (TweetPubFragment) mFragment.get();

        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images
                // being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        String sharedTitle = intent.getStringExtra(Intent.EXTRA_TITLE);
        if (sharedText != null) {
            currentFragment.setContentText(sharedText);
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            currentFragment.setContentImage(getAbsoluteImagePath(imageUri));
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent
                .getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            currentFragment.setContentImage(getAbsoluteImagePath(imageUris
                    .get(0)));
        }
    }

    protected String getAbsoluteImagePath(Uri uri) {
        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            return cursor.getString(column_index);
        } else {
            // 如果游标为空说明获取的已经是绝对路径了
            return uri.getPath();
        }
    }
}
