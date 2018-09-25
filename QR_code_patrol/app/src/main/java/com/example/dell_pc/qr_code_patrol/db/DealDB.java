package com.example.dell_pc.qr_code_patrol.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dell_pc.qr_code_patrol.item.DealItem;
import com.example.dell_pc.qr_code_patrol.item.ShopCheckedItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell-pc on 2016/2/28.
 */
public class DealDB extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "Deal.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "deal_table";


    public final static String ID = "id";
    public final static String Card_NO = "card_No";
    public final static String RECORD_ID = "record_id";
    public final static String TIME = "check_time";
    public final static String IMAGE_NAME = "image_name";


    public DealDB(Context context) {
// TODO Auto-generated constructor stub
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getReadableDatabase();
        onCreate(db);
    }


    //创建table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID
                + " INTEGER primary key autoincrement, " + Card_NO + " text, " + RECORD_ID + " text, " +  TIME + " text, " + IMAGE_NAME + " text);");
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

    //增加操作
    public long insert(String card_NO, String recordID,  String time, String imageName) {
        SQLiteDatabase db = this.getWritableDatabase();
        /* ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(Card_NO, card_NO);
        cv.put(RECORD_ID,recordID);
        cv.put(TIME, time);
        cv.put(IMAGE_NAME, imageName);

        long row = db.insert(TABLE_NAME, null, cv);
        return row;
    }

    //删除操作
    public void delete(String imageName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = IMAGE_NAME + " = ?";
        String[] whereValue = {imageName};
        db.delete(TABLE_NAME, where, whereValue);
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    //修改操作
    public void update(int id, String card_NO, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = ID + " = ?";
        String[] whereValue = {Integer.toString(id)};

        ContentValues cv = new ContentValues();
        cv.put(Card_NO, card_NO);
        cv.put(TIME, time);
        db.update(TABLE_NAME, cv, where, whereValue);
    }

    public  List<DealItem> getLocalRecord() {
        List<DealItem> dealItemList;
        SQLiteDatabase mDb = this.getWritableDatabase();

        Cursor c = mDb.rawQuery("select * from " + TABLE_NAME + " order by id", null);

        dealItemList = new ArrayList<DealItem>();

        while (c.moveToNext()) {
            String strCardNo = c.getString(1);
            String strRecordID = c.getString(2);
            String strTime = c.getString(3);
            String strImageName = c.getString(4);

            dealItemList.add(new DealItem(strCardNo, strRecordID, strTime, strImageName));
        }

        return dealItemList;
    }







    public Integer getCount() {
        SQLiteDatabase mDb = this.getWritableDatabase();
        Cursor c = mDb.rawQuery("select * from " + TABLE_NAME, null);
        int Count = c.getCount();
        return Count;
    }

}