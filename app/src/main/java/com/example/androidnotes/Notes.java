package com.example.androidnotes;

import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notes implements Serializable {

    private String title;
    private String description;
    private String time;

    public Notes(String title, String description, String time){
        this.title = title;
        this.description = description;
        this.time = time;

    }

    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public String getTime(){return time;}


    @NonNull
    public String toString(){
        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.beginObject();
            jsonWriter.name("title").value(getTitle());
            jsonWriter.name("description").value(getDescription());
            jsonWriter.name("time").value(getTime());
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        } catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }
}
