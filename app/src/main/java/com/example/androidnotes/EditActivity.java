package com.example.androidnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class EditActivity extends AppCompatActivity {

    private EditText title;
    private EditText description;
    private Notes currentNote;
    private Integer pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        title = findViewById(R.id.titleEdit);
        description = findViewById(R.id.descriptionEdit);
        description.setMovementMethod(new ScrollingMovementMethod());


        Intent intent = getIntent();
        if (intent.hasExtra("CURRENT_NOTE")) {
            currentNote = (Notes) intent.getSerializableExtra("CURRENT_NOTE");
            title.setText(currentNote.getTitle());
            description.setText(currentNote.getDescription());
            pos = intent.getIntExtra("CURRENT_POS", -1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editactivitymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveNote) {
            if (!title.getText().toString().trim().isEmpty()) { // if title not empty
                if (currentNote == null) {
                    String t = title.getText().toString();
                    String d = description.getText().toString();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d, hh:mm aa", Locale.US);
                    String tm = simpleDateFormat.format(new Date());

                    Notes newNote = new Notes(t, d, tm);
                    Intent intent = new Intent();
                    intent.putExtra("NOTES", newNote);
                    setResult(RESULT_OK, intent);
                    finish();
                    return true;
                } else {

                    if (title.getText().toString().equals(currentNote.getTitle()) &&
                            description.getText().toString().equals(currentNote.getDescription())) {
                        finish();
                        return true;
                    } else {
                        String t = title.getText().toString();
                        String d = description.getText().toString();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d, hh:mm aa", Locale.US);

                        String tm = simpleDateFormat.format(new Date());

                        Notes updatedNote = new Notes(t, d, tm);
                        Intent intent = new Intent();
                        intent.putExtra("NOTES", updatedNote);
                        intent.putExtra("CURRENT_POS", pos);
                        setResult(RESULT_OK, intent);
                        finish();
                        return true;
                    }
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Your note is missing a title and will not be saved!");
                builder.setPositiveButton("OK", (dialogInterface, i) -> {
                    Intent data = new Intent();
                    setResult(RESULT_CANCELED, data);
                    finish();
                });

                builder.setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.cancel());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        if (!title.getText().toString().trim().isEmpty()) {      // if title not empty

            if (currentNote == null) {                 // new note
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Your note is not saved! Save note " + title.getText().toString() + "?");
                builder.setPositiveButton("YES", (dialogInterface, i) -> {
                    String t = title.getText().toString();
                    String d = description.getText().toString();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d, hh:mm aa", Locale.US);
                    String tm = simpleDateFormat.format(new Date());

                    Notes newNote = new Notes(t, d, tm);
                    Intent intent = new Intent();
                    intent.putExtra("NOTES", newNote);
                    setResult(RESULT_OK, intent);
                    finish();

                });
                builder.setNegativeButton("NO", (dialogInterface, i) -> {
                    Intent data = new Intent();
                    setResult(RESULT_CANCELED, data);
                    finish();
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {            // existing note

                if (title.getText().toString().equals(currentNote.getTitle()) &&
                        description.getText().toString().equals(currentNote.getDescription())) { // no changes made
                    finish();
                } else {                       // updating existing note
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Your note is not saved! Save note " + title.getText().toString() + "?");
                    builder.setPositiveButton("YES", (dialogInterface, i) -> {
                        String t = title.getText().toString();
                        String d = description.getText().toString();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d, hh:mm aa", Locale.US);

                        String tm = simpleDateFormat.format(new Date());

                        Notes updatedNote = new Notes(t, d, tm);
                        Intent intent = new Intent();
                        intent.putExtra("NOTES", updatedNote);
                        intent.putExtra("CURRENT_POS", pos);
                        setResult(RESULT_OK, intent);
                        finish();
                    });
                    builder.setNegativeButton("NO", (dialogInterface, i) -> {
                        Intent data = new Intent();
                        setResult(RESULT_CANCELED, data);
                        finish();
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    }

                }
        } else {// no title
            if (description.getText().toString().isEmpty()) { // empty title and description
                Intent data = new Intent();
                setResult(RESULT_CANCELED, data);
                finish();
            } else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Your note is missing a title and will not be saved!");
                builder.setPositiveButton("OK", (dialogInterface, i) -> {
                    Intent data = new Intent();
                    setResult(RESULT_CANCELED, data);
                    finish();
                });

                builder.setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.cancel());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
            }
    }
}
