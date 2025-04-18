package com.example.vocab;

import com.example.vocab.Vocab;
import java.io.Serializable;
import java.util.List;

public class Topic implements Serializable {
    public String name;
    public List<Vocab> vocabList;

    public Topic(String name, List<Vocab> vocabList) {
        this.name = name;
        this.vocabList = vocabList;
    }
}
