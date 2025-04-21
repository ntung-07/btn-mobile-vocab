package com.example.mobile_vocab_project;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {VocabEntity.class}, version = 1)
public abstract class VocabDatabase extends RoomDatabase {

    private static VocabDatabase INSTANCE;

    public abstract VocabDao vocabDao();

    public static synchronized VocabDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            VocabDatabase.class, "vocab_database")
                    .allowMainThreadQueries() // okay for small apps; later use async
                    .fallbackToDestructiveMigration()
                    .addCallback(new Callback() {

                    })
                    .build();
        }
        return INSTANCE;
    }
}
