package com.jinganweigu.entrysystem.utils;

/**
 * url常量类
 */
public interface Url {

    //    String IMAGE_IP = "http://shopx.senmaisoft.com";//主机地址
//    String IMAGE_IP = "http://dndzl.cn/warehouse/";//主机地址
    String HOME_IP = "http://dndzl.cn/warehouse/";//主机地址
//    String IP = "http://shopxx.senmaisoft.com/Area/";//主机Api地址
    String URL_LOGIN=HOME_IP+"login.php";//登录接口
    String ENTER_HOUSE_URL=HOME_IP+"mitu_in_house.php";//成品预入库
    String NEW_DEVICE_ENTER_HOUSE_URL=HOME_IP+"in_new_mitu.php";//新预入库
    String BAD_ENTER_HOUSE_URL=HOME_IP+"waste_insert.php";//废品预入库
    String SURE_OUT_HOUSE=HOME_IP+"rtu_id_select.php";//查询设备信息接口
    String GOOD_ENTER_HOUSE_EXAMINE_LIST=HOME_IP+"product_preloaded_select.php";//成品预入库审核列表
    String WASTE_ENTER_HOUSE_EXAMINE_LIST=HOME_IP+"waste_select.php";//废品预入库审核列表
    String WASTE_DEVICE_ENTER_HOUSE_SURN=HOME_IP+"waste_update.php";//废品预入库变为入库
    String GOOD_ENTER_HOUSE_DELAGE_ONE_ENTRY=HOME_IP+"product_preloaded_delete.php";//成品预入库审核列表中删除一条
    String WASTE_ENTER_HOUSE_DELAGE_ONE_ENTRY=HOME_IP+"waste_delete.php";//废品预入库审核列表中删除一条
    String MAKE_SURE_READY_DEVICE_CHANE_INTO=HOME_IP+"product_preloaded_update.php";//将预入库产品，变为入库产品
    String WORKER_INFO_LIST=HOME_IP+"worker_info.php";//领用人列表
    String DEVICE_OUT_HOUSE_URL=HOME_IP+"advance_delivery.php";//成品预出库
    String DEVICE_OUT_HOUSE_LIST_URL=HOME_IP+"advance_delivery_select.php";//成品预出库列表
    String DEVICE_OUT_OUT_LIST_DELATE=HOME_IP+"advance_delivery_delete.php";//删除一条预出库设备
    String SURE_OUT_HOUSE_URL=HOME_IP+"advance_delivery_update.php";//审核列表确定出库接口

    String EXAMINE_ALL_DEVICE_NUM=HOME_IP+"check_statistics.php";//审核列表设备数量



    String GOOD_DEVICE_RETURN=HOME_IP+"nice_return.php";//正常成品退货
    String BAD_DEVICE_RETURN=HOME_IP+"bad_return.php";//损坏成品退货


    String DEVICE_STATISTICS_CHARTS_DATA=HOME_IP+"device_statistics.php";//设备统计圆饼图接口
    String DEVICE_STATISTICS_DATA=HOME_IP+"device_particulars.php";//设备统计圆饼图下边的数据
    String GET_STATISTICS_OUT_AND_INTO_DATA=HOME_IP+"statistics_page.php";//出入库统计柱状图
    String GET_STATISTICS_PROJECT_DATA=HOME_IP+"project_statistics.php";//项目统计柱状图数据
    String GET_STATISTICS_PROJECT_LIST_DATA= HOME_IP+"company_project.php";//项目统计列表数据
    String GET_STATISTICS_PERSON_DATA=HOME_IP+"personnel_statistics.php";//人员统计数据
    String GET_STATISTICS_WASTE_DATA=HOME_IP+"waster_statistic.php";//废品统计数据
    String GET_STATISTICS_RETURN_BACK_DATA=HOME_IP+"return_statistics.php";//退货统计数据
    String GET_STATISTICS_RETURN_BACK_DATA_DETAIL=HOME_IP+"waste_details.php";//退货统计数据详情

    String GET_UPDATE_VERSION=HOME_IP+"version_code.php";//获取最新版本










}
