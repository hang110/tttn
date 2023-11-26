package com.tttn.fragment_admin;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.tttn.adapter.RecycleviewAdapter_payment;
import com.tttn.model.ChamCongModel;
import com.tttn.model.UserModel;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class PaymentFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecycleviewAdapter_payment adapter;
    private ImageButton back;
    private TextView name, total_works, tele,dateTT, salary,total ;
    private Button thanhtoan, them;
    private Spinner sp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.payment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Init();
        SetUI();

//        InitListEmployee(idUser);

        thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = WorkDetailDirections.actionToHispaymentsss();
                Navigation.findNavController(view).navigate(action);
            }
        });

    }

    private void SetUI() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String sDate = ""+day+'/'+month+'/'+year;
        dateTT.setText(sDate);

        String s =  PaymentFragmentArgs.fromBundle(getArguments()).getName();
        name.setText(s);
        String t = PaymentFragmentArgs.fromBundle(getArguments()).getTotal();
        total_works.setText(t);
        String idUser = PaymentFragmentArgs.fromBundle(getArguments()).getIdU();
        float total= Float.parseFloat(t);
        DataManager.getInstance().getUser(idUser, new UserCallback() {
            @Override
            public void onSuccess(UserModel model, String id) {
                tele.setText(model.getTele());
                int luong = model.getLuong();
                float tl= total *luong;
                salary.setText(tl+"");
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = PaymentFragmentDirections.actionBack(s,t,idUser);
                Navigation.findNavController(v).navigate(action);
            }
        });
    }
//    private void InitListEmployee(String idUser){
//
//
//        adapter = new RecycleviewAdapter_payment();
//        Handler handler = new Handler();
//        DataManager.getInstance().getChamCongofUser(sDate, eDate, idUser, new ChamCongCallback() {
//            @Override
//            public void onSuccess(ChamCongModel model, String id) {
//            }
//
//            @Override
//            public void onSuccessTotal(Map<String, Float> total) {
//            }
//
//            @Override
//            public void onSuccessAll(List<ChamCongModel> model) {
//                total_day.setText(""+model.size());
//                progressBar.setVisibility(View.VISIBLE);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressBar.setVisibility(View.GONE);
//                        adapter.setList(model);
//                    }
//                }, 1000);
//                LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//                recyclerView.setLayoutManager(manager);
//                recyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//            }
//        });
//    }

    private void Init(){
        recyclerView = getView().findViewById(R.id.resDetail);
        back = getView().findViewById(R.id.backbtP);

        name = getView().findViewById(R.id.nameNV2);
        total = getView().findViewById(R.id.total);
        total_works = getView().findViewById(R.id.tongCong2);
        tele = getView().findViewById(R.id.sdtP);
        dateTT = getView().findViewById(R.id.ngayTT);
        salary = getView().findViewById(R.id.luongP);

        thanhtoan= getView().findViewById(R.id.thanhToan);
        them= getView().findViewById(R.id.addTP);
        sp = getView().findViewById(R.id.thuong);
           }
}