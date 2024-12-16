package com.example.inventry;


import static com.example.inventry.Databases.CategoryDatabaseHelper.COLUMN_AVAILABLE_QUANTITIES;
import static com.example.inventry.Databases.CategoryDatabaseHelper.COLUMN_CATEGORY_ID_FK;
import static com.example.inventry.Databases.CategoryDatabaseHelper.COLUMN_PRODUCT_NAME;
import static com.example.inventry.Databases.CategoryDatabaseHelper.COLUMN_PURCHASE_RATE;
import static com.example.inventry.Databases.CategoryDatabaseHelper.TABLE_PRODUCTS;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.inventry.Classes.TempProduct;
import com.example.inventry.Databases.CategoryDatabaseHelper;
import com.example.inventry.Databases.TempProductDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PurchaseActivity extends AppCompatActivity {

    private TempProductDatabaseHelper dbHelper;
    private CategoryDatabaseHelper dbHelper2;
    private LinearLayout dynamicContainer;
    String supplierName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        dbHelper = new TempProductDatabaseHelper(this);
        dbHelper2 = new CategoryDatabaseHelper(this);

        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        int newHeight = 500; // Set the height to 500 pixels (you can calculate this dynamically)

        // Retrieve data from the Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("itemName");
        int quantity = intent.getIntExtra("quantity", 1);
        double taxPercentage = intent.getDoubleExtra("tax", 0);
        String taxAmount = intent.getStringExtra("TaxValue");
        double discountPercentage = intent.getDoubleExtra("discountPercentage", 0);
        double discountAmount = intent.getDoubleExtra("discountValue", 0);
        double itemSubtotal = intent.getDoubleExtra("subtotal", 0);
        double totalAmount = intent.getDoubleExtra("totalAmount", 0);
        double rate = intent.getDoubleExtra("rate", 0);
        String category = intent.getStringExtra("Category");
        String Supplier_Name = intent.getStringExtra("Supplier Name");


        // Find the dynamic container
        Spinner spinnerPaymentMode = findViewById(R.id.payment_mode);
        dynamicContainer = findViewById(R.id.dynamicContainer);
        EditText supplierNameInput = findViewById(R.id.Supplier_Name_Input);
        supplierNameInput.setText(Supplier_Name);






        // Only populate the dynamic container if the subtotal is greater than zero
        if (itemSubtotal > 0) {
            // Create TempProduct object
            TempProduct tempProduct = new TempProduct(
                    productName, quantity, rate, taxPercentage, Double.parseDouble(taxAmount),
                    discountPercentage, discountAmount, itemSubtotal, totalAmount,category
            );

            // Save the tempProduct to the database
            dbHelper.addProduct(tempProduct);

            // Populate the dynamic container with the new tempProduct view
            populateDynamicContainer(dynamicContainer);
        }

        TextView totalDiscount = findViewById(R.id.totalDiscount);
        totalDiscount.setText(String.format("₹"+"%.2f", calculateTotalDiscount())); // Format to 2 decimal places

        TextView totalTax = findViewById(R.id.totalTax);
        totalTax.setText(String.format("₹"+"%.2f", calculateTotalTax())); // Format to 2 decimal places

        TextView total_Amount = findViewById(R.id.totalAmount);
        total_Amount.setText(String.format("₹"+"%.2f", calculateTotalAmount())); // Format to 2 decimal places
        // Check if the number of children in dynamicContainer is greater than 3
        if (dynamicContainer.getChildCount() > 3) {
            setLinearLayoutHeight(linearLayout, newHeight);// Call the method if condition is met
        }


        // Set click listeners
        findViewById(R.id.Back_Button).setOnClickListener(v -> reset_db());
        findViewById(R.id.buttonFirst).setOnClickListener(v -> {
            String SupplierName = supplierNameInput.getText().toString().trim();
            intent.setClass(PurchaseActivity.this, AddProductActivity.class);
            intent.putExtra("Supplier Name",SupplierName);
            startActivity(intent);
            finish();
        });
        findViewById(R.id.Purchase_button).setOnClickListener(v ->{
            add_to_db_2();
            Toast.makeText(this, "Purchased successfully!", Toast.LENGTH_SHORT).show();
            String SupplierName = supplierNameInput.getText().toString().trim();
            String payment_Method = spinnerPaymentMode.getSelectedItem().toString();
            int Supplier_ID = checkAndAddSupplier(SupplierName);
            long result = dbHelper2.addPurchase(Supplier_ID,getCurrentDate(),totalAmount,payment_Method);
            if (result == -1) {
                Toast.makeText(this, "Error during Purchase", Toast.LENGTH_SHORT).show();
            }
            // Clear the database
            dbHelper.clearProducts();
            refreshDynamicLayout();

        });
        findViewById(R.id.Cancel).setOnClickListener(v ->{
            reset_db();
        });
    }

    public void refreshDynamicLayout() {
        finish();
    }


    public int checkAndAddSupplier(String supplierName) {
        CategoryDatabaseHelper dbHelper = new CategoryDatabaseHelper(this);

        // Step 1: Check if the supplier exists
        int supplierId = dbHelper.getSupplierIdByName(supplierName);

        // Step 2: If the supplier doesn't exist, add a new supplier with placeholder values
        if (supplierId == -1) {
            // Placeholder values for missing fields
            String defaultContact = "N/A";
            String defaultEmail = "unknown@example.com";
            String defaultAddress = "No Address Provided";
            String defaultRemarks = "No Remarks";

            // Add the new supplier and retrieve its ID
            long newSupplierId = dbHelper.addSupplier(supplierName, defaultContact, defaultEmail, defaultAddress, defaultRemarks);

            if (newSupplierId != -1) {
                supplierId = (int) newSupplierId; // Cast to int as IDs are typically integers
            } else {
                Log.e("CheckAndAddSupplier", "Failed to add supplier: " + supplierName);
            }
        }

        // Step 3: Return the supplier ID
        return supplierId;
    }


    private void reset_db() {
        if (dynamicContainer.getChildCount() > 0) {

            new AlertDialog.Builder(this)
                    .setTitle("Confirm Exit")
                    .setMessage("Are you sure you want to go back? Unsaved changes will be lost.")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        // Clear the database
                        dbHelper.clearProducts();
                        // Call the super method to handle the default back button action
                        super.onBackPressed();
                    })
                    .setNegativeButton("No", (dialog, which) -> {

                        // Dismiss the dialog if user cancels
                        dialog.dismiss();
                    })
                    .setCancelable(false) // Prevent the dialog from being dismissed by tapping outside
                    .show();
        }else {
            finish();
        }
    }



    @Override
    public void onBackPressed() {
        // Create a confirmation dialog
        if (dynamicContainer.getChildCount() > 0) {

            new AlertDialog.Builder(this)
                    .setTitle("Confirm Exit")
                    .setMessage("Are you sure you want to go back? Unsaved changes will be lost.")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        // Clear the database
                        dbHelper.clearProducts();
                        // Call the super method to handle the default back button action
                        super.onBackPressed();
                    })
                    .setNegativeButton("No", (dialog, which) -> {

                        // Dismiss the dialog if user cancels
                        dialog.dismiss();
                    })
                    .setCancelable(false) // Prevent the dialog from being dismissed by tapping outside
                    .show();
        }else {
            finish();
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }


    public void scrollToLastChild(NestedScrollView nestedScrollView) {
        // Get the total number of child views
        int childCount = nestedScrollView.getChildCount();

        if (childCount > 0) {
            // Get the last child view (index is childCount - 1)
            View lastChild = nestedScrollView.getChildAt(childCount - 1);

            // Scroll to the bottom position of the last child view
            nestedScrollView.scrollTo(0, lastChild.getBottom());
        }
    }






    // Method to populate the container with products from the database
    private void populateDynamicContainer(LinearLayout dynamicContainer) {
        Cursor cursor = dbHelper.getAllProducts();

        // Check if cursor is not null and contains data
        if (cursor != null && cursor.moveToFirst()) {
            // Loop through all products in the database
            do {
                // Safely get column indices, checking if they're valid
                int nameIndex = cursor.getColumnIndex("name");
                int quantityIndex = cursor.getColumnIndex("quantity");
                int rateIndex = cursor.getColumnIndex("rate");
                int taxPercentageIndex = cursor.getColumnIndex("tax_percentage");
                int taxAmountIndex = cursor.getColumnIndex("tax_amount");
                int discountPercentageIndex = cursor.getColumnIndex("discount_percentage");
                int discountAmountIndex = cursor.getColumnIndex("discount_amount");
                int subtotalIndex = cursor.getColumnIndex("subtotal");
                int totalAmountIndex = cursor.getColumnIndex("total_amount");

                // Only proceed if the indices are valid
                if (nameIndex != -1 && quantityIndex != -1 && rateIndex != -1 && taxPercentageIndex != -1 &&
                        taxAmountIndex != -1 && discountPercentageIndex != -1 && discountAmountIndex != -1 &&
                        subtotalIndex != -1 && totalAmountIndex != -1) {

                    String name = cursor.getString(nameIndex);
                    int quantity = cursor.getInt(quantityIndex);
                    double rate = cursor.getDouble(rateIndex);
                    double taxPercentage = cursor.getDouble(taxPercentageIndex);
                    double taxAmount = cursor.getDouble(taxAmountIndex);
                    double discountPercentage = cursor.getDouble(discountPercentageIndex);
                    double discountAmount = cursor.getDouble(discountAmountIndex);
                    double subtotal = cursor.getDouble(subtotalIndex);
                    double totalAmount = cursor.getDouble(totalAmountIndex);

                    // Dynamically inflate the product view and populate it with data
                    View productView = getLayoutInflater().inflate(R.layout.productview, dynamicContainer, false);

                    TextView snTextView = productView.findViewById(R.id.SN);
                    TextView productNameTextView = productView.findViewById(R.id.product_name);
                    TextView totalAmountTextView = productView.findViewById(R.id.Total_Amount);
                    TextView taxPercentageTextView = productView.findViewById(R.id.Tax_percentage);
                    TextView taxAmountTextView = productView.findViewById(R.id.Tax_amount);
                    TextView discountPercentageTextView = productView.findViewById(R.id.discount_percentage);
                    TextView discountAmountTextView = productView.findViewById(R.id.discount_amount);
                    TextView itemSubtotalTextView = productView.findViewById(R.id.item_subtotal_amount);

                    snTextView.setText("#" + (dynamicContainer.getChildCount() + 1));
                    productNameTextView.setText(quantity + " - " + (name != null ? name : "Unknown TempProduct"));
                    totalAmountTextView.setText("₹" + totalAmount);
                    taxPercentageTextView.setText("Tax: " + taxPercentage + "%");
                    taxAmountTextView.setText("₹" + taxAmount);
                    discountPercentageTextView.setText("Discount: " + discountPercentage + "%");
                    discountAmountTextView.setText("₹" + discountAmount);
                    itemSubtotalTextView.setText("₹" + subtotal);

                    // Add the product view to the dynamic container
                    dynamicContainer.addView(productView);

                    // After adding the product, scroll to the last child
                    NestedScrollView nestedScrollView = findViewById(R.id.scrollView);
                    // Scroll to the last child after populating the container
                    nestedScrollView.post(() -> scrollToLastChild(nestedScrollView));   // Call the scroll method to scroll to the last child

                } else {
                    // Handle the case where one of the indices is invalid
                    Toast.makeText(this, "Error: Invalid database column", Toast.LENGTH_SHORT).show();
                }

            } while (cursor.moveToNext());
        } else {
        }

        cursor.close();
    }

    // Method to Add Products in the database2
    private void add_to_db_2() {
        Cursor cursor = dbHelper.getAllProducts();

        // Check if cursor is not null and contains data
        if (cursor != null && cursor.moveToFirst()) {
            // Loop through all products in the database
            do {
                // Safely get column indices, checking if they're valid
                int nameIndex = cursor.getColumnIndex("name");
                int quantityIndex = cursor.getColumnIndex("quantity");
                int rateIndex = cursor.getColumnIndex("rate");
                int taxPercentageIndex = cursor.getColumnIndex("tax_percentage");
                int taxAmountIndex = cursor.getColumnIndex("tax_amount");
                int discountPercentageIndex = cursor.getColumnIndex("discount_percentage");
                int discountAmountIndex = cursor.getColumnIndex("discount_amount");
                int subtotalIndex = cursor.getColumnIndex("subtotal");
                int totalAmountIndex = cursor.getColumnIndex("total_amount");
                int categoryIndex = cursor.getColumnIndex("category");

                // Only proceed if the indices are valid
                if (nameIndex != -1 && quantityIndex != -1 && rateIndex != -1 && taxPercentageIndex != -1 &&
                        taxAmountIndex != -1 && discountPercentageIndex != -1 && discountAmountIndex != -1 &&
                        subtotalIndex != -1 && totalAmountIndex != -1 && categoryIndex != -1) {

                    String name = cursor.getString(nameIndex);
                    int quantity = cursor.getInt(quantityIndex);
                    double rate = cursor.getDouble(rateIndex);
                    double taxPercentage = cursor.getDouble(taxPercentageIndex);
                    double taxAmount = cursor.getDouble(taxAmountIndex);
                    double discountPercentage = cursor.getDouble(discountPercentageIndex);
                    double discountAmount = cursor.getDouble(discountAmountIndex);
                    double subtotal = cursor.getDouble(subtotalIndex);
                    double totalAmount = cursor.getDouble(totalAmountIndex);
                    String category = cursor.getString(categoryIndex);

                    // Get the category ID based on the name
                    if (dbHelper2 != null) {
                        int categoryId = dbHelper2.getCategoryIdByName(category);
                        Log.d("add_to_db_2", "categoryId: " + categoryId);

                        // Check if the product exists in the given category
                        boolean productExists = dbHelper2.isProductExists(categoryId, name);

                        if (productExists) {
                            // Product already exists, so update it
                            updateProduct(categoryId, name, quantity, rate);
                        } else {
                            // Product does not exist, add a new product
                            addNewProduct(categoryId, name, quantity, rate);
                        }

                        // Insert Product detail to purchaseDetail Table
                        int PurchaseId = dbHelper2.getNextPurchaseId();
                        int ProductId = dbHelper2.getNextProductId();
                        long result2 = dbHelper2.addPurchaseDetails(PurchaseId, ProductId, quantity, rate, discountPercentage, discountAmount, taxPercentage, taxAmount, totalAmount, subtotal);
                        if (result2 == -1) {
                            Toast.makeText(this, "Error adding product details.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle the case where one of the indices is invalid
                        Toast.makeText(this, "Error: Invalid database column", Toast.LENGTH_SHORT).show();
                        Log.e("PurchaseActivity", "dbHelper2 is null");
                    }
                }

            } while (cursor.moveToNext());
            cursor.close();
        } else {
            // Handle the case where the cursor is empty or null
            Log.e("PurchaseActivity", "No products found or cursor is null");
        }
    }


    // Method to update an existing product
    private void updateProduct(int categoryId, String productName, int quantity, double rate) {
        SQLiteDatabase db = dbHelper2.getWritableDatabase();

        // Step 1: Retrieve the existing product's quantity and rate
        Cursor cursor = db.query(
                TABLE_PRODUCTS,
                new String[]{COLUMN_AVAILABLE_QUANTITIES, COLUMN_PURCHASE_RATE},
                COLUMN_CATEGORY_ID_FK + " = ? AND " + COLUMN_PRODUCT_NAME + " = ?",
                new String[]{String.valueOf(categoryId), productName},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            // Step 2: Safely retrieve existing quantity and rate
            int existingQuantityIndex = cursor.getColumnIndex(COLUMN_AVAILABLE_QUANTITIES);
            int existingRateIndex = cursor.getColumnIndex(COLUMN_PURCHASE_RATE);

            // Check if column indices are valid
            if (existingQuantityIndex >= 0 && existingRateIndex >= 0) {
                int existingQuantity = cursor.getInt(existingQuantityIndex);
                double existingRate = cursor.getDouble(existingRateIndex);

                // Step 3: Add the new quantity to the existing quantity
                int updatedQuantity = existingQuantity + quantity;

                // Step 4: Replace the old rate with the new rate
                double updatedRate = rate; // New rate will replace the old one

                // Step 5: Update the product in the products table with new quantity and rate
                ContentValues values = new ContentValues();
                values.put(COLUMN_AVAILABLE_QUANTITIES, updatedQuantity);
                values.put(COLUMN_PURCHASE_RATE, updatedRate);

                // Perform the update
                db.update(
                        TABLE_PRODUCTS,
                        values,
                        COLUMN_CATEGORY_ID_FK + " = ? AND " + COLUMN_PRODUCT_NAME + " = ?",
                        new String[]{String.valueOf(categoryId), productName}
                );
            } else {
                // Handle case where column index is invalid
                Log.e("updateProduct", "Column index invalid: existingQuantityIndex=" + existingQuantityIndex + ", existingRateIndex=" + existingRateIndex);
            }

            cursor.close();
        } else {
            // Handle case if the product doesn't exist in the database
            if (cursor != null) {
                cursor.close();
            }
            Log.e("updateProduct", "Product not found: " + productName);
        }
    }

    // Method to add a new product
    private void addNewProduct(int categoryId, String productName, int quantity, double rate) {
        SQLiteDatabase db = dbHelper2.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, productName);
        values.put(COLUMN_PURCHASE_RATE, rate);
        values.put(COLUMN_AVAILABLE_QUANTITIES, quantity);
        values.put(COLUMN_CATEGORY_ID_FK, categoryId);

        // Insert the new product into the products table
        db.insert(TABLE_PRODUCTS, null, values);
    }



    public void setLinearLayoutHeight(LinearLayout linearLayout, int height) {
        // Get the current layout parameters of the LinearLayout
        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();

        // Update the height to the new value
        layoutParams.height = height;

        // Apply the updated layout parameters to the LinearLayout
        linearLayout.setLayoutParams(layoutParams);
    }

    private double calculateTotalDiscount() {

        return dbHelper.getTotalDiscount();
    }
    private double calculateTotalTax() {

        return dbHelper.getTotalTax();
    }
    private double calculateTotalAmount() {

        return dbHelper.getTotalAmount();
    }


    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout dynamicContainer = findViewById(R.id.dynamicContainer);
        dynamicContainer.removeAllViews(); // Clear the container
        populateDynamicContainer(dynamicContainer); // Repopulate with database data
    }

}
