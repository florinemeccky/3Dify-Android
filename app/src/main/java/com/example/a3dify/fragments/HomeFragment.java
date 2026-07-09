package com.example.a3dify.fragments;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3dify.DatabaseHelper;
import com.example.a3dify.R;
import com.example.a3dify.TutorialRepository;
import com.example.a3dify.activities.AllTutorialsActivity;
import com.example.a3dify.activities.NotificationsActivity;
import com.example.a3dify.activities.TutorialDetailActivity;
import com.example.a3dify.adapters.CategoryAdapter;
import com.example.a3dify.adapters.TutorialAdapter;
import com.example.a3dify.models.Category;
import com.example.a3dify.models.Tutorial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private long lastClickTime = 0;
    private TutorialAdapter  featuredAdapter;
    private TutorialRepository repo;
    private RecyclerView     rvFeatured;
    private View             llNoResults;
    private String           selectedCategory = "All";

    // Category names matching TutorialRepository exactly
    private static final String[] CATEGORY_NAMES = {
        "All", "Beginner Basics", "3D Modeling", "Animation",
        "Sculpting", "Rendering", "Geometry Nodes", "Materials & Textures"
    };
    private static final String[] CATEGORY_ICONS = {
        "⭐", "🔷", "🧊", "🎬", "🌊", "💡", "⚙️", "🎨"
    };
    private static final String[] CATEGORY_COLORS = {
        "#FF6A00", "#4A90E2", "#FF6A00", "#7B5EA7",
        "#2ECC71", "#F4D03F", "#E74C3C", "#9B59B6"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repo = TutorialRepository.getInstance();

        setupGreeting(view);
        setupSearch(view);
        setupContinueLearning(view);
        setupCategoryRow(view);
        setupFeaturedTutorials(view);
        setupSeeAll(view);
        setupNotificationBell(view);

        // Add press animations to interactive cards
        View cardContinue = view.findViewById(R.id.card_continue);
        if (cardContinue != null) {
            com.example.a3dify.utils.AnimUtils.addPressAnimation(cardContinue);
        }

        // Add search bar glow
        EditText etSearch = view.findViewById(R.id.et_search);
        if (etSearch != null) {
            com.example.a3dify.utils.AnimUtils.addSearchFocusGlow(etSearch);
        }
    }

    // ── Greeting ──────────────────────────────────────────────────
    private void setupGreeting(View view) {
        TextView tvGreeting = view.findViewById(R.id.tv_greeting_label);
        TextView tvUsername = view.findViewById(R.id.tv_username);

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (tvGreeting != null) {
            if (hour < 12)      tvGreeting.setText("Good morning,");
            else if (hour < 17) tvGreeting.setText("Good afternoon,");
            else                tvGreeting.setText("Good evening,");
        }

        if (tvUsername != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                DatabaseHelper db = DatabaseHelper.getInstance(requireContext());
                Cursor cursor = db.getUserProfile(user.getUid());
                if (cursor != null && cursor.moveToFirst()) {
                    int col = cursor.getColumnIndex(DatabaseHelper.COL_USERNAME);
                    if (col >= 0) tvUsername.setText(cursor.getString(col) + " ✦");
                    cursor.close();
                } else {
                    String email = user.getEmail() != null ? user.getEmail() : "";
                    String name  = email.contains("@") ? email.split("@")[0] : "Learner";
                    tvUsername.setText(name + " ✦");
                }
            } else {
                tvUsername.setText("Guest ✦");
            }
        }

        // Rotating motivational quote
        TextView tvQuote = view.findViewById(R.id.tv_quote);
        if (tvQuote != null) {
            String[] quotes = {
                "Every expert was once a beginner. Start your lesson today.",
                "3D mastery is built one tutorial at a time.",
                "The best render is the one you finish.",
                "Blender is a tool. Creativity is yours.",
                "Model something today that did not exist yesterday."
            };
            int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
            tvQuote.setText(quotes[day % quotes.length]);
        }
    }

    // ── Real search bar ───────────────────────────────────────────
    private void setupSearch(View view) {
        EditText etSearch = view.findViewById(R.id.et_search);
        llNoResults = view.findViewById(R.id.ll_no_results);

        if (etSearch == null) return;

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTutorials(s.toString(), selectedCategory);
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    /*
     * Filters the featured tutorials list based on search text
     * AND the currently selected category.
     * Shows a "no results" message if nothing matches.
     */
    private void filterTutorials(String query, String category) {
        List<Tutorial> base = category.equals("All")
            ? repo.getAll()
            : repo.getByCategory(category);

        List<Tutorial> filtered = new ArrayList<>();
        String lower = query.toLowerCase().trim();
        for (Tutorial t : base) {
            if (lower.isEmpty()
                || t.getTitle().toLowerCase().contains(lower)
                || t.getCategory().toLowerCase().contains(lower)
                || t.getDifficulty().toLowerCase().contains(lower)) {
                filtered.add(t);
            }
        }

        if (featuredAdapter != null) {
            featuredAdapter.updateList(filtered);
        }

        if (llNoResults != null) {
            llNoResults.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    // ── Continue Learning ─────────────────────────────────────────
    private void setupContinueLearning(View view) {
        LinearLayout cardContinue   = view.findViewById(R.id.card_continue);
        TextView     tvContinueTitle = view.findViewById(R.id.tv_continue_title);
        TextView     tvContinueLesson = view.findViewById(R.id.tv_continue_lesson);
        View         progressFill   = view.findViewById(R.id.view_progress_fill);

        // Read last-viewed tutorial from SharedPreferences
        SharedPreferences prefs = requireContext()
            .getSharedPreferences("continue_learning", requireContext().MODE_PRIVATE);
        String lastId    = prefs.getString("last_tutorial_id", null);
        String lastTitle = prefs.getString("last_tutorial_title", null);

        Tutorial target;
        if (lastId != null) {
            target = repo.findById(lastId);
        } else {
            target = repo.getBeginnerDefault();
        }

        final Tutorial finalTarget = (target != null) ? target : repo.getBeginnerDefault();

        if (tvContinueTitle != null) {
            tvContinueTitle.setText(finalTarget.getTitle());
        }
        if (tvContinueLesson != null) {
            tvContinueLesson.setText(lastId != null
                ? "Continue where you left off"
                : "Start here — recommended for beginners");
        }

        // Animate progress bar
        if (progressFill != null) {
            progressFill.post(() -> {
                View track = (View) progressFill.getParent();
                int trackWidth  = track.getWidth();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                int completed = 0;
                if (user != null) {
                    completed = DatabaseHelper.getInstance(requireContext())
                        .getCompletedCount(user.getUid());
                }
                // Show 0–100% based on completed count (capped at 20 tutorials)
                float pct = Math.min(completed / 20f, 1.0f);
                int targetWidth = (int) (trackWidth * pct);

                ValueAnimator animator = ValueAnimator.ofInt(0, Math.max(targetWidth, 1));
                animator.setDuration(900);
                animator.setStartDelay(300);
                animator.addUpdateListener(anim -> {
                    ViewGroup.LayoutParams p = progressFill.getLayoutParams();
                    p.width = (int) anim.getAnimatedValue();
                    progressFill.setLayoutParams(p);
                });
                animator.start();
            });
        }

        // Tapping the card opens that tutorial
        if (cardContinue != null) {
            cardContinue.setOnClickListener(v -> openTutorial(finalTarget));
        }
    }

    // ── Category pills ────────────────────────────────────────────
    private void setupCategoryRow(View view) {
        RecyclerView rvCats = view.findViewById(R.id.rv_categories);
        if (rvCats == null) return;

        rvCats.setLayoutManager(
            new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        List<Category> cats = new ArrayList<>();
        for (int i = 0; i < CATEGORY_NAMES.length; i++) {
            int count = CATEGORY_NAMES[i].equals("All")
                ? repo.getAll().size()
                : repo.getByCategory(CATEGORY_NAMES[i]).size();
            cats.add(new Category(
                CATEGORY_ICONS[i],
                CATEGORY_NAMES[i],
                count,
                CATEGORY_COLORS[i],
                ""
            ));
        }

        CategoryAdapter adapter = new CategoryAdapter(getContext(), cats, false);
        adapter.setOnItemClickListener(cat -> {
            selectedCategory = cat.getName();
            adapter.setSelectedCategory(selectedCategory);

            // Get current search text
            EditText etSearch = view.findViewById(R.id.et_search);
            String query = etSearch != null ? etSearch.getText().toString() : "";
            filterTutorials(query, selectedCategory);
        });
        rvCats.setAdapter(adapter);
    }

    // ── Featured tutorials ────────────────────────────────────────
    private void setupFeaturedTutorials(View view) {
        rvFeatured = view.findViewById(R.id.rv_featured);
        llNoResults = view.findViewById(R.id.ll_no_results);
        if (rvFeatured == null) return;

        rvFeatured.setLayoutManager(
            new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        featuredAdapter = new TutorialAdapter(getContext(), repo.getAll());
        featuredAdapter.setOnItemClickListener(this::openTutorial);
        rvFeatured.setAdapter(featuredAdapter);
    }

    // ── See All ───────────────────────────────────────────────────
    private void setupSeeAll(View view) {
        TextView tvSeeAll = view.findViewById(R.id.tv_see_all);
        if (tvSeeAll != null) {
            tvSeeAll.setOnClickListener(v ->
                startActivity(new Intent(requireActivity(), AllTutorialsActivity.class))
            );
        }
    }

    // ── Notification bell ─────────────────────────────────────────
    private void setupNotificationBell(View view) {
        View bellContainer = view.findViewById(R.id.btn_notif);
        if (bellContainer != null) {
            bellContainer.setOnClickListener(v ->
                startActivity(new Intent(requireActivity(), NotificationsActivity.class))
            );
        }
    }

    /*
     * Opens TutorialDetailActivity for the given tutorial.
     * Also saves it as the last-viewed tutorial for Continue Learning.
     */
    private void openTutorial(Tutorial tutorial) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime < 600) return;
        lastClickTime = currentTime;

        // Save as last viewed
        requireContext()
            .getSharedPreferences("continue_learning", requireContext().MODE_PRIVATE)
            .edit()
            .putString("last_tutorial_id",    tutorial.getTutorialId())
            .putString("last_tutorial_title", tutorial.getTitle())
            .apply();

        Intent intent = new Intent(requireActivity(), TutorialDetailActivity.class);
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
