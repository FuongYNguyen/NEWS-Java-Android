package com.example.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> news_id, news_title, news_content, news_image;

    CustomAdapter(Context context, ArrayList<String> news_id, ArrayList<String> news_title, ArrayList<String> news_content, ArrayList<String> news_image) {
        this.context = context;
        this.news_id = news_id;
        this.news_title = news_title;
        this.news_content = news_content;
        this.news_image = news_image;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.news_id_txt.setText(news_id.get(position));
        holder.news_title_txt.setText(news_title.get(position));
        holder.news_content_txt.setText(news_content.get(position));

        // Load image using Glide
        Glide.with(context).load(news_image.get(position)).into(holder.thumbnail_input_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("news_id", news_id.get(position));
                intent.putExtra("news_title", news_title.get(position));
                intent.putExtra("news_content", news_content.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news_id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail_input_img;
        TextView news_id_txt, news_title_txt, news_content_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail_input_img = itemView.findViewById(R.id.thumbnail_input_img);
            news_id_txt = itemView.findViewById(R.id.news_id_txt);
            news_title_txt = itemView.findViewById(R.id.news_title_txt);
            news_content_txt = itemView.findViewById(R.id.news_content_txt);
        }
    }
}
