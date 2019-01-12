package com.example.myfirstapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

public class TableAdapter extends Adapter<TableAdapter.MyViewHolder> {
    private List<Table> tableList;
    public class MyViewHolder extends ViewHolder{
        public ImageView icon;
        public TextView name, vicinity;
        public MyViewHolder(View view){
            super(view);
            icon = (ImageView) view.findViewById(R.id.icon);
            name = (TextView) view.findViewById(R.id.name);
            vicinity = (TextView) view.findViewById(R.id.vicinity);
        }
    }

    public TableAdapter(List<Table> tableList){
        this.tableList = tableList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_list, parent,false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Table table = tableList.get(position);
        Picasso.get().load(table.getIcon()).into(holder.icon);
        holder.name.setText(table.getName());
        holder.vicinity.setText(table.getVicinity());
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailintent = new Intent(v.getContext(), DetailsActivity.class);
                detailintent.putExtra("placeid",table.getPlaceid());
                v.getContext().startActivity(detailintent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return tableList.size();
    }
}
