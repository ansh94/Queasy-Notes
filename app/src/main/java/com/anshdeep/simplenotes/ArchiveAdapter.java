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
 * Created by ANSHDEEP on 02-04-2016.
 */
public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ArchivesVH> {

    Context context;
    List<Archive> archives;

    public ArchiveAdapter(Context context, List<Archive> archives) {
        this.context = context;
        this.archives = archives;
    }

    @Override
    public ArchivesVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        ArchivesVH viewHolder = new ArchivesVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArchivesVH holder, int position) {

        holder.title.setText(archives.get(position).getTitle());
        holder.note.setText(archives.get(position).getNote());
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        holder.itemView.setBackgroundColor(randomAndroidColor);

    }



    @Override
    public int getItemCount() {
        return archives.size();
    }

    class ArchivesVH extends RecyclerView.ViewHolder  {
        TextView title, note;

        public ArchivesVH(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.note_item_title);
            note = (TextView) itemView.findViewById(R.id.note_item_desc);

        }


    }
}
