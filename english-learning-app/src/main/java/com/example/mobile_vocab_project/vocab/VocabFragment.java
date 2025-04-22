package com.example.mobile_vocab_project.vocab;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Optional: make dialog use a light style
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog_Alert);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true); // Optional
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Optional
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vocab_layout, container, false);

        if (getArguments() != null) {
            vocab = (VocabEntity) getArguments().getSerializable("vocab");
        }

        TextView termTextView = rootView.findViewById(R.id.termTextView);
        TextView defTextView = rootView.findViewById(R.id.defTextView);
        TextView ipaTextView = rootView.findViewById(R.id.ipaTextView);
        exampleEditText = rootView.findViewById(R.id.exampleEditText);
        Button addExampleButton = rootView.findViewById(R.id.addExampleButton);
        exampleListTextView = rootView.findViewById(R.id.exampleListTextView);
        Button reloadButton = rootView.findViewById(R.id.reloadButton);
        sharedPreferences = requireContext().getSharedPreferences("examples", Context.MODE_PRIVATE);

        if (vocab != null) {
            termTextView.setText(vocab.term);
            defTextView.setText(getString(R.string.definition_prefix) + " " + vocab.def);
            ipaTextView.setText(vocab.ipa);
            showExamples();
        }

        addExampleButton.setOnClickListener(v -> {
            String newExample = exampleEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(newExample)) {
                saveExample(newExample);
                exampleEditText.setText("");
                showExamples();
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Example added!", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Please enter an example sentence", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (reloadButton != null) {
            reloadButton.setOnClickListener(v -> {
                Log.d("VocabFragment", "Reload clicked");
                showExamples();
            });
        }

        // Show keyboard on start
        exampleEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(exampleEditText, InputMethodManager.SHOW_IMPLICIT);

        return rootView;
    }

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

    private void showExamples() {
        if (!isAdded() || vocab == null) return;

        String key = "examples_" + vocab.def.toLowerCase();
        Set<String> examples = sharedPreferences.getStringSet(key, new HashSet<>());

        if (examples == null || examples.isEmpty()) {
            exampleListTextView.setText(getString(R.string.no_examples_yet));
        } else {
            StringBuilder builder = new StringBuilder(getString(R.string.saved_examples_label));
            for (String sentence : examples) {
                builder.append("- ").append(sentence).append("\n");
            }
            exampleListTextView.setText(builder.toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            // ✅ Clear focus-blocking flags
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

            // Optional: make background less dim if needed
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.dimAmount = 0.1f; // default is 0.5
            window.setAttributes(layoutParams);
            getDialog().getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }
}
