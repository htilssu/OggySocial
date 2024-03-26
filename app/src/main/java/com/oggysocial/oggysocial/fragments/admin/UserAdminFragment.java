package com.oggysocial.oggysocial.fragments.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.adapters.UserFragmentAdapter;

public class UserAdminFragment extends Fragment {

    ViewPager2 viewPager;
    BottomNavigationView bottomNavigationView;

    public UserAdminFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView();
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initView() {
        viewPager = requireView().findViewById(R.id.user_pager);
        UserFragmentAdapter adapter = new UserFragmentAdapter(requireActivity());
        viewPager.setAdapter(adapter);

        bottomNavigationView = requireView().findViewById(R.id.bottom_navigation_view);

        // Set a page change callback on the ViewPager2
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // When the page changes, update the selected item in the BottomNavigationView
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });

        // Set a listener on the BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            // When a navigation item is selected, update the current item in the ViewPager2
            int itemId = item.getItemId();
            adapter.notifyDataSetChanged();
            if (itemId == R.id.nav_list_user) {
                viewPager.setCurrentItem(0);
                return true;
            } else if (itemId == R.id.nav_block_user) {
                viewPager.setCurrentItem(1);
                return true;
            }
            return false;
        });
    }
}