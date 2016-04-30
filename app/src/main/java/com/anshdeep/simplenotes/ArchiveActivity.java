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
    int modifyPos = -1;
    int archiveCount;
    boolean deleted;

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
        archiveCount = intent.getIntExtra("count",0);



        archiveRecyclerView = (RecyclerView) findViewById(R.id.archive_list);

        //using staggered grid layout manager for main recycler view
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        archiveRecyclerView.setLayoutManager(gridLayoutManager);


        mArchiveAdapter = new ArchiveAdapter(ArchiveActivity.this, archives);
        archiveRecyclerView.setAdapter(mArchiveAdapter);
        Log.d("ArchiveActivity", "mArchive adapter : " + mArchiveAdapter.getItemCount() );

    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_archive, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//
//        if (id == R.id.action_trash) {
////            Toast.makeText(getApplicationContext(),"Trash pressed",Toast.LENGTH_SHORT).show();
//            AlertDialog.Builder builder = new AlertDialog.Builder(ArchiveActivity.this);
//            builder.setMessage("Delete all archived notes?");
//
//            String positiveText = "Delete";
//            builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    deleted=true;
//                    Log.d("ArchiveActivity", "archive count before deleting :" + Archive.count(Archive.class));
//
//                    Archive.deleteAll(Archive.class);
//                    archives.clear();
//                    mArchiveAdapter.notifyDataSetChanged();
//                    Log.d("ArchiveActivity","mArchiveAdapter after notifying: " + mArchiveAdapter.getItemCount());
//                    Log.d("ArchiveActivity", "archive count after deleting :" + Archive.count(Archive.class));
//                    Snackbar.make(archiveRecyclerView,"Deleted all archives",Snackbar.LENGTH_SHORT).show();
//
//                }
//            });
//
//            String negativeText = "Cancel";
//            builder.setNegativeButton(negativeText,null);
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }



    @Override
    protected void onResume() {
        super.onResume();

        final long newCount = Archive.count(Archive.class);

        //make list empty and refreshing recyler view
        if(newCount==0){
            archives.clear();
            mArchiveAdapter.notifyDataSetChanged();
        }
        mArchiveAdapter.notifyDataSetChanged();

        Log.d("ArchiveActivity", "archives size: " + archives.size());



//        if(newCount > archiveCount){
//            // Just load the last added note (new)
//            Archive archive = Archive.last(Archive.class);
//
//            archives.add(archive);
//            mArchiveAdapter.notifyItemInserted((int) newCount);
//
//            archiveCount = (int)newCount;
//        }




    }
}
