package com.example.lab2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;
public class ObjAdapter extends ArrayAdapter<Obj> {

    private OnDeleteClickListener deleteClickListener;
    private OnEditClickListener editClickListener;

    public ObjAdapter(Context context, List<Obj> objects, OnDeleteClickListener deleteClickListener, OnEditClickListener editClickListener) {
        super(context, 0, objects);
        this.deleteClickListener = deleteClickListener;
        this.editClickListener = editClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Obj obj = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_obj, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.tvAge = convertView.findViewById(R.id.tvAge);
            viewHolder.btnDelete = convertView.findViewById(R.id.btnDelete);
            viewHolder.btnEdit = convertView.findViewById(R.id.btnEdit);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(obj.getName());
        viewHolder.tvAge.setText(String.valueOf(obj.getAge()));

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(obj);
                }
            }
        });

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editClickListener != null) {
                    editClickListener.onEditClick(obj);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvAge;
        Button btnDelete;
        Button btnEdit;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Obj obj);
    }

    public interface OnEditClickListener {
        void onEditClick(Obj obj);
    }
}
