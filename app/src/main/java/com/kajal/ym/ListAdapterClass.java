package com.kajal.ym;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kajal.ym.R;

import java.util.List;

public class ListAdapterClass extends RecyclerView.Adapter<ListAdapterClass.MyViewHolder> {

    private Context context;
    private List<com.kajal.ym.AddList> mServices;

    public ListAdapterClass(Context context, List<com.kajal.ym.AddList> mServices) {
        this.context = context;
        this.mServices= mServices;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.service);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.addlist, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        com.kajal.ym.AddList text = mServices.get(position);

        holder.mTextView.setText(text.getService());


    }

    @Override
    public int getItemCount() {
        return mServices.size();
    }
}