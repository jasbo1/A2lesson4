package com.e.lesson_4.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.e.lesson_4.Task;

@Database(entities = {Task.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();

}
