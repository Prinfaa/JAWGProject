package com.example.dell_pc.qr_code_patrol;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell_pc.qr_code_patrol.tools.CallPhone;

import com.example.dell_pc.qr_code_patrol.tools.PicManager;
import com.example.dell_pc.qr_code_patrol.tools.downImageTask1;
import com.example.dell_pc.qr_code_patrol.view.RoundCornerImageView;


import java.io.File;



/**
 * Created by dell-pc on 2016/2/2.
 */
public class ProblemDeal extends Activity implements View.OnClickListener {
    private String unitID;
    private String cardNo = "";
    private String imageName = "";
    private int id;
    private String unitName;
    private Bitmap pic = null;

    private Button btnCancel, btnSave;
    private PicManager mPicManger = new PicManager();

    RoundCornerImageView ivProblem;

    private TextView tvProblem;
    private TextView tvShopName;
    private TextView tvLinkman;
    private TextView tvPhone;

    private String shopID, shopName, linkman, phone;
    private RoundCornerImageView ivProblemPic;
    public static int screenWidth;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.problem_deal);
        initData();//数据初始化
        initView();//界面初始化

        tvShopName.setText(shopName);
        tvLinkman.setText(linkman);
        tvPhone.setText(phone);

    }




    //初始化数据
    private void initData() {
        Intent intent = this.getIntent();
        unitID = intent.getStringExtra("unitID");
        unitName = intent.getStringExtra("unitName");
        cardNo = intent.getStringExtra("code");
        shopID = intent.getStringExtra("shopID");
        shopName = intent.getStringExtra("shopName");
        linkman = intent.getStringExtra("linkman");
        phone = intent.getStringExtra("phone");
        id = Integer.parseInt(intent.getStringExtra("id"));

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;     // 屏幕宽度（像素）


    }

    //初始化界面
    private void initView() {
        tvShopName = (TextView) findViewById(R.id.tvShopName);
        tvLinkman = (TextView) findViewById(R.id.tvLinkman);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        ivProblemPic = (RoundCornerImageView) findViewById(R.id.ivProblemPic);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave = (Button) findViewById(R.id.btnSave);

        tvProblem = (TextView) findViewById(R.id.tvProblem);

        ivProblem = (RoundCornerImageView) findViewById(R.id.ivProblem);
        String url = ShopProblem.shopProblemNotDealList.get(id).getImageName();
        ivProblem.setTag(url);
        new downImageTask1().execute(ivProblem);
        tvProblem.setText(ShopProblem.shopProblemNotDealList.get(id).getProblem());

        tvPhone.setOnClickListener(this);
        ivProblemPic.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000 && resultCode == Activity.RESULT_OK) {


            String fileName = mPicManger.getPhotopath();
            pic = mPicManger.getBitmapFromUrl(fileName, 800, 800);
            mPicManger.saveScalePhoto(pic, unitID);
            imageName = mPicManger.getName();

            double w = pic.getWidth();
            double h = pic.getHeight();
            double scale;
            if (w > h) {
                scale = 0.94;
            } else {
                scale = 0.6;
            }
            double r = h / w;
            ivProblemPic.setLayoutParams(new LinearLayout.LayoutParams((int) (screenWidth * scale), (int) (screenWidth * r * scale)));
            ivProblemPic.setImageBitmap(pic);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == tvPhone) {
            String inputStr = (String) tvPhone.getText();
            new CallPhone().call(this, inputStr);
        } else if (view == ivProblemPic) {
            if (!shopID.equals("")) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                String fileName = mPicManger.getPhotopath();
                File out = new File(fileName);
                Uri uri = Uri.fromFile(out);
                // 获取拍照后未压缩的原图片，并保存在uri路径中
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 2000);
            }
        }  else if (view == btnSave) {
            if (pic == null) {
                Toast.makeText(ProblemDeal.this, "请拍摄现场照片", Toast.LENGTH_LONG).show();
            } else {
                String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                String recordID = ShopProblem.shopProblemNotDealList.get(id).getId();
                Chose.dealDB.insert(cardNo, recordID,  timestamp, imageName);
                finish();
            }
        } else if (view == btnCancel) {
            finish();
        }
    }




    //回退键控制
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}















