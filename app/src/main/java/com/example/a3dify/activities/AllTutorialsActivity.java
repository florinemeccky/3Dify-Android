package com.example.a3dify.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3dify.R;
import com.example.a3dify.TutorialRepository;
import com.example.a3dify.adapters.TutorialAdapter;
import com.example.a3dify.models.Tutorial;
import java.util.List;

/*
 * AllTutorialsActivity
 * Shows every tutorial with a search bar and optional category filter.
 * Opened from HomeFragment "See All" button and from ExploreFragment category taps.
 *
 * Accepts optional Intent extra "category" to pre-filter the list.
 */
public class AllTutorialsActivity extends AppCompatActivity {

    private long lastClickTime = 0;
    private TutorialAdapter    adapter;
    private TutorialRepository repo;
    private String             categoryFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tutorials);

        repo = TutorialRepository.getInstance();

        // Check if launched with a category filter
        categoryFilter = getIntent().getStringExtra("category");

        // Set screen title
        TextView tvTitle = findViewById(R.id.tv_title);
        if (tvTitle != null) {
            tvTitle.setText(categoryFilter != null ? categoryFilter : "All Tutorials");
        }

        // Back arrow
        TextView tvBack = findViewById(R.id.tv_back);
        if (tvBack != null) tvBack.setOnClickListener(v -> finish());

        // Set up RecyclerView
        RecyclerView rv = findViewById(R.id.rv_all_tutorials);
        if (rv != null) {
            rv.setLayoutManager(new LinearLayoutManager(this));

            List<Tutorial> initial = categoryFilter != null
                ? repo.getByCategory(categoryFilter)
                : repo.getAll();

            adapter = new TutorialAdapter(this, initial);
            adapter.setOnItemClickListener(this::openTutorial);
            rv.setAdapter(adapter);
        }

        // Set up search
        EditText etSearch = findViewById(R.id.et_search);
        if (etSearch != null) {
            if (categoryFilter != null) {
                etSearch.setHint("Search in " + categoryFilter + "…");
            }
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
                @Override public void afterTextChanged(Editable s) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    List<Tutorial> base = categoryFilter != null
                        ? repo.getByCategory(categoryFilter)
                        : repo.getAll();
                    String q = s.toString().toLowerCase().trim();
                    List<Tutorial> filtered = new java.util.ArrayList<>();
                    for (Tutorial t : base) {
                        if (q.isEmpty()
                            || t.getTitle().toLowerCase().contains(q)
                            || t.getDifficulty().toLowerCase().contains(q)) {
                            filtered.add(t);
                        }
                    }
                    if (adapter != null) adapter.updateList(filtered);

                    TextView tvEmpty = findViewById(R.id.tv_empty);
                    if (tvEmpty != null) {
                        tvEmpty.setVisibility(filtered.isEmpty()
                            ? android.view.View.VISIBLE
                            : android.view.View.GONE);
                    }
                }
            });
        }
    }

    private void openTutorial(Tutorial tutorial) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime < 600) return;
        lastClickTime = currentTime;

        // Save as last viewed for Continue Learning
        getSharedPreferences("continue_learning", MODE_PRIVATE)
            .edit()
            .putString("last_tutorial_id",    tutorial.getTutorialId())
            .putString("last_tutorial_title", tutorial.getTitle())
            .apply();

        Intent intent = new Intent(this, TutorialDetailActivity.class);
        intent.putExtra("icon",        tutorial.getIcon());
        intent.putExtra("title",       tutorial.getTitle());
        intent.putExtra("category",    tutorial.getCategory());
        intent.putExtra("difficulty",  tutorial.getDifficulty());
        intent.putExtra("duration",    tutorial.getDuration());
        intent.putExtra("description", tutorial.getDescription());
        intent.putExtra("tutorialId",  tutorial.getTutorialId());
        startActivity(intent);
    }
}
