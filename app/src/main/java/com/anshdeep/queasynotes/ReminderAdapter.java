package com.anshdeep.queasynotes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private String updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }

    private String updateDate(String date){
        SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");

        String updatedDate = "";
        try {
            Date oneWayTripDate = input.parse(date);             // parse input
            updatedDate = output.format(oneWayTripDate);    // format output
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return updatedDate;
    }

    @Override
    public RemindersVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item, parent, false);
        RemindersVH viewHolder = new RemindersVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RemindersVH reminderHolder, int position) {
        String[] time = reminders.get(position).getTime().split(":");
        String updatedTime = updateTime(Integer.parseInt(time[0]),Integer.parseInt(time[1]));
        String date = reminders.get(position).getDate();
        String myUpdatedDate = updateDate(date);
        reminderHolder.title.setText(reminders.get(position).getTitle());
        reminderHolder.desc.setText(reminders.get(position).getBody());
        reminderHolder.datetime.setText(myUpdatedDate + "  " + updatedTime);
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        reminderHolder.itemView.setBackgroundColor(randomAndroidColor);
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    class RemindersVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, desc,datetime;

        public RemindersVH(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.reminder_item_title);
            desc = (TextView) itemView.findViewById(R.id.reminder_item_desc);
            datetime = (TextView) itemView.findViewById(R.id.reminder_item_datetime);
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
