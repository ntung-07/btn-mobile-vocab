package com.example.mobile_vocab_project;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VocabDao {

    @Insert
    void insert(VocabEntity vocab);

    @Query("SELECT * FROM vocab_table")
    List<VocabEntity> getAll();
}
