package com.example.dell_pc.qr_code_patrol.tools;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.dell_pc.qr_code_patrol.ProblemDeal;

/**
 * Created by dell-pc on 2016/1/19.
 */
public class downImageTask1 extends AsyncTask<ImageView, Void, Bitmap> {

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

            double w = result.getWidth();
            double h = result.getHeight();
            double scale;
            if (w > h) {
                scale = 0.94;
            } else {
                scale = 0.6;
            }
            double r = h / w;
            gView.setLayoutParams(new LinearLayout.LayoutParams((int) (ProblemDeal.screenWidth * scale), (int) (ProblemDeal.screenWidth * r * scale)));


            this.gView.setImageBitmap(result);

    }
        this.gView = null;
    }
}