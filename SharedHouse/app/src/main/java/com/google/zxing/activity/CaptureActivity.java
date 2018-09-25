package com.google.zxing.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.camera.CameraManager;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.decoding.CaptureActivityHandler;
import com.google.zxing.decoding.InactivityTimer;
import com.google.zxing.decoding.RGBLuminanceSource;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.view.ViewfinderView;
import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.activities.DeviceDeticalActivity;
import com.jinganweigu.entrysystem.activities.DeviceEnterExamineActivity;
import com.jinganweigu.entrysystem.activities.DeviceOutExamineActivity;
import com.jinganweigu.entrysystem.activities.ReturnReasonActivity;
import com.jinganweigu.entrysystem.adapter.DeviceDeticalAdapter;
import com.jinganweigu.entrysystem.entry.BaseBean;
import com.jinganweigu.entrysystem.entry.OutHouseBean;
import com.jinganweigu.entrysystem.entry.ReadyEnterDeviceBean;
import com.jinganweigu.entrysystem.entry.SureOutHouseBean;
import com.jinganweigu.entrysystem.entry.WasteDeviceEnterBean;
import com.jinganweigu.entrysystem.utils.HttpCode;
import com.jinganweigu.entrysystem.utils.LoadImageUtil;
import com.jinganweigu.entrysystem.utils.Mycontants;
import com.jinganweigu.entrysystem.utils.Sptools;
import com.jinganweigu.entrysystem.utils.ToastUtil;
import com.jinganweigu.entrysystem.utils.Url;
import com.jinganweigu.entrysystem.utils.http.HttpTool;
import com.jinganweigu.entrysystem.utils.http.SMObjectCallBack;
import com.jinganweigu.entrysystem.view.CommonProgressDialog;
import com.lidroid.xutils.http.RequestParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;


/**
 * Initial the camera
 *
 * @author prinfaa
 */
public class CaptureActivity extends AppCompatActivity implements Callback {

    private static final int REQUEST_CODE_SCAN_GALLERY = 100;

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private ImageView back;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.90f;
    private boolean vibrate;
    private ProgressDialog mProgress;
    private String photo_path;
    private Bitmap scanBitmap;
    //	private Button cancelScanButton;
    public static final int RESULT_CODE_QR_SCAN = 0xA1;
    public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";

    private List<String> DeviceNum = new ArrayList<>();
    private ListView xListView;
    private ScanCodeAdapter adapter;
    private Button btnEndScanCode, btnBadReturn, btnGoodReturn;
    private LinearLayout llScanCodeReturn;
    private int FromCode;
    private String deviceType;
    private CommonProgressDialog dialog;
    private String Name;
    private TextView tvTitle;
    private final static int REQUESTCODE = 1005; // 返回的结果码

    //退货需要的id
    private String id;

    DeviceDeticalAdapter detailadapter;
//    List<SureOutHouseBean.ResultBean> list = new ArrayList<>();

    //出库参数

    private String outPeople, receivePeople, receiveUnit;

    private Handler mHanlder = new Handler();


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_content);
        back = (ImageView) findViewById(R.id.scanner_toolbar_back);
        xListView = (ListView) findViewById(R.id.xl_device);
        tvTitle = (TextView) findViewById(R.id.scanner_toolbar_title);
        btnEndScanCode = (Button) findViewById(R.id.btn_end_scan_code);
        btnBadReturn = (Button) findViewById(R.id.btn_bad_return);
        btnGoodReturn = (Button) findViewById(R.id.btn_good_return);
        llScanCodeReturn = (LinearLayout) findViewById(R.id.ll_scan_code_return);
        FromCode = getIntent().getIntExtra("from", 0);//区分出入库  //0是入库，1是出库 2正常退货 3损坏退货
        dialog = new CommonProgressDialog(this, "正在载入....");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Name = Sptools.getString(this, Mycontants.REAL_NAME, "");
//		cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        adapter = new ScanCodeAdapter(this, DeviceNum, 0);//0是扫码  1 是手动

        xListView.setAdapter(adapter);

        DeviceNum.clear();

        //添加toolbar
//        addToolbar();

        if (FromCode == 0) {

            tvTitle.setText("入库扫码");
            btnEndScanCode.setVisibility(View.VISIBLE);
            llScanCodeReturn.setVisibility(View.GONE);
            deviceType = getIntent().getStringExtra("deviceType");
        } else if (FromCode == 1) {

            tvTitle.setText("出库扫码");

            btnEndScanCode.setVisibility(View.VISIBLE);
            llScanCodeReturn.setVisibility(View.GONE);
            outPeople = getIntent().getStringExtra("out_people");
            receivePeople = getIntent().getStringExtra("receivePeople");
            receiveUnit = getIntent().getStringExtra("receive_unit");


        } else if (FromCode == 2) {

            tvTitle.setText("退货扫码");
            btnEndScanCode.setVisibility(View.GONE);
            llScanCodeReturn.setVisibility(View.VISIBLE);

        } else if (FromCode == 3) {

            btnEndScanCode.setVisibility(View.GONE);
            llScanCodeReturn.setVisibility(View.GONE);


        }
//        else if (FromCode == 3) {
//
//            tvTitle.setText("损坏退货扫码");
//        }


        btnEndScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FromCode == 0) {//入库

                    if (!TextUtils.isEmpty(deviceType) && deviceType.equals("成品入库")) {

                        Intent intent = new Intent(CaptureActivity.this, DeviceEnterExamineActivity.class);
                        intent.putExtra("deviceType", deviceType);
                        startActivity(intent);
                        finish();

                    } else if (!TextUtils.isEmpty(deviceType) && deviceType.equals("新品入库")) {

                        Intent intent = new Intent(CaptureActivity.this, DeviceEnterExamineActivity.class);
                        intent.putExtra("deviceType", deviceType);
                        startActivity(intent);
                        finish();

                    } else if (!TextUtils.isEmpty(deviceType) && deviceType.equals("废品入库")) {

                        Intent intent = new Intent(CaptureActivity.this, DeviceEnterExamineActivity.class);
                        intent.putExtra("deviceType", deviceType);
                        startActivity(intent);
                        finish();


                    }


                } else if (FromCode == 1) {//出库
                    Intent intent = new Intent(CaptureActivity.this, DeviceOutExamineActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    ToastUtil.showToast("出错了！！！", CaptureActivity.this);

                }


            }
        });


        btnGoodReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//正常退货
//                    String deviceId = DeviceNum.get(0);
                if (!TextUtils.isEmpty(id)) {

                    LoadImageUtil.IntentMethod.getInstance().startMethodWithStringInt
                            (CaptureActivity.this, ReturnReasonActivity.class, "deviceId", id, "ReturnThing", 0);// ReturnThing 0 为正常退货 1

                    finish();
                } else {

                    ToastUtil.showToast("请扫描退货设备", CaptureActivity.this);


                }


            }
        });


        btnBadReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//损坏退货


                if (!TextUtils.isEmpty(id)) {

                    LoadImageUtil.IntentMethod.getInstance().startMethodWithStringInt
                            (CaptureActivity.this, ReturnReasonActivity.class, "deviceId", id, "ReturnThing", 1);// ReturnThing 0 为正常退货 1

                    finish();
                } else {


                    ToastUtil.showToast("请扫描退货设备", CaptureActivity.this);

                }


            }
        });

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (handler != null) {
                handler.restartPreviewAndDecode();

            }


        }
    };


    private void addToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        ImageView more = (ImageView) findViewById(R.id.scanner_toolbar_more);
//        assert more != null;
//        more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.scanner_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.scan_local:
//                //打开手机中的相册
//                Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
//                innerIntent.setType("image/*");
//                Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
//                this.startActivityForResult(wrapperIntent, REQUEST_CODE_SCAN_GALLERY);
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN_GALLERY:
                    //获取选中图片的路径
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    cursor.close();

                    mProgress = new ProgressDialog(CaptureActivity.this);
                    mProgress.setMessage("正在扫描...");
                    mProgress.setCancelable(false);
                    mProgress.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Result result = scanningImage(photo_path);
                            if (result != null) {
                                Intent resultIntent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString(INTENT_EXTRA_KEY_QR_SCAN, result.getText());
                                resultIntent.putExtras(bundle);
                                CaptureActivity.this.setResult(RESULT_CODE_QR_SCAN, resultIntent);

                            }
                        }
                    }).start();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 扫描二维码图片的方法
     *
     * @param path
     * @return
     */
    public Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scanner_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

//        Log.e("loge", "onResume: ===>>>"+FromCode );

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        mHanlder.postDelayed(runnable, 2000);
        String resultString = result.getText();//扫描二维码后获取的数据
        //FIXME
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {


            if (DeviceNum.contains(resultString)) {

                ToastUtil.showToast("已经扫描过了！", this);

            } else {

                if (FromCode == 0) { //入库
//                    DeviceNum.clear();
                    if (!TextUtils.isEmpty(Name)) {

                        if (!TextUtils.isEmpty(deviceType) && deviceType.equals("成品入库")) {
                            upLoadIntoGoodDevice(resultString, Name);
                        } else if (!TextUtils.isEmpty(deviceType) && deviceType.equals("新品入库")) {
                            upLoadNewDeviceIntoHouse(resultString, Name);
                        } else if (!TextUtils.isEmpty(deviceType) && deviceType.equals("废品入库")) {
                            upLoadIntoBadDevice(resultString, Name);
                        }

                    } else {

                        ToastUtil.showToast("姓名错误", CaptureActivity.this);

                    }

                } else if (FromCode == 1) {//出库

                    ScanCodeOutHouse(resultString, outPeople, receivePeople, receiveUnit);

                } else if (FromCode == 2) {//正常退货

                    DeviceNum.clear();
//                    list.clear();

                    if (!TextUtils.isEmpty(resultString)) {

//                        GoodReturn(resultString, Name);
//                        DeviceNum.add(resultString);
//                        adapter.notifyDataSetChanged();
                        id = resultString;
                        CheckOutReturnDevice(resultString);

                    } else {

                        ToastUtil.showToast("退货人为null", CaptureActivity.this);

                    }

                } else if (FromCode == 3) {

//                    DeviceNum.clear();
//                    CheckOutReturnDevice(resultString);
                    Intent intent = new Intent(CaptureActivity.this, DeviceDeticalActivity.class);
                    intent.putExtra("num", resultString);
                    startActivity(intent);


                }


            }


        }
    }

    //扫码新品入库接口
    private void upLoadNewDeviceIntoHouse(final String DeviceID, String Name) {
        dialog.show();
        RequestParams params = new RequestParams();

        params.addBodyParameter("rtu_id", DeviceID);
        params.addBodyParameter("in_mitu_peo", Name);


        HttpTool.getInstance(this).httpWithParams(Url.NEW_DEVICE_ENTER_HOUSE_URL, params, new SMObjectCallBack<ReadyEnterDeviceBean>() {

            @Override
            public void onSuccess(ReadyEnterDeviceBean Bean) {

                if (Bean.getCode() == HttpCode.REQUEST_SUCCESS) {//操作成功

                    dialog.dismiss();

                    ToastUtil.showToast(Bean.getMsg(), CaptureActivity.this);

                    DeviceNum.add(Bean.getResult().getRtu_id());

                    adapter.notifyDataSetChanged();


                } else {

                    dialog.dismiss();
                    ToastUtil.showToast(Bean.getMsg(), CaptureActivity.this);

                }


            }

            @Override
            public void onError(int error, String msg) {


                Log.e("code", "onSuccess: =code==>" + msg);

                dialog.dismiss();


            }
        });


    }


    //扫码二次入库接口
    private void upLoadIntoGoodDevice(final String DeviceID, String Name) {

        dialog.show();
        RequestParams params = new RequestParams();

        params.addBodyParameter("rtu_id", DeviceID);
        params.addBodyParameter("in_mitu_peo", Name);


        HttpTool.getInstance(this).httpWithParams(Url.ENTER_HOUSE_URL, params, new SMObjectCallBack<ReadyEnterDeviceBean>() {

            @Override
            public void onSuccess(ReadyEnterDeviceBean Bean) {


                Log.e("code", "onSuccess: =code==>" + Bean.getCode());

                if (Bean.getCode() == HttpCode.REQUEST_SUCCESS) {//操作成功

                    dialog.dismiss();

                    ToastUtil.showToast(Bean.getMsg(), CaptureActivity.this);

                    DeviceNum.add(Bean.getResult().getRtu_id());

                    adapter.notifyDataSetChanged();


                } else if (Bean.getCode() == HttpCode.THE_DEVICE_IS_SECOND_ENTER_HOUSE) {//该设备为二次入库设备
                    dialog.dismiss();

                    DeviceNum.add(DeviceID);
                    adapter.notifyDataSetChanged();
                    ToastUtil.showToast(Bean.getMsg(), CaptureActivity.this);


                } else if (Bean.getCode() == HttpCode.CODE_ERROR) {//操作失败
                    dialog.dismiss();
                    ToastUtil.showToast(Bean.getMsg(), CaptureActivity.this);


                } else if (Bean.getCode() == HttpCode.NO_DEVICE) {//没有该设备
                    dialog.dismiss();
                    ToastUtil.showToast(Bean.getMsg(), CaptureActivity.this);

                } else if (Bean.getCode() == HttpCode.FINISH_PRODUCT_INTO_HOUSE) {//该成品已入库
                    dialog.dismiss();
                    ToastUtil.showToast(Bean.getMsg(), CaptureActivity.this);

                } else if (Bean.getCode() == HttpCode.THE_FINISHED_PRODUCT_INTO_HOUSE_SUCCESS) {//该成品已预入库
                    dialog.dismiss();
                    ToastUtil.showToast(Bean.getMsg(), CaptureActivity.this);

                } else {

                    dialog.dismiss();
                    ToastUtil.showToast(Bean.getMsg(), CaptureActivity.this);

                }


            }

            @Override
            public void onError(int error, String msg) {


                Log.e("code", "onSuccess: =code==>" + msg);

                dialog.dismiss();


            }
        });


    }

    //扫码入废库接口
    private void upLoadIntoBadDevice(String deviceId, String name) {

        dialog.show();

        RequestParams params = new RequestParams();

        params.addBodyParameter("rtu_id", deviceId);
        params.addBodyParameter("waste_peo", name);

        HttpTool.getInstance(this).httpWithParams(Url.BAD_ENTER_HOUSE_URL, params, new SMObjectCallBack<WasteDeviceEnterBean>() {


            @Override
            public void onSuccess(WasteDeviceEnterBean baseBean) {

                if (baseBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    dialog.dismiss();

                    ToastUtil.showToast(baseBean.getMsg(), CaptureActivity.this);

//                    DeviceNum.add(baseBean.getResult().getRtu_id());

                    adapter.notifyDataSetChanged();


                } else {

                    dialog.dismiss();
                    ToastUtil.showToast(baseBean.getMsg(), CaptureActivity.this);
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }

    //扫码预出库
    private void ScanCodeOutHouse(final String deviceId, String Name, String Leader, String unit) {


        RequestParams params = new RequestParams();

        params.addBodyParameter("rtu_id", deviceId);
        params.addBodyParameter("out_people", Name);
        params.addBodyParameter("receive_people", Leader);
        params.addBodyParameter("receive_unit", unit);

        HttpTool.getInstance(this).httpWithParams(Url.DEVICE_OUT_HOUSE_URL, params, new SMObjectCallBack<OutHouseBean>() {


            @Override
            public void onSuccess(OutHouseBean outHouseBean) {

                if (outHouseBean.getCode() == HttpCode.REQUEST_SUCCESS) {

                    ToastUtil.showToast(outHouseBean.getMsg(), CaptureActivity.this);

                    DeviceNum.add(deviceId);

                    adapter.notifyDataSetChanged();


                } else {
                    ToastUtil.showToast(outHouseBean.getMsg(), CaptureActivity.this);

                }


            }

            @Override
            public void onError(int error, String msg) {

            }
        });


    }

    //查询出退货设备

    private void CheckOutReturnDevice(String DeviceId) {


        dialog.show();

        RequestParams params = new RequestParams();

        params.addBodyParameter("rtu_id", DeviceId);

        HttpTool.getInstance(this).httpWithParams(Url.SURE_OUT_HOUSE, params, new SMObjectCallBack<SureOutHouseBean>() {


            @Override
            public void onSuccess(SureOutHouseBean sureOutHouseBean) {

                dialog.dismiss();

                if (sureOutHouseBean.getCode() == HttpCode.REQUEST_SUCCESS) {


                    for (int i = 0; i < sureOutHouseBean.getResult().size(); i++) {

                        DeviceNum.add(sureOutHouseBean.getResult().get(i).getFirst_line() + ":" + sureOutHouseBean.getResult().get(i).getSecond_line());

                    }

                    adapter.notifyDataSetChanged();

                    if (FromCode == 3) {

                    }


                } else {

                    dialog.dismiss();


                    ToastUtil.showToast(sureOutHouseBean.getMsg(), CaptureActivity.this);

                }


            }

            @Override
            public void onError(int error, String msg) {


                dialog.dismiss();

            }
        });


    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

}