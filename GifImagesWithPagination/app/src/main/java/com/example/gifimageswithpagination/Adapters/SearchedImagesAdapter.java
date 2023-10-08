package com.example.gifimageswithpagination.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gifimageswithpagination.Models.Image;
import com.example.gifimageswithpagination.R;

import java.util.List;

public class SearchedImagesAdapter extends RecyclerView.Adapter<SearchedImagesAdapter.ViewHolder> {

    Context context;
    List<Image> imgList;

    private OnItemClickListener onItemClickListener;

    public SearchedImagesAdapter(Context context, List<Image> imgList){
        this.context = context;
        this.imgList = imgList;
    }

    public void setFilteredList(List<Image> filteredList){
        this.imgList = filteredList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(int pos);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image data = imgList.get(position);

        Glide.with(context).load(data.getImgUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.imgView);


            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        int pos = getAbsoluteAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                            onItemClickListener.onItemClick(pos);
                        }
                    }
                }
            });
        }

    }
}
