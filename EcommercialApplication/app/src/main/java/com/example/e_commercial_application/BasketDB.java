package com.example.e_commercial_application;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.e_commercial_application.Model.AllProducts;

import java.util.ArrayList;
import java.util.List;

public class BasketDB extends SQLiteOpenHelper {

    private ArrayList<AllProducts> allProducts;
    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "BaronShopDB2";
    private static String TABLE_NAME = "basketItems";
    public static  String KEY_ID = "id";
    public static  String ITEM_TITLE = "basketTitle";
    public static String  ITEM_IMAGE = "basketImage";
    public static String ITEM_PRICE = "basketPrice";
    public static String ITEM_PIECE = "basketPiece";

    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " TEXT," +
            ITEM_TITLE + " TEXT,"
            +ITEM_IMAGE + " TEXT," + ITEM_PIECE + " TEXT," + ITEM_PRICE + " TEXT)";

    public BasketDB(Context context){super(context,DATABASE_NAME,null,DB_VERSION);}





    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        allProducts = new ArrayList<>();

        for (int x = 1; x<allProducts.size(); x++){
            cv.put(KEY_ID, x);
            db.insert(TABLE_NAME,null,cv);
        }
    }

    public void insertIntoTheDatabase(String item_title, String item_image, String id, String item_price, String item_piece){
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_TITLE, item_title);
        cv.put(ITEM_IMAGE, item_image);
        cv.put(KEY_ID, id);
        cv.put(ITEM_PRICE, item_price);
        cv.put(ITEM_PIECE, item_piece);

        db.insert(TABLE_NAME,null,cv);
        Log.d("BasketDb status",item_title+ cv);
    }

    public Cursor read_all_data(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + TABLE_NAME + " where " + KEY_ID+"="+id+"";
        return db.rawQuery(sql,null,null);
    }
    @SuppressLint("Range")
    public List<AllProducts> getAllBasketProducts() {
        List<AllProducts> allProductsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_ID + " ASC", null);

        if (cursor.moveToFirst()) {
            do {
                AllProducts allProducts = new AllProducts();
                allProducts.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                allProducts.setProductName(cursor.getString(cursor.getColumnIndex(ITEM_TITLE)));
                allProducts.setProductImg(cursor.getString(cursor.getColumnIndex(ITEM_IMAGE)));
                allProducts.setProductPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_PRICE))));
                allProducts.setNumber(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_PIECE))));
                allProductsList.add(allProducts);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return allProductsList;
    }

    public void updateQuantity(String productId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_PIECE, newQuantity);
        db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{productId});
        db.close();
    }






}
