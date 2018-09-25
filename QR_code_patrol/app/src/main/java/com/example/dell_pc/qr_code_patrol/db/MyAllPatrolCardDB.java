package com.example.dell_pc.qr_code_patrol.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dell-pc on 2016/2/28.
 */
public class MyAllPatrolCardDB extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "Patrol.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "patrol_card_table";

    public final static String ID = "id";
    public final static String Card_NO = "patrol_card_No";
    public final static String POSITION = "position";


    public MyAllPatrolCardDB(Context context) {
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
                + " INTEGER primary key autoincrement, " + Card_NO + " text, " + POSITION + " text);");


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

        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " order by id desc", null);
        return c;
    }

    public int count(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db
                .query(TABLE_NAME, null, null, null, null, null, null);

        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " order by id desc", null);
        int count = c.getCount();
        return count;
    }


    //增加操作
    public long insert(String card_NO, String position) {
        SQLiteDatabase db = this.getWritableDatabase();
/* ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(Card_NO, card_NO);
        cv.put(POSITION, position);
        long row = db.insert(TABLE_NAME, null, cv);

        return row;
    }


    public void delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public Integer isMyCard(String cardNO) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where patrol_card_No = '" + cardNO + "'", null);
        int Count = c.getCount();
        return Count;
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

    public String getCode(int index){
        String Position = null;
        SQLiteDatabase mDb = this.getWritableDatabase();
        String queryString = "select patrol_card_No from " + TABLE_NAME ;
        Cursor c = mDb.rawQuery(queryString,null);
        return c.getString(index);
//        if (c1.moveToNext()) {
//            Position = c1.getString(0);
//        }
//        ;
//        return Position;
    }

    public String getPositionById(int index){
        String Position = null;
        SQLiteDatabase mDb = this.getWritableDatabase();
        String queryString = "select " + POSITION + " from " + TABLE_NAME ;
        Cursor c = mDb.rawQuery(queryString,null);
//        return c.getString(index);
        for (int i=0;i<c.getCount();i++){
            c.moveToNext();
            if(i==index){
                Position = c.getString(0);
            }
        }
        return Position;
    }
    public String getCodeById(int index){
        String Position = null;
        SQLiteDatabase mDb = this.getWritableDatabase();
        String queryString = "select " + Card_NO + " from " + TABLE_NAME ;
        Cursor c = mDb.rawQuery(queryString,null);
//        return c.getString(index);
        for (int i=0;i<c.getCount();i++){
            c.moveToNext();
            if(i==index){
                Position = c.getString(0);
            }
        }
        return Position;
    }


}