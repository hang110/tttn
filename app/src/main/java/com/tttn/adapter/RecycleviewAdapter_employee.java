package com.tttn.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.tttn.R;
import com.tttn.fragment_admin.ManagerEmployeeFragment;
import com.tttn.fragment_admin.ManagerEmployeeFragmentDirections;
import com.tttn.fragment_admin.home_adminDirections;
import com.tttn.model.UserModel;
import java.util.ArrayList;
import java.util.List;

public class RecycleviewAdapter_employee extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_EMPTY = 0;

    private List<UserModel> list;
    private List<String> id;

    public RecycleviewAdapter_employee() {
        list = new ArrayList<>();
        id = new ArrayList<>();
    }

    public List<UserModel> getList() {
        return list;
    }
    public List<String> getListID() {
        return id;
    }

    public void setList(List<UserModel> list, List<String> id) {
        this.list = list;
        this.id = id;
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
            return new RecycleviewAdapter_employee.EmptyViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_employee, parent, false);
            return new RecycleviewAdapter_employee.HomeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecycleviewAdapter_employee.HomeViewHolder) {
            UserModel userModel = list.get(position);
            RecycleviewAdapter_employee.HomeViewHolder viewHolder = (RecycleviewAdapter_employee.HomeViewHolder) holder;
            viewHolder.name.setText(userModel.getName());
            viewHolder.tele.setText(userModel.getTele());
            viewHolder.diachi.setText(userModel.getAddress());
            viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int clickedPosition = holder.getAdapterPosition();
                    String idU = id.get(clickedPosition);
                    NavDirections action = ManagerEmployeeFragmentDirections.actionToEditemployee(idU);
                    Navigation.findNavController(view).navigate(action);
                }
            });
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
        private TextView name, tele, diachi;
        private ImageButton edit ;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.nameU);
            edit = view.findViewById(R.id.Edit);
            tele = view.findViewById(R.id.teleU);
            diachi =view.findViewById(R.id.addressU);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
