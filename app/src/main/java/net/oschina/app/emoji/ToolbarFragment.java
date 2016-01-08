package net.oschina.app.emoji;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.oschina.app.R;
import net.oschina.app.base.BaseFragment;

public class ToolbarFragment extends BaseFragment {

    public interface OnActionClickListener {
        public void onActionClick(ToolAction action);
    }

    public enum ToolAction {
        ACTION_CHANGE, ACTION_WRITE_COMMENT, ACTION_VIEW_COMMENT, ACTION_FAVORITE, ACTION_SHARE, ACTION_REPORT
    }

    private OnActionClickListener mActionListener;

    private View mActionWriteComment, mActionViewComment, mActionFavorite,
            mActionReport, mActionShare;

    private View mIvFavorite;
    private boolean mFavorite;

    private int mCommentCount;

    private TextView mTvCommentCount;

    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_detail_tool_bar,
                container, false);
        initView(mRootView);
        return mRootView;
    }

    public View getRootView() {
        return mRootView;
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.btn_change).setOnClickListener(this);
        mActionWriteComment = view.findViewById(R.id.write_comment_layout);
        mActionWriteComment.setOnClickListener(this);

        mActionFavorite = view.findViewById(R.id.favor_layout);
        mActionFavorite.setOnClickListener(this);

        mActionViewComment = view.findViewById(R.id.view_comment_layout);
        mActionViewComment.setOnClickListener(this);

        mActionShare = view.findViewById(R.id.repost_layout);
        mActionShare.setOnClickListener(this);

        mActionReport = view.findViewById(R.id.report_layout);
        mActionReport.setOnClickListener(this);

        mIvFavorite = view.findViewById(R.id.action_favor);
        mIvFavorite.setSelected(mFavorite);

        mTvCommentCount = (TextView) view
                .findViewById(R.id.action_comment_count);
        mTvCommentCount.setText(String.valueOf(mCommentCount));

    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        ToolAction action = null;
        if (id == R.id.btn_change) {
            action = ToolAction.ACTION_CHANGE;
        } else if (id == R.id.write_comment_layout) {
            action = ToolAction.ACTION_WRITE_COMMENT;
        } else if (id == R.id.view_comment_layout) {
            action = ToolAction.ACTION_VIEW_COMMENT;
        } else if (id == R.id.repost_layout) {
            action = ToolAction.ACTION_SHARE;
        } else if (id == R.id.report_layout) {
            action = ToolAction.ACTION_REPORT;
        } else if (id == R.id.favor_layout) {
            action = ToolAction.ACTION_FAVORITE;
        }
        if (action != null && mActionListener != null) {
            mActionListener.onActionClick(action);
        }
    }

    public void setOnActionClickListener(OnActionClickListener lis) {
        mActionListener = lis;
    }

    public void setCommentCount(int count) {
        mCommentCount = count;
        if (mTvCommentCount != null) {
            mTvCommentCount.setText(String.valueOf(mCommentCount));
            mTvCommentCount.setVisibility(mCommentCount > 0 ? View.VISIBLE
                    : View.GONE);
        }
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
        if (mIvFavorite != null) {
            mIvFavorite.setSelected(favorite);
        }
    }

    public void showReportButton() {
        mActionReport.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {}
}