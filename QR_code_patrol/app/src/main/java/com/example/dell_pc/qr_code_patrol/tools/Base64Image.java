package com.example.dell_pc.qr_code_patrol.tools;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by dell-pc on 2016/4/14.
 */
public class Base64Image {
    public static String Base64Image(Bitmap bitmap, int rate) {

        if( bitmap!=null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, rate, stream);
            byte[] bytes = stream.toByteArray();
            String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));

            return img;
        }else{

            Log.e("baseimagecompress", "Base64Image: ====>bietmap为null" );
            return "";
        }



    }
}
