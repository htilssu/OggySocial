package com.oggysocial.oggysocial.fragments.main;

import android.annotation.SuppressLint;
import android.app.Service;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;

import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.adapters.UserAdapter;
import com.oggysocial.oggysocial.adapters.UserAdapterType;
import com.oggysocial.oggysocial.models.User;
import com.oggysocial.oggysocial.services.UserService;

import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {

    List<User> userList;
    EditText etSearch;
    String searchQuery;
    ImageView ivBack;
    RecyclerView rvSearchResults;
    UserAdapter userAdapter;

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Slide slide = new Slide();
        slide.setDuration(200);
        slide.setSlideEdge(Gravity.END);
        setEnterTransition(slide);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        etSearch = requireView().findViewById(R.id.etSearch);
        ivBack = requireView().findViewById(R.id.ivBack);
        rvSearchResults = requireView().findViewById(R.id.rvSearchResult);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void initListener() {
        //Search input
        etSearch.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                //Hide keyboard
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                searchQuery = Objects.requireNonNull(etSearch.getText()).toString();
                //Not focus on the search bar
                etSearch.clearFocus();
                return true;
            }
            return false;
        });

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do nothing
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void afterTextChanged(Editable s) {
                searchQuery = s.toString();
                UserService.getUserByName(searchQuery, users -> {
                    userList = users;
                    userAdapter.setUserList(userList);
                    userAdapter.notifyDataSetChanged();
                });
            }
        });

        ivBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initData() {
        userAdapter = new UserAdapter(userList, UserAdapterType.SEARCH);
        userAdapter.setOnUserClickListener(user -> {
            ProfileFragment profileFragment = new ProfileFragment(user, true);
            getParentFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.fragmentContainerView, profileFragment).commit();
        });
        rvSearchResults.setAdapter(userAdapter);

    }

}