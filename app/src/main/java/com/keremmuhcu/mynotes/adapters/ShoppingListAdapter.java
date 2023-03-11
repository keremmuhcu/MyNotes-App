package com.keremmuhcu.mynotes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keremmuhcu.mynotes.R;
import com.keremmuhcu.mynotes.entities.ShoppingListEntities;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    List<ShoppingListEntities> lists;

    public ShoppingListAdapter(List<ShoppingListEntities> lists) {
        this.lists = lists;
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ViewHolder holder, int position) {
        holder.setNote(lists.get(position));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle, textDateTime, textNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.shopping_heading);
            textDateTime = itemView.findViewById(R.id.date_shop);
            textNote = itemView.findViewById(R.id.shopping_list_text);
        }

        public void setNote(ShoppingListEntities shoppingListEntities) {
            textTitle.setText(shoppingListEntities.getTitle());
            textDateTime.setText(shoppingListEntities.getDateTime());
            textNote.setText(shoppingListEntities.getNoteText());
        }
    }
}
