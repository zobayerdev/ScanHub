package com.trodev.scanhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trodev.scanhub.R;
import com.trodev.scanhub.models.WIFIMModels;

import java.util.ArrayList;

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<WIFIMModels> list;
    private String category;

    public WifiAdapter(Context context, ArrayList<WIFIMModels> list, String category) {
        this.context = context;
        this.list = list;
        this.category = category;
    }

    @NonNull
    @Override
    public WifiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wifi_qr_item, parent, false);
        return new WifiAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WifiAdapter.MyViewHolder holder, int position) {

        WIFIMModels models = list.get(position);

        /*set data on views*/
        holder.name.setText(models.getNet_name());
        holder.pass.setText(models.getPass());
        holder.time.setText(models.getTime());
        holder.date.setText(models.getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, pass, date, time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTv);
            pass = itemView.findViewById(R.id.passTv);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
        }
    }
}
