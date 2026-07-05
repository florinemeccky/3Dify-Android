package com.example.a3dify.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3dify.R;
import com.example.a3dify.models.Category;
import java.util.List;

/*
 * CategoryAdapter
 * Used in two places:
 *   1. HomeFragment — horizontal pill row (useRowLayout = false)
 *   2. ExploreFragment — vertical full-width rows (useRowLayout = true)
 *
 * The useRowLayout flag tells the adapter which layout to inflate.
 * This avoids needing two separate adapter classes for the same data.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final Context        context;
    private final List<Category> categories;
    private final boolean        useRowLayout;
    private       OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CategoryAdapter(Context context, List<Category> categories, boolean useRowLayout) {
        this.context      = context;
        this.categories   = categories;
        this.useRowLayout = useRowLayout;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Choose layout based on where this adapter is being used
        int layout = useRowLayout
                ? R.layout.item_category_row
                : R.layout.item_category_pill;

        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new CategoryViewHolder(view, useRowLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category cat = categories.get(position);

        holder.tvIcon.setText(cat.getIcon());
        holder.tvName.setText(cat.getName());

        // Parse the category color and create a 15% opacity version for backgrounds
        try {
            int color    = Color.parseColor(cat.getColorHex());
            int dimColor = Color.argb(
                    38,                   // 15% opacity
                    Color.red(color),
                    Color.green(color),
                    Color.blue(color)
            );
            if (holder.llIconBg != null) {
                holder.llIconBg.setBackgroundColor(dimColor);
            }
            if (holder.tvDifficulty != null) {
                holder.tvDifficulty.setTextColor(color);
                holder.tvDifficulty.setBackgroundColor(dimColor);
            }
        } catch (IllegalArgumentException ignored) {
            // If color parsing fails, leave the default orange
        }

        // Fill row-only fields
        if (useRowLayout) {
            if (holder.tvCount != null) {
                holder.tvCount.setText(cat.getCount() + " tutorials");
            }
            if (holder.tvDifficulty != null) {
                holder.tvDifficulty.setText(cat.getDifficulty());
            }
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(cat);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView     tvIcon, tvName, tvCount, tvDifficulty;
        LinearLayout llIconBg;

        CategoryViewHolder(@NonNull View itemView, boolean useRowLayout) {
            super(itemView);
            tvIcon = itemView.findViewById(
                    useRowLayout ? R.id.tv_cat_icon : R.id.tv_category_icon
            );
            tvName = itemView.findViewById(
                    useRowLayout ? R.id.tv_cat_name : R.id.tv_category_name
            );
            llIconBg = itemView.findViewById(
                    useRowLayout ? R.id.ll_cat_icon_bg : R.id.ll_icon_bg
            );
            // These only exist in the row layout
            if (useRowLayout) {
                tvCount      = itemView.findViewById(R.id.tv_cat_count);
                tvDifficulty = itemView.findViewById(R.id.tv_cat_difficulty);
            }
        }
    }
}