package com.example.dell_pc.qr_code_patrol.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dell-pc on 2016/2/28.
 */
public class MyAllShopCardDB extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "Shop.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "shop_card_table";

    public final static String ID = "id";
    public final static String SHOP_ID = "shop_id";
    public final static String SHOP_NAME = "shop_name";
    public final static String CARD_NO = "card_No";
    public final static String LINKMAN = "linkman";
    public final static String PHONE = "phone";


    public MyAllShopCardDB(Context context) {
// TODO Auto-generated constructor stub
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getReadableDatabase();
        //delete();
        onCreate(db);
    }

    //创建table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID
                + " INTEGER primary key autoincrement, " + SHOP_ID + " text, " + SHOP_NAME + " text, " + CARD_NO + " text, " + LINKMAN + " text, " + PHONE + " text);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }


    public Cursor select() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db
                .query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }


    public Cursor select1() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db
                .query(TABLE_NAME, null, null, null, null, null, null);

        Cursor c = db.rawQuery("select * from check_card_table order by id desc", null);
        return c;
    }


    //增加操作
    public long insert(String card_NO, String shopID, String shopName, String linkman, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
/* ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(CARD_NO, card_NO);
        cv.put(SHOP_ID, shopID);
        cv.put(SHOP_NAME, shopName);
        cv.put(LINKMAN, linkman);
        cv.put(PHONE, phone);
        long row = db.insert(TABLE_NAME, null, cv);
        return row;
    }


    public void delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public Integer isMyCard(String cardNO) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where check_card_No = '" + cardNO + "'", null);
        int Count = c.getCount();
        return Count;
    }

    public String getShopID(String cardNo) {
        String shopID = null;
        SQLiteDatabase mDb = this.getWritableDatabase();
        String queryString = "select " + SHOP_ID + " from " + TABLE_NAME + " where " + CARD_NO + " =?";
        Cursor c1 = mDb.rawQuery(queryString, new String[]{cardNo});
        if (c1.moveToNext()) {
            shopID = c1.getString(0);
        }
        ;
        return shopID;
    }


    public String getShopName(String cardNo) {
        String shopName = null;
        SQLiteDatabase mDb = this.getWritableDatabase();
        String queryString = "select " + SHOP_NAME + " from " + TABLE_NAME + " where " + CARD_NO + " =?";
        Cursor c1 = mDb.rawQuery(queryString, new String[]{cardNo});
        if (c1.moveToNext()) {
            shopName = c1.getString(0);
        }
        ;
        return shopName;
    }

    public String getLinkman(String cardNo) {
        String linkman = null;
        SQLiteDatabase mDb = this.getWritableDatabase();
        String queryString = "select " + LINKMAN + " from " + TABLE_NAME + " where " + CARD_NO + " =?";
        Cursor c1 = mDb.rawQuery(queryString, new String[]{cardNo});
        if (c1.moveToNext()) {
            linkman = c1.getString(0);
        }
        ;
        return linkman;
    }

    public String getPhone(String cardNo) {
        String phone = null;
        SQLiteDatabase mDb = this.getWritableDatabase();
        String queryString = "select " + PHONE + " from " + TABLE_NAME + " where " + CARD_NO + " =?";
        Cursor c1 = mDb.rawQuery(queryString, new String[]{cardNo});
        if (c1.moveToNext()) {
            phone = c1.getString(0);
        }
        ;
        return phone;
    }


}