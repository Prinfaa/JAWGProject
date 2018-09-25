package com.jinganweigu.entrysystem.utils.http;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.jinganweigu.entrysystem.utils.LoadImageUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import com.jinganweigu.entrysystem.R;
import com.jinganweigu.entrysystem.activities.LoginActivity;
import com.jinganweigu.entrysystem.utils.BaseUtils;

import java.io.File;
import java.util.List;

import static com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET;
import static com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST;

public class HttpTool {
    private static HttpUtils httpUtils;
    private static HttpTool httpTool;
    private Context context;
    public static boolean isDeleteUser = true;

    public static HttpTool getInstance(Context context) {
        synchronized (HttpTool.class) {
            if (httpTool == null) {
                httpTool = new HttpTool(context);
            }
        }
        return httpTool;
    }

    private HttpTool(Context context) {
        this.context = context;
        httpUtils = new HttpUtils();
        //缓存时间内直接返回上次的结果，HttpUtils对于GET请求采用了LRU缓存处理
        httpUtils.configCurrentHttpCacheExpiry(0);
    }

    public void http(String url, final SMObjectCallBack callBack) {
        final boolean isNetConnected = NetWorkUtil.isNetConnected(context);
        httpUtils.send(POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Log.d("###", "请求结果 " + result);
                if (result == null) {
                    if (callBack != null) {
                        callBack.onError(SMError.ERROR_SERVER, context.getString(R.string.service_error));
                    }
                } else {
                    if (callBack != null) {
                        try {
                            Object data = JSON.parseObject(result, callBack.getClazz());
                            callBack.onSuccess(data);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callBack.onError(SMError.DATA_PARSE_ERROR, "数据解析异常");
                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (callBack != null) {
                    if (!isNetConnected) {
                        callBack.onError(SMError.ERROR_NO_NET, "亲，没有网络了~");
                    } else {
                        callBack.onError(SMError.ERROR_SERVER, context.getString(R.string.service_error));
                    }
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                if (callBack != null) {
                    callBack.onLoading(total, current, isUploading);
                }
            }
        });
    }

    public void httpGet(String url, final SMObjectCallBack callBack) {
        final boolean isNetConnected = NetWorkUtil.isNetConnected(context);
        httpUtils.send(GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
//                Log.d("###", "请求结果 " + result);
                if (result == null) {
                    if (callBack != null) {
                        callBack.onError(SMError.ERROR_SERVER, context.getString(R.string.service_error));
                    }
                } else {
                    if (callBack != null) {
                        try {
                            Object data = JSON.parseObject(result, callBack.getClazz());
                            callBack.onSuccess(data);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callBack.onError(SMError.DATA_PARSE_ERROR, "数据解析异常");
                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (callBack != null) {
                    if (!isNetConnected) {
                        callBack.onError(SMError.ERROR_NO_NET, "亲，没有网络了~");
                    } else {
                        callBack.onError(SMError.ERROR_SERVER, context.getString(R.string.service_error));
                    }
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                if (callBack != null) {
                    callBack.onLoading(total, current, isUploading);
                }
            }
        });
    }

    public void httpDialog(String url, final SMObjectCallBack callBack) {
        final boolean isNetConnected = NetWorkUtil.isNetConnected(context);
        httpUtils.send(POST, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
//                Log.d("###", "请求结果 " + result);
                if (result == null) {
                    if (callBack != null) {
                        callBack.onError(SMError.ERROR_SERVER, context.getString(R.string.service_error));
                    }
                } else {
                    if (callBack != null) {
                        try {
                            Object data = JSON.parseObject(result, callBack.getClazz());
                            callBack.onSuccess(data);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callBack.onError(SMError.DATA_PARSE_ERROR, "数据解析异常");
                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (callBack != null) {
                    if (!isNetConnected) {
                        callBack.onError(SMError.ERROR_NO_NET, "亲，没有网络了~");
                    } else {
                        callBack.onError(SMError.ERROR_SERVER, context.getString(R.string.service_error));
                    }
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                if (callBack != null) {
                    callBack.onLoading(total, current, isUploading);
                }
            }
        });
    }


    public void uploadImage(String url, String path, final SMObjectCallBack callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("file", new File(path));
        httpUtils.send(POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
//                Log.d("上传图片：", "" + result);
                if (result == null) {
                    if (callBack != null) {
                        callBack.onError(SMError.ERROR_SERVER, context.getString(R.string.service_error));
                    }
                } else {
                    if (callBack != null) {
                        try {
                            Object data = JSON.parseObject(result, callBack.getClazz());
                            callBack.onSuccess(data);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callBack.onError(SMError.DATA_PARSE_ERROR, context.getString(R.string.service_error));
                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (callBack != null) {
                    callBack.onError(SMError.ERROR_SERVER, context.getString(R.string.service_error));
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }
        });
    }


    public void httpWithParams(String url, RequestParams params, final SMObjectCallBack callBack) {
        final boolean isNetConnected = NetWorkUtil.isNetConnected(context);
        httpUtils.send(POST, url, params, new RequestCallBack<String>() {

            private String mS;

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
//                Log.e("###", "with===>" + result);
                if (result == null) {
                    if (callBack != null) {
                        callBack.onError(SMError.ERROR_SERVER, context.getString(R.string.service_error));
                    }
                } else {
                    if (callBack != null) {
//                        try {
//                            //获取resultCode,msg
//                            if (result.length() > 100) {
//                                mS = result.substring(0, 100);
//                            } else {
//                                mS = result;
//                            }
//
//                            List<String> stringList = BaseUtils.multiSubString("\"", mS);
//
//                            Log.e("###", "stringList===>" + stringList.size()+"\n==tostring==>"+stringList.toString());
//
//                            String msg = stringList.get(2);
//
//                            Log.e("###", "msg===>" + msg);
//
//                            String Resultcode = msg.substring(1, msg.indexOf(","));
//
//                            Log.e("###", "mS===>" + Resultcode);
//
//                            String msg1 = stringList.get(5);
//
//                            Log.e("###", "msg1===>" + msg1);
//
//
//                            if (100 == Integer.parseInt(Resultcode.trim())) {
//                               /* BaseUtils.showAlertDialog2(context,"温馨提示",msg1, new BaseUtils.OnConfirm() {
//                                    @Override
//                                    public void onConfirm() {
//                                        new IntentMethod().startMethodTwo(context, LoginActivity.class);
//                                    }
//                                });*/
//                               /* AlertDialog dialog = new AlertDialog.Builder(context).setCancelable(false)
//                                        .setTitle("温馨提示").setMessage(msg1)
//                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface arg0, int arg1) {
//                                                new IntentMethod().startMethodTwo(context, LoginActivity.class);
//                                            }
//                                        }).create();
//                                dialog.show();*/
//
//                                //Toast.makeText(context, "12121212", Toast.LENGTH_SHORT).show();
//                                if (isDeleteUser) {
//                                    isDeleteUser = false;
//                                    /*if (mDelateUserListener != null) {
//                                        mDelateUserListener.OnDelateUser(msg1);
//                                    }*/
//                                    BaseUtils.showAlertDialog2(context,"温馨提示",msg1, new BaseUtils.OnConfirm() {
//                                        @Override
//                                        public void onConfirm() {
//                                            new LoadImageUtil.IntentMethod().startMethodTwo(context, LoginActivity.class);
//                                        }
//                                    });
//                                }
//                            }
                            Object data = JSON.parseObject(result, callBack.getClazz());
                            callBack.onSuccess(data);

//                        } catch (Exception e) {
//                            Log.e("###数据异常",e.toString()+"数据异常");
//                            e.printStackTrace();
//                            callBack.onError(SMError.DATA_PARSE_ERROR, "数据异常");
//                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
//                Log.e("###", "ads" + msg);
                if (callBack != null) {
                    if (!isNetConnected) {
                        callBack.onError(SMError.ERROR_NO_NET, "亲，没有网络了~");
                    } else {
                        callBack.onError(SMError.ERROR_SERVER, context.getString(R.string.service_error));
                    }
                }
            }
        });
    }



    //提供按钮点击接口
    public static OnDelateUserListener mDelateUserListener;

    public interface OnDelateUserListener {
        void OnDelateUser(String msg1);
    }

    public static void setOnDelateUserListener(OnDelateUserListener onDelateUserListener) {
        mDelateUserListener = onDelateUserListener;
    }

    public void httpWithParamsGet(String url, RequestParams params, final SMObjectCallBack callBack) {
        final boolean isNetConnected = NetWorkUtil.isNetConnected(context);
        httpUtils.send(GET, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
//                Log.d("####", "" + result);
                if (result == null) {
                    if (callBack != null) {
                        callBack.onError(SMError.ERROR_SERVER, context.getString(R.string.service_error));
                    }
                } else {
                    if (callBack != null) {
                        try {
                            Object data = JSON.parseObject(result, callBack.getClazz());
                            callBack.onSuccess(data);
                        } catch (Exception e) {
                            e.printStackTrace();
//                            callBack.onError(SMError.DATA_PARSE_ERROR, "数据异常");
                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (callBack != null) {
                    if (!isNetConnected) {
                        callBack.onError(SMError.ERROR_NO_NET, "亲，没有网络了~");
                    } else {
                        callBack.onError(SMError.ERROR_SERVER, context.getString(R.string.service_error));
                    }
                }
            }
        });
    }


}
