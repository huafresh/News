package com.example.hua.framework.tools.avatar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.hua.framework.interfaces.IWindow;
import com.example.hua.framework.tools.permission.PermissionHelper;
import com.example.hua.framework.tools.permission.PermissionListener;
import com.example.hua.framework.utils.FileUtil;
import com.example.hua.framework.utils.MLog;
import com.example.hua.framework.wrapper.popupwindow.CommonPopupWindow;

import java.io.File;

import static android.app.Activity.RESULT_OK;


/**
 * 不少应用，如果涉及到账户系统，都难免会涉及到用户头像这一块。这一块的业务逻辑大致是：用户点击头像
 * -> 弹出对话框让用户选择是使用相机拍照，还是使用相册选择照片-> 打开系统相机或相册-> 调起系统的图片
 * 裁剪-> 得到裁剪结果，上传服务器，更新UI。
 * <p>
 * 本类提供了一些方法可以用来简化上述中的某些步骤，具体方法有：
 * 1、弹出对话框
 * 对话框的弹出可以调用{@link #popupChangeAvatar}方法。
 * 如果不指定弹出的UI，则默认使用的是{@link BaseAvatarContent}
 * 2、打开系统相机：{@link #openCamera()}
 * 3、打开系统相册：{@link #openAlbum()}
 * 4、打开系统裁剪：{@link #openCropImage(Uri)}，默认自动调用
 *
 * @author hua
 * @date 2017/7/13
 */

public class AvatarHelper implements IWindow.IContainer {

    private static final String TAG = "AvatarHelper";

    /** 打开系统相机的请求码 */
    public static final int ACTION_CAMERA_REQUEST_CODE = 1;
    /** 打开系统相册的请求码 */
    public static final int ACTION_ALBUM_REQUEST_CODE = 2;
    /** 打开系统图片裁剪的请求码 */
    public static final int ACTION_CROP_REQUEST_CODE = 3;

    /** 拍照后的原始图片的存储相对路径 */
    public final String TEMP_FILE_RELATIVE_PATH = "/cache/camera_temp_file.jpg";

    /** 裁剪后的图片存储的相对路径 */
    public final String CROP_FILE_PATH = "/cache/crop_file.jpg";

    private final Activity mActivity;

    /** 拍照后的照片存放路径 */
    private String mPicPath;

    /** 相册返回uri */
    private Uri mAlbumUri;

    /** 裁剪后的图片存放路径 */
    private String mCropPath;

    /** 选择图片选择方式的弹出框的内容 */
    private IWindow.IContentView mContentView;

    /** 裁剪图片默认的宽和高 */
    public final int DEFAULT_WIDTH = 150;
    public final int DEFAULT_HEIGHT = 150;
    private CommonPopupWindow mPopupWindow;

    /** 是否在选择图片后进行裁剪 */
    private boolean isEnableCrop = true;

    /** 裁剪结束监听 */
    private OnCropOverListener mCropOverListener;

    public AvatarHelper(Activity activity) {
        mActivity = activity;
    }


    public void popupChangeAvatar() {
        popupChangeAvatar(null);
    }

    /**
     * 底部以popupWindow的形式弹出头像选择对话框
     *
     * @param contentView 对话框的内容。传null时使用默认UI。
     */
    public void popupChangeAvatar(IWindow.IContentView contentView) {
        if (contentView == null) {
            addContentView(new BaseAvatarContent());
        } else {
            addContentView(contentView);
        }
    }

    @Override
    public void addContentView(IWindow.IContentView contentView) {
        mContentView = contentView;
        mContentView.onAttachToContainer(this);

        mPopupWindow = CommonPopupWindow.get(mActivity)
                .setContent(contentView)
                .show();
    }

    @Override
    public void removeContentView(IWindow.IContentView contentView) {
        mContentView = null;
        mContentView.onDetachContainer(this);

        mPopupWindow.dismiss();
    }

    /**
     * 打开系统应用的回调方法
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        返回的数据
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTION_CAMERA_REQUEST_CODE:
                    //相机返回
                    onGetImageFromCamera(resultCode, data);
                    break;
                case ACTION_ALBUM_REQUEST_CODE:
                    //相册返回
                    onGetImageFromAlbum(resultCode, data);
                    break;
                case ACTION_CROP_REQUEST_CODE:
                    //照片裁剪
                    if (mCropOverListener != null) {
                        mCropOverListener.onCropOver(resultCode, data);
                    }
                    break;
                default:
                    break;
            }
        } else {
            MLog.e(TAG + "调起系统应用返回错误结果，resultCode = " + requestCode);
        }
    }

    private void onGetImageFromAlbum(int resultCode, Intent data) {
        mAlbumUri = data.getData();
        openCropImage(Uri.parse("file:///" + getPathFromUri(mActivity, mAlbumUri)));
    }

    private void onGetImageFromCamera(int resultCode, Intent data) {
        openCropImage(Uri.parse("file:///" + getCameraResultPath()));
    }


    /**
     * 权限申请回调
     *
     * @param requestCode  请求码
     * @param permissions  申请的权限
     * @param grantResults 权限申请结果
     */
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 打开系统相机。
     * <p>
     * 必须重写Activity的{@link Activity#onActivityResult(int, int, Intent)}方法并调用本类的
     * {@link #onActivityResult(int, int, Intent)}方法，以便本类能拿到相机的回调
     * 6.0及以上系统，需要动态申请权限：
     * {@link Manifest.permission#CAMERA}和{@link Manifest.permission#WRITE_EXTERNAL_STORAGE}
     * <p>
     * 本方法默认使用{@link PermissionHelper}权限申请帮助类来进行权限申请，因此需要重写
     * Activity的{@link Activity#onRequestPermissionsResult}方法，并调用本类的
     * {@link #onRequestPermissionsResult}方法。
     * <p>
     * 相机拍照结束后，可以通过{@link #getCameraResultPath()}方法获取照片存储的路径。
     *
     * @see {@link PermissionHelper}
     */
    public void openCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionHelper.get(mActivity).requestPermissions(new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                @Override
                public void onGranted(String[] permissions) {
                    doOpenCamera();
                }

                @Override
                public void onDenied(String[] permissions) {
                    Toast.makeText(mActivity, "很抱歉，打开相机失败，原因是没有允许相关权限",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            doOpenCamera();
        }
    }

    private void doOpenCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String tempFilePath = mActivity.getExternalFilesDir(null) + TEMP_FILE_RELATIVE_PATH;
        if (TextUtils.isEmpty(tempFilePath)) {
            tempFilePath = mActivity.getFilesDir() + TEMP_FILE_RELATIVE_PATH;
        }
        File result = FileUtil.getFile(tempFilePath);
        if (result != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(result));
            mActivity.startActivityForResult(intent, ACTION_CAMERA_REQUEST_CODE);
            mPicPath = result.getAbsolutePath();
        } else {
            MLog.e("相机打开失败，原因是创建存储拍照结果的文件失败");
        }
    }

    /**
     * 获取相机拍照后的原始照片存储路径
     *
     * @return 路径
     */
    public String getCameraResultPath() {
        return mPicPath;
    }

    /**
     * 打开系统相册。
     * <p>
     * 必须重写Activity的{@link Activity#onActivityResult(int, int, Intent)}方法并调用本类的
     * {@link #onActivityResult(int, int, Intent)}方法，以便本类能拿到相册的回调
     * 6.0及以上系统，需要动态申请权限：
     * {@link Manifest.permission#WRITE_EXTERNAL_STORAGE}
     * <p>
     * 本方法默认使用{@link PermissionHelper}权限申请帮助类来进行权限申请，因此需要重写
     * Activity的{@link Activity#onRequestPermissionsResult}方法，并调用本类的
     * {@link #onRequestPermissionsResult}方法。
     * <p>
     * 相册选择图片后，可以通过{@link #getAlbumUri()}方法获取照片存储的Uri。
     *
     * @see {@link PermissionHelper}
     */
    public void openAlbum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionHelper.get(mActivity).requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    new PermissionListener() {
                        @Override
                        public void onGranted(String[] permissions) {
                            doOpenAlbum();
                        }

                        @Override
                        public void onDenied(String[] permissions) {
                            Toast.makeText(mActivity, "很抱歉，打开相册失败，原因是没有允许相关权限",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            doOpenAlbum();
        }
    }

    private void doOpenAlbum() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            intent.setType("image/*");
            mActivity.startActivityForResult(intent, ACTION_ALBUM_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();

    /* *
     * 以下代码不理解，注释掉
     */

//            try {
//                Intent intent = new Intent(Intent.ACTION_PICK, null);
//                intent.setDataAndType(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                mActivity.startmActivityForResult(intent, ACTION_ALBUM_REQUEST_CODE);
//            } catch (Exception e2) {
//                // TODO: handle exception
//                e.printStackTrace();
//            }
        }
    }

    /**
     * 获取相册返回的Uri
     *
     * @return 相册返回的Uri
     */
    public Uri getAlbumUri() {
        return mAlbumUri;
    }

    /**
     * 设置是否使能裁剪
     *
     * @param isEnable 是否使能裁剪
     */
    public void setEnableCrop(boolean isEnable) {
        isEnableCrop = isEnable;
    }

    public void setOnCropImageOverListerner(OnCropOverListener listerner) {
        mCropOverListener = listerner;
    }

    /**
     * 打开系统裁剪。
     * <p>
     * 必须重写Activity的{@link Activity#onActivityResult(int, int, Intent)}方法并调用本类的
     * {@link #onActivityResult(int, int, Intent)}方法，以便本类能拿到裁剪的回调
     * 6.0及以上系统，需要动态申请权限：
     * {@link Manifest.permission#WRITE_EXTERNAL_STORAGE}
     * <p>
     * 本方法默认使用{@link PermissionHelper}权限申请帮助类来进行权限申请，因此需要重写
     * Activity的{@link Activity#onRequestPermissionsResult}方法，并调用本类的
     * {@link #onRequestPermissionsResult}方法。
     * <p>
     * 裁剪结束后，可以通过{@link #getCropResultPath}方法获取图片存储的路径。
     *
     * @param orgUri 需要被裁剪的图片的uri
     * @see {@link PermissionHelper}
     */
    public void openCropImage(final Uri orgUri) {
        if (orgUri == null || !isEnableCrop) {
            MLog.e("orgUri为null，调起系统剪裁失败，");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionHelper.get(mActivity).requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    new PermissionListener() {
                        @Override
                        public void onGranted(String[] permissions) {
                            doCropImage(orgUri);
                        }

                        @Override
                        public void onDenied(String[] permissions) {
                            Toast.makeText(mActivity, "很抱歉，打开系统裁剪失败，原因是没有允许相关权限",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            doCropImage(orgUri);
        }
    }

    private void doCropImage(Uri orgUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(orgUri, "image/*");
        // 下面这个crop = true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", DEFAULT_WIDTH);
        intent.putExtra("outputY", DEFAULT_HEIGHT);
        intent.putExtra("scale", true);
        //将剪切的图片保存到目标Uri中
        Context context = mActivity.getApplicationContext();
        String tempFilePath = context.getExternalFilesDir(null) + CROP_FILE_PATH;
        if (TextUtils.isEmpty(tempFilePath)) {
            tempFilePath = context.getFilesDir() + CROP_FILE_PATH;
        }
        File result = FileUtil.getFile(tempFilePath);
        if (result != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(result));
            mCropPath = result.getAbsolutePath();
            //是否将数据保留在Bitmap中返回
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            mActivity.startActivityForResult(intent, ACTION_CROP_REQUEST_CODE);
        } else {
            MLog.e("图片裁剪打开失败，原因是创建存储裁剪结果的文件失败");
        }
    }

    /**
     * 获取照片裁剪后的存储路径
     *
     * @return 路径
     */
    public String getCropResultPath() {
        return mCropPath;
    }

    /**
     * 安卓4.4及以上版本，相册返回的Uri是"content:\\...."，4.4以下是"file:\\...."。
     * 而系统的图片裁剪却只识别"file:\\..."类型的Uri。因此需要调用此方法得到图片的绝对路径。
     * <p>
     * 实际上，使用ACTION_GET_CONTENT这个action获取相关的资源时，都会有上述的问题
     *
     * @param context
     * @param uri     需要解析的Uri
     * @return 解析uri后得到的图片路径
     */
    @SuppressLint("NewApi")
    public String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                final String primary = "primary";
                if (primary.equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);

                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                final String image = "image";
                final String video = "video";
                final String audio = "audio";
                if (image.equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else {
                    if (video.equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else {
                        if (audio.equals(type)) {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }
                    }
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else {
            final String content = "content";
            if (content.equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else {
                final String file = "file";
                if (file.equalsIgnoreCase(uri.getScheme())) {
                    return uri.getPath();
                }
            }
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int columnIndex = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
