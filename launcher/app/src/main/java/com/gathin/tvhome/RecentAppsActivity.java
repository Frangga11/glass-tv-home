package com.gathin.tvhome;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecentAppsActivity extends Activity implements RecentAppsAdapter.OnRecentAppInteractionListener {
    private TextView tvRamText;
    private ProgressBar pbRam;
    private RecyclerView rvRecentApps;
    private TextView tvEmptyRecents;
    private TextView tvBottomHint;
    private RecentAppsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_apps);

        tvRamText = findViewById(R.id.tvRamText);
        pbRam = findViewById(R.id.pbRam);
        rvRecentApps = findViewById(R.id.rvRecentApps);
        tvEmptyRecents = findViewById(R.id.tvEmptyRecents);
        tvBottomHint = findViewById(R.id.tvBottomHint);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvRecentApps.setLayoutManager(layoutManager);

        adapter = new RecentAppsAdapter(this);
        rvRecentApps.setAdapter(adapter);

        updateRamStats();
        loadRecentApps();
    }

    private void updateRamStats() {
        RecentAppsManager.MemoryStats stats = RecentAppsManager.getMemoryStats(this);
        tvRamText.setText(stats.formattedText);
        pbRam.setProgress(stats.percentUsed);
    }

    private void loadRecentApps() {
        List<RecentAppItem> apps = RecentAppsManager.getRecentApps(this);
        if (apps.isEmpty()) {
            showEmptyState();
        } else {
            tvEmptyRecents.setVisibility(View.GONE);
            rvRecentApps.setVisibility(View.VISIBLE);
            tvBottomHint.setVisibility(View.VISIBLE);
            adapter.setItems(apps);

            // Give initial focus to first card
            rvRecentApps.postDelayed(() -> {
                RecyclerView.ViewHolder vh = rvRecentApps.findViewHolderForAdapterPosition(0);
                if (vh != null) {
                    View focusTarget = vh.itemView.findViewById(R.id.cardContainer);
                    if (focusTarget != null) {
                        focusTarget.requestFocus();
                    }
                }
            }, 100);
        }
    }

    private void showEmptyState() {
        rvRecentApps.setVisibility(View.GONE);
        tvBottomHint.setVisibility(View.GONE);
        tvEmptyRecents.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAppClicked(RecentAppItem item) {
        if (item.launchIntent != null) {
            SoundEffects.playSelect();
            try {
                item.launchIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(item.launchIntent);
                finish();
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Cannot open app", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAppClosed(RecentAppItem item, int position, View itemView) {
        SoundEffects.playSelect();
        itemView.animate()
                .translationY(-320f)
                .alpha(0f)
                .setDuration(220)
                .withEndAction(() -> {
                    itemView.setTranslationY(0f);
                    itemView.setAlpha(1f);
                    RecentAppsManager.killApp(this, item.packageName);
                    adapter.removeItem(position);
                    updateRamStats();
                    if (adapter.isEmpty()) {
                        showEmptyState();
                    }
                })
                .start();
    }

    @Override
    public void onCloseAllClicked() {
        SoundEffects.playSelect();
        rvRecentApps.animate()
                .translationY(-260f)
                .alpha(0f)
                .setDuration(260)
                .withEndAction(() -> {
                    RecentAppsManager.closeAll(this, adapter.getItems());
                    adapter.clearAll();
                    updateRamStats();
                    showEmptyState();
                    Toast.makeText(this, "RAM cleared!", Toast.LENGTH_SHORT).show();
                })
                .start();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}
