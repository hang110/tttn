package com.tttn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tttn.R;
import com.tttn.model.ChamCongModel;

import java.util.ArrayList;
import java.util.List;

public class RecycleviewAdapter_payment extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_EMPTY = 0;

    private List<ChamCongModel> list;

    public RecycleviewAdapter_payment() {
        list = new ArrayList<>();
    }

    public List<ChamCongModel> getList() {
        return list;
    }

    public void setList(List<ChamCongModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ChamCongModel getChamcong(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_EMPTY) {
            View view = inflater.inflate(R.layout.no_data, parent, false);
            return new RecycleviewAdapter_payment.EmptyViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_detail, parent, false);
            return new RecycleviewAdapter_payment.HomeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecycleviewAdapter_payment.HomeViewHolder) {
            ChamCongModel model = list.get(position);
            RecycleviewAdapter_payment.HomeViewHolder viewHolder = (RecycleviewAdapter_payment.HomeViewHolder) holder;
            viewHolder.date.setText(model.getDate());
            viewHolder.timeci.setText(model.getTime_CI());
            if(model.getTime_CO().isEmpty() )
            {
                viewHolder.timeco.setText("No data");}
            else{
                viewHolder.timeco.setText(model.getTime_CO());
            }
        }
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
        private TextView date, timeci, timeco;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            date = view.findViewById(R.id.date_work);
            timeci = view.findViewById(R.id.time_CI);
            timeco = view.findViewById(R.id.time_CO);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
