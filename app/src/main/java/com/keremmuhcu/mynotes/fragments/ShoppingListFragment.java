package com.keremmuhcu.mynotes.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.keremmuhcu.mynotes.activities.AddNewShoppingListActivity;
import com.keremmuhcu.mynotes.adapters.ShoppingListAdapter;
import com.keremmuhcu.mynotes.database.MyNoteDatabase;
import com.keremmuhcu.mynotes.databinding.FragmentShoppingListBinding;
import com.keremmuhcu.mynotes.entities.MyReminderEntities;
import com.keremmuhcu.mynotes.entities.ShoppingListEntities;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends Fragment {
    private FragmentShoppingListBinding binding;
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    ImageView addShoppingList;
    RecyclerView shoppingRecycler;
    List<ShoppingListEntities> shoppingEntitiesList;
    ShoppingListAdapter shoppingAdapter;
    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        addShoppingList = binding.addShoppingListImgView;
        addShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddNewShoppingListActivity.class),REQUEST_CODE_ADD_NOTE);
            }
        });

        shoppingRecycler = binding.shoppingRecycler;
        shoppingRecycler.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        shoppingEntitiesList = new ArrayList<>();
        shoppingAdapter = new ShoppingListAdapter(shoppingEntitiesList);
        shoppingRecycler.setAdapter(shoppingAdapter);
        
        getAllShoppingList();

        return view;
    }

    private void getAllShoppingList() {
        class GetShoppingTask extends AsyncTask<Void,Void,List<ShoppingListEntities>> {

            @Override
            protected List<ShoppingListEntities> doInBackground(Void... voids) {
                return MyNoteDatabase.getMyNoteDatabase(getActivity().getApplicationContext()).notesDao().getAllShoppingList();
            }

            @Override
            protected void onPostExecute(List<ShoppingListEntities> shoppingListEntities) {
                super.onPostExecute(shoppingListEntities);
                if (shoppingEntitiesList.size() == 0) {
                    shoppingEntitiesList.addAll(shoppingListEntities);
                    shoppingAdapter.notifyDataSetChanged();
                } else {
                    shoppingEntitiesList.add(0,shoppingListEntities.get(0));
                    shoppingAdapter.notifyItemInserted(0);
                }

                shoppingRecycler.smoothScrollToPosition(0);
            }
        }

        new GetShoppingTask().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getAllShoppingList();
        }
    }
}