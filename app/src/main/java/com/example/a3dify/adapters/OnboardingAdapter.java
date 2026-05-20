package com.example.a3dify.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3dify.R;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.PageHolder> {

    private final Context context;

    private final String[] emojis    = {"🔷",              "📊",                   "🏆"};
    private final String[] titles    = {"Master Blender 3D","Track Your Progress",  "Join the Community"};
    private final String[] subtitles = {
            "Structured lessons from zero to professional 3D artist",
            "Streaks, badges, and skill bars keep you motivated daily",
            "Share your renders and learn alongside thousands of creators"
    };
    private final String[][] chips = {
            {"📐 Model","🎨 Texture","💡 Light","🎬 Animate"},
            {"📈 Stats","🔥 Streak","🏅 Badges","📋 History"},
            {"💬 Forums","🖼️ Gallery","🤝 Teams","🌍 Global"}
    };

    public OnboardingAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.fragment_onboard_page, parent, false);
        return new PageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PageHolder holder, int position) {
        holder.tvEmoji.setText(emojis[position]);
        holder.tvTitle.setText(titles[position]);
        holder.tvSub.setText(subtitles[position]);
        holder.tvChip1.setText(chips[position][0]);
        holder.tvChip2.setText(chips[position][1]);
        holder.tvChip3.setText(chips[position][2]);
        holder.tvChip4.setText(chips[position][3]);
    }

    @Override
    public int getItemCount() { return 3; }

    static class PageHolder extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvTitle, tvSub, tvChip1, tvChip2, tvChip3, tvChip4;

        PageHolder(@NonNull View itemView) {
            super(itemView);
            tvEmoji = itemView.findViewById(R.id.tv_onboard_emoji);
            tvTitle = itemView.findViewById(R.id.tv_onboard_title);
            tvSub   = itemView.findViewById(R.id.tv_onboard_sub);
            tvChip1 = itemView.findViewById(R.id.tv_chip1);
            tvChip2 = itemView.findViewById(R.id.tv_chip2);
            tvChip3 = itemView.findViewById(R.id.tv_chip3);
            tvChip4 = itemView.findViewById(R.id.tv_chip4);
        }
    }
}