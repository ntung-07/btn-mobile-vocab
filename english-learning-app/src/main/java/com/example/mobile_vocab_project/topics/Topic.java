package com.example.mobile_vocab_project.topics;
import com.example.mobile_vocab_project.vocab.VocabEntity;
import java.io.Serializable;
import java.util.List;

public class Topic implements Serializable {
    public String name;
    public List<VocabEntity> vocabList;

    public Topic(String name, List<VocabEntity> vocabList) {
        this.name = name;
        this.vocabList = vocabList;
    }
}
