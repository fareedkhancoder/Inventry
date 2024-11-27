package com.example.inventry.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.inventry.Helpers.TempProductDatabaseHelper;
import com.example.inventry.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TempProductDatabaseHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize the database helper to interact with the database
        dbHelper = TempProductDatabaseHelper.getInstance(getContext());

        // Call the method to reset the database when the fragment is created
        reset_db();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void reset_db() {
        // Reset the database by clearing all products
        if (dbHelper != null) {
            dbHelper.clearProducts();
        }
    }
}
