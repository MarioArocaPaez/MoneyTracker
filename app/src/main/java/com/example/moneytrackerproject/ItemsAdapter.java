package com.example.moneytrackerproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    private Context context;
    private List<Data> dataList;

    public ItemsAdapter(Context context, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.retrieve_layout, parent, false);
        return new ItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Data data = dataList.get(position);
        //holder.item.setText("Item: " + data.getItem());
        holder.quantity.setText("Spent: " + data.getQuantity());
        holder.date.setText("Date: " + data.getDate());
        holder.notes.setText("Notes: " + data.getNotes());

        //TODO: Add operation to add images of each item
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item;
        public TextView quantity;
        public TextView date;
        public TextView notes;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            item = itemView.findViewById(R.id.item);
            quantity = itemView.findViewById(R.id.quantity);
            date = itemView.findViewById(R.id.date);
            notes = itemView.findViewById(R.id.note);
        }
    }
}
