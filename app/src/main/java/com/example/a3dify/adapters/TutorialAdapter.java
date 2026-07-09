package com.example.a3dify.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3dify.R;
import com.example.a3dify.activities.TutorialDetailActivity;
import com.example.a3dify.models.Tutorial;
import java.util.ArrayList;
import java.util.List;

/*
 * TutorialAdapter
 * Handles two display modes:
 *   isCard = true  → horizontal card (item_tutorial_card.xml)
 *   isCard = false → vertical row  (item_tutorial_row.xml)
 *
 * Uses Tutorial.getThumbnailBackground() for gradient backgrounds.
 * Uses Tutorial.getCategoryIcon() for the vector icon.
 * Uses Tutorial.getDifficultyBadgeBackground() for colored badges.
 *
 * The constructor with no isCard parameter defaults to card mode.
 */
public class TutorialAdapter extends RecyclerView.Adapter<TutorialAdapter.TutorialViewHolder> {

    private final Context      context;
    private       List<Tutorial> tutorials;
    private final boolean      isCard;
    private       OnItemClickListener listener;
    private       long           lastClickTime = 0;

    public interface OnItemClickListener {
        void onItemClick(Tutorial tutorial);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Card mode (horizontal scroll)
    public TutorialAdapter(Context context, List<Tutorial> tutorials) {
        this.context   = context;
        this.tutorials = new ArrayList<>(tutorials);
        this.isCard    = true;
    }

    // Explicit mode selection
    public TutorialAdapter(Context context, List<Tutorial> tutorials, boolean isCard) {
        this.context   = context;
        this.tutorials = new ArrayList<>(tutorials);
        this.isCard    = isCard;
    }

    public void updateList(List<Tutorial> newList) {
        this.tutorials = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TutorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = isCard
            ? R.layout.item_tutorial_card
            : R.layout.item_tutorial_row;
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new TutorialViewHolder(view, isCard);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorialViewHolder holder, int position) {
        Tutorial tutorial = tutorials.get(position);

        // ── Title ──
        if (holder.tvTitle != null) {
            holder.tvTitle.setText(tutorial.getTitle());
        }

        // ── Difficulty badge ──
        if (holder.tvDifficulty != null) {
            holder.tvDifficulty.setText(tutorial.getDifficulty());
            holder.tvDifficulty.setTextColor(tutorial.getDifficultyColor());
            holder.tvDifficulty.setBackgroundResource(
                tutorial.getDifficultyBadgeBackground());
        }

        // ── Duration ──
        if (holder.tvDuration != null) {
            holder.tvDuration.setText(tutorial.getDuration());
        }

        // ── Gradient thumbnail background ──
        if (holder.viewThumbBg != null) {
            holder.viewThumbBg.setBackgroundResource(
                tutorial.getThumbnailBackground());
        }

        // ── Category icon ──
        if (holder.ivIcon != null) {
            holder.ivIcon.setImageResource(tutorial.getCategoryIcon());
        }

        // ── Row-only fields ──
        if (!isCard) {
            if (holder.tvCategory != null) {
                holder.tvCategory.setText(tutorial.getCategory());
            }
        }

        // ── Tap → open TutorialDetailActivity ──
        holder.itemView.setOnClickListener(v -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime < 600) return;
            lastClickTime = currentTime;

            if (listener != null) {
                listener.onItemClick(tutorial);
            } else {
                // Default behavior if no listener is provided:
                // Save as last viewed for Continue Learning
                SharedPreferences prefs = context.getSharedPreferences(
                        "continue_learning", Context.MODE_PRIVATE);
                prefs.edit()
                        .putString("last_tutorial_id",    tutorial.getTutorialId())
                        .putString("last_tutorial_title", tutorial.getTitle())
                        .apply();

                Intent intent = new Intent(context, TutorialDetailActivity.class);
                intent.putExtra("icon",        tutorial.getIcon());
                intent.putExtra("title",       tutorial.getTitle());
                intent.putExtra("category",    tutorial.getCategory());
                intent.putExtra("difficulty",  tutorial.getDifficulty());
                intent.putExtra("duration",    tutorial.getDuration());
                intent.putExtra("description", tutorial.getDescription());
                intent.putExtra("tutorialId",  tutorial.getTutorialId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return tutorials.size(); }

    static class TutorialViewHolder extends RecyclerView.ViewHolder {
        TextView  tvTitle, tvDifficulty, tvDuration, tvCategory;
        ImageView ivIcon;
        View      viewThumbBg;

        TutorialViewHolder(@NonNull View itemView, boolean isCard) {
            super(itemView);
            tvTitle      = itemView.findViewById(
                isCard ? R.id.tv_tutorial_title : R.id.tv_row_title);
            tvDifficulty = itemView.findViewById(
                isCard ? R.id.tv_tutorial_difficulty : R.id.tv_row_difficulty);
            tvDuration   = itemView.findViewById(
                isCard ? R.id.tv_thumb_duration : R.id.tv_row_duration);
            ivIcon       = itemView.findViewById(
                isCard ? R.id.iv_tutorial_icon : R.id.iv_row_icon);
            viewThumbBg  = itemView.findViewById(
                isCard ? R.id.view_thumb_bg : R.id.view_row_thumb_bg);
            if (!isCard) {
                tvCategory = itemView.findViewById(R.id.tv_row_category);
            }
        }
    }
}
