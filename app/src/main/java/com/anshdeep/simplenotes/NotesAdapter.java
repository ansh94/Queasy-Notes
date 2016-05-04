package com.anshdeep.simplenotes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by ANSHDEEP on 01-04-2016.
 */
public class NotesAdapter extends SelectableAdapter<NotesAdapter.NoteVH>  {
    Context context;
    List<Note> notes;

    @SuppressWarnings("unused")
    private static final String TAG = NotesAdapter.class.getSimpleName();

    private static final int TYPE_INACTIVE = 0;
    private static final int TYPE_ACTIVE = 1;

    private static final int ITEM_COUNT = 50;


    private ClickListener listener;



    public NotesAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;


    }

    public void removeItem(int position) {
        Note note = notes.get(position);
        notes.remove(position);
        notifyItemRemoved(position);
        note.delete();
    }

    public void removeItems(List<Integer> positions) {
        // Reverse-sort the list
        Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });

        // Split the list in ranges
        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                removeItem(positions.get(0));
                positions.remove(0);
            } else {
                int count = 1;
                while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
                    ++count;
                }

                if (count == 1) {
                    removeItem(positions.get(0));
                } else {
                    removeRange(positions.get(count - 1), count);

                }

                for (int i = 0; i < count; ++i) {
                    positions.remove(0);
                }
            }
        }
    }

    private void removeRange(int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; ++i) {
            Note note = notes.get(positionStart);
            notes.remove(positionStart);
            note.delete();
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }


    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        NoteVH viewHolder = new NoteVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteVH holder, int position) {

        holder.title.setText(notes.get(position).getTitle());
        holder.note.setText(notes.get(position).getNote());
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        holder.itemView.setBackgroundColor(randomAndroidColor);

        // Highlight the item if it's selected
        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }




    class NoteVH extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        TextView title, note;
        View selectedOverlay;

        public NoteVH(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.note_item_title);
            note = (TextView) itemView.findViewById(R.id.note_item_desc);
            selectedOverlay = itemView.findViewById(R.id.selected_overlay);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {

            if (listener != null) {
                return listener.onItemLongClicked(getAdapterPosition());
            }
            return false;
        }

    }



    public interface ClickListener {
        public void onItemClick(View view, int position);
        public boolean onItemLongClicked(int position);
    }

    public void SetOnItemClickListener(final ClickListener itemClickListener) {
        this.listener = itemClickListener;
    }

    public void SetOnLongItemClickListener(final ClickListener itemLongClickListener){
        this.listener = itemLongClickListener;
    }
}
