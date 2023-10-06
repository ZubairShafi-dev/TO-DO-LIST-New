package com.example.to_do_list;

public class Modeltask {
    public Modeltask() {
    }

    boolean completed;
    String id,notes,title,when,who;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Modeltask(boolean completed, String id, String notes, String title, String when, String who) {
        this.completed = completed;
        this.id = id;
        this.notes = notes;
        this.title = title;
        this.when = when;
        this.who = who;
    }

    public boolean isCompleted() {
        return completed; // Return the completion status
    }

    public boolean setCompleted(boolean completed) {
        this.completed = completed; // Set the completion status
        return completed;
    }
}
