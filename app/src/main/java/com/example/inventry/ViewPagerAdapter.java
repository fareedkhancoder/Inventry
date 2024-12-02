package com.example.inventry;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.inventry.Classes.Category;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private List<Category> categories;

    public ViewPagerAdapter(@NonNull Fragment fragment, List<Category> categories) {
        super(fragment);
        this.categories = categories != null ? categories : new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Category category = categories.get(position);
        return ProductListFragment.newInstance(category.getId());
    }

    public void updateCategories(List<Category> categories) {
        if (categories == null) return;
        this.categories = categories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }
}







