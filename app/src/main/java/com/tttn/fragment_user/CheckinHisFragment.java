package com.tttn.fragment_user;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.tttn.ChamCongCallback;
import com.tttn.DataManager;
import com.tttn.R;
import com.tttn.model.ChamCongModel;

import java.util.Calendar;

public class CheckinHisFragment extends Fragment {
    private CalendarView calendarView;
    private TextView TimeCI, TimeCO;
    private ImageButton back;
    OnBackPressedCallback callback;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) ;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        GetValueChamCong(year, month, day);
        return inflater.inflate(R.layout.checkin_history, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavDirections action = CheckinFragmentDirections.actionCheckinBackto();
                Navigation.findNavController(requireView()).navigate(action);
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        callback.remove();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                GetValueChamCong(year, month, dayOfMonth);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = CheckinFragmentDirections.actionCheckinBackto();
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    private void GetValueChamCong(int year, int month, int dayOfMonth) {
        String ngay = dayOfMonth + "/" + (month+1) + "/" + year;
        DataManager.getInstance().DebugCustom(ngay);
        DataManager.getInstance().getChamCong(DataManager.getInstance().idUser, ngay, new ChamCongCallback() {
            @Override
            public void onSuccess(ChamCongModel model, String id) {
                if(model.getIdUser().isEmpty()){
                    TimeCI.setText("không có dữ liệu");
                    TimeCO.setText("Không có dữ liệu");
                }else {
                    TimeCI.setText(model.getTime_CI());
                    if(model.getTime_CO().isEmpty()){
                        TimeCO.setText("Không có dữ liệu");
                    }else{
                        TimeCO.setText(model.getTime_CO());
                    }
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
    }

   void init() {
        calendarView= getView().findViewById(R.id.calendarView);
        TimeCI = getView().findViewById(R.id.timeCI);
        TimeCO = getView().findViewById(R.id.timeCO);
        back = getView().findViewById(R.id.backbt);
    }

}