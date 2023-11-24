package com.tttn.fragment_admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.tttn.R;

import de.codecrafters.tableview.TableView;

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
