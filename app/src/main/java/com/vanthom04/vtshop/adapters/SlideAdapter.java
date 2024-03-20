package com.vanthom04.vtshop.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vanthom04.vtshop.R;
import com.vanthom04.vtshop.models.Slide;

import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {

    private List<Slide> listSlide;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Slide> list) {
        this.listSlide = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide, parent, false);
        return new SlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        Slide slide = listSlide.get(position);
        if (slide == null) return;
        Context context = holder.itemView.getContext();
        Glide.with(context).load(slide.getThumbnail()).into(holder.imageSlide);
    }

    @Override
    public int getItemCount() {
        if (listSlide != null) return listSlide.size();
        return 0;
    }

    public static class SlideViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSlide;
        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSlide = itemView.findViewById(R.id.item_slide_image);
        }
    }
}
