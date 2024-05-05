package com.example.reusethings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String qry1 = "create table users(username text, email text, password text)";
        db.execSQL(qry1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void register(String username, String email, String password) {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("email", email);
        cv.put("password", password);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("users", null, cv);
        db.close();
    }

    public int login(String username, String password) {
        int result = 0;
        String[] param = new String[2];
        param[0] = username;
        param[1] = password;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from users where username=? and password=?", param);
        if (c.moveToFirst()) {
            result = 1;
        }
        return result;
    }

    public boolean usernameExists(String username) {
        SQLiteDatabase db = getReadableDatabase(); // Open a readable database connection

        Cursor cursor = db.query(
                "users",  // Table name
                new String[]{"username"}, // Columns to return
                "username = ?", // WHERE clause to filter by username
                new String[]{username}, // Selection arguments
                null, null, null // No grouping, having, or sorting
        );

        boolean exists = cursor.getCount() > 0; // Check if any records were returned
        cursor.close(); // Close the cursor to free up resources
        db.close(); // Close the database connection

        return exists; // Return whether the username exists
    }
}
