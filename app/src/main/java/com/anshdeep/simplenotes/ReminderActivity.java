package com.anshdeep.simplenotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.List;

public class ReminderActivity extends AppCompatActivity {

    private RecyclerView reminderRecyclerView;
    ReminderAdapter reminderAdapter;
    private FloatingActionButton fab;
    List<Reminder> reminders;

    long initialCount;
    int modifyPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fab = (FloatingActionButton) findViewById(R.id.rem_add_fab);
        reminderRecyclerView = (RecyclerView) findViewById(R.id.reminder_list);




        //using staggered grid layout manager for main recycler view
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        reminderRecyclerView.setLayoutManager(gridLayoutManager);

        initialCount = Reminder.count(Reminder.class);
//        Log.d("ReminderActivity", "reminder count before deleting: " + initialCount);
//        Reminder.deleteAll(Reminder.class);
//        Log.d("ReminderActivity", "reminder count before deleting: " + Reminder.count(Reminder.class));

        if (savedInstanceState != null) {
            modifyPos = savedInstanceState.getInt("modify");
        }

        reminders = Reminder.listAll(Reminder.class);


        Log.d("ReminderActitivty", "Reminder count: " + reminders.size());
        reminderAdapter = new ReminderAdapter(ReminderActivity.this, reminders);
        reminderRecyclerView.setAdapter(reminderAdapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReminderActivity.this, AddReminderActivity.class);
                startActivity(i);
            }
        });

        // Handling swipe to delete
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //Remove swiped item from list and notify the RecyclerView

                final int position = viewHolder.getAdapterPosition();
                final Reminder reminder = reminders.get(viewHolder.getAdapterPosition());


                //removing from notes arraylist
                reminders.remove(viewHolder.getAdapterPosition());
                reminderAdapter.notifyItemRemoved(position);


                reminder.delete();
                initialCount -= 1;

                Snackbar.make(reminderRecyclerView, "Reminder deleted", Snackbar.LENGTH_SHORT)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                reminder.save();
                                reminders.add(position, reminder);
                                reminderAdapter.notifyItemInserted(position);
                                initialCount += 1;

                            }
                        })
                        .show();
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(reminderRecyclerView);

        reminderAdapter.SetOnItemClickListener(new ReminderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Log.d("Main", "click");

                Intent i = new Intent(ReminderActivity.this, AddReminderActivity.class);
                i.putExtra("isEditing", true);
                i.putExtra("reminder_title", reminders.get(position).title);
                i.putExtra("reminder_body", reminders.get(position).body);
                i.putExtra("reminder_date", reminders.get(position).date);
                i.putExtra("reminder_time", reminders.get(position).time);


                modifyPos = position;

                startActivity(i);
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("modify", modifyPos);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        modifyPos = savedInstanceState.getInt("modify");
    }

    @Override
    protected void onResume() {
        super.onResume();

        final long newCount = Reminder.count(Reminder.class);

        if (newCount > initialCount) {
            // A note is added
            Log.d("Main", "Adding new reminder");

            // Just load the last added note (new)
            Reminder reminder = Reminder.last(Reminder.class);

            reminders.add(reminder);
            reminderAdapter.notifyItemInserted((int) newCount);

            initialCount = newCount;
        }

        if (modifyPos != -1) {
            reminders.set(modifyPos, Reminder.listAll(Reminder.class).get(modifyPos));
            reminderAdapter.notifyItemChanged(modifyPos);
        }

    }

}
