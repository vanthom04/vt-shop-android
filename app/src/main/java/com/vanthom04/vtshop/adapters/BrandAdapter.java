package com.vanthom04.vtshop.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vanthom04.vtshop.R;
import com.vanthom04.vtshop.models.Brand;
import com.vanthom04.vtshop.interfaces.IOnClickItemBrandListener;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.CategoryViewHolder> {

    private List<Brand> brands;
    private final IOnClickItemBrandListener brandListener;
    public BrandAdapter(IOnClickItemBrandListener categoryListener) {
        this.brandListener = categoryListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Brand> list) {
        this.brands = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_category_view, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Brand brand = brands.get(position);
        if (brand == null) return;
        holder.imgIcon.setImageResource(brand.getImgUrl());
        holder.name.setText(brand.getName());
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandListener.onClickItemBrand(brand.getName().toLowerCase());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (brands != null) return brands.size();
        return 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        CardView layoutItem;
        ImageView imgIcon;
        TextView name;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.layout_item_category);
            imgIcon = itemView.findViewById(R.id.image_icon_category);
            name = itemView.findViewById(R.id.name_category);
        }
    }
}
