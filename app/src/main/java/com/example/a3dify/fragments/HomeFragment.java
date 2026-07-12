package com.example.a3dify.fragments;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a3dify.DatabaseHelper;
import com.example.a3dify.R;
import com.example.a3dify.SoftwareToolRepository;
import com.example.a3dify.TutorialRepository;
import com.example.a3dify.activities.AllTutorialsActivity;
import com.example.a3dify.activities.NotificationsActivity;
import com.example.a3dify.activities.TutorialDetailActivity;
import com.example.a3dify.adapters.CategoryAdapter;
import com.example.a3dify.adapters.SoftwareToolAdapter;
import com.example.a3dify.models.Category;
import com.example.a3dify.models.Tutorial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
 * HomeFragment — Redesigned
 * Sections:
 *   1. Greeting + notification bell
 *   2. Search bar (filters tutorials live)
 *   3. Continue Learning card
 *   4. Categories horizontal strip
 *   5. 3D Tools & Software (replaces Featured Tutorials)
 *   6. Daily Challenge (static rotating content)
 *   7. Learning Roadmap (visual step progress)
 */
public class HomeFragment extends Fragment {

    private TutorialRepository repo;
    private String selectedCategory = "All";

    private static final String[] CATEGORY_NAMES = {
        "All", "Beginner Basics", "3D Modeling", "Animation",
        "Sculpting", "Rendering", "Geometry Nodes", "Materials & Textures"
    };
    private static final String[] CATEGORY_ICONS_KEYS = {
        "all", "basics", "modeling", "animation",
        "sculpting", "rendering", "nodes", "materials"
    };
    private static final String[] CATEGORY_COLORS = {
        "#FF6A00","#4A90E2","#FF6A00","#7B5EA7",
        "#2ECC71","#F4D03F","#E74C3C","#EC4899"
    };

    // Daily challenges — rotates by day of year
    private static final String[] CHALLENGE_TITLES = {
        "Create a simple coffee mug",
        "Model a wooden chair",
        "Sculpt a smooth sphere face",
        "Build a low-poly tree",
        "Create a stylised gem"
    };
    private static final String[] CHALLENGE_OBJECTIVES = {
        "Using basic mesh tools, model a coffee mug with a handle. Focus on clean topology and smooth curves.",
        "Model a four-legged wooden chair using box modeling. Pay attention to proportions and edge loops.",
        "Using Sculpt Mode, start from a sphere and sculpt basic facial features — keep forms simple and clean.",
        "Build a low-polygon tree using simple geometry. Aim for style over realism.",
        "Model a faceted gemstone using the Mirror modifier and a single half. Apply a glass material."
    };
    private static final String[] CHALLENGE_DIFFICULTIES = {
        "Beginner","Beginner","Intermediate","Beginner","Intermediate"
    };
    private static final String[] CHALLENGE_TIMES = {
        "30 – 45 min","45 – 60 min","30 – 40 min","20 – 30 min","40 – 55 min"
    };

    // Roadmap steps
    private static final String[] ROADMAP_STEPS = {
        "Blender Interface",
        "Viewport Navigation",
        "Object Mode Basics",
        "Edit Mode & Mesh Tools",
        "Materials & Shaders",
        "Lighting Setups",
        "Camera & Rendering",
        "Keyframe Animation",
        "Geometry Nodes",
        "Advanced Projects"
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
        setupCategories(view);
        setupSoftwareTools(view);
        setupDailyChallenge(view);
        setupRoadmap(view);
        setupNotificationBell(view);
    }

    // ── 1. Greeting ───────────────────────────────────────────────
    private void setupGreeting(View view) {
        TextView tvGreeting = view.findViewById(R.id.tv_greeting_label);
        TextView tvUsername = view.findViewById(R.id.tv_username);

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (tvGreeting != null) {
            if      (hour < 12) tvGreeting.setText("Good morning,");
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
                    if (col >= 0) tvUsername.setText(cursor.getString(col));
                    cursor.close();
                } else {
                    String email = user.getEmail() != null ? user.getEmail() : "";
                    tvUsername.setText(email.contains("@")
                        ? email.split("@")[0] : "Learner");
                }
            } else {
                tvUsername.setText("Guest");
            }
        }
    }

    // ── 2. Search ─────────────────────────────────────────────────
    private void setupSearch(View view) {
        EditText etSearch = view.findViewById(R.id.et_search);
        LinearLayout llNoResults = view.findViewById(R.id.ll_no_results);
        if (etSearch == null) return;

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    // Restore normal sections
                    if (llNoResults != null)
                        llNoResults.setVisibility(View.GONE);
                    return;
                }
                // Search across tutorials and navigate to AllTutorials with query
                // For live filtering we open AllTutorialsActivity with a pre-filled query
                if (query.length() >= 2) {
                    if (llNoResults != null) {
                        List<Tutorial> results = repo.search(query);
                        llNoResults.setVisibility(
                            results.isEmpty() ? View.VISIBLE : View.GONE);
                    }
                }
            }
        });

        // Pressing search on keyboard opens AllTutorials
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            String query = etSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                Intent intent = new Intent(requireActivity(), AllTutorialsActivity.class);
                intent.putExtra("search_query", query);
                startActivity(intent);
                etSearch.setText("");
            }
            return true;
        });

        // Focus glow
        etSearch.setOnFocusChangeListener((v, hasFocus) -> {
            View parent = (View) v.getParent();
            if (parent != null) {
                parent.setBackgroundResource(hasFocus
                    ? R.drawable.bg_input_focused
                    : R.drawable.bg_input_default);
            }
        });
    }

    // ── 3. Continue Learning ──────────────────────────────────────
    private void setupContinueLearning(View view) {
        LinearLayout cardContinue    = view.findViewById(R.id.card_continue);
        TextView     tvTitle         = view.findViewById(R.id.tv_continue_title);
        TextView     tvLesson        = view.findViewById(R.id.tv_continue_lesson);
        View         progressFill    = view.findViewById(R.id.view_progress_fill);
        View         thumbBg         = view.findViewById(R.id.view_continue_thumb);
        ImageView    ivIcon          = view.findViewById(R.id.iv_continue_icon);

        SharedPreferences prefs = requireContext()
            .getSharedPreferences("continue_learning", requireContext().MODE_PRIVATE);
        String lastId = prefs.getString("last_tutorial_id", null);

        Tutorial target = lastId != null
            ? repo.findById(lastId)
            : repo.getBeginnerDefault();
        if (target == null) target = repo.getBeginnerDefault();
        final Tutorial finalTarget = target;

        if (tvTitle   != null) tvTitle.setText(finalTarget.getTitle());
        if (tvLesson  != null) tvLesson.setText(lastId != null
            ? "Continue where you left off"
            : "Start here — recommended for beginners");

        // Set thumbnail gradient
        if (thumbBg != null)
            thumbBg.setBackgroundResource(finalTarget.getThumbnailBackground());
        if (ivIcon != null) {
            ivIcon.setImageResource(finalTarget.getCategoryIcon());
            ivIcon.setColorFilter(Color.WHITE,
                android.graphics.PorterDuff.Mode.SRC_IN);
        }

        // Animate progress bar
        if (progressFill != null) {
            progressFill.post(() -> {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                int completed = 0;
                if (user != null) {
                    completed = DatabaseHelper.getInstance(requireContext())
                        .getCompletedCount(user.getUid());
                }
                float pct = Math.min(completed / 20f, 1.0f);
                int targetW = (int) (((View) progressFill.getParent()).getWidth() * pct);

                ValueAnimator anim = ValueAnimator.ofInt(0, Math.max(targetW, 0));
                anim.setDuration(900);
                anim.setStartDelay(300);
                anim.addUpdateListener(a -> {
                    ViewGroup.LayoutParams p = progressFill.getLayoutParams();
                    p.width = (int) a.getAnimatedValue();
                    progressFill.setLayoutParams(p);
                });
                anim.start();
            });
        }

        if (cardContinue != null) {
            cardContinue.setOnClickListener(v -> openTutorial(finalTarget));
        }
    }

    // ── 4. Categories ─────────────────────────────────────────────
    private void setupCategories(View view) {
        RecyclerView rv = view.findViewById(R.id.rv_categories);
        if (rv == null) return;

        rv.setLayoutManager(
            new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        List<Category> cats = new ArrayList<>();
        for (int i = 0; i < CATEGORY_NAMES.length; i++) {
            int count = CATEGORY_NAMES[i].equals("All")
                ? repo.getAll().size()
                : repo.getByCategory(CATEGORY_NAMES[i]).size();
            cats.add(new Category(
                getCategoryEmoji(CATEGORY_NAMES[i]),
                CATEGORY_NAMES[i], count,
                CATEGORY_COLORS[i], ""));
        }

        CategoryAdapter adapter = new CategoryAdapter(getContext(), cats, false);
        adapter.setSelectedCategory(selectedCategory);
        adapter.setOnItemClickListener(cat -> {
            selectedCategory = cat.getName();
            adapter.setSelectedCategory(selectedCategory);
            // Open AllTutorialsActivity filtered to this category
            Intent intent = new Intent(requireActivity(), AllTutorialsActivity.class);
            if (!cat.getName().equals("All"))
                intent.putExtra("category", cat.getName());
            startActivity(intent);
        });
        rv.setAdapter(adapter);
    }

    private String getCategoryEmoji(String name) {
        switch (name) {
            case "3D Modeling":          return "M";
            case "Animation":            return "A";
            case "Sculpting":            return "S";
            case "Rendering":            return "R";
            case "Geometry Nodes":       return "G";
            case "Materials & Textures": return "T";
            case "Beginner Basics":      return "B";
            default:                     return "★";
        }
    }

    // ── 5. 3D Tools ───────────────────────────────────────────────
    private void setupSoftwareTools(View view) {
        RecyclerView rv = view.findViewById(R.id.rv_software_tools);
        if (rv == null) return;

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(new SoftwareToolAdapter(
            getContext(),
            SoftwareToolRepository.getInstance().getAll()));
    }

    // ── 6. Daily Challenge ────────────────────────────────────────
    private void setupDailyChallenge(View view) {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int i   = day % CHALLENGE_TITLES.length;

        TextView tvTitle      = view.findViewById(R.id.tv_challenge_title);
        TextView tvObjective  = view.findViewById(R.id.tv_challenge_objective);
        TextView tvDifficulty = view.findViewById(R.id.tv_challenge_difficulty);
        TextView tvTime       = view.findViewById(R.id.tv_challenge_time);

        if (tvTitle     != null) tvTitle.setText(CHALLENGE_TITLES[i]);
        if (tvObjective != null) tvObjective.setText(CHALLENGE_OBJECTIVES[i]);
        if (tvTime      != null) tvTime.setText(CHALLENGE_TIMES[i]);

        if (tvDifficulty != null) {
            tvDifficulty.setText(CHALLENGE_DIFFICULTIES[i]);
            if (CHALLENGE_DIFFICULTIES[i].equals("Beginner")) {
                tvDifficulty.setTextColor(Color.parseColor("#34D399"));
                tvDifficulty.setBackgroundResource(R.drawable.bg_badge_beginner);
            } else {
                tvDifficulty.setTextColor(Color.parseColor("#FBBF24"));
                tvDifficulty.setBackgroundResource(R.drawable.bg_badge_intermediate);
            }
        }
    }

    // ── 7. Learning Roadmap ───────────────────────────────────────
    private void setupRoadmap(View view) {
        LinearLayout llSteps = view.findViewById(R.id.ll_roadmap_steps);
        TextView     tvProg  = view.findViewById(R.id.tv_roadmap_progress);
        if (llSteps == null) return;

        // How many steps are "done" based on completed tutorial count
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        int completedCount = 0;
        if (user != null) {
            completedCount = DatabaseHelper.getInstance(requireContext())
                .getCompletedCount(user.getUid());
        }
        // Each 2 completed tutorials = 1 roadmap step
        int stepsComplete = Math.min(completedCount / 2, ROADMAP_STEPS.length);

        if (tvProg != null) {
            tvProg.setText(stepsComplete + " / " + ROADMAP_STEPS.length);
        }

        // Build each step row programmatically
        for (int i = 0; i < ROADMAP_STEPS.length; i++) {
            boolean done = i < stepsComplete;

            // Row container
            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(android.view.Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(rowParams);

            // Left column: node + line
            LinearLayout leftCol = new LinearLayout(requireContext());
            leftCol.setOrientation(LinearLayout.VERTICAL);
            leftCol.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
            LinearLayout.LayoutParams leftParams =
                new LinearLayout.LayoutParams(dp(36), LinearLayout.LayoutParams.WRAP_CONTENT);
            leftCol.setLayoutParams(leftParams);

            // Node circle
            View node = new View(requireContext());
            int nodeSize = dp(16);
            LinearLayout.LayoutParams nodeParams =
                new LinearLayout.LayoutParams(nodeSize, nodeSize);
            nodeParams.topMargin = dp(14);
            node.setLayoutParams(nodeParams);
            node.setBackgroundResource(done
                ? R.drawable.bg_roadmap_node_done
                : R.drawable.bg_roadmap_node_pending);
            leftCol.addView(node);

            // Connector line (not shown for last item)
            if (i < ROADMAP_STEPS.length - 1) {
                View line = new View(requireContext());
                LinearLayout.LayoutParams lineParams =
                    new LinearLayout.LayoutParams(dp(2), dp(32));
                lineParams.topMargin = dp(2);
                lineParams.gravity = android.view.Gravity.CENTER_HORIZONTAL;
                line.setLayoutParams(lineParams);
                line.setBackgroundResource(R.drawable.bg_roadmap_line);
                if (done) line.setBackgroundColor(Color.parseColor("#FF6A00"));
                leftCol.addView(line);
            }

            row.addView(leftCol);

            // Step label
            TextView label = new TextView(requireContext());
            LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            labelParams.setMarginStart(dp(12));
            labelParams.topMargin = dp(14);
            if (i < ROADMAP_STEPS.length - 1)
                labelParams.bottomMargin = 0;
            label.setLayoutParams(labelParams);
            label.setText((i + 1) + ".  " + ROADMAP_STEPS[i]);
            label.setTextSize(14f);

            if (done) {
                label.setTextColor(Color.parseColor("#FF6A00"));
                label.setTypeface(null, android.graphics.Typeface.BOLD);
            } else if (i == stepsComplete) {
                // Current step
                label.setTextColor(
                    requireContext().getColor(R.color.text_primary));
                label.setTypeface(null, android.graphics.Typeface.BOLD);
            } else {
                label.setTextColor(
                    requireContext().getColor(R.color.text_muted));
            }

            row.addView(label);

            // Done checkmark
            if (done) {
                ImageView check = new ImageView(requireContext());
                LinearLayout.LayoutParams checkParams =
                    new LinearLayout.LayoutParams(dp(16), dp(16));
                checkParams.setMarginEnd(dp(4));
                checkParams.topMargin = dp(14);
                check.setLayoutParams(checkParams);
                check.setImageResource(R.drawable.ic_check);
                check.setScaleType(ImageView.ScaleType.FIT_CENTER);
                row.addView(check);
            }

            llSteps.addView(row);
        }
    }

    // ── Notification bell ─────────────────────────────────────────
    private void setupNotificationBell(View view) {
        View bell = view.findViewById(R.id.btn_notif);
        if (bell != null) {
            bell.setOnClickListener(v ->
                startActivity(new Intent(requireActivity(),
                    NotificationsActivity.class)));
        }
    }

    // ── Helper: open a tutorial ───────────────────────────────────
    private void openTutorial(Tutorial tutorial) {
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

    // ── Helper: dp to px ──────────────────────────────────────────
    private int dp(int value) {
        return Math.round(value
            * requireContext().getResources().getDisplayMetrics().density);
    }
}