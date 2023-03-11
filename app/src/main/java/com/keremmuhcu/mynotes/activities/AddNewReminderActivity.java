package com.keremmuhcu.mynotes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.keremmuhcu.mynotes.R;
import com.keremmuhcu.mynotes.database.MyNoteDatabase;
import com.keremmuhcu.mynotes.databinding.ActivityAddNewReminderBinding;
import com.keremmuhcu.mynotes.entities.MyReminderEntities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNewReminderActivity extends AppCompatActivity {
    private ActivityAddNewReminderBinding binding;
    private EditText title;
    private TextView textDateTime, saveReminder;
    private View viewReminder;
    private String selectedReminderColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewReminderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        saveReminder = binding.saveReminder;
        viewReminder = binding.viewReminder;
        title = binding.inputNoteTitle;
        textDateTime = binding.textDateTime;

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        selectedReminderColor = "#FF937B";
        saveReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReminders();
            }
        });

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );

        bottomSheet();
        setViewColor();
    }

    private void saveReminders() {
        if (title.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note Title Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        final MyReminderEntities myReminderEntities = new MyReminderEntities();
        myReminderEntities.setTitle(title.getText().toString());
        myReminderEntities.setDateTime(textDateTime.getText().toString());
        myReminderEntities.setColor(selectedReminderColor);

        class SaveReminder extends AsyncTask<Void,Void,Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                MyNoteDatabase
                        .getMyNoteDatabase(getApplicationContext())
                        .notesDao()
                        .insertReminder(myReminderEntities);
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
        new SaveReminder().execute();

    }

    private void setViewColor() {
        GradientDrawable gradientDrawable1 = (GradientDrawable) viewReminder.getBackground();
        gradientDrawable1.setColor(Color.parseColor(selectedReminderColor));
    }

    private void bottomSheet() {

        final LinearLayout linearLayout = findViewById(R.id.bottomLayoutReminder);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        linearLayout.findViewById(R.id.bottomReminderText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        final ImageView imgColor1 = linearLayout.findViewById(R.id.imgColor1);
        final ImageView imgColor2 = linearLayout.findViewById(R.id.imgColor2);
        final ImageView imgColor3 = linearLayout.findViewById(R.id.imgColor3);
        final ImageView imgColor4 = linearLayout.findViewById(R.id.imgColor4);

        //color1
        linearLayout.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedReminderColor = "#FF937B";
                imgColor1.setImageResource(R.drawable.baseline_done_24);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                setViewColor();
            }
        });

        //color2
        linearLayout.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedReminderColor = "#FFFB7B";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(R.drawable.baseline_done_24);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                setViewColor();
            }
        });

        //color3
        linearLayout.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedReminderColor = "#ADFF7B";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(R.drawable.baseline_done_24);
                imgColor4.setImageResource(0);
                setViewColor();
            }
        });

        //color4
        linearLayout.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedReminderColor = "#96FFEA";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(R.drawable.baseline_done_24);
                setViewColor();
            }
        });
    }
}