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
import com.vanthom04.vtshop.models.ProductCart;
import com.vanthom04.vtshop.interfaces.IOnClickItemProductListener;

import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder> {

    private List<ProductCart> list;
    private final IOnClickItemProductListener itemProductListener;

    public CartProductAdapter(IOnClickItemProductListener itemProductListener) {
        this.itemProductListener = itemProductListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<ProductCart> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_view, parent, false);
        return new CartProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductViewHolder holder, int position) {
        ProductCart product = list.get(position);
        if (product == null) return;

        Context context = holder.itemView.getContext();
        Glide.with(context).load(product.getThumbnail()).into(holder.imgView);
        holder.name.setText(product.getName());
        holder.price.setText(changeIntToString(product.getPrice()));
        holder.quantity.setText(String.valueOf(product.getQuantity()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                itemProductListener.onClickItemProduct(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) return list.size();
        return 0;
    }

    public static class CartProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        TextView name, price, quantity, totalAmount;
        ImageView btnPrev, btnNext;
        public CartProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.img_item_cart);
            name = itemView.findViewById(R.id.name_item_cart);
            price = itemView.findViewById(R.id.price_item_cart);
            quantity = itemView.findViewById(R.id.quantity_item_cart);
            btnPrev = itemView.findViewById(R.id.btn_prev_item_cart);
            btnNext = itemView.findViewById(R.id.btn_next_item_cart);
        }
    }
}
