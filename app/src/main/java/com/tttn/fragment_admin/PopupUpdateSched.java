package com.tttn.fragment_admin;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tttn.R;
import com.tttn.model.UserModel;

import java.util.List;

public class tPopupUpdateSched extends DialogFragment {

    private List<UserModel> employeeList;
    private SpinnerAdapter adapter;

    public PopupUpdateSched(List<UserModel> employeeList, SpinnerAdapter adapter) {
        this.employeeList = employeeList;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_update_schedule, null);

        Spinner sp1 = view.findViewById(R.id.sp1);
        Spinner sp2 = view.findViewById(R.id.sp2);
        ArrayAdapter<UserModel> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, employeeList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(arrayAdapter);

        builder.setView(view)
                .setTitle("Chỉnh sửa danh sách nhân viên")
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lưu danh sách nhân viên đã chỉnh sửa
                        //updateEmployeeList(spinner);
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    private void updateEmployeeList(Spinner spinner) {
        int selectedIndex = spinner.getSelectedItemPosition();

        // Cập nhật adapter với danh sách nhân viên mới
        //adapter.setList(employeeList);

        // Lấy dữ liệu từ Spinner nếu cần
        if (selectedIndex != Spinner.INVALID_POSITION) {
            UserModel selectedUser = employeeList.get(selectedIndex);
            // Thực hiện xử lý với selectedUser nếu cần
        }
    }
}