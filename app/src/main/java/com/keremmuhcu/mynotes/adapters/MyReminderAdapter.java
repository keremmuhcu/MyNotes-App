package com.keremmuhcu.mynotes.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keremmuhcu.mynotes.R;
import com.keremmuhcu.mynotes.entities.MyReminderEntities;

import java.util.List;

public class MyReminderAdapter extends RecyclerView.Adapter<MyReminderAdapter.ViewHolder> {

    List<MyReminderEntities> reminderEntities;

    public MyReminderAdapter(List<MyReminderEntities> reminderEntities) {
        this.reminderEntities = reminderEntities;
    }

    @NonNull
    @Override
    public MyReminderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.remember_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyReminderAdapter.ViewHolder holder, int position) {
        holder.setReminder(reminderEntities.get(position));
    }

    @Override
    public int getItemCount() {
        return reminderEntities.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, dateTime;
        private View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.reminderHeading);
            dateTime = itemView.findViewById(R.id.dateReminder);
            view = itemView.findViewById(R.id.viewReminderItem);
        }

        public void setReminder(MyReminderEntities myReminderEntities) {
            title.setText(myReminderEntities.getTitle());
            dateTime.setText(myReminderEntities.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) view.getBackground();
            if (myReminderEntities.getColor() != null) {
                gradientDrawable.setColor(Color.parseColor(myReminderEntities.getColor()));
            } else {
                gradientDrawable.setColor(Color.parseColor("#FFFB7B"));
            }
        }
    }
}
