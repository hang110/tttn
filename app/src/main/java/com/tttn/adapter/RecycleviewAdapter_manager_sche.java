package com.tttn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tttn.R;
import com.tttn.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class RecycleviewAdapter_manager_sche extends RecyclerView.Adapter<RecycleviewAdapter_manager_sche.HomeViewHolder>{

private List<UserModel> list;

public RecycleviewAdapter_manager_sche() {
        list = new ArrayList<>();
        }

public List<UserModel> getList() {
        return list;
        }

public void setList(List<UserModel> list) {
        this.list = list;
        notifyDataSetChanged();
        }
public UserModel getUser(int position){ return list.get(position);}

@NonNull
@Override
public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manager_schedule,parent, false);
        return new HomeViewHolder(view);
}

@Override
public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
    UserModel userModel = list.get(position);
        holder.name.setText(userModel.getName());

}


@Override
public int getItemCount() {
        return list.size();
        }

public class HomeViewHolder extends RecyclerView.ViewHolder{
    private TextView name;

    public HomeViewHolder(@NonNull View view) {
        super(view);
        name = view.findViewById(R.id.nameUser);


    }
}
}
