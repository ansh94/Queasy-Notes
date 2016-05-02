package com.anshdeep.simplenotes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;

    private FloatingActionButton fab;
    NotesAdapter adapter;

    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;


    private Toolbar toolbar;
    List<Note> notes = new ArrayList<>();
    List<Archive> archives = new ArrayList<>();
    List<Reminder> reminders = new ArrayList<>();


    long initialCount;

    int archiveCount;
    int modifyPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initNavigationDrawer();


        recyclerView = (RecyclerView) findViewById(R.id.main_list);
        fab = (FloatingActionButton) findViewById(R.id.fab);


        //using staggered grid layout manager for main recycler view
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(gridLayoutManager);


        initialCount = Note.count(Note.class);
        archiveCount = (int) Archive.count(Archive.class);

        Log.d("MainActivity", "note count: " + initialCount);


        if (savedInstanceState != null) {
            modifyPos = savedInstanceState.getInt("modify");
        }


        if (initialCount >= 0) {
            notes = Note.listAll(Note.class);
//

            archives = Archive.listAll(Archive.class);
            reminders = Reminder.listAll(Reminder.class);
            adapter = new NotesAdapter(MainActivity.this, notes);
            recyclerView.setAdapter(adapter);
        } else {
            Snackbar.make(recyclerView, "No notes added.", Snackbar.LENGTH_SHORT).show();
        }

        /*
        recycler view.onClick
        @Override
            public void onItemLongClick(View view, final int position) {
                PopupMenu popupMenu = new PopupMenu(NotesActivity.this, view);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu_actions_notes, popupMenu.getMenu());
                popupMenu.show();
                final View v = view;
                final int pos = position;
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.action_delete) {
                            moveToTrash();
                            delete(v, pos);
                        } else if(item.getItemId() == R.id.action_archive) {
                            moveToArchive(v, pos);
                        } else if(item.getItemId() == R.id.action_edit) {
                            edit(v);
                        }

                        return false;
                    }
                });
            }
        }));
    }


         */


        // tinting FAB icon
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_add_24dp);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, Color.WHITE);
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);

            fab.setImageDrawable(drawable);

        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, AddNoteActivity.class);
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
                final Note note = notes.get(viewHolder.getAdapterPosition());


                archiveCount++;
                Log.d("MainActivity", "archivecount: " + archiveCount);


                //removing from notes arraylist
                notes.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(position);

                String myTitle = note.getTitle();
                String myNote = note.getNote();
                long myTime = note.getTime();
                final Archive archive = new Archive(myTitle, myNote, myTime);
                archive.save();
                archives.add(archive);


                note.delete();
                initialCount -= 1;

                Snackbar.make(recyclerView, "Note archived", Snackbar.LENGTH_SHORT)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                note.save();
                                notes.add(position, note);
                                adapter.notifyItemInserted(position);
                                initialCount += 1;

                                archives.remove(archive);
                                archive.delete();
                                archiveCount--;

                            }
                        })
                        .show();
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.SetOnItemClickListener(new NotesAdapter.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Log.d("Main", "click");

                Intent i = new Intent(MainActivity.this, AddNoteActivity.class);
                i.putExtra("isEditing", true);
                i.putExtra("note_title", notes.get(position).title);
                i.putExtra("note", notes.get(position).note);
                i.putExtra("note_time", notes.get(position).time);


                modifyPos = position;

                startActivity(i);
            }

            @Override
            public boolean onItemLongClicked(int position) {
                if (actionMode == null) {
                    actionMode = startSupportActionMode(actionModeCallback);
                }

                toggleSelection(position);

                return true;
            }


        });


    }

    /**
     * Toggle the selection state of an item.
     * <p/>
     * If the item was the last one in the selection and is unselected, the selection is stopped.
     * Note that the selection must already be started (actionMode must not be null).
     *
     * @param position Position of the item to toggle the selection state
     */
    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.selected_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    adapter.removeItems(adapter.getSelectedItems());
                    Snackbar.make(recyclerView,"Selected notes deleted",Snackbar.LENGTH_SHORT).show();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelection();
            actionMode = null;
        }
    }


    public void initNavigationDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.home:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.archives:
                        Intent intent = new Intent(MainActivity.this, ArchiveActivity.class);
                        Bundle args = new Bundle();
                        args.putSerializable("ARRAYLIST", (Serializable) archives);
                        intent.putExtra("BUNDLE", args);
                        intent.putExtra("count", archiveCount);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.reminders:
                        Intent i = new Intent(MainActivity.this, ReminderActivity.class);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.help:
                        Intent in = new Intent(MainActivity.this, HelpActivity.class);
                        startActivity(in);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.feedback:
                        Intent inte = new Intent(MainActivity.this,FeedbackActivity.class);
                        startActivity(inte);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.share:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                                "Hey check out the new Simple Notes app at: https://play.google.com/store/apps/details?id=com.anshdeep.simplenotes");
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.rate:
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=com.anshdeep.simplenotes")));
                        } catch (android.content.ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=com.anshdeep.simplenotes")));
                        }
                        drawerLayout.closeDrawers();
                        break;

                    //About section
                    case R.id.logout:
                        finish();
                }
                return true;
            }
        });

        View header = navigationView.getHeaderView(0);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
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

        final long newCount = Note.count(Note.class);

        if (newCount > initialCount) {
            // A note is added
            Log.d("Main", "Adding new note");

            // Just load the last added note (new)
            Note note = Note.last(Note.class);

            notes.add(note);
            adapter.notifyItemInserted((int) newCount);

            initialCount = newCount;
        }

        if (modifyPos != -1) {
            notes.set(modifyPos, Note.listAll(Note.class).get(modifyPos));
            adapter.notifyItemChanged(modifyPos);
        }

    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateFormat(long date) {
        return new SimpleDateFormat("dd MMM yyyy").format(new Date(date));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuItem searchViewItem = menu.findItem(R.id.action_search);
//        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
//        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchViewAndroidActionBar.clearFocus();
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                final List<Note> filteredNotes = filter(notes,newText);
//                adapter.setFilter(filteredNotes);
//                return true;
//            }
//        });

        return true;
    }

//    private List<Note> filter(List<Note> models, String query) {
//        query = query.toLowerCase();
//
//        final List<Note> filteredNoteList = new ArrayList<>();
//        for (Note model : models) {
//            final String text = model.getTitle().toLowerCase();
//            if (text.contains(query)) {
//                filteredNoteList.add(model);
//            }
//        }
//        return filteredNoteList;
//    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }


        return super.onOptionsItemSelected(item);
    }
}
