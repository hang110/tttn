package com.tttn.fragment_user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.tttn.R;
public class user_home extends Fragment {
    private static final int pic_id = 123;
    private ImageButton chamcong, dklich, exit;
    OnBackPressedCallback callback;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chamcong= view.findViewById(R.id.Chamcong);
        dklich= view.findViewById(R.id.DKLich);
        exit = view.findViewById(R.id.Exit);
        chamcong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = user_homeDirections.actionMainuserToCheckinFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });
        dklich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = user_homeDirections.actionSchedule();
                Navigation.findNavController(view).navigate(action);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = user_homeDirections.actionBack();
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
