package com.tttn.fragment_admin;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.tttn.R;
public class home_admin extends Fragment {
    private static final int pic_id = 123;
    private ImageButton qlCong, qlLichlam, exit;
    OnBackPressedCallback callback;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        qlCong= view.findViewById(R.id.qlCong);
        qlLichlam= view.findViewById(R.id.qlLich);
        exit = view.findViewById(R.id.Exit);
        qlCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        qlLichlam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = home_adminDirections.actionManagerSchedule();
                Navigation.findNavController(view).navigate(action);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = home_adminDirections.actionBack();
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        callback.remove();
    }
}
