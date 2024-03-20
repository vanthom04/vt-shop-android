package com.vanthom04.vtshop.activities.sites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vanthom04.vtshop.R;
import com.vanthom04.vtshop.adapters.NotificationAdapter;
import com.vanthom04.vtshop.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    ImageView btnBack;
    RecyclerView recyclerViewNotification;
    NotificationAdapter notificationAdapter;
    List<Notification> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        setBtnBack();

        setList();

        setViewNotification();
    }

    private void setBtnBack() {
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setList() {
        list.add(new Notification(1, "THÁNG MA MỊ, QUÀ MÊ LY", "Halloween with VTShop. Chỉ từ 490K. Giảm lên đến 20 Triệu"));
        list.add(new Notification(2, "MUA NGAY TAI NGHE, BẬT MODE GIAI ĐIỆU", "Giảm giá lên đến 50%. Bảo hành chính hãng. Free ship toàn quốc!"));
        list.add(new Notification(3, "GAMING STATION, CỨ ĐIỂM CHƠI GAME", "Giá chỉ từ 4.000.000đ. Khuyến mãi lên đến 21.000.000đ"));
        list.add(new Notification(4, "Màn hình chính hãng", "Giá ngon quà chất - Cân tất mọi game. Giảm sốc đến 47%, chỉ từ 2 triệu"));
        list.add(new Notification(5, "RAPOO MIO PLUS", "Tặng ngay chuột không dây khi mua màn gaming 144Hz chỉ từ 3 triệu"));
        list.add(new Notification(6, "ƯU ĐÃI", "Khi mua kèm PC và Laptop. Giảm giá lên đến 8.000.000đ"));
        list.add(new Notification(7, "Zenbook | Vivobook", "Laptop Lumina OLED tốt nhất"));
        list.add(new Notification(8, "ƯU ĐÃI MÀN HÌNH SẮM NGAY MÁY XỊN", "Ưu đãi giảm giá màn hình ASUS 50%..."));
    }

    private void setViewNotification() {
        recyclerViewNotification = findViewById(R.id.recyclerview_notification);
        notificationAdapter = new NotificationAdapter();
        recyclerViewNotification.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        notificationAdapter.setData(list);
        recyclerViewNotification.setAdapter(notificationAdapter);
    }
}