package com.tttn.fragment_user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.tttn.R;
public class user_home extends Fragment {
    private static final int pic_id = 123;
    private ImageButton chamcong, dklich;
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
        String phone = CheckinFragmentArgs.fromBundle(getArguments()).getEusername();

        chamcong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = user_homeDirections.actionMainuserToCheckinFragment(phone);
                Navigation.findNavController(view).navigate(action);
            }
        });
    }
}
