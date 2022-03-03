package com.example.ranwildimal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DB_NAME = "RanWildAnimal.db";
    private static final int DB_VERSION = 1;

    /**
     * Constructor
     */

    public DatabaseOpenHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
}
