package com.jinganweigu.entrysystem.utils;

/**
 * Created by Administrator on 2018/3/7 0007.
 */

public class HttpCode {

    public static final int REQUEST_SUCCESS = 200;//操作成功，请求成功

    public static final int CODE_ERROR = 10001;//参数错误，参数为空或格式不正确

    public static final int DEVICE_EXIST = 10002;//该设备已存在

    public static final int USERNAME_OR_PASSWORD_ERROR = 10003;//账号或密码错误

    public static final int IMEI_ERROR = 10004;//记录IMEI失败

    public static final int BIND_PHONE_SUCCESS = 10005;//绑定手机成功

    public static final int USER_NAME_INCORRECT = 10006;//账号与绑定手机不匹配

    public static final int NO_DEVICE = 10007;// 没有该设备

    public static final int FINISH_PRODUCT_INTO_HOUSE = 10008;// 该成品已入库

    public static final int THE_DEVICE_OUT_HOUSE = 10009;// 该设备已出库

    public static final int PLAN_INTO_HOUSE_EXAMINE_NULL = 10010;// 预入库审核为空

    public static final int THE_FINISHED_PRODUCT_INTO_HOUSE_SUCCESS = 10011;// 该成品已预入库

    public static final int THE_DEVICE_IS_SECOND_ENTER_HOUSE=10014;//该设备为二次入库设备

    public static final int THE_EZDEVICE_NOT_EXIST = 20002;// 设备不存在 该接口出现这个错误码表示设备未注册至萤石云

    public static final int THE_DEVICE_NOT_ONLINE = 20007;// 设备不在线 检查设备是否在线

    public static final int CHECK_CODE_ERROR = 20010;// 设备验证码错误 检查设备验证码是否错误

    public static final int THE_DEVICE_ADD_ERROR = 20011;// 设备添加失败 检查设备网络等是否正常

    public static final int THE_DEVICE_ADDED_BY_OTHER = 20013;// 设备已被别人添加 该设备已被别的账号添加

    public static final int THE_DEVICE_SERIAL_ERROR = 20014;// deviceSerial不合法

    public static final int THE_DEVICE_ADDED_BY_OWN = 20017;// 设备已被自己添加 设备已经添加到该账号下

    public static final int THE_DATA_ERROR = 49999;// 数据异常，接口调用异常


}
