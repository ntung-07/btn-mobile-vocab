package com.example.mobile_vocab_project.sync;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import android.util.Log;

import com.example.mobile_vocab_project.vocab.VocabDao;
import com.example.mobile_vocab_project.vocab.VocabDatabase;
import com.example.mobile_vocab_project.vocab.VocabEntity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncUtils {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }
    public static void syncRoomWithFireStore(Context context) {
        VocabDao dao = VocabDatabase.getInstance(context).vocabDao();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        List<VocabEntity> localVocabList = dao.getAll();

        try {
            if (localVocabList.isEmpty()) {
                // Then pull from Firestore -> Room
                firestore.collection("vocab_items").get()
                        .addOnSuccessListener(querySnapshot -> {
                            for (var doc : querySnapshot.getDocuments()) {
                                String term = doc.getString("term");
                                String def = doc.getString("def");
                                String ipa = doc.getString("ipa");

                                VocabEntity vocab = new VocabEntity(term, def, ipa);
                                dao.insert(vocab);
                            }
                            Log.d("SyncUtils", "Pulled from Firestore to Room");
                        })
                        .addOnFailureListener(e -> Log.e("SyncUtils", "Failed to download from Firestore", e));
            } else {
                // Then push from Room -> Firestore
                for (VocabEntity vocab : localVocabList) {
                    Map<String, Object> vocabMap = new HashMap<>();
                    vocabMap.put("term", vocab.term);
                    vocabMap.put("def", vocab.def);
                    vocabMap.put("ipa", vocab.ipa);

                    firestore.collection("vocab_items")
                            .document(vocab.term)
                            .set(vocabMap, SetOptions.merge())
                            .addOnSuccessListener(unused -> Log.d("SyncUtils", "Uploaded: " + vocab.term))
                            .addOnFailureListener(e -> Log.d("SyncUtils", "Failed to upload: " + vocab.term, e));
                }
            }
        } catch (Exception e) {
            Log.e("SyncUtils", "Sync failed (maybe offline): " + e.getMessage());
        }
    }
}
