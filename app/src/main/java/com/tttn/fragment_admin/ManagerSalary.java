package com.tttn.fragment_admin;

import android.os.Bundle;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.firebase.database.DatabaseReference;
import com.tttn.ChamCongCallback;
import com.tttn.DataManager;
import com.tttn.R;
import com.tttn.UserCallback;
import com.tttn.model.ChamCongModel;
import com.tttn.model.UserModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;


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
        table.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(), header));
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String sDate = ""+year+'/'+month+'/'+"01";
        String eDate= endDate(month, year);
        int[] index = {0};

        DataManager.getInstance().GetChamCongtheothang(sDate, eDate, new ChamCongCallback() {
            @Override
            public void onSuccess(ChamCongModel model, String id) {
            }
            @Override
            public void onSuccessAll(List<ChamCongModel> model){}
            @Override
            public void onSuccessTotal(Map<String, Float> total) {
                String[][] data = new String[total.size()][2];
                List<String> idUser = new ArrayList<>();
                for (Map.Entry<String, Float> entry : total.entrySet()) {
                    DataManager.getInstance().getUser(entry.getKey(), new UserCallback() {
                        @Override
                        public void onSuccess(UserModel model, String id) {
                            data[index[0]][0] = model.getName();
                            data[index[0]][1] = String.valueOf(entry.getValue());
                            idUser.add(entry.getKey());
                            index[0]++;
                            table.setDataAdapter(new SimpleTableDataAdapter(getContext(), data));
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

                table.addDataClickListener(new TableDataClickListener<Object[]>() {
                    @Override
                    public void onDataClicked(int rowIndex, Object[] clickedData) {
                        String name = (String) clickedData[0];
                        String total = (String) clickedData[1];
                        String idU= idUser.get(rowIndex);
                        NavDirections action = ManagerSalaryDirections.actionSeeDetail(name, total, idU);
                        Navigation.findNavController(view).navigate(action);
                    }
                });
            }
            @Override
            public void onFailure(Exception e) {
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = ManagerSalaryDirections.actionBack();
                Navigation.findNavController(view).navigate(action);
            }
        });

    }

    void init() {
        table = getView().findViewById(R.id.table_data_view);
        back = getView().findViewById(R.id.backbtMS);
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
