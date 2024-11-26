package com.example.inventry;

import static android.app.DownloadManager.COLUMN_DESCRIPTION;

import static com.example.inventry.Helpers.CategoryDatabaseHelper.COLUMN_CATEGORY_DATE;
import static com.example.inventry.Helpers.CategoryDatabaseHelper.COLUMN_CATEGORY_NAME;
import static com.example.inventry.Helpers.CategoryDatabaseHelper.TABLE_CATEGORIES;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inventry.Classes.Category;
import com.example.inventry.Helpers.CategoryDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddProductActivity extends AppCompatActivity {

    private EditText itemNameEditText, quantityEditText, rateEditText, discountPercentageEditText, discountValueEditText;
    private TextView subtotalTextView, totalAmountTextView, TaxValue;
    private Spinner taxSpinner;
    private LinearLayout detailWindow;
    CategoryDatabaseHelper categoryDatabaseHelper; // Declare the database helper
    private Spinner categorySpinner;

    private int quantity = 1;
    private double rate = 0;
    private double discountPercentage = 0;
    private double discountValue = 0;
    private double subtotal = 0;
    private double totalAmount = 0;

    // Flag to avoid recursive updates when modifying discount fields
    private boolean isUpdatingDiscount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Initialize views
        itemNameEditText = findViewById(R.id.item_name);
        quantityEditText = findViewById(R.id.Quantity);
        rateEditText = findViewById(R.id.Rate);
        discountPercentageEditText = findViewById(R.id.discount_percentage_input);
        discountValueEditText = findViewById(R.id.discount_value_input);
        subtotalTextView = findViewById(R.id.subtotal);
        totalAmountTextView = findViewById(R.id.total_amount);
        taxSpinner = findViewById(R.id.tax_spinner);
        TaxValue = findViewById(R.id.tax_value);
        detailWindow = findViewById(R.id.Detail_window);
        categorySpinner = findViewById(R.id.category_spinner);

        // Initialize the CategoryDatabaseHelper in the onCreate method of AddProductActivity
        categoryDatabaseHelper = new CategoryDatabaseHelper(this);

        //Initialize the Spinner
        categorySpinner.setSelection(0);// Set default selection to the "None" option, assuming "None" is the first item in the list.
        populateCategorySpinner();

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                String selectedCategory = categorySpinner.getSelectedItem().toString();

                if (selectedCategory.equals("Add New Category")) {
                    // Show dialog to add new category
                    showAddCategoryDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });



        // Make the detail window visible when user starts typing in quantity or rate
        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                if (after > 0 || start > 0) {
                    detailWindow.setVisibility(View.VISIBLE);
                }
                updateCalculations();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    quantity = 1; // Default value for quantity
                } else {
                    quantity = (int) Double.parseDouble(editable.toString());
                }
                if (!isUpdatingDiscount) {
                    isUpdatingDiscount = true;
                    updateDiscountValue(); // Ensure discount value is updated when quantity changes
                    isUpdatingDiscount = false;
                }
                updateCalculations();
            }
        });

        rateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                if (after > 0 || start > 0) {
                    detailWindow.setVisibility(View.VISIBLE);
                }
                updateCalculations();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    rate = 0; // Default value for rate
                } else {
                    rate = Double.parseDouble(editable.toString());
                }
                if (!isUpdatingDiscount) {
                    isUpdatingDiscount = true;
                    updateDiscountValue(); // Ensure discount value is updated when rate changes
                    isUpdatingDiscount = false;
                }
                updateCalculations();
            }
        });

        // Handle discount percentage or value conversion
        discountPercentageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                if (after > 0 && !isUpdatingDiscount) {
                    isUpdatingDiscount = true;
                    updateDiscountValue(); // Update discount value when percentage is entered
                    isUpdatingDiscount = false;
                } else if (after == 0 && before > 0 && !isUpdatingDiscount) {
                    // Handle case when a digit is deleted
                    isUpdatingDiscount = true;
                    updateDiscountValue(); // Ensure value updates when a digit is removed
                    isUpdatingDiscount = false;
                }
                updateCalculations();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    discountPercentage = 0; // Default discount

                } else {
                    discountPercentage = Double.parseDouble(editable.toString());
                }
                updateCalculations();
            }
        });

        discountValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                if (after > 0 && !isUpdatingDiscount) {
                    isUpdatingDiscount = true;
                    updateDiscountPercentage(); // Update discount percentage when value is entered
                    isUpdatingDiscount = false;
                } else if (after == 0 && before > 0 && !isUpdatingDiscount) {
                    // Handle case when a digit is deleted
                    isUpdatingDiscount = true;
                    updateDiscountPercentage(); // Ensure percentage updates when a digit is removed
                    isUpdatingDiscount = false;
                }
                updateCalculations();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    isUpdatingDiscount = true;
                    updateDiscountPercentage(); // Update discount percentage when value is entered
                    isUpdatingDiscount = false;
                    discountValue = 0; // Default discount value
                } else {
                    discountValue = Double.parseDouble(editable.toString());
                }
                updateCalculations();
            }
        });

        taxSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                updateCalculations(); // Update the calculation when the tax item changes
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        findViewById(R.id.save_button).setOnClickListener(v -> saveData());
        findViewById(R.id.Cancel).setOnClickListener(v -> cancel());
    }


    // Method to get the selected item from the Spinner
    private String getSelectedCategory() {
        if (categorySpinner != null && categorySpinner.getSelectedItem() != null) {
            return categorySpinner.getSelectedItem().toString();
        } else {
            return null; // Handle case where nothing is selected
        }
    }




    private void populateCategorySpinner() {
        // Fetch all categories from the database
        List<Category> categories = categoryDatabaseHelper.getAllCategories();

        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("None"); // Add the default "None" option

        // Add existing category names
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        // Add the option to add a new category
        categoryNames.add("Add New Category");

        // Create and set the adapter for the spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // If the newly added category exists in the list, select it
        if (!categories.isEmpty()) {
            categorySpinner.setSelection(categoryNames.size() - 2); // Select the last added category
        }
    }




    private void showAddCategoryDialog() {
        // Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_category, null);

        EditText categoryNameEditText = view.findViewById(R.id.category_name);
        EditText categoryDescriptionEditText = view.findViewById(R.id.category_description);

        builder.setTitle("Add New Category")
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = categoryNameEditText.getText().toString().trim();
                    String description = categoryDescriptionEditText.getText().toString().trim();


                    if (!name.isEmpty() && !description.isEmpty()) {
                        if (categoryDatabaseHelper.isCategoryExists(name)) {
                            Toast.makeText(this, "Category already exists!", Toast.LENGTH_SHORT).show();
                            categorySpinner.setSelection(0);
                        } else {
                            // Save the new category
                            Category newCategory = new Category(name, description, getCurrentDate());
                            categoryDatabaseHelper.addCategory(newCategory); // Save to database
                            populateCategorySpinner(); // Refresh the spinner with new category list
                        }
                    } else {
                        Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        categorySpinner.setSelection(0);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> categorySpinner.setSelection(0))
                .create()
                .show();
    }




    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }



    public void addCategory(Category category) {
        SQLiteDatabase db = categoryDatabaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getName());
        values.put(COLUMN_DESCRIPTION, category.getDescription());
        values.put(COLUMN_CATEGORY_DATE, category.getDate());

        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    private void updateCalculations() {
        subtotal = rate * quantity;
        double discount = discountPercentage > 0 ? (calculateAmount() * discountPercentage / 100) : discountValue;
        subtotal -= discount;
        double taxPercentage = getTaxPercentage();
        double taxAmount = subtotal * taxPercentage / 100;
        totalAmount = subtotal + taxAmount;

        // Update the UI with the new values
        TaxValue.setText(String.valueOf(taxAmount));
        subtotalTextView.setText(String.valueOf(rate * quantity));
        totalAmountTextView.setText("₹ " + String.format("%.2f", totalAmount));
    }
    private double calculateAmount() {
        try {
            // Get rate and quantity from EditText, default to 0 if empty or invalid
            double rate = 0;
            double quantity = 0;

            // Check if rate and quantity fields are not empty and parse them, otherwise default to 0
            if (!rateEditText.getText().toString().isEmpty()) {
                rate = Double.parseDouble(rateEditText.getText().toString());
            }

            if (!quantityEditText.getText().toString().isEmpty()) {
                quantity = Double.parseDouble(quantityEditText.getText().toString());
            }

            // Calculate and return the amount
            return rate * quantity;
        } catch (NumberFormatException e) {
            // Handle invalid input and return 0 if there's an error
            // Optionally, show a toast or log the error
            return 0;
        }
    }

    private void updateDiscountValue() {
        // Calculate discount value from percentage if percentage is entered
        String discountPercentageText = discountPercentageEditText.getText().toString();

        if (discountPercentageText.isEmpty()) {
            discountValue = 0;  // Set discount value to 0 when the discount percentage is empty
            discountValueEditText.setText(""); // Optionally set the UI to 0.00
        } else {
            double fareed = rate * quantity;
            discountPercentage = Double.parseDouble(discountPercentageText);
            discountValue = (fareed * discountPercentage) / 100;
            discountValueEditText.setText(String.format("%.2f", discountValue)); // Update UI with calculated value
        }
    }


    private void updateDiscountPercentage() {
        // Update discount percentage from discount value if value is entered
        String discountValueText = discountValueEditText.getText().toString();

        if (discountValueText.isEmpty()) {
            discountPercentage = 0;  // Set discount percentage to 0 when the discount value is empty
            discountPercentageEditText.setText(""); // Optionally set the UI to 0.00
        } else {
            double fareed = rate * quantity;
            discountValue = Double.parseDouble(discountValueText);
            discountPercentage = (discountValue / fareed) * 100;
            discountPercentageEditText.setText(String.format("%.2f", discountPercentage)); // Update UI with calculated value
        }
    }

    private double getTaxPercentage() {
        // Safely get the selected tax string from the spinner
        String selectedTax = taxSpinner.getSelectedItem() != null ? taxSpinner.getSelectedItem().toString() : "";

        // Check if the selection is empty or invalid
        if (selectedTax.isEmpty()) {
            return 0.00;  // Default to 0% if no valid tax rate is selected
        }

        // Extract the percentage from the selected tax string
        if (selectedTax.contains("0%")) {
            return 0;
        } else if (selectedTax.contains("0.25%")) {
            return 0.25;
        } else if (selectedTax.contains("3%")) {
            return 3;
        } else if (selectedTax.contains("5%")) {
            return 5;
        } else if (selectedTax.contains("12%")) {
            return 12;
        } else if (selectedTax.contains("18%")) {
            return 18;
        } else if (selectedTax.contains("28%")) {
            return 28;
        } else {
            return 0;  // Default case if the tax rate doesn't match
        }
    }
    private void saveData() {
        // Validate itemName field
        String itemName = itemNameEditText.getText().toString().trim();
        if (itemName.isEmpty()) {
            itemNameEditText.setError("Item name cannot be empty");
            itemNameEditText.requestFocus();
            return;
        }

        // Validate quantity
        if (quantity <= 0) {
            Toast.makeText(this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate rate
        if (rate <= 0) {
            Toast.makeText(this, "Rate must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate discountPercentage
        if (discountPercentage < 0 || discountPercentage > 100) {
            Toast.makeText(this, "Discount percentage must be between 0 and 100", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate calculated values
        if (subtotal <= 0 || totalAmount <= 0) {
            Toast.makeText(this, "Subtotal or Total Amount cannot be zero or negative", Toast.LENGTH_SHORT).show();
            return;
        }

        double taxPercentage = getTaxPercentage();
        double taxAmount = subtotal * taxPercentage / 100;

        // If validation passes, send data to PurchaseActivity
        Intent intent = new Intent(this, PurchaseActivity.class);
        intent.putExtra("itemName", itemName);
        intent.putExtra("quantity", quantity);
        intent.putExtra("rate", rate);
        intent.putExtra("discountPercentage", discountPercentage);
        intent.putExtra("discountValue", discountValue);
        intent.putExtra("subtotal", calculateAmount());
        intent.putExtra("tax", getTaxPercentage());
        intent.putExtra("TaxValue" , String.valueOf(taxAmount));
        intent.putExtra("totalAmount", totalAmount);
        intent.putExtra("Category" , getSelectedCategory());


        Toast.makeText(this, "Validation Successful. Proceeding to Purchase Activity", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }

    private void cancel(){
        Intent intent = new Intent(this, PurchaseActivity.class);
        startActivity(intent);
        finish();

    }




}
