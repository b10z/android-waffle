package com.example.b10z.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.EOFException;

/**
 * Created by b10z on 11/18/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASENAME = "storage.db";
    private static final String TABLE_NAME = "users_table";

    public DbHelper(Context context) {
        super(context, DATABASENAME, null, 2);
    }


    //default android methods
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table users_table " +
                        "(Id INTEGER PRIMARY KEY AUTOINCREMENT, Username TEXT NOT NULL,Password TEXT NOT NULL, Data TEXT)"
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users_table");
        onCreate(db);
    }

    public boolean Insert_db(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Username", username);
        contentValues.put("Password", password);

        db.insert("users_table", null, contentValues);
        db.close();
        return true;
    }

    //checking if the name exist in the DB

    public boolean nameCheck(String name){
        String selectQuery = "SELECT * FROM " + TABLE_NAME+" WHERE Username= '"+name+"' ;";
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);

        if(cursor.getCount() <= 0){


            cursor.close();
            db.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;




    }


    // checking if password is correct
    public boolean AuthCheck(String username, String password) {


        SQLiteDatabase db = getReadableDatabase();
        Cursor mCursor;
        try {
            mCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Username= '" + username.trim() + "' AND Password= '" + password.trim() + "';", null);
        } catch (Exception e) {
            db.close();
            return false;
        }
        mCursor.moveToFirst();


        if (mCursor != null) {
            if (mCursor.getCount() > 0) {
                mCursor.close();
                db.close();
                return true;
            } else
                mCursor.close();
            db.close();
            return false;
        } else {
            mCursor.close();
            db.close();
            return false;

        }


    }


    //data registration
    public boolean Data_Register(String usernme, String data) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Data", data);

        db.update(TABLE_NAME, contentValues,"Username='"+usernme+"';",null);
        db.close();



        return true;

    }

    //get data from DB

    public String getData(String username){


        String selectQuery = "SELECT Data FROM " + TABLE_NAME+" WHERE Username= '"+username+"';";
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        String data=null;

        if( cursor != null && cursor.moveToFirst() ){
           data = cursor.getString( cursor.getColumnIndex("Data") );

        }


        cursor.close();
        db.close();
        return data;

    }
}