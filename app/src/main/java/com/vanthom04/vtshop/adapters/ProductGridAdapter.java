package com.vanthom04.vtshop.adapters;

import static com.vanthom04.vtshop.utils.AppUtils.changeIntToString;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vanthom04.vtshop.R;
import com.vanthom04.vtshop.models.Product;
import com.vanthom04.vtshop.interfaces.IOnClickItemProductListener;

import java.util.List;

public class ProductGridAdapter extends RecyclerView.Adapter<ProductGridAdapter.ProductGirdViewHolder> {
    private List<Product> products;
    private final IOnClickItemProductListener itemProductListener;

    public ProductGridAdapter(IOnClickItemProductListener listener) {
        this.itemProductListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Product> list) {
        this.products = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductGirdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_gird_view, parent, false);
        return new ProductGirdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductGirdViewHolder holder, int position) {
        Product product = products.get(position);
        if (product == null) return;

        Context context = holder.itemView.getContext();
        Glide.with(context).load(product.getThumbnail()).into(holder.image);
        holder.name.setText(product.getName());
        holder.price.setText(changeIntToString(product.getPrice()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemProductListener.onClickItemProduct(product.getProductId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (products != null) return products.size();
        return 0;
    }

    public static class ProductGirdViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, price;
        public ProductGirdViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_item_grid);
            name = itemView.findViewById(R.id.name_item_grid);
            price = itemView.findViewById(R.id.price_item_grid);
        }
    }
}
