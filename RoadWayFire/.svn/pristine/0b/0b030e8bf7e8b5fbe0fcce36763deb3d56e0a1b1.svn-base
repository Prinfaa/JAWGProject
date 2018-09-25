package com.jinganweigu.RoadWayFire.BeseClassUtils;

import android.content.Context;
import android.text.TextUtils;

import com.jinganweigu.RoadWayFire.Interfaces.Mycontants;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.BaseUtils;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.Sptools;
import com.jinganweigu.RoadWayFire.ToolsUtils.GeneralUtil.ToastUtil;

import java.util.List;

/**
 * @作者: 房玉虎
 * @创建时间: 16-10-27
 * @功能描述:表单验证类
 */
public abstract class FormValidation {

    private final static int AGREE = 1;//同意服务协议

    /**
     * 注册验证
     *
     * @param context         上下文
     * @param phone           手机
     * @param capture         验证码
     * @param password        密码
     * @param phoneCaptcha    获取验证码时的手机号
     * @param onSuccess       符合后调用的方法
     */
    public static void validation(Context context, String phone, String capture, String password, String phoneCaptcha, OnSuccess onSuccess) {

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast("请输入手机号", context);
        } else if (!BaseUtils.isMobiles(phone)) {
            ToastUtil.showToast("请输入正确的手机号", context);
        } else if (TextUtils.isEmpty(capture)) {
            ToastUtil.showToast("请输入验证码", context);
        } else if (!phone.equals(phoneCaptcha)) {
            ToastUtil.showToast("注册手机号与验证手机号不一致", context);
        } else if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast("请输入密码", context);
        } else if (password.length() < 6 ) {
            ToastUtil.showToast("亲,密码个数不能少于6位哦", context);
        } else if (password.length() > 20 ) {
            ToastUtil.showToast("亲,密码个数不能多于20位哦", context);
//        } else if (TextUtils.isEmpty(passwordConfirm)) {
//            ToastUtil.showToast("请再次输入密码", context);
//        } else if (!password.equals(passwordConfirm)) {
//            ToastUtil.showToast("两次输入密码不一致", context);
//        } else if (agree == 1 && from != 1) {
//            ToastUtil.showToast("请选择同意服务协议", context);
        } else {

            onSuccess.onSuccess();

        }

    }
    /**
     * 注册验证
     *  @param context         上下文
     * @param phone           手机
     * @param capture         验证码
     * @param password        密码
     * @param passwordConfirm 确认密码
     * @param phoneCaptcha    获取验证码时的手机号
     * @param oldPwd
     * @param onSuccess       符合后调用的方法
     */
    public static void validation2(Context context, String phone, String capture, String password, String passwordConfirm, String phoneCaptcha, String oldPwd, OnSuccess onSuccess) {

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast("请输入手机号", context);
        } else if (!BaseUtils.isMobiles(phone)) {
            ToastUtil.showToast("请输入正确的手机号", context);
        } else if (TextUtils.isEmpty(capture)) {
            ToastUtil.showToast("请输入验证码", context);
        } else if (!phone.equals(phoneCaptcha)) {
            ToastUtil.showToast("注册手机号与验证手机号不一致", context);
        } else if (TextUtils.isEmpty(oldPwd)) {
            ToastUtil.showToast("请输入旧密码", context);
        }else if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast("请输入新密码", context);
        } else if (password.length() < 6 || password.length() > 20) {
            ToastUtil.showToast("新密码格式不正确", context);
        } else if (TextUtils.isEmpty(passwordConfirm)) {
            ToastUtil.showToast("请再次输入新密码", context);
        } else if (!password.equals(passwordConfirm)) {
            ToastUtil.showToast("两次输入密码不一致", context);
        } else {

            onSuccess.onSuccess();

        }

    }

    /**
     * 修改密码验证
     *
     * @param context         上下文
     * @param oldPassword     旧密码
     * @param newPassword     新密码
     * @param passwordConfirm 确认新密码
     * @param onSuccess       符合后调用的方法
     */
    public static void CheckChangePassword(Context context, String oldPassword, String newPassword, String passwordConfirm, OnSuccess onSuccess) {

        if (TextUtils.isEmpty(oldPassword)) {
            ToastUtil.showToast("请输入旧密码", context);
        } else if (oldPassword.length() < 6 || oldPassword.length() > 20) {
            ToastUtil.showToast("旧密码格式不正确", context);
        } else if (TextUtils.isEmpty(newPassword)) {
            ToastUtil.showToast("请输入新密码", context);
        } else if (newPassword.length() < 6 || newPassword.length() > 20) {
            ToastUtil.showToast("新密码格式不正确", context);
        } else if (newPassword.equals(oldPassword)) {
            ToastUtil.showToast("新密码与旧密码不能相同", context);
        } else if (TextUtils.isEmpty(passwordConfirm)) {
            ToastUtil.showToast("请再次输入新密码", context);
        } else if (!newPassword.equals(passwordConfirm)) {
            ToastUtil.showToast("两次新密码输入不一致", context);
        } else {

            onSuccess.onSuccess();

        }

    }


    /**
     * @param context        上下文
     * @param phone          手机号
     * @param username       收货人姓名
     * @param locationDetail 收货人姓名
     * @param area
     * @param postcode
     * @param onSuccess      符合后调用的方法
     */
    public static void checkAddAddress(Context context, String phone, String username, String locationDetail, String area, String postcode, OnSuccess onSuccess) {

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast("请输入手机号", context);
        } else if (phone.length()<8 || phone.length()>12) {
            ToastUtil.showToast("请输入正确的联系方式", context);
        } else if (TextUtils.isEmpty(username)) {
            ToastUtil.showToast("请输入收货人姓名", context);
        } else if (TextUtils.isEmpty(area)) {
            ToastUtil.showToast("请选择省市", context);
        } else if (TextUtils.isEmpty(locationDetail)) {
            ToastUtil.showToast("请输入详细地址", context);
        } else {

            onSuccess.onSuccess();

        }

    }


    public static void checkEmpty(Context context, String cateId, String price, String identity1, String identity2, List<String> list, OnSuccess onSuccess) {

        if (TextUtils.isEmpty(cateId)) {
            ToastUtil.showToast("请选择工种", context);
        } else if (TextUtils.isEmpty(price)) {
            ToastUtil.showToast("请输入价格", context);
        } else if (TextUtils.isEmpty(identity1) | TextUtils.isEmpty(identity2)) {
            ToastUtil.showToast("请上传身份证", context);
        } else if (list.size() == 0) {
            ToastUtil.showToast("请上传资质证明", context);
        } else {

            onSuccess.onSuccess();

        }

    }


    public interface OnSuccess {

        void onSuccess();

    }

    //是否登录
    public static void ifLogin(Context context, OnSuccess onSuccess) {
        boolean ifLogin = Sptools.getBoolean(context, Mycontants.IF_LOGIN, false);
        if (ifLogin) {
            //已登录
            onSuccess.onSuccess();

        } else {
            ToastUtil.showToast("请先登录", context);
//            new LoadImageUtil.IntentMethod().startMethodTwo(context, LoginActivity.class);
        }
    }

}
