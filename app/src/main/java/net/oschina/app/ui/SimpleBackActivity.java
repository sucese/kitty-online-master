package net.oschina.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.oschina.app.R;
import net.oschina.app.base.BaseActivity;
import net.oschina.app.base.BaseFragment;
import net.oschina.app.bean.SimpleBackPage;
import net.oschina.app.emoji.OnSendClickListener;
import net.oschina.app.ui.fragment.MessageDetailFragment;
import net.oschina.app.ui.fragment.TweetPubFragment;
import net.oschina.app.ui.fragment.TweetsFragment;
import net.oschina.app.util.UIHelper;

import java.lang.ref.WeakReference;

public class SimpleBackActivity extends BaseActivity implements
        OnSendClickListener {

    public final static String BUNDLE_KEY_PAGE = "BUNDLE_KEY_PAGE";
    public final static String BUNDLE_KEY_ARGS = "BUNDLE_KEY_ARGS";
    private static final String TAG = "FLAG_TAG";
    protected WeakReference<Fragment> mFragment;
    protected int mPageValue = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (mPageValue == -1) {
            mPageValue = getIntent().getIntExtra(BUNDLE_KEY_PAGE, 0);
        }
        initFromIntent(mPageValue, getIntent());
    }

    protected void initFromIntent(int pageValue, Intent data) {
        if (data == null) {
            throw new RuntimeException(
                    "you must provide a page info to display");
        }
        SimpleBackPage page = SimpleBackPage.getPageByValue(pageValue);
        if (page == null) {
            throw new IllegalArgumentException("can not find page by value:"
                    + pageValue);
        }

        setActionBarTitle(page.getTitle());

        try {
            Fragment fragment = (Fragment) page.getClz().newInstance();

            Bundle args = data.getBundleExtra(BUNDLE_KEY_ARGS);
            if (args != null) {
                fragment.setArguments(args);
            }

            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.container, fragment, TAG);
            trans.commitAllowingStateLoss();

            mFragment = new WeakReference<Fragment>(fragment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    "generate fragment error. by value:" + pageValue);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFragment.get() instanceof TweetsFragment) {
            setActionBarTitle("话题");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.public_menu_send:
                if (mFragment.get() instanceof TweetsFragment) {
                    sendTopic();
                } else {
                    return super.onOptionsItemSelected(item);
                }
                break;
            case R.id.chat_friend_home:
                if (mFragment.get() instanceof MessageDetailFragment) {
                    ((MessageDetailFragment)mFragment.get()).showFriendUserCenter();
                } else {
                    return super.onOptionsItemSelected(item);
                }
                break;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mFragment.get() instanceof TweetsFragment) {
            getMenuInflater().inflate(R.menu.pub_topic_menu, menu);
        }else if (mFragment.get() instanceof MessageDetailFragment){
            getMenuInflater().inflate(R.menu.chat_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 发送话题
     */
    private void sendTopic() {
        Bundle bundle = new Bundle();
        bundle.putInt(TweetPubFragment.ACTION_TYPE,
                TweetPubFragment.ACTION_TYPE_TOPIC);
        bundle.putString("tweet_topic", "#"
                + ((TweetsFragment) mFragment.get()).getTopic() + "# ");
        UIHelper.showTweetActivity(this, SimpleBackPage.TWEET_PUB, bundle);
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null && mFragment.get() != null
                && mFragment.get() instanceof BaseFragment) {
            BaseFragment bf = (BaseFragment) mFragment.get();
            if (!bf.onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.ACTION_DOWN
                && mFragment.get() instanceof BaseFragment) {
            ((BaseFragment) mFragment.get()).onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void initView() {}

    @Override
    public void initData() {}

    @Override
    public void onClickSendButton(Editable str) {
        if (mFragment.get() instanceof MessageDetailFragment) {
            ((OnSendClickListener) mFragment.get()).onClickSendButton(str);
            ((MessageDetailFragment) mFragment.get()).emojiFragment.clean();
        }
    }

    @Override
    public void onClickFlagButton() {}
}
