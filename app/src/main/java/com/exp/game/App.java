package com.exp.game;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.exp.game.util.SPUtils;

public class App extends Application {

    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
        SPUtils.init(this);
    }

    /**
     * database settings
     */
    private void setupDatabase() {
        //Create database shop.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "shop.db", null);
        //get writeable database
        SQLiteDatabase db = helper.getWritableDatabase();
        //get database object
        DaoMaster daoMaster = new DaoMaster(db);
        //get DAO
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }

}
