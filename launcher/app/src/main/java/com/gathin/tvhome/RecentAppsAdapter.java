package com.gathin.tvhome;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecentAppsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_APP = 1;
    private static final int VIEW_TYPE_CLOSE_ALL = 2;

    public interface OnRecentAppInteractionListener {
        void onAppClicked(RecentAppItem item);
        void onAppClosed(RecentAppItem item, int position, View itemView);
        void onCloseAllClicked();
    }

    private final List<RecentAppItem> items = new ArrayList<>();
    private final OnRecentAppInteractionListener listener;

    public RecentAppsAdapter(OnRecentAppInteractionListener listener) {
        this.listener = listener;
    }

    public void setItems(List<RecentAppItem> newItems) {
        items.clear();
        items.addAll(newItems);
        // Add Close All as the last item if there are running apps
        if (!newItems.isEmpty()) {
            items.add(new RecentAppItem(true));
        }
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < items.size()) {
            items.remove(position);
            notifyItemRemoved(position);
            // If only Close All remains, remove it too
            if (items.size() == 1 && items.get(0).isCloseAll) {
                items.clear();
                notifyDataSetChanged();
            }
        }
    }

    public void clearAll() {
        items.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<RecentAppItem> getItems() {
        return items;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).isCloseAll ? VIEW_TYPE_CLOSE_ALL : VIEW_TYPE_APP;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_CLOSE_ALL) {
            View view = inflater.inflate(R.layout.item_recent_close_all, parent, false);
            return new CloseAllViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_recent_app, parent, false);
            return new AppViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecentAppItem item = items.get(position);
        if (holder instanceof AppViewHolder) {
            ((AppViewHolder) holder).bind(item, listener);
        } else if (holder instanceof CloseAllViewHolder) {
            ((CloseAllViewHolder) holder).bind(listener);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivSmallIcon;
        private final TextView tvAppLabel;
        private final FrameLayout cardContainer;
        private final ImageView ivCardIcon;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSmallIcon = itemView.findViewById(R.id.ivSmallIcon);
            tvAppLabel = itemView.findViewById(R.id.tvAppLabel);
            cardContainer = itemView.findViewById(R.id.cardContainer);
            ivCardIcon = itemView.findViewById(R.id.ivCardIcon);
        }

        public void bind(RecentAppItem item, OnRecentAppInteractionListener listener) {
            tvAppLabel.setText(item.label);
            if (item.icon != null) {
                ivSmallIcon.setImageDrawable(item.icon);
                ivCardIcon.setImageDrawable(item.icon);
            }

            cardContainer.setOnFocusChangeListener((v, hasFocus) -> {
                float scale = hasFocus ? 1.06f : 1.0f;
                v.animate().scaleX(scale).scaleY(scale).setDuration(160).start();
            });

            cardContainer.setOnClickListener(v -> {
                if (listener != null) listener.onAppClicked(item);
            });

            cardContainer.setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (listener != null) {
                        int pos = getBindingAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            listener.onAppClosed(item, pos, itemView);
                        }
                    }
                    return true;
                }
                return false;
            });
        }
    }

    static class CloseAllViewHolder extends RecyclerView.ViewHolder {
        private final FrameLayout btnCircle;

        public CloseAllViewHolder(@NonNull View itemView) {
            super(itemView);
            btnCircle = itemView.findViewById(R.id.btnCircle);
        }

        public void bind(OnRecentAppInteractionListener listener) {
            btnCircle.setOnFocusChangeListener((v, hasFocus) -> {
                float scale = hasFocus ? 1.12f : 1.0f;
                v.animate().scaleX(scale).scaleY(scale).setDuration(160).start();
            });

            btnCircle.setOnClickListener(v -> {
                if (listener != null) listener.onCloseAllClicked();
            });
        }
    }
}
