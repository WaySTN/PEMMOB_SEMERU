package com.example.logincrud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager2 extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DonasiDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "donasi";

    private static final String COL_ID = "id";
    private static final String COL_NAMA = "nama";
    private static final String COL_EMAIL = "email";
    private static final String COL_NOMINAL = "nominal";
    private static final String COL_METODE = "metode";
    private static final String COL_PESAN = "pesan";

    public DatabaseManager2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAMA + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_NOMINAL + " TEXT, " +
                COL_METODE + " TEXT, " +
                COL_PESAN + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(String nama, String email, String nominal, String metode, String pesan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAMA, nama);
        values.put(COL_EMAIL, email);
        values.put(COL_NOMINAL, nominal);
        values.put(COL_METODE, metode);
        values.put(COL_PESAN, pesan);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public void updateData(int id, String nama, String email, String nominal, String metode, String pesan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAMA, nama);
        values.put(COL_EMAIL, email);
        values.put(COL_NOMINAL, nominal);
        values.put(COL_METODE, metode);
        values.put(COL_PESAN, pesan);
        db.update(TABLE_NAME, values, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
