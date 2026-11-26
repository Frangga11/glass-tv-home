package com.gathin.tvhome;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * App tiles. The first {@link MainActivity#COLUMNS} positions are the pinned dock (favourites, fixed order),
 * then the module cards the owner chose to show (by label), the rest is alphabetical. Once anyone has
 * moved a tile (hold OK, then Move), the order they made is kept instead, and apps installed later
 * join at the end.
 */
public class AppAdapter extends RecyclerView.Adapter<AppAdapter.Holder> {

    interface TileListener {
        void onTileFocused(int position, View card);

        /** Called for a D-pad direction key on a tile. Return true when handled (focus stays put). */
        boolean onTileVerticalKey(int position, int keyCode);

        /** OK was held on a tile. Return false to open the app's info page, as before. */
        boolean onTileHeld(int position, View card);
    }

    static final class Entry {
        static final int APP = 0, HOME_SETTINGS = 1, CARD = 2, SYSTEM_HOME = 3;

        final int kind;
        final String label;
        final ComponentName component;
        final String pkg;
        final Drawable banner;
        final Drawable icon;
        /** Text under the tile when it is focused in the grid: the label, plus the caption for a card. */
        final String name;
        /** Ambient glow colour; 0 until sampled from the banner, or the card's accent from the start. */
        int ambient;

        Entry(String label, ComponentName component, Drawable banner, Drawable icon) {
            this(APP, label, component, banner, icon, null, 0);
        }

        Entry(int kind, String label, ComponentName component, Drawable banner, Drawable icon, String caption, int accent) {
            this.kind = kind;
            this.label = label;
            this.component = component;
            this.pkg = component.getPackageName();
            this.banner = banner;
            this.icon = icon;
            this.name = caption == null ? label : label + " \u00B7 " + caption;
            this.ambient = accent;
        }
    }

    /** Classic Home Universal focus zoom: Dock 1.12x, Grid 1.10x. */
    static final float DOCK_FOCUS_SCALE = 1.12f;
    static final float GRID_FOCUS_SCALE = 1.10f;
    /** Keep the existing layout reservation so normal icon dimensions and spacing do not change. */
    static final float LAYOUT_FOCUS_RESERVATION_SCALE = 1.07f;
    static float focusScale(boolean dockTile) {
        return dockTile ? DOCK_FOCUS_SCALE : GRID_FOCUS_SCALE;
    }
    /** Shared by every tile's zoom, so a focus move makes no new objects. */
    private static final DecelerateInterpolator ZOOM_REDUCED = new DecelerateInterpolator();
    private static final OvershootInterpolator ZOOM_IN = new OvershootInterpolator(1.8f);
    private static final DecelerateInterpolator ZOOM_OUT = new DecelerateInterpolator();
    /** Design size of a tile (dp); tiles shrink from here when the columns don't fit, see tileWidthPx. */
    static final float DESIGN_W_DP = 180f, DESIGN_H_DP = 101f;

    /**
     * Tile width for a grid of this inner width: the cell minus room for the focus zoom and the glass
     * highlight, capped at the design size so 4 columns on a 1080p TV look exactly as before.
     */
    static int tileWidthPx(int innerWidthPx, int columns, float density) {
        int design = Math.round(DESIGN_W_DP * density);
        if (innerWidthPx <= 0 || columns <= 0) return design;
        int cell = innerWidthPx / columns;
        int fit = (int) ((cell - 18 * density) / LAYOUT_FOCUS_RESERVATION_SCALE);
        return Math.max(Math.round(80 * density), Math.min(design, fit));
    }

    static int tileHeightPx(int tileWidthPx) {
        return Math.round(tileWidthPx * DESIGN_H_DP / DESIGN_W_DP);
    }

    /** Height of one grid item: the tile, the name label under it and the item's own padding. */
    static int itemHeightPx(int tileHeightPx, float density) {
        return tileHeightPx + Math.round(45 * density);
    }

    private final Context ctx;
    private final TileListener listener;
    private final int gridColumns;
    /** Dock order (config.xml dock_apps, or a theme's config.json). */
    private final List<String> favourites;
    /** Never shown as tiles (res/values/config.xml, hidden_apps). */
    private final Set<String> hidden;
    private final boolean showHomeSettings;
    /** Calmer motion for weak GPUs (config.xml, reduced_motion): no overshoot, shorter fades. */
    private final boolean reducedMotion;
    private boolean iconAnimationsEnabled;
    /** Single ordered source of truth; Dock/Grid are projections, never independent app inventories. */
    private final TileBinder tileBinder;
    private final AppInventory<Entry> items = new AppInventory<>(
            entry -> entry.component.flattenToShortString());
    /** The order someone arranged the tiles in, by key (see {@link #keyAt}); empty until they do. */
    private final List<String> arranged = new ArrayList<>();
    /** Vertical Classic mode derives Dock membership from one ordered app list, as Home Universal does. */
    private boolean classicVerticalMode;
    private DockLayoutModel.Snapshot<Entry> presentationSnapshot;
    /** Classic Move Mode edits are previews until OK commits; BACK restores this snapshot. */
    private boolean moveTransactionActive;
    private List<Entry> moveOriginalItems;
    private List<String> moveOriginalArranged;
    private List<String> moveOriginalPinnedKeys;
    private List<String> moveOriginalUnpinnedKeys;
    private List<String> movePreviewPinnedKeys;
    private List<String> movePreviewUnpinnedKeys;
    private static final String ORDER_PREFS = "tiles", ORDER_KEY = "order";
    AppAdapter(Context ctx, TileListener listener, int gridColumns, Config cfg) {
        this.ctx = ctx;
        this.listener = listener;
        this.tileBinder = new TileBinder(ctx);
        this.gridColumns = Math.max(1, gridColumns);
        this.favourites = cfg.dockApps;
        this.hidden = new HashSet<>(cfg.hiddenApps);
        this.showHomeSettings = cfg.showHomeSettingsShortcut;
        this.reducedMotion = cfg.reducedMotion;
        this.iconAnimationsEnabled = ctx.getSharedPreferences("wallpaper_prefs", Context.MODE_PRIVATE)
                .getBoolean("icon_animations_enabled", true);
        // Tiles keep their identity across reload(), so a focused tile stays on its app instead of
        // its view being rebound to whatever app now sits in that slot.
        setHasStableIds(true);
        String saved = ctx.getSharedPreferences(ORDER_PREFS, Context.MODE_PRIVATE).getString(ORDER_KEY, "");
        if (!saved.isEmpty()) arranged.addAll(Arrays.asList(saved.split("\n")));
    }

    /**
     * Rebuilds the tiles. A shown module card stands in for every tile of its package (same component,
     * so focus follows it); hidden_apps wins over a card, and a card's package in dock_apps takes that slot.
     */
    /** What the tiles were last built from; the same list next time means there is nothing to redo. */
    private String builtFrom;

    void invalidateCache() {
        builtFrom = null;
    }
    /**
     * The app tiles of the last build, by component and install path: a rebuild (one app updated,
     * installed or removed) loads only what changed, and the rest keep their artwork and sampled glow.
     */
    private Map<String, Entry> builtApps = new HashMap<>();

    /**
     * Rebuilds the tiles, unless the launchable apps, their installed versions and the cards are
     * exactly what the tiles were last built from. Loading every banner, icon and label is most of
     * what coming back to Home costs, it runs before Home can draw, and almost every return finds
     * nothing changed.
     *
     * @return whether the tiles were rebuilt
     */
    void setClassicVerticalMode(boolean enabled) {
        if (classicVerticalMode == enabled) return;
        classicVerticalMode = enabled;
        if (enabled) {
            // Home Universal persists package names. Convert legacy Glass component keys once,
            // so the Classic inventory and its saved order share the same identity model.
            List<String> packages = new ArrayList<>();
            for (String savedKey : arranged) {
                int slash = savedKey.indexOf('/');
                String pkg = slash > 0 ? savedKey.substring(0, slash) : savedKey;
                int at = pkg.indexOf('@');
                if (at >= 0) pkg = pkg.substring(0, at);
                if (!pkg.isEmpty() && !packages.contains(pkg)) packages.add(pkg);
            }
            if (!arranged.equals(packages)) {
                arranged.clear();
                arranged.addAll(packages);
                // Persist the migration immediately; otherwise each process start would
                // re-parse the legacy component-key format before the first catalog reload.
                ctx.getSharedPreferences(ORDER_PREFS, Context.MODE_PRIVATE).edit()
                        .putString(ORDER_KEY, android.text.TextUtils.join("\n", arranged)).apply();
            }
        }
        presentationSnapshot = null;
    }

    /** Publish a fresh Dock/Grid partition after mode or capacity preferences change. */
    void refreshPresentation() {
        presentationSnapshot = null;
        notifyDataSetChanged();
    }

    boolean isClassicVerticalMode() { return classicVerticalMode; }
    int classicDockCapacity() { return gridColumns; }

    boolean reload(List<Cards.Card> cards) {
        // A package/catalog refresh invalidates adapter positions; never carry a preview across it.
        if (moveTransactionActive) cancelMoveTransaction();
        PackageManager pm = ctx.getPackageManager();
        Intent probe = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> found = new ArrayList<>(pm.queryIntentActivities(probe, 0));
        // Match Home Universal's complete app catalog in both modes: TV launcher activities
        // first, then ordinary launcher activities. The first launchable activity per package
        // is authoritative in both the Classic Grid and the Horizontal all-app Carousel.
        found.addAll(pm.queryIntentActivities(new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER), 0));
        // The install path moves when an app is updated, so it stands in for the version: an update
        // that brought a new banner still gets a rebuild.
        List<String> parts = new ArrayList<>();
        for (ResolveInfo ri : found) {
            if (ri.activityInfo == null) continue;
            parts.add(ri.activityInfo.packageName + '/' + ri.activityInfo.name + '@'
                    + ri.activityInfo.applicationInfo.sourceDir);
        }
        for (Cards.Card c : cards) {
            parts.add('#' + c.pkg + '/' + c.component.flattenToShortString() + '|' + c.label + '|'
                    + c.caption + '|' + c.accent);
        }
        android.content.pm.ActivityInfo classicNativeHome = classicVerticalMode
                ? ClassicSystemHome.resolve(ctx) : null;
        if (classicNativeHome != null) {
            parts.add("@system-home:" + classicNativeHome.packageName + "/" + classicNativeHome.name);
        }
        // The same installed packages can expose different launchable activities by app mode.
        // Include the catalog policy in the signature so switching modes cannot reuse a stale inventory.
        parts.add("@classic-mode:" + classicVerticalMode);
        Collections.sort(parts);
        String signature = android.text.TextUtils.join(";", parts);
        if (signature.equals(builtFrom) && !items.isEmpty()) return false;
        builtFrom = signature;
        Map<String, Cards.Card> cardByPkg = new HashMap<>();
        for (Cards.Card c : cards) cardByPkg.put(c.pkg, c);
        List<Entry> fresh = new ArrayList<>();
        Map<String, Entry> apps = new HashMap<>();
        Set<String> seenPackages = new HashSet<>();
        for (ResolveInfo ri : found) {
            if (ri.activityInfo == null) continue;
            String pkg = ri.activityInfo.packageName;
            if (!seenPackages.add(pkg)) continue;
            if (pkg.equals(ctx.getPackageName()) || hidden.contains(pkg) || ChildModeManager.isAppHidden(ctx, pkg)) continue;
            ComponentName component = new ComponentName(pkg, ri.activityInfo.name);
            Cards.Card card = cardByPkg.get(pkg);
            if (card != null && !card.component.equals(component)) continue;   // the card stands in for this tile
            String key = component.flattenToShortString() + '@' + ri.activityInfo.applicationInfo.sourceDir;
            Entry built = card == null ? builtApps.get(key) : null;
            if (built != null) {
                fresh.add(built);
                apps.put(key, built);
                continue;
            }
            Drawable banner = loadBanner(pm, ri);
            // The icon only shows on a tile without a banner.
            Drawable icon = banner == null ? ri.loadIcon(pm) : null;
            if (card != null) {
                fresh.add(new Entry(Entry.CARD, card.label, component, banner, icon, card.caption, card.accent));
            } else {
                // Same cleaner as the card prompt: an app label with a bidi override or line separator must not
                // redraw the tile name as something else (#117).
                Entry entry = new Entry(Cards.cleanLabel(String.valueOf(ri.loadLabel(pm)), pkg), component, banner, icon);
                fresh.add(entry);
                apps.put(key, entry);
            }
        }
        builtApps = apps;
        Entry classicSystemHomeEntry = null;
        if (classicNativeHome != null) {
            // Keep System Home in the same movable, persisted inventory. It used to be appended
            // after sorting, which silently discarded a user-moved position on the next reload.
            for (int i = fresh.size() - 1; i >= 0; i--) {
                if (classicNativeHome.packageName.equals(fresh.get(i).pkg)) fresh.remove(i);
            }
            ComponentName homeComponent = new ComponentName(
                    classicNativeHome.packageName, classicNativeHome.name);
            classicSystemHomeEntry = new Entry(Entry.SYSTEM_HOME, ctx.getString(R.string.system_home),
                    homeComponent, ctx.getDrawable(R.drawable.classic_system_home_banner), null, null, 0);
        }

        List<String> classicOrder = null;
        Map<String, Integer> place = new HashMap<>();
        if (classicVerticalMode) {
            List<Entry> alphabeticalApps = new ArrayList<>(fresh);
            Collections.sort(alphabeticalApps, (a, b) -> {
                int byLabel = a.label.compareToIgnoreCase(b.label);
                return byLabel != 0 ? byLabel : a.pkg.compareTo(b.pkg);
            });
            List<String> installedPackages = new ArrayList<>();
            for (Entry entry : alphabeticalApps) installedPackages.add(entry.pkg);
            if (classicNativeHome != null && !installedPackages.contains(classicNativeHome.packageName)) {
                // Preserve the existing default: System Home starts after ordinary apps unless
                // a saved Classic order explicitly places it elsewhere.
                installedPackages.add(classicNativeHome.packageName);
            }
            classicOrder = ClassicAppOrder.merge(arranged, favourites, installedPackages);
            if (!moveTransactionActive && !arranged.equals(classicOrder)) {
                arranged.clear();
                arranged.addAll(classicOrder);
                ctx.getSharedPreferences(ORDER_PREFS, Context.MODE_PRIVATE).edit()
                        .putString(ORDER_KEY, android.text.TextUtils.join("\n", arranged)).apply();
            }
            for (int i = 0; i < classicOrder.size(); i++) place.put(classicOrder.get(i), i);
        } else {
            for (int i = 0; i < arranged.size(); i++) place.put(arranged.get(i), i);
        }
        Collections.sort(fresh, (a, b) -> {
            // Classic persists package order like Home Universal. Accept that package order
            // when switching to Horizontal; legacy component-key orders remain supported too.
            Integer pa = place.get(a.component.flattenToShortString());
            Integer pb = place.get(b.component.flattenToShortString());
            if (pa == null) pa = place.get(a.pkg);
            if (pb == null) pb = place.get(b.pkg);
            if (pa != null || pb != null) {
                if (pa == null) return 1;
                if (pb == null) return -1;
                return Integer.compare(pa, pb);
            }
            int fa = favourites.indexOf(a.pkg), fb = favourites.indexOf(b.pkg);
            if (fa >= 0 || fb >= 0) {
                if (fa < 0) return 1;
                if (fb < 0) return -1;
                return Integer.compare(fa, fb);
            }
            // Cards come right after the configured dock apps, before the alphabetical grid.
            boolean ca = a.kind == Entry.CARD, cb = b.kind == Entry.CARD;
            if (ca != cb) return ca ? -1 : 1;
            return a.label.compareToIgnoreCase(b.label);
        });
        if (classicVerticalMode) {
            if (classicSystemHomeEntry != null) {
                Integer homePosition = place.get(classicSystemHomeEntry.pkg);
                int insertAt = homePosition == null ? fresh.size()
                        : Math.max(0, Math.min(homePosition, fresh.size()));
                fresh.add(insertAt, classicSystemHomeEntry);
            }
        } else if (showHomeSettings
                && pm.resolveActivity(new Intent(Settings.ACTION_HOME_SETTINGS), 0) != null) {
            fresh.add(new Entry(Entry.HOME_SETTINGS, ctx.getString(R.string.home_settings),
                    new ComponentName(ctx, HomeSettingsActivity.class), null,
                    ctx.getDrawable(R.drawable.ic_home_settings), null, 0));
        }
        items.replace(fresh);
        presentationSnapshot = null;
        notifyDataSetChanged();
        return true;
    }

    private Drawable loadBanner(PackageManager pm, ResolveInfo ri) {
        int id = ri.activityInfo.banner;
        if (id == 0) id = ri.activityInfo.applicationInfo.banner;
        if (id != 0) {
            try {
                Resources resources = pm.getResourcesForApplication(ri.activityInfo.applicationInfo);
                // Keep at least xhdpi detail rather than downsampling artwork before the focus zoom.
                int density = Math.max(DisplayMetrics.DENSITY_XHIGH, ctx.getResources().getDisplayMetrics().densityDpi);
                return resources.getDrawableForDensity(id, density, null);
            } catch (PackageManager.NameNotFoundException | Resources.NotFoundException ignored) {
            }
        }
        return ri.activityInfo.loadBanner(pm);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
        float density = parent.getResources().getDisplayMetrics().density;
        int width = parent.getWidth() > 0 ? parent.getWidth() : parent.getMeasuredWidth();
        int inner = width - parent.getPaddingLeft() - parent.getPaddingRight();
        int w = tileWidthPx(inner, gridColumns, density), h = tileHeightPx(w);
        View card = v.findViewById(R.id.card);
        ViewGroup.LayoutParams cardLp = card.getLayoutParams();
        cardLp.width = w;
        cardLp.height = h;
        card.setLayoutParams(cardLp);
        View name = v.findViewById(R.id.name);
        ViewGroup.MarginLayoutParams nameLp = (ViewGroup.MarginLayoutParams) name.getLayoutParams();
        nameLp.width = w;
        nameLp.topMargin = h + Math.round(7 * density);
        name.setLayoutParams(nameLp);
        return new Holder(v, listener, reducedMotion, iconAnimationsEnabled, false);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        Entry e = gridSnapshot().gridItems.get(position);
        tileBinder.bind(h, e, true, iconAnimationsEnabled);
        h.card.setOnClickListener(v -> tileBinder.launch(e));
        h.card.setOnLongClickListener(v -> {
            int pos = h.getBindingAdapterPosition();
            if (listener != null && pos != RecyclerView.NO_POSITION && listener.onTileHeld(pos, v)) return true;
            openAppInfo(e);
            return true;
        });
    }
    /** Android's page for the app at {@code position}: storage, permissions, uninstall. */
    void openAppInfo(int position) {
        List<Entry> grid = gridSnapshot().gridItems;
        if (position >= 0 && position < grid.size()) openAppInfo(grid.get(position));
    }

    void openAppInfoByKey(String key) {
        int index = inventoryPositionOf(key);
        if (index >= 0) openAppInfo(items.get(index));
    }

    private int inventoryPositionOf(String key) {
        return items.indexOfKey(key);
    }

    private void openAppInfo(Entry e) {
        Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + e.pkg));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            ctx.startActivity(i);
        } catch (ActivityNotFoundException ignored) {
        }
    }

    /** Whether the tile at {@code position} can be moved: every app can, the Default home tile cannot. */
    boolean canMove(int position) {
        List<Entry> grid = gridSnapshot().gridItems;
        return position >= 0 && position < grid.size() && grid.get(position).kind != Entry.HOME_SETTINGS;
    }

    /** The last position a moved tile can take: before the Default home tile, which stays last. */
    int lastMovable() {
        List<Entry> grid = gridSnapshot().gridItems;
        int last = grid.size() - 1;
        while (last >= 0 && grid.get(last).kind == Entry.HOME_SETTINGS) last--;
        return last;
    }

    /** Begin an in-memory Classic Move Mode transaction. */
    boolean beginMoveTransaction() {
        if (moveTransactionActive) return true;
        moveOriginalItems = new ArrayList<>(items.snapshot());
        moveOriginalArranged = new ArrayList<>(arranged);
        moveOriginalPinnedKeys = currentPinnedKeys();
        moveOriginalUnpinnedKeys = DockPreferences.loadUnpinnedKeys(ctx);
        movePreviewPinnedKeys = new ArrayList<>(moveOriginalPinnedKeys);
        movePreviewUnpinnedKeys = new ArrayList<>(moveOriginalUnpinnedKeys);
        moveTransactionActive = true;
        presentationSnapshot = null;
        return true;
    }

    boolean isMoveTransactionActive() {
        return moveTransactionActive;
    }

    /** Commit the preview only after the user presses OK. */
    boolean commitMoveTransaction() {
        if (!moveTransactionActive) return true;
        if (!classicVerticalMode
                && !DockPreferences.savePinnedState(ctx, movePreviewPinnedKeys, movePreviewUnpinnedKeys)) {
            return false;
        }
        boolean orderSaved = ctx.getSharedPreferences(ORDER_PREFS, Context.MODE_PRIVATE).edit()
                .putString(ORDER_KEY, android.text.TextUtils.join("\n", arranged))
                .commit();
        if (!orderSaved) {
            DockPreferences.savePinnedState(ctx, moveOriginalPinnedKeys, moveOriginalUnpinnedKeys);
            return false;
        }
        clearMoveTransaction();
        presentationSnapshot = null;
        notifyDataSetChanged();
        return true;
    }

    /** Discard every previewed reorder when the user presses BACK. */
    void cancelMoveTransaction() {
        if (!moveTransactionActive) return;
        items.replace(moveOriginalItems);
        arranged.clear();
        arranged.addAll(moveOriginalArranged);
        clearMoveTransaction();
        presentationSnapshot = null;
        notifyDataSetChanged();
    }

    private void clearMoveTransaction() {
        moveTransactionActive = false;
        moveOriginalItems = null;
        moveOriginalArranged = null;
        moveOriginalPinnedKeys = null;
        moveOriginalUnpinnedKeys = null;
        movePreviewPinnedKeys = null;
        movePreviewUnpinnedKeys = null;
    }

    private List<String> currentUnpinnedKeys() {
        return moveTransactionActive
                ? new ArrayList<>(movePreviewUnpinnedKeys)
                : DockPreferences.loadUnpinnedKeys(ctx);
    }

    private boolean savePinnedState(List<String> pinned, List<String> unpinned) {
        if (!moveTransactionActive) return DockPreferences.savePinnedState(ctx, pinned, unpinned);
        movePreviewPinnedKeys = new ArrayList<>(DockPreferences.normalizePinnedKeys(pinned));
        movePreviewUnpinnedKeys = new ArrayList<>(DockPreferences.normalizePinnedKeys(unpinned));
        presentationSnapshot = null;
        return true;
    }

    /**
     * Moves the tile at {@code from} to {@code to}, the tiles between shifting to make room, and keeps
     * the new order. Returns false when it cannot go there.
     */
    boolean move(int from, int to) {
        DockLayoutModel.Snapshot<Entry> snapshot = dockSnapshot();
        List<Entry> grid = new ArrayList<>(snapshot.gridItems);
        if (from == to || !canMove(from) || to < 0 || to > lastMovable()) return false;
        Entry moved = grid.remove(from);
        grid.add(to, moved);
        replacePresentationOrder(snapshot.dockItems, grid);
        return true;
    }

    boolean moveDock(int from, int to) {
        DockLayoutModel.Snapshot<Entry> snapshot = dockSnapshot();
        List<String> pinned = currentPinnedKeys();
        if (from == to || from < 0 || to < 0 || from >= snapshot.dockItems.size()
                || to >= snapshot.dockItems.size() || from >= pinned.size() || to >= pinned.size()) return false;
        List<String> reordered = DockLayoutModel.movePinnedKey(pinned, from, to, snapshot.dockItems.size());
        if (reordered.equals(pinned) || !savePinnedState(reordered, currentUnpinnedKeys())) return false;
        if (moveTransactionActive) {
            Map<String, Entry> entriesByKey = new HashMap<>();
            for (Entry entry : snapshot.dockItems) {
                if (entry.component != null) {
                    entriesByKey.put(entry.component.flattenToShortString(), entry);
                }
            }
            List<Entry> orderedDock = new ArrayList<>();
            for (String key : reordered) {
                Entry entry = entriesByKey.get(key);
                if (entry != null) orderedDock.add(entry);
            }
            // Classic Mode is one ordered list: Dock first, then Grid, just like Home Universal.
            replacePresentationOrder(orderedDock, snapshot.gridItems);
        } else {
            presentationSnapshot = null;
            notifyDataSetChanged();
        }
        return true;
    }

    boolean pinToDock(String key, int requestedPosition) {
        if (key == null || key.trim().isEmpty()) return false;
        List<String> pinned = currentPinnedKeys();
        List<String> updated = DockLayoutModel.pinKey(pinned, key, requestedPosition, DockPreferences.load(ctx).capacity);
        if (updated.equals(pinned)) return false;
        List<String> excluded = currentUnpinnedKeys();
        excluded.remove(key);
        if (!savePinnedState(updated, excluded)) return false;
        if (moveTransactionActive) {
            DockLayoutModel.Snapshot<Entry> snapshot = dockSnapshot();
            replacePresentationOrder(snapshot.dockItems, snapshot.gridItems);
        } else {
            presentationSnapshot = null;
            notifyDataSetChanged();
        }
        return true;
    }

    boolean unpinFromDock(String key) {
        if (key == null || key.trim().isEmpty()) return false;
        if (moveTransactionActive) {
            return unpinFromDockAtGridPosition(key, dockSnapshot().gridItems.size());
        }
        List<String> pinned = currentPinnedKeys();
        List<String> updated = DockLayoutModel.unpinKey(pinned, key);
        if (updated.equals(pinned)) return false;
        List<String> excluded = currentUnpinnedKeys();
        if (!excluded.contains(key)) excluded.add(key);
        if (!savePinnedState(updated, excluded)) return false;
        presentationSnapshot = null;
        notifyDataSetChanged();
        return true;
    }

    /**
     * Removes a visible Dock tile and inserts it into the Grid at the requested position.
     * Used by horizontal Move Mode so DOWN returns directly below the Dock slot currently occupied,
     * rather than restoring the app's stale pre-Dock Grid position.
     */
    boolean unpinFromDockAtGridPosition(String key, int gridPosition) {
        if (key == null || key.trim().isEmpty()) return false;
        DockLayoutModel.Snapshot<Entry> snapshot = dockSnapshot();
        List<Entry> dock = new ArrayList<>(snapshot.dockItems);
        Entry selected = null;
        for (int i = 0; i < dock.size(); i++) {
            Entry candidate = dock.get(i);
            if (key.equals(candidate.component.flattenToShortString())) {
                selected = dock.remove(i);
                break;
            }
        }
        if (selected == null) return false;

        List<String> pinned = currentPinnedKeys();
        List<String> updated = DockLayoutModel.unpinKey(pinned, key);
        if (updated.equals(pinned)) return false;
        List<String> excluded = currentUnpinnedKeys();
        if (!excluded.contains(key)) excluded.add(key);
        if (!savePinnedState(updated, excluded)) return false;

        // Re-split after the Dock vacancy is created. An overflow-pinned entry may be promoted
        // into the freed Dock slot; that promotion must happen BEFORE the Grid insertion index
        // is chosen or the moved app shifts one column to the left after the rebind.
        presentationSnapshot = null;
        DockLayoutModel.Snapshot<Entry> updatedSnapshot = dockSnapshot();
        List<Entry> finalGrid = new ArrayList<>(updatedSnapshot.gridItems);
        for (int i = finalGrid.size() - 1; i >= 0; i--) {
            if (key.equals(finalGrid.get(i).component.flattenToShortString())) {
                finalGrid.remove(i);
                break;
            }
        }
        List<Entry> finalDock = new ArrayList<>(updatedSnapshot.dockItems);
        List<Entry> positionedGrid = DockLayoutModel.insertAtPosition(finalGrid, selected, gridPosition);
        replacePresentationOrder(finalDock, positionedGrid);
        return true;
    }

    private void replacePresentationOrder(List<Entry> dock, List<Entry> grid) {
        List<Entry> ordered = new ArrayList<>(dock.size() + grid.size());
        ordered.addAll(dock);
        ordered.addAll(grid);
        items.replace(ordered);
        presentationSnapshot = null;
        arranged.clear();
        for (Entry x : ordered) if (x.kind != Entry.HOME_SETTINGS) {
            arranged.add(classicVerticalMode ? x.pkg : x.component.flattenToShortString());
        }
        if (!moveTransactionActive) {
            ctx.getSharedPreferences(ORDER_PREFS, Context.MODE_PRIVATE).edit()
                    .putString(ORDER_KEY, android.text.TextUtils.join("\n", arranged)).apply();
        }
        notifyDataSetChanged();
    }

    /** Immutable presentation partition shared by the independent Dock and App Grid adapters. */
    DockLayoutModel.Snapshot<Entry> gridSnapshot() {
        return dockSnapshot();
    }

    DockLayoutModel.Snapshot<Entry> dockSnapshot() {
        if (presentationSnapshot == null) {
            int capacity = classicVerticalMode ? gridColumns : DockPreferences.load(ctx).capacity;
            List<String> pinnedComponentKeys = currentPinnedKeys();
            presentationSnapshot = DockLayoutModel.split(items, pinnedComponentKeys, capacity,
                    entry -> entry.component.flattenToShortString());
        }
        return presentationSnapshot;
    }

    void invalidateDockSnapshot() {
        presentationSnapshot = null;
    }

    /** Complete ordered app inventory used by the single-row horizontal carousel. */
    List<Entry> allEntriesSnapshot() {
        List<Entry> result = new ArrayList<>();
        for (Entry entry : items) result.add(entry);
        return Collections.unmodifiableList(result);
    }

    /** Reorders the full carousel inventory while keeping the non-movable Home Settings tile last. */
    boolean moveAllEntries(int from, int to) {
        List<Entry> ordered = new ArrayList<>();
        for (Entry entry : items) ordered.add(entry);
        int last = ordered.size() - 1;
        while (last >= 0 && ordered.get(last).kind == Entry.HOME_SETTINGS) last--;
        if (from < 0 || from > last || to < 0 || to > last || from == to) return false;
        List<String> keys = new ArrayList<>(ordered.size());
        Map<String, Entry> byKey = new HashMap<>();
        for (Entry entry : ordered) {
            String key = entry.component.flattenToShortString();
            keys.add(key);
            byKey.put(key, entry);
        }
        String movedKey = keys.get(from);
        ClassicAppOrder.move(keys, movedKey, to);
        List<Entry> reordered = new ArrayList<>(ordered.size());
        for (String key : keys) {
            Entry entry = byKey.get(key);
            if (entry != null) reordered.add(entry);
        }
        replacePresentationOrder(Collections.emptyList(), reordered);
        return true;
    }

    private List<String> currentPinnedKeys() {
        if (classicVerticalMode) {
            List<String> classicDock = new ArrayList<>();
            for (Entry entry : items) {
                if (entry.kind == Entry.HOME_SETTINGS) continue;
                if (classicDock.size() >= gridColumns) break;
                classicDock.add(entry.component.flattenToShortString());
            }
            return classicDock;
        }
        List<String> explicit = moveTransactionActive
                ? new ArrayList<>(movePreviewPinnedKeys) : DockPreferences.loadPinnedKeys(ctx);
        List<String> defaults = DockLayoutModel.orderedPinnedKeys(items, favourites, arranged,
                entry -> entry.component.flattenToShortString(),
                entry -> entry.kind == Entry.HOME_SETTINGS ? null : entry.pkg);
        List<String> merged = DockLayoutModel.mergePinnedKeys(
                explicit, defaults, currentUnpinnedKeys());
        List<String> resolved = DockLayoutModel.orderedExplicitPinnedKeys(items, merged,
                entry -> entry.component.flattenToShortString());
        List<String> inventoryKeys = new ArrayList<>();
        for (Entry entry : items) inventoryKeys.add(entry.component.flattenToShortString());
        // Patch 4.1 contract: configured favourites lead, then fill the remaining Dock slots
        // from the same ordered inventory. A short/empty favourites list must not erase the Dock.
        return new ArrayList<>(DockLayoutModel.fillDockSlots(
                resolved, inventoryKeys, currentUnpinnedKeys(),
                DockPreferences.load(ctx).capacity));
    }

    @Override
    public int getItemCount() {
        return gridSnapshot().gridItems.size();
    }

    @Override
    public long getItemId(int position) {
        return gridSnapshot().gridItems.get(position).component.flattenToShortString().hashCode();
    }

    void setIconAnimationsEnabled(boolean enabled, RecyclerView recyclerView) {
        iconAnimationsEnabled = enabled;
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            if (holder instanceof Holder) ((Holder) holder).setIconAnimationsEnabled(enabled);
        }
    }

    /** Stable key for the app at a position (its component), or null. */
    String keyAt(int position) {
        List<Entry> grid = gridSnapshot().gridItems;
        return position >= 0 && position < grid.size() ? grid.get(position).component.flattenToShortString() : null;
    }

    /** Position of the app with this key in the App Grid, or -1 when it is not there. */
    int positionOf(String key) {
        if (key == null) return -1;
        List<Entry> grid = gridSnapshot().gridItems;
        for (int i = 0; i < grid.size(); i++) {
            if (grid.get(i).component.flattenToShortString().equals(key)) return i;
        }
        return -1;
    }

    String labelAt(int position) {
        List<Entry> grid = gridSnapshot().gridItems;
        return position >= 0 && position < grid.size() ? grid.get(position).label : "";
    }

    /** A vivid colour sampled from the app's banner (or icon), cached per entry; a card's accent when it set one. */
    int ambientColorAt(int position) {
        List<Entry> grid = gridSnapshot().gridItems;
        if (position < 0 || position >= grid.size()) return 0xFF444444;
        Entry e = grid.get(position);
        if (e.ambient == 0) e.ambient = vividColor(e.banner != null ? e.banner : e.icon);
        return e.ambient;
    }

    int ambientColorFor(Entry entry) {
        if (entry == null) return 0xFF444444;
        if (entry.ambient == 0) entry.ambient = vividColor(entry.banner != null ? entry.banner : entry.icon);
        return entry.ambient;
    }

    private static int vividColor(Drawable live) {
        if (live == null) return 0xFF444444;
        // Draw a private copy: changing the bounds of the drawable an ImageView shows would blank the tile.
        Drawable d = live.getConstantState() != null ? live.getConstantState().newDrawable().mutate() : live;
        Rect saved = new Rect(d.getBounds());
        int n = 12;
        Bitmap bmp = Bitmap.createBitmap(n, n, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        d.setBounds(0, 0, n, n);
        d.draw(c);
        d.setBounds(saved);
        float[] hsv = new float[3];
        int best = 0xFF444444;
        float bestScore = -1f;
        long r = 0, g = 0, b = 0;
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                int p = bmp.getPixel(x, y);
                r += Color.red(p);
                g += Color.green(p);
                b += Color.blue(p);
                Color.colorToHSV(p, hsv);
                float score = hsv[1] * Math.min(1f, hsv[2] * 1.4f);   // saturated and not too dark
                if (score > bestScore) {
                    bestScore = score;
                    best = p;
                }
            }
        }
        bmp.recycle();
        if (bestScore < 0.25f) {   // nearly monochrome banner (e.g. white): use its average, lifted
            int avg = Color.rgb((int) (r / (n * n)), (int) (g / (n * n)), (int) (b / (n * n)));
            Color.colorToHSV(avg, hsv);
            hsv[2] = Math.max(hsv[2], 0.55f);
            return Color.HSVToColor(hsv);
        }
        Color.colorToHSV(best, hsv);
        hsv[1] = Math.min(1f, hsv[1] * 1.1f);
        hsv[2] = Math.max(0.5f, hsv[2]);
        return Color.HSVToColor(hsv);
    }

    /** Corner radius for a tile of this width: 16dp at the design width, proportionally less when smaller. */
    static float cornerRadius(int tileWidthPx, float density) {
        return 16f * density * Math.min(1f, tileWidthPx / (DESIGN_W_DP * density));
    }

    static final class Holder extends RecyclerView.ViewHolder {
        final View card;
        final ImageView banner;
        final ImageView icon;
        final TextView label;
        final TextView name;
        final boolean reducedMotion;
        private final boolean dockTile;
        private boolean iconAnimationsEnabled;

        Holder(View v, TileListener listener, boolean reducedMotion,
               boolean iconAnimationsEnabled, boolean dockTile) {
            super(v);
            card = v.findViewById(R.id.card);
            banner = v.findViewById(R.id.banner);
            icon = v.findViewById(R.id.icon);
            label = v.findViewById(R.id.label);
            name = v.findViewById(R.id.name);
            this.reducedMotion = reducedMotion;
            this.iconAnimationsEnabled = iconAnimationsEnabled;
            this.dockTile = dockTile;

            final float density = v.getResources().getDisplayMetrics().density;
            card.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    // 16dp at the design width, scaled down with the tile so small tiles keep the same shape.
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius(view.getWidth(), density));
                }
            });
            card.setClipToOutline(true);

            card.setOnKeyListener((view, keyCode, event) -> {
                if (event.getAction() != KeyEvent.ACTION_DOWN) return false;
                if (keyCode != KeyEvent.KEYCODE_DPAD_UP && keyCode != KeyEvent.KEYCODE_DPAD_DOWN
                        && keyCode != KeyEvent.KEYCODE_DPAD_LEFT && keyCode != KeyEvent.KEYCODE_DPAD_RIGHT) return false;
                int pos = getBindingAdapterPosition();
                return listener != null && pos != RecyclerView.NO_POSITION && listener.onTileVerticalKey(pos, keyCode);
            });

            card.setOnFocusChangeListener((view, focused) -> {
                float s = focused && iconAnimationsEnabled ? focusScale() : 1.0f;
                view.animate().cancel();
                name.animate().cancel();
                if (!iconAnimationsEnabled) {
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                    name.setAlpha(focused ? 1f : 0f);
                    name.setTranslationY(0f);
                } else if (reducedMotion) {
                    // No overshoot: the extra frames redraw the banner and the dock band on a weak GPU.
                    view.animate().scaleX(s).scaleY(s)
                            .setDuration(focused ? 120 : 80)
                            .setInterpolator(ZOOM_REDUCED)
                            .withLayer()
                            .start();
                    name.animate().alpha(focused ? 1f : 0f).setDuration(100).start();
                } else {
                    view.animate().scaleX(s).scaleY(s)
                            .setDuration(focused ? 320 : 200)
                            .setInterpolator(focused ? ZOOM_IN : ZOOM_OUT)
                            .withLayer()
                            .start();
                    name.animate().alpha(focused ? 1f : 0f)
                            .translationY(0f)
                            .setDuration(220).start();
                }
                int pos = getBindingAdapterPosition();
                if (focused && listener != null && pos != RecyclerView.NO_POSITION) {
                    listener.onTileFocused(pos, view);
                }
            });
        }

        float focusScale() {
            return AppAdapter.focusScale(dockTile);
        }

        void setIconAnimationsEnabled(boolean enabled) {
            iconAnimationsEnabled = enabled;
            card.animate().cancel();
            name.animate().cancel();
            boolean focused = card.isFocused();
            if (!enabled) {
                card.setScaleX(1f);
                card.setScaleY(1f);
                name.setAlpha(focused ? 1f : 0f);
                name.setTranslationY(0f);
            } else if (focused) {
                card.setScaleX(focusScale());
                card.setScaleY(focusScale());
                name.setAlpha(1f);
            }
        }
    }
}
