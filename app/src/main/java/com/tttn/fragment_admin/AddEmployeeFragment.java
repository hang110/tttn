package com.tttn.fragment_admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.tttn.DataManager;
import com.tttn.R;
import com.tttn.UserCallback;
import com.tttn.model.UserModel;

import java.util.List;

public class AddEmployeeFragment extends Fragment {
    private EditText ten, sdt, dchi, lcb, email;
    private ImageButton back;
    private Button luu;
    private String idUser="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_employee, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Init();
        String id = EditEmployeeFragmentArgs.fromBundle(getArguments()).getId();
        System.out.println(id);
        DataManager.getInstance().getUserbyID(id, new UserCallback() {
            @Override
            public void onSuccess(UserModel model, String id) {
                System.out.println(model.getName());
                ten.setText(model.getName());
                sdt.setText(model.getTele());
                dchi.setText(model.getAddress());
                lcb.setText(model.getLuong()+"");
                idUser = model.getIdUser();
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
        luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, address, tele;
                int luong=0;
                UserModel user = new UserModel();
                user.setIdUser(idUser);
                name=ten.getText().toString();
                address =dchi.getText().toString();
                tele= sdt.getText().toString();
                if (!lcb.getText().toString().isEmpty()) {
                    try {
                        luong = Integer.parseInt(lcb.getText().toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    luong =0;
                }
                if(name.isEmpty()||address.isEmpty()||tele.isEmpty()||luong==0)
                {
                    Toast.makeText(getContext(), "Fill in all fields.", Toast.LENGTH_LONG).show();
                }else{
                    user.setName(name);
                    user.setAddress(address);
                    user.setTele(tele);
                    user.setLuong(luong);
                    DataManager.getInstance().updateUser(id,user,getContext());
                }

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = EditEmployeeFragmentDirections.actionBack();
                Navigation.findNavController(view).navigate(action);
            }
        });

    }

    private void Init(){
        ten = getView().findViewById(R.id.addnameNV);
        sdt = getView().findViewById(R.id.addSDT);
        dchi = getView().findViewById(R.id.adddiachi);
        email = getView().findViewById(R.id.addEmail);
        lcb = getView().findViewById(R.id.addluong);
        back = getView().findViewById(R.id.backbtAdd);
        luu = getView().findViewById(R.id.btnAdd);
    }
}

