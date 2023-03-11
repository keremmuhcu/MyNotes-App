package com.keremmuhcu.mynotes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.keremmuhcu.mynotes.entities.MyNoteEntities;
import com.keremmuhcu.mynotes.entities.MyReminderEntities;
import com.keremmuhcu.mynotes.entities.ShoppingListEntities;

import java.util.List;

@Dao
public interface MyNotesDao {

    //Notes
    @Query("SELECT * FROM note ORDER BY id DESC")
    List<MyNoteEntities> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertShoppingList(MyNoteEntities noteEntities);

    @Delete
    void deleteNotes(MyNoteEntities noteEntities);


    //Reminder
    @Query("SELECT * FROM reminder ORDER BY id DESC")
    List<MyReminderEntities> getAllReminders();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReminder(MyReminderEntities reminderEntities);

    @Delete
    void deleteReminder(MyReminderEntities reminderEntities);


    //ShoppingList
    @Query("SELECT * FROM shoppinglist ORDER BY id DESC")
    List<ShoppingListEntities> getAllShoppingList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertShoppingList(ShoppingListEntities shoppingListEntities);
}
