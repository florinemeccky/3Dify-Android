package com.example.a3dify.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3dify.R;
import com.example.a3dify.models.Tutorial;
import java.util.List;

/*
 * TutorialAdapter
 * Supplies tutorial data to a RecyclerView.
 * Used in HomeFragment for horizontal featured/recommended rows.
 *
 * Each item inflates item_tutorial_card.xml and fills in
 * the icon, title, difficulty badge, and duration.
 *
 * onItemClickListener lets the fragment respond to card taps
 * without the adapter needing to know about activities.
 */
public class TutorialAdapter extends RecyclerView.Adapter<TutorialAdapter.TutorialViewHolder> {

    private final Context        context;
    private       List<Tutorial> tutorials;
    private       OnItemClickListener listener;

    // Interface so fragments can handle card taps
    public interface OnItemClickListener {
        void onItemClick(Tutorial tutorial);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /*
     * Updates the list and refreshes the RecyclerView.
     * Called by search filtering in HomeFragment and AllTutorialsActivity.
     */
    public void updateList(List<Tutorial> newList) {
        this.tutorials.clear();
        this.tutorials.addAll(newList);
        notifyDataSetChanged();
    }

    public TutorialAdapter(Context context, List<Tutorial> tutorials) {
        this.context   = context;
        this.tutorials = new java.util.ArrayList<>(tutorials);
    }

    @NonNull
    @Override
    public TutorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_tutorial_card, parent, false);
        return new TutorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorialViewHolder holder, int position) {
        Tutorial tutorial = tutorials.get(position);

        holder.tvIcon.setText(tutorial.getIcon());
        holder.tvTitle.setText(tutorial.getTitle());
        holder.tvDifficulty.setText(tutorial.getDifficulty());
        holder.tvDuration.setText(tutorial.getDuration());

        // Color the difficulty badge based on level
        int badgeColor;
        switch (tutorial.getDifficulty()) {
            case "Intermediate": badgeColor = 0xFF4A90E2; break; // blue
            case "Advanced":     badgeColor = 0xFF7B5EA7; break; // purple
            default:             badgeColor = 0xFFFF6A00; break; // orange
        }
        holder.itemView.setOnClickListener(v -> {
            // Notify the fragment if it has a listener set
            if (listener != null) {
                listener.onItemClick(tutorial);
            }

            // Also directly launch TutorialDetailActivity with the tutorial data
            // This works even if no listener is set
            android.content.Context ctx = holder.itemView.getContext();
            android.content.Intent intent =
                    new android.content.Intent(ctx,
                            com.example.a3dify.activities.TutorialDetailActivity.class);

            intent.putExtra("icon",        tutorial.getIcon());
            intent.putExtra("title",       tutorial.getTitle());
            intent.putExtra("category",    tutorial.getCategory());
            intent.putExtra("difficulty",  tutorial.getDifficulty());
            intent.putExtra("duration",    tutorial.getDuration());
            intent.putExtra("description", tutorial.getDescription());
            // Use title as ID — unique enough for demo purposes
            intent.putExtra("tutorialId",  tutorial.getTitle().replaceAll("\\s+", "_").toLowerCase());

            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tutorials.size();
    }

    // ViewHolder holds references to each view inside one card
    static class TutorialViewHolder extends RecyclerView.ViewHolder {
        TextView     tvIcon, tvTitle, tvDifficulty, tvDuration;

        TutorialViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIcon       = itemView.findViewById(R.id.tv_tutorial_icon);
            tvTitle      = itemView.findViewById(R.id.tv_tutorial_title);
            tvDifficulty = itemView.findViewById(R.id.tv_tutorial_difficulty);
            tvDuration   = itemView.findViewById(R.id.tv_tutorial_duration);
        }
    }
}