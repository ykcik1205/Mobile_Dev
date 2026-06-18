package com.example.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.k234112eapp.R;
import com.example.models.PaperItem;

public class PaperAdapter extends ArrayAdapter<PaperItem> {
    Activity context;
    int resource;

    public PaperAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View custom = inflater.inflate(resource, null);
        PaperItem item = getItem(position);

        ImageView imgThumb = custom.findViewById(R.id.imgThumb);
        TextView txtTitle = custom.findViewById(R.id.txtTitle);
        TextView txtPrice = custom.findViewById(R.id.txtPrice);

        if (item != null) {
            txtTitle.setText(item.getTitle());
            txtPrice.setText(item.getPrice());

            // Sử dụng Glide để tải ảnh từ URL
            Glide.with(context)
                 .load(item.getThumb())
                 .placeholder(android.R.drawable.ic_menu_gallery)
                 .into(imgThumb);

            // Sự kiện nhấp vào ảnh để mở link web
            imgThumb.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
                context.startActivity(intent);
            });
        }
        return custom;
    }
}
