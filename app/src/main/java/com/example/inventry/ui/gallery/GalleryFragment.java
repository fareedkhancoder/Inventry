package com.example.inventry.ui.gallery;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.inventry.Helpers.CategoryDatabaseHelper;
import com.example.inventry.Classes.Category;
import com.example.inventry.R;
import com.example.inventry.ViewPagerAdapter;
import com.example.inventry.databinding.FragmentGalleryBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private CategoryDatabaseHelper databaseHelper;
    private List<Category> categories;
    private ViewPagerAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        databaseHelper = new CategoryDatabaseHelper(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetch all categories initially
        categories = databaseHelper.getAllCategories();

        // Set up the ViewPager2 and adapter with the initial categories
        adapter = new ViewPagerAdapter(this, categories);
        binding.viewPager.setAdapter(adapter);

        // Link TabLayout and ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText(categories.get(position).getName())
        ).attach();

        EditText searchView = view.findViewById(R.id.search_view);

        // Set up the search icon click listener to trigger SearchView
        ImageView searchIcon = view.findViewById(R.id.search_category);
        searchIcon.setOnClickListener(v -> {
            // Toggle visibility of the SearchView
            toggleSearchView();
            // Adjust the marginTop of ViewPager2
            updateViewPagerMarginTop();

            clearEditText(searchView);

        });

        // Handle real-time text changes (TextWatcher)
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter categories in real-time as the user types
                filterCategories(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text changes
            }
        });

        // Handle text submission (e.g., pressing Enter key)
        searchView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                // Perform the search and clear the text after submission
                filterCategories(v.getText().toString());
                revertViewPagerMarginTop();
                toggleSearchView();
                return true; // Return true to indicate the event is handled
            }
            return false; // Pass the event to the next listener
        });

    }

    // update the marginTop of ViewPager2
    private void updateViewPagerMarginTop() {
        // Get the current LayoutParams of the ViewPager
        ViewPager2 viewPager = binding.viewPager;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) viewPager.getLayoutParams();

        // Set the new marginTop to 90dp
        int marginTop = (int) (90 * getResources().getDisplayMetrics().density); // Convert dp to pixels
        params.topMargin = marginTop;

        // Apply the updated LayoutParams to the ViewPager
        viewPager.setLayoutParams(params);
    }
    private void loseFocusFromEditText(EditText editText, Context context) {
        if (editText != null) {
            // Clear focus from the EditText
            editText.clearFocus();

            // Hide the soft keyboard
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        }
    }

    // revert the marginTop of ViewPager2
    private void revertViewPagerMarginTop() {
        // Get the current LayoutParams of the ViewPager
        ViewPager2 viewPager = binding.viewPager;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) viewPager.getLayoutParams();

        // Set the new marginTop to 45dp
        int marginTop = (int) (45 * getResources().getDisplayMetrics().density); // Convert dp to pixels
        params.topMargin = marginTop;

        // Apply the updated LayoutParams to the ViewPager
        viewPager.setLayoutParams(params);
    }

    // Method to focus the SearchView
    private void focusSearchView() {
        EditText searchView = binding.getRoot().findViewById(R.id.search_view);

        // Request focus for the SearchView
        searchView.requestFocus();

        // Optionally, you can show the keyboard programmatically (if required)
        showKeyboard(searchView);
    }

    // Method to show the keyboard
    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void clearEditText(EditText editText) {
        if (editText != null) {
            editText.setText(""); // Clear the text
        }
    }


    // toggle visibility of the SearchView
    private void toggleSearchView() {
        EditText searchView = binding.getRoot().findViewById(R.id.search_view);

        if (searchView.getVisibility() == View.GONE) {
            // Show the SearchView
            searchView.setVisibility(View.VISIBLE);
            // Set the focus to the SearchView so that the user can immediately type
            focusSearchView();
        } else {
            searchView.setVisibility(View.GONE);
            loseFocusFromEditText(searchView, getActivity());
            revertViewPagerMarginTop();
        }
    }

    private void filterCategories(String query) {
        // Find the category that matches the search query
        Category selectedCategory = null;

        for (Category category : categories) {
            if (category.getName().toLowerCase().contains(query.toLowerCase())) {
                selectedCategory = category;
                break;
            }
        }

        // Update the ViewPager's selected item to the category that matches the query
        if (selectedCategory != null) {
            int selectedIndex = categories.indexOf(selectedCategory);
            binding.viewPager.setCurrentItem(selectedIndex, true);
        } else {
            // If no match is found, you can handle this scenario (e.g., reset selection or show a message)
            // Optionally, you could keep the current category selected or reset the ViewPager selection.
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
