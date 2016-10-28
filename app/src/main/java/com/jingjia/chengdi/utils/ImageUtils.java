package com.jingjia.chengdi.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by Administrator on 2016/9/29.
 * 用于相片设置为头像
 */
public class ImageUtils {

    public static final int REQUESTCODE_CAMERA = 0;//相机请求码
    public static final int REQUESTCODE_ALBUM = 1;//相册请求码
    public static final int REQUESTCODE_CLIP = 2;//照片剪切请求码
    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    private Context mContext;
    private Uri uri;


//    public ImageUtils(Context mContext) {
//        this.mContext = mContext;
//    }

    public Intent getOpenCameraIntent() {
        uri = Uri.fromFile(getPhotoFile());
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intentCamera;
    }

    private File getPhotoFile() {
        return new File(FileUtils.createFile(FileUtils.headImagePath), getPhotoName());
    }

    /**
     * 由headImage+系统当前时间组成
     *
     * @return
     */
    private String getPhotoName() {
        return "headImage" + System.currentTimeMillis() + ".png";
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri 图片来源
     */
    public Intent getClipPhotoIntent(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        return intent;
    }
//错误做法
//    public Bitmap getBitmap(Intent photoIntent) {
//        Bundle extras = photoIntent.getExtras();
//        Bitmap bitmap = null;
//        if (extras != null) {
//            bitmap = extras.getParcelable("data");
//            // TODO 设置图片之前 应该将之前的图片删除，将最新的图片保存
//        }
//
//        return bitmap;
//    }

    public Intent getOpenAlbumIntent() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(albumIntent, ImageUtils.REQUESTCODE_ALBUM);
        return albumIntent;
    }

    public Uri getUri() {
        return uri;
    }
}
