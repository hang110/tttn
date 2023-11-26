package com.tttn.fragment_user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.tttn.ChamCongCallback;
import com.tttn.DangkyCallback;
import com.tttn.DataManager;
import com.tttn.LichlamCallback;
import com.tttn.R;
import com.tttn.model.ChamCongModel;
import com.tttn.model.LichLamModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateFragment extends Fragment {
    private CalendarView calendar;
    private RadioButton ca1, ca2, ca3;
    private RadioGroup group;
    private Button save;
    private ImageButton back;
    private   boolean isCheckDone;
    private LichLamModel _model;
    private String idUser;
    private Calendar startDate, endDate,selectDate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_schedu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = CreateFragmentDirections.actionCheckinBackto();
                Navigation.findNavController(view).navigate(action);
            }
        });

        Calendar calendars = Calendar.getInstance();
        int year = calendars.get(Calendar.YEAR);
        int month = calendars.get(Calendar.MONTH);
        int day = calendars.get(Calendar.DAY_OF_MONTH);
        String ngay = ""+year+'/'+(month+1)+'/'+day;
        Calendar nowDate = Calendar.getInstance();
        nowDate.set(year, month, day);

        startDate = (Calendar) nowDate.clone();
        startDate.add(Calendar.DAY_OF_MONTH, 2);
        endDate = (Calendar) nowDate.clone();
        endDate.add(Calendar.DAY_OF_MONTH, 9);

        register1(ngay);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                group.removeAllViews();
                addRadioButtonsForSelectedDate();
                String day = "";
                day = dayOfMonth + "";
                if(dayOfMonth < 10){
                    day = "0"+day;
                }
                String date = ""+ year +'/'+(month+1)+'/'+day;
                selectDate = Calendar.getInstance();
                selectDate.set(year, month, dayOfMonth);
                System.out.println(selectDate+" "+startDate+" "+endDate);
                register(date);
            }
        });
    }


    private void register(String date) {
        idUser = DataManager.getInstance().idUser;
        _model = new LichLamModel(date,0,idUser );
        System.out.println(date);

        DataManager.getInstance().getMotLichlam(date, new DangkyCallback() {
            @Override
            public void onSuccess(LichLamModel model, String id) {
                    if(model.getIdUser().isEmpty()){
                        if(selectDate.before(startDate) ||selectDate.after(endDate))
                        {
                            save.setEnabled(false);
                            ca1.setEnabled(false);
                            ca2.setEnabled(false);
                            ca3.setEnabled(false);
                            ca1.setChecked(false);
                            ca2.setChecked(false);
                            ca3.setChecked(false);
                        }else {
                            setEnable();
                            save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ca1.isChecked()) {
                                        checkRegister(date, 1);
                                        System.out.println(isCheckDone);
                                    } else if (ca2.isChecked()) {
                                        checkRegister(date, 2);
                                        System.out.println(isCheckDone);
                                    } else if (ca3.isChecked()) {
                                        checkRegister(date, 3);
                                        System.out.println(isCheckDone);
                                    } else
                                        Toast.makeText(requireContext(), "Vui lòng chọn ca làm việc", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }}else {
                    save.setEnabled(false);
                    if(model.getCaID()==1) ca1.setChecked(true);
                    if(model.getCaID()==2) ca2.setChecked(true);
                    if(model.getCaID()==3) ca3.setChecked(true);
                    ca1.setEnabled(false);
                    ca2.setEnabled(false);
                    ca3.setEnabled(false);
                }
            }
            @Override
            public void onFailure(Exception e) {
            }
        });
    }
    private void register1(String date) {
        System.out.println(date);
        DataManager.getInstance().getMotLichlam(date, new DangkyCallback() {
            @Override
            public void onSuccess(LichLamModel model, String id) {
                if(model.getIdUser().isEmpty()){
                    save.setEnabled(false);
                    ca1.setEnabled(false);
                    ca2.setEnabled(false);
                    ca3.setEnabled(false);
                    }else {
                    save.setEnabled(false);
                    if(model.getCaID()==1) ca1.setChecked(true);
                    if(model.getCaID()==2) ca2.setChecked(true);
                    if(model.getCaID()==3) ca3.setChecked(true);
                    ca1.setEnabled(false);
                    ca2.setEnabled(false);
                    ca3.setEnabled(false);
                }}
            @Override
            public void onFailure(Exception e) {
            }
        });
    }

    private void setEnable() {
        save.setEnabled(true);
        ca1.setEnabled(true);
        ca2.setEnabled(true);
        ca3.setEnabled(true);
        ca1.setChecked(false);
        ca2.setChecked(false);
        ca3.setChecked(false);
    }

    private void saveRegister(String date, int i) {
        if(isCheckDone)
        {
            _model.setCaID(i);
            _model.setIdUser(idUser);
            _model.setWorkday(date);
            DataManager.getInstance().putDangky(_model);
            Toast.makeText(requireContext(), "Đăng ký ca làm việc thành công",Toast.LENGTH_SHORT).show();
            save.setEnabled(false);
            ca1.setEnabled(false);
            ca2.setEnabled(false);
            ca3.setEnabled(false);
        }
        else
            Toast.makeText(requireContext(), "Ca làm đã đủ nhân viên. Vui lòng chọn ca khác.",Toast.LENGTH_SHORT).show();
    }


    private void checkRegister(String date, int caID) {
        isCheckDone = false;
        DataManager.getInstance().checkDangky(date, caID, new LichlamCallback() {
            @Override
            public void onSuccess(List<LichLamModel> model) {
                if(model.size() < 2){
                    isCheckDone = true;
                }else{
                    isCheckDone = false;
                }
                saveRegister(date, caID);
            }
            @Override
            public void onSuccessGetAll(List<LichLamModel> model, List<String> id) {
            }
            @Override
            public void onFailure(Exception e) {
            }
        });
    }

    private void addRadioButtonsForSelectedDate() {
        addRadioButton(ca1, "Ca Sáng ( 8h - 13h )");
        addRadioButton(ca2, "Ca Chiều ( 13h - 18h )");
        addRadioButton(ca3, "Ca Tối ( 18h - 23h )");
    }

    private void addRadioButton(RadioButton id, String text) {
        id.setId(View.generateViewId());
        id.setText(text);
        group.addView(id);
    }

    void init() {
        calendar = getView().findViewById(R.id.calendar);
        ca1 = getView().findViewById(R.id.radioMorning);
        ca2 = getView().findViewById(R.id.radioAfternoon);
        ca3 = getView().findViewById(R.id.radioEvening);
        save = getView().findViewById(R.id.save_button);
        back = getView().findViewById(R.id.backbtCre);
        group = getView().findViewById(R.id.radioGroup);
    }

}
