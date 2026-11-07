package com.gathin.tvhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

/** Dedicated Dock renderer. It reads the same authoritative inventory as AppAdapter but owns
 * its own adapter positions, orientation, sizing and focus callbacks. */
final class DockAdapter extends RecyclerView.Adapter<AppAdapter.Holder> {
    interface Listener {
        void onDockTileFocused(String key, View card);
        boolean onDockTileKey(String key, int keyCode);
        boolean onDockTileHeld(String key, View card);
    }

    private final Context context;
    private final AppAdapter source;
    private final Listener listener;
    private final TileBinder binder;
    private final boolean reducedMotion;
    private boolean iconAnimationsEnabled;
    private int capacity = 5;
    private List<AppAdapter.Entry> entries = Collections.emptyList();
    private boolean vertical;

    DockAdapter(Context context, AppAdapter source, Listener listener, boolean reducedMotion) {
        this.context = context;
        this.source = source;
        this.listener = listener;
        this.binder = new TileBinder(context);
        this.reducedMotion = reducedMotion;
        this.iconAnimationsEnabled = context.getSharedPreferences("wallpaper_prefs", Context.MODE_PRIVATE)
                .getBoolean("icon_animations_enabled", true);
        setHasStableIds(true);
        refresh();
    }

    void refresh() {
        capacity = DockPreferences.load(context).capacity;
        DockLayoutModel.Snapshot<AppAdapter.Entry> snapshot = source.dockSnapshot();
        entries = snapshot == null ? Collections.emptyList() : snapshot.dockItems;
        notifyDataSetChanged();
    }

    void setVertical(boolean vertical) {
        if (this.vertical == vertical) return;
        this.vertical = vertical;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        // A changed capacity/orientation must recreate holders so their measured tile dimensions
        // cannot remain stale after a live settings update.
        return DockLayoutModel.dockViewType(vertical, capacity);
    }

    @NonNull
    @Override
    public AppAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
        float density = parent.getResources().getDisplayMetrics().density;
        int inner = Math.max(0, parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight());
        int width;
        if (vertical) {
            width = Math.max(Math.round(112 * density), Math.min(Math.round(170 * density), inner - Math.round(20 * density)));
        } else {
            width = DockGeometry.horizontalTileWidthPx(inner, capacity, density);
        }
        int height = AppAdapter.tileHeightPx(width);
        View card = item.findViewById(R.id.card);
        ViewGroup.LayoutParams cardParams = card.getLayoutParams();
        cardParams.width = width;
        cardParams.height = height;
        card.setLayoutParams(cardParams);
        View name = item.findViewById(R.id.name);
        ViewGroup.MarginLayoutParams nameParams = (ViewGroup.MarginLayoutParams) name.getLayoutParams();
        nameParams.width = width;
        nameParams.topMargin = height + Math.round(7 * density);
        name.setLayoutParams(nameParams);

        AppAdapter.TileListener holderListener = new AppAdapter.TileListener() {
            @Override public void onTileFocused(int position, View focusedCard) {
                String key = keyAt(position);
                if (key != null && listener != null) listener.onDockTileFocused(key, focusedCard);
            }
            @Override public boolean onTileVerticalKey(int position, int keyCode) {
                String key = keyAt(position);
                return key != null && listener != null && listener.onDockTileKey(key, keyCode);
            }
            @Override public boolean onTileHeld(int position, View focusedCard) {
                String key = keyAt(position);
                return key != null && listener != null && listener.onDockTileHeld(key, focusedCard);
            }
        };
        return new AppAdapter.Holder(item, holderListener, reducedMotion, iconAnimationsEnabled, true);
    }

    @Override
    public void onBindViewHolder(@NonNull AppAdapter.Holder holder, int position) {
        AppAdapter.Entry entry = entries.get(position);
        binder.bind(holder, entry, false, iconAnimationsEnabled);
        holder.card.setOnClickListener(v -> binder.launch(entry));
        holder.card.setOnLongClickListener(v -> {
            String key = keyAt(holder.getBindingAdapterPosition());
            if (key != null && listener != null && listener.onDockTileHeld(key, v)) return true;
            source.openAppInfoByKey(key);
            return true;
        });
    }

    @Override public int getItemCount() { return entries.size(); }

    @Override
    public long getItemId(int position) {
        return entries.get(position).component.flattenToShortString().hashCode();
    }

    String keyAt(int position) {
        return position >= 0 && position < entries.size()
                ? entries.get(position).component.flattenToShortString() : null;
    }

    int positionOf(String key) {
        if (key == null) return -1;
        for (int i = 0; i < entries.size(); i++) {
            if (key.equals(entries.get(i).component.flattenToShortString())) return i;
        }
        return -1;
    }

    String labelAt(int position) {
        return position >= 0 && position < entries.size() ? entries.get(position).label : "";
    }

    int ambientColorAt(int position) {
        if (position < 0 || position >= entries.size()) return 0xFF444444;
        AppAdapter.Entry entry = entries.get(position);
        return source.ambientColorFor(entry);
    }

    boolean canMove(int position) {
        return position >= 0 && position < entries.size()
                && entries.get(position).kind != AppAdapter.Entry.HOME_SETTINGS;
    }

    int lastMovable() {
        int last = entries.size() - 1;
        while (last >= 0 && entries.get(last).kind == AppAdapter.Entry.HOME_SETTINGS) last--;
        return last;
    }

    boolean move(int from, int to) {
        boolean moved = source.moveDock(from, to);
        if (moved) refresh();
        return moved;
    }

    void setIconAnimationsEnabled(boolean enabled, RecyclerView recyclerView) {
        iconAnimationsEnabled = enabled;
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            if (holder instanceof AppAdapter.Holder) ((AppAdapter.Holder) holder).setIconAnimationsEnabled(enabled);
        }
    }
}
