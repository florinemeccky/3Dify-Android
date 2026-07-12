package com.example.a3dify.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3dify.R;
import com.example.a3dify.activities.SoftwareDetailActivity;
import com.example.a3dify.models.SoftwareTool;
import java.util.List;

public class SoftwareToolAdapter
    extends RecyclerView.Adapter<SoftwareToolAdapter.ToolHolder> {

    private final Context context;
    private final List<SoftwareTool> tools;

    public SoftwareToolAdapter(Context context, List<SoftwareTool> tools) {
        this.context = context;
        this.tools   = tools;
    }

    @NonNull
    @Override
    public ToolHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
            .inflate(R.layout.item_software_tool, parent, false);
        return new ToolHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ToolHolder holder, int position) {
        SoftwareTool tool = tools.get(position);

        // Initial letter
        if (holder.tvInitial != null) {
            holder.tvInitial.setText(
                String.valueOf(tool.getName().charAt(0)));
            holder.tvInitial.setTextColor(tool.getIconColor());
        }

        // Avatar background tint
        if (holder.llAvatar != null) {
            int dimColor = Color.argb(30,
                Color.red(tool.getIconColor()),
                Color.green(tool.getIconColor()),
                Color.blue(tool.getIconColor()));
            holder.llAvatar.setBackgroundColor(dimColor);
        }

        if (holder.tvName    != null) holder.tvName.setText(tool.getName());
        if (holder.tvTagline != null) holder.tvTagline.setText(tool.getTagline());

        // Pricing badge
        if (holder.tvPricing != null) {
            holder.tvPricing.setText(tool.getPricing().contains("Free")
                ? "Free" : "Paid");
            if (tool.getPricing().startsWith("Free")) {
                holder.tvPricing.setTextColor(Color.parseColor("#34D399"));
                holder.tvPricing.setBackgroundResource(R.drawable.bg_badge_free);
            } else {
                holder.tvPricing.setTextColor(Color.parseColor("#FBBF24"));
                holder.tvPricing.setBackgroundResource(R.drawable.bg_badge_paid);
            }
        }

        // Tap → SoftwareDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SoftwareDetailActivity.class);
            intent.putExtra("software_name", tool.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return tools.size(); }

    static class ToolHolder extends RecyclerView.ViewHolder {
        LinearLayout llAvatar;
        TextView tvInitial, tvName, tvTagline, tvPricing;

        ToolHolder(@NonNull View v) {
            super(v);
            llAvatar  = v.findViewById(R.id.ll_software_avatar);
            tvInitial = v.findViewById(R.id.tv_software_initial);
            tvName    = v.findViewById(R.id.tv_software_name);
            tvTagline = v.findViewById(R.id.tv_software_tagline);
            tvPricing = v.findViewById(R.id.tv_software_pricing);
        }
    }
}