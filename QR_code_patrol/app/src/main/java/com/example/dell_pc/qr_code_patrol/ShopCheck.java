package com.example.dell_pc.qr_code_patrol;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell_pc.qr_code_patrol.adpter.ProblemTypeAdapter;
import com.example.dell_pc.qr_code_patrol.adpter.ProblemsAdapter;
import com.example.dell_pc.qr_code_patrol.db.CheckDB;
import com.example.dell_pc.qr_code_patrol.db.MyAllCheckCardDB;
import com.example.dell_pc.qr_code_patrol.item.CheckedItem;
import com.example.dell_pc.qr_code_patrol.item.ProblemTypeItem;
import com.example.dell_pc.qr_code_patrol.item.ProblemsItem;
import com.example.dell_pc.qr_code_patrol.tools.CallPhone;
import com.example.dell_pc.qr_code_patrol.tools.FileOperation;
import com.example.dell_pc.qr_code_patrol.tools.HttpUtils;
import com.example.dell_pc.qr_code_patrol.tools.PicManager;
import com.example.dell_pc.qr_code_patrol.view.MyProgress;
import com.example.dell_pc.qr_code_patrol.view.RoundCornerImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by dell-pc on 2016/2/2.
 */
public class ShopCheck extends Activity implements View.OnClickListener {
    private String unitID;
    private String cardNo = "";
    private String imageName = "";
    private String unitName;
    private String Position;
    private String strTime;
    private ListView lvChecked;
    private Bitmap pic = null;

    private List<CheckedItem> checkedItemList = new ArrayList<CheckedItem>();
    private List<CheckedItem> listShow = new ArrayList<CheckedItem>();
    private Button btnCancel, btnSave;
    private PicManager mPicManger = new PicManager();
    private ListView lvProblem;
    private List<ProblemsItem> problemsList = new ArrayList<ProblemsItem>();
    private ProblemsAdapter problemsAdapter;


    private MyAllCheckCardDB myAllCardDB;
    private CheckDB checkDB;
    private Cursor mCursor, mCursor1;

    public static final String SEND_RECORD_TEXT_URL = "http://jn.xiaofang365.cn/xfwlw/xfwlw/addCheckRecord";
    public static final String SEND_RECORD_PIC_URL = "http://jn.xiaofang365.cn/xfwlw/xfwlw/uploadCheckRecordPic";


    String[] strResponseText = new String[200];
    String[] strResponsePic = new String[200];


    private TextView tvProblem;
    private TextView tvShopName;
    private TextView tvLinkman;
    private TextView tvPhone;

    private String shopID, shopName, linkman, phone;
    private RoundCornerImageView ivProblemPic;
    private RadioGroup rgStatue;
    private RadioButton rbNoProblem, rbProblem;
    private String statue = "noProblem";
    private LinearLayout llProblemType, llProblem;
    private List<ProblemTypeItem> problemTypeList = new ArrayList<ProblemTypeItem>();
    private ListView lvProblemType;
    private ProblemTypeAdapter problemTypeAdapter;

    private Button btnProblemTypeBack, btnProblemsBack;
    private String problemID = "";
    private String problem = "noProblem";


    private String URL_GET_PROBLEM_TYPES = "http://jn.xiaofang365.cn/xfwlw/xfwlw/getProblemTypes";
    private String URL_GET_PROBLEMS = "http://jn.xiaofang365.cn/xfwlw/xfwlw/getProblems?problemType=";

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_check);

        initView();//界面初始化
        initData();//数据初始化

        tvShopName.setText(shopName);
        tvLinkman.setText(linkman);
        tvPhone.setText(phone);

        HttpUtils.getJSON(URL_GET_PROBLEM_TYPES, getProblemTypesHandler);


    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    myProgress.setVisibility(View.GONE);
                    Toast.makeText(ShopCheck.this, "上传完毕", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    myProgress.setVisibility(View.GONE);
                    break;
            }
        }
    };


    private MyProgress myProgress = null;
    private Handler mHandler;
    int nCountRecord = 0;
    int nCountSend = 0;


    private void ProgressBarStart() {
        myProgress.setVisibility(View.VISIBLE);
        nCountRecord = checkedItemList.size();
        final Message msg = new Message();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub

                while (true) {
                    if (nCountRecord == 0) {
                        msg.what = 1;
                        handler.sendMessage(msg);

                        return;
                    }
                    if (nCountRecord == nCountSend) {
                        nCountRecord = 0;
                        nCountSend = 0;
                        checkedItemList.clear();
                        msg.what = 0;
                        handler.sendMessage(msg);
                        return;
                    }
                    int n = nCountSend * 10000 / nCountRecord / 100;
                    mHandler.sendEmptyMessage(n);
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }


//    private void ShowMsg() {
//        String strCountRecord = String.valueOf(listShow.size());
//        tvMsg.setText("目前有  " + strCountRecord + "  条巡检记录等待上传");
//    }


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


    }

    //初始化界面
    private void initView() {
        tvShopName = (TextView) findViewById(R.id.tvShopName);
        tvLinkman = (TextView) findViewById(R.id.tvLinkman);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        ivProblemPic = (RoundCornerImageView) findViewById(R.id.ivProblemPic);
        llProblemType = (LinearLayout) findViewById(R.id.llProblemType);
        llProblem = (LinearLayout) findViewById(R.id.llProblem);

        lvProblemType = (ListView) findViewById(R.id.lvProblemType);
        problemTypeAdapter = new ProblemTypeAdapter(ShopCheck.this, problemTypeList);
        lvProblemType.setAdapter(problemTypeAdapter);

        lvProblem = (ListView) findViewById(R.id.lvProblem);
        problemsAdapter = new ProblemsAdapter(this, problemsList);
        lvProblem.setAdapter(problemsAdapter);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnProblemTypeBack = (Button) findViewById(R.id.btnProblemTypeBack);
        btnProblemsBack = (Button) findViewById(R.id.btnProblemBack);
        tvProblem = (TextView) findViewById(R.id.tvProblem);

        rbNoProblem = (RadioButton) findViewById(R.id.rbNoProblem);
        rbProblem = (RadioButton) findViewById(R.id.rbProblem);

        rgStatue = (RadioGroup) findViewById(R.id.rgStatue);
        rgStatue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbNoProblem) {
                    statue = "noProblem";
                } else {
                    statue = "problem";
                    llProblemType.setVisibility(View.VISIBLE);
                }
            }
        });

        tvPhone.setOnClickListener(this);
        ivProblemPic.setOnClickListener(this);
        btnProblemTypeBack.setOnClickListener(this);
        btnProblemsBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        lvProblemType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                llProblemType.setVisibility(View.GONE);
                llProblem.setVisibility(View.VISIBLE);
                String problemTypeID = problemTypeList.get(position).getId();
                String url = URL_GET_PROBLEMS + problemTypeID;
                HttpUtils.getJSON(url, getProblemsHandler);
            }
        });

        lvProblem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                llProblem.setVisibility(View.GONE);
                llProblemType.setVisibility(View.GONE);
                tvProblem.setVisibility(View.VISIBLE);
                problem = problemsList.get(position).getType();
                tvProblem.setText(problem);
                problemID = problemsList.get(position).getId();
                problemsList.clear();
                problemsAdapter.notifyDataSetChanged();
            }
        });


    }

    //获取本单位所有卡片资料
    private Handler getCheckCardHandler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String cardNo = object.getString("cardNo");
                    String Position = object.getString("position");
                    String Fac = object.getString("fac");
                    String problemType = object.getString("problemType");
                    add(cardNo, Position, Fac, problemType);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };

    //获取所检查设备可能存在的问题
    private Handler getProblemTypesHandler = new Handler() {
        public void handleMessage(Message msg) {
            problemTypeList.clear();
            problemTypeAdapter.notifyDataSetChanged();
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.getString("id");
                    String type = object.getString("type");
                    problemTypeList.add(new ProblemTypeItem(id, type));
                }
                problemTypeAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };


    //获取所检查设备可能存在的问题
    private Handler getProblemsHandler = new Handler() {
        public void handleMessage(Message msg) {
            problemsList.clear();
            problemsAdapter.notifyDataSetChanged();
            String jsonData = (String) msg.obj;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.getString("id");
                    String problem = object.getString("problem");
                    problemsList.add(new ProblemsItem(id, problem));
                }
                problemsAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };


    double GetFileSize() {
        double dSize = 0;
        for (int i = 0; i < checkedItemList.size(); i++) {
            String imageName = checkedItemList.get(i).getImageName();
            String pathUrl = Environment.getExternalStorageDirectory().toString() + File.separator + File.separator + "IMAGE" + imageName + ".jpg";
            dSize = dSize + FileOperation.getFileOrFilesSize(pathUrl, 3);
        }
        return dSize;
    }

    void ShowAlertDlg() {
        double dFileSize = GetFileSize();
        new AlertDialog.Builder(ShopCheck.this).setTitle("系统提示")//设置对话框标题
                .setMessage("WIFI网络已断开,\n是否使用移动网络?\n传输数据约" + dFileSize + "Mb")//设置显示的内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        SendRecord();
                        ProgressBarStart();
                    }

                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件
                // TODO Auto-generated method stub
            }
        }).show();//在按键响应事件中显示此对话框
    }


    private Handler sendRecordHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                myProgress.setVisibility(View.GONE);
                Toast.makeText(ShopCheck.this, "网络连接超时", Toast.LENGTH_SHORT).show();
                return;
            }

            String jsonData = (String) msg.obj;
            int position = msg.arg1;
            if (jsonData.equals("null")) {
                Toast.makeText(ShopCheck.this, "网络错误", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                String strResponse = jsonObject.getString("response");
                for (int i = 0; i < 200; i++) {
                    if (strResponseText[i].equals("")) {
                        strResponseText[i] = strResponse;
                        DeleteRecord1(strResponsePic[i], position);
                        return;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };


    private Handler sendCheckPicHandler = new Handler() {
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;

            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                String strResponse = jsonObject.getString("response");
                int position = msg.arg1;
                nCountSend++;
                for (int i = 0; i < 200; i++) {
                    if (strResponsePic[i].equals("")) {
                        strResponsePic[i] = strResponse;
                        DeleteRecord2(strResponsePic[i], position);
                        return;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };


    void DeleteRecord1(String imageName, int position) {
        for (int i = 0; i < 200; i++) {
            if (strResponsePic[i].equals(imageName)) {
                checkDB.delete(imageName);
                //checkedItemList.get(position).setImageName("0000");
//                getListShow();
                return;
            }
        }
    }


    void DeleteRecord2(String imageName, int position) {
        for (int i = 0; i < 200; i++) {
            if (strResponseText[i].equals(imageName)) {

                checkDB.delete(imageName);
                mPicManger.deleteFile(imageName);
                if (checkedItemList.size() != 0) {
                    checkedItemList.get(position).setPosition("0000");
                }
//                getListShow();
                return;
            }
        }
    }


    //将本单位所有卡片资料写入本地数据库
    public void add(String cardNo, String Position, String Fac, String problemType) {
        myAllCardDB.insert(cardNo, Position, Fac, problemType);
        mCursor1.requery();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000 && resultCode == Activity.RESULT_OK) {

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
        } else if (view == btnProblemTypeBack) {
            llProblemType.setVisibility(View.GONE);
            rbProblem.setChecked(false);
            rbNoProblem.setChecked(true);
        } else if (view == btnProblemsBack) {
            problemsList.clear();
            problemsAdapter.notifyDataSetChanged();
            llProblem.setVisibility(View.GONE);
            llProblemType.setVisibility(View.VISIBLE);
        } else if (view == btnSave) {
            if (pic == null) {
                Toast.makeText(ShopCheck.this, "请拍摄现场照片", Toast.LENGTH_LONG).show();
            } else {
                String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                String statue;
                if (rbNoProblem.isChecked()) {
                    statue = "0";
                } else {
                    statue = "1";
                }
                Chose.shopCheckDB.insert(cardNo, shopID, unitID, timestamp, imageName, problemID, problem, statue);
                finish();
            }
        } else if (view == btnCancel) {
            finish();
        }
//        else if (view == btnSend) {
//            if (listShow.size() != 0) {
//                switch (NetStatue.GetNetType(this)) {
//                    case -1:
//                        Toast.makeText(ShopCheck.this, "无可用网络", Toast.LENGTH_LONG).show();
//                        return;
//                    case 2:
//                        ShowAlertDlg();
//                        return;
//                    default:
//                        break;
//                }
//                SendRecord();
//                ProgressBarStart();
//            }
//        }
    }


    private void SendRecord() {
        for (int i = 0; i < checkedItemList.size(); i++) {
            CheckedItem checkedItem = checkedItemList.get(i);
            String strCardNO = null, strStatue = null, strProblemID = null, strProblem = null, strImageName = null, strTime = null;
            try {
                strCardNO = URLEncoder.encode(checkedItem.getCardNo(), "UTF-8");
                strStatue = URLEncoder.encode(checkedItem.getStatue(), "UTF-8");
                strProblemID = URLEncoder.encode(checkedItem.getProblemID(), "UTF-8");
                strProblem = URLEncoder.encode(checkedItem.getProblem(), "UTF-8");
                strImageName = URLEncoder.encode(checkedItem.getImageName(), "UTF-8");
                strTime = URLEncoder.encode(checkedItem.getDateTime(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String url = SEND_RECORD_TEXT_URL
                    + "?cardNo=" + strCardNO
                    + "&unitID=" + unitID
                    + "&statue=" + strStatue
                    + "&problemID=" + strProblemID
                    + "&problem=" + strProblem
                    + "&imageName=" + strImageName
                    + "&time=" + strTime;
            HttpUtils.getJSONWithPosition(url, sendRecordHandler, i);
            Bitmap bitmap = mPicManger.getDiskBitmap(strImageName);
            if(bitmap!=null){

                HttpUtils.uploadBitmap(bitmap, strImageName, SEND_RECORD_PIC_URL, sendCheckPicHandler, i);

            }

        }
    }


    //回退键控制
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {

            if (llProblem.getVisibility() == View.VISIBLE) {
                problemsList.clear();
                problemsAdapter.notifyDataSetChanged();
                llProblem.setVisibility(View.GONE);
                llProblemType.setVisibility(View.VISIBLE);
                return true;
            } else if (llProblemType.getVisibility() == View.VISIBLE) {
                rbProblem.setChecked(false);
                rbNoProblem.setChecked(true);
                llProblemType.setVisibility(View.GONE);
                return true;
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}















