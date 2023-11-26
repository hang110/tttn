package com.tttn.fragment_admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.tttn.DataManager;
import com.tttn.LichlamCallback;
import com.tttn.R;
import com.tttn.UserCallback;
import com.tttn.adapter.RecycleviewAdapter_manager_sche;
import com.tttn.model.LichLamModel;
import com.tttn.model.UserModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ManagerScheduFragment extends Fragment {
    private RecyclerView reCa1, reCa2, reCa3;
    private ImageButton back, add1, add2, add3;
    private RecycleviewAdapter_manager_sche adapter1, adapter2, adapter3;
    private CalendarView calendarView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.manager_schedule, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        progressBar = view.findViewById(R.id.loading2);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) ;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String ngay = ""+year+'/'+(month+1)+'/'+day;
        add1.setEnabled(false);
        add2.setEnabled(false);
        add3.setEnabled(false);
        show(ngay);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = ""+ year +'/'+(month+1)+'/'+dayOfMonth;
                show(date);
                if(date.equals(ngay)) {
                    add1.setEnabled(false);
                    add2.setEnabled(false);
                    add3.setEnabled(false);
                }else{
                    add1.setEnabled(true);
                    add2.setEnabled(true);
                    add3.setEnabled(true);
                    add1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            add(v,1,date);
                        }
                        });
                    add2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            add(v,2,date);
                        }
                    });
                    add3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            add(v,3,date);
                        }
                    });
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = ManagerScheduFragmentDirections.actionBack();
                Navigation.findNavController(view).navigate(action);
            }
        });

    }

    public List<UserModel> getUser(List<LichLamModel> models)
    {
        List<UserModel> user = new ArrayList<>();

        for(int i = 0; i< models.size(); i++){

            String id = models.get(i).getIdUser();
            DataManager.getInstance().getUser(id, new UserCallback() {
                @Override
                public void onSuccess(UserModel model, String id) {
                    user.add(model);
                }
                @Override
                public void onFailure(Exception e) {
                }
                @Override
                public void onSuccessGetList(List<UserModel> models, List<String> id) {
                }
                @Override
                public void onSuccessGetName(List<String> name, List<String> id) {
                }
            });
        }
        return user;
    }

    public void show(String date){
        adapter1 = new RecycleviewAdapter_manager_sche();
        adapter2 = new RecycleviewAdapter_manager_sche();
        adapter3 = new RecycleviewAdapter_manager_sche();
        Handler handler = new Handler();
        List<LichLamModel> ca1 = new ArrayList<>();
        List<LichLamModel> ca2 = new ArrayList<>();
        List<LichLamModel> ca3 = new ArrayList<>();
        List<String> idCa1 = new ArrayList<>();
        List<String> idCa2 = new ArrayList<>();
        List<String> idCa3 = new ArrayList<>();
        DataManager.getInstance().getAllLichlam(date, new LichlamCallback() {
            @Override
            public void onSuccess(List<LichLamModel> model) {
            }

            @Override
            public void onFailure(Exception e) {
            }
            @Override
            public void onSuccessGetAll(List<LichLamModel> model,List<String> id) {

                for(int i =0 ;i < model.size(); i++){
                    if(model.get(i).getCaID() == 1){
                        ca1.add(model.get(i));
                        idCa1.add(id.get(i));
                    }
                    else{
                        if(model.get(i).getCaID() == 2){
                            ca2.add(model.get(i));
                            idCa2.add(id.get(i));
                        }else{
                            ca3.add(model.get(i));
                            idCa3.add(id.get(i));
                        }
                    }
                }
                List<UserModel> user1 = getUser(ca1);
                List<UserModel> user2 = getUser(ca2);
                List<UserModel> user3 = getUser(ca3);
                progressBar.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        adapter1.setList(user1, idCa1, ca1);
                        adapter2.setList(user2,idCa2, ca2);
                        adapter3.setList(user3,idCa3,ca3);
                    }
                }, 2000);
                LinearLayoutManager manager1 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                LinearLayoutManager manager2 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                LinearLayoutManager manager3 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                reCa1.setLayoutManager(manager1);
                reCa1.setAdapter(adapter1);
                reCa2.setLayoutManager(manager2);
                reCa2.setAdapter(adapter2);
                reCa3.setLayoutManager(manager3);
                reCa3.setAdapter(adapter3);
            }
        });
    }

    void init() {
        reCa1= getView().findViewById(R.id.reCa1);
        reCa2= getView().findViewById(R.id.reCa2);
        reCa3= getView().findViewById(R.id.reCa3);
        back = getView().findViewById(R.id.backbtMS);
        add1 = getView().findViewById(R.id.addCa1);
        add2 = getView().findViewById(R.id.addCa2);
        add3 = getView().findViewById(R.id.addCa3);
        calendarView = getView().findViewById(R.id.calendarViewM);
    }

    public void add(View v,int ca, String date ){
        LichLamModel lichLamModel = new LichLamModel();
        lichLamModel.setCaID(ca);
        lichLamModel.setWorkday(date);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.popup_update_schedule, null);
        Spinner spinner = dialogView.findViewById(R.id.sp1);
        DataManager.getInstance().GetAllEmployee(new UserCallback() {
            @Override
            public void onSuccess(UserModel model, String id) {
            }
            @Override
            public void onFailure(Exception e) {
            }
            @Override
            public void onSuccessGetList(List<UserModel> models, List<String> id) {
            }
            @Override
            public void onSuccessGetName(List<String> uName, List<String> id) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, uName);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setView(dialogView)
                        .setTitle("Thêm nhân viên làm việc")
                        .setPositiveButton("Save", (dialog, which) -> {
                            lichLamModel.setIdUser(id.get(spinner.getSelectedItemPosition()));
                            DataManager.getInstance().putDangky(lichLamModel);
                            show(date);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                System.out.println("show");
            }
        });
        
    }
}
