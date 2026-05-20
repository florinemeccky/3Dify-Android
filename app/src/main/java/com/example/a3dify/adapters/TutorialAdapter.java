package com.example.a3dify.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3dify.R;
import com.example.a3dify.models.TutorialModel;
import java.util.List;

public class TutorialAdapter extends RecyclerView.Adapter<TutorialAdapter.TutHolder> {

    private final List<TutorialModel> data;
    private final boolean isCard; // true = horizontal card, false = vertical row
    private final Activity activity;

    public TutorialAdapter(List<TutorialModel> data, boolean isCard, Activity activity) {
        this.data     = data;
        this.isCard   = isCard;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = isCard
                ? R.layout.item_tutorial_card
                : R.layout.item_tutorial_row;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new TutHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TutHolder holder, int position) {
        TutorialModel tut = data.get(position);

        if (holder.tvIcon  != null) holder.tvIcon.setText(tut.icon);
        if (holder.tvTitle != null) holder.tvTitle.setText(tut.title);
        if (holder.tvLevel != null) holder.tvLevel.setText(tut.level);
        if (holder.tvMeta  != null)
            holder.tvMeta.setText(tut.duration + "  •  " + tut.level);

        holder.itemView.setOnClickListener(v -> {
            // TODO: pass real data to TutorialDetailActivity
            // Intent intent = new Intent(activity, TutorialDetailActivity.class);
            // intent.putExtra("title", tut.title);
            // activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class TutHolder extends RecyclerView.ViewHolder {
        TextView tvIcon, tvTitle, tvLevel, tvMeta;

        TutHolder(@NonNull View v) {
            super(v);
            tvIcon  = v.findViewById(R.id.tv_tut_icon) != null
                    ? v.findViewById(R.id.tv_tut_icon)
                    : v.findViewById(R.id.tv_row_icon);
            tvTitle = v.findViewById(R.id.tv_tut_title) != null
                    ? v.findViewById(R.id.tv_tut_title)
                    : v.findViewById(R.id.tv_row_title);
            tvLevel = v.findViewById(R.id.tv_tut_level);
            tvMeta  = v.findViewById(R.id.tv_row_meta);
        }
    }
}