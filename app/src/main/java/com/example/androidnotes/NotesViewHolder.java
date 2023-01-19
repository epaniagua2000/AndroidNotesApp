package com.example.androidnotes;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class NotesViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView description;
    TextView time;


    NotesViewHolder(View v){
        super(v);
        title = v.findViewById(R.id.titleText);
        description = v.findViewById(R.id.descriptionText);
        time = v.findViewById(R.id.timeText);

    }
}
