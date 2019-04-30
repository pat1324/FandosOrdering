package com.example.patrick.fandosordering;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.patrick.fandosordering.Order.Order;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * The database helper class that holds the logic of the favorites system
 *
 */
public class FavoritesDatabaseHelper extends SQLiteOpenHelper {

    // Set Database Properties
    public static final String DATABASE_NAME = "FavoritesDB";
    public static final int DATABASE_VERSION = 1;


    public FavoritesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Order.CREATE_STATEMENT);
    }

    //Upgrade table
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Order.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //Add order to database
    public void addOrder(Order order){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Order.COLUMN_ORDER, order.getOrder());
        db.insert(Order.TABLE_NAME, null, values);
        db.close();
    }

    //remove order from database
    public void removeOrder(Order order){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Order.TABLE_NAME,
                Order.COLUMN_ID + " = ?",
                new String[] {String.valueOf(order.getId())});
    }

    //Returns all the orders as a hashmap
    public HashMap<Long, Order> getAllFavorites() {
        HashMap<Long, Order> orders = new LinkedHashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Order.TABLE_NAME, null);
        // Add each order to hash map (Each row has 1 order)
        while (cursor.moveToNext()) {
            Order order = new Order(cursor.getLong(0),
                    cursor.getString(1));
            orders.put(order.getId(), order);
        }
        //closing cursor and database
        cursor.close();
        db.close();
        return orders;
    }
}
