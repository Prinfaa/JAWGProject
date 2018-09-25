package com.jinganweigu.RoadWayFire.Interfaces;

/**
 * url常量类
 */
public interface Url {

    String EZUIkit_LOGIN="https://open.ys7.com/api/lapp/token/get";
    //    String IMAGE_IP = "http://shopx.senmaisoft.com";//主机地址
//    String IMAGE_IP = "http://dndzl.cn/warehouse/";//主机地址
//    String HOME_IP = "http://dndzl.cn/lane_occupancy/";//主机地址
    String HOME_IP = "http://ali.fis119.com/htdocs/lane_occupancy/";//主机地址

    String LOGIN_IN=HOME_IP+"login.php";//登录接口
    String FORGET_PASSSWORD_GET_CODE=HOME_IP+"SUBMAIL_PHP_SDK-master/demo/message_send.php";//获取验证啊
    String SET_PASSWORD_SUBMENT=HOME_IP+"update_new_pass.php";//提交新密码
    String COVER_EZvIDEO_DEVICE_INFO=HOME_IP+"camera_map.php";//地图中覆盖物的信息
    String GET_PIRCTURE_FROM_MONTH=HOME_IP+"select_incident.php";//查询某月下报警图片
    String GET_CONTACTS_LIST=HOME_IP+"address_list.php";//获取电话列表
    String GET_PUBLIC_MESSAGE=HOME_IP+"alarm_msg.php";//公共报警信息
//    String GET_PERSON_MESSAGE_LIST=HOME_IP+"transpond_list.php";//获取个人消息列表
    String GET_ALARM_EVENT_IMAGE=HOME_IP+"select_photo.php";//获取事件图片
    String SEND_MESSAGE_BACK_VALUE="http://ali.fis119.com/php/transmit.php";//发送短信的返回值
    String CONTACTS_LIST_SEARCH=HOME_IP+"select_list.php";//通讯录查询接口
    String GET_PERSON_MESSAGE_DATA=HOME_IP+"transpond_msg.php";//转发信息详情
    String DRTAIL_ALARM_MESSAGE=HOME_IP+"eliminate_alarm.php";//处警
    String DETAIL_TYPE=HOME_IP+"warning_type.php";//处警方式
    String GET_ALRAM_MESSAGE_ALL_COUNT=HOME_IP+"number_msg.php";//报警短信总数量
//    String UNREAD_MESSAGE_CEHNGE_READED=HOME_IP+"update_war_push.php";//将未读短信改为已读
    String GET_PERSON_INFORMATION=HOME_IP+"personal_information.php";//获取个人信息
    String UPLOAD_PERSON_INFORMATION="http://www.fis119.com/picture/upload.php";//上传个人信息
    String UPDATE_MESSAGE_NUM=HOME_IP+"update_deal.php";//更新短信数量
    String GET_STATISTICS_DATA=HOME_IP+"graph.php";//获取统计图表数据
    String GET_STATISTICS_DATA_ON_FORM=HOME_IP+"statistical_information.php";//总统计界面上部分信息
    String INSERT_USER_TO_COMPANY=HOME_IP+"insert_user.php";//添加人员
    String DELATE_USER_MANAGER=HOME_IP+"delete_user.php";//人员管理中删除人员
    String ADD_CLASS_MANAGER=HOME_IP+"insert_classes.php";//添加班次
    String GET_CLASS_LIST_MANAGET=HOME_IP+"select_classes.php";//获取班次列表
    String GET_CLASS_INNER_PERSON=HOME_IP+"select_user_cla.php";//获取班次内人员
    String DELATE_CLASS_LIST_ITEM=HOME_IP+"delete_classes.php";//删除班次
    String UPDATE_PASSWORD=HOME_IP+"update_password.php";//修改密码
    String DELATE_USER_IN_CLA=HOME_IP+"delete_user_cla.php";//删除班次中人员
    String ADD_USER_IN_CLA=HOME_IP+"insert_user_cla.php";//添加班次中人员
    String URL_MESSAGE_LIST=HOME_IP+"msg_list.php";//消息列表













}
