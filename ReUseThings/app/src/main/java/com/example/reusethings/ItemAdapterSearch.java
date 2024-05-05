package com.example.reusethings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapterSearch extends RecyclerView.Adapter<ItemAdapterSearch.ViewHolder> {
    private List<Item> items;
    private Context context;
    private DatabaseHelper databaseHelper;
    private String loggedInUsername;

    public ItemAdapterSearch(List<Item> items, Context context, String loggedInUsername) {
        this.items = items;
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
        this.loggedInUsername = loggedInUsername;
    }

    public void setFilteredList(List<Item> filteredList){
        this.items = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public ItemAdapterSearch.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_search, parent, false);
        return new ItemAdapterSearch.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemAdapterSearch.ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.nameTextView.setText("\n" + item.getName());
        holder.phoneNumberView.setText("\n" + item.getPhoneNumber());
        holder.addressView.setText("\n" + item.getAddress());
        holder.imageView.setImageBitmap(item.getImage());

        // Show or hide ownership indicator based on owner match:
        if (item.getUserName().equals(loggedInUsername)) {
            holder.ownerIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.ownerIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView phoneNumberView;
        public TextView addressView;
        public ImageView imageView;
        public ImageView ownerIndicator;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.item_name);
            phoneNumberView = itemView.findViewById(R.id.phone_number);
            ownerIndicator = itemView.findViewById(R.id.owner_indicator);
            addressView = itemView.findViewById(R.id.address);
            imageView = itemView.findViewById(R.id.item_image);

        }
    }
}
