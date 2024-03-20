package com.vanthom04.vtshop.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vanthom04.vtshop.R;
import com.vanthom04.vtshop.models.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> list;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Notification> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = list.get(position);
//        if (notification == null) return;
        holder.titleNotification.setText(notification.getTitle());
        holder.messageNotification.setText(notification.getMessage());
    }

    @Override
    public int getItemCount() {
        if (list != null) return list.size();
        return 0;
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView titleNotification, messageNotification;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            titleNotification = itemView.findViewById(R.id.title_notification);
            messageNotification = itemView.findViewById(R.id.message_notification);
        }
    }
}
