package com.example.VoyaGo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DBNAME = "Login.db";

    public DBHelper(@Nullable Context context) {
        super(context, DBNAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE users(username TEXT, email TEXT PRIMARY KEY, password TEXT)");
        MyDB.execSQL("CREATE TABLE items(id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, item TEXT)");
        MyDB.execSQL("CREATE TABLE destinations_visited(id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, item_id INTEGER, destination TEXT)");
        MyDB.execSQL("CREATE TABLE planned_destinations(id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, item TEXT, destination TEXT, checked INTEGER)");
        MyDB.execSQL("CREATE TABLE item_images(id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, item TEXT, image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("DROP TABLE IF EXISTS users");
        MyDB.execSQL("DROP TABLE IF EXISTS items");
        MyDB.execSQL("DROP TABLE IF EXISTS destinations_visited");
        MyDB.execSQL("DROP TABLE IF EXISTS planned_destinations");
        MyDB.execSQL("DROP TABLE IF EXISTS item_images");
        onCreate(MyDB);
    }

    public Boolean insertData(String username, String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        return result != -1;
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        return cursor.getCount() > 0;
    }

    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        return cursor.getCount() > 0;
    }

    public Boolean insertItem(String email, String item) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("item", item);
        long result = MyDB.insert("items", null, contentValues);
        return result != -1;
    }

    public ArrayList<String> getItems(String email) {
        ArrayList<String> itemList = new ArrayList<>();
        SQLiteDatabase MyDB = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = MyDB.rawQuery("SELECT item FROM items WHERE email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String item = cursor.getString(cursor.getColumnIndex("item"));
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemList;
    }

    public boolean moveItem(String email, String item, String destination, boolean toVisited) {
        if (email == null || item == null || destination == null) {
            return false;
        }

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT id FROM items WHERE email = ? AND item = ?", new String[]{email, item});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int itemId = cursor.getInt(cursor.getColumnIndex("id"));
            ContentValues contentValues = new ContentValues();
            contentValues.put("item_id", itemId);
            contentValues.put("destination", destination);
            if (toVisited) {
                long result = MyDB.insert("destinations_visited", null, contentValues);
                if (result != -1) {
                    MyDB.delete("planned_destinations", "email = ? AND item = ? AND destination = ?", new String[]{email, item, destination});
                    return true;
                }
            } else {
                long result = MyDB.insert("planned_destinations", null, contentValues);
                if (result != -1) {
                    MyDB.delete("destinations_visited", "email = ? AND item = ? AND destination = ?", new String[]{email, item, destination});
                    return true;
                }
            }
        }
        cursor.close();
        return false;
    }

    public Boolean insertPlannedDestination(String email, String item, String destination, boolean checked) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("item", item);
        contentValues.put("destination", destination);
        contentValues.put("checked", checked ? 1 : 0); // Convert boolean to integer for SQLite
        long result = MyDB.insert("planned_destinations", null, contentValues);
        return result != -1;
    }

    public ArrayList<String> getPlannedDestinations(String email, String item) {
        ArrayList<String> destinationList = new ArrayList<>();
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT destination FROM planned_destinations WHERE email = ? AND item = ?", new String[]{email, item});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String destination = cursor.getString(cursor.getColumnIndex("destination"));
                destinationList.add(destination);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return destinationList;
    }

    @SuppressLint("Range")
    public boolean isDestinationChecked(String email, String item, String destination) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT checked FROM planned_destinations WHERE email = ? AND item = ? AND destination = ?", new String[]{email, item, destination});
        boolean isChecked = false;
        if (cursor.moveToFirst()) {
            isChecked = cursor.getInt(cursor.getColumnIndex("checked")) == 1;
        }
        cursor.close();
        return isChecked;
    }

    public ArrayList<String> getVisitedDestinations(String email, String item) {
        ArrayList<String> destinationList = new ArrayList<>();
        SQLiteDatabase MyDB = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = MyDB.rawQuery("SELECT destination FROM destinations_visited WHERE email = ? AND item_id IN (SELECT id FROM items WHERE email = ? AND item = ?)", new String[]{email, email, item});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String destination = cursor.getString(cursor.getColumnIndex("destination"));
                destinationList.add(destination);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return destinationList;
    }

    public boolean insertItemImage(String email, String item, byte[] image) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("item", item);
        contentValues.put("image", image);
        long result = MyDB.insert("item_images", null, contentValues);
        return result != -1;
    }

    // Retrieve image associated with an item
    public byte[] getItemImage(String email, String item) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT image FROM item_images WHERE email = ? AND item = ?", new String[]{email, item});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
            cursor.close();
            return imageBytes;
        }
        cursor.close();
        return null;
    }
}