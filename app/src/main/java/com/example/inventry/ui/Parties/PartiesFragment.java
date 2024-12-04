package com.example.inventry.ui.Parties;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inventry.Classes.Supplier;
import com.example.inventry.R;
import com.example.inventry.Databases.CategoryDatabaseHelper;
import com.example.inventry.SuppliersAdapter;

import java.util.ArrayList;
import java.util.List;

public class PartiesFragment extends Fragment {

    private RecyclerView recyclerView;
    private SuppliersAdapter adapter;
    private CategoryDatabaseHelper dbHelper;

    public PartiesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_parties, container, false);
        recyclerView = rootView.findViewById(R.id.Parties_list_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = CategoryDatabaseHelper.getInstance(getContext());
        List<Supplier> suppliers = fetchSuppliers();
        adapter = new SuppliersAdapter(suppliers);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private List<Supplier> fetchSuppliers() {
        // Assuming `Supplier` is a model class for suppliers.
        return dbHelper.getAllSuppliers(); // Fetch from your database.
    }
}
