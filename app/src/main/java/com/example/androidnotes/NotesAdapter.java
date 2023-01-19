package com.example.androidnotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    private final ArrayList<Notes> notesList;
    private final MainActivity mainAct;

    NotesAdapter(ArrayList<Notes> notesList, MainActivity ma){
        this.notesList = notesList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_layout, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position){
        Notes note = notesList.get(position);


        if(note.getTitle().length() > 80){
            String t = (note.getTitle().substring(0, Math.min(note.getTitle().length(), 79))) ;
            holder.title.setText(t);
        }else{
            String t = note.getTitle() ;
            holder.title.setText(t);
        }

        if(note.getDescription().length() > 80){
            String d = (note.getDescription().substring(0, Math.min(note.getDescription().length(), 79))) + "...";
            holder.description.setText(d);}
        else{
            String d = note.getDescription();
            holder.description.setText(d);
        }

        holder.time.setText(note.getTime());
    }

    @Override
    public int getItemCount(){ return notesList.size();}
}
