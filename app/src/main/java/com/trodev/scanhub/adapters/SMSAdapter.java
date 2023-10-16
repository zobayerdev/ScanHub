package com.trodev.scanhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trodev.scanhub.ProductQrFullActivity;
import com.trodev.scanhub.R;
import com.trodev.scanhub.SmsQrFullActivity;
import com.trodev.scanhub.models.SMSModels;

import java.util.ArrayList;

public class SMSAdapter extends RecyclerView.Adapter<SMSAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<SMSModels> list;
    private String category;

    public SMSAdapter(Context context, ArrayList<SMSModels> list, String category) {
        this.context = context;
        this.list = list;
        this.category = category;
    }


    @NonNull
    @Override
    public SMSAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sms_qr_item, parent, false);
        return new SMSAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SMSAdapter.MyViewHolder holder, int position) {

        /*get data from database model*/
        SMSModels models = list.get(position);

        holder.from.setText(models.getFrom());
        holder.to.setText(models.getTo());
        holder.time.setText(models.getTime());
        holder.date.setText(models.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, SmsQrFullActivity.class);
                intent.putExtra("mFrom", models.getFrom());
                intent.putExtra("mTo", models.getTo());
                intent.putExtra("mText", models.getSms());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView  from, to, date, time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Here its a Time & Date Section
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);

            from = itemView.findViewById(R.id.fromTv);
            to = itemView.findViewById(R.id.toTv);

        }
    }
}
