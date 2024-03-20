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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vanthom04.vtshop.R;
import com.vanthom04.vtshop.models.Product;
import com.vanthom04.vtshop.interfaces.IOnClickItemProductListener;

import java.util.List;

public class ProductLinearAdapter extends RecyclerView.Adapter<ProductLinearAdapter.ProductLinearViewHolder> {

    private List<Product> list;
    private final IOnClickItemProductListener itemProductListener;

    public ProductLinearAdapter(IOnClickItemProductListener itemProductListener) {
        this.itemProductListener = itemProductListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Product> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductLinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_linear_view, parent, false);
        return new ProductLinearViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductLinearViewHolder holder, int position) {
        Product product = list.get(position);
        if (product == null) return;

        Context context = holder.imageView.getContext();
        Glide.with(context).load(product.getThumbnail()).into(holder.imageView);
        holder.name.setText(product.getName());
        holder.price.setText(changeIntToString(product.getPrice()));
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemProductListener.onClickItemProduct(product.getProductId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) return list.size();
        return 0;
    }

    public static class ProductLinearViewHolder extends RecyclerView.ViewHolder {
        CardView layoutItem;
        ImageView imageView;
        TextView name, price;
        public ProductLinearViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.layout_item_linear);
            imageView = itemView.findViewById(R.id.img_item_linear);
            name = itemView.findViewById(R.id.name_item_linear);
            price = itemView.findViewById(R.id.price_item_linear);
        }
    }
}
