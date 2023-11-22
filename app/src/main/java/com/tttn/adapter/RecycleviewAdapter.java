package com.tttn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tttn.R;
import com.tttn.model.LichLamModel;

import java.util.ArrayList;
import java.util.List;

public class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.HomeViewHolder>{

    private List<LichLamModel> list;

    public RecycleviewAdapter() {
        list = new ArrayList<>();
    }

    public List<LichLamModel> getList() {
        return list;
    }

    public void setList(List<LichLamModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public LichLamModel getLichLam(int position){ return list.get(position);}

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule,parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        LichLamModel lichLamModel = list.get(position);
        holder.date.setText(lichLamModel.getWorkday());
        if (lichLamModel.getCaID()==1)
        {
            holder.name.setText("Ca sáng");
            holder.time.setText("8:00 - 13:00");
        }else if(lichLamModel.getCaID()==2)
        {
            holder.name.setText("Ca chiều");
            holder.time.setText("13:00 - 18:00");
        }else {
            holder.name.setText("Ca tối");
            holder.time.setText("18:00 - 23:00");
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder{
        private TextView date, name, time;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            date = view.findViewById(R.id.date);
            name = view.findViewById(R.id.nameCa);
            time = view.findViewById(R.id.timeCa);

        }
    }
}
