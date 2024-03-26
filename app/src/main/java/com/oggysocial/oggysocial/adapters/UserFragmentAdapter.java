package com.oggysocial.oggysocial.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.oggysocial.oggysocial.fragments.admin.BlockedUserFragment;
import com.oggysocial.oggysocial.fragments.admin.UserFragment;

public class UserFragmentAdapter extends FragmentStateAdapter {

    public UserFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return switch (position) {
            case 0 -> new UserFragment();
            case 1 -> new BlockedUserFragment();
            default -> null;
        };

    }
    

    @Override
    public int getItemCount() {
        return 2;
    }
}
