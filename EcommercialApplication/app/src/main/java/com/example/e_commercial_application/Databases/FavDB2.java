package com.example.e_commercial_application.Databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.e_commercial_application.Model.DiscountedProducts;

import java.util.ArrayList;
import java.util.List;

public class FavDB2 extends SQLiteOpenHelper {

    private ArrayList<DiscountedProducts> discountedProducts;
    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "BaronShopDB3";
    private static String TABLE_NAME = "favItems2";
    public static String KEY_ID = "id";
    public static String ITEM_TITLE = "itemTitle";
    public static String ITEM_IMAGE = "itemImage";
    public static String ITEM_PRICE = "itemPrice";
    public static String OLD_PRICE = "oldPrice";
    public static String FAVORITE_STATUS = "fStatus";


    public static String ITEM_RATE = "itemRate";
    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " TEXT," +
            ITEM_TITLE + " TEXT,"
            +ITEM_IMAGE + " TEXT," + FAVORITE_STATUS + " TEXT," + ITEM_RATE + " TEXT,"+ OLD_PRICE + " TEXT," + ITEM_PRICE + " TEXT)";

    public FavDB2(Context context){super(context,DATABASE_NAME,null,DB_VERSION);}
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @SuppressLint("Range")
    public List<DiscountedProducts> getAllFavProducts() {
        List<DiscountedProducts> discountedProductsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + FAVORITE_STATUS + " = 1", null);

        if (cursor.moveToFirst()) {
            do {
               DiscountedProducts discountedProducts1 = new DiscountedProducts();
                discountedProducts1.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_ID))));
                discountedProducts1.setProductName(cursor.getString(cursor.getColumnIndex(ITEM_TITLE)));
                discountedProducts1.setProductImg(cursor.getString(cursor.getColumnIndex(ITEM_IMAGE)));
                discountedProducts1.setProductPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_PRICE))));
                discountedProducts1.setOldPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(OLD_PRICE))));
                discountedProducts1.setFavStatus(cursor.getString(cursor.getColumnIndex(FAVORITE_STATUS)));
                discountedProducts1.setProductRate(Float.parseFloat(cursor.getString(cursor.getColumnIndex(ITEM_RATE))));
                discountedProductsList.add(discountedProducts1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return discountedProductsList;
    }

    public void insertEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        discountedProducts = new ArrayList<>();

        for (int x = 1; x<discountedProducts.size(); x++){
            cv.put(KEY_ID, x);
            cv.put(FAVORITE_STATUS, "0");

            db.insert(TABLE_NAME,null,cv);
        }
    }

    public void insertIntoTheDatabase(String item_title, String item_image, String id, String fav_status, String item_rate, String item_price, String item_old){
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_TITLE, item_title);
        cv.put(ITEM_IMAGE, item_image);
        cv.put(KEY_ID, id);
        cv.put(FAVORITE_STATUS, fav_status);
        cv.put(ITEM_PRICE, item_price);
        cv.put(OLD_PRICE, item_old);
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
        Log.d("remove",id.toString());
    }

    public Cursor select_all_favorite_list(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+FAVORITE_STATUS+" ='1'";
        return db.rawQuery(sql,null,null);
    }
}
