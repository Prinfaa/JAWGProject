package com.example.dell_pc.qr_code_patrol.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dell_pc.qr_code_patrol.item.CheckCardItem;
import com.example.dell_pc.qr_code_patrol.item.CheckedItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell-pc on 2016/2/28.
 */
public class CheckDB extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "Check.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "check_table";
    private final static String TABLE_NAME1 = "check_card_table";


    public final static String ID = "id";
    public final static String Card_NO = "check_card_No";
    public final static String POSITION = "position";
    public final static String FAC = "fac";
    public final static String TIME = "check_time";
    public final static String IMAGE_NAME = "image_name";
    //    public final static String NOTE = "note";
    public final static String STATUE = "statue";
    public final static String PROBLEM_ID = "problem_id";
    public final static String PROBLEM = "problem";


    public CheckDB(Context context) {
// TODO Auto-generated constructor stub
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getReadableDatabase();
        onCreate(db);
    }


    //创建table
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID
                + " INTEGER primary key autoincrement, " + Card_NO + " text, " + TIME + " text, " + IMAGE_NAME + " text, " + PROBLEM_ID + " text, " + PROBLEM + " text, " + POSITION + " text, " + FAC + " text, " + STATUE + " text);");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME1 + " (" + ID
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

    //增加操作
    public long insert(String card_NO, String time, String imageName, String problemID, String problem, String strPosition, String Fac, String Statue) {
        SQLiteDatabase db = this.getWritableDatabase();
/* ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(Card_NO, card_NO);
        cv.put(TIME, time);
        cv.put(IMAGE_NAME, imageName);
        cv.put(PROBLEM_ID, problemID);
        cv.put(PROBLEM, problem);
        cv.put(POSITION, strPosition);
        cv.put(FAC, Fac);
        cv.put(STATUE, Statue);
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

    public List getAllCard() {
        List<CheckCardItem> checkCardItemList;
        SQLiteDatabase mDb = this.getWritableDatabase();

        Cursor c = mDb.rawQuery("select * from check_card_table order by id desc", null);

        //int columnsSize = c.getColumnCount();
        checkCardItemList = new ArrayList<CheckCardItem>();
        String cardNo = null;
        String Position = null;
        String Fac = null;
        // 获取表的内容
        //System.out.println("DBcount==================" + c.getCount());
        while (c.moveToNext()) {
            cardNo = c.getString(1);
            Position = c.getString(3);
            Fac = c.getString(2);
            checkCardItemList.add(new CheckCardItem(cardNo, Position, Fac));
        }

        return checkCardItemList;
    }


    public List<CheckedItem> getLocalRecord() {
        List<CheckedItem> checkedItemList;
        SQLiteDatabase mDb = this.getWritableDatabase();

        Cursor c = mDb.rawQuery("select * from " + TABLE_NAME + " order by id", null);

        //int columnsSize = c.getColumnCount();
        checkedItemList = new ArrayList<CheckedItem>();
        // 获取表的内容
        //System.out.println("DBcount==================" + c.getCount());


//        cv.put(Card_NO, card_NO);
//        cv.put(TIME, time);
//        cv.put(IMAGE_NAME, imageName);
//        cv.put(NOTE,Note);
//        cv.put(STATUE,Statue);

        while (c.moveToNext()) {
            String strCardNo = c.getString(1);
            String strTime = c.getString(2);
            String strImageName = c.getString(3);
            String strProblemID = c.getString(4);
            String strProblem = c.getString(5);
            String strPosition = c.getString(6);
            String strFac = c.getString(7);
            String strStatue = c.getString(8);
            checkedItemList.add(new CheckedItem(strCardNo, strPosition, strTime, strImageName, strFac, strProblem, strProblemID , strStatue));
        }

        return checkedItemList;
    }


    public List getAllData() {
        ArrayList<CheckedItem> checkList;
        SQLiteDatabase mDb = this.getWritableDatabase();

        Cursor c = mDb.rawQuery("select * from " + TABLE_NAME + " order by " + ID + " desc", null);

        //int columnsSize = c.getColumnCount();
        checkList = new ArrayList<CheckedItem>();
        // 获取表的内容
        while (c.moveToNext()) {
            String cardNo = null;
            String Position = null;
            String dateTime = null;
            String imageName = null;
            String Fac = null;
            String problem = null;
            String problemID = null;
            String Statue = null;

            //for (int i = 0; i < columnsSize; i++) {
            cardNo = c.getString(1);
            dateTime = c.getString(2);
            imageName = c.getString(3);
            problemID = c.getString(4);
            problem = c.getString(5);
            Statue = c.getString(8);

            //}
            Position = getPosition(cardNo);
            Fac = getFac(cardNo);
            checkList.add(new CheckedItem(cardNo, Position, dateTime, imageName, Fac, problem, problemID, Statue));
        }

        return checkList;
    }


    public String getPosition(String cardNo) {
        String Position = null;
        SQLiteDatabase mDb = this.getWritableDatabase();
        String queryString = "select position from " + TABLE_NAME1 + " where " + Card_NO + " =?";
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
        String queryString = "select fac from " + TABLE_NAME1 + " where " + Card_NO + " =?";
        Cursor c1 = mDb.rawQuery(queryString, new String[]{cardNo});
        if (c1.moveToNext()) {
            Fac = c1.getString(0);
        }
        ;
        return Fac;
    }

    public String getProblem(String id) {
        String problem = null;
        SQLiteDatabase mDb = this.getWritableDatabase();
        String queryString = "select " + PROBLEM + " from " + TABLE_NAME + " where " + ID + " =?";
        Cursor c1 = mDb.rawQuery(queryString, new String[]{id});
        if (c1.moveToNext()) {
            problem = c1.getString(0);
        }
        ;
        return problem;
    }

    public String getProblemID(String id) {
        String problemID = null;
        SQLiteDatabase mDb = this.getWritableDatabase();
        String queryString = "select " + PROBLEM_ID + " from " + TABLE_NAME + " where " + ID + " =?";
        Cursor c1 = mDb.rawQuery(queryString, new String[]{id});
        if (c1.moveToNext()) {
            problemID= c1.getString(0);
        }
        ;
        return problemID;
    }



    public Integer getCount() {
        SQLiteDatabase mDb = this.getWritableDatabase();
        Cursor c = mDb.rawQuery("select * from " + TABLE_NAME, null);
        int Count = c.getCount();
        return Count;
    }

}