package com.example.marieniel.registration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "registration.db";
    public static final String TABLE_NAME = "customerinfo_tbl";
    public static final String COL1 = "ID";
    public static final String COL2 = "FULLNAME";
    public static final String COL3 = "COMPANY";
    public static final String COL4 = "DESIGNATION";
    public static final String COL5 = "CONTACTNUMBER";
    public static final String COL6 = "EMAIL";
    public static final String COL7 ="ENABLEUPDATE";
    public static final String COL8 ="DATETIME";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, FULLNAME TEXT, COMPANY TEXT, DESIGNATION TEXT,CONTACTNUMBER TEXT, EMAIL TEXT, ENABLEUPDATE TEXT, DATETIME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertDATA (String fullname, String company, String designation, String contactnumber, String email, String enableupdate, String datetime){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,fullname);
        contentValues.put(COL3,company);
        contentValues.put(COL4,designation);
        contentValues.put(COL5,contactnumber);
        contentValues.put(COL6,email);
        contentValues.put(COL7,enableupdate);
        contentValues.put(COL8,datetime);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+ TABLE_NAME,null);
        return result;
    }
}
