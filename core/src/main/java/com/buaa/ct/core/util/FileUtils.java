package com.buaa.ct.core.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.buaa.ct.core.manager.RuntimeManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 文件操作工具类
 *
 * @author nieyu
 */
public class FileUtils {

    public static final int GLOBLE_BUFFER_SIZE = 5 * 1024;

    public static String printSDCardRoot() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static FileInputStream getFileInputStream(String path) {
        return getFileInputStream(new File(path));
    }

    public static FileInputStream getFileInputStream(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            // do nothing
        }
        return fis;
    }

    public static boolean closeStream(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
                return true;
            }
        } catch (IOException e) {
            // do nothing
        }
        return false;
    }

    public static InputStream makeInputBuffered(InputStream input_) {

        if ((input_ instanceof BufferedInputStream)) {
            return input_;
        }

        return new BufferedInputStream(input_, GLOBLE_BUFFER_SIZE);
    }


    // 检测文件是否存在
    public static boolean isFileAlive(String filePath) {
        return isFileExist(filePath) && getFileSize(filePath) > 0;
    }

    // 检测文件是否存在
    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.isFile() && file.exists();
    }

    // 检测目录是否存在
    public static boolean isDirectoryExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.isDirectory() && file.exists();
    }


    // 如果文件大写超过maxSize，则不写入
    public static void writeFile(String filePath, String content, boolean isAppend, long maxSize) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (file.exists() && file.length() >= maxSize) {
            return;
        }
        writeFile(filePath, content, isAppend);
    }

    public static void writeFile(String filePath, String content, boolean isAppend) {
        if (filePath == null) {
            return;
        }
        if (content == null) {
            return;
        }
        File file = new File(filePath);
        FileWriter fileWriter = null;
        try {
            if (!file.exists()) {
                makesureFileExist(file);
                // file.createNewFile();
            }
            fileWriter = new FileWriter(file, isAppend);
            fileWriter.write(content);
            fileWriter.flush();
        } catch (IOException e) {
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void makesureParentExist(File file_) {
        File parent = file_.getParentFile();
        if ((parent != null) && (!parent.exists()))
            parent.mkdirs();
    }

    public static void makesureParentExist(String filepath_) {
        makesureParentExist(new File(filepath_));
    }

    public static void makesureFileExist(File file_) {
        try {
            if (!file_.exists()) {
                makesureParentExist(file_);
                file_.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void makesureFileExist(String filePath_) {
        makesureFileExist(new File(filePath_));
    }

    public static void copy(String pathIn_, String pathOut_) throws IOException {
        copy(new File(pathIn_), new File(pathOut_));
    }

    public static void copy(File in_, File out_) throws IOException {

        makesureParentExist(out_);
        copy(new FileInputStream(in_), new FileOutputStream(out_));
    }

    public static void copy(InputStream input_, OutputStream output_)
            throws IOException {
        try {
            byte[] buffer = new byte[GLOBLE_BUFFER_SIZE];
            int temp = -1;
            input_ = makeInputBuffered(input_);
            output_ = makeOutputBuffered(output_);
            while ((temp = input_.read(buffer)) != -1) {
                output_.write(buffer, 0, temp);
                output_.flush();
            }
        } catch (IOException e) {
            throw e;
        } finally {
            FileUtils.closeStream(input_);
            FileUtils.closeStream(output_);
        }
    }

    public static OutputStream makeOutputBuffered(OutputStream output_) {
        if ((output_ instanceof BufferedOutputStream)) {
            return output_;
        }

        return new BufferedOutputStream(output_, GLOBLE_BUFFER_SIZE);
    }

    public static void unZipFile(File source, String destDir) throws IOException {
        if (!source.exists()) {
            return;
        }
        File outFile = new File(destDir);
        if (!outFile.exists()) {
            outFile.mkdirs();
        }
        ZipFile zipFile = new ZipFile(source);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            File destination = new File(outFile, entry.getName());
            if (entry.isDirectory()) {
                destination.mkdirs();
            } else {
                destination.createNewFile();
                FileOutputStream outStream = new FileOutputStream(destination);
                copy(zipFile.getInputStream(entry), outStream);
                outStream.close();
            }
        }
        zipFile.close();
    }

    public static void deleteFileExcept(File file_, @NonNull List<String> exceptPaths) {
        if (exceptPaths.contains(file_.getAbsolutePath())) {
            return;
        }
        if ((file_.exists()) && (file_.isDirectory())) {
            File[] childrenFile = file_.listFiles();
            if (childrenFile != null) {
                for (File f : childrenFile) {
                    if (f.isFile()) {
                        if (!exceptPaths.contains(f.getAbsolutePath())) {
                            delete(f);
                        }
                    } else if (f.isDirectory()) {
                        deleteFileExcept(f, exceptPaths);
                    }
                }
            }
            if (!exceptPaths.contains(file_.getAbsolutePath())) {
                delete(file_);
            }
        } else if ((file_.exists()) && (file_.isFile())) {
            if (!exceptPaths.contains(file_.getAbsolutePath())) {
                delete(file_);
            }
        }
    }

    public static void deleteFiles(File file_) {

        if ((file_.exists()) && (file_.isDirectory())) {
            File[] childrenFile = file_.listFiles();
            if (childrenFile != null) {
                for (File f : childrenFile) {
                    if (f.isFile()) {
                        delete(f);
                    } else if (f.isDirectory()) {
                        deleteFiles(f);
                    }
                }
            }
            delete(file_);
        } else if ((file_.exists()) && (file_.isFile())) {
            delete(file_);
        }
    }

    public static void deleteFiles(String path_) {
        if (!TextUtils.isEmpty(path_))
            deleteFiles(new File(path_));
    }

    public static long getFileSize(File file_) {

        long totalsize = 0;
        if (!file_.exists()) {
        } else if (file_.isFile()) {
            totalsize = (totalsize + file_.length());
        } else {
            File[] childrenFile = file_.listFiles();
            if (childrenFile != null) {
                for (File f : childrenFile) {
                    totalsize = totalsize + getFileSize(f);
                }
            }
        }
        return totalsize;
    }

    public static long getFileSize(String filepath_) {
        return getFileSize(new File(filepath_));
    }

    public static void delete(File f) {
        if (isFileExist(f.getAbsolutePath())) {
            f.delete();
        }
    }

    public static void delete(String path) {
        delete(new File(path));
    }

    public static void deleteAllFiles(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File[] files = file.listFiles();
                if (files != null) {
                    for (File fileItem : files) { // 遍历目录下所有的文件
                        deleteAllFiles(fileItem); // 把每个文件 用这个方法进行迭代
                    }
                }
            }
            file.delete();
        }
    }

    public static void renameTo(File src, File dest) {
        if (isFileExist(src.getAbsolutePath()) && (dest != null)) {
            src.renameTo(dest);
        }
    }

    public static void openAPKFileByOS(Context context, String filepath) {
        if (TextUtils.isEmpty(filepath)) {
            return;
        }
        File file = new File(filepath);
        if (file == null || !file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, RuntimeManager.getInstance().getContext().getPackageName() + ".fileprovider", file);
            intent.setDataAndType(contentUri, type);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), type);
        }
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            // no activity found
        }
    }

    /**
     * 依靠系统打开文件，类型依靠filepath的后缀名获取
     *
     * @param context
     * @param filepath
     */
    public static void openFileByOS(Context context, String filepath) {
        if (TextUtils.isEmpty(filepath) || !filepath.contains(".")) {
            return;
        }
        if (!isFileExist(filepath)) {
            return;
        }
        File file = new File(filepath);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            // no activity found
        }
    }

    /**
     * 根据后缀名获取文件的MIME
     *
     * @param file
     * @return
     */
    public static String getMIMEType(File file) {
        String type = "*/*";
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        String end = fileName.substring(dotIndex, fileName.length());
        if (TextUtils.isEmpty(end) && end.length() < 2) {
            return type;
        }
        end = end.substring(1, end.length()).toLowerCase();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        type = mimeTypeMap.getMimeTypeFromExtension(end);
        return type;
    }

    /**
     * 获取本地apk文件的信息
     *
     * @param context
     * @param file
     * @return
     */
    public static PackageInfo getPackageInfo(Context context, String file) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(file, PackageManager.GET_ACTIVITIES);
        return info;
    }

    /**
     * 计算文件夹大小(没有过滤)
     *
     * @param directory 文件夹
     * @return the number of bytes in this folder
     */
    public static long folderSize(File directory) {
        long length = 0;
        if (isDirectoryExist(directory.getAbsolutePath())) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null) {
                for (File file : listFiles) {
                    if (file.isFile()) {
                        length += file.length();
                    } else {
                        length += folderSize(file);
                    }
                }
            }
        }
        return length;
    }

    public static boolean isDirectoryNotEmpty(String filePath) {
        if (!isDirectoryExist(filePath))
            return false;
        File directory = new File(filePath);
        if (directory.isDirectory() && directory.exists()) {
            File[] files = directory.listFiles();
            return files != null && files.length > 0;
        }
        return false;
    }
}
