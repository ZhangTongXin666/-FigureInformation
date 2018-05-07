package util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by 张同心 on 2017/9/29.
 * @function 文件工具
 */

public class FileUtil {

    /**
     * 获得文件的大小
     * @param file
     * @return 文件大小
     */
    private static long getFileSize(File file){
        long size = 0;
        if (file.exists()){
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 获得文件的大小
     * @param fileDir
     * @return
     */
    public static long getFileSizes(File fileDir){
        long size = 0;
        if (fileDir.isDirectory()){
            File files[] = fileDir.listFiles();
            for (int i = 0; i<files.length; i++){
                if (files[i].isDirectory()){
                    size = size + getFileSizes(files[i]);
                }else {
                    size = size + getFileSize(files[i]);
                }
            }
        }else {
            size = getFileSize(fileDir);
        }
        return size;
    }

    /**
     * 转换文件大小
     * @param fileSize
     * @return 文件大小
     */
    public static String formatFileSize(long fileSize){
        DecimalFormat df = new DecimalFormat("#.00");
        String strFileSize = "";
        String strWrongFileSize = "0B";
        if (fileSize == 0){
            return strWrongFileSize;
        }
        else if (fileSize < 1024){
            strFileSize = df.format((double)fileSize)+"B";
        }
        else if (fileSize < 1024*1024){
            strFileSize = df.format((double)fileSize/(1024))+"KB";
        }
        else if (fileSize < 1024*1024*1024){
            strFileSize = df.format((double)fileSize/(1024*1024))+"MB";
        }
        else if (fileSize < 1024*1024*1024*1024){
            strFileSize = df.format((double)fileSize/(1024*1024*1024))+"GB";
        }
        return strFileSize;
    }

    /**
     * 删除文件夹
     * @param fileDir
     */
    public static void deleteDirFile(File fileDir){
        if (fileDir != null && fileDir.isDirectory()){
            File files[] = fileDir.listFiles();
            for (File file:files){
                if (file.exists()){
                    file.delete();
                }else if (file.isDirectory()){
                    deleteDirFile(file);
                }
            }
        }
        fileDir.delete();
    }

    public static File getCahceDirPath(Context context, String onlyName){
        String cacehDirPath= null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()){
            cacehDirPath = context.getExternalCacheDir().getPath();
        }else {
            cacehDirPath = context.getCacheDir().getPath();
        }
        return new File(cacehDirPath+File.separator+onlyName);
    }
}
