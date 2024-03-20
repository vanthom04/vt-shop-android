package com.vanthom04.vtshop.fragments;

import static com.vanthom04.vtshop.utils.AppUtils.onClickToToActivityProductsBrand;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vanthom04.vtshop.R;
import com.vanthom04.vtshop.adapters.BrandAdapter;
import com.vanthom04.vtshop.models.Brand;
import com.vanthom04.vtshop.interfaces.IOnClickItemBrandListener;

import java.util.ArrayList;
import java.util.List;

public class BrandFragment extends Fragment {

    private View view;
    RecyclerView categoryView;
    BrandAdapter brandAdapter;
    List<Brand> categoryList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category, container, false);
        //

        setCategoryList();
        setCategoryView(categoryList);

        return view;
    }

    private void setCategoryList() {
        categoryList.add(new Brand(1, "Apple", R.drawable.logo_apple));
        categoryList.add(new Brand(2, "Dell", R.drawable.logo_dell));
        categoryList.add(new Brand(3, "Asus", R.drawable.logo_asus));
        categoryList.add(new Brand(4, "Msi", R.drawable.logo_msi));
        categoryList.add(new Brand(5, "Lenovo", R.drawable.logo_lenovo));
        categoryList.add(new Brand(6, "Acer", R.drawable.logo_acer));
        categoryList.add(new Brand(7, "Hp", R.drawable.logo_hp));
    }

    private void setCategoryView(List<Brand> list) {
        categoryView = view.findViewById(R.id.recyclerview_category);
        brandAdapter = new BrandAdapter(new IOnClickItemBrandListener() {
            @Override
            public void onClickItemBrand(String brand) {
                onClickToToActivityProductsBrand(getContext(), brand);
            }
        });
        brandAdapter.setData(list);
        categoryView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        categoryView.setAdapter(brandAdapter);
    }
}