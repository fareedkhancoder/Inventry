package com.example.inventry.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.inventry.Classes.TempProduct;

public class TempProductDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 2;
    // Singleton instance of TempProductDatabaseHelper
    private static TempProductDatabaseHelper instance;

    // Table name and columns
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_RATE = "rate";
    private static final String COLUMN_TAX_PERCENTAGE = "tax_percentage";
    private static final String COLUMN_TAX_AMOUNT = "tax_amount";
    private static final String COLUMN_DISCOUNT_PERCENTAGE = "discount_percentage";
    private static final String COLUMN_DISCOUNT_AMOUNT = "discount_amount";
    private static final String COLUMN_SUBTOTAL = "subtotal";
    private static final String COLUMN_TOTAL_AMOUNT = "total_amount";
    private static final String COLUMN_CATEGORY = "category";

    public TempProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_QUANTITY + " REAL, " +
                COLUMN_RATE + " REAL, " +
                COLUMN_TAX_PERCENTAGE + " REAL, " +
                COLUMN_TAX_AMOUNT + " REAL, " +
                COLUMN_DISCOUNT_PERCENTAGE + " REAL, " +
                COLUMN_DISCOUNT_AMOUNT + " REAL, " +
                COLUMN_SUBTOTAL + " REAL, " +
                COLUMN_CATEGORY + " TEXT," +
                COLUMN_TOTAL_AMOUNT + " REAL)";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    // Insert tempProduct data
    public void addProduct(TempProduct tempProduct) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, tempProduct.getName());
        values.put(COLUMN_QUANTITY, tempProduct.getQuantity());
        values.put(COLUMN_RATE, tempProduct.getRate());
        values.put(COLUMN_TAX_PERCENTAGE, tempProduct.getTaxPercentage());
        values.put(COLUMN_TAX_AMOUNT, tempProduct.getTaxAmount());
        values.put(COLUMN_DISCOUNT_PERCENTAGE, tempProduct.getDiscountPercentage());
        values.put(COLUMN_DISCOUNT_AMOUNT, tempProduct.getDiscountAmount());
        values.put(COLUMN_SUBTOTAL, tempProduct.getSubtotal());
        values.put(COLUMN_TOTAL_AMOUNT, tempProduct.getTotalAmount());
        values.put(COLUMN_CATEGORY, tempProduct.getCategory());

        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    // Singleton pattern to get the same instance of TempProductDatabaseHelper
    public static synchronized TempProductDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new TempProductDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Retrieve all products
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }

    // Clear the products table
    public void clearProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PRODUCTS);
        db.close();
    }

    public double getTotalDiscount() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalDiscount = 0.0;

        Cursor cursor = db.rawQuery("SELECT SUM(discount_amount) AS total_discount FROM products", null);
        if (cursor != null && cursor.moveToFirst()) {
            totalDiscount = cursor.getDouble(cursor.getColumnIndexOrThrow("total_discount"));
            cursor.close();
        }
        return totalDiscount;
    }
    public double getTotalTax() {
        double totalTax = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to calculate the sum of tax amounts
        String query = "SELECT SUM(tax_amount) AS totalTax FROM products";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Retrieve the total tax from the first row
                totalTax = cursor.getDouble(cursor.getColumnIndexOrThrow("totalTax"));
            }
            cursor.close(); // Close the cursor to free resources
        }

        return totalTax;
    }

    public double getTotalAmount() {
        double totalAmount = 0.00;
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to calculate the sum of tax amounts
        String query = "SELECT SUM(total_amount) AS totalAmount FROM products";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Retrieve the total Amount from the first row
                totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("totalAmount"));
            }
            cursor.close(); // Close the cursor to free resources
        }

        return totalAmount;
    }




}
