package com.tttn.fragment_admin;

import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tttn.DataManager_M;
import com.tttn.LichlamCallback;
import com.tttn.R;
import com.tttn.UserCallback;
import com.tttn.adapter.RecycleviewAdapter_manager_sche;
import com.tttn.model.LichLamModel;
import com.tttn.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ManagerSalary extends Fragment {
    private ImageButton back;
    private TableView table;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.manager_salari, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        String[] header = {"Tên nhân viên", "Tổng công"};
        String[][] data= {{"Hang", "10.1"},{"Thuy", "20.3"}};
//        table.setHeaderAdapter(new SimpleTableHeaderAdapter(this, header));
//        table.setDataAdapter(new SimpleTableDataAdapter(this, data));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = ManagerScheduFragmentDirections.actionBack();
                Navigation.findNavController(view).navigate(action);
            }
        });

    }



    void init() {
        table = getView().findViewById(R.id.table_data_view);
        back = getView().findViewById(R.id.backbt);
    }

}
