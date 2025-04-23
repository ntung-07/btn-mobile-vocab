package com.example.mobile_vocab_project.vocab;

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
                        @Override
                        public void onCreate(@androidx.annotation.NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            new Thread(() -> {
                                VocabDao dao = INSTANCE.vocabDao();
                                dao.insert(new VocabEntity("Cat", "Mèo", "[kæt]"));
                                dao.insert(new VocabEntity("Tiger", "Hổ", "[ˈtaɪɡər]"));
                                dao.insert(new VocabEntity("Fish", "Cá", "[fɪʃ]"));
                                dao.insert(new VocabEntity("Bird", "Chim", "[bɜrd]"));
                                dao.insert(new VocabEntity("Elephant", "Voi", "[ˈɛlɪfənt]"));
                                dao.insert(new VocabEntity("Snake", "Rắn", "[sneɪk]"));
                                dao.insert(new VocabEntity("Monkey", "Khỉ", "[ˈmʌŋki]"));
                                dao.insert(new VocabEntity("Bear", "Gấu", "[bɛr]"));
                                dao.insert(new VocabEntity("Cow", "Bò", "[kaʊ]"));
                                dao.insert(new VocabEntity("Horse", "Ngựa", "[hɔrs]"));
                                dao.insert(new VocabEntity("Duck", "Vịt", "[dʌk]"));
                                dao.insert(new VocabEntity("Chicken", "Gà", "[ˈtʃɪkɪn]"));
                                dao.insert(new VocabEntity("Sheep", "Cừu", "[ʃiːp]"));
                                dao.insert(new VocabEntity("Goat", "Dê", "[ɡoʊt]"));
                                dao.insert(new VocabEntity("Wolf", "Sói", "[wʊlf]"));
                                dao.insert(new VocabEntity("Fox", "Cáo", "[fɑks]"));
                                dao.insert(new VocabEntity("Rabbit", "Thỏ", "[ˈræbɪt]"));
                                dao.insert(new VocabEntity("Frog", "Ếch", "[frɔɡ]"));
                                dao.insert(new VocabEntity("Ant", "Kiến", "[ænt]"));
                                dao.insert(new VocabEntity("Bee", "Ong", "[bi]"));
                            }).start();
                        }
                    })
                    .build();
        }
        return INSTANCE;
    }
}
