package com.example.logincrud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "donasi_pendidikan.db";
    private static final int DATABASE_VERSION = 2; // Update versi untuk trigger onUpgrade

    // Table Users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD = "password";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT NOT NULL, " +
                COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COLUMN_PHONE + " TEXT NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL)";

        db.execSQL(createUsersTable);
        Log.d("DATABASE", "Tabel users berhasil dibuat.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DATABASE", "Upgrade database dari versi " + oldVersion + " ke " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // **Metode untuk Menambahkan User**
    public boolean insertUser(String username, String email, String phone, String password) {
        SQLiteDatabase db = null;
        long result = -1;

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, username);
            values.put(COLUMN_EMAIL, email);
            values.put(COLUMN_PHONE, phone);
            values.put(COLUMN_PASSWORD, password); // Sebaiknya gunakan enkripsi untuk keamanan.

            result = db.insert(TABLE_USERS, null, values);
            Log.d("DATABASE", "Insert User Result: " + result);
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error inserting user: " + e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return result != -1;
    }

    // **Metode untuk Mengecek Apakah User Sudah Terdaftar**
    public boolean isUserExists(String email) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        boolean exists = false;

        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
            exists = cursor.getCount() > 0;
            Log.d("DATABASE", "Cek user: " + email + " Exists: " + exists);
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error checking if user exists: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        return exists;
    }

    // **Metode untuk Login User**
    public Cursor checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{email, password});
    }
}





