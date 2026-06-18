package com.example.dals;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.models.Category;

import java.util.ArrayList;

public class CategoryDAO {
    public static final String DATABASE_NAME = "K234112E.sqlite";
    public static final String TABLE_NAME = "Category";
    public static SQLiteDatabase database = null;
    public static ArrayList<Category> getCategories(Context context)
    {
        ArrayList<Category> categories = new ArrayList<>();
        database = context.openOrCreateDatabase(DATABASE_NAME,
                context.MODE_PRIVATE, null);

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME,
                null);
        while(cursor.moveToNext()) {
            String cateId = cursor.getString(0);
            String cateName = cursor.getString(1);
            String cateDesc = cursor.getString(2);
            Category c = new Category(cateId, cateName, cateDesc);
            categories.add(c);
        }
        cursor.close();
        return categories;
    }
    public static long saveCategory(Context context, Category category)
    {
        database = context.openOrCreateDatabase(DATABASE_NAME,
                context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("CateId", category.getCateId());
        values.put("CateName", category.getCateName());
        values.put("CateDesc", category.getCateDesc());
        long result = database.insert(TABLE_NAME, null, values);
        return result;
    }
    public static long removeCategory(Context context, Category category)
    {
        database = context.openOrCreateDatabase(DATABASE_NAME,
                context.MODE_PRIVATE, null);
        long result=database.delete(TABLE_NAME,
                "CateId=?",
                new String[]{category.getCateId()});
        return result;
    }
}
