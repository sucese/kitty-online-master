package com.guoxiaoxing.kitty.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import com.guoxiaoxing.kitty.AppContext;
import net.oschina.kitty.R;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.base.BaseFragment;
import com.guoxiaoxing.kitty.bean.MyInformation;
import com.guoxiaoxing.kitty.bean.Result;
import com.guoxiaoxing.kitty.bean.ResultBean;
import com.guoxiaoxing.kitty.bean.User;
import com.guoxiaoxing.kitty.ui.empty.EmptyLayout;
import com.guoxiaoxing.kitty.util.DialogHelp;
import com.guoxiaoxing.kitty.util.FileUtil;
import com.guoxiaoxing.kitty.util.ImageUtils;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;

import org.kymjs.kjframe.Core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 登录用户信息详情
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2015年1月6日 上午10:33:18
 */

public class MyInformationFragmentDetail extends BaseFragment {

    public static final int ACTION_TYPE_ALBUM = 0;
    public static final int ACTION_TYPE_PHOTO = 1;

    @InjectView(R.id.iv_avatar)
    ImageView mUserFace;

    @InjectView(R.id.tv_name)
    TextView mName;

    @InjectView(R.id.tv_join_time)
    TextView mJoinTime;

    @InjectView(R.id.tv_location)
    TextView mFrom;

    @InjectView(R.id.tv_development_platform)
    TextView mPlatFrom;

    @InjectView(R.id.tv_academic_focus)
    TextView mFocus;

    @InjectView(R.id.error_layout)
    EmptyLayout mErrorLayout;

    private User mUser;

    private boolean isChangeFace = false;

    private String theLarge;

    private final static int CROP = 200;

    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/OSChina/Portrait/";
    private Uri origUri;
    private Uri cropUri;
    private File protraitFile;
    private Bitmap protraitBitmap;
    private String protraitPath;

    private final AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            MyInformation  user = XmlUtils.toBean(MyInformation.class,
                    new ByteArrayInputStream(arg2));
            if (user != null && user.getUser() != null) {
                mUser = user.getUser();
                fillUI();
            } else {
                this.onFailure(arg0, arg1, arg2, null);
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(
                R.layout.fragment_my_information_detail, container, false);
        initView(view);
        initData();
        return view;
    }

    @Override
    @OnClick({R.id.iv_avatar, R.id.btn_logout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_avatar:
                showClickAvatar();
                break;
            case R.id.btn_logout:
                AppContext.getInstance().Logout();
                AppContext.showToastShort(R.string.tip_logout_success);
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    public void showClickAvatar() {
        if (mUser == null) {
            AppContext.showToast("");
            return;
        }
        DialogHelp.getSelectDialog(getActivity(), "选择操作", getResources().getStringArray(R.array.avatar_option), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    handleSelectPicture();
                } else {
                    UIHelper.showUserAvatar(getActivity(),
                            mUser.getPortrait());
                }
            }
        }).show();
    }

    private void handleSelectPicture() {
        DialogHelp.getSelectDialog(getActivity(), "选择图片", getResources().getStringArray(R.array.choose_picture), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goToSelectPicture(i);
            }
        }).show();
    }

    private void goToSelectPicture(int position) {
        switch (position) {
            case ACTION_TYPE_ALBUM:
                startImagePick();
                break;
            case ACTION_TYPE_PHOTO:
                startTakePhoto();
                break;
            default:
                break;
        }
    }

    @Override
    public void initView(View view) {
        ButterKnife.inject(this, view);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequiredData();
            }
        });

        mUserFace.setOnClickListener(this);
    }

    @Override
    public void initData() {
        sendRequiredData();
    }

    public void fillUI() {
        Core.getKJBitmap().displayWithLoadBitmap(mUserFace, mUser.getPortrait(),
                R.drawable.widget_dface);
        mName.setText(mUser.getName());
        mJoinTime.setText(StringUtils.friendly_time(mUser.getJointime()));
        mFrom.setText(mUser.getFrom());
        mPlatFrom.setText(mUser.getDevplatform());
        mFocus.setText(mUser.getExpertise());
    }

    public void sendRequiredData() {
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        OSChinaApi.getMyInformation(AppContext.getInstance().getLoginUid(),
                mHandler);
    }

    /**
     * 上传新照片
     */
    private void uploadNewPhoto() {
        showWaitDialog("正在上传头像...");

        // 获取头像缩略图
        if (!StringUtils.isEmpty(protraitPath) && protraitFile.exists()) {
            protraitBitmap = ImageUtils
                    .loadImgThumbnail(protraitPath, 200, 200);
        } else {
            AppContext.showToast("图像不存在，上传失败");
        }
        if (protraitBitmap != null) {

            try {
                OSChinaApi.updatePortrait(AppContext.getInstance()
                                .getLoginUid(), protraitFile,
                        new AsyncHttpResponseHandler() {

                            @Override
                            public void onSuccess(int arg0, Header[] arg1,
                                                  byte[] arg2) {
                                Result res = XmlUtils.toBean(ResultBean.class,
                                        new ByteArrayInputStream(arg2))
                                        .getResult();
                                if (res.OK()) {
                                    AppContext.showToast("更换成功");
                                    // 显示新头像
                                    mUserFace.setImageBitmap(protraitBitmap);
                                    isChangeFace = true;
                                } else {
                                    AppContext.showToast(res.getErrorMessage());
                                }
                            }

                            @Override
                            public void onFailure(int arg0, Header[] arg1,
                                                  byte[] arg2, Throwable arg3) {
                                AppContext.showToast("更换头像失败");
                            }

                            @Override
                            public void onFinish() {
                                hideWaitDialog();
                            }
                        });
            } catch (FileNotFoundException e) {
                AppContext.showToast("图像不存在，上传失败");
            }
        }
    }

    /**
     * 选择图片裁剪
     */
    private void startImagePick() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        }
    }

    private void startTakePhoto() {
        Intent intent;
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
        origUri = uri;

        theLarge = savePath + fileName;// 该照片的绝对路径

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    // 裁剪头像的绝对路径
    private Uri getUploadTempFile(Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            AppContext.showToast("无法保存上传的头像，请检查SD卡是否挂载");
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (StringUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(getActivity(), uri);
        }
        String ext = FileUtil.getFileFormat(thePath);
        ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "osc_crop_" + timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        protraitFile = new File(protraitPath);

        cropUri = Uri.fromFile(protraitFile);
        return this.cropUri;
    }

    /**
     * 拍照后裁剪
     *
     * @param data 原始图片
     */
    private void startActionCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output", this.getUploadTempFile(data));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP);// 输出图片大小
        intent.putExtra("outputY", CROP);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent imageReturnIntent) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
                startActionCrop(origUri);// 拍照后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                startActionCrop(imageReturnIntent.getData());// 选图后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
                uploadNewPhoto();
                break;
        }
    }
}
