package com.tttn.fragment_admin;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tttn.ChamCongCallback;
import com.tttn.DataManager;
import com.tttn.R;
import com.tttn.UserCallback;
import com.tttn.adapter.RecycleviewAdapter_detail;
import com.tttn.adapter.RecycleviewAdapter_employee;
import com.tttn.model.ChamCongModel;
import com.tttn.model.UserModel;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class WorkDetail extends Fragment {
    private RecyclerView recyclerView;
    private RecycleviewAdapter_detail adapter;
    private ImageButton back;
    private ProgressBar progressBar;
    private TextView name, total_works, total_day;
    private Button thanhtoan, lichsu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.work_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Init();
        String s =  WorkDetailArgs.fromBundle(getArguments()).getName();
        name.setText(s);
        String t = WorkDetailArgs.fromBundle(getArguments()).getTotal();
        total_works.setText(t);
        String idUser = WorkDetailArgs.fromBundle(getArguments()).getIdU();
        progressBar = view.findViewById(R.id.loadingDetail);
        InitListEmployee(idUser);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = WorkDetailDirections.actionBack();
                Navigation.findNavController(view).navigate(action);
            }
        });
        thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = WorkDetailDirections.actionToPayment(s,t,idUser);
                Navigation.findNavController(view).navigate(action);
            }
        });
        lichsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = WorkDetailDirections.actionToHispaymentsss();
                Navigation.findNavController(view).navigate(action);
            }
        });

    }
    private void InitListEmployee(String idUser){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String sDate = ""+year+'/'+month+'/'+"01";
        String eDate= endDate(month, year);

        adapter = new RecycleviewAdapter_detail();
        Handler handler = new Handler();
        DataManager.getInstance().getChamCongofUser(sDate, eDate, idUser, new ChamCongCallback() {
            @Override
            public void onSuccess(ChamCongModel model, String id) {
            }

            @Override
            public void onSuccessTotal(Map<String, Float> total) {
            }

            @Override
            public void onSuccessAll(List<ChamCongModel> model) {
                total_day.setText(""+model.size());
                progressBar.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        adapter.setList(model);
                    }
                }, 1000);
                LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
            }
        });
    }

    private void Init(){
        recyclerView = getView().findViewById(R.id.resDetail);
        back = getView().findViewById(R.id.backbtDetail);
        name = getView().findViewById(R.id.nameNV2);
        total_day = getView().findViewById(R.id.tongNgay);
        total_works = getView().findViewById(R.id.tongCong2);
        thanhtoan= getView().findViewById(R.id.thanhToan);
        lichsu= getView().findViewById(R.id.btnXem);
    }
    public String endDate( int month, int year) {
        String eDate="";
        switch (month) {
            case 1:
                eDate = "" + year + '/' + month  + '/' + "31";
                break;
            case 2:
                eDate = "" + year + '/' + month + '/' + "28";
                break;
            case 3:
                eDate = "" + year + '/' + month + '/' + "31";
                break;
            case 4:
                eDate = "" + year + '/' + month + '/' + "30";
                break;
            case 5:
                eDate = "" + year + '/' + month + '/' + "31";
                break;
            case 6:
                eDate = "" + year + '/' + month + '/' + "30";
                break;
            case 7:
                eDate = "" + year + '/' + month+ '/' + "31";
                break;
            case 8:
                eDate = "" + year + '/' +month + '/' + "31";
                break;
            case 9:
                eDate = "" + year + '/' + month + '/' + "30";
                break;
            case 10:
                eDate = "" + year + '/' + month + '/' + "31";
                break;
            case 11:
                eDate = "" + year + '/' + month + '/' + "30";
                break;
            case 12:
                eDate = "" + year + '/' + month + '/' + "31";
                break;

        }
        return eDate;
    }
}


