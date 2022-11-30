package com.example.studentagenda;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Displays data from the model into a row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnClickListener {
        void onItemCLicked(int position);
    }

    public interface OnLongClickListener {
        // The position parameter is important to know which element we want to delete from the list
        void onItemLongClicked(int position);
    }

    List<String> itemsList;
    OnLongClickListener longClickedItem;
    OnClickListener clickListener;

    public ItemsAdapter(List<String> itemsList, OnLongClickListener longClickedItem, OnClickListener clickedItem) {
        this.itemsList = itemsList;
        this.longClickedItem = longClickedItem;
        this.clickListener = clickedItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(todoView);
    }

    // Responsible for binding data to a particular ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Contains the item at the specified position
        String item = itemsList.get(position);
        // Binds the item into the specified ViewHolder
        holder.bind(item);

    }

    // Returns how many items are in the list
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    // Contains the views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        // Updates the view inside the ViewHolder with this data
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemCLicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Notifies the position of the item that was long pressed
                    longClickedItem.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}