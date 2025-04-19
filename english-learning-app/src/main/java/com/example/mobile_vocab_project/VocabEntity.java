package com.example.mobile_vocab_project;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vocab_table")
public class VocabEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String term;
    public String def;
    public String ipa;

    public VocabEntity(String term, String def, String ipa) {
        this.term = term;
        this.def = def;
        this.ipa = ipa;
    }
}
