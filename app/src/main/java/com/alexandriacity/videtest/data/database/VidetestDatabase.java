package com.alexandriacity.videtest.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CourseEntry.class, VideoEntry.class}, version = 1)
public abstract class VidetestDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "VidetestDatabase";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static VidetestDatabase sInstance;


    public static VidetestDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        VidetestDatabase.class, VidetestDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    // The associated DAOs for the VidetestDatabase
    public abstract CourseDao courseDao();
    public abstract VideoDao videoDao();
}