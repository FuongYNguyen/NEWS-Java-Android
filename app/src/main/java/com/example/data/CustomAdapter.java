package com.example.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> news_id, news_title, news_content, news_image, news_sc, news_categoryId;
    char typeRow;

    // Constructor
    CustomAdapter(Context context, ArrayList<String> news_id, ArrayList<String> news_title, ArrayList<String> news_content, ArrayList<String> news_image, ArrayList<String> news_sc, ArrayList<String> news_categoryId, char typeRow) {
        this.context = context;
        this.news_id = news_id;
        this.news_title = news_title;
        this.news_content = news_content;
        this.news_sc=news_sc;
        this.news_categoryId=news_categoryId;
        this.news_image = news_image;
        this.typeRow = typeRow;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if (typeRow == '1') {
            // Inflate my_row2.xml
            view = inflater.inflate(R.layout.my_row2, parent, false);
        } else if (typeRow == '2') {
            view = inflater.inflate(R.layout.row_news_manage, parent, false);
        } else if (typeRow == '3') {
            view = inflater.inflate(R.layout.row_category, parent, false);
        } else if (typeRow == '4') {
            view = inflater.inflate(R.layout.row_category_manage, parent, false);
        } else if (typeRow == '5') {
            view = inflater.inflate(R.layout.row_category, parent, false);
        }else {
            // Inflate my_row.xml
            view = inflater.inflate(R.layout.my_row, parent, false);
        }
        return new MyViewHolder(view, typeRow);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.news_id_txt.setText(news_id.get(position));
        holder.news_title_txt.setText(news_title.get(position));
        if (typeRow == '4'){
            holder.del.setOnClickListener(v -> {
                MyDataHelper db= new MyDataHelper(holder.itemView.getContext());
                db.deleteById("category", Integer.parseInt(news_id.get(position)));
                news_id.remove(position);
                news_title.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, news_id.size());
            });
        }
        if (typeRow == '3') {
            holder.itemView.setOnClickListener(v -> {
                // Lấy categoryId từ news_id
                String categoryId = news_id.get(position);

                // Chuyển đến Activity hiển thị các bài viết thuộc category này
                Intent intent = new Intent(context, NewsByCategoryActivity.class);
                intent.putExtra("category_id", categoryId);
                context.startActivity(intent);
            });
        }
        if (typeRow == '5') {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("news_id", news_id.get(position));
                context.startActivity(intent);
            });
        }


        if(typeRow != '3' && typeRow != '4' && typeRow != '5')
        {
            holder.news_sc_txt.setText(news_sc.get(position));
            // Chỉ load hình ảnh nếu là layout row.xml
            if (typeRow == '0' || typeRow == '2') {
                Glide.with(context).load(news_image.get(position)).into(holder.thumbnail_input_img);
            }

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("news_id", news_id.get(position));
                context.startActivity(intent);
            });
            if (typeRow == '2'){
                holder.edit.setOnClickListener(v -> {
                    Intent intent = new Intent(context, EditActivity.class);
                    intent.putExtra("news_id", news_id.get(position));
                    context.startActivity(intent);
                });

                holder.del.setOnClickListener(v -> {
                    MyDataHelper db= new MyDataHelper(holder.itemView.getContext());
                    db.deleteById("news", Integer.parseInt(news_id.get(position)));
                    news_id.remove(position);
                    news_title.remove(position);
                    news_sc.remove(position);
                    news_image.remove((position));
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, news_id.size());
                });
            }
        }


    }

    @Override
    public int getItemCount() {
        return news_id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail_input_img;
        TextView news_id_txt, news_title_txt,news_sc_txt;
        ImageButton edit, del;
        String newsId;
        public MyViewHolder(@NonNull View itemView, char typeRow) {
            super(itemView);
            if (typeRow == '0' || typeRow == '2') {
                // row.xml
                thumbnail_input_img = itemView.findViewById(R.id.thumbnail_input_img);
            }
            else {
                // row2.xml: thumbnail_input_img không tồn tại
                thumbnail_input_img = null;
            }
            news_id_txt = itemView.findViewById(R.id.news_id_txt);
            news_title_txt = itemView.findViewById(R.id.news_title_txt);
            news_sc_txt = itemView.findViewById(R.id.news_sc_txt);
            edit = itemView.findViewById(R.id.edit);
            del = itemView.findViewById(R.id.del);
        }
    }
}
