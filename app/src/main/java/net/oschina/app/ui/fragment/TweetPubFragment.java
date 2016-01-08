package net.oschina.app.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.base.BaseFragment;
import net.oschina.app.bean.Tweet;
import net.oschina.app.emoji.EmojiKeyboardFragment;
import net.oschina.app.emoji.Emojicon;
import net.oschina.app.emoji.InputHelper;
import net.oschina.app.emoji.OnEmojiClickListener;
import net.oschina.app.service.ServerTaskUtils;
import net.oschina.app.ui.SelectFriendsActivity;
import net.oschina.app.util.DialogHelp;
import net.oschina.app.util.FileUtil;
import net.oschina.app.util.ImageUtils;
import net.oschina.app.util.SimpleTextWatcher;
import net.oschina.app.util.StringUtils;
import net.oschina.app.util.TDevice;
import net.oschina.app.util.UIHelper;

import org.kymjs.kjframe.Core;
import org.kymjs.kjframe.bitmap.BitmapCallBack;
import org.kymjs.kjframe.bitmap.BitmapCreate;
import org.kymjs.kjframe.bitmap.DiskImageRequest;
import org.kymjs.kjframe.http.KJAsyncTask;
import org.kymjs.kjframe.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TweetPubFragment extends BaseFragment implements
        OnEmojiClickListener {

    public static final int ACTION_TYPE_ALBUM = 0;
    public static final int ACTION_TYPE_PHOTO = 1;
    public static final int ACTION_TYPE_RECORD = 2; // 录音
    public static final int ACTION_TYPE_TOPIC = 3; // 录音
    public static final String FROM_IMAGEPAGE_KEY = "from_image_page";

    public static final String ACTION_TYPE = "action_type";

    private static final int MAX_TEXT_LENGTH = 160;
    private static final String TEXT_ATME = "@请输入用户名 ";
    private static final String TEXT_SOFTWARE = "#请输入软件名#";

    private static final int SELECT_FRIENDS_REEQUEST_CODE = 100;

    private String fromSharedTextContent = "";

    @InjectView(R.id.ib_emoji_keyboard)
    ImageButton mIbEmoji;

    @InjectView(R.id.ib_picture)
    ImageButton mIbPicture;

    @InjectView(R.id.ib_mention)
    ImageButton mIbMention;

    @InjectView(R.id.ib_trend_software)
    ImageButton mIbTrendSoftware;

    @InjectView(R.id.tv_clear)
    TextView mTvClear;

    @InjectView(R.id.rl_img)
    View mLyImage;

    @InjectView(R.id.iv_img)
    ImageView mIvImage;

    @InjectView(R.id.et_content)
    EditText mEtInput;

    private MenuItem mSendMenu;

    private boolean mIsKeyboardVisible;

    private final EmojiKeyboardFragment keyboardFragment = new EmojiKeyboardFragment();

    private String theLarge, theThumbnail;
    private File imgFile;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && msg.obj != null) {
                // 显示图片
                mIvImage.setImageBitmap((Bitmap) msg.obj);
                mLyImage.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        getActivity().getWindow().setSoftInputMode(mode);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.pub_topic_menu, menu);
        mSendMenu = menu.findItem(R.id.public_menu_send);
        updateMenuState();
    }

    public void updateMenuState() {
        if (mSendMenu == null) {
            return;
        }
        if (mEtInput.getText().length() == 0) {
            mSendMenu.setEnabled(false);
            mSendMenu.setIcon(R.drawable.actionbar_unsend_icon);
        } else {
            mSendMenu.setEnabled(true);
            mSendMenu.setIcon(R.drawable.actionbar_send_icon);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.public_menu_send:
            handleSubmit();
            break;
        default:
            break;
        }
        return true;
    }

    /**
     * 方便外部Activity调用
     */
    public void setContentText(String content) {
        fromSharedTextContent = content;
        if (mEtInput != null) {
            mEtInput.setText(content);
        }
    }

    /**
     * 方便外部Activity调用
     */
    public void setContentImage(String url) {
        handleImageFile(url);
    }

    private void handleSubmit() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_network_error);
            return;
        }
        if (!AppContext.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        String content = mEtInput.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            mEtInput.requestFocus();
            AppContext.showToastShort(R.string.tip_content_empty);
            return;
        }
        if (content.length() > MAX_TEXT_LENGTH) {
            AppContext.showToastShort(R.string.tip_content_too_long);
            return;
        }

        Tweet tweet = new Tweet();
        tweet.setAuthorid(AppContext.getInstance().getLoginUid());
        tweet.setBody(content);
        if (imgFile != null && imgFile.exists()) {
            tweet.setImageFilePath(imgFile.getAbsolutePath());
        }
        ServerTaskUtils.pubTweet(getActivity(), tweet);
        if (mIsKeyboardVisible) {
            TDevice.hideSoftKeyboard(getActivity().getCurrentFocus());
        }
        getActivity().finish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_pub, container,
                false);

        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int action_type = bundle.getInt(ACTION_TYPE, -1);
            goToSelectPicture(action_type);
            final String imgUrl = bundle.getString(FROM_IMAGEPAGE_KEY);
            handleImageUrl(imgUrl);
        }
    }

    /**
     * 处理从第三方分享跳转来的图片
     * 
     * @param filePath
     */
    private void handleImageFile(final String filePath) {
        if (!StringUtils.isEmpty(filePath)) {
            KJAsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    final Message msg = Message.obtain();
                    msg.what = 1;
                    try {
                        msg.obj = BitmapCreate.bitmapFromStream(
                                new FileInputStream(filePath), 300, 300);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    String path = FileUtils.getSDCardPath()
                            + "/OSChina/tempfile.jpg";
                    FileUtils.bitmapToFile((Bitmap) msg.obj, path);
                    imgFile = new File(path);
                    handler.sendMessage(msg);
                }
            });
        }
    }

    /**
     * 处理从图片浏览跳转来的图片
     * 
     * @param url
     */
    private void handleImageUrl(final String url) {
        if (!StringUtils.isEmpty(url)) {
            final Message msg = Message.obtain();
            msg.what = 1;
            byte[] cache = Core.getKJBitmap().getCache(url);
            msg.obj = BitmapFactory.decodeByteArray(cache, 0, cache.length);
            if (msg.obj == null) {
                DiskImageRequest req = new DiskImageRequest();
                req.load(url, 300, 300, new BitmapCallBack() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        super.onSuccess(bitmap);
                        msg.obj = bitmap;
                        String path = FileUtils.getSDCardPath()
                                + "/OSChina/tempfile.jpg";
                        handler.sendMessage(msg);
                        FileUtils.bitmapToFile((Bitmap) msg.obj, path);
                        imgFile = new File(path);
                    }
                });
            } else {
                String path = FileUtils.getSDCardPath()
                        + "/OSChina/tempfile.jpg";
                FileUtils.bitmapToFile((Bitmap) msg.obj, path);
                imgFile = new File(path);
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.inject(this, view);
        setHasOptionsMenu(true);
        mIbEmoji.setOnClickListener(this);
        mIbPicture.setOnClickListener(this);
        mIbMention.setOnClickListener(this);
        mIbTrendSoftware.setOnClickListener(this);
        mTvClear.setOnClickListener(this);
        mTvClear.setText(String.valueOf(MAX_TEXT_LENGTH));
        view.findViewById(R.id.iv_clear_img).setOnClickListener(this);

        mEtInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                mTvClear.setText((MAX_TEXT_LENGTH - s.length()) + "");
                updateMenuState();
            }
        });
        // 获取保存的tweet草稿
        mEtInput.setText(AppContext.getTweetDraft());
        mEtInput.setSelection(mEtInput.getText().toString().length());

        mEtInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                mTvClear.setText((MAX_TEXT_LENGTH - s.length()) + "");
            }
        });
        // 获取保存的tweet草稿
        String content = fromSharedTextContent;
        if (StringUtils.isEmpty(fromSharedTextContent)) {
            content = AppContext.getTweetDraft();
        }
        mEtInput.setText(content);
        mEtInput.setSelection(mEtInput.getText().toString().length());

        getFragmentManager().beginTransaction()
                .replace(R.id.emoji_keyboard_fragment, keyboardFragment)
                .commit();
        keyboardFragment.setOnEmojiClickListener(new OnEmojiClickListener() {
            @Override
            public void onEmojiClick(Emojicon v) {
                InputHelper.input2OSC(mEtInput, v);
            }

            @Override
            public void onDeleteButtonClick(View v) {
                InputHelper.backspace(mEtInput);
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        final String tweet = mEtInput.getText().toString();
        if (!TextUtils.isEmpty(tweet)) {
            DialogHelp.getConfirmDialog(getActivity(), "是否保存为草稿?", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    AppContext.setTweetDraft(tweet);
                    getActivity().finish();
                }
            }, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AppContext.setTweetDraft("");
                    getActivity().finish();
                }
            }).show();
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.ib_picture) {
            handleSelectPicture();
        } else if (id == R.id.ib_mention) {
            //insertMention();
            handleSelectFriends();
        } else if (id == R.id.ib_trend_software) {
            insertTrendSoftware();
        } else if (id == R.id.tv_clear) {
            handleClearWords();
        } else if (id == R.id.iv_clear_img) {
            mIvImage.setImageBitmap(null);
            mLyImage.setVisibility(View.GONE);
            imgFile = null;
        } else if (id == R.id.ib_emoji_keyboard) {
            if (!keyboardFragment.isShow()) {// emoji隐藏中
                keyboardFragment.showEmojiKeyBoard();
                keyboardFragment.hideSoftKeyboard();
            } else {
                keyboardFragment.hideEmojiKeyBoard();
                keyboardFragment.showSoftKeyboard(mEtInput);
            }
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
            final Intent imageReturnIntent) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if(requestCode == SELECT_FRIENDS_REEQUEST_CODE) {
            //选中好友的名字
            String names[] = imageReturnIntent.getStringArrayExtra("names");
            if(names != null && names.length > 0) {
                //拼成字符串
                String text = "";
                for(String n : names) {
                    text += "@" + n + " ";
                }
                //插入到文本中
                mEtInput.getText().insert(mEtInput.getSelectionStart(), text);
            }
            return;
        }
        new Thread() {
            private String selectedImagePath;

            @Override
            public void run() {
                Bitmap bitmap = null;

                if (requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD) {
                    if (imageReturnIntent == null)
                        return;
                    Uri selectedImageUri = imageReturnIntent.getData();
                    if (selectedImageUri != null) {
                        selectedImagePath = ImageUtils.getImagePath(
                                selectedImageUri, getActivity());
                    }

                    if (selectedImagePath != null) {
                        theLarge = selectedImagePath;
                    } else {
                        bitmap = ImageUtils.loadPicasaImageFromGalley(
                                selectedImageUri, getActivity());
                    }

                    if (AppContext
                            .isMethodsCompat(android.os.Build.VERSION_CODES.ECLAIR_MR1)) {
                        String imaName = FileUtil.getFileName(theLarge);
                        if (imaName != null)
                            bitmap = ImageUtils.loadImgThumbnail(getActivity(),
                                    imaName,
                                    MediaStore.Images.Thumbnails.MICRO_KIND);
                    }
                    if (bitmap == null && !StringUtils.isEmpty(theLarge))
                        bitmap = ImageUtils
                                .loadImgThumbnail(theLarge, 100, 100);
                } else if (requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA) {
                    // 拍摄图片
                    if (bitmap == null && !StringUtils.isEmpty(theLarge)) {
                        bitmap = ImageUtils
                                .loadImgThumbnail(theLarge, 100, 100);
                    }
                }

                if (bitmap != null) {// 存放照片的文件夹
                    String savePath = Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/OSChina/Camera/";
                    File savedir = new File(savePath);
                    if (!savedir.exists()) {
                        savedir.mkdirs();
                    }

                    String largeFileName = FileUtil.getFileName(theLarge);
                    String largeFilePath = savePath + largeFileName;
                    // 判断是否已存在缩略图
                    if (largeFileName.startsWith("thumb_")
                            && new File(largeFilePath).exists()) {
                        theThumbnail = largeFilePath;
                        imgFile = new File(theThumbnail);
                    } else {
                        // 生成上传的800宽度图片
                        String thumbFileName = "thumb_" + largeFileName;
                        theThumbnail = savePath + thumbFileName;
                        if (new File(theThumbnail).exists()) {
                            imgFile = new File(theThumbnail);
                        } else {
                            try {
                                // 压缩上传的图片
                                ImageUtils.createImageThumbnail(getActivity(),
                                        theLarge, theThumbnail, 800, 80);
                                imgFile = new File(theThumbnail);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    // 保存动弹临时图片
                    // ((AppContext) getApplication()).setProperty(
                    // tempTweetImageKey, theThumbnail);

                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                }
            };
        }.start();
    }

    private void handleClearWords() {
        if (TextUtils.isEmpty(mEtInput.getText().toString()))
            return;
        DialogHelp.getConfirmDialog(getActivity(), "是否清空内容?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mEtInput.getText().clear();
                if (mIsKeyboardVisible) {
                    TDevice.showSoftKeyboard(mEtInput);
                }
            }
        }).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        keyboardFragment.showSoftKeyboard(mEtInput);
        keyboardFragment.hideEmojiKeyBoard();
    }

    /** 跳转选择好友*/
    private void handleSelectFriends() {
        //如果没登录，则先去登录界面
        if (!AppContext.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        Intent intent = new Intent(getActivity(), SelectFriendsActivity.class);
        startActivityForResult(intent, SELECT_FRIENDS_REEQUEST_CODE);
    }

    private void handleSelectPicture() {
        DialogHelp.getSelectDialog(getActivity(), getResources().getStringArray(R.array.choose_picture), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goToSelectPicture(i);
            }
        }).show();
    }

    private void goToSelectPicture(int position) {
        switch (position) {
        case ACTION_TYPE_ALBUM:
            Intent intent;
            if (Build.VERSION.SDK_INT < 19) {
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "选择图片"),
                        ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
            } else {
                intent = new Intent(Intent.ACTION_PICK,
                        Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "选择图片"),
                        ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
            }
            break;
        case ACTION_TYPE_PHOTO:
            // 判断是否挂载了SD卡
            String savePath = "";
            String storageState = Environment.getExternalStorageState();
            if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                savePath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/oschina/Camera/";
                File savedir = new File(savePath);
                if (!savedir.exists()) {
                    savedir.mkdirs();
                }
            }

            // 没有挂载SD卡，无法保存文件
            if (StringUtils.isEmpty(savePath)) {
                AppContext.showToastShort("无法保存照片，请检查SD卡是否挂载");
                return;
            }

            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                    .format(new Date());
            String fileName = "osc_" + timeStamp + ".jpg";// 照片命名
            File out = new File(savePath, fileName);
            Uri uri = Uri.fromFile(out);

            theLarge = savePath + fileName;// 该照片的绝对路径

            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent,
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
            break;
        case ACTION_TYPE_TOPIC:
            Bundle bundle = getArguments();
            if (bundle != null) {
                String topic = bundle.getString("tweet_topic");
                setContentText(topic);
                if (mEtInput != null) {
                    mEtInput.setSelection(topic.length());
                }
            }
            break;
        default:
            break;
        }
    }

    private void insertMention() {
        TDevice.showSoftKeyboard(mEtInput);
        // 在光标所在处插入“@用户名”
        int curTextLength = mEtInput.getText().length();
        if (curTextLength >= MAX_TEXT_LENGTH)
            return;
        String atme = TEXT_ATME;
        int start, end;
        if ((MAX_TEXT_LENGTH - curTextLength) >= atme.length()) {
            start = mEtInput.getSelectionStart() + 1;
            end = start + atme.length() - 2;
        } else {
            int num = MAX_TEXT_LENGTH - curTextLength;
            if (num < atme.length()) {
                atme = atme.substring(0, num);
            }
            start = mEtInput.getSelectionStart() + 1;
            end = start + atme.length() - 1;
        }
        if (start > MAX_TEXT_LENGTH || end > MAX_TEXT_LENGTH) {
            start = MAX_TEXT_LENGTH;
            end = MAX_TEXT_LENGTH;
        }
        mEtInput.getText().insert(mEtInput.getSelectionStart(), atme);
        mEtInput.setSelection(start, end);// 设置选中文字
    }

    private void insertTrendSoftware() {
        // 在光标所在处插入“#软件名#”
        int curTextLength = mEtInput.getText().length();
        if (curTextLength >= MAX_TEXT_LENGTH)
            return;
        String software = TEXT_SOFTWARE;
        int start, end;
        if ((MAX_TEXT_LENGTH - curTextLength) >= software.length()) {
            start = mEtInput.getSelectionStart() + 1;
            end = start + software.length() - 2;
        } else {
            int num = MAX_TEXT_LENGTH - curTextLength;
            if (num < software.length()) {
                software = software.substring(0, num);
            }
            start = mEtInput.getSelectionStart() + 1;
            end = start + software.length() - 1;
        }
        if (start > MAX_TEXT_LENGTH || end > MAX_TEXT_LENGTH) {
            start = MAX_TEXT_LENGTH;
            end = MAX_TEXT_LENGTH;
        }
        mEtInput.getText().insert(mEtInput.getSelectionStart(), software);
        mEtInput.setSelection(start, end);// 设置选中文字
    }

    @Override
    public void initData() {}

    @Override
    public void onDeleteButtonClick(View v) {}

    @Override
    public void onEmojiClick(Emojicon v) {
        InputHelper.input2OSC(mEtInput, v);
    }
}
