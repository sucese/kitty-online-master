package net.oschina.app.team.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.base.BaseFragment;
import net.oschina.app.bean.NotebookData;
import net.oschina.app.bean.SimpleBackPage;
import net.oschina.app.db.NoteDatabase;
import net.oschina.app.ui.SimpleBackActivity;
import net.oschina.app.util.DialogHelp;
import net.oschina.app.util.KJAnimations;
import net.oschina.app.util.StringUtils;
import net.oschina.app.util.UIHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 便签编辑界面
 * 
 * @author kymjs (https://github.com/kymjs)
 * 
 */
public class NoteEditFragment extends BaseFragment implements OnTouchListener {
    @InjectView(R.id.note_detail_edit)
    EditText mEtContent;
    @InjectView(R.id.note_detail_tv_date)
    TextView mTvDate;
    @InjectView(R.id.note_detail_titlebar)
    RelativeLayout mLayoutTitle;
    @InjectView(R.id.note_detail_img_thumbtack)
    ImageView mImgThumbtack;

    @InjectView(R.id.note_detail_img_button)
    ImageView mImgMenu;
    @InjectView(R.id.note_detail_menu)
    RelativeLayout mLayoutMenu;

    @InjectView(R.id.note_detail_img_green)
    ImageView mImgGreen;
    @InjectView(R.id.note_detail_img_blue)
    ImageView mImgBlue;
    @InjectView(R.id.note_detail_img_purple)
    ImageView mImgPurple;
    @InjectView(R.id.note_detail_img_yellow)
    ImageView mImgYellow;
    @InjectView(R.id.note_detail_img_red)
    ImageView mImgRed;

    private NotebookData editData;
    private NoteDatabase noteDb;
    private boolean isNewNote;
    private int whereFrom = QUICK_DIALOG;// 从哪个界面来

    public static final String NOTE_KEY = "notebook_key";
    public static final String NOTE_FROMWHERE_KEY = "fromwhere_key";
    public static final int QUICK_DIALOG = 0;
    public static final int NOTEBOOK_FRAGMENT = 1;
    public static final int NOTEBOOK_ITEM = 2;

    public static final int[] sBackGrounds = { 0xffe5fce8,// 绿色
            0xfffffdd7,// 黄色
            0xffffddde,// 红色
            0xffccf2fd,// 蓝色
            0xfff7f5f6,// 紫色
    };
    public static final int[] sTitleBackGrounds = { 0xffcef3d4,// 绿色
            0xffebe5a9,// 黄色
            0xffecc4c3,// 红色
            0xffa9d5e2,// 蓝色
            0xffddd7d9,// 紫色
    };

    public static final int[] sThumbtackImgs = { R.drawable.green,
            R.drawable.yellow, R.drawable.red, R.drawable.blue,
            R.drawable.purple };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_note_detail,
                container, false);
        ButterKnife.inject(this, rootView);
        initData();
        initView(rootView);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.note_detail_img_green:
            editData.setColor(0);
            break;
        case R.id.note_detail_img_blue:
            editData.setColor(3);
            break;
        case R.id.note_detail_img_purple:
            editData.setColor(4);
            break;
        case R.id.note_detail_img_yellow:
            editData.setColor(1);
            break;
        case R.id.note_detail_img_red:
            editData.setColor(2);
            break;
        }
        mImgThumbtack.setImageResource(sThumbtackImgs[editData.getColor()]);
        mEtContent.setBackgroundColor(sBackGrounds[editData.getColor()]);
        mLayoutTitle.setBackgroundColor(sTitleBackGrounds[editData.getColor()]);
        closeMenu();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            if (mLayoutMenu.getVisibility() == View.GONE) {
                openMenu();
            } else {
                closeMenu();
            }
        }
        return true;
    }

    @Override
    public void initData() {
        noteDb = new NoteDatabase(getActivity());
        if (editData == null) {
            editData = new NotebookData();
            editData.setContent(AppContext.getNoteDraft());
            isNewNote = true;
        }
        if (StringUtils.isEmpty(editData.getDate())) {
            editData.setDate(StringUtils.getDataTime("yyyy/MM/dd"));
        }
    }

    @Override
    public void initView(View view) {
        mImgGreen.setOnClickListener(this);
        mImgBlue.setOnClickListener(this);
        mImgPurple.setOnClickListener(this);
        mImgYellow.setOnClickListener(this);
        mImgRed.setOnClickListener(this);

        mEtContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mEtContent.setSingleLine(false);
        mEtContent.setHorizontallyScrolling(false);
        mEtContent.setText(Html.fromHtml(editData.getContent()).toString());
        mTvDate.setText(editData.getDate());

        mEtContent.setBackgroundColor(sBackGrounds[editData.getColor()]);
        mLayoutTitle.setBackgroundColor(sTitleBackGrounds[editData.getColor()]);
        mImgThumbtack.setImageResource(sThumbtackImgs[editData.getColor()]);

        mImgMenu.setOnTouchListener(this);
        mLayoutMenu.setOnTouchListener(this);
    }

    /**
     * 切换便签颜色的菜单
     */
    private void openMenu() {
        KJAnimations.openAnimation(mLayoutMenu, mImgMenu, 500);
    }

    /**
     * 切换便签颜色的菜单
     */
    private void closeMenu() {
        KJAnimations.closeAnimation(mLayoutMenu, mImgMenu, 500);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = getActivity().getIntent().getBundleExtra(
                SimpleBackActivity.BUNDLE_KEY_ARGS);
        if (bundle != null) {
            editData = (NotebookData) bundle.getSerializable(NOTE_KEY);
            whereFrom = bundle.getInt(NOTE_FROMWHERE_KEY, QUICK_DIALOG);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notebook_edit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.public_menu_send:
            if (!StringUtils.isEmpty(mEtContent.getText().toString())) {
                save();
                if (whereFrom == QUICK_DIALOG) {
                    UIHelper.showSimpleBack(getActivity(), SimpleBackPage.NOTE);
                }
            }
            getActivity().finish();
            break;
        }
        return true;
    }

    /**
     * 保存已编辑内容到数据库
     */
    private void save() {
        if (editData.getId() == 0) {
            editData.setId(-1
                    * org.kymjs.kjframe.utils.StringUtils.toInt(
                            StringUtils.getDataTime("dddHHmmss"), 0));
        }
        editData.setUnixTime(StringUtils.getDataTime("yyyy-MM-dd HH:mm:ss"));
        editData.setContent(mEtContent.getText().toString());
        noteDb.save(editData);
    }

    @Override
    public boolean onBackPressed() {
        if (isNewNote) {
            final String content = mEtContent.getText().toString();
            if (!TextUtils.isEmpty(content)) {
                DialogHelp.getConfirmDialog(getActivity(), "是否保存为草稿?", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppContext.setNoteDraft("");
                        getActivity().finish();
                    }
                }, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppContext.setNoteDraft(content);
                        getActivity().finish();
                    }
                }).show();
                return true;
            }
        }
        return super.onBackPressed();
    }
}