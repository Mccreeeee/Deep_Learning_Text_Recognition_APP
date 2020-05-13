package com.example.dell.myui.SQLite;

import android.app.Application;

public class MYDB extends Application {
    //本地数据库的全局变量

    private localDB my_db;

    @Override
    public void onCreate(){
        super.onCreate();
        my_db = new localDB(this);
        my_db.openDataBase();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        my_db.closeDataBase();
    }

    public localDB getDB(){
        return my_db;
    }
}
