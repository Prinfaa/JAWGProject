package com.example.dell_pc.qr_code_patrol.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dell_pc.qr_code_patrol.item.PatrolItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell-pc on 2016/2/28.
 */
public class PatrolDB extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "Patrol.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "patrol_table";


    public final static String ID = "patrol_id";
    public final static String Card_NO = "patrol_card_No";
    public final static String POSITION = "position";
    public final static String TIME = "patrol_time";


    public PatrolDB(Context context) {
// TODO Auto-generated constructor stub
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getReadableDatabase();
        onCreate(db);
    }


    //创建table
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID
                + " INTEGER primary key autoincrement, " + Card_NO + " text, " + POSITION + " text, " + TIME + " text);");
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
    public long insert(String card_NO, String position, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
/* ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(Card_NO, card_NO);
        cv.put(POSITION, position);
        cv.put(TIME, time);
        long row = db.insert(TABLE_NAME, null, cv);
        return row;
    }

    //删除操作
    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = ID + " = ?";
        String[] whereValue = {Integer.toString(id)};
        db.delete(TABLE_NAME, where, whereValue);
    }

    //删除操作
    public void deleteByCardNo(String cardNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = Card_NO + " = ?";
        String[] whereValue = {cardNo};
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

    public List<PatrolItem> getAllData() {

        List<PatrolItem> patrolList = new ArrayList<PatrolItem>();
        SQLiteDatabase mDb = this.getWritableDatabase();

        Cursor c = mDb.rawQuery("select * from " + TABLE_NAME + " order by " + ID, null);
        // 获取表的内容
        while (c.moveToNext()) {
            String cardNo = null;
            String position = null;
            String dateTime = null;

            cardNo = c.getString(1);
            position = c.getString(2);
            dateTime = c.getString(3);

            patrolList.add(new PatrolItem(cardNo, position, dateTime));
        }

        return patrolList;
    }


    public String getPosition(String cardNo) {
        String Position = null;
        SQLiteDatabase mDb = this.getWritableDatabase();
        String queryString = "select position from " + TABLE_NAME + " where " + Card_NO + " =?";
        Cursor c1 = mDb.rawQuery(queryString, new String[]{cardNo});
        if (c1.moveToNext()) {
            Position = c1.getString(0);
        }
        ;

        return Position;
    }


    public Integer getCount() {
        SQLiteDatabase mDb = this.getWritableDatabase();
        Cursor c = mDb.rawQuery("select * from " + TABLE_NAME, null);
        int Count = c.getCount();
        return Count;
    }

}