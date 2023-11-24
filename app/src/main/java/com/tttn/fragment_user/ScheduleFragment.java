package com.tttn.fragment_user;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tttn.DataManager;
import com.tttn.LichlamCallback;
import com.tttn.R;
import com.tttn.adapter.RecycleviewAdapter;
import com.tttn.model.LichLamModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ScheduleFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageButton back;
    private FloatingActionButton flBtn;
    private RecycleviewAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.scheduleview, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        ProgressBar progressBar = view.findViewById(R.id.loading1);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) ;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Calendar sDate = Calendar.getInstance();
        sDate.set(year, month, day);

        Calendar eDate = (Calendar) sDate.clone();
        eDate.add(Calendar.DAY_OF_MONTH, 12);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String startDate = formatDateString(sDate, dateFormat);
        String endDate = formatDateString(eDate, dateFormat);
        System.out.println(startDate+' '+endDate);

        adapter = new RecycleviewAdapter();
        Handler handler = new Handler();

        DataManager.getInstance().getLichlam(startDate,endDate, new LichlamCallback() {
            @Override
            public void onSuccess(List<LichLamModel> model) {
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = ScheduleFragmentDirections.actionScheduleBackto();
                Navigation.findNavController(view).navigate(action);
            }
        });
        flBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = ScheduleFragmentDirections.actionCheckinFragmentToHistoryFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });
    }


    void init() {
        recyclerView= getView().findViewById(R.id.resSchedule);
        flBtn = getView().findViewById(R.id.addBtn);
        back = getView().findViewById(R.id.backbt);
    }
    private static String formatDateString(Calendar calendar, SimpleDateFormat dateFormat) {
        String formattedDate = dateFormat.format(calendar.getTime());
        if (formattedDate.startsWith("0")) {
            return formattedDate.substring(1);
        }
        return formattedDate;
    }
}
