package com.example.dell_pc.qr_code_patrol.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dell-pc on 2016/2/28.
 */
public class MyAllCheckCardDB extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "Check.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "check_card_table";

    public final static String ID = "id";
    public final static String Card_NO = "check_card_No";
    public final static String POSITION = "position";
    public final static String FAC = "fac";
    public final static String PROBLEM_TYPE = "problem_type";


    public MyAllCheckCardDB(Context context) {
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
                + " INTEGER primary key autoincrement, " + Card_NO + " text, " + FAC + " text, " + PROBLEM_TYPE + " text, " + POSITION + " text);");


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
    public long insert(String card_NO, String position, String fac, String problemType) {
        SQLiteDatabase db = this.getWritableDatabase();
/* ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(Card_NO, card_NO);
        cv.put(POSITION, position);
        cv.put(FAC, fac);
        cv.put(PROBLEM_TYPE, problemType);
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

    public String getFac(String cardNo) {
        String Fac = null;
        SQLiteDatabase mDb = this.getWritableDatabase();
        String queryString = "select fac from " + TABLE_NAME + " where " + Card_NO + " =?";
        Cursor c1 = mDb.rawQuery(queryString, new String[]{cardNo});
        if (c1.moveToNext()) {
            Fac = c1.getString(0);
        }
        ;
        return Fac;
    }

    public String getProblemType(String cardNo) {
        String problem = null;
        SQLiteDatabase mDb = this.getWritableDatabase();
        String queryString = "select " + PROBLEM_TYPE + " from " + TABLE_NAME + " where " + Card_NO + " =?";
        Cursor c1 = mDb.rawQuery(queryString, new String[]{cardNo});
        if (c1.moveToNext()) {
            problem = c1.getString(0);
        }

        return problem;
    }


}