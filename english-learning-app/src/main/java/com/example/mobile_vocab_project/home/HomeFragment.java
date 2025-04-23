package com.example.mobile_vocab_project.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.PopupWindow;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_vocab_project.MainActivity;
import com.example.mobile_vocab_project.QuizActivity;
import com.example.mobile_vocab_project.R;
import com.example.mobile_vocab_project.vocab.VocabDatabase;
import com.example.mobile_vocab_project.vocab.VocabEntity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private EditText etSearch;
    private PopupWindow suggestionPopup;
    private RecyclerView popupRecycler;
    private SuggestionAdapter suggestionAdapter;
    private final List<VocabEntity> suggestions = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearch = view.findViewById(R.id.etSearch);

        // Grid setup
        RecyclerView gridRecyclerView = view.findViewById(R.id.gridResources);
        gridRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        List<ResourceItem> resourceList = new ArrayList<>();
        resourceList.add(new ResourceItem(R.drawable.vocabulary_icon, getString(R.string.resource_vocab)));
        resourceList.add(new ResourceItem(R.drawable.quiz_icon, getString(R.string.resource_quiz)));
        resourceList.add(new ResourceItem(R.drawable.gramar_icon, getString(R.string.resource_grammar)));
        resourceList.add(new ResourceItem(R.drawable.dictionary_icon, getString(R.string.resource_dict)));
        resourceList.add(new ResourceItem(R.drawable.tips_icon, getString(R.string.resource_tips)));
        resourceList.add(new ResourceItem(R.drawable.ic_info_big, getString(R.string.resource_about)));


        ResourceGridAdapter adapter = new ResourceGridAdapter(requireContext(), resourceList, item -> {
            switch (item.getLabel()) {
                case "Vocabulary":
                    startActivity(new Intent(requireContext(), MainActivity.class));
                    break;

                case "Quiz":
                    startActivity(new Intent(requireContext(), QuizActivity.class)); // Replace with your actual quiz activity
                    break;

                case "About":
                    showAboutDialog();
                    break;

                default:
                    // You can add more cases here for other resources
                    break;
            }
        });
        gridRecyclerView.setAdapter(adapter);

        // Popup suggestion list setup
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_suggestion, null);
        popupRecycler = popupView.findViewById(R.id.rvPopupSuggestions);
        popupRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        suggestionAdapter = new SuggestionAdapter(suggestions, vocab -> {
            openWordDetail(vocab);
            if (suggestionPopup != null && suggestionPopup.isShowing()) {
                suggestionPopup.dismiss();
            }
        });
        popupRecycler.setAdapter(suggestionAdapter);

        suggestionPopup = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        suggestionPopup.setOutsideTouchable(true);
        suggestionPopup.setElevation(8f);
        suggestionPopup.setFocusable(false);

        // Live search listener
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();
                if (input.length() >= 1) {
                    List<VocabEntity> result = VocabDatabase.getInstance(requireContext())
                            .vocabDao()
                            .searchSuggestions(input);
                    suggestions.clear();
                    suggestions.addAll(result);
                    suggestionAdapter.notifyDataSetChanged();

                    if (!suggestionPopup.isShowing() && etSearch.isFocused()) {
                        suggestionPopup.setWidth(etSearch.getWidth());
                        suggestionPopup.showAsDropDown(etSearch, 0, 8, Gravity.START);
                    } else if (suggestionPopup.isShowing()) {
                        suggestionPopup.update(etSearch, etSearch.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                    }

                    if (suggestions.isEmpty() && suggestionPopup.isShowing()) {
                        suggestionPopup.dismiss();
                    }

                } else {
                    suggestions.clear();
                    suggestionAdapter.notifyDataSetChanged();
                    if (suggestionPopup.isShowing()) {
                        suggestionPopup.dismiss();
                    }
                }
            }

            @Override public void afterTextChanged(Editable s) {}
        });

        // Close popup on keyboard action
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE && suggestionPopup.isShowing()) {
                suggestionPopup.dismiss();
            }
            return false;
        });
    }

    // Called when a suggestion is tapped
    private void openWordDetail(VocabEntity vocab) {
        WordDetailActivity.start(requireContext(), vocab);
    }

    // Show About dialog like the info icon
    private void showAboutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("About SVEnglish")
                .setMessage("SVEnglish is a vocabulary learning app designed to make English fun and effective.\n\nDeveloped by you 👌")
                .setPositiveButton("OK", null)
                .show();
    }
}
