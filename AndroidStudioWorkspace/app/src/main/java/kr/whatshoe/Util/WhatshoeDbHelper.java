package kr.whatshoe.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jaewoo on 2015-10-17.
 */
public class WhatshoeDbHelper {

    public static final String KEY_DESC = "desc";
    public static final String KEY_DATE = "date";
    public static final String KEY_PRICE = "price";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_STATUS = "status";
    private static final String TAG = "NotesDbAdapter";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */

    private static final String DATABASE_CREATE = "create table coupon (_id integer primary key autoincrement, "
            + "desc text not null, date text not null, price text not null, status text not null);";
    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "coupon";
    private static final int DATABASE_VERSION = 2;
    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    public WhatshoeDbHelper(Context ctx) {
        this.mCtx = ctx;
    }

    public WhatshoeDbHelper open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createCoupon(String desc, String date, String price, String status) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DESC, desc);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_PRICE,price);
        initialValues.put(KEY_STATUS,status);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteCoupon(long rowId) {
        Log.i("Delete called", "value__" + rowId);
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllCoupons() {
        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_DESC, KEY_DATE, KEY_PRICE, KEY_STATUS}, null, null, null, null, null);
    }

    public Cursor fetchCoupon(long rowId) throws SQLException {

        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[]{KEY_ROWID,  KEY_DESC, KEY_DATE, KEY_PRICE, KEY_STATUS}, KEY_ROWID
                + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateNote(long rowId, String desc, String date, String price, String status) {
        ContentValues args = new ContentValues();
        args.put(KEY_DESC, desc);
        args.put(KEY_DATE, date);
        args.put(KEY_PRICE, price);
        args.put(KEY_STATUS, status);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}


