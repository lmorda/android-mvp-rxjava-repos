package com.example.lmorda.rxrepos.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.lmorda.rxrepos.data.source.local.ReposPersistenceContract.RepoEntry;

public class ReposDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Repos.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RepoEntry.TABLE_NAME + " (" +
                    RepoEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + " PRIMARY KEY," +
                    RepoEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    RepoEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    RepoEntry.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    RepoEntry.COLUMN_NAME_LANGUAGE + TEXT_TYPE +
                    " )";

    public ReposDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1

    }
}
