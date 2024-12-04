package com.example.inventry;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventry.Classes.PurchaseHistory;
import com.example.inventry.Databases.CategoryDatabaseHelper;

import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView availableQuantityText, lastPurchasePriceText, ProductName;
    private RecyclerView purchaseHistoryRecyclerView;
    private CategoryDatabaseHelper databaseHelper;
    private PurchaseHistoryAdapter purchaseHistoryAdapter;
    private ImageButton BackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Initialize views
        availableQuantityText = findViewById(R.id.Available_Quantity);
        lastPurchasePriceText = findViewById(R.id.Last_Purchase_Price);
        purchaseHistoryRecyclerView = findViewById(R.id.dynamic_Purchase_history_container);
        ProductName = findViewById(R.id.product_name);
        BackButton = findViewById(R.id.Back_Button);


        //set click listener on back button
        BackButton.setOnClickListener(v -> {
            finish();
        });

        // Initialize database helper
        databaseHelper = new CategoryDatabaseHelper(this);

        // Get product ID from Intent
        int productId = getIntent().getIntExtra("product_id", -1);

        if (productId != -1) {
            loadProductDetails(productId);
        } else {
            Log.e("ProductDetailActivity", "Invalid product ID");
        }
    }

    private void loadProductDetails(int productId) {
        // Fetch available quantity and last purchase price
        int availableQuantity = databaseHelper.getAvailableQuantity(productId);
        double lastPurchasePrice = databaseHelper.getLastPurchasePrice(productId);
        String productName = databaseHelper.getProductNameById(productId);

        // Update the UI
        availableQuantityText.setText(String.valueOf(availableQuantity));
        lastPurchasePriceText.setText(String.format("₹ %.2f", lastPurchasePrice));
        ProductName.setText(String.valueOf(productName));

        // Fetch purchase history
        List<PurchaseHistory> purchaseHistoryList = databaseHelper.getPurchaseHistory(productId);

        // Set up RecyclerView for purchase history
        purchaseHistoryAdapter = new PurchaseHistoryAdapter(purchaseHistoryList);
        purchaseHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        purchaseHistoryRecyclerView.setAdapter(purchaseHistoryAdapter);
    }
}
