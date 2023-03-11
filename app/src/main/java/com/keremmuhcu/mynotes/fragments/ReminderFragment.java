package com.keremmuhcu.mynotes.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.keremmuhcu.mynotes.activities.AddNewReminderActivity;
import com.keremmuhcu.mynotes.adapters.MyReminderAdapter;
import com.keremmuhcu.mynotes.database.MyNoteDatabase;
import com.keremmuhcu.mynotes.databinding.FragmentReminderBinding;
import com.keremmuhcu.mynotes.entities.MyReminderEntities;

import java.util.ArrayList;
import java.util.List;

public class ReminderFragment extends Fragment {
    private FragmentReminderBinding binding;
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    ImageView addReminder;
    private RecyclerView reminderRecycler;
    private List<MyReminderEntities> reminderEntitiesList;
    private MyReminderAdapter myReminderAdapter;

    public ReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReminderBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        addReminder = binding.addReminderImgView;
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddNewReminderActivity.class),REQUEST_CODE_ADD_NOTE);
            }
        });

        reminderRecycler = binding.reminderRec;
        reminderRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        reminderEntitiesList = new ArrayList<>();
        myReminderAdapter = new MyReminderAdapter(reminderEntitiesList);
        reminderRecycler.setAdapter(myReminderAdapter);

        getAllReminders();

        return view;
    }

    private void getAllReminders() {
        class GetReminderTask extends AsyncTask<Void,Void,List<MyReminderEntities>> {

            @Override
            protected List<MyReminderEntities> doInBackground(Void... voids) {
                return MyNoteDatabase.getMyNoteDatabase(getActivity().getApplicationContext()).notesDao().getAllReminders();
            }

            @Override
            protected void onPostExecute(List<MyReminderEntities> myReminderEntities) {
                super.onPostExecute(myReminderEntities);
                if (reminderEntitiesList.size() == 0) {
                    reminderEntitiesList.addAll(myReminderEntities);
                    myReminderAdapter.notifyDataSetChanged();
                } else {
                    reminderEntitiesList.add(0,myReminderEntities.get(0));
                    myReminderAdapter.notifyItemInserted(0);
                }

                reminderRecycler.smoothScrollToPosition(0);
            }
        }

        new GetReminderTask().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getAllReminders();
        }
    }
}