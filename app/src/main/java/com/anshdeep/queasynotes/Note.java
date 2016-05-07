package com.anshdeep.queasynotes;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by ANSHDEEP on 01-04-2016.
 */
public class Note extends SugarRecord implements Serializable {
    String title,note;
    long time;


    public Note(){

    }

    public Note(String title, String note, long time) {
        this.title = title;
        this.note = note;
        this.time = time;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }




    public void setNote(String note) {
        this.note = note;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
