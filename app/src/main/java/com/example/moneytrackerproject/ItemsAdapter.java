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

        Data data = dataList.get(position);
        holder.item.setText("Item: " + data.getItem());
        holder.quantity.setText("Spent: " + data.getQuantity());
        holder.date.setText("Date: " + data.getDate());
        holder.notes.setText("Notes: " + data.getNotes());

        switch (data.getItem()){
            case "Car":
                holder.imageView.setImageResource(R.drawable.car);
                break;
            case "House":
                holder.imageView.setImageResource(R.drawable.home);
                break;
            case "Food":
                holder.imageView.setImageResource(R.drawable.food);
                break;
            case "Communication":
                holder.imageView.setImageResource(R.drawable.communication);
                break;
            case "Sport":
                holder.imageView.setImageResource(R.drawable.sport);
                break;
            case "Entertainment":
                holder.imageView.setImageResource(R.drawable.entertainment);
                break;
            case "Bills":
                holder.imageView.setImageResource(R.drawable.bills);
                break;
            case "Hygiene":
                holder.imageView.setImageResource(R.drawable.hygiene);
                break;
            case "Pets":
                holder.imageView.setImageResource(R.drawable.pets);
                break;
            case "Presents":
                holder.imageView.setImageResource(R.drawable.presents);
                break;
            case "Restaurants":
                holder.imageView.setImageResource(R.drawable.restaurants);
                break;
            case "Clothes":
                holder.imageView.setImageResource(R.drawable.clothes);
                break;
            case "Health":
                holder.imageView.setImageResource(R.drawable.health);
                break;
            case "Taxi":
                holder.imageView.setImageResource(R.drawable.taxi);
                break;
            case "Transportation":
                holder.imageView.setImageResource(R.drawable.transportation);
                break;
            case "Other":
                holder.imageView.setImageResource(R.drawable.others);
                break;
        }
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
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
