package com.example.inventry;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.inventry.Classes.Product;
import com.example.inventry.Helpers.CategoryDatabaseHelper;
import com.example.inventry.Helpers.ProductDatabaseHelper;

public class PurchaseActivity extends AppCompatActivity {

    private ProductDatabaseHelper dbHelper;
    private CategoryDatabaseHelper dbHelper2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        dbHelper = new ProductDatabaseHelper(this);
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

        // Find the dynamic container
        LinearLayout dynamicContainer = findViewById(R.id.dynamicContainer);



        // Only populate the dynamic container if the subtotal is greater than zero
        if (itemSubtotal > 0) {
            // Create Product object
            Product product = new Product(
                    productName, quantity, rate, taxPercentage, Double.parseDouble(taxAmount),
                    discountPercentage, discountAmount, itemSubtotal, totalAmount,category
            );

            // Save the product to the database
            dbHelper.addProduct(product);

            // Populate the dynamic container with the new product view
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
            intent.setClass(PurchaseActivity.this, AddProductActivity.class);
            startActivity(intent);
            finish();
        });
        findViewById(R.id.Purchase_button).setOnClickListener(v ->{
            add_to_db_2();
            Toast.makeText(this, "Purchased successfully!", Toast.LENGTH_SHORT).show();

        });
        findViewById(R.id.Cancel).setOnClickListener(v ->{
            reset_db();
        });
    }

    private void reset_db() {
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
    }
    private void insertProduct(String productName, double purchaseRate, int availableQuantities, int categoryId) {
        // Insert the product into the database
        long result = dbHelper2.addProduct(productName, purchaseRate, availableQuantities, categoryId);

        // Check if the product was inserted successfully
        if (result != -1) {
            Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error adding product", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onBackPressed() {
        // Create a confirmation dialog
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
                    productNameTextView.setText(quantity + " - " + (name != null ? name : "Unknown Product"));
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
            // Handle the case where the cursor is empty or null
            Toast.makeText(this, "No products found in the database", Toast.LENGTH_SHORT).show();
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

                    // Get the product ID based on the name
                    if (dbHelper2 != null) {
                        int categoryId = dbHelper2.getCategoryIdByName(category);
                        Log.d("add_to_db_2", "categoryId: " + categoryId);

                        // Insert product into the database2
                        long result = dbHelper2.addProduct(name, rate, quantity, categoryId);
                        if (result != -1) {
                            Toast.makeText(this, "Products added successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error adding products.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle the case where one of the indices is invalid
                        Toast.makeText(this, "Error: Invalid database column", Toast.LENGTH_SHORT).show();
                        Log.e("PurchaseActivity", "dbHelper2 is null");
                    }
                }

            } while (cursor.moveToNext());
        } else {
            // Handle the case where the cursor is empty or null
            Toast.makeText(this, "No products found in the database", Toast.LENGTH_SHORT).show();
            Log.e("PurchaseActivity", "No products found or cursor is null");
        }

        cursor.close();
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