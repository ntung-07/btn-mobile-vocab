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

    public VocabFragment() {
        // Required empty constructor for DialogFragment
    }

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

        if (getArguments() != null) {
            vocab = (VocabEntity) getArguments().getSerializable("vocab");
        }

        TextView termTextView = rootView.findViewById(R.id.termTextView);
        TextView defTextView = rootView.findViewById(R.id.defTextView);
        TextView ipaTextView = rootView.findViewById(R.id.ipaTextView);
        EditText exampleEditText = rootView.findViewById(R.id.exampleEditText);
        Button addExampleButton = rootView.findViewById(R.id.addExampleButton);
        exampleListTextView = rootView.findViewById(R.id.exampleListTextView);

        sharedPreferences = requireContext().getSharedPreferences("examples", Context.MODE_PRIVATE);

        if (vocab != null) {
            termTextView.setText(vocab.term);
            defTextView.setText(getString(R.string.definition_prefix) + " " + vocab.def);
            ipaTextView.setText(vocab.ipa);
            showExamples();  // Load any saved examples
        }

        addExampleButton.setOnClickListener(v -> {
            String newExample = exampleEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(newExample)) {
                saveExample(newExample);
                exampleEditText.setText("");
                showExamples();
                Toast.makeText(getContext(), "Example added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please enter an example sentence", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isAdded()) {
            showExamples();
        }
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
        Log.d("VocabFragment", "Saved new example: " + sentence);
    }

    private void showExamples() {
        if (!isAdded() || vocab == null) {
            Log.e("VocabFragment", "Fragment not attached or vocab is null");
            return;
        }

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
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
}
