package com.example.inventry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inventry.Classes.Products;
import com.example.inventry.Databases.CategoryDatabaseHelper;

import java.util.List;

public class ProductListFragment extends Fragment {

    private static final String ARG_CATEGORY_ID = "category_id";
    private int categoryId;

    public static ProductListFragment newInstance(int categoryId) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt(ARG_CATEGORY_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        CategoryDatabaseHelper databaseHelper = new CategoryDatabaseHelper(requireContext());
        List<Products> products = databaseHelper.getProductsByCategoryId(categoryId);

        // Set up the RecyclerView with an adapter
        ProductAdapter adapter = new ProductAdapter(products);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
