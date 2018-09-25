package com.example.dell_pc.unitpad.tools;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * Created by dell-pc on 2016/1/19.
 */
public class downImageTask extends AsyncTask<ImageView, Void, Bitmap> {

    ImageView gView = null;
    @Override
    protected Bitmap doInBackground(ImageView... arg0) {
        gView = (ImageView)arg0[0];
        Bitmap bitmap = Download.getBitmapFromUrl(gView.getTag().toString());

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if(result != null){
            this.gView.setImageBitmap(result);
    }
        this.gView = null;
    }
}