package com.tttn.fragment_admin;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tttn.DataManager;
import com.tttn.R;
import com.tttn.UserCallback;
import com.tttn.adapter.RecycleviewAdapter;
import com.tttn.adapter.RecycleviewAdapter_employee;
import com.tttn.model.UserModel;

import java.util.List;

public class ManagerEmployeeFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecycleviewAdapter_employee adapter;
    private ImageButton back;
    private  ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.manager_employee, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Init();
        progressBar = view.findViewById(R.id.loadingME);
        InitListEmployee();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = ManagerEmployeeFragmentDirections.actionBack();
                Navigation.findNavController(view).navigate(action);
            }
        });

    }
    private void InitListEmployee(){
        adapter = new RecycleviewAdapter_employee();
        Handler handler = new Handler();
        DataManager.getInstance().GetAllEmployee(new UserCallback() {
            @Override
            public void onSuccess(UserModel model, String id) {
            }
            @Override
            public void onFailure(Exception e) {
            }
            @Override
            public void onSuccessGetName(List<String> name, List<String> id) {
            }
            @Override
            public void onSuccessGetList(List<UserModel> models, List<String> id) {

                progressBar.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        adapter.setList(models, id);
                    }
                }, 2000);
                LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
            }
        });
    }
    private void Init(){
        recyclerView = getView().findViewById(R.id.resSchedule);
        back = getView().findViewById(R.id.backbtME);
    }
}
