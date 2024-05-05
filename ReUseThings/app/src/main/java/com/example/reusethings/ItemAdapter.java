package com.example.reusethings;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> items;
    private Context context;
    private DatabaseHelper databaseHelper;
    private OnDeleteListener deleteListener;

    public ItemAdapter(List<Item> items, Context context) {
        this.items = items;
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
    }

    public void setFilteredList(List<Item> filteredList){
        this.items = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.nameTextView.setText("\n" + item.getName());
        holder.phoneNumberView.setText("\n" + item.getPhoneNumber());
        holder.addressView.setText("\n" + item.getAddress());
        holder.imageView.setImageBitmap(item.getImage());

        holder.deleteButton.setOnClickListener(v -> {
            String itemId = item.getName();
            String itemPhone = item.getPhoneNumber();
            String itemAddress = item.getAddress();

            databaseHelper.deleteItemByDetails(itemId, itemPhone, itemAddress); // Delete the item from the database

            items.remove(position); // Remove it from the list
            notifyItemRemoved(position); // Notify the RecyclerView
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setDeleteListener(OnDeleteListener listener) {
        this.deleteListener = listener;
    }

    public interface OnDeleteListener {
        void onDelete(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView phoneNumberView;
        public TextView addressView;
        public ImageView imageView;
        public Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.item_name);
            phoneNumberView = itemView.findViewById(R.id.phone_number);
            addressView = itemView.findViewById(R.id.address);
            imageView = itemView.findViewById(R.id.item_image);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
