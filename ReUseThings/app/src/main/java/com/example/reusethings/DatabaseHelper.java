package com.example.reusethings;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reusethings.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ITEMS = "items";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_IMAGE_URI = "image_uri";
    private static final String COLUMN_USER_ID = "user_id";  // New column to store the user's ID
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_ADDRESS = "address";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_ITEMS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_IMAGE_URI + " BLOB, " +
                COLUMN_USER_ID + " TEXT, " +
                COLUMN_PHONE_NUMBER + " TEXT, " +
                COLUMN_ADDRESS + " TEXT " +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    public boolean addItem(String name, Bitmap image,String phoneNumber, String address, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Convert the Bitmap to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_IMAGE_URI, imageBytes);
        contentValues.put(COLUMN_PHONE_NUMBER, phoneNumber);
        contentValues.put(COLUMN_ADDRESS, address);
        contentValues.put(COLUMN_USER_ID, userId);

        long result = db.insert(TABLE_ITEMS, null, contentValues);

        db.close();

        return result != -1; // Returns true if insertion was successful
    }
    public List<Item> getItemsByUserId(String userId) {
        List<Item> items = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + COLUMN_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] {userId});

        if (cursor.moveToFirst()) {
            do {
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int phoneNumberIndex = cursor.getColumnIndex(COLUMN_PHONE_NUMBER);
                int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
                int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);
                int imageUriIndex = cursor.getColumnIndex(COLUMN_IMAGE_URI);

                // Check if all columns are valid
                if (nameIndex != -1 && phoneNumberIndex != -1 && addressIndex != -1 && userIdIndex != -1 && imageUriIndex != -1) {
                    String name = cursor.getString(nameIndex);
                    String phoneNumber = cursor.getString(phoneNumberIndex);
                    String address = cursor.getString(addressIndex);
                    String username = cursor.getString(userIdIndex);
                    byte[] imageBytes = cursor.getBlob(imageUriIndex);

                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    items.add(new Item(name, phoneNumber, address, bitmap, username));
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return items;
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME); // This deletes the entire database
    }

    public void deleteItemByDetails(String itemName, String phone, String address) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Define the selection and selectionArgs for the deletion
        String selection = COLUMN_NAME + " = ? AND " + COLUMN_PHONE_NUMBER + " = ? AND " + COLUMN_ADDRESS + " = ?";
        String[] selectionArgs = new String[]{itemName, phone, address};

        // Delete the item from the database
        db.delete(TABLE_ITEMS, selection, selectionArgs);
        db.close();
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ITEMS;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int phoneNumberIndex = cursor.getColumnIndex(COLUMN_PHONE_NUMBER);
                int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
                int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);
                int imageUriIndex = cursor.getColumnIndex(COLUMN_IMAGE_URI);

                // Check if all columns are valid
                if (nameIndex != -1 && phoneNumberIndex != -1 && addressIndex != -1 && userIdIndex != -1 && imageUriIndex != -1) {
                    String name = cursor.getString(nameIndex);
                    String phoneNumber = cursor.getString(phoneNumberIndex);
                    String address = cursor.getString(addressIndex);
                    String username = cursor.getString(userIdIndex);
                    byte[] imageBytes = cursor.getBlob(imageUriIndex);

                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    items.add(new Item(name, phoneNumber, address, bitmap, username));
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return items;
    }

}

