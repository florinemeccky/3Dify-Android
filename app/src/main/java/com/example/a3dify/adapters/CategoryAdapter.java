package com.example.a3dify.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3dify.R;
import com.example.a3dify.models.CategoryModel;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CatHolder> {

    private final List<CategoryModel> data;
    private final boolean isVertical; // true = row layout, false = pill layout

    public CategoryAdapter(List<CategoryModel> data, boolean isVertical) {
        this.data       = data;
        this.isVertical = isVertical;
    }

    @NonNull
    @Override
    public CatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = isVertical
                ? R.layout.item_category_row
                : R.layout.item_category_pill;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new CatHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CatHolder holder, int position) {
        CategoryModel cat = data.get(position);
        holder.tvIcon.setText(cat.icon);
        holder.tvName.setText(cat.name);

        if (isVertical) {
            if (holder.tvCount != null) holder.tvCount.setText(cat.count + " tutorials");
            if (holder.tvLevel != null) holder.tvLevel.setText(cat.level);
        }

        // Tint icon container background with category color at ~15% opacity
        try {
            int color = Color.parseColor(cat.colorHex);
            int dimColor = Color.argb(38, Color.red(color),
                    Color.green(color),
                    Color.blue(color));
            if (holder.llIconContainer != null) {
                holder.llIconContainer.setBackgroundColor(dimColor);
            }
        } catch (Exception ignored) {}
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class CatHolder extends RecyclerView.ViewHolder {
        TextView tvIcon, tvName, tvCount, tvLevel;
        LinearLayout llIconContainer;

        CatHolder(@NonNull View v) {
            super(v);
            tvIcon          = v.findViewById(R.id.tv_cat_icon) != null
                    ? v.findViewById(R.id.tv_cat_icon)
                    : v.findViewById(R.id.tv_cat_row_icon);
            tvName          = v.findViewById(R.id.tv_cat_label) != null
                    ? v.findViewById(R.id.tv_cat_label)
                    : v.findViewById(R.id.tv_cat_name);
            tvCount         = v.findViewById(R.id.tv_cat_count);
            tvLevel         = v.findViewById(R.id.tv_cat_level);
            llIconContainer = v.findViewById(R.id.ll_icon_container) != null
                    ? v.findViewById(R.id.ll_icon_container)
                    : v.findViewById(R.id.ll_cat_icon);
        }
    }
}