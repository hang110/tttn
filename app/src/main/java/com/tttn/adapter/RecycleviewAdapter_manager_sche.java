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

public class RecycleviewAdapter_manager_sche extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_EMPTY = 0;

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

    public UserModel getUser(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_EMPTY) {
            View view = inflater.inflate(R.layout.no_data, parent, false);
            return new EmptyViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_manager_schedule, parent, false);
            return new HomeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HomeViewHolder) {
            UserModel userModel = list.get(position);
            HomeViewHolder viewHolder = (HomeViewHolder) holder;
            viewHolder.name.setText(userModel.getName());
        }
        // You can add an else statement here if you have other view types
    }

    @Override
    public int getItemCount() {
        return list.isEmpty() ? 1 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.isEmpty() ? VIEW_TYPE_EMPTY : VIEW_TYPE_ITEM;
    }

    // ViewHolder for data
    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.nameUser);
        }
    }

    // ViewHolder for empty view
    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
