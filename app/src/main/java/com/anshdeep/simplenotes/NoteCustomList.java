package com.anshdeep.simplenotes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANSHDEEP on 06-04-2016.
 */
public class NoteCustomList extends LinearLayout {

    private Context mContext;
    private LinearLayout mListItem;
    private List<EditText> mTextBoxes = new ArrayList<>();

    public NoteCustomList(Context context){
        super(context);
        mContext = context;
    }

    public void setUp(){
        setOrientation(VERTICAL);
        LayoutParams params  = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
    }

    public void setUpForHomeAdapter(String listEntries){
        // Eggs$false%Ham$false%Bread$false%Vegemite$false

        setOrientation(VERTICAL);
        String[] listEntryTokens = listEntries.split("%");
        // [0] Eggs$false
        // [1] Ham$false
        // ...

        boolean isStrikeOut = false;
        String listItem = "";
        for(String entryDetails: listEntryTokens){
            String[] listEntry = entryDetails.split("\\$");
            mListItem = new LinearLayout(mContext);
            mListItem.setOrientation(HORIZONTAL);
            // [0] Eggs
            // [1] false
            for(int i=0;i<listEntry.length;i++){
                if(i%2==0){
                    listItem = listEntry[i];
                } else {
                    isStrikeOut = Boolean.valueOf(listEntry[i]);
                }
            }

            CheckBox checkBox = new CheckBox(mContext);
            checkBox.setChecked(isStrikeOut);
            checkBox.setEnabled(false);
            TextView textView = new TextView(mContext);
            textView.setText(listItem);
            textView.setBackgroundColor(Color.TRANSPARENT);
            if(isStrikeOut){
                textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }

            mListItem.addView(checkBox);
            mListItem.addView(textView);
            addView(mListItem);
        }
    }

    public void setUpForEditMode(String listEntries) {
        setOrientation(VERTICAL);
        mTextBoxes = new ArrayList<>();
        String[] listEntryTokens = listEntries.split("%");
        boolean isStrikeOut = false;
        String listItem = "";

        for (String entryDetails : listEntryTokens) {
            mListItem = new LinearLayout(mContext);
            mListItem.setOrientation(HORIZONTAL);
            String[] listEntry = entryDetails.split("\\$");
            for (int i = 0; i < listEntry.length; i++) {
                if (i % 2 == 0)
                    listItem = listEntry[i];
                else
                    isStrikeOut = Boolean.valueOf(listEntry[i]);
            }

            final CheckBox checkBox = new CheckBox(mContext);
            checkBox.setChecked(isStrikeOut);

            final ImageView deleteImageView = new ImageView(mContext);
            deleteImageView.setImageResource(android.R.drawable.ic_menu_delete);

            final EditText textBox = new EditText(mContext);
            textBox.setText(listItem);
            textBox.setBackgroundColor(Color.TRANSPARENT);

            if (isStrikeOut) {
                textBox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }

            mListItem.addView(deleteImageView);
            mListItem.addView(checkBox);
            mListItem.addView(textBox);
            mTextBoxes.add(textBox);
            addView(mListItem);

            deleteImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout layout = (LinearLayout) v.getParent();
                    mTextBoxes.remove(mTextBoxes.indexOf(layout.getChildAt(2)));
                    layout.removeAllViews();
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        textBox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        textBox.setPaintFlags(checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                }
            });
        }
    }
//    public void setUpForListNotification(String listEntries) {
//
//        setOrientation(VERTICAL);
//        mTextBoxes = new ArrayList<>();
//        String[] listEntryTokens = listEntries.split("%");
//        boolean isStrikeOut = false;
//        String listItem = "";
//
//        for (String entryDetails : listEntryTokens) {
//            mListItem = new LinearLayout(mContext);
//            mListItem.setOrientation(HORIZONTAL);
//            //
//            if(entryDetails.contains(AppConstant.TRUE)) {
//                listItem = entryDetails.split(AppConstant.TRUE)[0];
//                isStrikeOut = true;
//            } else if(entryDetails.contains(AppConstant.FALSE)){
//                listItem = entryDetails.split(AppConstant.FALSE)[0];
//                isStrikeOut = false;
//            }
//
//            final CheckBox checkBox = new CheckBox(mContext);
//            checkBox.setChecked(isStrikeOut);
//            checkBox.setEnabled(false);
//            final EditText textBox = new EditText(mContext);
//            textBox.setText(listItem);
//            textBox.setEnabled(false);
//            textBox.setBackgroundColor(Color.TRANSPARENT);
//
//            if (isStrikeOut) {
//                textBox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            }
//
//            mListItem.addView(checkBox);
//            mListItem.addView(textBox);
//            mTextBoxes.add(textBox);
//            addView(mListItem);
//        }
//    }
    public void addNewCheckbox() {
        mListItem = new LinearLayout(mContext);
        mListItem.setOrientation(HORIZONTAL);
        final ImageView deleteImageView = new ImageView(mContext);
        deleteImageView.setImageResource(android.R.drawable.ic_menu_delete);

        final EditText textBox = new EditText(mContext);
        textBox.setBackgroundColor(Color.TRANSPARENT);
        textBox.setHint("....");
        textBox.setSelection(0);
        textBox.requestFocus();
        final CheckBox checkBox = new CheckBox(mContext);
        checkBox.setPadding(10, 10, 10, 10);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textBox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    textBox.setPaintFlags(checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });
        mListItem.addView(deleteImageView);
        mListItem.addView(checkBox);
        mListItem.addView(textBox);
        mTextBoxes.add(textBox);
        addView(mListItem);

        deleteImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout) v.getParent();
                mTextBoxes.remove(mTextBoxes.indexOf(layout.getChildAt(2)));
                layout.removeAllViews();
            }
        });
    }

    public String getLists() {

        String stringToReturn = "";
        boolean isStrikeThrough;
        int itemsCount = mTextBoxes.size();

        for (int i = 0; i < itemsCount; i++) {
            EditText editText = mTextBoxes.get(i);
            if (editText.getPaintFlags() == Paint.STRIKE_THRU_TEXT_FLAG)
                isStrikeThrough = true;
            else
                isStrikeThrough = false;
            if (editText.getText().toString().length() > 0)
                stringToReturn = stringToReturn + editText.getText().toString() + "$" + isStrikeThrough + "%";
        }

        return stringToReturn;
    }

}
/*

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder> {

    private LayoutInflater mInflater;
    private List<Note> mNotes = Collections.emptyList();
    private Context mContext;

    public NotesAdapter(Context mContext, List<Note> mNotes) {
        mInflater = LayoutInflater.from(mContext);
        this.mNotes = mNotes;
        this.mContext = mContext;
    }

    // Called when the RecyclerView needs a new RecyclerView.ViewHolder (NoteHolder)
    // to represent an item.  We inflate the XML layout and return our view (NoteHolder)
    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custom_notes_adapter_layout, parent, false);
        return new NoteHolder(view);
    }

    // Called by RecyclerView to display the data (a note) at the specified position.
    // This method needs to update the contents of the view to reflect the item at the
    // given position e.g. we are updating the view here with the data
    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        holder.mId.setText(mNotes.get(position).getId() + "");
        holder.mTitle.setText(mNotes.get(position).getTitle());
        // We have to deal with a note or a note list here, so
        // check for both types and process accordingly
       //
            NoteCustomList noteCustomList = new NoteCustomList(mContext);
            noteCustomList.setUpForHomeAdapter(mNotes.get(position));
            holder.mLinearLayout.removeAllViews();
            holder.mLinearLayout.addView(noteCustomList);
            holder.mDescription.setVisibility(View.GONE);
        } else {
            // Not a list note, so hide it.
            holder.mLinearLayout.setVisibility(View.GONE);

        }



    }


    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public void setData(List<Note> notes) {
        this.mNotes = notes;
    }

    public void delete(int position) {
        mNotes.remove(position);
        notifyItemRemoved(position);
    }

    // This will be called when an image is obtained from
    // Google Drive or Dropbox, as it will happen at a timeframe
    // after the non image data has been changed.  So we need to
    // notify the recyclerview of a change of data
    public void notifyImageObtained() {
        notifyDataSetChanged();
    }

    // Simple nested class that holds the various view components for the adapter
    // and as specified in custom_notes_adapter_layout.xml which we just created
    public class NoteHolder extends RecyclerView.ViewHolder {

        TextView mTitle, mDescription, mDate, mId;
        ImageView mImage;
        LinearLayout mLinearLayout;

        public NoteHolder(View itemView) {
            super(itemView);
            mId = (TextView) itemView.findViewById(R.id.id_note_custom_home);
            mTitle = (TextView) itemView.findViewById(R.id.title_note_custom_home);
            mDescription = (TextView) itemView.findViewById(R.id.description_note_custom_home);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.home_list);
            mDate = (TextView) itemView.findViewById(R.id.date_time_note_custom_home);
            mImage = (ImageView) itemView.findViewById(R.id.image_note_custom_home);
        }
    }

}
 */
