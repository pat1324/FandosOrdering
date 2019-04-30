package com.example.patrick.fandosordering.Order;

/**
 * This class is intended to be used for storing of favorites in the persistent database
 */
public class Order {

    private long _id;
    private String order;

    public static final String TABLE_NAME = "Favorites";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ORDER = "orderlist";

    // Table create statement
    public static final String CREATE_STATEMENT = "CREATE TABLE "
            + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COLUMN_ORDER + " TEXT NOT NULL " +
            ")";

    //constructor
    public Order(long id, String order){
        this._id = id;
        this.order = order;
    }

    public long getId() { return _id; }
    public void setId(long _id) { this._id = _id; }

    //returns the order to be used for quick ordering
    //currently broken so this version of the app doesn't have the functionality
    public String getOrder(){
        return this.order;
    }
}
