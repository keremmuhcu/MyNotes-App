package com.keremmuhcu.mynotes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.keremmuhcu.mynotes.dao.MyNotesDao;
import com.keremmuhcu.mynotes.entities.MyNoteEntities;
import com.keremmuhcu.mynotes.entities.MyReminderEntities;
import com.keremmuhcu.mynotes.entities.ShoppingListEntities;

@Database(entities = {MyNoteEntities.class, MyReminderEntities.class, ShoppingListEntities.class}, version = 1, exportSchema = false)
public abstract class MyNoteDatabase extends RoomDatabase {
    private static MyNoteDatabase myNoteDatabase;

    public static synchronized MyNoteDatabase getMyNoteDatabase(Context context) {
        if (myNoteDatabase == null) {
            myNoteDatabase = Room.databaseBuilder(
                    context,
                    MyNoteDatabase.class,
                    "note db"
            ).build();
        }
        return myNoteDatabase;
    }

    public abstract MyNotesDao notesDao();
}
