package com.example.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.k234112eapp.R;
import com.example.models.Category;


public class CategoryAdapter extends ArrayAdapter<Category>
{
    Activity context;
    int resource;
    public CategoryAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View custom=inflater.inflate(resource,null);
        Category c=getItem(position);
        TextView txtcateId=custom.findViewById(R.id.txtcateId);
        TextView txtcateName=custom.findViewById(R.id.txtcateName);
        TextView txtcateDesc=custom.findViewById(R.id.txtcateDesc);
        txtcateId.setText(c.getCateId()+"");
        txtcateName.setText(c.getCateName()+"");
        txtcateDesc.setText(c.getCateDesc()+"");
        //return super.getView(position, convertView, parent); //nhớ phải đổi lại thành return custom, view cái nào return cái đó
        return custom;
    }
}
