package com.example.dell_pc.qr_code_patrol.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dell-pc on 2016/3/4.
 */
public class PicManager {
    private String imageName;
    private String Img64 = null;
    private String fileName;



    public String getName(){
        return imageName;
    }

    public String getPhotopath() {
        // 照片全路径

        // 文件夹路径
//        String pathUrl = Environment.getExternalStorageDirectory() + "/checkpic/";
//        String imageName = "temp.jpg";
//        File file = new File(pathUrl);
//        file.mkdirs();// 创建文件夹
//        fileName = pathUrl + imageName;
//        System.out.println(fileName);
//        return fileName;

        String pathUrl = Environment.getExternalStorageDirectory().toString() + File.separator + File.separator + "IMAGE";
        String imageName = "temp.jpg";
        File file = new File(pathUrl);
        file.mkdirs();// 创建文件夹
        fileName = pathUrl + imageName;
        return fileName;

    }

    public Bitmap getBitmapFromUrl(String url, double width, double height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 设置了此属性一定要记得将值设置为false
        Bitmap bitmap = BitmapFactory.decodeFile(url);
        // 防止OOM发生
        options.inJustDecodeBounds = false;
        int mWidth = bitmap.getWidth();
        int mHeight = bitmap.getHeight();
        double rate = mHeight/mWidth;
        Matrix matrix = new Matrix();
        float scale = 1;

        if (mWidth <= mHeight) {
            scale = (float)(height/mHeight);
        } else {
            scale = (float)(width/mWidth);
        }
        matrix.postRotate(0); /* 翻转90度 */
        // 按照固定大小对图片进行缩放
        matrix.postScale(scale,scale);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, mWidth, mHeight, matrix, true);
        Img64 = Base64Image(newBitmap);

        // 用完了记得回收
        bitmap.recycle();
        return newBitmap;
    }

    public void saveScalePhoto(Bitmap bitmap, String unitId) {
        // 照片全路径
        String fileName;
        String strPicName;
        Date date;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        date = new Date(System.currentTimeMillis());
        strPicName = format.format(date);
        String pathUrl = Environment.getExternalStorageDirectory().toString() + File.separator + File.separator + "IMAGE";
        imageName = unitId + strPicName;
        FileOutputStream fos = null;
        fileName = pathUrl + imageName + ".jpg";
        try {
            fos = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String Base64Image(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] bytes = stream.toByteArray();
        String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        return img;
    }

    public Bitmap getDiskBitmap(String imageName) {
        Bitmap bitmap = null;
        String pathUrl = Environment.getExternalStorageDirectory().toString() + File.separator + File.separator + "IMAGE";
        try {
            bitmap = BitmapFactory.decodeFile(pathUrl + imageName + ".jpg");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bitmap;
    }


    public void deleteFile(String imageName) {
        String pathUrl = Environment.getExternalStorageDirectory().toString() + File.separator + File.separator + "IMAGE";
        File file = new File(pathUrl + imageName + ".jpg");
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            }
        }
    }


}
