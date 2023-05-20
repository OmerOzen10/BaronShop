package com.example.e_commercial_application.Databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.e_commercial_application.Model.AllProducts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavDB extends SQLiteOpenHelper {

    private ArrayList<AllProducts> allProducts;
    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "BaronShopDB";
    private static String TABLE_NAME = "favItems";
    public static String KEY_ID = "id";
    public static String ITEM_TITLE = "itemTitle";
    public static String ITEM_IMAGE = "itemImage";
    public static String ITEM_PRICE = "itemPrice";
    public static String FAVORITE_STATUS = "fStatus";

    public static String ITEM_RATE = "itemRate";
    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " TEXT," +
            ITEM_TITLE + " TEXT,"
            +ITEM_IMAGE + " TEXT," + FAVORITE_STATUS + " TEXT," + ITEM_RATE + " TEXT,"+ ITEM_PRICE + " TEXT)";

    public FavDB(Context context){super(context,DATABASE_NAME,null,DB_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    @SuppressLint("Range")
    public List<AllProducts> getAllFavProducts() {
        List<AllProducts> allProductsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + FAVORITE_STATUS + " = 1", null);

        if (cursor.moveToFirst()) {
            do {
                AllProducts allProducts = new AllProducts();
                allProducts.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_ID))));
                allProducts.setProductName(cursor.getString(cursor.getColumnIndex(ITEM_TITLE)));
                allProducts.setProductImg(cursor.getString(cursor.getColumnIndex(ITEM_IMAGE)));
                allProducts.setProductPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_PRICE))));
                allProducts.setFavStatus(cursor.getString(cursor.getColumnIndex(FAVORITE_STATUS)));
                allProducts.setProductRate(Float.parseFloat(cursor.getString(cursor.getColumnIndex(ITEM_RATE))));
                allProductsList.add(allProducts);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return allProductsList;
    }



    public void insertEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        allProducts = new ArrayList<>();

        for (int x = 1; x<allProducts.size(); x++){
            cv.put(KEY_ID, x);
            cv.put(FAVORITE_STATUS, "0");

            db.insert(TABLE_NAME,null,cv);
        }
    }

    public void insertIntoTheDatabase(String item_title, String item_image, String id, String fav_status, String item_rate, String item_price){
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_TITLE, item_title);
        cv.put(ITEM_IMAGE, item_image);
        cv.put(KEY_ID, id);
        cv.put(FAVORITE_STATUS, fav_status);
        cv.put(ITEM_PRICE, item_price);
        cv.put(ITEM_RATE, item_rate);

        db.insert(TABLE_NAME,null,cv);
        Log.d("FavDB Status",item_title + ", favstatus - "+fav_status+" - . " + cv);
    }

    public Cursor read_all_data(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + TABLE_NAME + " where " + KEY_ID+"="+id+"";
        return db.rawQuery(sql,null,null);
    }

    public void remove_fav(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME + " SET "+ FAVORITE_STATUS+" ='0' WHERE "+KEY_ID+"="+id+"";
        db.execSQL(sql);
    }

    public Cursor select_all_favorite_list(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+FAVORITE_STATUS+" ='1'";
        return db.rawQuery(sql,null,null);
    }

}
