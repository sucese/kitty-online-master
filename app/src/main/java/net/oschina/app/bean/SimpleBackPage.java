package net.oschina.app.bean;

import net.oschina.app.R;
import net.oschina.app.ui.fragment.AboutOSCFragment;
import net.oschina.app.ui.fragment.ActiveFragment;
import net.oschina.app.ui.fragment.BrowserFragment;
import net.oschina.app.ui.fragment.CommentFrament;
import net.oschina.app.ui.fragment.EventAppliesFragment;
import net.oschina.app.ui.fragment.EventFragment;
import net.oschina.app.ui.fragment.FeedBackFragment;
import net.oschina.app.ui.fragment.MessageDetailFragment;
import net.oschina.app.ui.fragment.MyInformationFragment;
import net.oschina.app.ui.fragment.MyInformationFragmentDetail;
import net.oschina.app.ui.fragment.QuestionTagFragment;
import net.oschina.app.ui.fragment.SettingsFragment;
import net.oschina.app.ui.fragment.SettingsNotificationFragment;
import net.oschina.app.ui.fragment.SoftWareTweetsFrament;
import net.oschina.app.ui.fragment.TweetLikeUsersFragment;
import net.oschina.app.ui.fragment.TweetPubFragment;
import net.oschina.app.ui.fragment.TweetRecordFragment;
import net.oschina.app.ui.fragment.TweetsFragment;
import net.oschina.app.ui.fragment.UserBlogFragment;
import net.oschina.app.ui.fragment.UserCenterFragment;
import net.oschina.app.team.fragment.NoteBookFragment;
import net.oschina.app.team.fragment.NoteEditFragment;
import net.oschina.app.team.fragment.TeamActiveFragment;
import net.oschina.app.team.fragment.TeamDiaryDetailFragment;
import net.oschina.app.team.fragment.TeamDiscussFragment;
import net.oschina.app.team.fragment.TeamIssueFragment;
import net.oschina.app.team.fragment.TeamMemberInformationFragment;
import net.oschina.app.team.fragment.TeamProjectFragment;
import net.oschina.app.team.fragment.TeamProjectMemberSelectFragment;
import net.oschina.app.team.viewpagefragment.MyIssuePagerfragment;
import net.oschina.app.team.viewpagefragment.TeamDiaryFragment;
import net.oschina.app.team.viewpagefragment.TeamIssueViewPageFragment;
import net.oschina.app.team.viewpagefragment.TeamProjectViewPagerFragment;
import net.oschina.app.viewpagerfragment.BlogViewPagerFragment;
import net.oschina.app.viewpagerfragment.EventViewPagerFragment;
import net.oschina.app.viewpagerfragment.FriendsViewPagerFragment;
import net.oschina.app.viewpagerfragment.NoticeViewPagerFragment;
import net.oschina.app.viewpagerfragment.OpensourceSoftwareFragment;
import net.oschina.app.viewpagerfragment.QuestViewPagerFragment;
import net.oschina.app.viewpagerfragment.SearchViewPageFragment;
import net.oschina.app.viewpagerfragment.UserFavoriteViewPagerFragment;

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
            MyInformationFragment.class),

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
            MyInformationFragmentDetail.class),

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
