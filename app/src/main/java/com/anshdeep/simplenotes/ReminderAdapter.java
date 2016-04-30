package com.anshdeep.simplenotes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

/**
 * Created by ANSHDEEP on 05-04-2016.
 */
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.RemindersVH> {

    Context context;
    List<Reminder> reminders;

    OnItemClickListener clickListener;

    public ReminderAdapter(Context context, List<Reminder> reminders) {
        this.context = context;
        this.reminders = reminders;
    }

    @Override
    public RemindersVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item, parent, false);
        RemindersVH viewHolder = new RemindersVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RemindersVH reminderHolder, int position) {
        reminderHolder.title.setText(reminders.get(position).getTitle());
        reminderHolder.desc.setText(reminders.get(position).getBody());
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        reminderHolder.itemView.setBackgroundColor(randomAndroidColor);
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    class RemindersVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, desc;

        public RemindersVH(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.reminder_item_title);
            desc = (TextView) itemView.findViewById(R.id.reminder_item_desc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getAdapterPosition());
        }


    }
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
