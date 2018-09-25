package com.example.dell_pc.qr_code_patrol;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dell_pc.qr_code_patrol.adpter.ProblemAdapter;
import com.example.dell_pc.qr_code_patrol.item.ProblemItem;
import com.example.dell_pc.qr_code_patrol.tools.HttpUtils;
import com.example.dell_pc.qr_code_patrol.tools.PicManager;
import com.example.dell_pc.qr_code_patrol.view.RoundCornerImageView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by dell-pc on 2016/2/2.
 */
public class Check extends Activity implements View.OnClickListener {
    private String unitID;
    private String cardNo = "";
    private String imageName = "";
    private String unitName;
    private Bitmap pic = null;


    private TextView tvFac, tvPosition;
    private ImageView ivCamera, ivPic;
    private LinearLayout llPatrolPage1;
    private LinearLayout llStatues;
    private FrameLayout flPatrolPage2;
    private RadioGroup rgCheck;
    private RadioButton rbNormal, rbAbnormal;
    private Button btnSave, btnCancel;
    private PicManager mPicManger;
    private LinearLayout llPic;

    private LinearLayout llProblem;
    private ListView lvProblem;
    private Button btnProblemCancel, btnProblemOK;
    private List<ProblemItem> problemList = new ArrayList<ProblemItem>();
    private ProblemAdapter problemAdapter;

    String problem = "noProblem";
    String problemID = "";

    private String URL_GET_PROBLEMS = "http://jn.xiaofang365.cn/xfwlw/xfwlw/getProblems?problemType=";

    private TextView tvProblem;
    private ScrollView svInfo;
    private Button btnInfoOK;
    private Button btnSend;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check);

        initView();//界面初始化
        initData();//数据初始化

    }

    //初始化数据
    private void initData() {
        Intent intent1 = this.getIntent();
        unitID = intent1.getStringExtra("unitID");
        unitName = intent1.getStringExtra("unitName");
        cardNo = intent1.getStringExtra("code");

        if (!cardNo.equals("")) {
            llPatrolPage1.setVisibility(View.GONE);
            flPatrolPage2.setVisibility(View.VISIBLE);

            String strFac = Chose.myAllCheckCardDB.getFac(cardNo);
            String strPosition = Chose.myAllCheckCardDB.getPosition(cardNo);

            tvFac.setText(strFac);
            tvPosition.setText(strPosition);

            String url = URL_GET_PROBLEMS + Chose.myAllCheckCardDB.getProblemType(cardNo);
            HttpUtils.getJSON(url, getProblemsHandler);

        }

        mPicManger = new PicManager();

    }

    //初始化界面
    private void initView() {
        llPatrolPage1 = (LinearLayout) findViewById(R.id.patrol_page1);
        flPatrolPage2 = (FrameLayout) findViewById(R.id.patrol_page2);
        ivCamera = (ImageView) findViewById(R.id.ivCamera);
        ivPic = (ImageView) findViewById(R.id.ivPic);
        rgCheck = (RadioGroup) findViewById(R.id.rgCheck);
        rbNormal = (RadioButton) findViewById(R.id.rbNormal);
        rbAbnormal = (RadioButton) findViewById(R.id.rbAbnormal);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        llPic = (LinearLayout) findViewById(R.id.ll_pic);
        llStatues = (LinearLayout) findViewById(R.id.ll_status);
        tvFac = (TextView) findViewById(R.id.tvFac);
        tvPosition = (TextView) findViewById(R.id.tvPosition);
        llProblem = (LinearLayout) findViewById(R.id.llProblem);
        btnProblemCancel = (Button) findViewById(R.id.btnProblemCancel);
        btnProblemOK = (Button) findViewById(R.id.btnProblemOK);

        svInfo = (ScrollView) findViewById(R.id.svInfo);
        btnInfoOK = (Button) findViewById(R.id.btnInfoOK);
        btnSend = (Button) findViewById(R.id.btnSend);


        ivCamera.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnProblemCancel.setOnClickListener(this);
        btnProblemOK.setOnClickListener(this);
        btnInfoOK.setOnClickListener(this);
        btnSend.setOnClickListener(this);

        lvProblem = (ListView) findViewById(R.id.lvProblem);
        problemAdapter = new ProblemAdapter(this, problemList);
        lvProblem.setAdapter(problemAdapter);

        tvProblem = (TextView) findViewById(R.id.tvProblem);

        rbAbnormal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    llProblem.setVisibility(View.VISIBLE);
                    problemAdapter.notifyDataSetChanged();
                }
            }
        });

        rbNormal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    problem = "";
                    problemID = "";
                    tvProblem.setText("");
                    for (int i = 0; i < problemList.size(); i++) {
                        problemList.get(i).setChecked(false);
                    }
                }
            }
        });
    }


    //获取所检查设备可能存在的问题
    private Handler getProblemsHandler = new Handler() {
        public void handleMessage(Message msg) {
            problemList.clear();
            problemAdapter.notifyDataSetChanged();
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.getString("id");
                    String problem = object.getString("problem");
                    problemList.add(new ProblemItem(cardNo, id, problem, false));
                }
                problemAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000 && resultCode == Activity.RESULT_OK) {

            llPic.setVisibility(View.VISIBLE);
            int screenWidth, screenHeight;
            Resources res;
            res = this.getResources();
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            screenWidth = metric.widthPixels;     // 屏幕宽度（像素）

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
            ivPic.setLayoutParams(new LinearLayout.LayoutParams((int) (screenWidth * scale), (int) (screenWidth * r * scale)));

            llPic.setLayoutParams(new LinearLayout.LayoutParams((int) (screenWidth * scale), (int) (((screenWidth * r) + (70 * res.getDisplayMetrics().scaledDensity)) * scale)));
            ivPic.setImageBitmap(pic);

            llStatues.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == ivCamera) {
            if (!cardNo.equals("")) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                String fileName = mPicManger.getPhotopath();
                File out = new File(fileName);
                Uri uri = Uri.fromFile(out);
                // 获取拍照后未压缩的原图片，并保存在uri路径中
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent2, 2000);
            }
        } else if (view == btnSave) {
            if (pic == null) {
                Toast.makeText(Check.this, "请拍摄设备照片", Toast.LENGTH_LONG).show();
            } else {
                String strStatue;
                if (rbNormal.isChecked()) {
                    strStatue = "0";
                } else {
                    strStatue = "1";
                }

                String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                Chose.checkDB.insert(cardNo, timestamp, imageName, problemID, problem, tvPosition.getText().toString(), tvFac.getText().toString(), strStatue);
                finish();
            }


        } else if (view == btnCancel) {
            finish();
        } else if (view == btnProblemCancel) {
            llProblem.setVisibility(View.GONE);
        } else if (view == btnProblemOK) {
            problem = "";
            problemID = "";
            for (int i = 0; i < problemList.size(); i++) {
                if (problemList.get(i).isChecked()) {
                    if (problem == "") {
                        problem = problem + problemList.get(i).getProblem();
                        problemID = problemID + problemList.get(i).getProblemID();
                    } else {
                        problem = problem + "_" + problemList.get(i).getProblem();
                        problemID = problemID + "_" + problemList.get(i).getProblemID();
                    }
                }
            }
            llProblem.setVisibility(View.GONE);
            tvProblem.setText(problem.replace("_", "\r\n"));

        } else if (view == btnInfoOK) {
            svInfo.setVisibility(View.GONE);
            llPatrolPage1.setVisibility(View.VISIBLE);
        }
    }






    //回退键控制
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {

            if (llProblem.getVisibility() == View.VISIBLE) {
                llProblem.setVisibility(View.GONE);
                return true;
            } else if (svInfo.getVisibility() == View.VISIBLE) {
                svInfo.setVisibility(View.GONE);
                llPatrolPage1.setVisibility(View.VISIBLE);
                return true;
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}















