package com.example.rowingappfyp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseObject extends SQLiteOpenHelper {

   // ref [1] showed how to make this whole class. i just had to alter what he showed in the video to suit me.

    public static final String Column_ID = "ID";
    public static final String USERSTATS_TABLE = "USERSTATS_TABLE";
    public static final String SESSION_ID = "SessionID";
    public static final String DISTANCE = "DISTANCE";
    public static final String SPEED = "SPEED";
    public static final String HOURS = "HOURS";
    public static final String MINUTES = "MINUTES";
    public static final String SECONDS = "SECONDS";

    public DatabaseObject(@Nullable Context context) {
        super(context,"UserStats.db" , null , 1);
    }



    //this is run first time db is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {

        //String Createtablestatement = "CREATE TABLE " + USERSTATS_TABLE + "(" + Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + Column_DISTANCE + " FLOAT)";

        String Createtablestatement = "CREATE TABLE " + USERSTATS_TABLE + " ( " + SESSION_ID + " INTEGER , " + DISTANCE + " FLOAT , " + SPEED + " STRING , " + HOURS + " INTEGER , " + MINUTES + " INTEGER , " + SECONDS + " INTEGER)";


        db.execSQL(Createtablestatement);

    }


    ///this is run if we make structural changes to db
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int sessionID (){

        int SessionID = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT MAX(SessionID) FROM " + USERSTATS_TABLE;


        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor != null && cursor.moveToFirst()){
            SessionID = cursor.getInt(0);
        }
        db.close();
       return  SessionID;
    }

    public boolean addOne (UserStatsDBModel userStatsDBModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();


        //these are the columns
        cv.put(SESSION_ID,userStatsDBModel.getSessionID());
        cv.put(DISTANCE,userStatsDBModel.getDistance() );
        cv.put(SPEED,userStatsDBModel.getSpeed() );
        cv.put(HOURS, userStatsDBModel.getHours());
        cv.put(MINUTES, userStatsDBModel.getMinutes());
        cv.put(SECONDS, userStatsDBModel.getSeconds());


        db.insert(USERSTATS_TABLE,null , cv);
        db.close();
        return true;

    }
}
