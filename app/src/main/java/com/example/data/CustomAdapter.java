package com.example.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> news_id, news_title, news_content;

    CustomAdapter(Context context, ArrayList news_id, ArrayList news_title, ArrayList news_content){
        this.context        =context;
        this.news_id        =news_id;
        this.news_title     =news_title;
        this.news_content   =news_content;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view               = inflater.inflate(R.layout.my_row, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.news_id_txt.setText(String.valueOf(news_id.get(position)));
        holder.news_title_txt.setText(String.valueOf(news_title.get(position)));
        holder.news_content_txt.setText(String.valueOf(news_content.get(position)));
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView news_id_txt, news_title_txt, news_content_txt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            news_id_txt=itemView.findViewById(R.id.news_id_txt);
            news_title_txt=itemView.findViewById(R.id.news_title_txt);
            news_content_txt=itemView.findViewById(R.id.news_content_txt);

        }
    }
}
