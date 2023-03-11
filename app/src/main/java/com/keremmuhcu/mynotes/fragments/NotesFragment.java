package com.keremmuhcu.mynotes.fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.keremmuhcu.mynotes.R;
import com.keremmuhcu.mynotes.activities.AddNewNotesActivity;
import com.keremmuhcu.mynotes.adapters.MyNoteAdapter;
import com.keremmuhcu.mynotes.database.MyNoteDatabase;
import com.keremmuhcu.mynotes.databinding.FragmentNotesBinding;
import com.keremmuhcu.mynotes.entities.MyNoteEntities;
import com.keremmuhcu.mynotes.listeners.MyNoteListeners;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment implements MyNoteListeners {
    private FragmentNotesBinding binding;
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int UPDATE_NOTE = 2;
    public static final int SHOW_NOTE = 3;
    private int clickedPosition = -1;
    private RecyclerView noteRecycler;
    private List<MyNoteEntities> noteEntitiesList;
    private MyNoteAdapter myNoteAdapter;
    ImageView addNotes;
    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        addNotes = binding.addNoteImgView;
        addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddNewNotesActivity.class),REQUEST_CODE_ADD_NOTE);
            }
        });

        noteRecycler = binding.noteRecNotesFragment;
        noteRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteEntitiesList = new ArrayList<>();
        myNoteAdapter = new MyNoteAdapter(noteEntitiesList, this);
        noteRecycler.setAdapter(myNoteAdapter);
        EditText inputSearch = binding.searchEditText;
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myNoteAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (noteEntitiesList.size() != 0) {
                    myNoteAdapter.searchNote(s.toString());
                }
            }
        });
        getAllNotes(SHOW_NOTE, false);
        return view;
    }

    private void getAllNotes(final int requestCode, final boolean isNoteDeleted) {

        @SuppressLint("StaticFieldLeak")
        class GetNoteTask extends AsyncTask<Void,Void,List<MyNoteEntities>> {

            @Override
            protected List<MyNoteEntities> doInBackground(Void... voids) {
                return MyNoteDatabase.getMyNoteDatabase(getActivity().getApplicationContext()).notesDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<MyNoteEntities> myNoteEntities) {
                super.onPostExecute(myNoteEntities);

                if (requestCode == SHOW_NOTE) {
                    noteEntitiesList.addAll(myNoteEntities);
                    myNoteAdapter.notifyDataSetChanged();
                } else if (requestCode == REQUEST_CODE_ADD_NOTE) {
                    noteEntitiesList.add(0, myNoteEntities.get(0));
                    myNoteAdapter.notifyItemInserted(0);
                    noteRecycler.smoothScrollToPosition(0);
                } else if (requestCode == UPDATE_NOTE) {
                    noteEntitiesList.remove(clickedPosition);
                    if (isNoteDeleted) {
                        myNoteAdapter.notifyItemRemoved(clickedPosition);
                    } else {
                        noteEntitiesList.add(clickedPosition, myNoteEntities.get(clickedPosition));
                        myNoteAdapter.notifyItemChanged(clickedPosition);
                    }
                }
            }
        }

        new GetNoteTask().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getAllNotes(REQUEST_CODE_ADD_NOTE, false);
        } else if (requestCode == UPDATE_NOTE && resultCode == RESULT_OK) {
            if (data != null) {
                getAllNotes(UPDATE_NOTE, data.getBooleanExtra("isNoteDeleted", false));
            }
        }
    }

    @Override
    public void myNoteClick(MyNoteEntities myNoteEntities, int position) {
        clickedPosition = position;
        Intent intent = new Intent(getContext().getApplicationContext(), AddNewNotesActivity.class);
        intent.putExtra("updateOrView", true);
        intent.putExtra("myNotes", myNoteEntities);
        startActivityForResult(intent, UPDATE_NOTE);
    }
}