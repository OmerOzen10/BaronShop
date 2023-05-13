package com.example.e_commercial_application.Databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.e_commercial_application.Adapter.BasketAdapter;
import com.example.e_commercial_application.Model.AllProducts;
import com.example.e_commercial_application.Model.DiscountedProducts;

import java.util.ArrayList;
import java.util.List;

public class BasketDBDiscounted  extends SQLiteOpenHelper {

    private ArrayList<DiscountedProducts> discountedProducts;
    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "BaronShopDB4";
    private static String TABLE_NAME = "basketItems2";
    public static  String KEY_ID = "id";
    public static  String ITEM_TITLE = "basketTitle";
    public static String  ITEM_IMAGE = "basketImage";
    public static String ITEM_PRICE = "basketPrice";
    public static String ITEM_OLD = "oldPrice";
    public static String ITEM_PIECE = "basketPiece";
    public static String FAVORITE_STATUS = "fStatus";

    public static String ITEM_RATE = "itemRate";

    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " TEXT," +
            ITEM_TITLE + " TEXT,"
            +ITEM_IMAGE + " TEXT," + ITEM_PIECE + " TEXT," + ITEM_OLD + " TEXT," + ITEM_RATE + " TEXT," + FAVORITE_STATUS + " TEXT," +  ITEM_PRICE + " TEXT)";

    public BasketDBDiscounted(Context context){super(context,DATABASE_NAME,null,DB_VERSION);}

    @SuppressLint("Range")
    public List<DiscountedProducts> getAllFavProducts() {
        List<DiscountedProducts> allProductsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + FAVORITE_STATUS + " = 1", null);

        if (cursor.moveToFirst()) {
            do {
                DiscountedProducts discountedProducts1 = new DiscountedProducts();
                discountedProducts1.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_ID))));
                discountedProducts1.setProductName(cursor.getString(cursor.getColumnIndex(ITEM_TITLE)));
                discountedProducts1.setProductImg(cursor.getString(cursor.getColumnIndex(ITEM_IMAGE)));
                discountedProducts1.setProductPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_PRICE))));
                discountedProducts1.setOldPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_OLD))));
                discountedProducts1.setNumber(Integer.parseInt(String.valueOf(cursor.getString(cursor.getColumnIndex(ITEM_PIECE)))));
                discountedProducts1.setFavStatus(cursor.getString(cursor.getColumnIndex(FAVORITE_STATUS)));
                discountedProducts1.setProductRate(Float.parseFloat(cursor.getString(cursor.getColumnIndex(ITEM_RATE))));
                allProductsList.add(discountedProducts1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return allProductsList;
    }

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
        discountedProducts = new ArrayList<>();

        for (int x = 1; x<discountedProducts.size(); x++){
            cv.put(KEY_ID, x);
            cv.put(FAVORITE_STATUS,"0");
            db.insert(TABLE_NAME,null,cv);
        }
    }

    public void insertIntoTheDatabase(String item_title, String item_image, String id, String item_price, String item_old, String item_piece,String item_rate,String fav_status){
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_TITLE, item_title);
        cv.put(ITEM_IMAGE, item_image);
        cv.put(KEY_ID, id);
        cv.put(ITEM_PRICE, item_price);
        cv.put(ITEM_OLD,item_old);
        cv.put(ITEM_PIECE, item_piece);
        cv.put(ITEM_RATE, item_rate);
        cv.put(FAVORITE_STATUS,fav_status);

        db.insert(TABLE_NAME,null,cv);
        Log.d("BasketDb status",item_title+ cv);
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

    @SuppressLint("Range")
    public List<DiscountedProducts> getAllBasketProducts() {
        List<DiscountedProducts> discountedProductsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_ID + " ASC", null);

        if (cursor.moveToFirst()) {
            do {
                DiscountedProducts discountedProducts1 = new DiscountedProducts();
                discountedProducts1.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                discountedProducts1.setProductName(cursor.getString(cursor.getColumnIndex(ITEM_TITLE)));
                discountedProducts1.setProductImg(cursor.getString(cursor.getColumnIndex(ITEM_IMAGE)));
                discountedProducts1.setProductPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_PRICE))));
                discountedProducts1.setOldPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_OLD))));
                discountedProducts1.setNumber(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_PIECE))));
                discountedProducts1.setProductRate(Float.parseFloat(cursor.getString(cursor.getColumnIndex(ITEM_RATE))));
                discountedProductsList.add(discountedProducts1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return discountedProductsList;
    }

    public void updateQuantity(String productId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_PIECE, newQuantity);
        db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{productId});
        db.close();
    }

    public void deleteAllBasketItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();

        BasketAdapter adapter = new BasketAdapter();
        adapter.notifyDataSetChanged();
    }

}
