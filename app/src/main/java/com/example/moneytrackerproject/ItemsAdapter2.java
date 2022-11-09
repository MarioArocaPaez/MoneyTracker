package com.example.moneytrackerproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsAdapter2 extends RecyclerView.Adapter<ItemsAdapter2.ViewHolder>{

    private Context context;
    private List<Data> dataList;
    Map<String,Integer> res;
    private String item;
    private String percentage;
    public List<String> myList;

    public ItemsAdapter2(Context context, List<Data> dataList, Map<String,Integer> res) {
        this.context = context;
        this.dataList = dataList;
        this.res = res;

        myList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ItemsAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.percentage_layout, parent, false);
        return new ItemsAdapter2.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter2.ViewHolder holder, int position) {

        Data data = dataList.get(position);
        if (myList.contains(data.getItem()) == false) {

            holder.itemName.setText(data.getItem());
            holder.tvItem.setText(res.get(data.getItem()) + "%");
            myList.add(data.getItem());
        }
    }

    @Override
    public int getItemCount() {
        return res.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemName;
        public TextView tvItem;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            tvItem = itemView.findViewById(R.id.tvItem);


        }
    }
}
