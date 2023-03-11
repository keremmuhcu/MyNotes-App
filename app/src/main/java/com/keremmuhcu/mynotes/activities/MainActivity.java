package com.keremmuhcu.mynotes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.keremmuhcu.mynotes.R;
import com.keremmuhcu.mynotes.databinding.ActivityMainBinding;
import com.keremmuhcu.mynotes.fragments.NotesFragment;
import com.keremmuhcu.mynotes.fragments.ReminderFragment;
import com.keremmuhcu.mynotes.fragments.ShoppingListFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        chipNavigationBar = binding.bottomNavigationBar;
        chipNavigationBar.setItemSelected(R.id.home,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotesFragment()).commit();

        bottomMenu();
    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.home:
                        fragment = new NotesFragment();
                        break;
                    case R.id.reminder:
                        fragment = new ReminderFragment();
                        break;
                    case R.id.shopping_list:
                        fragment = new ShoppingListFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

            }
        });
    }
}