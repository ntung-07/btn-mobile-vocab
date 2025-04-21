package com.example.mobile_vocab_project.vocab;

import java.io.Serializable;

public class Vocab implements Serializable {
    public String term;
    public String def;
    public String ipa;

    public Vocab(String term, String def, String ipa) {
        this.term = term;
        this.def = def;
        this.ipa = ipa;
    }
}
