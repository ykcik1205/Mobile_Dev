package com.example.dals;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.models.Product;

import java.util.ArrayList;

public class ProductDAO {
    public static final String DATABASE_NAME = "K234112E.sqlite";
    public static final String TABLE_NAME = "Product";
    public static SQLiteDatabase database = null;
    public static ArrayList<Product> getProducts(Context context){
        ArrayList<Product> products = new ArrayList<>();
        database = context.openOrCreateDatabase(DATABASE_NAME,
                Context.MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME,
                null);
        while(cursor.moveToNext()) {
            String productId = cursor.getString(0);
            String productName = cursor.getString(1);
            int quantity = cursor.getInt(2);
            double price = cursor.getDouble(3);
            double coupon = cursor.getDouble(4);
            double VAT = cursor.getDouble(5);
            String cateId = cursor.getString(6);

            Product p = new Product(productId, productName, quantity, price, coupon, VAT, cateId);
            products.add(p);
        }
        cursor.close();
        return products;
    }
}
