package com.anshdeep.queasynotes;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by ANSHDEEP on 03-04-2016.
 */
public class Archive extends SugarRecord implements Serializable {
    String title,note;
    long time;


    public Archive(){

    }

    public Archive(String title, String note, long time) {
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
