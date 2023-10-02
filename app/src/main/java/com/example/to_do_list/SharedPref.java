package com.example.to_do_list;

import android.content.SharedPreferences;
import com.google.gson.Gson;

class SharedPref {
    private SharedPreferences mypref;
    private SharedPreferences.Editor editor;

    public SharedPref(SharedPreferences mypref) {
        this.mypref = mypref;
        this.editor = mypref.edit(); // Initialize the editor
    }


    public void setTask(Modeltask task) {
        editor.putString("task", new Gson().toJson(task)).apply();
    }

    public Modeltask getTask() {
        String taskJson = mypref.getString("task", "");
        if (!taskJson.isEmpty()) {
            return new Gson().fromJson(taskJson, Modeltask.class);
        }
        return null;
    }

    public void setUser(ModelUser user) {
        editor.putString("user", new Gson().toJson(user)).apply();
    }

    public ModelUser getUser() {
        String userJson = mypref.getString("user", "");
        if (!userJson.isEmpty()) {
            return new Gson().fromJson(userJson, ModelUser.class);
        }
        return null;
    }
}

