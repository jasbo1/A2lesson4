package com.e.lesson_4;

import android.app.Application;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.e.lesson_4.room.AppDatabase;




public class App extends Application {
    public static AppDatabase database;
    @Override
    public void onCreate() {
        super.onCreate();
        database= Room.databaseBuilder(this,AppDatabase.class,"database")
                .allowMainThreadQueries().build();
    }

    public static AppDatabase getDatabase() {
        return database;
    }


}
