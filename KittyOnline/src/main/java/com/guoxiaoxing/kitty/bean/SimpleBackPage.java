package com.guoxiaoxing.kitty.bean;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.ui.fragment.AboutOSCFragment;
import com.guoxiaoxing.kitty.ui.fragment.ActiveFragment;
import com.guoxiaoxing.kitty.ui.fragment.BrowserFragment;
import com.guoxiaoxing.kitty.ui.fragment.CommentFrament;
import com.guoxiaoxing.kitty.ui.fragment.EventAppliesFragment;
import com.guoxiaoxing.kitty.ui.fragment.EventFragment;
import com.guoxiaoxing.kitty.ui.fragment.FeedBackFragment;
import com.guoxiaoxing.kitty.ui.fragment.MessageDetailFragment;
import com.guoxiaoxing.kitty.ui.fragment.MineFragment;
import com.guoxiaoxing.kitty.ui.fragment.MineFragmentDetail;
import com.guoxiaoxing.kitty.ui.fragment.QuestionTagFragment;
import com.guoxiaoxing.kitty.ui.fragment.SettingsFragment;
import com.guoxiaoxing.kitty.ui.fragment.SettingsNotificationFragment;
import com.guoxiaoxing.kitty.ui.fragment.SoftWareTweetsFrament;
import com.guoxiaoxing.kitty.ui.fragment.TweetLikeUsersFragment;
import com.guoxiaoxing.kitty.ui.fragment.TweetPubFragment;
import com.guoxiaoxing.kitty.ui.fragment.TweetRecordFragment;
import com.guoxiaoxing.kitty.ui.fragment.TweetsFragment;
import com.guoxiaoxing.kitty.ui.fragment.UserBlogFragment;
import com.guoxiaoxing.kitty.ui.fragment.UserCenterFragment;
import com.guoxiaoxing.kitty.team.fragment.NoteBookFragment;
import com.guoxiaoxing.kitty.team.fragment.NoteEditFragment;
import com.guoxiaoxing.kitty.team.fragment.TeamActiveFragment;
import com.guoxiaoxing.kitty.team.fragment.TeamDiaryDetailFragment;
import com.guoxiaoxing.kitty.team.fragment.TeamDiscussFragment;
import com.guoxiaoxing.kitty.team.fragment.TeamIssueFragment;
import com.guoxiaoxing.kitty.team.fragment.TeamMemberInformationFragment;
import com.guoxiaoxing.kitty.team.fragment.TeamProjectFragment;
import com.guoxiaoxing.kitty.team.fragment.TeamProjectMemberSelectFragment;
import com.guoxiaoxing.kitty.team.viewpagefragment.MyIssuePagerfragment;
import com.guoxiaoxing.kitty.team.viewpagefragment.TeamDiaryFragment;
import com.guoxiaoxing.kitty.team.viewpagefragment.TeamIssueViewPageFragment;
import com.guoxiaoxing.kitty.team.viewpagefragment.TeamProjectViewPagerFragment;
import com.guoxiaoxing.kitty.viewpagerfragment.BlogViewPagerFragment;
import com.guoxiaoxing.kitty.viewpagerfragment.EventViewPagerFragment;
import com.guoxiaoxing.kitty.viewpagerfragment.FriendsViewPagerFragment;
import com.guoxiaoxing.kitty.viewpagerfragment.NoticeViewPagerFragment;
import com.guoxiaoxing.kitty.viewpagerfragment.OpensourceSoftwareFragment;
import com.guoxiaoxing.kitty.viewpagerfragment.QuestViewPagerFragment;
import com.guoxiaoxing.kitty.viewpagerfragment.SearchViewPageFragment;
import com.guoxiaoxing.kitty.viewpagerfragment.UserFavoriteViewPagerFragment;

public enum SimpleBackPage {

    COMMENT(1, R.string.actionbar_title_comment, CommentFrament.class),

    QUEST(2, R.string.actionbar_title_questions, QuestViewPagerFragment.class),

    TWEET_PUB(3, R.string.actionbar_title_tweetpub, TweetPubFragment.class),

    SOFTWARE_TWEETS(4, R.string.actionbar_title_softtweet,
            SoftWareTweetsFrament.class),

    USER_CENTER(5, R.string.actionbar_title_user_center,
            UserCenterFragment.class),

    USER_BLOG(6, R.string.actionbar_title_user_blog, UserBlogFragment.class),

    MY_INFORMATION(7, R.string.actionbar_title_my_information,
            MineFragment.class),

    MY_ACTIVE(8, R.string.actionbar_title_active, ActiveFragment.class),

    MY_MES(9, R.string.actionbar_title_mes, NoticeViewPagerFragment.class),

    OPENSOURCE_SOFTWARE(10, R.string.actionbar_title_softwarelist,
            OpensourceSoftwareFragment.class),

    MY_FRIENDS(11, R.string.actionbar_title_my_friends,
            FriendsViewPagerFragment.class),

    QUESTION_TAG(12, R.string.actionbar_title_question,
            QuestionTagFragment.class),

    MESSAGE_DETAIL(13, R.string.actionbar_title_message_detail,
            MessageDetailFragment.class),

    USER_FAVORITE(14, R.string.actionbar_title_user_favorite,
            UserFavoriteViewPagerFragment.class),

    SETTING(15, R.string.actionbar_title_setting, SettingsFragment.class),

    SETTING_NOTIFICATION(16, R.string.actionbar_title_setting_notification,
            SettingsNotificationFragment.class),

    ABOUT_OSC(17, R.string.actionbar_title_about_osc, AboutOSCFragment.class),

    BLOG(18, R.string.actionbar_title_blog_area, BlogViewPagerFragment.class),

    RECORD(19, R.string.actionbar_title_tweetpub, TweetRecordFragment.class),

    SEARCH(20, R.string.actionbar_title_search, SearchViewPageFragment.class),

    EVENT_LIST(21, R.string.actionbar_title_event, EventViewPagerFragment.class),

    EVENT_APPLY(22, R.string.actionbar_title_event_apply,
            EventAppliesFragment.class),

    SAME_CITY(23, R.string.actionbar_title_same_city, EventFragment.class),

    NOTE(24, R.string.actionbar_title_note, NoteBookFragment.class),

    NOTE_EDIT(25, R.string.actionbar_title_noteedit, NoteEditFragment.class),

    BROWSER(26, R.string.app_name, BrowserFragment.class),

    DYNAMIC(27, R.string.team_dynamic, TeamActiveFragment.class),

    MY_INFORMATION_DETAIL(28, R.string.actionbar_title_my_information,
            MineFragmentDetail.class),

    FEED_BACK(29, R.string.str_feedback_title, FeedBackFragment.class),

    TEAM_USER_INFO(30, R.string.str_team_userinfo,
            TeamMemberInformationFragment.class),

    MY_ISSUE_PAGER(31, R.string.str_team_my_issue, MyIssuePagerfragment.class),

    TEAM_PROJECT_MAIN(32, 0, TeamProjectViewPagerFragment.class),

    TEAM_ISSUECATALOG_ISSUE_LIST(33, 0, TeamIssueFragment.class),

    TEAM_ACTIVE(34, R.string.team_actvie, TeamActiveFragment.class),

    TEAM_ISSUE(35, R.string.team_issue, TeamIssueViewPageFragment.class),

    TEAM_DISCUSS(36, R.string.team_discuss, TeamDiscussFragment.class),

    TEAM_DIRAY(37, R.string.team_diary, TeamDiaryFragment.class),

    TEAM_DIRAY_DETAIL(38, R.string.team_diary_detail, TeamDiaryDetailFragment.class),

    TEAM_PROJECT_MEMBER_SELECT(39, 0, TeamProjectMemberSelectFragment.class),

    TEAM_PROJECT(40, R.string.team_project, TeamProjectFragment.class),

    TWEET_LIKE_USER_LIST(41, 0, TweetLikeUsersFragment.class),

    TWEET_TOPIC_LIST(42, 0, TweetsFragment.class);

    private int title;
    private Class<?> clz;
    private int value;

    private SimpleBackPage(int value, int title, Class<?> clz) {
        this.value = value;
        this.title = title;
        this.clz = clz;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static SimpleBackPage getPageByValue(int val) {
        for (SimpleBackPage p : values()) {
            if (p.getValue() == val)
                return p;
        }
        return null;
    }
}
