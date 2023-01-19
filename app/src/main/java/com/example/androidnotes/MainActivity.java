package com.example.androidnotes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener{

    private static final String TAG = "MainActivity";
    private final ArrayList<Notes> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Notes currentNote;
    private NotesAdapter nAdapter;
    private TextView title;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.titleText);
        description = findViewById(R.id.descriptionText);
        recyclerView = findViewById(R.id.recycler);

        nAdapter = new NotesAdapter(notesList, this);
        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);

        notesList.clear();
        notesList.addAll(loadFile());
        setTitle("Android Notes (" + notesList.size() + ")");

    }

    public void handleResult(ActivityResult result){
        if (result == null || result.getData() == null){
            Log.d(TAG, "handleResult: NULL ActivityResult received");
            return;
        }
       Intent data = result.getData();
       if (result.getResultCode() == RESULT_OK){
           if(data.hasExtra("NOTES")){
               currentNote = (Notes) data.getSerializableExtra("NOTES");
                   if(data.hasExtra("CURRENT_POS")) {
                       int pos = data.getIntExtra("CURRENT_POS", -1);
                       notesList.remove(pos);
                       notesList.add(0, currentNote);
                       saveNote();
                       nAdapter.notifyDataSetChanged();

                   } else{
                       notesList.add(0, currentNote);
                       saveNote();
                       nAdapter.notifyDataSetChanged();
                   }
           }
       }
    }

    public void openEditActivity(){
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("Notes", currentNote);
        activityResultLauncher.launch(intent);
    }

    public void openAboutActivity(){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.aboutIcon){
            openAboutActivity();
            return true;
        } else if (item.getItemId() == R.id.newNote){
            openEditActivity();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private ArrayList<Notes> loadFile(){

        Log.d(TAG, "loadFile: Loading JSON File");
        ArrayList<Notes> notesList = new ArrayList<>();

        try{
            InputStream is = getApplicationContext().openFileInput("Notes.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String description = jsonObject.getString("description");
                String time = jsonObject.getString("time"); // not sure
                Notes note = new Notes(title, description, time);
                notesList.add(note);
                setTitle("Android Notes (" + notesList.size() + ")");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return notesList;
    }

    private void saveNote(){

        Log.d(TAG, "saveNote: Saving JSON File");

        try{

            FileOutputStream fos = getApplicationContext().
                    openFileOutput("Notes.json", Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(notesList);
            printWriter.close();
            fos.close();
            setTitle("Android Notes (" + notesList.size() + ")");

            Log.d(TAG, "saveNote: JSON:\n" + notesList.toString());

        } catch (Exception e){
            e.getStackTrace();
        }
    }

    @Override
    public void onClick(View v){
        int pos = recyclerView.getChildLayoutPosition(v);
        currentNote = notesList.get(pos);
        Intent modifyNoteIntent = new Intent(this, EditActivity.class);
        modifyNoteIntent.putExtra("CURRENT_NOTE", currentNote);
        modifyNoteIntent.putExtra("CURRENT_POS", pos);
        activityResultLauncher.launch(modifyNoteIntent);

    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        currentNote = notesList.get(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note '" + currentNote.getTitle() + "'?");
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            notesList.remove(currentNote);
            currentNote = null;
            nAdapter.notifyDataSetChanged();
            saveNote();
        });

        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }
}