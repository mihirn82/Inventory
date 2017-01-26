package com.gmail.mihirn82.android.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.gmail.mihirn82.android.inventory.data.InventoryContract.InventoryEntry;

/**
 * Created by mihirnewalkar on 1/2/17.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "store.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String BLOB_TYPE = " BLOB";
    private static final String TEXT_NOT_NULL = " NOT NULL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + InventoryEntry.TABLE_NAME + " (" +
                    InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    InventoryEntry.COLUMN_PRODUCT_NAME + TEXT_TYPE + TEXT_NOT_NULL + COMMA_SEP +
                    InventoryEntry.COLUMN_IMAGE + BLOB_TYPE + COMMA_SEP +
                    InventoryEntry.COLUMN_PRICE + " INTEGER NOT NULL DEFAULT 0" + COMMA_SEP +
                    InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0" + COMMA_SEP +
                    InventoryEntry.COLUMN_SUPPLIER + TEXT_TYPE + TEXT_NOT_NULL + " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + InventoryEntry.TABLE_NAME;

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
