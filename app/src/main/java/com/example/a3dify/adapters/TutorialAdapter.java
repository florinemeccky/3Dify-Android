package com.example.a3dify.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a3dify.R;
import com.example.a3dify.activities.TutorialDetailActivity;
import com.example.a3dify.models.Tutorial;

import java.util.ArrayList;
import java.util.List;

/*
 * TutorialAdapter
 *
 * isCard = true  → inflates item_tutorial_card.xml  (horizontal scroll)
 * isCard = false → inflates item_tutorial_row.xml   (vertical list)
 *
 * The colored gradient thumbnail is applied to the FrameLayout container
 * (fl_thumbnail or fl_row_thumbnail) directly — not to a child View.
 * This guarantees the color is always visible on all Android versions.
 *
 * The white icon is tinted via setColorFilter() in Java, not via XML,
 * because XML colorFilter attribute is unreliable below API 29.
 */
public class TutorialAdapter
    extends RecyclerView.Adapter<TutorialAdapter.TutorialViewHolder> {

    private final Context context;
    private List<Tutorial> tutorials;
    private final boolean isCard;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Tutorial tutorial);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.listener = l;
    }

    // Default constructor — card mode
    public TutorialAdapter(Context context, List<Tutorial> tutorials) {
        this.context   = context;
        this.tutorials = new ArrayList<>(tutorials);
        this.isCard    = true;
    }

    // Explicit mode
    public TutorialAdapter(Context context, List<Tutorial> tutorials, boolean isCard) {
        this.context   = context;
        this.tutorials = new ArrayList<>(tutorials);
        this.isCard    = isCard;
    }

    // Called by search filtering
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
        View v = LayoutInflater.from(context).inflate(layout, parent, false);
        return new TutorialViewHolder(v, isCard);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorialViewHolder holder, int position) {
        Tutorial t = tutorials.get(position);

        // ── Title ──────────────────────────────────────────────────
        if (holder.tvTitle != null) {
            holder.tvTitle.setText(t.getTitle());
        }

        // ── Difficulty badge ────────────────────────────────────────
        if (holder.tvDifficulty != null) {
            holder.tvDifficulty.setText(t.getDifficulty());
            holder.tvDifficulty.setTextColor(t.getDifficultyColor());
            holder.tvDifficulty.setBackgroundResource(
                t.getDifficultyBadgeBackground());
        }

        // ── Duration ────────────────────────────────────────────────
        if (holder.tvDuration != null) {
            holder.tvDuration.setText(t.getDuration());
        }

        // ── Category label (row mode only) ──────────────────────────
        if (!isCard && holder.tvCategory != null) {
            holder.tvCategory.setText(t.getCategory());
        }

        // ── THUMBNAIL GRADIENT ──────────────────────────────────────
        // Set on the FrameLayout itself — this is the guaranteed approach.
        // Setting on a child View can silently fail when the RecyclerView
        // recycles views before they are fully laid out.
        if (holder.flThumbnail != null) {
            holder.flThumbnail.setBackgroundResource(t.getThumbnailBackground());
        }

        // ── CATEGORY ICON ───────────────────────────────────────────
        // Set icon resource and tint it white in Java.
        // Using DrawableCompat for better compatibility across Android versions.
        if (holder.ivIcon != null) {
            Drawable icon = ContextCompat.getDrawable(context, t.getCategoryIcon());
            if (icon != null) {
                icon = DrawableCompat.wrap(icon).mutate();
                DrawableCompat.setTint(icon, Color.WHITE);
                holder.ivIcon.setImageDrawable(icon);
            }
            holder.ivIcon.setVisibility(View.VISIBLE);
        }

        // ── TAP HANDLER ─────────────────────────────────────────────
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(t);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tutorials.size();
    }

    // ── ViewHolder ───────────────────────────────────────────────────
    static class TutorialViewHolder extends RecyclerView.ViewHolder {

        // Shared between card and row
        TextView    tvTitle, tvDifficulty, tvDuration;
        ImageView   ivIcon;
        FrameLayout flThumbnail;   // ← the container that gets the background

        // Row mode only
        TextView tvCategory;

        TutorialViewHolder(@NonNull View itemView, boolean isCard) {
            super(itemView);

            if (isCard) {
                // Card layout IDs
                flThumbnail  = itemView.findViewById(R.id.fl_thumbnail);
                tvTitle      = itemView.findViewById(R.id.tv_tutorial_title);
                tvDifficulty = itemView.findViewById(R.id.tv_tutorial_difficulty);
                tvDuration   = itemView.findViewById(R.id.tv_thumb_duration);
                ivIcon       = itemView.findViewById(R.id.iv_tutorial_icon);
            } else {
                // Row layout IDs
                flThumbnail  = itemView.findViewById(R.id.fl_row_thumbnail);
                tvTitle      = itemView.findViewById(R.id.tv_row_title);
                tvDifficulty = itemView.findViewById(R.id.tv_row_difficulty);
                tvDuration   = itemView.findViewById(R.id.tv_row_duration);
                ivIcon       = itemView.findViewById(R.id.iv_row_icon);
                tvCategory   = itemView.findViewById(R.id.tv_row_category);
            }
        }
    }
}