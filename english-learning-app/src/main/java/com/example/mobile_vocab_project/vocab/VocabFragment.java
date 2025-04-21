package com.example.mobile_vocab_project.vocab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mobile_vocab_project.R;
import com.example.mobile_vocab_project.VocabEntity;

import java.util.HashSet;
import java.util.Set;

public class VocabFragment extends DialogFragment {

    private VocabEntity vocab;
    private SharedPreferences sharedPreferences;
    private TextView exampleListTextView;
    private EditText exampleEditText;

    public VocabFragment() {}

    public static VocabFragment newInstance(VocabEntity vocab) {
        VocabFragment fragment = new VocabFragment();
        Bundle args = new Bundle();
        args.putSerializable("vocab", vocab);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.vocab_layout, container, false);

        // Retrieve vocab object passed to the fragment
        if (getArguments() != null) {
            vocab = (VocabEntity) getArguments().getSerializable("vocab");
        }

        // Initialize views
        TextView termTextView = rootView.findViewById(R.id.termTextView);
        TextView defTextView = rootView.findViewById(R.id.defTextView);
        TextView ipaTextView = rootView.findViewById(R.id.ipaTextView);
        exampleEditText = rootView.findViewById(R.id.exampleEditText);
        Button addExampleButton = rootView.findViewById(R.id.addExampleButton);
        exampleListTextView = rootView.findViewById(R.id.exampleListTextView);

        // Initialize shared preferences to store examples
        sharedPreferences = requireContext().getSharedPreferences("examples", Context.MODE_PRIVATE);

        // Set vocab data if available
        if (vocab != null) {
            termTextView.setText(vocab.term);
            defTextView.setText(getString(R.string.definition_prefix) + " " + vocab.term);
            ipaTextView.setText(vocab.ipa);
            showExamples(); // Load any saved examples
        }

        // Set up listener for the "Add Example" button
        addExampleButton.setOnClickListener(v -> {
            Log.d("VocabFragment", "Add Example Button Clicked");
            String newExample = exampleEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(newExample)) {
                saveExample(newExample);
                exampleEditText.setText(""); // Clear input field

                // Cập nhật lại danh sách ví dụ sau khi câu được thêm vào
                showExamples();  // Re-load the examples immediately after adding a new example

                // Kiểm tra nếu getContext() không phải là null
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Example added!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("VocabFragment", "Context is null, unable to show Toast");
                }
            } else {
                // Kiểm tra nếu getContext() không phải là null trước khi hiển thị Toast
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Please enter an example sentence", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("VocabFragment", "Context is null, unable to show Toast");
                }
            }
        });

        // Set up listener for the "Reload" button (Tải lại)
        Button reloadButton = rootView.findViewById(R.id.reloadButton);
        reloadButton.setOnClickListener(v -> {
            Log.d("VocabFragment", "Reload Button Clicked");
            showExamples();  // Reload the example list
        });

        // Request focus and show the soft keyboard when the fragment is displayed
        exampleEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(exampleEditText, InputMethodManager.SHOW_IMPLICIT);

        return rootView;
    }

    // Save the new example sentence to SharedPreferences
    private void saveExample(String sentence) {
        if (vocab == null) {
            Log.e("VocabFragment", "Vocab object is null, cannot save example.");
            return;
        }

        String key = "examples_" + vocab.def.toLowerCase();
        Set<String> oldSet = sharedPreferences.getStringSet(key, new HashSet<>());
        Set<String> newSet = new HashSet<>(oldSet);
        newSet.add(sentence);

        sharedPreferences.edit().putStringSet(key, newSet).apply();
    }

    // Show all saved examples for the current vocab
    private void showExamples() {
        if (!isAdded() || vocab == null) return;

        String key = "examples_" + vocab.def.toLowerCase();
        Set<String> examples = sharedPreferences.getStringSet(key, new HashSet<>());

        if (examples.isEmpty()) {
            exampleListTextView.setText(getString(R.string.no_examples_yet));
        } else {
            StringBuilder builder = new StringBuilder(getString(R.string.saved_examples_label));
            for (String ex : examples) {
                builder.append("- ").append(ex).append("\n");
            }
            exampleListTextView.setText(builder.toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
