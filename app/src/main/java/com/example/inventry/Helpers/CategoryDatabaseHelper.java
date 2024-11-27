package com.example.inventry.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.SQLException;
import android.util.Log;

import com.example.inventry.Classes.Category;
import com.example.inventry.Classes.Products;

import java.util.ArrayList;
import java.util.List;

public class CategoryDatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventoryDB";
    private static final int DATABASE_VERSION = 2;


    // Categories table
    public static final String TABLE_CATEGORIES = "categories";
    private static final String COLUMN_CATEGORY_ID = "id";
    public static final String COLUMN_CATEGORY_NAME = "name";
    public static final String COLUMN_CATEGORY_DESCRIPTION = "description";
    public static final String COLUMN_CATEGORY_DATE = "date";

    // Products table
    public static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_PRODUCT_NAME = "product_name";
    public static final String COLUMN_PURCHASE_RATE = "purchase_rate";
    public static final String COLUMN_AVAILABLE_QUANTITIES = "available_quantities";
    public static final String COLUMN_CATEGORY_ID_FK = "category_id";

    // SQL to create categories table
    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
            COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CATEGORY_NAME + " TEXT, " +
            COLUMN_CATEGORY_DESCRIPTION + " TEXT, " +
            COLUMN_CATEGORY_DATE + " TEXT" +
            ");";

    // SQL to create products table
    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
            COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PRODUCT_NAME + " TEXT, " +
            COLUMN_PURCHASE_RATE + " REAL, " +
            COLUMN_AVAILABLE_QUANTITIES + " INTEGER, " +
            COLUMN_CATEGORY_ID_FK + " INTEGER, " +
            "FOREIGN KEY(" + COLUMN_CATEGORY_ID_FK + ") REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + ")" +
            ");";

    public CategoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_PRODUCTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    // Add a new product
    public long addProduct(String productName, double purchaseRate, int availableQuantities, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, productName);
        values.put(COLUMN_PURCHASE_RATE, purchaseRate);
        values.put(COLUMN_AVAILABLE_QUANTITIES, availableQuantities);
        values.put(COLUMN_CATEGORY_ID_FK, categoryId);

        return db.insert(TABLE_PRODUCTS, null, values);
    }

    // Update a product
    public int updateProduct(int productId, String productName, double purchaseRate, int availableQuantities, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, productName);
        values.put(COLUMN_PURCHASE_RATE, purchaseRate);
        values.put(COLUMN_AVAILABLE_QUANTITIES, availableQuantities);
        values.put(COLUMN_CATEGORY_ID_FK, categoryId);

        return db.update(TABLE_PRODUCTS, values, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
    }

    // Delete a product
    public void deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
    }


    // Method to get category ID by category name
    public int getCategoryIdByName(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int categoryId = -1; // Default to -1 if not found

        try {
            // Query the database for the product with the given name
            cursor = db.query(TABLE_CATEGORIES, new String[]{COLUMN_CATEGORY_ID}, COLUMN_CATEGORY_NAME + "=?",
                    new String[]{category}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Get the category ID from the cursor
                categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close(); // Always close the cursor
            }
        }

        return categoryId;
    }

    // Get all products by category ID
    public List<Products> getProductsByCategoryId(int categoryId) {
        List<Products> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCTS,
                null,
                COLUMN_CATEGORY_ID_FK + " = ?",
                new String[]{String.valueOf(categoryId)},
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME));
                double rate = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PURCHASE_RATE));
                int quantities = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AVAILABLE_QUANTITIES));

                products.add(new Products(id, name, rate, quantities, categoryId));
            }
            cursor.close();
        }

        return products;
    }

    // Add a new category
    public long addCategory(Category category) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a ContentValues object to store the data
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getName());
        values.put(COLUMN_CATEGORY_DESCRIPTION, category.getDescription());
        values.put(COLUMN_CATEGORY_DATE, category.getDate());

        // Insert the data into the table
        long id = db.insert(TABLE_CATEGORIES, null, values);

        // Log the inserted category ID (for debugging)
        if (id != -1) {
            Log.d("CategoryInsertion", "Inserted category with ID: " + id);
        } else {
            Log.d("CategoryInsertion", "Failed to insert category.");
        }

        // Close the database
        db.close();

        // Return the ID of the inserted category (or -1 if insertion failed)
        return id;
    }

    // Get a category by ID
    public Category getCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Category category = null;

        try {
            cursor = db.query(TABLE_CATEGORIES, null, COLUMN_CATEGORY_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Safely check if the column indexes are valid
                int idColumnIndex = cursor.getColumnIndex(COLUMN_CATEGORY_ID);
                int nameColumnIndex = cursor.getColumnIndex(COLUMN_CATEGORY_NAME);
                int descriptionColumnIndex = cursor.getColumnIndex(COLUMN_CATEGORY_DESCRIPTION);
                int dateColumnIndex = cursor.getColumnIndex(COLUMN_CATEGORY_DATE);

                // Check if column indexes are valid (>= 0)
                if (idColumnIndex >= 0 && nameColumnIndex >= 0 && descriptionColumnIndex >= 0 && dateColumnIndex >= 0) {
                    category = new Category(
                            cursor.getInt(idColumnIndex),
                            cursor.getString(nameColumnIndex),
                            cursor.getString(descriptionColumnIndex),
                            cursor.getString(dateColumnIndex)
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the error or handle it as appropriate
        } finally {
            if (cursor != null) {
                cursor.close();  // Always close the cursor to release resources
            }
        }

        return category;
    }


    // Update a category
    public int updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getName());
        values.put(COLUMN_CATEGORY_DESCRIPTION, category.getDescription());
        values.put(COLUMN_CATEGORY_DATE, category.getDate());

        return db.update(TABLE_CATEGORIES, values, COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(category.getId())});
    }

    // Delete a category
    public void deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORIES, COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Get all categories
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_CATEGORIES,
                new String[]{COLUMN_CATEGORY_ID, COLUMN_CATEGORY_NAME, COLUMN_CATEGORY_DESCRIPTION, COLUMN_CATEGORY_DATE},
                null, null, null, null, null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_DATE));

                Category category = new Category(id, name, description, date);
                categories.add(category);
            }
            cursor.close();
        }

        Log.d("GalleryFragment", "Total Categories fetched: " + categories.size());  // Add this log
        db.close(); // Close the database
        return categories;
    }

    public List<Category> searchCategories(String query) {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Use a SQL query to filter categories based on the search query
        String selection = COLUMN_CATEGORY_NAME + " LIKE ?";
        String[] selectionArgs = new String[] { "%" + query + "%" };

        // Execute the query
        Cursor cursor = db.query(TABLE_CATEGORIES, null, selection, selectionArgs,
                null, null, null);

        // Process the results
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_DATE));

                categories.add(new Category(id, name, description, date));
            }
            cursor.close();
        }

        db.close();
        return categories;
    }

    public boolean isCategoryExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM categories WHERE name = ?", new String[]{name});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
