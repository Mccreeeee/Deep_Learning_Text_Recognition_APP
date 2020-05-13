package com.example.dell.myui.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Picture;

import com.example.dell.myui.entity.PictureEntity;

import java.util.ArrayList;
import java.util.List;

public class localDB {
    //保存拍照记录，保存的是图片的url，类型，名字，和识别出的的数据
    private final String DB_NAME = "picture.db";
    private final String TABLE_NAME = "picture_info";
    private final int DB_VERSION = 1;

    //表的字段名

    private final String KEY_NAME = "pictureName";
    private final String KEY_TYPE = "pictureType";
    private final String KEY_URL = "pictureUrl";
    private final String KEY_DATA = "pictureData";


    private SQLiteDatabase my_database;
    private Context my_context;
    private DBOpenHelper my_dbOpenHelper;

    public localDB(Context context){
        my_context = context;
    }

    public void openDataBase(){
        my_dbOpenHelper = new DBOpenHelper(my_context,DB_NAME,null,DB_VERSION);
        try{
            my_database = my_dbOpenHelper.getWritableDatabase();
    }catch (Exception e){
        my_database = my_dbOpenHelper.getReadableDatabase();}
    }

    public void closeDataBase(){
        if(my_database != null)
            my_database.close();
    }
    public long insertData(PictureEntity picture){
        if(queryData(picture.getPictureName()) != null){
            return updateData(picture.getPictureName(),picture);//如果这张图片已经存在，则更新而不是插入
        }
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,picture.getPictureName());
        values.put(KEY_TYPE,picture.getPictureType());
        values.put(KEY_URL,picture.getPictureUrl());
        values.put(KEY_DATA,picture.getPictureData());

        return my_database.insert(TABLE_NAME, null, values);
    }
    public long deleteData(String name){
        return my_database.delete(TABLE_NAME,KEY_NAME+ "=?" ,new String[]{name} );
    }
    public long deleteAllData(){
        return my_database.delete(TABLE_NAME,null,null);
    }
    public long updateData(String name, PictureEntity picture){
        ContentValues values = new ContentValues();
        values.put(KEY_DATA,picture.getPictureData());
        values.put(KEY_URL,picture.getPictureUrl());
        values.put(KEY_TYPE,picture.getPictureType());
        return my_database.update(TABLE_NAME,values,KEY_NAME + "=?" ,new String[]{name});
    }
    public PictureEntity queryData(String name){
        PictureEntity picture = new PictureEntity();
        Cursor result = my_database.query(TABLE_NAME, new String[]{KEY_NAME,KEY_TYPE,KEY_URL,KEY_DATA},
                KEY_NAME + "=?", new String[]{name},null,null,null);
        if(result.getCount() == 0|| !result.moveToFirst()) {
            return null;
        }
        picture.setPictureName(result.getString(result.getColumnIndex(KEY_NAME)));
        picture.setPictureType(result.getInt(result.getColumnIndex(KEY_TYPE)));
        picture.setPictureUrl(result.getString(result.getColumnIndex(KEY_URL)));
        picture.setPictureData(result.getString(result.getColumnIndex(KEY_DATA)));

        return picture;
    }


    public List<PictureEntity> queryData(){
        List<PictureEntity>pictureList = new ArrayList<>();
        Cursor result = my_database.query(TABLE_NAME,new String[]{KEY_NAME,KEY_TYPE,KEY_URL,KEY_DATA},
                null,null,null,null,null);
        int resCount = result.getCount();
        if(resCount == 0 || !result.moveToFirst()) {
            return null;}
        for(int i=0;i<resCount;i++){
            PictureEntity picture = new PictureEntity();
            picture.setPictureName(result.getString(result.getColumnIndex(KEY_NAME)));
            picture.setPictureType(result.getInt(result.getColumnIndex(KEY_TYPE)));
            picture.setPictureUrl(result.getString(result.getColumnIndex(KEY_URL)));
            picture.setPictureData(result.getString(result.getColumnIndex(KEY_DATA)));
            pictureList.add(picture);
            result.moveToNext();
        }
        return pictureList;
    }

    public class DBOpenHelper extends SQLiteOpenHelper {
        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase db){
            final String sqlStr = "create table if not exists " + TABLE_NAME + " (" +
                    KEY_NAME + " text primary key, " +
                    KEY_TYPE+ " integer, " +
                    KEY_URL + " text, " +
                    KEY_DATA + " text );";
            db.execSQL(sqlStr);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            final String sqlStr = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(sqlStr);
            onCreate(db);
        }
    }
}

