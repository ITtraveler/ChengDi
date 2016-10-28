package com.jingjia.chengdi.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/9/29.
 * sdCard的操作
 */
public class FileUtils {
    public static final String headImagePath = "/ChengDi/HeadImage";

    public static File createFile(String relativePath) {
        File file = null;
        if (sdCordExist()) {
            File sdCardir = Environment.getExternalStorageDirectory();
            try {
                file = new File(sdCardir.getCanonicalPath() + relativePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 判断文件是否存在，不存在则创建此文件
            if (!file.exists()) {
                file.mkdirs();// mkdir自能单级目录，mkdir可以多级目录
            }
        }
        return file;
    }

    /**
     * 判断是否存在sd卡
     *
     * @return
     */
    private static boolean sdCordExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
}
