package com.example.zcc.lxtxvideodownload.base;

import android.os.Environment;

/**
 * 作者： zcc on 2020/6/23 14:00
 * 邮箱：m15632271759_1@163.com
 */
public class Constant {
    public static final String DOWN_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/VideoDownload"; // 下载地址文件夹
    public static final String FILE_PROVIDER = "com.example.zcc.lxtxvideodownload.fileprovider"; // 下载地址文件夹
}
