package com.kajal.ym;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {

    private Context context;
    private List<Image> image;

    public AdapterClass(Context context, List<Image> image) {
        this.context = context;
        this.image= image;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.image);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Image img = image.get(position);

        holder.mImage.setImageResource(img.getImage());


    }

    @Override
    public int getItemCount() {
        return image.size();
    }
}