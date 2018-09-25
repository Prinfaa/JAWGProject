package com.jinganweigu.RoadWayFire.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jinganweigu.RoadWayFire.BeseClassUtils.BaseActivity;
import com.jinganweigu.RoadWayFire.BeseClassUtils.MyApplication;
import com.jinganweigu.RoadWayFire.Entries.BaseModel;
import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.Interfaces.Url;
import com.jinganweigu.RoadWayFire.R;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.Sptools;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.HttpTool;
import com.jinganweigu.RoadWayFire.ToolsUtils.HttpUtil.SMObjectCallBack;
import com.jinganweigu.RoadWayFire.ToolsUtils.ImageUtil.GlideCircleTransform;
import com.jinganweigu.RoadWayFire.ToolsUtils.ImageUtil.LoadImageUtil;
import com.jinganweigu.RoadWayFire.ToolsUtils.ToolUntils.FileStorage;
import com.jinganweigu.RoadWayFire.Views.SelectDialog;
import com.lidroid.xutils.http.RequestParams;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.util.ProviderUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonInformationActivity extends BaseActivity {


    @BindView(R.id.top_name)
    TextView topName;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_phone_num)
    EditText edPhoneNum;
    @BindView(R.id.btn_change_username)
    Button btnChangeUsername;
    @BindView(R.id.btn_quit)
    Button btnQuit;
    private static final int REQUEST_PICK_IMAGE = 1; //相册选取
    private static final int REQUEST_CAPTURE = 2;  //拍照
    private static final int REQUEST_PICTURE_CUT = 3;  //剪裁图片
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private Uri imageUri;//原图保存地址
    private boolean isClickCamera;
    private String imagePath;
    private String name;
    private String phoneNum;
    private String token;
    private String user_id;

    @Override
    public void initViews() {

        setContentView(R.layout.activity_person_information_activity);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        topName.setText("个人中心");
        tvSave.setTextColor(Color.parseColor("#009688"));
        tvSave.setVisibility(View.VISIBLE);

       name= getIntent().getStringExtra("name");
       phoneNum=getIntent().getStringExtra("phoneNum");
       imagePath=getIntent().getStringExtra("imagePath");


    }

    @Override
    public void initData() {
        mPermissionsChecker = new PermissionsChecker(this);

    }

    @Override
    public void initEvents() {


        if(!TextUtils.isEmpty(name)){

            edName.setText(name);

        }
        if(!TextUtils.isEmpty(phoneNum)){

         edPhoneNum.setText(phoneNum);

        }
        if(!TextUtils.isEmpty(imagePath)){

            Glide.with(mContext)
                    .load(imagePath)
                    .override(300,300)
                    .transform(new GlideCircleTransform(mContext))
                    .into(ivPhoto);

//            Glide.with(mContext)
//                    .load(imagePath)
//                    .transform(new GlideCircleTransform(mContext))
//                    .into(ivPhoto);
        }
    }



    @OnClick({R.id.back_btn, R.id.tv_save, R.id.iv_photo, R.id.btn_change_username, R.id.btn_quit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:

                finish();

                break;
            case R.id.tv_save:

//                ToastUtil.showToast("success",this);

                name =edName.getText().toString().trim();
                phoneNum =edPhoneNum.getText().toString().trim();
                token = Sptools.getString(this, Mycontants.API_TOKEN,"");
                user_id =Sptools.getString(this,Mycontants.USER_ID,"");

                if(TextUtils.isEmpty(token)){

                    ToastUtil.showToast("TOKEN错误",this);
                }else if(TextUtils.isEmpty(user_id)){

                    ToastUtil.showToast("用户ID错误",this);
                }else if(TextUtils.isEmpty(name)){

                    ToastUtil.showToast("请输入姓名",this);
                }else if(TextUtils.isEmpty(phoneNum)){

                    ToastUtil.showToast("请输入手机号",this);
                }else if(TextUtils.isEmpty(imagePath)){

                    ToastUtil.showToast("请选择图片",this);
                }else{

                    UpLoadPersonInformation(user_id,token,imagePath,name,phoneNum);

                    Log.e("abc", "onViewClicked: ====>"+ new File(imagePath).length() );
                }

                break;
            case R.id.iv_photo:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                //检查权限(6.0以上做权限判断)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                                        startPermissionsActivity();
                                    } else {
                                        openCamera();
                                    }
                                } else {
                                    openCamera();
                                }
                                isClickCamera = true;

                                break;
                            case 1:
                                //打开选择,本次允许选择的数量

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                                        startPermissionsActivity();
                                    } else {
                                        selectFromAlbum();
                                    }
                                } else {
                                    selectFromAlbum();
                                }
                                isClickCamera = false;

                                break;
                            default:
                                break;
                        }

                    }
                }, names);



                break;
            case R.id.btn_change_username:


                Intent intent = new Intent();
                intent.setAction(MainActivity.REFURSH_MESSAGE_COUNT);
                intent.putExtra("refrush", "finished");
                sendBroadcast(intent);
                intentMethod.startMethodOne(this, LoginActivity.class);

                break;
            case R.id.btn_quit:

                MyApplication.getInstance().exit();


                break;
        }
    }


    private void UpLoadPersonInformation(String user_id,String api_token,String filepath,String name,String phone){

        RequestParams params=new RequestParams();

        params.addBodyParameter("user_id",user_id);
        params.addBodyParameter("api_token",api_token);
        params.addBodyParameter("file",new File(filepath));
        params.addBodyParameter("name",name);
        params.addBodyParameter("phone",phone);

        HttpTool.getInstance(this).httpWithParams(Url.UPLOAD_PERSON_INFORMATION, params, new SMObjectCallBack<BaseModel>() {

            @Override
            public void onSuccess(BaseModel baseModel) {

                if (baseModel.getCode()==200){

                    ToastUtil.showToast(baseModel.getMsg(),PersonInformationActivity.this);
                    finish();

                }else{

                    ToastUtil.showToast(baseModel.getMsg(),PersonInformationActivity.this);

                }

            }

            @Override
            public void onError(int error, String msg) {

            }
        });






    }









    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }


    /**
     * 打开系统相机
     */
    private void openCamera() {
        File file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            imageUri = FileProvider.getUriForFile(this, ProviderUtil.getFileProviderName(this), file);

//            imageUri = FileProvider.getUriForFile(PersonInformationActivity.this, "com.jinganweigu.RoadWayFire.fileprovider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            imageUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 从相册选择
     */
    private void selectFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    /**
     * 裁剪
     */
    private void cropPhoto() {
        File file = new FileStorage().createCropFile();
        Uri outputUri = Uri.fromFile(file);//缩略图保存地址
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_PICTURE_CUT);
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_PERMISSION,
                PERMISSIONS);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE://从相册选择
                if (Build.VERSION.SDK_INT >= 19) {
                    handleImageOnKitKat(data);
                } else {
                    handleImageBeforeKitKat(data);
                }
                break;
            case REQUEST_CAPTURE://拍照
                if (resultCode == RESULT_OK) {
                    cropPhoto();
                }
                break;
            case REQUEST_PICTURE_CUT://裁剪完成
                Bitmap bitmap = null;
                try {
                    if (isClickCamera) {

                        bitmap=getBitmapFormUri(PersonInformationActivity.this,imageUri);

//                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    } else {
                        bitmap = BitmapFactory.decodeFile(imagePath);

//                        bitmap=compressPixel(imagePath);
                    }





//                    ivPhoto.setImageBitmap(bitmap);


//                    Glide.with(mContext).load(imageUri).centerCrop().placeholder(R.drawable.event_list_fail_pic)
//                            .transform(new GlideCircleTransform(mContext))
//                            .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivPhoto);

                    Glide.with(mContext)
                            .load(imagePath)
                            .override(300,300)
                            .transform(new GlideCircleTransform(mContext))
                            .into(ivPhoto);
//                    iv.setImageBitmap(bitmap);



                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_PERMISSION://权限请求
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    finish();
                } else {
                    if (isClickCamera) {
                        openCamera();
                    } else {
                        selectFromAlbum();
                    }
                }
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        imageUri = data.getData();
        if (DocumentsContract.isDocumentUri(this, imageUri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(imageUri);
            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(imageUri, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = imageUri.getPath();
        }

        cropPhoto();
    }
    private void handleImageBeforeKitKat(Intent intent) {
        imageUri = intent.getData();
        imagePath = getImagePath(imageUri, null);
        cropPhoto();
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection老获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }




    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

}
