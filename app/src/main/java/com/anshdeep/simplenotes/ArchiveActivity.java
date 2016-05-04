package com.anshdeep.simplenotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ArchiveActivity extends AppCompatActivity {

    private RecyclerView archiveRecyclerView;
    ArchiveAdapter mArchiveAdapter;
    List<Archive> archives;
    int archiveCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        archives = (ArrayList<Archive>) args.getSerializable("ARRAYLIST");
        archiveCount = intent.getIntExtra("count", 0);


        archiveRecyclerView = (RecyclerView) findViewById(R.id.archive_list);

        //using staggered grid layout manager for main recycler view
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        archiveRecyclerView.setLayoutManager(gridLayoutManager);


        mArchiveAdapter = new ArchiveAdapter(ArchiveActivity.this, archives);
        archiveRecyclerView.setAdapter(mArchiveAdapter);
        Log.d("ArchiveActivity", "mArchive adapter : " + mArchiveAdapter.getItemCount());

    }


    @Override
    protected void onResume() {
        super.onResume();

        final long newCount = Archive.count(Archive.class);

        //make list empty and refreshing recyler view
        if (newCount == 0) {
            archives.clear();
            mArchiveAdapter.notifyDataSetChanged();
        }
        mArchiveAdapter.notifyDataSetChanged();

        Log.d("ArchiveActivity", "archives size: " + archives.size());


    }
}
