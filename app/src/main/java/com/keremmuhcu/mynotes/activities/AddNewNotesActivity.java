package com.keremmuhcu.mynotes.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.keremmuhcu.mynotes.R;
import com.keremmuhcu.mynotes.database.MyNoteDatabase;
import com.keremmuhcu.mynotes.databinding.ActivityAddNewNotesBinding;
import com.keremmuhcu.mynotes.entities.MyNoteEntities;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNewNotesActivity extends AppCompatActivity {
    private ActivityAddNewNotesBinding binding;
    private EditText inputNoteTitle, inputNoteText;
    private TextView textDateTime, saveNote;
    private View indicator1, indicator2;
    String selectedColor;
    ImageView addImage;
    private String imagePath;
    private MyNoteEntities alreadyAvailableNote;
    public static final int STORAGE_PERMISSION = 1;
    public static final int SELECT_IMG = 1;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewNotesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        indicator1 = binding.viewLeftIndicator;
        indicator2 = binding.viewRightIndicator;
        saveNote = binding.saveNote;
        inputNoteText = binding.inputEditText;
        inputNoteTitle = binding.inputNoteTitle;
        textDateTime = binding.textDateTime;
        addImage = binding.imageNote;

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selectedColor = "#FF937B";
        imagePath = "";

        if (getIntent().getBooleanExtra("updateOrView", false)) {
            alreadyAvailableNote = (MyNoteEntities) getIntent().getSerializableExtra("myNotes");
            setViewUpdate();
        }

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotes();
            }
        });

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );

        findViewById(R.id.imageRemove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage.setImageBitmap(null);
                addImage.setVisibility(View.GONE);
                findViewById(R.id.imageRemove).setVisibility(View.GONE);

                imagePath = "";
            }
        });

        bottomSheet();
        setViewColor();

    }

    private void setViewUpdate() {
        inputNoteTitle.setText(alreadyAvailableNote.getTitle());
        inputNoteText.setText(alreadyAvailableNote.getNoteText());
        textDateTime.setText(alreadyAvailableNote.getDateTime());

        if (alreadyAvailableNote.getImagePath() != null && !alreadyAvailableNote.getImagePath().trim().isEmpty()) {
            addImage.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath()));
            addImage.setVisibility(View.VISIBLE);
            findViewById(R.id.imageRemove).setVisibility(View.VISIBLE);
            imagePath = alreadyAvailableNote.getImagePath();
        }
    }

    private void setViewColor() {
        GradientDrawable gradientDrawable1 = (GradientDrawable) indicator1.getBackground();
        gradientDrawable1.setColor(Color.parseColor(selectedColor));

        GradientDrawable gradientDrawable2 = (GradientDrawable) indicator2.getBackground();
        gradientDrawable2.setColor(Color.parseColor(selectedColor));
    }

    private void saveNotes() {
        if (inputNoteTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note Title Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputNoteText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note Text Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        final MyNoteEntities myNoteEntities = new MyNoteEntities();
        myNoteEntities.setTitle(inputNoteTitle.getText().toString());
        myNoteEntities.setNoteText(inputNoteText.getText().toString());
        myNoteEntities.setDateTime(textDateTime.getText().toString());
        myNoteEntities.setColor(selectedColor);
        myNoteEntities.setImagePath(imagePath);

        if (alreadyAvailableNote != null) {
            myNoteEntities.setId(alreadyAvailableNote.getId());
        }

        class SaveNotes extends AsyncTask<Void,Void,Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                MyNoteDatabase
                        .getMyNoteDatabase(getApplicationContext())
                        .notesDao()
                        .insertShoppingList(myNoteEntities);

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
        new SaveNotes().execute();
    }

    private  void bottomSheet() {
        final LinearLayout linearLayout = findViewById(R.id.bottomLayout);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        linearLayout.findViewById(R.id.bottomText).setOnClickListener(new View.OnClickListener() {
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
        final ImageView imgColor5 = linearLayout.findViewById(R.id.imgColor5);
        final ImageView imgColor6 = linearLayout.findViewById(R.id.imgColor6);

        //color1
        linearLayout.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = "#FF937B";
                imgColor1.setImageResource(R.drawable.baseline_done_24);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                imgColor6.setImageResource(0);
                setViewColor();
            }
        });

        //color2
        linearLayout.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = "#FFFB7B";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(R.drawable.baseline_done_24);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                imgColor6.setImageResource(0);
                setViewColor();
            }
        });

        //color3
        linearLayout.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = "#ADFF7B";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(R.drawable.baseline_done_24);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                imgColor6.setImageResource(0);
                setViewColor();
            }
        });

        //color4
        linearLayout.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = "#96FFEA";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(R.drawable.baseline_done_24);
                imgColor5.setImageResource(0);
                imgColor6.setImageResource(0);
                setViewColor();
            }
        });

        //color5
        linearLayout.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = "#969CFF";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(R.drawable.baseline_done_24);
                imgColor6.setImageResource(0);
                setViewColor();
            }
        });

        //color6
        linearLayout.findViewById(R.id.viewColor6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = "#FF96F5";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                imgColor6.setImageResource(R.drawable.baseline_done_24);
                setViewColor();
            }
        });

        if (alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null && !alreadyAvailableNote.getColor().trim().isEmpty()) {

            switch (alreadyAvailableNote.getColor()) {
                case "#FFFB7B":
                    linearLayout.findViewById(R.id.viewColor2).performClick();
                    break;
                case "#ADFF7B":
                    linearLayout.findViewById(R.id.viewColor3).performClick();
                    break;
                case "#96FFEA":
                    linearLayout.findViewById(R.id.viewColor4).performClick();
                    break;
                case "#969CFF":
                    linearLayout.findViewById(R.id.viewColor5).performClick();
                    break;
                case "#FF96F5":
                    linearLayout.findViewById(R.id.viewColor6).performClick();
                    break;
            }
        }

        //Add Image
        linearLayout.findViewById(R.id.addImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddNewNotesActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
                } else {
                    selectYourImage();
                }
            }
        });

        if (alreadyAvailableNote != null) {
            linearLayout.findViewById(R.id.removeNote).setVisibility(View.VISIBLE);
            linearLayout.findViewById(R.id.removeNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showDeleteDialog();
                }
            });
        }

    }

    private void showDeleteDialog() {
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddNewNotesActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_delete_note, (ViewGroup) findViewById(R.id.layoutDeleteNote_Container));
            builder.setView(view);
            alertDialog = builder.create();

            if (alertDialog. getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    class DeleteNoteTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            MyNoteDatabase.getMyNoteDatabase(getApplicationContext()).notesDao().deleteNotes(alreadyAvailableNote);

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void unused) {
                            super.onPostExecute(unused);

                            Intent intent = new Intent();
                            intent.putExtra("isNoteDeleted", true);
                            setResult(RESULT_OK, intent);
                            finish();

                        }
                    }

                    new DeleteNoteTask().execute();
                }
            });

            view.findViewById(R.id.textCancelNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }

        alertDialog.show();
    }

    private void selectYourImage() {
        Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentToGallery, SELECT_IMG);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION && grantResults.length > 0) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectYourImage();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMG && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectImageUri = data.getData();
                if (selectImageUri != null) {

                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        addImage.setImageBitmap(bitmap);
                        addImage.setVisibility(View.VISIBLE);
                        imagePath = getPathFromUri(selectImageUri);
                        findViewById(R.id.imageRemove).setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }
    }

    private String getPathFromUri(Uri uri) {
        String filePath;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor == null) {
            filePath = uri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return  filePath;
    }
}