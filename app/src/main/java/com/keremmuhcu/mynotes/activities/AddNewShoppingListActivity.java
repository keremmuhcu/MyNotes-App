package com.keremmuhcu.mynotes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.keremmuhcu.mynotes.database.MyNoteDatabase;
import com.keremmuhcu.mynotes.databinding.ActivityAddNewShoppingListBinding;
import com.keremmuhcu.mynotes.entities.ShoppingListEntities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNewShoppingListActivity extends AppCompatActivity {
    private ActivityAddNewShoppingListBinding binding;
    private EditText inputNoteTitle, inputNoteText;
    private TextView textDateTime, saveShopping;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewShoppingListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        inputNoteText = binding.textList;
        inputNoteTitle = binding.inputNoteTitle;
        textDateTime = binding.textDateTime;
        saveShopping = binding.saveShopping;

        saveShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotes();
            }
        });

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );

    }

    private void saveNotes() {
        if (inputNoteTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note Title Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputNoteText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note Text Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        final ShoppingListEntities entities = new ShoppingListEntities();
        entities.setTitle(inputNoteTitle.getText().toString());
        entities.setNoteText(inputNoteText.getText().toString());
        entities.setDateTime(textDateTime.getText().toString());

        class SaveShoppingList extends AsyncTask<Void,Void,Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                MyNoteDatabase
                        .getMyNoteDatabase(getApplicationContext())
                        .notesDao()
                        .insertShoppingList(entities);

                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new SaveShoppingList().execute();
    }
}