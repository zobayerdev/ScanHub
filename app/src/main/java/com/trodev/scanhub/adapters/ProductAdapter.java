package com.trodev.scanhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.trodev.scanhub.detail_activity.ProductQrFullActivity;
import com.trodev.scanhub.models.QRModels;
import com.trodev.scanhub.R;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<QRModels> list;
    private String category;

    public ProductAdapter(Context context, ArrayList<QRModels> list, String category) {
        this.context = context;
        this.list = list;
        this.category = category;
    }

    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_qr_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, int position) {

        QRModels models = list.get(position);

        holder.name.setText(models.getProduct_name());
        holder.info.setText(models.getProduct_info());
        holder.date.setText(models.getDate());
        holder.time.setText(models.getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductQrFullActivity.class);
                intent.putExtra("mDate", models.getMake_date());
                intent.putExtra("eDate", models.getExpire_date());
                intent.putExtra("pName", models.getProduct_name());
                intent.putExtra("code", models.getProduct_info());
                intent.putExtra("company", models.getCompany_name());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, info, date, time;
        private CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            info = itemView.findViewById(R.id.info);
            // Here its a Time & Date Section
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}
