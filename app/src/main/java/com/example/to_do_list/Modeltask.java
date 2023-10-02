package com.example.to_do_list;

public class Modeltask {
    String title,notes,when,who;
    public  Modeltask() {
        // Required empty constructor for Firestore
    }

    public Modeltask(String title, String notes, String when, String who) {
        this.title = title;
        this.notes = notes;
        this.when = when;
        this.who = who;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }


}
