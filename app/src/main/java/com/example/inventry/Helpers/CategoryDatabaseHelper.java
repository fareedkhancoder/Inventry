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


    private static CategoryDatabaseHelper instance;



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

    // Purchase table
    public static final String TABLE_PURCHASE = "purchase";
    private static final String COLUMN_PURCHASE_ID = "purchase_id";
    private static final String COLUMN_SUPPLIER_ID_FK = "supplier_id";
    private static final String COLUMN_PURCHASE_DATE = "purchase_date";
    private static final String COLUMN_TOTAL_COST = "total_cost";
    private static final String COLUMN_PAYMENT_METHOD = "payment_method";

    // PurchaseDetails table
    public static final String TABLE_PURCHASE_DETAILS = "purchase_details";
    private static final String COLUMN_PURCHASE_DETAIL_ID = "purchase_detail_id";
    private static final String COLUMN_PURCHASE_ID_FK = "purchase_id";
    private static final String COLUMN_PRODUCT_ID_FK = "product_id";
    private static final String COLUMN_QUANTITY_PURCHASED = "quantity_purchased";
    private static final String COLUMN_RATE = "rate";
    private static final String COLUMN_DISCOUNT_PERCENTAGE = "discount_percentage";
    private static final String COLUMN_DISCOUNT_AMOUNT = "discount_amount";
    private static final String COLUMN_TAX_PERCENTAGE = "tax_percentage";
    private static final String COLUMN_TAX_AMOUNT = "tax_amount";
    private static final String COLUMN_TOTAL_PRODUCT_COST = "total_product_cost";
    private static final String COLUMN_SUBTOTAL= "subtotal";

    // Suppliers table
    public static final String TABLE_SUPPLIERS = "suppliers";
    public static final String COLUMN_SUPPLIER_ID = "supplier_id";
    public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
    public static final String COLUMN_SUPPLIER_CONTACT = "supplier_contact";
    public static final String COLUMN_SUPPLIER_EMAIL = "supplier_email";
    public static final String COLUMN_SUPPLIER_ADDRESS = "supplier_address";
    public static final String COLUMN_REMARKS = "remarks";

    // SQL to create categories table
    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
            COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CATEGORY_NAME + " TEXT, " +
            COLUMN_CATEGORY_DESCRIPTION + " TEXT, " +
            COLUMN_CATEGORY_DATE + " TEXT" +
            ");";

    // SQL to create Purchase table
    private static final String CREATE_TABLE_PURCHASE = "CREATE TABLE " + TABLE_PURCHASE + " (" +
            COLUMN_PURCHASE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SUPPLIER_ID_FK + " INTEGER, " +
            COLUMN_PURCHASE_DATE + " TEXT, " +
            COLUMN_TOTAL_COST + " REAL, " +
            COLUMN_PAYMENT_METHOD + " TEXT" +
            ");";

    // SQL to create PurchaseDetails table
    private static final String CREATE_TABLE_PURCHASE_DETAILS = "CREATE TABLE " + TABLE_PURCHASE_DETAILS + " (" +
            COLUMN_PURCHASE_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PURCHASE_ID_FK + " INTEGER, " +
            COLUMN_PRODUCT_ID_FK + " INTEGER, " +
            COLUMN_QUANTITY_PURCHASED + " INTEGER, " +
            COLUMN_RATE + " REAL, " +
            COLUMN_DISCOUNT_PERCENTAGE + " REAL, " +
            COLUMN_DISCOUNT_AMOUNT + " REAL, " +
            COLUMN_TAX_PERCENTAGE + " REAL, " +
            COLUMN_TAX_AMOUNT + " REAL, " +
            COLUMN_TOTAL_PRODUCT_COST + " REAL, " +
            COLUMN_SUBTOTAL + " REAL, " +
            "FOREIGN KEY(" + COLUMN_PURCHASE_ID_FK + ") REFERENCES " + TABLE_PURCHASE + "(" + COLUMN_PURCHASE_ID + "), " +
            "FOREIGN KEY(" + COLUMN_PRODUCT_ID_FK + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ")" +
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
    // Public method to get the singleton instance
    public static synchronized CategoryDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new CategoryDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    // SQL to create SUPPLIER table
    private static final String CREATE_TABLE_SUPPLIERS = "CREATE TABLE " + TABLE_SUPPLIERS + " (" +
            COLUMN_SUPPLIER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, " +
            COLUMN_SUPPLIER_CONTACT + " TEXT, " +
            COLUMN_SUPPLIER_EMAIL + " TEXT, " +
            COLUMN_SUPPLIER_ADDRESS + " TEXT, " +
            COLUMN_REMARKS + " TEXT" +
            ");";



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_PURCHASE);
        db.execSQL(CREATE_TABLE_PURCHASE_DETAILS);
        db.execSQL(CREATE_TABLE_SUPPLIERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PURCHASE_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PURCHASE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPLIERS);
        onCreate(db);
    }
      //Method for deleting a supplier
    public void deleteSupplier(int supplierId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUPPLIERS, COLUMN_SUPPLIER_ID + " = ?", new String[]{String.valueOf(supplierId)});
    }


    // Method to get supplier ID by name
    public int getSupplierIdByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int supplierId = -1; // Default to -1 if the supplier is not found

        try {
            // Query the Suppliers table for the supplier name
            cursor = db.query(
                    TABLE_SUPPLIERS, // Table name
                    new String[]{COLUMN_SUPPLIER_ID}, // Columns to fetch
                    COLUMN_SUPPLIER_NAME + " = ?", // WHERE clause
                    new String[]{name}, // WHERE arguments
                    null, null, null
            );

            if (cursor != null && cursor.moveToFirst()) {
                // Get the supplier ID from the cursor
                supplierId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUPPLIER_ID));
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log any errors
        } finally {
            if (cursor != null) {
                cursor.close(); // Always close the cursor
            }
        }

        return supplierId;
    }

    // Method to update a supplier

    public int updateSupplier(int supplierId, String name, String contact, String email, String address, String remarks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUPPLIER_NAME, name);
        values.put(COLUMN_SUPPLIER_CONTACT, contact);
        values.put(COLUMN_SUPPLIER_EMAIL, email);
        values.put(COLUMN_SUPPLIER_ADDRESS, address);
        values.put(COLUMN_REMARKS, remarks);

        return db.update(TABLE_SUPPLIERS, values, COLUMN_SUPPLIER_ID + " = ?", new String[]{String.valueOf(supplierId)});
    }


    // Method to fetch all the suppliers

    public List<String> getAllSuppliers() {
        List<String> suppliers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_SUPPLIERS,
                new String[]{COLUMN_SUPPLIER_ID, COLUMN_SUPPLIER_NAME, COLUMN_SUPPLIER_CONTACT},
                null, null, null, null, COLUMN_SUPPLIER_NAME + " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String supplier = "ID: " + cursor.getInt(0) + ", Name: " + cursor.getString(1);
                suppliers.add(supplier);
            }
            cursor.close();
        }
        return suppliers;
    }


    // Method to add a new supplier

    public long addSupplier(String name, String contact, String email, String address, String remarks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUPPLIER_NAME, name);
        values.put(COLUMN_SUPPLIER_CONTACT, contact);
        values.put(COLUMN_SUPPLIER_EMAIL, email);
        values.put(COLUMN_SUPPLIER_ADDRESS, address);
        values.put(COLUMN_REMARKS, remarks);

        return db.insert(TABLE_SUPPLIERS, null, values);
    }

    // Method to get the next purchase ID
    public int getNextPurchaseId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int nextId = 1; // Default to 1 if the table is empty

        try {
            // Get the maximum ID from the Purchase table
            cursor = db.rawQuery("SELECT MAX(" + COLUMN_PURCHASE_ID + ") FROM " + TABLE_PURCHASE, null);
            if (cursor != null && cursor.moveToFirst()) {
                int maxId = cursor.getInt(0);
                nextId = maxId + 1; // Next ID is the max ID + 1
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close(); // Always close the cursor
            }
        }

        return nextId;
    }


    // Method to get the next product ID
    public int getNextProductId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int nextId = 1; // Default to 1 if the table is empty

        try {
            // Get the maximum ID from the Products table
            cursor = db.rawQuery("SELECT MAX(" + COLUMN_PRODUCT_ID + ") FROM " + TABLE_PRODUCTS, null);
            if (cursor != null && cursor.moveToFirst()) {
                int maxId = cursor.getInt(0);
                nextId = maxId; // Next ID is the max ID + 1
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close(); // Always close the cursor
            }
        }

        return nextId;
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
    // Add A new purchase
    public long addPurchase(int supplierId, String purchaseDate, double totalCost, String paymentMethod) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUPPLIER_ID_FK, supplierId);
        values.put(COLUMN_PURCHASE_DATE, purchaseDate);
        values.put(COLUMN_TOTAL_COST, totalCost);
        values.put(COLUMN_PAYMENT_METHOD, paymentMethod);

        return db.insert(TABLE_PURCHASE, null, values);
    }
    // Add Purchase Details
    public long addPurchaseDetails(int purchaseId, int productId, int quantityPurchased, double purchaseRate, double discPercentage, double discAmount, double taxPercentage, double taxAmount, double totalCost, double subtotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PURCHASE_ID_FK, purchaseId);
        values.put(COLUMN_PRODUCT_ID_FK, productId);
        values.put(COLUMN_QUANTITY_PURCHASED, quantityPurchased);
        values.put(COLUMN_RATE, purchaseRate);
        values.put(COLUMN_DISCOUNT_PERCENTAGE, discPercentage);
        values.put(COLUMN_DISCOUNT_AMOUNT, discAmount);
        values.put(COLUMN_TAX_PERCENTAGE, taxPercentage);
        values.put(COLUMN_TAX_AMOUNT, taxAmount);
        values.put(COLUMN_TOTAL_PRODUCT_COST, totalCost);
        values.put(COLUMN_SUBTOTAL, subtotal);

        return db.insert(TABLE_PURCHASE_DETAILS, null, values);
    }

    public Cursor getAllPurchases() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PURCHASE, null, null, null, null, null, COLUMN_PURCHASE_DATE + " DESC");
    }

    public List<String> getPurchaseDetails(int purchaseId) {
        List<String> purchaseDetails = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PURCHASE_DETAILS,
                null,
                COLUMN_PURCHASE_ID_FK + " = ?",
                new String[]{String.valueOf(purchaseId)},
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID_FK));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY_PURCHASED));
                double rate = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RATE));
                double cost = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_PRODUCT_COST));
                double discPercentage = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISCOUNT_PERCENTAGE));
                double discAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISCOUNT_AMOUNT));
                double taxPercentage = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TAX_PERCENTAGE));
                double taxAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TAX_AMOUNT));

                purchaseDetails.add("Product ID: " + productId + ", Quantity: " + quantity + ", Rate: " + rate + ", Cost: " + cost + ", discount Percentage:" + discPercentage + "discount Amount:" + discAmount + "tax Percantage:" + taxPercentage + "tax Amount:" + taxAmount);
            }
            cursor.close();
        }

        return purchaseDetails;
    }

    // Get the total stock value from the Products table
    public double getTotalStockValue() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalStockValue = 0.0;

        String query = "SELECT SUM(" + COLUMN_PURCHASE_RATE + " * " + COLUMN_AVAILABLE_QUANTITIES + ") FROM " + TABLE_PRODUCTS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            totalStockValue = cursor.getDouble(0);  // Get the total stock value
            cursor.close();
        }

        return totalStockValue;
    }

    // Get top 3 products by stock value (quantity * purchase_rate)
    public List<Products> getTop3ProductsByStockValue() {
        List<Products> topProducts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to get the top 3 products by stock value (quantity * purchase_rate)
        String query = "SELECT * FROM " + TABLE_PRODUCTS +
                " ORDER BY " + COLUMN_AVAILABLE_QUANTITIES + " * " + COLUMN_PURCHASE_RATE + " DESC LIMIT 3";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Safely get the column index for each column
                int productIdIndex = cursor.getColumnIndex(COLUMN_PRODUCT_ID);
                int productNameIndex = cursor.getColumnIndex(COLUMN_PRODUCT_NAME);
                int availableQuantitiesIndex = cursor.getColumnIndex(COLUMN_AVAILABLE_QUANTITIES);
                int purchaseRateIndex = cursor.getColumnIndex(COLUMN_PURCHASE_RATE);
                int categoryIdIndex = cursor.getColumnIndex(COLUMN_CATEGORY_ID);

                // Ensure the column index is valid (>= 0)
                if (productIdIndex >= 0 && productNameIndex >= 0 && availableQuantitiesIndex >= 0 &&
                        purchaseRateIndex >= 0 && categoryIdIndex >= 0) {

                    // Create the Product object and add it to the list
                    int productId = cursor.getInt(productIdIndex);
                    String productName = cursor.getString(productNameIndex);
                    int availableQuantities = cursor.getInt(availableQuantitiesIndex);
                    double purchaseRate = cursor.getDouble(purchaseRateIndex);
                    int categoryId = cursor.getInt(categoryIdIndex);

                    // Add the product to the list
                    topProducts.add(new Products(productId, productName, purchaseRate, availableQuantities, categoryId));
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        return topProducts;
    }

}
