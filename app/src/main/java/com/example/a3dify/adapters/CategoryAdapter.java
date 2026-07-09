package com.example.a3dify.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3dify.R;
import com.example.a3dify.models.Category;
import java.util.List;

/*
 * CategoryAdapter
 * Two modes:
 *   isRowLayout = false → pill chips (Home screen horizontal row)
 *   isRowLayout = true  → full-width rows (Explore screen)
 *
 * Selected category pill gets orange border + orange text.
 * All others get gray border + gray text.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CatHolder> {

    private final Context        context;
    private final List<Category> categories;
    private final boolean        isRowLayout;
    private       String         selectedCategory = "All";
    private       OnItemClickListener listener;
    private       long           lastClickTime = 0;

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.listener = l;
    }

    public CategoryAdapter(Context context, List<Category> categories, boolean isRowLayout) {
        this.context      = context;
        this.categories   = categories;
        this.isRowLayout  = isRowLayout;
    }

    public void setSelectedCategory(String name) {
        this.selectedCategory = name;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = isRowLayout
            ? R.layout.item_category_row
            : R.layout.item_category_pill;
        View v = LayoutInflater.from(context).inflate(layout, parent, false);
        return new CatHolder(v, isRowLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull CatHolder holder, int position) {
        Category cat = categories.get(position);
        boolean isSelected = cat.getName().equals(selectedCategory);

        if (!isRowLayout) {
            // ── Pill mode ──
            if (holder.tvName != null) {
                holder.tvName.setText(cat.getName());
                holder.tvName.setTextColor(isSelected
                    ? Color.parseColor("#FF6A00")
                    : Color.parseColor("#555555"));
            }
            holder.itemView.setBackgroundResource(isSelected
                ? R.drawable.bg_pill_orange
                : R.drawable.bg_pill);

            // Set icon
            if (holder.ivIcon != null) {
                holder.ivIcon.setImageResource(getCategoryIcon(cat.getName()));
                holder.ivIcon.setAlpha(isSelected ? 1.0f : 0.4f);
            }

        } else {
            // ── Row mode ──
            if (holder.tvName    != null) holder.tvName.setText(cat.getName());
            if (holder.tvCount   != null) holder.tvCount.setText(cat.getCount() + " tutorials");
            if (holder.tvLevel   != null) holder.tvLevel.setText(cat.getDifficulty());
            if (holder.ivIcon    != null) {
                holder.ivIcon.setImageResource(getCategoryIcon(cat.getName()));
            }

            // Color the icon background
            try {
                int color = Color.parseColor(cat.getColorHex());
                int dim   = Color.argb(30,
                    Color.red(color), Color.green(color), Color.blue(color));
                if (holder.vIconBg != null) holder.vIconBg.setBackgroundColor(dim);
                if (holder.tvLevel != null) holder.tvLevel.setTextColor(color);
            } catch (Exception ignored) {}
        }

        holder.itemView.setOnClickListener(v -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime < 600) return;
            lastClickTime = currentTime;

            if (listener != null) listener.onItemClick(cat);
        });
    }

    @Override
    public int getItemCount() { return categories.size(); }

    private int getCategoryIcon(String name) {
        switch (name) {
            case "3D Modeling":          return R.drawable.ic_cube;
            case "Animation":            return R.drawable.ic_movie;
            case "Sculpting":            return R.drawable.ic_brush;
            case "Rendering":            return R.drawable.ic_image;
            case "Geometry Nodes":       return R.drawable.ic_device_hub;
            case "Materials & Textures": return R.drawable.ic_palette;
            default:                     return R.drawable.ic_school;
        }
    }

    static class CatHolder extends RecyclerView.ViewHolder {
        TextView  tvName, tvCount, tvLevel;
        ImageView ivIcon;
        View      vIconBg;

        CatHolder(@NonNull View v, boolean isRow) {
            super(v);
            tvName  = v.findViewById(isRow ? R.id.tv_cat_name : R.id.tv_category_name);
            ivIcon  = v.findViewById(isRow ? R.id.iv_cat_row_icon : R.id.iv_pill_icon);
            if (isRow) {
                tvCount = v.findViewById(R.id.tv_cat_count);
                tvLevel = v.findViewById(R.id.tv_cat_difficulty);
                vIconBg = v.findViewById(R.id.view_cat_icon_bg);
            }
        }
    }
}
