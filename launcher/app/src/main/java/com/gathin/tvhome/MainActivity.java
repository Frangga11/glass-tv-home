package com.gathin.tvhome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.tv.TvContract;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gathin.tvhome.qrcode.QrBitmapUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * tvOS style home: black, a pinned dock row, an app grid, and one glass highlight that springs between tiles.
 */
public class MainActivity extends Activity implements AppAdapter.TileListener, DockAdapter.Listener {

    private static java.lang.ref.WeakReference<MainActivity> sInstance;

    public static MainActivity getInstance() {
        return sInstance != null ? sInstance.get() : null;
    }

    public static boolean isForeground() {
        MainActivity inst = getInstance();
        return inst != null && !inst.isFinishing() && !inst.isDestroyed() && !inst.hiddenSinceResume;
    }

    public static void openSettingsFromGlobal() {
        MainActivity inst = getInstance();
        if (inst != null) {
            inst.runOnUiThread(() -> {
                if (inst.screensaverActive) inst.dismissScreensaver();
                inst.showSettingsPopup();
            });
        }
    }

    private boolean isHomeLongPressed = false;
    private final Runnable longPressHomeRunnable = new Runnable() {
        @Override
        public void run() {
            isHomeLongPressed = true;
            SoundEffects.playSelect();
            if (screensaverActive) dismissScreensaver();
            showSettingsPopup();
        }
    };

    /** Tiles per row and dock slots (res/values/config.xml, columns). Set in onCreate. */
    static int COLUMNS = 4;
    private static final long MINUTE_MS = 60_000L;
    private String liveTvPkg;
    private String liveTvAction;
    private String inputAction;
    private String sourcePanelService;
    private String sourcePanelAction;

    private AppAdapter adapter;
    private RecyclerView grid;
    private RecyclerView dockRecycler;
    private DockAdapter dockAdapter;
    private DockDecoration tray;
    private boolean dockVertical;
    /** Horizontal Grid stays parked out of sight until the user presses Down from the Dock. */
    private boolean horizontalGridExpanded;
    /** Serializes the first Dock-to-overflow reveal against repeated vertical DPAD input. */
    private boolean horizontalDockExpansionInProgress;
    /** Pending target for serialized horizontal Grid row transitions (supports rapid DPAD repeats). */
    private int pendingHorizontalGridPosition = RecyclerView.NO_POSITION;
    /** Stable component identity for a pending row target while the app inventory changes. */
    private String pendingHorizontalGridKey;
    private int horizontalGridTransitionGeneration;
    private boolean horizontalGridTransitionRunning;
    private int horizontalGridTransitionRetries;
    private android.animation.ValueAnimator horizontalGridScrollAnimator;
    /** Coordinated Dock/overflow-row slide; focus is committed only after the geometry settles. */
    private android.animation.ValueAnimator horizontalDockTransitionAnimator;
    private int horizontalDockTransitionGeneration;
    private boolean horizontalDockCollapseInProgress;
    /** One semantic DPAD intent captured during collapse; never replay synthetic Android key events. */
    private int pendingDockCollapseDirection = -1;
    /** Monotonic token prevents delayed layout callbacks from stealing focus from a newer DPAD target. */
    private int gridFocusRequestGeneration;
    private boolean lastFocusedDock;
    private int lastDockDecorationCount = -1;
    private boolean lastDockDecorationVertical;
    private static final String HOME_FOCUS_REGION = "home_focus_region";
    private static final String HOME_DOCK_FOCUS_KEY = "home_dock_focus_key";
    private static String lastDockFocusedKey;
    private FrameLayout root;
    private GlassView glass;
    private View ambient;
    /** The focus: one dark shape gliding between tiles and pills (see Glider). */
    private Glider glider;
    /** Whether the glider is on the top menu, so a move down travels dark. */
    private boolean gliderOnPill;
    private final int[] pillAt = new int[2], rootAt = new int[2];
    /** Reused for positioning the Home information shelf relative to the Dock. */
    private final Rect shelfAnchorRect = new Rect();
    /** How much of the space between the side paddings the top shelf may use. */
    private static final float HERO_WIDTH_SHARE = 0.40f;
    /** The name is one line, and may run wider than the description, up to this share (capped to avoid overlapping Islamic shelf). */
    private static final float HERO_NAME_WIDTH_SHARE = 0.45f;
    private static final float HERO_TITLE_MAX_SP = 40f;
    /** The breathing room between the shelf's last line and the top of the app container (50dp gives airy, balanced spacing). */
    private static final float HERO_TITLE_MIN_SP = 26f;
    /** Small measured offset keeps the wallpaper shelf 32dp clear of the Dock tray. */
    private static final float HOME_SHELF_BASELINE_OFFSET_DP = 3.5f;
    /** Minimum visual clearance between the information shelf and Dock tray. */
    private static final float MIN_HOME_SHELF_TRAY_GAP_DP = 32f;
    /** Vertical Dock fallback retains its established inter-shelf spacing. */
    private static final float ISLAMIC_SHELF_BOTTOM_OFFSET_DP = 10f;
    /** The shelf's width in pixels, worked out once from the screen. */
    private int heroMaxWidth, heroNameMaxWidth;
    private View heroShelf;
    private TextView heroName;
    private TextView heroDesc;
    private TextView heroWhere;
    /** The place on screen and a few lines about it; null for the user's own wallpapers. */
    private String placeName;
    private String placeText = "", placeCountry = "";
    private int ambientColor = -1;
    private boolean heroShown;
    /** The glow behind the tiles. It follows every app, while the shelf only names dock apps. */
    private boolean ambientShown;

    /** The colour the glass is blending from, how far along it is, and the blend itself. */
    private int glowFrom;
    private float glowMix = 1f;
    private android.animation.ValueAnimator glowBlend;
    private Bitmap ambientShape;
    /** Where the shelf's bottom edge belongs; its top follows once the lines have been measured. */
    private float heroBottom;
    /** Stable bottom edge for the Islamic shelf; focus changes only animate visibility, never position. */
    private float islamicShelfBottomAnchor = Float.NaN;
    private boolean homeShelfAnchorReady;
    private View btnInputs;
    private TextView btnWifi;
    private View btnAssistant;
    private View btnCandyBox;
    private View btnHome;
    private boolean activityResumed;
    private TextView ampmView, dateView;
    private TextView shamsiDate;
    private TextView prayerTimesView;
    private TextView weatherAirQualityView;
    private View nowPlayingCard;
    private ImageView cardAlbumArt;
    private View cardTrackInfo;
    private TextView cardSubtitle;
    private TextView cardTitle;
    private ImageView btnInlinePrev;
    private ImageView btnInlinePlayPause;
    private ImageView btnInlineNext;
    private ImageView btnInlineClose;
    private boolean nowPlayingDisabledByUser = false;
    private String lastPlayedTrackKey = "";
    private NowPlayingManager.Listener nowPlayingListener;
    private View islamicShelf;
    private View ayahBlock;
    private TextView ayahLabel, ayahArabic, ayahText, prayerHeader;
    private TextView ayahOpenHint;
    private boolean ayahRunning;
    private boolean homeWeatherRunning;
    private TextView lblSubuh, lblSyuruk, lblZohor, lblAsar, lblMaghrib, lblIsyak;
    private TextView timeSubuh, timeSyuruk, timeZohor, timeAsar, timeMaghrib, timeIsyak;
    private boolean shamsi;
    private String vpnApp;
    private SpringAnimation springX, springY;
    private float glassTargetX, glassTargetY;
    // How far the grid has scrolled the glass. springY works without it, in grid content space, so a
    // scroll moves the glass in lockstep with its tile instead of becoming a moving spring target.
    private float glassScrollShift;
    private boolean glassShown;
    // Static so they survive the activity being destroyed while another app is open (low-memory TVs,
    // or Developer options → "Don't keep activities"); they only reset when the process dies.
    private static int lastFocused = 0;
    /** App of the last focused tile, so focus follows it when the app list changes. */
    private static String lastFocusedKey;
    /** Last focused top-bar pill, restored when returning from an external Android screen. */
    private static boolean lastFocusWasTopPill;
    private static String lastFocusedTopPillKey;
    private float density;
    /** Module cards (res/values/config.xml, module_cards): discovered on resume, shown only after the owner's Show. */
    private boolean moduleCards;
    /** Calmer motion for weak GPUs (config.xml, reduced_motion): stiff spring, short fades, no replay. */
    private boolean reducedMotion;
    /** hidden_apps as the adapter sees them (config.xml or a theme's config.json): a hidden app gets no card either. */
    private Set<String> hiddenApps = Collections.emptySet();
    private List<Cards.Card> cards = Collections.emptyList();
    /** The grid has been laid out and focused since the last resume; the card prompt waits for this. */
    private boolean tilesReady;
    private boolean tilesAnimatedOnce;
    /** Original XML top inset; Dock placement must not push the App Grid below it. */
    private int gridBasePaddingTop;
    private int gridBasePaddingBottom;
    /** The longest Home holds its first frame while it puts itself together (see holdFirstFrame). */
    private static final long FIRST_FRAME_WAIT_MS = 1200;
    private static final long RETURN_SETTLE_MS = 6000;
    /** True while Home rebuilds on return; focus passes through the pills then and must not light them. */
    private boolean resuming;
    /** True while Home's first frame is held back (see holdFirstFrame). */
    private boolean firstFrameHeld;
    private Dialog cardPrompt;
    /** Set when a decision could not be saved: no more prompts until the next resume, instead of asking in a loop. */
    private boolean promptsPaused;
    /** The Inputs or Module cards dropdown while one is open, so onPause can close it. */
    private PopupWindow openPopup;

    private final Handler handler = new Handler(Looper.getMainLooper());
    /** Rotating wallpaper (Config.wallpapers), the view that shows it and the bitmap on screen. */
    private List<File> wallpapers = Collections.emptyList();
    private long wallpaperIntervalMs;
    /** Decodes the next picture off the main thread, so a switch never stutters the D-pad animations. */
    private final java.util.concurrent.ExecutorService wallDecoder = java.util.concurrent.Executors.newSingleThreadExecutor();
    /** Latest-request token prevents stale wallpaper decodes from replacing newer selections. */
    private final java.util.concurrent.atomic.AtomicLong wallpaperRequestGeneration = new java.util.concurrent.atomic.AtomicLong();
    private final java.util.concurrent.atomic.AtomicLong wallpaperThumbnailGeneration = new java.util.concurrent.atomic.AtomicLong();
    private long wallShownAt;
    private android.widget.ImageView wallView;
    private android.graphics.Bitmap wallBitmap;
    /** Dynamic atmospheric weather canvas particle overlay over the wallpaper */
    private WeatherBackgroundView weatherOverlayView;
    String testWeatherOverride = null;
    /** Settings kept for the places refresh, which runs after onCreate. */
    private Config config;
    /** Pictures of places are on (see PlaceWallpapers); they are dimmed behind the tiles. */
    private boolean placesMode;
    private volatile boolean placesRefreshRunning;
    private boolean screensaverActive = false;
    private View screensaverView;
    private String standbyPhotoUriShown = "";
    private final Runnable standbyPhotoTick = new Runnable() {
        @Override public void run() {
            if (screensaverActive) {
                updateNightstandPhoto();
                handler.postDelayed(this, StandbyPhotoStore.ROTATION_INTERVAL_MS);
            }
        }
    };
    private boolean wallpaperFullscreen = false;
    private View mainContent;
    private static final int REQUEST_STANDBY_PHOTOS = 6041;
    private static final long DOUBLE_BACK_DELAY_MS = 350L;
    private Runnable singleBackRunnable = null;
    private boolean doubleBackPending = false;
    /** Refreshes the network pill when the connection changes: at boot Wi-Fi often connects after Home is shown. */
    private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            handler.post(MainActivity.this::updateWifiLabel);
        }

        @Override
        public void onLost(@NonNull Network network) {
            handler.post(MainActivity.this::updateWifiLabel);
        }
    };
    /** Shows the Inputs pill once the TV registers an input: at boot HDMI inputs can appear after Home is shown. */
    private final TvInputManager.TvInputCallback inputCallback = new TvInputManager.TvInputCallback() {
        @Override
        public void onInputAdded(String inputId) {
            updateInputsButton();
        }

        @Override
        public void onInputRemoved(String inputId) {
            updateInputsButton();
        }
    };
    /** Listens for any VPN coming up or going down, even one this app's traffic is excluded from. */
    private final ConnectivityManager.NetworkCallback vpnCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            handler.post(MainActivity.this::updateWifiLabel);
        }

        @Override
        public void onLost(@NonNull Network network) {
            handler.post(MainActivity.this::updateWifiLabel);
        }
    };
    private final IntentFilter packageFilter = new IntentFilter();
    private final BroadcastReceiver packageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Updates send REMOVED followed by ADDED; keep the tile until the new APK is ready.
            if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())
                    && intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) return;
            reloadApps(false);
        }
    };
    private final Runnable minuteTick = new Runnable() {
        @Override
        public void run() {
            updateShamsiDate();
            updatePrayerTimes();
            PrayerTimes.syncIfDue(MainActivity.this, MainActivity.this::updatePrayerTimes);
            updateDateAndPeriod();
            updateWifiLabel();
            updateWeatherAqi();
            WeatherAirQuality.syncIfDue(MainActivity.this, MainActivity.this::updateWeatherAqi);
            // HomeWeather has a separate 15-minute cadence for the atmospheric wallpaper overlay.
            // Polling from the active Home minute tick keeps it fresh without starting duplicate fetches.
            updateHomeWeather();
            updateIslamicShelf();
            refreshPlacesIfDue();
            refreshOnlineWallpapersIfDue();
            refreshManualNightstand();
            handler.postDelayed(this, MINUTE_MS);
        }
    };

    private final Runnable nowPlayingPoll = new Runnable() {
        @Override
        public void run() {
            if (!isFinishing() && !isDestroyed()) {
                NowPlayingManager.updateCurrentMedia(MainActivity.this);
                handler.postDelayed(this, 3000);
            }
        }
    };

    public boolean isWeatherWidgetEnabled() {
        return getSharedPreferences("theme_prefs", MODE_PRIVATE).getBoolean("weather_widget_enabled", true);
    }

    boolean isIconAnimationsEnabled() {
        return getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getBoolean("icon_animations_enabled", true);
    }

    void setIconAnimationsEnabled(boolean enabled) {
        getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).edit().putBoolean("icon_animations_enabled", enabled).apply();
        if (adapter != null && grid != null) {
            adapter.setIconAnimationsEnabled(enabled, grid);
            if (dockAdapter != null && dockRecycler != null) dockAdapter.setIconAnimationsEnabled(enabled, dockRecycler);
            if (dockRecycler != null && dockRecycler.hasFocus()) {
                View dockItem = dockRecycler.getFocusedChild();
                int dockPosition = dockItem != null ? dockRecycler.getChildAdapterPosition(dockItem) : RecyclerView.NO_POSITION;
                View dockCard = dockItem != null ? dockItem.findViewById(R.id.card) : null;
                if (dockCard != null && dockPosition != RecyclerView.NO_POSITION) onDockTileFocused(dockAdapter.keyAt(dockPosition), dockCard);
                return;
            }
            View focusedItem = grid.getFocusedChild();
            View focusedCard = focusedItem != null ? focusedItem.findViewById(R.id.card) : null;
            int position = focusedItem != null ? grid.getChildAdapterPosition(focusedItem) : RecyclerView.NO_POSITION;
            if (focusedCard != null && position != RecyclerView.NO_POSITION) {
                onTileFocused(position, focusedCard);
            }
        }
    }

    boolean isGlassEffectsEnabled() {
        return getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getBoolean("glass_effects_enabled", true);
    }

    void setGlassEffectsEnabled(boolean enabled) {
        getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).edit().putBoolean("glass_effects_enabled", enabled).apply();
        if (glider != null && ((grid != null && grid.hasFocus()) || (dockRecycler != null && dockRecycler.hasFocus()))) {
            glider.setStyle(enabled ? Glider.GLASS : Glider.DARK);
        }
        if (grid != null) grid.invalidateItemDecorations();
        if (dockRecycler != null) dockRecycler.invalidateItemDecorations();
        if (enabled && wallBitmap != null) blurBackdrop(wallBitmap);
        if (root != null) root.invalidate();
        if (openPopup != null && openPopup.getContentView() != null) {
            openPopup.getContentView().invalidate();
        }
        if (openMenu != null) openMenu.invalidate();
    }

    public void setWeatherWidgetEnabled(boolean enabled) {
        getSharedPreferences("theme_prefs", MODE_PRIVATE).edit().putBoolean("weather_widget_enabled", enabled).apply();
        if (weatherAirQualityView != null) {
            weatherAirQualityView.setVisibility(enabled ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    protected void attachBaseContext(android.content.Context newBase) {
        super.attachBaseContext(LocaleContext.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StandbyPhotoStore.cleanupOrphanedUploads(this);
        WallpaperUploadServer.cleanupOrphanedCustomWallpaperUploads(this);
        ProfileManager.cleanupOrphanedAvatarUploads(this);
        sInstance = new java.lang.ref.WeakReference<>(this);
        SoundEffects.init(this);
        setContentView(R.layout.activity_main);
        density = getResources().getDisplayMetrics().density;
        // config.xml, overlaid by config.json from a pushed theme (see Config and tools/theme.sh).
        Config cfg = Config.load(this);
        COLUMNS = cfg.columns;
        android.content.SharedPreferences navigationPrefs =
                getSharedPreferences(NavigationState.PREFS, MODE_PRIVATE);
        if (lastFocusedKey == null) {
            lastFocusedKey = NavigationState.normalizeFocusKey(
                    navigationPrefs.getString(NavigationState.HOME_FOCUS_KEY, null));
            lastFocused = Math.max(0, navigationPrefs.getInt(NavigationState.HOME_FOCUS_POSITION, 0));
        }
        lastFocusedDock = "dock".equals(navigationPrefs.getString(HOME_FOCUS_REGION, "grid"));
        lastDockFocusedKey = NavigationState.normalizeFocusKey(navigationPrefs.getString(HOME_DOCK_FOCUS_KEY, null));
        lastFocusWasTopPill = navigationPrefs.getBoolean(NavigationState.HOME_FOCUS_TOP_PILL, false);
        lastFocusedTopPillKey = NavigationState.normalizeTopPillKey(
                navigationPrefs.getString(NavigationState.HOME_FOCUS_TOP_PILL_KEY, null));
        reducedMotion = cfg.reducedMotion;
        liveTvPkg = cfg.liveTvPackage;
        liveTvAction = cfg.liveTvAction;
        inputAction = cfg.inputAction;
        sourcePanelService = cfg.sourcePanelService;
        sourcePanelAction = cfg.sourcePanelAction;

        root = findViewById(R.id.root);
        // The tiles' entrance plays once per TV boot. Android reclaims the launcher during long stays
        // in big apps; starting again then is a return to Home, not a first launch.
        long bootAt = System.currentTimeMillis() - android.os.SystemClock.elapsedRealtime();
        android.content.SharedPreferences launch = getSharedPreferences("launch", MODE_PRIVATE);
        if (Math.abs(launch.getLong("boot_at", 0) - bootAt) < 60_000) tilesAnimatedOnce = true;
        else launch.edit().putLong("boot_at", bootAt).apply();
        root.setBackgroundColor(cfg.backgroundColor);
        config = cfg;
        wallpapers = cfg.wallpapers;
        wallpaperIntervalMs = getWallpaperIntervalMs();
        placesMode = cfg.places;
        mainContent = findViewById(R.id.mainContent);
        glass = findViewById(R.id.glass);
        ambient = findViewById(R.id.ambient);
        Backdrop.setWorker(wallDecoder, handler);
        heroShelf = findViewById(R.id.heroShelf);
        if (heroShelf != null) {
            heroShelf.addOnLayoutChangeListener((v, left, top, right, bottom,
                                                  oldLeft, oldTop, oldRight, oldBottom) -> {
                if (homeShelfAnchorReady && heroShown) v.setY(heroBottom - v.getHeight());
            });
        }
        heroName = findViewById(R.id.heroName);
        heroDesc = findViewById(R.id.heroDesc);
        heroWhere = findViewById(R.id.heroWhere);
        int usable = getResources().getDisplayMetrics().widthPixels - Math.round(68 * density);
        heroMaxWidth = Math.round(usable * HERO_WIDTH_SHARE);
        heroNameMaxWidth = Math.round(usable * HERO_NAME_WIDTH_SHARE);
        if (heroName != null) heroName.setMaxWidth(heroNameMaxWidth);
        if (heroDesc != null) heroDesc.setMaxWidth(heroMaxWidth);

        String activeCat = reconcileActiveWallpaperCategoryForStartup();
        if ("Default".equalsIgnoreCase(activeCat)) {
            placesMode = false;
            File defaultWallpaper = getBundledDefaultWallpaper();
            wallpapers = defaultWallpaper != null
                    ? Collections.singletonList(defaultWallpaper) : Collections.emptyList();
        } else if ("Custom".equalsIgnoreCase(activeCat)) {
            placesMode = false;
            File customDir = new File(getFilesDir(), "custom_wallpapers");
            File[] files = customDir.listFiles((dir, name) -> UploadFilePolicy.isSafeStoredImageName(name));
            boolean singleMode = getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getBoolean("custom_single_mode", false);
            String singleFile = getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getString("custom_single_file", null);
            if (singleMode && singleFile != null) {
                File sf = new File(customDir, singleFile);
                if (sf.exists() && sf.length() > 0) {
                    wallpapers = Collections.singletonList(sf);
                } else if (files != null && files.length > 0) {
                    Arrays.sort(files, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
                    wallpapers = new ArrayList<>(Arrays.asList(files));
                }
            } else if (files != null && files.length > 0) {
                Arrays.sort(files, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
                wallpapers = new ArrayList<>(Arrays.asList(files));
            } else if (cfg.wallpaperOnline && wallpapers.isEmpty()) {
                wallpapers = OnlineWallpapers.list(this);
            }
        } else {
            placesMode = false;
            List<File> catFiles = OnlineWallpapers.listByCategory(this, activeCat);
            if (!catFiles.isEmpty()) {
                wallpapers = catFiles;
            } else if ("Minimalist".equalsIgnoreCase(activeCat)) {
                // Do not silently show a non-OLED wallpaper while the selected OLED collection syncs.
                wallpapers = Collections.emptyList();
            } else if (cfg.wallpaperOnline && wallpapers.isEmpty()) {
                wallpapers = OnlineWallpapers.list(this);
            }
            if (wallpapers.isEmpty()) {
                new Thread(() -> {
                    OnlineWallpapers.syncCategory(getApplicationContext(), activeCat);
                    handler.post(() -> {
                        if (isDestroyed()) return;
                        List<File> fresh = OnlineWallpapers.listByCategory(MainActivity.this, activeCat);
                        if (!fresh.isEmpty()) {
                            wallpapers = fresh;
                            showNextWallpaper(false);
                        } else if ("Minimalist".equalsIgnoreCase(activeCat)) {
                            // A failed network fetch leaves a clean black OLED-safe background.
                            root.setBackgroundColor(android.graphics.Color.BLACK);
                            if (wallView != null) {
                                wallView.setImageDrawable(null);
                                wallView.setBackgroundColor(android.graphics.Color.BLACK);
                            }
                        }
                    });
                }, "wall-default-sync").start();
            }
        }
        if (!wallpapers.isEmpty()) showNextWallpaper(false);
        else if (cfg.wallpaper != null && !placesMode && !"Minimalist".equalsIgnoreCase(activeCat) && !"Default".equalsIgnoreCase(activeCat)) {
            queueWallpaperDecode(cfg.wallpaper, false, false, false);
        }
        else if ("Minimalist".equalsIgnoreCase(activeCat) || "Default".equalsIgnoreCase(activeCat)) {
            // Never fall back to a theme/legacy wallpaper when Default's bundled asset is unavailable.
            root.setBackgroundColor(android.graphics.Color.BLACK);
            if (wallView != null) {
                wallView.setImageDrawable(null);
                wallView.setBackgroundColor(android.graphics.Color.BLACK);
            }
            if ("Default".equalsIgnoreCase(activeCat)) {
                android.util.Log.e("TvHome", "Default wallpaper asset unavailable; showing safe black background");
            }
        }
        if (!isWallpaperDescEnabled() && heroShelf != null) {
            heroShelf.setVisibility(View.GONE);
            heroShelf.setAlpha(0f);
        }
        // The shelf grows and shrinks with the name and the description, so it hangs from its
        // bottom edge: every time it is measured, its top is worked out again. Positioning it by
        // the height it happened to have beforehand is what let a tall block run into the dock.
        heroShelf.addOnLayoutChangeListener((v, l, t, r, b, ol, ot, orr, ob) -> {
            if (heroBottom > 0) v.setY(heroBottom - v.getHeight());
        });
        ampmView = findViewById(R.id.ampm);
        dateView = findViewById(R.id.date);
        updateDateAndPeriod();
        weatherAirQualityView = findViewById(R.id.weatherAirQuality);
        if (weatherAirQualityView != null) {
            weatherAirQualityView.setVisibility(isWeatherWidgetEnabled() ? View.VISIBLE : View.GONE);
            weatherAirQualityView.setOnClickListener(v -> {
                startActivity(new Intent(this, WeatherActivity.class));
            });
            // Single authoritative Weather/AQI DPAD listener. Keep its vertical destination
            // aligned with the Home graph: Weather/AQI DOWN enters the primary Dock row first.
            weatherAirQualityView.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() != KeyEvent.ACTION_DOWN) return false;
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    View locationPill = findViewById(R.id.locationPill);
                    if (locationPill != null && locationPill.isShown() && locationPill.requestFocus()) return true;
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (btnHome != null && btnHome.isShown() && btnHome.requestFocus()) return true;
                    return false;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if (nowPlayingCard != null && nowPlayingCard.getVisibility() == View.VISIBLE) {
                        if (cardTrackInfo != null && cardTrackInfo.isShown() && cardTrackInfo.requestFocus()) return true;
                        if (nowPlayingCard.isShown() && nowPlayingCard.requestFocus()) return true;
                    }
                    if (dockAdapter != null && dockAdapter.getItemCount() > 0) focusDockTile(0);
                    else focusTile(0);
                    return true;
                }
                return false;
            });
        }
        updateWeatherAqi();
        WeatherAirQuality.syncIfDue(this, this::updateWeatherAqi);

        // Now Playing Widget Card (matching bravia-glass-tv UI)
        nowPlayingCard = findViewById(R.id.nowPlayingCard);
        if (nowPlayingCard != null) {
            cardAlbumArt = findViewById(R.id.cardAlbumArt);
            cardTrackInfo = findViewById(R.id.cardTrackInfo);
            cardSubtitle = findViewById(R.id.cardSubtitle);
            cardTitle = findViewById(R.id.cardTitle);
            btnInlinePrev = findViewById(R.id.btnInlinePrev);
            btnInlinePlayPause = findViewById(R.id.btnInlinePlayPause);
            btnInlineNext = findViewById(R.id.btnInlineNext);
            btnInlineClose = findViewById(R.id.btnInlineClose);

            View.OnClickListener openDialogClick = v -> {
                SoundEffects.playSelect();
                showNowPlayingDialog();
            };
            if (cardAlbumArt != null) cardAlbumArt.setOnClickListener(openDialogClick);
            if (cardTrackInfo != null) cardTrackInfo.setOnClickListener(openDialogClick);

            if (btnInlinePlayPause != null) {
                btnInlinePlayPause.setOnClickListener(v -> {
                    SoundEffects.playSelect();
                    NowPlayingManager.togglePlayPause();
                    handler.postDelayed(() -> NowPlayingManager.updateCurrentMedia(MainActivity.this), 250);
                });
            }
            if (btnInlineNext != null) {
                btnInlineNext.setOnClickListener(v -> {
                    SoundEffects.playSelect();
                    NowPlayingManager.skipToNext();
                    handler.postDelayed(() -> NowPlayingManager.updateCurrentMedia(MainActivity.this), 300);
                });
            }
            if (btnInlinePrev != null) {
                btnInlinePrev.setOnClickListener(v -> {
                    SoundEffects.playSelect();
                    NowPlayingManager.skipToPrevious();
                    handler.postDelayed(() -> NowPlayingManager.updateCurrentMedia(MainActivity.this), 300);
                });
            }
            if (btnInlineClose != null) {
                btnInlineClose.setOnClickListener(v -> {
                    SoundEffects.playSelect();
                    disableNowPlaying();
                });
            }

            View.OnFocusChangeListener inlineFocus = (v, hasFocus) -> {
                if (hasFocus) SoundEffects.playNav();
            };
            if (cardAlbumArt != null) cardAlbumArt.setOnFocusChangeListener(inlineFocus);
            if (cardTrackInfo != null) cardTrackInfo.setOnFocusChangeListener(inlineFocus);
            if (btnInlinePrev != null) btnInlinePrev.setOnFocusChangeListener(inlineFocus);
            if (btnInlinePlayPause != null) btnInlinePlayPause.setOnFocusChangeListener(inlineFocus);
            if (btnInlineNext != null) btnInlineNext.setOnFocusChangeListener(inlineFocus);
            if (btnInlineClose != null) btnInlineClose.setOnFocusChangeListener(inlineFocus);

            View.OnKeyListener inlineKey = (v, keyCode, event) -> {
                if (event.getAction() != KeyEvent.ACTION_DOWN) return false;
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if (dockAdapter != null && dockAdapter.getItemCount() > 0) focusDockTile(0);
                    else focusTile(0);
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    if (weatherAirQualityView != null && weatherAirQualityView.getVisibility() == View.VISIBLE) {
                        weatherAirQualityView.requestFocus();
                        return true;
                    }
                }
                return false;
            };
            if (cardAlbumArt != null) cardAlbumArt.setOnKeyListener(inlineKey);
            if (cardTrackInfo != null) cardTrackInfo.setOnKeyListener(inlineKey);
            if (btnInlinePrev != null) btnInlinePrev.setOnKeyListener(inlineKey);
            if (btnInlinePlayPause != null) btnInlinePlayPause.setOnKeyListener(inlineKey);
            if (btnInlineNext != null) btnInlineNext.setOnKeyListener(inlineKey);
            if (btnInlineClose != null) btnInlineClose.setOnKeyListener(inlineKey);
        }
        NowPlayingManager.init(this);
        nowPlayingListener = this::updateNowPlayingUi;
        NowPlayingManager.registerListener(nowPlayingListener);

        islamicShelf = findViewById(R.id.islamicShelf);
        if (islamicShelf != null) {
            islamicShelf.addOnLayoutChangeListener((v, left, top, right, bottom,
                                                     oldLeft, oldTop, oldRight, oldBottom) -> {
                // Keep the panel's bottom anchor fixed when Ayah/prayer content changes height.
                applyIslamicShelfPosition();
            });
            GradientDrawable islamicScrim = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{0x00000000, 0x12000000, 0x26000000});
            islamicScrim.setCornerRadius(10f * density);
            islamicShelf.setBackground(islamicScrim);
        }
        applyHomeShelfSafeMargins();
        if (islamicShelf != null) {
            ayahBlock = findViewById(R.id.ayahBlock);
            ayahLabel = findViewById(R.id.ayahLabel);
            ayahArabic = findViewById(R.id.ayahArabic);
            ayahText = findViewById(R.id.ayahText);
            ayahOpenHint = findViewById(R.id.ayahOpenHint);
            prayerHeader = findViewById(R.id.prayerHeader);

            if (ayahBlock != null) {
                ayahBlock.setFocusable(true);
                ayahBlock.setClickable(true);
                ayahBlock.setOnClickListener(v -> showDailyAyahDialog());
                ayahBlock.setOnKeyListener((v, keyCode, event) -> {
                    if (event.getAction() != KeyEvent.ACTION_DOWN) return false;
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        if (btnAssistant != null && btnAssistant.isShown() && btnAssistant.requestFocus()) return true;
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        View prayerTable = findViewById(R.id.prayerTable);
                        if (prayerTable != null && prayerTable.isShown() && prayerTable.requestFocus()) return true;
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                        showDailyAyahDialog();
                        return true;
                    }
                    return false;
                });
                ayahBlock.setBackground(TvFocusIndicator.createRoundedStateBackground(
                        density, 10f, 0x00000000, TvUiTokens.FOCUS_HALO,
                        0x00000000, TvUiTokens.FOCUS_BORDER));
                ayahBlock.setOnFocusChangeListener((v, hasFocus) -> {
                    if (hasFocus) SoundEffects.playNav();
                });
            }
            if (ayahOpenHint != null) {
                ayahOpenHint.setText(R.string.daily_ayah_open);
                GradientDrawable hintBg = new GradientDrawable();
                hintBg.setColor(0x223F4A55);
                hintBg.setCornerRadius(6 * density);
                hintBg.setStroke(Math.max(1, Math.round(density)), 0x44DCE5ED);
                ayahOpenHint.setBackground(hintBg);
            }

            int uWidth = getResources().getDisplayMetrics().widthPixels - Math.round(68 * density);
            int iMaxWidth = Math.round(uWidth * 0.46f);
            if (ayahArabic != null) ayahArabic.setMaxWidth(iMaxWidth);
            if (ayahText != null) ayahText.setMaxWidth(iMaxWidth);

            lblSubuh = findViewById(R.id.lblSubuh);
            lblSyuruk = findViewById(R.id.lblSyuruk);
            lblZohor = findViewById(R.id.lblZohor);
            lblAsar = findViewById(R.id.lblAsar);
            lblMaghrib = findViewById(R.id.lblMaghrib);
            lblIsyak = findViewById(R.id.lblIsyak);

            timeSubuh = findViewById(R.id.timeSubuh);
            timeSyuruk = findViewById(R.id.timeSyuruk);
            timeZohor = findViewById(R.id.timeZohor);
            timeAsar = findViewById(R.id.timeAsar);
            timeMaghrib = findViewById(R.id.timeMaghrib);
            timeIsyak = findViewById(R.id.timeIsyak);

            updateIslamicShelf();
        }

        btnWifi = findViewById(R.id.btnWifi);
        if (!cfg.showNetworkPill) btnWifi.setVisibility(View.GONE);
        shamsiDate = findViewById(R.id.shamsiDate);
        shamsi = cfg.shamsiDate;
        if (shamsi) {
            shamsiDate.setVisibility(View.VISIBLE);
            TextView date = findViewById(R.id.date);
            date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
            date.setPadding(0, (int) (4 * density), 0, 0);
        }
        prayerTimesView = findViewById(R.id.prayerTimes);
        if (prayerTimesView != null) {
            prayerTimesView.setOnClickListener(v -> showPrayerPopup());
            prayerTimesView.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() != KeyEvent.ACTION_DOWN) return false;
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    View inputs = findViewById(R.id.btnInputs);
                    View wifi = findViewById(R.id.btnWifi);
                    View settings = findViewById(R.id.btnSettings);
                    if (inputs != null && inputs.getVisibility() == View.VISIBLE) inputs.requestFocus();
                    else if (wifi != null && wifi.getVisibility() == View.VISIBLE) wifi.requestFocus();
                    else if (settings != null) settings.requestFocus();
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    focusLastTile();
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) return true;
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                    showPrayerPopup();
                    return true;
                }
                return false;
            });
        }

        // The visible timetable is the real Home entry point; the legacy fallback
        // TextView is GONE and must not be the only route to the prayer popup.
        View prayerTableView = findViewById(R.id.prayerTable);
        if (prayerTableView != null) {
            prayerTableView.setFocusable(true);
            prayerTableView.setClickable(true);
            prayerTableView.setOnClickListener(v -> showPrayerPopup());
            prayerTableView.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() != KeyEvent.ACTION_DOWN) return false;
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    View inputs = findViewById(R.id.btnInputs);
                    View wifi = findViewById(R.id.btnWifi);
                    View settings = findViewById(R.id.btnSettings);
                    if (inputs != null && inputs.getVisibility() == View.VISIBLE) inputs.requestFocus();
                    else if (wifi != null && wifi.getVisibility() == View.VISIBLE) wifi.requestFocus();
                    else if (settings != null) settings.requestFocus();
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    // The prayer timetable is the final upper-shelf stop; enter the primary Dock row.
                    if (dockAdapter != null && dockAdapter.getItemCount() > 0) focusDockTile(0);
                    else focusTile(0);
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    if (ayahBlock != null && ayahBlock.isShown() && ayahBlock.requestFocus()) return true;
                    if (btnAssistant != null && btnAssistant.isShown()) btnAssistant.requestFocus();
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                    showPrayerPopup();
                    return true;
                }
                return false;
            });
            prayerTableView.setBackground(TvFocusIndicator.createRoundedStateBackground(
                    density, 8f, 0x00000000, TvUiTokens.FOCUS_HALO,
                    0x00000000, TvUiTokens.FOCUS_BORDER));
            prayerTableView.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) SoundEffects.playNav();
            });
        }
        updatePrayerTimes();
        PrayerTimes.syncIfDue(this, this::updatePrayerTimes);
        vpnApp = cfg.vpnApp;
        moduleCards = cfg.moduleCards;
        hiddenApps = new HashSet<>(cfg.hiddenApps);

        grid = findViewById(R.id.grid);
        gridWrap = findViewById(R.id.gridWrap);
        gridBasePaddingTop = grid.getPaddingTop();
        gridBasePaddingBottom = grid.getPaddingBottom();
        grid.setLayoutManager(new GridLayoutManager(this, COLUMNS));
        grid.setHasFixedSize(true);
        grid.setItemAnimator(null);
        Backdrop.watch(grid);

        // Dock is a sibling surface inside the same clipped/fade-aware host, not a row in the App Grid.
        dockRecycler = new RecyclerView(this);
        dockRecycler.setHasFixedSize(true);
        dockRecycler.setItemAnimator(null);
        dockRecycler.setClipChildren(false);
        dockRecycler.setClipToPadding(false);
        dockRecycler.setFocusable(true);
        dockRecycler.setOverScrollMode(View.OVER_SCROLL_NEVER);
        gridWrap.addView(dockRecycler, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Backdrop.watch(dockRecycler);
        setUpGlider();
        // Both Dock and Grid app cards now use the same DockBreathingBorderView focus animation.
        // Do not add a second static Grid outline or a Glider companion over the same focused tile.
        // Preserve one shared glass capsule for Home/Profile/Settings.
        // Each focusable header control paints the same moving Glider after its glass surface
        // and before its own text/icon content, so no capsule can cover the focus indicator.
        // Individual tabs below remain independent focus/click targets with transparent backgrounds.
        View navCapsule = findViewById(R.id.navPill);
        if (navCapsule != null) {
            navCapsule.setBackground(new GlassDrawable(navCapsule, 20f * density, density, glider));
            glider.addCompanion(navCapsule);
            Backdrop.watch(navCapsule);
        }
        // One restrained glass surface unifies the prayer timetable and Ayat Pilihan.
        // Background-only treatment: keep shelf bounds, child layout, text, RTL and spacing unchanged.
        View islamicGlassPanel = findViewById(R.id.islamicShelf);
        if (islamicGlassPanel != null) {
            islamicGlassPanel.setBackground(new GlassDrawable(
                    islamicGlassPanel, 18f * density, density, null, 0.55f));
            Backdrop.watch(islamicGlassPanel);
        }
        for (int id : new int[]{R.id.locationPill, R.id.weatherAirQuality, R.id.btnInputs,
                R.id.btnWifi, R.id.btnAssistant, R.id.btnHome, R.id.btnCandyBox, R.id.btnSettings}) {
            View pill = findViewById(id);
            if (pill == null) continue;
            boolean navigationTab = id == R.id.btnHome || id == R.id.btnCandyBox
                    || id == R.id.btnSettings;
            if (navigationTab) {
                // Paint the same moving Glider inside each tab's own drawing pass:
                // above the shared capsule surface, but below the tab's icon and label.
                pill.setBackground(new GliderFocusDrawable(pill, glider));
            } else {
                // Compose the focus layer after the capsule glass and before the view's content.
                Drawable glassBackground = new GlassDrawable(pill, 20f * density, density, glider);
                pill.setBackground(new android.graphics.drawable.LayerDrawable(
                        new Drawable[]{glassBackground, new GliderFocusDrawable(pill, glider)}));
            }
            glider.addCompanion(pill);
            Backdrop.watch(pill);
            pill.setOnFocusChangeListener((v, has) -> {
                if (has) {
                    // Any header focus supersedes a deferred Grid target.
                    gridFocusRequestGeneration++;
                    moveTopHighlight(v);
                    // Keep each tab's measured segment geometry stable inside the unified capsule.
                }
            });
        }
        if (prayerTimesView != null) {
            prayerTimesView.setOnFocusChangeListener((v, has) -> {
                if (has) {
                    SoundEffects.playNav();
                    glider.hide();
                }
            });
        }
        // The apps rise into place on the first launch after the TV starts, and only then. Setting
        // the animation arms it for the grid's first draw, so on any later fresh start (Android
        // closing Home while an app was open) it is not set at all: armed, it drew the dock's tray
        // empty while the apps faded in.
        if (!tilesAnimatedOnce && isIconAnimationsEnabled()) {
            int enterAnim = reducedMotion ? R.anim.tile_enter_reduced : R.anim.tile_enter;
            float enterDelay = reducedMotion ? 0f : 0.06f;
            grid.setLayoutAnimation(new LayoutAnimationController(
                    AnimationUtils.loadAnimation(this, enterAnim), enterDelay));
        }
        adapter = new AppAdapter(this, this, COLUMNS, cfg);
        grid.setAdapter(adapter);
        dockAdapter = new DockAdapter(this, adapter, this, reducedMotion);
        dockRecycler.setAdapter(dockAdapter);
        dockRecycler.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            int measuredWidth = Math.max(0, right - left);
            if (dockAdapter != null && measuredWidth > 0) dockAdapter.setHorizontalViewportWidth(measuredWidth);
            if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom) {
                homeShelfAnchorReady = false;
                if (heroShown) updateHomeShelfAnchor();
            }
        });
        applyDockLayout();
        glider.addCompanion(dockRecycler, tray.area);
        holdFirstFrame();

        // Recalculate the two independent regions when the host receives a new size.
        gridWrap.addOnLayoutChangeListener((v, l, t, r, b, ol, ot, or, ob) -> {
            parkDock();
            updateTopFade();
            if (l != ol || t != ot || r != or || b != ob) {
                homeShelfAnchorReady = false;
                if (heroShown) updateHomeShelfAnchor();
            }
        });
        grid.addOnLayoutChangeListener((v, l, t, r, b, ol, ot, or, ob) -> updateTopFade());

        // Keep the glass glued to its tile while the grid scrolls.
        grid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                updateTopFade();
                if (!glassShown) return;
                glassTargetY -= dy;
                glassScrollShift -= dy;
                glider.scrolled(dy);
            }
        });

        // Liquid feel: soft springs on the highlight's position.
        springX = new SpringAnimation(glass, DynamicAnimation.TRANSLATION_X);
        springY = new SpringAnimation(glass, new FloatPropertyCompat<View>("glassContentY") {
            @Override
            public float getValue(View v) {
                return v.getTranslationY() - glassScrollShift;
            }

            @Override
            public void setValue(View v, float value) {
                v.setTranslationY(value + glassScrollShift);
            }
        });
        for (SpringAnimation s : new SpringAnimation[]{springX, springY}) {
            if (reducedMotion) {
                // Stiff and almost critically damped: settles in a few frames instead of oscillating,
                // and stops once the highlight is within 1.5 px of its tile.
                s.setSpring(new SpringForce().setStiffness(1400f).setDampingRatio(0.95f));
                s.setMinimumVisibleChange(1.5f);
            } else {
                // Critically damped: glides onto the tile and stops there, with no bounce back.
                s.setSpring(new SpringForce().setStiffness(900f).setDampingRatio(1.0f));
            }
        }

        btnInputs = findViewById(R.id.btnInputs);
        btnAssistant = findViewById(R.id.btnAssistant);
        btnHome = findViewById(R.id.btnHome);
        btnCandyBox = findViewById(R.id.btnCandyBox);
        View btnSettings = findViewById(R.id.btnSettings);
        updateHeaderTabLabels();
        updateLocationPill();
        if (btnHome != null) btnHome.setOnClickListener(v -> updateHomeTabIndicator());
        if (btnInputs != null) btnInputs.setOnClickListener(v -> showInputs());
        if (btnWifi != null) {
            btnWifi.setOnClickListener(v -> showWifiPopup());
            btnWifi.setOnLongClickListener(v -> {
                open(systemIntent(Settings.ACTION_WIFI_SETTINGS));
                return true;
            });
        }
        if (btnAssistant != null) {
            btnAssistant.setOnClickListener(v -> triggerGoogleAssistant());
        }
        if (btnCandyBox != null) {
            btnCandyBox.setOnClickListener(v -> showCandyBoxPopup());
            updateTopBarUserIcon();
        }
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> showQuickControlPopup());
            btnSettings.setOnLongClickListener(v -> {
                if (moduleCards) {
                    showModuleCards();
                    return true;
                }
                openSystemSettings(systemIntent(Settings.ACTION_SETTINGS));
                return true;
            });
        }

        // Owner-defined Home DPAD graph:
        // Header DOWN -> primary Dock row; Dock DOWN -> matching overflow Grid column.
        // Grid UP -> matching Dock column; Dock UP -> top shelf.
        // Home LEFT -> Location DOWN -> Weather/AQI DOWN -> primary Dock row.
        // Mic DOWN -> Ayah -> Prayer timetable -> primary Dock row. Inputs remains an optional branch.
        View locationPill = findViewById(R.id.locationPill);
        if (locationPill != null) locationPill.setOnClickListener(v -> showPrayerZoneDialog());
        View.OnKeyListener pillKeys = (v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN) return false;
            int id = v.getId();
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER
                    || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                v.performClick();
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                if (id == R.id.btnWifi && btnInputs != null && btnInputs.isShown()
                        && btnInputs.requestFocus()) return true;
                if (id == R.id.btnAssistant && btnWifi != null && btnWifi.isShown()
                        && btnWifi.requestFocus()) return true;
                if (id == R.id.btnInputs && btnSettings != null && btnSettings.requestFocus()) return true;
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                if (id == R.id.locationPill && weatherAirQualityView != null
                        && weatherAirQualityView.isShown() && weatherAirQualityView.requestFocus()) return true;
                if (id == R.id.btnAssistant) {
                    if (ayahBlock != null && ayahBlock.isShown() && ayahBlock.requestFocus()) return true;
                }
                if (id == R.id.btnInputs && btnWifi != null && btnWifi.isShown()
                        && btnWifi.requestFocus()) return true;
                // The Dock is the primary visible app row; do not reveal the overflow Grid
                // directly from a header capsule.
                if (dockAdapter != null && dockAdapter.getItemCount() > 0) focusDockTile(0);
                else focusTile(0);
                return true;
            }
            View target = null;
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (id == R.id.locationPill) return true;
                if (id == R.id.btnHome) target = locationPill;
                else if (id == R.id.btnCandyBox) target = btnHome;
                else if (id == R.id.btnSettings) target = btnCandyBox;
                else if (id == R.id.btnWifi) target = btnSettings;
                else if (id == R.id.btnAssistant) target = btnWifi;
                else if (id == R.id.btnInputs) target = btnSettings;
                else return false;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (id == R.id.locationPill || id == R.id.weatherAirQuality) target = btnHome;
                else if (id == R.id.btnHome) target = btnCandyBox;
                else if (id == R.id.btnCandyBox) target = btnSettings;
                else if (id == R.id.btnSettings)
                    target = btnWifi != null && btnWifi.isShown() ? btnWifi : btnAssistant;
                else if (id == R.id.btnWifi) target = btnAssistant;
                else if (id == R.id.btnInputs)
                    target = btnWifi != null && btnWifi.isShown() ? btnWifi : btnAssistant;
                else if (id == R.id.btnAssistant) return true;
                else return false;
            } else {
                return false;
            }
            return target != null && target.isShown() && target.requestFocus();
        };
        if (locationPill != null) locationPill.setOnKeyListener(pillKeys);
        if (btnHome != null) btnHome.setOnKeyListener(pillKeys);
        if (btnCandyBox != null) btnCandyBox.setOnKeyListener(pillKeys);
        if (btnSettings != null) btnSettings.setOnKeyListener(pillKeys);
        if (btnInputs != null) btnInputs.setOnKeyListener(pillKeys);
        if (btnWifi != null) btnWifi.setOnKeyListener(pillKeys);
        if (btnAssistant != null) btnAssistant.setOnKeyListener(pillKeys);
        // Weather/AQI key handling is installed once during widget initialization above.
        // Do not register another listener here: View.setOnKeyListener replaces the previous one.

        // Measure the actual clock/date content edge, not leftBlock's constrained RelativeLayout
        // bounds. Using leftBlock.getRight() here can make the center navigation collapse to 0px.
        View leftHeader = findViewById(R.id.leftBlock);
        View dateHeader = findViewById(R.id.dateBlock);
        View rightHeader = findViewById(R.id.topButtons);
        View navHeader = findViewById(R.id.navPill);
        ViewGroup headerRow = leftHeader != null && leftHeader.getParent() instanceof ViewGroup
                ? (ViewGroup) leftHeader.getParent() : null;
        View.OnLayoutChangeListener headerMetricsListener =
                (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) ->
                        root.post(this::applyHeaderWidths);
        if (headerRow != null) headerRow.addOnLayoutChangeListener(headerMetricsListener);
        if (leftHeader != null) leftHeader.addOnLayoutChangeListener(headerMetricsListener);
        if (dateHeader != null) dateHeader.addOnLayoutChangeListener(headerMetricsListener);
        if (rightHeader != null) rightHeader.addOnLayoutChangeListener(headerMetricsListener);
        if (navHeader != null) navHeader.addOnLayoutChangeListener(headerMetricsListener);
        root.post(this::applyHeaderWidths);

        // Hide the glass when focus leaves the tiles.
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalFocusChangeListener((oldF, newF) -> {
            boolean onTile = newF != null && newF.getId() == R.id.card;
            // Any non-Grid focus destination supersedes queued Grid/resume restoration work.
            if (newF != null && !onTile) gridFocusRequestGeneration++;
            if (newF != null && isTopPill(newF)) {
                String pillKey = topPillKeyForView(newF);
                if (pillKey != null) {
                    lastFocusWasTopPill = true;
                    lastFocusedTopPillKey = pillKey;
                    getSharedPreferences(NavigationState.PREFS, MODE_PRIVATE).edit()
                            .putBoolean(NavigationState.HOME_FOCUS_TOP_PILL, true)
                            .putString(NavigationState.HOME_FOCUS_TOP_PILL_KEY, pillKey).apply();
                }
            } else if (newF != null && !onTile) {
                lastFocusWasTopPill = false;
                lastFocusedTopPillKey = null;
                getSharedPreferences(NavigationState.PREFS, MODE_PRIVATE).edit()
                        .putBoolean(NavigationState.HOME_FOCUS_TOP_PILL, false)
                        .remove(NavigationState.HOME_FOCUS_TOP_PILL_KEY).apply();
            }
            if (!onTile) hideGlass();
            if (!onTile && (newF == null || !(isTopPill(newF) || inMenu(newF)))) glider.hide();
        });

        packageFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        packageFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        packageFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        packageFilter.addDataScheme("package");

        ConnectivityManager cm = getSystemService(ConnectivityManager.class);
        TvInputManager tim = (TvInputManager) getSystemService(TV_INPUT_SERVICE);
        if (tim != null) tim.registerCallback(inputCallback, handler);
        if (cm != null) {
            cm.registerDefaultNetworkCallback(networkCallback);
            cm.registerNetworkCallback(new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
                    .build(), vpnCallback);
        }

        // Silent background update check after 5 seconds on launch

        if (getIntent() != null && getIntent().getBooleanExtra("open_settings", false)) {
            getIntent().removeExtra("open_settings");
            handler.postDelayed(() -> {
                if (!isFinishing() && !isDestroyed()) {
                    if (screensaverActive) dismissScreensaver();
                    showSettingsPopup();
                }
            }, 180);
        }
        if (getIntent() != null && getIntent().getBooleanExtra("open_candy_box", false)) {
            handler.postDelayed(() -> {
                if (screensaverActive) dismissScreensaver();
                showCandyBoxPopup();
            }, 100);
        }
        if (getIntent() != null && getIntent().getBooleanExtra("open_azan_settings", false)) {
            handler.postDelayed(() -> {
                if (screensaverActive) dismissScreensaver();
                showAzanSettingsDialog();
            }, 100);
        }
        if (getIntent() != null && getIntent().getBooleanExtra("open_clean_cache", false)) {
            handler.postDelayed(() -> {
                if (screensaverActive) dismissScreensaver();
                showCleanTvCacheDialog();
            }, 100);
        }
        if (getIntent() != null && getIntent().getBooleanExtra("open_wallpaper", false)) {
            handler.postDelayed(() -> {
                if (screensaverActive) dismissScreensaver();
                showWallpaperDialog();
            }, 100);
        }
        if (getIntent() != null && getIntent().getBooleanExtra("open_now_playing", false)) {
            handler.postDelayed(() -> {
                if (screensaverActive) dismissScreensaver();
                showNowPlayingDialog();
            }, 100);
        }
    }

    /**
     * A theme's full-screen picture behind everything. Decoded once, downsampled to the screen so a 4K
     * JPEG costs a 1080p bitmap at most (about 8 MB), which is the price of a wallpaper on a 1 GB TV.
     */
    private android.graphics.Bitmap decodeWallpaper(File file) {
        try {
            android.util.DisplayMetrics dm = getResources().getDisplayMetrics();
            android.graphics.BitmapFactory.Options opts = new android.graphics.BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            android.graphics.BitmapFactory.decodeFile(file.getPath(), opts);
            int sample = 1;
            while (opts.outWidth / (sample * 2) >= dm.widthPixels && opts.outHeight / (sample * 2) >= dm.heightPixels) sample *= 2;
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = sample;
            opts.inPreferredConfig = android.graphics.Bitmap.Config.RGB_565;
            return android.graphics.BitmapFactory.decodeFile(file.getPath(), opts);
        } catch (Exception | OutOfMemoryError e) {
            // A bad or huge picture must never take the home screen down; the plain colour stays.
            return null;
        }
    }

    /**
     * Decode and optionally blur away from the UI thread. A generation token coalesces rapid
     * selection changes: stale queued work exits before decoding and stale completed bitmaps are
     * released rather than being posted to a destroyed or superseded Home screen.
     */
    private void queueWallpaperDecode(File file, boolean fade, boolean prepareBlur, boolean updatePlace) {
        if (file == null) return;
        final long generation = wallpaperRequestGeneration.incrementAndGet();
        final android.util.DisplayMetrics metrics = getResources().getDisplayMetrics();
        final int screenW = metrics.widthPixels;
        final int screenH = metrics.heightPixels;
        final int multiply = placesMode ? 0xFF7A7A7A : 0;
        try {
            wallDecoder.execute(() -> {
                if (Thread.currentThread().isInterrupted()
                        || generation != wallpaperRequestGeneration.get()) return;
                android.graphics.Bitmap bmp = decodeWallpaper(file);
                if (bmp == null) {
                    android.util.Log.w("TvHome", "Wallpaper decode failed: " + file.getName());
                    return;
                }
                android.graphics.Bitmap blur = null;
                if (prepareBlur && isGlassEffectsEnabled()) {
                    blur = Backdrop.blur(bmp, screenW, screenH, multiply);
                }
                if (Thread.currentThread().isInterrupted()
                        || generation != wallpaperRequestGeneration.get()) {
                    if (!bmp.isRecycled()) bmp.recycle();
                    if (blur != null && !blur.isRecycled()) blur.recycle();
                    return;
                }
                final android.graphics.Bitmap readyBitmap = bmp;
                final android.graphics.Bitmap readyBlur = blur;
                boolean posted = handler.post(() -> {
                    if (isDestroyed() || generation != wallpaperRequestGeneration.get()) {
                        if (!readyBitmap.isRecycled()) readyBitmap.recycle();
                        if (readyBlur != null && !readyBlur.isRecycled()) readyBlur.recycle();
                        return;
                    }
                    setWallpaper(readyBitmap, fade, readyBlur);
                    if (updatePlace) setPlace(file);
                });
                if (!posted) {
                    if (!readyBitmap.isRecycled()) readyBitmap.recycle();
                    if (readyBlur != null && !readyBlur.isRecycled()) readyBlur.recycle();
                }
            });
        } catch (java.util.concurrent.RejectedExecutionException e) {
            android.util.Log.w("TvHome", "Wallpaper decode skipped because Home is shutting down", e);
        }
    }

    /** Loads the QR-upload preview off the UI thread and discards it if the dialog/request is stale. */
    private void queueWallpaperThumbnail(File file) {
        final ImageView target = qrImageView;
        if (file == null || target == null) return;
        final long generation = wallpaperThumbnailGeneration.incrementAndGet();
        try {
            wallDecoder.execute(() -> {
                if (Thread.currentThread().isInterrupted()
                        || generation != wallpaperThumbnailGeneration.get()) return;
                android.graphics.Bitmap thumb = decodeWallpaper(file);
                if (thumb == null) return;
                if (Thread.currentThread().isInterrupted()
                        || generation != wallpaperThumbnailGeneration.get()) {
                    if (!thumb.isRecycled()) thumb.recycle();
                    return;
                }
                boolean posted = handler.post(() -> {
                    if (isDestroyed() || generation != wallpaperThumbnailGeneration.get()
                            || qrImageView != target || qrUploadDialog == null || !qrUploadDialog.isShowing()) {
                        if (!thumb.isRecycled()) thumb.recycle();
                        return;
                    }
                    target.setImageBitmap(thumb);
                    target.setScaleType(ImageView.ScaleType.CENTER_CROP);
                });
                if (!posted && !thumb.isRecycled()) thumb.recycle();
            });
        } catch (java.util.concurrent.RejectedExecutionException e) {
            android.util.Log.d("TvHome", "Wallpaper preview skipped because Home is shutting down");
        }
    }

    /** Puts a picture behind everything, cross-fading from the previous one when asked. */
    private void setWallpaper(android.graphics.Bitmap bmp, boolean fade) {
        setWallpaper(bmp, fade, null);
    }

    /** With {@code blur} made in advance, the glass changes in step with the picture. */
    private void setWallpaper(android.graphics.Bitmap bmp, boolean fade, android.graphics.Bitmap blur) {
        if (bmp == null) return;
        if (wallView == null) {
            wallView = new android.widget.ImageView(this);
            wallView.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
            // Photos are bright: dim them so the clock, names and tiles stay readable.
            if (placesMode) wallView.setColorFilter(0xFF7A7A7A, android.graphics.PorterDuff.Mode.MULTIPLY);
            root.addView(wallView, 0, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ensureWeatherOverlayView();
        }
        android.graphics.Bitmap old = wallBitmap;
        wallBitmap = bmp;
        if (fade && old != null && !reducedMotion) {
            android.graphics.drawable.TransitionDrawable t = new android.graphics.drawable.TransitionDrawable(new android.graphics.drawable.Drawable[]{
                    new android.graphics.drawable.BitmapDrawable(getResources(), old),
                    new android.graphics.drawable.BitmapDrawable(getResources(), bmp)});
            t.setCrossFadeEnabled(true);
            wallView.setImageDrawable(t);
            t.startTransition(800);
            // The glass fades to the new picture over the same 800 ms as the wallpaper itself.
            if (blur != null) Backdrop.present(blur, 800);
            else blurBackdrop(bmp);
            // Drop the old picture once the fade is done, so only one 1080p bitmap stays in memory.
            handler.postDelayed(() -> {
                if (wallBitmap == bmp) wallView.setImageBitmap(bmp);
            }, 900);
        } else {
            wallView.setImageBitmap(bmp);
            if (blur != null) Backdrop.present(blur, 0);
            else blurBackdrop(bmp);
        }
        // An opaque picture covers the whole screen, so the black window and the plain background
        // under it are never seen. Painting them anyway cost two full-screen passes on every frame,
        // and this TV's graphics chip, not the app, is what holds each frame back.
        if (!bmp.hasAlpha()) {
            root.setBackground(null);
            getWindow().setBackgroundDrawable(null);
        }
    }

    /** Hands the picture now on screen to the glass, which blurs it once, off the main thread. */
    private void blurBackdrop(android.graphics.Bitmap bmp) {
        if (!isGlassEffectsEnabled()) return;
        android.util.DisplayMetrics dm = getResources().getDisplayMetrics();
        Backdrop.update(bmp, dm.widthPixels, dm.heightPixels, placesMode ? 0xFF7A7A7A : 0, wallDecoder, handler);
    }

    private int getWallpaperIntervalMinutes() {
        return getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getInt("wallpaper_interval_minutes", 5);
    }

    private String getWallpaperIntervalSummary() {
        int min = getWallpaperIntervalMinutes();
        if (min == 0) return "Manual";
        if (min == 60) return "1 Jam";
        return min + " Min";
    }

    private long getWallpaperIntervalMs() {
        int min = getWallpaperIntervalMinutes();
        if (min <= 0) return 0;
        return min * 60_000L;
    }

    private boolean isWallpaperDescEnabled() {
        return getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getBoolean("show_wallpaper_desc", true);
    }

    private void setWallpaperDescEnabled(boolean enabled) {
        getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).edit().putBoolean("show_wallpaper_desc", enabled).apply();
    }

    /** Shows the next picture from the wallpapers folder; the position survives restarts. */
    private void showNextWallpaper(boolean fade) {
        if (wallpapers == null || wallpapers.isEmpty()) return;
        android.content.SharedPreferences prefs = getSharedPreferences("wallpaper", MODE_PRIVATE);
        int last = prefs.getInt("index", -1);
        boolean shuffle = getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getBoolean("shuffle_mode", true);
        int next;
        String selectedFileName;
        if (shuffle && wallpapers.size() > 1) {
            List<String> fileNames = new ArrayList<>();
            for (File wallpaper : wallpapers) fileNames.add(wallpaper.getName());

            android.content.SharedPreferences shufflePrefs = getSharedPreferences("wallpaper_prefs", MODE_PRIVATE);
            String previousFile = shufflePrefs.getString("shuffle_last_file", "");
            List<String> cycle = new ArrayList<>();
            int position = 0;
            boolean validCycle = false;
            String storedCycle = shufflePrefs.getString("shuffle_cycle", null);
            if (storedCycle != null) {
                try {
                    org.json.JSONArray json = new org.json.JSONArray(storedCycle);
                    for (int i = 0; i < json.length(); i++) cycle.add(json.getString(i));
                    position = shufflePrefs.getInt("shuffle_position", 0);
                    validCycle = cycle.size() == fileNames.size()
                            && new HashSet<>(cycle).size() == fileNames.size()
                            && new HashSet<>(cycle).equals(new HashSet<>(fileNames))
                            && position >= 0 && position < cycle.size();
                } catch (org.json.JSONException ignored) {
                    validCycle = false;
                }
            }

            if (!validCycle) {
                cycle = WallpaperShuffle.newCycle(fileNames, previousFile, new java.util.Random());
                org.json.JSONArray json = new org.json.JSONArray();
                for (String name : cycle) json.put(name);
                shufflePrefs.edit().putString("shuffle_cycle", json.toString())
                        .putInt("shuffle_position", 0).apply();
                position = 0;
            }

            selectedFileName = cycle.get(position);
            next = fileNames.indexOf(selectedFileName);
            if (next < 0) {
                // A file changed while the list was being refreshed; rebuild once from the live list.
                cycle = WallpaperShuffle.newCycle(fileNames, previousFile, new java.util.Random());
                selectedFileName = cycle.get(0);
                next = fileNames.indexOf(selectedFileName);
                org.json.JSONArray json = new org.json.JSONArray();
                for (String name : cycle) json.put(name);
                shufflePrefs.edit().putString("shuffle_cycle", json.toString())
                        .putInt("shuffle_position", 1).apply();
            } else {
                shufflePrefs.edit().putInt("shuffle_position", position + 1).apply();
            }
        } else {
            next = (last + 1) % wallpapers.size();
            selectedFileName = wallpapers.get(next).getName();
        }
        prefs.edit().putInt("index", next).apply();
        getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).edit()
                .putString("shuffle_last_file", selectedFileName).apply();
        wallShownAt = android.os.SystemClock.elapsedRealtime();
        final File file = wallpapers.get(next);
        // On a cold start, name the place straight away: waiting for the decode showed the app's name
        // at full size and shrank it a moment later. After that the name changes with the picture,
        // or it would name the next place over the last one's picture while that one decodes.
        if (placeName == null) setPlace(file);
        final boolean firstPicture = wallBitmap == null;
        queueWallpaperDecode(file, fade, !firstPicture, true);
    }

    /** Switches to the next picture every wallpaper interval while Home is in front. */
    private final Runnable wallTick = new Runnable() {
        @Override
        public void run() {
            if (wallpapers != null && wallpapers.size() >= 2 && wallpaperIntervalMs > 0) {
                showNextWallpaper(true);
            }
            if (wallpaperIntervalMs > 0) {
                handler.postDelayed(this, wallpaperIntervalMs);
            }
        }
    };

    /** Fetches the week's places in the background, then switches the rotation to them. */
    private void refreshPlacesIfDue() {
        if (!placesMode || placesRefreshRunning || !PlaceWallpapers.due(this)) return;
        placesRefreshRunning = true;
        new Thread(() -> {
            boolean changed = PlaceWallpapers.refresh(getApplicationContext());
            handler.post(() -> {
                placesRefreshRunning = false;
                if (!changed || isDestroyed()) return;
                List<File> places = PlaceWallpapers.list(this);
                if (places.isEmpty()) return;
                // Last week's pictures are gone: show one of this week's straight away, so the
                // name on the shelf and the picture behind it always match.
                wallpapers = places;
                showNextWallpaper(wallBitmap != null);
                if (!isFinishing() && hasWindowFocus()) startWallpaperRotation();
            });
        }, "place-wallpapers").start();
    }

    private boolean onlineRefreshRunning;

    /** Fetches fresh 4K online wallpapers in the background when due. */
    private void refreshOnlineWallpapersIfDue() {
        if (config == null || !config.wallpaperOnline || onlineRefreshRunning || !OnlineWallpapers.due(this)) return;
        onlineRefreshRunning = true;
        new Thread(() -> {
            boolean changed = OnlineWallpapers.sync(getApplicationContext(), config.wallpaperFeed);
            handler.post(() -> {
                onlineRefreshRunning = false;
                if (!changed || isDestroyed()) return;
                List<File> fresh = OnlineWallpapers.list(this);
                if (fresh.isEmpty()) return;
                List<File> combined = new ArrayList<>(config.wallpapers);
                for (File f : fresh) {
                    if (!combined.contains(f)) combined.add(f);
                }
                wallpapers = combined;
                if (wallBitmap == null) showNextWallpaper(false);
                if (!isFinishing() && hasWindowFocus()) startWallpaperRotation();
            });
        }, "online-wallpapers").start();
    }

    /** Clears the TV's app caches when due, once Home has sat idle on screen for a while. */
    private final Runnable cacheClean = () -> {
        if (hasWindowFocus() && config != null) CacheCleaner.maybeRun(this, config.cacheCleanDays);
    };

    /** Whenever Home comes back: picks the rotation up where the interval left it. */
    private void startWallpaperRotation() {
        refreshPlacesIfDue();
        refreshOnlineWallpapersIfDue();
        handler.removeCallbacks(wallTick);
        wallpaperIntervalMs = getWallpaperIntervalMs();
        if (wallpapers == null || wallpapers.size() < 2 || wallpaperIntervalMs <= 0) return;
        long waited = android.os.SystemClock.elapsedRealtime() - wallShownAt;
        // A picture that fell due while you were in an app waits a few seconds: decoding, blurring
        // and fading it the moment you come back competed with Home for the TV's slow CPU.
        handler.postDelayed(wallTick, Math.max(RETURN_SETTLE_MS, wallpaperIntervalMs - waited));
    }

    int getStandbyPhotoCount() {
        return StandbyPhotoStore.getPhotoCount(this);
    }

    boolean isStandbyPhotoMode() {
        return StandbyPhotoStore.isPhotoMode(this);
    }

    boolean setStandbyPhotoMode(boolean enabled) {
        if (enabled && getStandbyPhotoCount() == 0) {
            TvFeedback.show(this, R.string.standby_photo_required, Toast.LENGTH_LONG);
            return false;
        }
        if (!StandbyPhotoStore.setPhotoMode(this, enabled)) {
            TvFeedback.show(this, R.string.standby_mode_save_failed, Toast.LENGTH_LONG);
            return false;
        }
        standbyPhotoUriShown = "";
        updateNightstandScreensaver();
        TvFeedback.show(this, enabled ? R.string.standby_photo_mode_enabled : R.string.standby_clock_mode_enabled, Toast.LENGTH_SHORT);
        return true;
    }

    void pickStandbyPhotos() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        try {
            startActivityForResult(intent, REQUEST_STANDBY_PHOTOS);
        } catch (Exception e) {
            TvFeedback.show(this, R.string.standby_picker_unavailable, Toast.LENGTH_LONG);
        }
    }

    void clearStandbyPhotos() {
        List<Uri> selectedUris = StandbyPhotoStore.clearCollectionAndMode(this);
        if (selectedUris == null) {
            TvFeedback.show(this, R.string.standby_clear_failed, Toast.LENGTH_LONG);
            return;
        }
        for (Uri uri : selectedUris) {
            // Managed QR photos are file:// URIs and their files were removed by the store.
            if ("file".equalsIgnoreCase(uri.getScheme())) continue;
            try {
                getContentResolver().releasePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (Exception ignored) {
            }
        }
        standbyPhotoUriShown = "";
        updateNightstandScreensaver();
        TvFeedback.show(this, R.string.standby_list_cleared, Toast.LENGTH_SHORT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_STANDBY_PHOTOS || resultCode != Activity.RESULT_OK || data == null) return;

        List<Uri> selected = new ArrayList<>();
        Set<String> unique = new HashSet<>();
        android.content.ClipData clip = data.getClipData();
        if (clip != null) {
            for (int i = 0; i < clip.getItemCount(); i++) {
                Uri uri = clip.getItemAt(i).getUri();
                if (uri != null && unique.add(uri.toString())) selected.add(uri);
            }
        } else if (data.getData() != null) {
            Uri uri = data.getData();
            if (unique.add(uri.toString())) selected.add(uri);
        }

        if (selected.isEmpty()) return;
        List<Uri> existingUris = StandbyPhotoStore.getUris(this);
        List<Uri> granted = new ArrayList<>();
        int skippedByLimit = 0;
        for (Uri uri : selected) {
            if (existingUris.contains(uri)) continue;
            if (existingUris.size() + granted.size() >= StandbyPhotoStore.MAX_PHOTO_COUNT) {
                skippedByLimit++;
                continue;
            }
            try {
                getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                granted.add(uri);
            } catch (Exception e) {
                android.util.Log.w("TvHome.StandBy", "Persistable photo permission unavailable for " + uri, e);
            }
        }
        if (granted.isEmpty()) {
            boolean allAlreadySelected = true;
            for (Uri uri : selected) {
                if (!existingUris.contains(uri)) {
                    allAlreadySelected = false;
                    break;
                }
            }
            String message = skippedByLimit > 0
                    ? getString(R.string.standby_collection_limit, StandbyPhotoStore.MAX_PHOTO_COUNT)
                    : (allAlreadySelected ? getString(R.string.standby_photos_already_selected)
                    : getString(R.string.standby_photo_access_error));
            TvFeedback.show(this, message, Toast.LENGTH_LONG);
            return;
        }
        // Merge against the latest persisted collection inside StandbyPhotoStore's
        // serialized mutation boundary; an upload may have completed while the picker was open.
        StandbyPhotoStore.UriMergeResult update = StandbyPhotoStore.appendUris(this, granted);
        List<Uri> finalUris = StandbyPhotoStore.getUris(this);
        for (Uri uri : granted) {
            if (!finalUris.contains(uri)) {
                try {
                    getContentResolver().releasePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (Exception ignored) {
                }
            }
        }
        if (!update.persisted) {
            TvFeedback.show(this, R.string.standby_save_failed, Toast.LENGTH_LONG);
            return;
        }
        int added = update.addedCount;
        int skipped = skippedByLimit + update.skippedCount;
        if (added == 0) {
            String message = skipped > 0
                    ? getString(R.string.standby_collection_limit, StandbyPhotoStore.MAX_PHOTO_COUNT)
                    : getString(R.string.standby_photos_already_selected);
            TvFeedback.show(this, message, Toast.LENGTH_LONG);
            return;
        }
        standbyPhotoUriShown = "";
        updateNightstandScreensaver();
        String message = getString(R.string.standby_photos_added, added, finalUris.size());
        if (skipped > 0) message += " " + getString(R.string.standby_photos_skipped, skipped);
        TvFeedback.show(this, message, Toast.LENGTH_LONG);
    }

    /**
     * Home pressed while Home is already in front: singleTask delivers it here, then onResume runs.
     * Back to the top with the first dock tile focused, like the stock launcher. Back from an app
     * never comes this way, so returning to the app you left from is unchanged (#118).
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getBooleanExtra("open_settings", false)) {
            if (screensaverActive) dismissScreensaver();
            showSettingsPopup();
            return;
        }
        if (intent != null && intent.getBooleanExtra("open_candy_box", false)) {
            if (screensaverActive) dismissScreensaver();
            showCandyBoxPopup();
            return;
        }
        if (intent != null && intent.getBooleanExtra("open_azan_settings", false)) {
            if (screensaverActive) dismissScreensaver();
            showAzanSettingsDialog();
            return;
        }
        if (intent != null && intent.getBooleanExtra("open_clean_cache", false)) {
            if (screensaverActive) dismissScreensaver();
            showCleanTvCacheDialog();
            return;
        }
        if (intent != null && intent.getBooleanExtra("open_wallpaper", false)) {
            if (screensaverActive) dismissScreensaver();
            showWallpaperDialog();
            return;
        }
        if (intent != null && intent.getBooleanExtra("open_now_playing", false)) {
            if (screensaverActive) dismissScreensaver();
            showNowPlayingDialog();
            return;
        }
        if (intent != null && intent.getBooleanExtra("screensaver", false)) {
            showNightstandScreensaver();
            return;
        }
        if (intent == null || !intent.hasCategory(Intent.CATEGORY_HOME)) return;
        if (hiddenSinceResume) return;
        if (screensaverActive) {
            dismissScreensaver();
            return;
        }
        // Single home pressed on Home
        if (cardPrompt != null) cardPrompt.dismiss();
        if (openPopup != null) openPopup.dismiss();
        closeMenu(false);
        if (movingPos >= 0) finishMove();
        focusTile(0);
    }

    /**
     * Home went behind another app since it was last resumed. A Home press that brings it back
     * arrives before onResume, so this tells it apart from a Home press on Home itself.
     */
    private boolean hiddenSinceResume;

    @Override
    protected void onStop() {
        hiddenSinceResume = true;
        updateHomeTabIndicator();
        handler.removeCallbacks(cacheClean);
        handler.removeCallbacks(nowPlayingPoll);
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WifiSettingsDialog.onPermissionResult(requestCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityResumed = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(packageReceiver, packageFilter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(packageReceiver, packageFilter);
        }
        tilesReady = false;
        promptsPaused = false;
        resuming = true;
        reloadApps(true);
        updateShamsiDate();
        updatePrayerTimes();
        PrayerTimes.syncIfDue(this, this::updatePrayerTimes);
        updateDateAndPeriod();
        updateWifiLabel();
        ModernSettingsDialog.onHostResumed(this);
        updateWeatherAqi();
        WeatherAirQuality.syncIfDue(this, this::updateWeatherAqi);
        // The atmospheric wallpaper overlay has its own location-scoped cache and cadence.
        // Refresh it on Home entry/resume; the shared G3B pill/dashboard cache remains untouched.
        updateHomeWeather();
        updateIslamicShelf();
        updateInputsButton();
        updateTopBarUserIcon();
        updateHeaderTabLabels();
        updateLocationPill();
        updateHomeTabIndicator();
        Location.Zone chosenPlace = Location.chosen(this);
        String zoneCode = (chosenPlace != null) ? chosenPlace.code : "WLY01";
        Azan.schedule(this, zoneCode);
        Azan.fetch(this);
        NowPlayingManager.updateCurrentMedia(this);
        startWallpaperRotation();
        handler.postDelayed(minuteTick, MINUTE_MS);
        handler.removeCallbacks(nowPlayingPoll);
        handler.postDelayed(nowPlayingPoll, 2000);
        handler.removeCallbacks(cacheClean);
        handler.postDelayed(cacheClean, CacheCleaner.IDLE_MS);
        if (weatherOverlayView != null && weatherOverlayView.getVisibility() == View.VISIBLE) {
            weatherOverlayView.startAnimation();
        }
        hiddenSinceResume = false;
        updateHomeTabIndicator();
        WifiSettingsDialog.onHostResumed();
    }

    /** Rebuilds the tiles (and the module cards), then puts focus back where it was. */
    private void reloadApps(boolean animate) {
        // Capture the app before rebinding can move focus and overwrite its key.
        final String returnKey = lastFocusedKey;
        final int returnSlot = lastFocused;
        final boolean restoreFocus = animate || grid.hasFocus() || (dockRecycler != null && dockRecycler.hasFocus());
        long started = android.os.SystemClock.uptimeMillis();
        boolean rebuilt = reloadTiles();
        // Nothing installed or removed: Home comes back exactly as it was left, with no rebuild and
        // no shelf, glow or selection fading back in.
        if (restoreFocus && rebuilt) {
            glassShown = false;
            glider.hideNow();
            heroShown = false;
            heroShelf.setAlpha(0f);
            ambientShown = false;
            ambient.setAlpha(0f);
            syncGlassGlow();
        }
        android.util.Log.i("TvHome.Perf", "tiles " + (rebuilt ? "rebuilt" : "unchanged") + " in "
                + (android.os.SystemClock.uptimeMillis() - started) + " ms");
        // The tiles rise into place on the first launch only. Replayed on every return from an app,
        // it showed the glass tray empty for a moment and held Home back while the tiles arrived.
        if (animate && !reducedMotion && !tilesAnimatedOnce) grid.scheduleLayoutAnimation();
        if (animate) tilesAnimatedOnce = true;

        if (restoreFocus) {
            // Follow the same app, or the nearest remaining slot when it was removed.
            restoreFocus();
        } else {
            // A package change must not steal focus from a top button or the Inputs popup.
            int dockPos = dockAdapter == null ? -1 : dockAdapter.positionOf(returnKey);
            int pos = adapter.positionOf(returnKey);
            if (dockPos >= 0) {
                lastFocusedDock = true;
                lastFocused = dockPos;
                lastFocusedKey = dockAdapter.keyAt(dockPos);
                lastDockFocusedKey = lastFocusedKey;
            } else {
                int itemCount = adapter.getItemCount();
                int resolvedPosition = NavigationState.resolveHomePosition(pos, returnSlot, itemCount);
                lastFocusedDock = false;
                lastFocused = itemCount > 0 ? resolvedPosition : 0;
                lastFocusedKey = itemCount > 0 ? NavigationState.normalizeFocusKey(adapter.keyAt(lastFocused)) : null;
            }
            android.content.SharedPreferences.Editor savedFocus =
                    getSharedPreferences(NavigationState.PREFS, MODE_PRIVATE).edit()
                            .putInt(NavigationState.HOME_FOCUS_POSITION, lastFocused)
                            .putString(HOME_FOCUS_REGION, lastFocusedDock ? "dock" : "grid");
            if (lastFocusedKey != null) savedFocus.putString(NavigationState.HOME_FOCUS_KEY, lastFocusedKey);
            else savedFocus.remove(NavigationState.HOME_FOCUS_KEY);
            savedFocus.apply();
            if (pos < 0 && dockPos < 0) hideHero();
            // A newly installed app may have brought a card with it.
            tilesReady = true;
            showCardPrompt();
        }
    }

    /**
     * Rebuilds the tiles; the module cards the owner chose to show stand in for their app's tile.
     * Every step into the card code runs under CardFeature: if it fails, the tiles are the ordinary
     * ones and cards stay off for this process.
     */
    boolean reloadTiles() {
        updateTopBarUserIcon();
        if (adapter != null) adapter.invalidateCache();
        // Read these before reload(): rebinding can move focus to the first tile and overwrite them.
        final String returnKey = lastFocusedKey;
        final int returnSlot = lastFocused;
        final boolean returnFocusWasTopPill = lastFocusWasTopPill;
        final String returnTopPillKey = lastFocusedTopPillKey;
        final List<Cards.Card> none = Collections.emptyList();
        cards = moduleCards ? CardFeature.run(() -> Cards.discover(this, hiddenApps, Cards.load(Cards.storeFile(this))), none) : none;
        boolean rebuilt = adapter.reload(cards.isEmpty() ? none : CardFeature.run(() -> Cards.shown(cards), none));
        if (dockAdapter != null) dockAdapter.refresh();
        applyDockLayout();
        // RecyclerView rebinding may transiently move focus and fire onTileFocused(). Preserve
        // the user's top-bar destination across that internal rebuild; it is not a user move.
        int dockPosition = dockAdapter == null ? -1 : dockAdapter.positionOf(returnKey);
        int foundPosition = adapter.positionOf(returnKey);
        if (dockPosition >= 0) {
            lastFocusedDock = true;
            lastFocused = dockPosition;
            lastFocusedKey = dockAdapter.keyAt(dockPosition);
            lastDockFocusedKey = lastFocusedKey;
        } else {
            int itemCount = adapter.getItemCount();
            int safePosition = itemCount > 0
                    ? NavigationState.resolveHomePosition(foundPosition, returnSlot, itemCount) : 0;
            lastFocusedDock = false;
            lastFocused = safePosition;
            lastFocusedKey = itemCount > 0 ? NavigationState.normalizeFocusKey(adapter.keyAt(safePosition)) : null;
        }
        lastFocusWasTopPill = returnFocusWasTopPill
                && NavigationState.normalizeTopPillKey(returnTopPillKey) != null;
        lastFocusedTopPillKey = lastFocusWasTopPill ? returnTopPillKey : null;

        // Restore the complete persisted snapshot after RecyclerView's transient rebind focus events.
        android.content.SharedPreferences.Editor savedNavigation =
                getSharedPreferences(NavigationState.PREFS, MODE_PRIVATE).edit()
                        .putInt(NavigationState.HOME_FOCUS_POSITION, lastFocused)
                        .putString(HOME_FOCUS_REGION, lastFocusedDock ? "dock" : "grid")
                        .putBoolean(NavigationState.HOME_FOCUS_TOP_PILL, lastFocusWasTopPill);
        if (lastFocusedKey != null) savedNavigation.putString(NavigationState.HOME_FOCUS_KEY, lastFocusedKey);
        else savedNavigation.remove(NavigationState.HOME_FOCUS_KEY);
        if (lastFocusedTopPillKey != null) {
            savedNavigation.putString(NavigationState.HOME_FOCUS_TOP_PILL_KEY, lastFocusedTopPillKey);
        } else {
            savedNavigation.remove(NavigationState.HOME_FOCUS_TOP_PILL_KEY);
        }
        savedNavigation.apply();
        return rebuilt;
    }

    /**
     * Return to the app we left from, or the same slot if that app is gone. Always refocus: after
     * reload() the view that holds focus may no longer show the app it had.
     */
    private void restoreFocus() {
        final String returnKey = lastFocusedKey;
        final int returnSlot = lastFocused;
        final boolean returnFocusWasTopPill = lastFocusWasTopPill;
        final String returnTopPillKey = lastFocusedTopPillKey;
        // A user's newer DPAD destination must win over this queued resume restoration.
        final int restoreGeneration = gridFocusRequestGeneration;
        grid.post(() -> {
            boolean supersededByNewFocus = restoreGeneration != gridFocusRequestGeneration;
            if (!supersededByNewFocus) {
                int dockPos = dockAdapter == null ? -1 : dockAdapter.positionOf(returnKey);
                int pos = adapter.positionOf(returnKey);
                boolean restoredTopPill = false;
                if (returnFocusWasTopPill) {
                    View pill = findTopPill(returnTopPillKey);
                    restoredTopPill = pill != null && pill.getVisibility() == View.VISIBLE
                            && pill.isFocusable() && pill.requestFocus();
                    if (!restoredTopPill) {
                        lastFocusWasTopPill = false;
                        lastFocusedTopPillKey = null;
                        getSharedPreferences(NavigationState.PREFS, MODE_PRIVATE).edit()
                                .putBoolean(NavigationState.HOME_FOCUS_TOP_PILL, false)
                                .remove(NavigationState.HOME_FOCUS_TOP_PILL_KEY).apply();
                    }
                }
                if (!restoredTopPill) {
                    if (dockPos >= 0) {
                        lastFocusedDock = true;
                        lastDockFocusedKey = returnKey;
                        focusDockTile(dockPos);
                    } else {
                        lastFocusedDock = false;
                        focusTile(pos >= 0 ? pos : returnSlot);
                    }
                }
            }
            // Complete resume bookkeeping even when a newer focus destination superseded restoration.
            tilesReady = true;
            resuming = false;
            View now = getCurrentFocus();
            if (now != null && isTopPill(now)) moveTopHighlight(now);
            showCardPrompt();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Invalidate focus work queued before Home yielded the window to another Activity.
        gridFocusRequestGeneration++;
        cancelHorizontalGridRowTransition();
        activityResumed = false;
        updateHomeTabIndicator();
        unregisterReceiver(packageReceiver);
        handler.removeCallbacks(minuteTick);
        handler.removeCallbacks(wallTick);
        handler.removeCallbacks(nowPlayingPoll);
        if (weatherOverlayView != null) {
            weatherOverlayView.stopAnimation();
        }
        // Nothing of ours stays on screen over another app; an undecided prompt simply returns next time.
        if (cardPrompt != null) cardPrompt.dismiss();
        if (openPopup != null) openPopup.dismiss();
        closeMenu(false);
        finishMove();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        updateHomeTabIndicator();
        if (hasFocus) showCardPrompt();
    }

    /**
     * Android is short of memory while Home sits behind another app: give back what can be remade,
     * so the launcher is less likely to be reclaimed and Home comes back without starting over.
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level >= TRIM_MEMORY_BACKGROUND) Backdrop.trim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WifiSettingsDialog.onHostDestroyed();
        ModernSettingsDialog.onHostDestroyed(this);
        if (sInstance != null && sInstance.get() == this) {
            sInstance = null;
        }
        if (uploadServer != null) {
            uploadServer.stop();
            uploadServer = null;
        }
        if (avatarUploadServer != null) {
            avatarUploadServer.stop();
            avatarUploadServer = null;
        }
        if (nowPlayingListener != null) {
            NowPlayingManager.unregisterListener(nowPlayingListener);
        }
        SoundEffects.release();
        wallpaperRequestGeneration.incrementAndGet();
        wallpaperThumbnailGeneration.incrementAndGet();
        handler.removeCallbacksAndMessages(null);
        wallDecoder.shutdownNow();
        ConnectivityManager cm = getSystemService(ConnectivityManager.class);
        TvInputManager tim = (TvInputManager) getSystemService(TV_INPUT_SERVICE);
        if (tim != null) tim.unregisterCallback(inputCallback);
        if (cm != null) {
            cm.unregisterNetworkCallback(networkCallback);
            cm.unregisterNetworkCallback(vpnCallback);
        }
    }

    public void refreshLocationDependentData() {
        updateLocationPill();
        updateWeatherAqi();
        updatePrayerTimes();
        WeatherAirQuality.sync(this, this::updateWeatherAqi);
        PrayerTimes.sync(this, this::updatePrayerTimes);
    }

    @Override
    public void onBackPressed() {
        handleBackPress();
    }

    private void handleBackPress() {
        if (openMenu != null) {
            closeMenu(true);
            return;
        }
        if (screensaverActive) {
            dismissScreensaver();
            return;
        }
        if (wallpaperFullscreen) {
            setWallpaperFullscreen(false);
            return;
        }

        if (doubleBackPending) {
            if (singleBackRunnable != null) {
                handler.removeCallbacks(singleBackRunnable);
                singleBackRunnable = null;
            }
            doubleBackPending = false;
            showNightstandScreensaver();
        } else {
            doubleBackPending = true;
            singleBackRunnable = () -> {
                doubleBackPending = false;
                singleBackRunnable = null;
                setWallpaperFullscreen(true);
            };
            handler.postDelayed(singleBackRunnable, DOUBLE_BACK_DELAY_MS);
        }
    }

    private void setWallpaperFullscreen(boolean fullscreen) {
        if (wallpaperFullscreen == fullscreen) return;
        wallpaperFullscreen = fullscreen;
        float alpha = fullscreen ? 0f : 1f;
        int duration = reducedMotion ? 120 : 300;

        if (mainContent != null) {
            mainContent.animate().alpha(alpha).setDuration(duration).start();
        }
        if (heroShelf != null) {
            heroShelf.animate().alpha(alpha).setDuration(duration).start();
        }
        if (islamicShelf != null) {
            islamicShelf.animate().alpha(alpha).setDuration(duration).start();
        }
        if (glass != null) {
            glass.animate().alpha(alpha).setDuration(duration).start();
        }
        if (glider != null) {
            glider.animate().alpha(alpha).setDuration(duration).start();
        }
        if (ambient != null && fullscreen) {
            ambient.animate().alpha(0f).setDuration(duration).start();
        }

        if (wallView != null && placesMode) {
            if (fullscreen) {
                wallView.clearColorFilter();
            } else {
                wallView.setColorFilter(0xFF7A7A7A, android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        }
        updateHomeTabIndicator();
    }

    // ---- glass highlight -------------------------------------------------------------------------

    /** Reused by every focus move. */
    private final Rect tileRect = new Rect(), showRect = new Rect();

    @Override
    public void onTileFocused(int position, View card) {
        if (adapter == null || card == null || position < 0 || position >= adapter.getItemCount()) return;
        handleTileFocused(adapter.keyAt(position), position, card, false);
    }

    @Override
    public void onDockTileFocused(String key, View card) {
        if (dockAdapter == null || card == null) return;
        int position = dockAdapter.positionOf(key);
        if (position < 0) return;
        // Dock is a newer, authoritative focus destination; cancel every queued Grid request.
        gridFocusRequestGeneration++;
        if (!dockVertical) cancelHorizontalGridRowTransition();
        // Horizontal mode mirrors Patch 4.1: the Dock is the resting Home surface.
        // Collapse the overflow Grid only after focus has actually reached a Dock tile.
        handleTileFocused(key, position, card, true);
    }

    private void handleTileFocused(String key, int position, View card, boolean inDock) {
        if (key == null || card == null) return;
        if (!resuming && hasWindowFocus()) SoundEffects.playNav();
        lastFocused = position;
        lastFocusedDock = inDock;
        if (inDock) lastDockFocusedKey = NavigationState.normalizeFocusKey(key);
        lastFocusedKey = NavigationState.normalizeFocusKey(key);
        android.content.SharedPreferences.Editor navigationEdit =
                getSharedPreferences(NavigationState.PREFS, MODE_PRIVATE).edit()
                        .putInt(NavigationState.HOME_FOCUS_POSITION, lastFocused)
                        .putString(HOME_FOCUS_REGION, inDock ? "dock" : "grid");
        if (lastFocusedKey != null) navigationEdit.putString(NavigationState.HOME_FOCUS_KEY, lastFocusedKey);
        else navigationEdit.remove(NavigationState.HOME_FOCUS_KEY);
        navigationEdit.putBoolean(NavigationState.HOME_FOCUS_TOP_PILL, false)
                .remove(NavigationState.HOME_FOCUS_TOP_PILL_KEY).apply();
        if (inDock && lastDockFocusedKey != null) {
            getSharedPreferences(NavigationState.PREFS, MODE_PRIVATE).edit()
                    .putString(HOME_DOCK_FOCUS_KEY, lastDockFocusedKey).apply();
        }
        lastFocusWasTopPill = false;
        lastFocusedTopPillKey = null;

        Rect r = tileRect;
        r.set(0, 0, card.getWidth(), card.getHeight());
        root.offsetDescendantRectToMyCoords(card, r);
        float pad = 6f * density;
        float focusScale = isIconAnimationsEnabled() ? AppAdapter.FOCUS_SCALE : 1f;
        float w = card.getWidth() * focusScale + 2 * pad;
        float h = card.getHeight() * focusScale + 2 * pad;
        float x = r.exactCenterX() - w / 2f;
        float y = r.exactCenterY() - h / 2f;

        ViewGroup.LayoutParams lp = glass.getLayoutParams();
        if (lp.width != (int) w || lp.height != (int) h) {
            lp.width = (int) w;
            lp.height = (int) h;
            glass.setLayoutParams(lp);
            glass.setRadius(AppAdapter.cornerRadius(card.getWidth(), density) * focusScale);
        }
        glassTargetX = x;
        glassTargetY = y;
        float cardRadius = AppAdapter.cornerRadius(card.getWidth(), density) * focusScale;
        if (gliderOnPill) {
            glider.moveTo(x + pad, y + pad, x + w - pad, y + h - pad, cardRadius);
            glider.onArrive(() -> {
                if (!gliderOnPill) glider.setStyle(isGlassEffectsEnabled() ? Glider.GLASS : Glider.DARK);
            });
        } else {
            glider.setStyle(isGlassEffectsEnabled() ? Glider.GLASS : Glider.DARK);
            glider.moveTo(x + pad, y + pad, x + w - pad, y + h - pad, cardRadius);
        }
        gliderOnPill = false;
        glassShown = true;

        if (inDock) {
            showHero(position);
            showAmbient(dockAdapter.ambientColorAt(position));
        } else {
            hideShelf();
            showAmbient(adapter.ambientColorAt(position));
            // Horizontal Dock mode owns a fixed one-row viewport. Revealing the enlarged
            // focus rectangle can scroll the active row away from the glass baseline.
            // Row placement is controlled explicitly by focusTile() in this mode.
            if (dockVertical) {
                View item = (View) card.getParent();
                showRect.set(0, -(int) pad * 2, item.getWidth(), item.getHeight() + (int) pad);
                grid.requestChildRectangleOnScreen(item, showRect, reducedMotion);
            }
        }
    }

    @Override
    public boolean onDockTileKey(String key, int keyCode) {
        if (dockAdapter == null) return false;
        int position = dockAdapter.positionOf(key);
        if (position < 0) return false;
        int count = dockAdapter.getItemCount();
        if (dockVertical) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                if (position > 0) focusDockTile(position - 1);
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                if (position < count - 1) focusDockTile(position + 1);
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                int target = DockLayoutModel.gridPositionForDock(
                        position, true, COLUMNS, adapter == null ? 0 : adapter.getItemCount());
                focusTile(target);
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                focusTopBarFallback();
                return true;
            }
        } else {
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (position > 0) focusDockTile(position - 1);
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (position < count - 1) focusDockTile(position + 1);
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                // The first Dock-to-overflow reveal owns the vertical axis until its handoff settles.
                if (horizontalDockExpansionInProgress) return true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                // The Dock is the first Home row in the Patch 4.1 navigation model:
                // Up returns to the top shelf, never to an app row in the middle.
                focusTopBarFallback();
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                // Reveal the overflow inventory only on demand. It remains part of the
                // same source inventory and is reachable without cluttering the idle Home.
                // The first overflow row sits directly below the Dock. Later rows replace it
                // through the one-row RecyclerView viewport as the user presses DPAD Down.
                int target = DockLayoutModel.gridPositionForDock(
                        position, false, COLUMNS, adapter == null ? 0 : adapter.getItemCount());
                if (target >= 0) focusTile(target);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onDockTileHeld(String key, View card) {
        return showTileContextMenu(key, card, true);
    }

    // ---- top shelf -------------------------------------------------------------------------------

    private boolean isCustomWallpaper() {
        String activeCat = getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getString("active_category", "Natural");
        if ("Custom".equalsIgnoreCase(activeCat)) return true;
        if (wallpapers != null && !wallpapers.isEmpty()) {
            int idx = getSharedPreferences("wallpaper", MODE_PRIVATE).getInt("index", 0);
            if (idx >= 0 && idx < wallpapers.size()) {
                File f = wallpapers.get(idx);
                if (f != null && f.getAbsolutePath().contains("custom_wallpapers")) return true;
            }
        }
        return false;
    }

    private void updateHomeShelfAnchor() {
        if (root == null || gridWrap == null || gridWrap.getHeight() <= 0) return;

        if (dockVertical) {
            // Vertical Dock has no horizontal tray edge below the information package.
            // Preserve its established virtual baseline; the 27.5dp contract is for the
            // horizontal Glass Dock used by the Mi TV Stick Home profile.
            root.offsetDescendantRectToMyCoords(gridWrap, shelfAnchorRect);
            DockPreferences preferences = DockPreferences.load(this);
            int innerWidth = Math.max(0, gridWrap.getWidth()
                    - gridWrap.getPaddingLeft() - gridWrap.getPaddingRight());
            int tileWidth = DockGeometry.horizontalTileWidthPx(innerWidth, preferences.capacity, density);
            int tileHeight = DockGeometry.tileHeightPx(tileWidth);
            int shelfAnchorHeight = DockGeometry.horizontalShelfAnchorHeightPx(tileHeight, density);
            float shelfDockTop = shelfAnchorRect.bottom - shelfAnchorHeight
                    + DockGeometry.dockItemTopOffsetPx(density);
            int shelfGapPx = DockGeometry.shelfBottomOffsetFromTileTopPx(density);
            int baselineOffsetPx = Math.round(HOME_SHELF_BASELINE_OFFSET_DP * density);
            int minimumGapPx = Math.round(MIN_HOME_SHELF_TRAY_GAP_DP * density);
            int safeOffsetPx = Math.min(baselineOffsetPx, Math.max(0, shelfGapPx - minimumGapPx));
            heroBottom = shelfDockTop - shelfGapPx + safeOffsetPx;
            islamicShelfBottomAnchor = heroBottom
                    + Math.round(ISLAMIC_SHELF_BOTTOM_OFFSET_DP * density);
        } else {
            // Use the DockDecoration's real visual tray edge, derived from the laid-out card
            // bounds (not a focused tile, guessed host height, or an independent margin).
            int trayTop = horizontalDockTrayTopInRoot();
            if (trayTop == Integer.MIN_VALUE) {
                homeShelfAnchorReady = false;
                if (dockRecycler != null) {
                    dockRecycler.postOnAnimation(() -> {
                        if (heroShown && !homeShelfAnchorReady) updateHomeShelfAnchor();
                    });
                }
                return;
            }
            // Anchor both visible information bottoms to the measured Glass tray edge.
            // prayerTable is translated upward in XML; compensate that runtime translation
            // so its visible bottom, not the container bottom, receives the exact 30dp gap.
            int shelfGapPx = DockGeometry.islamicShelfDockGapPx(density);
            heroBottom = trayTop - shelfGapPx;
            islamicShelfBottomAnchor = heroBottom + islamicShelfVisualBottomOffsetPx();
        }

        homeShelfAnchorReady = true;
        applyIslamicShelfPosition();
        if (heroShown && heroShelf != null) heroShelf.setY(heroBottom - heroShelf.getHeight());
    }

    /**
     * Returns the top of the actual horizontal Glass tray in root coordinates.
     * DockDecoration draws the tray from the minimum laid-out card top minus its
     * 14dp inset, so this measures the same visual boundary instead of a layout margin.
     */
    private int horizontalDockTrayTopInRoot() {
        if (root == null || dockRecycler == null || dockVertical || dockRecycler.getWidth() <= 0) {
            return Integer.MIN_VALUE;
        }
        int firstCardTop = Integer.MAX_VALUE;
        for (int i = 0; i < dockRecycler.getChildCount(); i++) {
            View item = dockRecycler.getChildAt(i);
            View card = item.findViewById(R.id.card);
            if (card == null || card.getHeight() <= 0) continue;
            Rect cardRect = new Rect(card.getLeft(), card.getTop(), card.getRight(), card.getBottom());
            root.offsetDescendantRectToMyCoords(item, cardRect);
            firstCardTop = Math.min(firstCardTop, cardRect.top);
        }
        if (firstCardTop == Integer.MAX_VALUE && tray != null && !tray.area.isEmpty()) {
            Rect hostRect = new Rect(0, 0, dockRecycler.getWidth(), dockRecycler.getHeight());
            root.offsetDescendantRectToMyCoords(dockRecycler, hostRect);
            firstCardTop = hostRect.top + Math.round(tray.area.top)
                    + Math.round(DockGeometry.trayPaddingPx(density));
        }
        if (firstCardTop == Integer.MAX_VALUE) return Integer.MIN_VALUE;
        return firstCardTop - Math.round(DockGeometry.trayPaddingPx(density));
    }

    private int islamicShelfVisualBottomOffsetPx() {
        View prayerTable = findViewById(R.id.prayerTable);
        if (prayerTable == null || prayerTable.getVisibility() != View.VISIBLE) return 0;
        return Math.max(0, -Math.round(prayerTable.getTranslationY()));
    }

    private void applyIslamicShelfPosition() {
        if (islamicShelf == null || Float.isNaN(islamicShelfBottomAnchor)) return;
        islamicShelf.setY(islamicShelfBottomAnchor - islamicShelf.getHeight());
    }

    private void showHero(int position) {
        int color = dockAdapter.ambientColorAt(position);
        if (!homeShelfAnchorReady) updateHomeShelfAnchor();

        if (islamicShelf != null) {
            applyIslamicShelfPosition();
            islamicShelf.animate().cancel();
            if (!heroShown) {
                if (firstFrameHeld || resuming) islamicShelf.setAlpha(1f);
                else islamicShelf.animate().alpha(1f).setDuration(260).start();
            } else {
                islamicShelf.setAlpha(1f);
            }
        }

        // When using custom wallpaper or when description is turned off, disable the description part
        if (isCustomWallpaper() || !isWallpaperDescEnabled()) {
            if (heroShelf != null) {
                heroShelf.animate().cancel();
                heroShelf.setAlpha(0f);
                heroShelf.setVisibility(View.GONE);
            }
            showAmbient(color);
            heroShown = true;
            return;
        }

        if (heroShelf != null) {
            heroShelf.setVisibility(View.VISIBLE);
        }

        // The place the picture is of, when there is one; the app's own name is the fallback for
        // the user's own wallpapers, which carry no title.
        final String label = placeName != null ? placeName : dockAdapter.labelAt(position);
        final String desc = placeName != null ? placeText : "";
        final String where = placeName != null ? placeCountry : "";
        float y = heroBottom - heroShelf.getHeight();
        boolean changed = !label.contentEquals(heroName.getText());

        if (reducedMotion) {
            // Snap the shelf instead of sliding and fading it: each of those frames repaints the
            // full-screen panel, and this switch exists for the GPUs that cannot afford it.
            heroShelf.animate().cancel();
            setHeroText(label, where, desc);
            heroShelf.setY(y);
            heroShelf.setTranslationX(0f);
            heroShelf.setAlpha(1f);
            showAmbient(color);
            heroShown = true;
            return;
        }

        if (!heroShown) {
            setHeroText(label, where, desc);
            heroShelf.setY(y);
            heroShelf.setTranslationX(0f);
            // Coming back to Home, the words are simply there with the picture: faded in after the
            // first frame, they arrived a beat behind everything else.
            heroShelf.animate().cancel();
            if (firstFrameHeld || resuming) heroShelf.setAlpha(1f);
            else heroShelf.animate().alpha(1f).setDuration(260).start();
            showAmbient(color);
            heroShown = true;
        } else if (changed) {
            heroShelf.animate().alpha(0f).translationX(-12 * density).setDuration(120).withEndAction(() -> {
                setHeroText(label, where, desc);
                heroShelf.setY(y);
                heroShelf.setTranslationX(12 * density);
                heroShelf.animate().alpha(1f).translationX(0f).setDuration(200).start();
            }).start();
            showAmbient(color);
        } else {
            // Same words, different app: every dock app shares the place's name, so the colour has
            // to follow focus on its own rather than ride along with a change of text.
            heroShelf.setY(y);
            showAmbient(color);
        }
    }



    /**
     * The shared Glider remains a single geometry/animation owner. Header capsule backgrounds
     * composite that same shape above their glass and below their text; the Dock keeps its own
     * established companion drawing path.
     */
    private void setUpGlider() {
        glider = new Glider(this, reducedMotion);
        // Behind the shelf's text, the pills and the tiles, so it never draws over any of them.
        root.addView(glider, root.indexOfChild(heroShelf), new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // The glider replaces the glass selection outright.
        glass.setVisibility(View.GONE);
    }

    private String topPillKeyForView(View view) {
        if (view == null) return null;
        int id = view.getId();
        if (id == R.id.locationPill) return "location";
        if (id == R.id.weatherAirQuality) return "weather";
        if (id == R.id.btnHome) return "home";
        if (id == R.id.btnInputs) return "inputs";
        if (id == R.id.btnWifi) return "wifi";
        if (id == R.id.btnAssistant) return "assistant";
        if (id == R.id.btnCandyBox) return "candy_box";
        if (id == R.id.btnSettings) return "settings";
        return null;
    }

    private View findTopPill(String key) {
        String normalized = NavigationState.normalizeTopPillKey(key);
        if (normalized == null) return null;
        int id;
        switch (normalized) {
            case "location": id = R.id.locationPill; break;
            case "weather": id = R.id.weatherAirQuality; break;
            case "home": id = R.id.btnHome; break;
            case "inputs": id = R.id.btnInputs; break;
            case "wifi": id = R.id.btnWifi; break;
            case "assistant": id = R.id.btnAssistant; break;
            case "candy_box": id = R.id.btnCandyBox; break;
            case "settings": id = R.id.btnSettings; break;
            default: return null;
        }
        return findViewById(id);
    }

    private boolean isTopPill(View v) {
        int id = v.getId();
        return id == R.id.locationPill || id == R.id.weatherAirQuality || id == R.id.btnHome
                || id == R.id.btnInputs || id == R.id.btnWifi || id == R.id.btnAssistant
                || id == R.id.btnCandyBox || id == R.id.btnSettings;
    }

    /** Sends the focus shape onto a pill: from a tile it glides up and narrows into the pill. */
    private void moveTopHighlight(View pill) {
        if (resuming) return;
        SoundEffects.playNav();
        pill.getLocationInWindow(pillAt);
        glider.getLocationInWindow(rootAt);
        float l = pillAt[0] - rootAt[0], t = pillAt[1] - rootAt[1];
        // The pills do not scroll with the grid, so the shape stops following the grid's scroll.
        glassShown = false;
        // Up to the top menu it is dark at once, and travels that way.
        gliderOnPill = true;
        glider.setStyle(Glider.DARK);
        glider.moveTo(l, t, l + pill.getWidth(), t + pill.getHeight(), Math.min(20f * density, pill.getHeight() / 2f));
    }

    /** The shelf's two lines. An empty description takes no room, so the title keeps its place. */
    private void setHeroText(String title, String where, String desc) {
        if (heroName == null || heroWhere == null || heroDesc == null) return;
        heroName.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, titleSizeSp(title));
        heroName.setText(title);
        heroWhere.setText(where);
        heroWhere.setVisibility(where == null || where.isEmpty() ? View.GONE : View.VISIBLE);
        // The description sits further down under a country line; with none, close under the name.
        ViewGroup.MarginLayoutParams descLp = (ViewGroup.MarginLayoutParams) heroDesc.getLayoutParams();
        if (descLp != null) {
            int top = Math.round(((where == null || where.isEmpty()) ? 6f : 9.5f) * density);
            if (descLp.topMargin != top) {
                descLp.topMargin = top;
                heroDesc.setLayoutParams(descLp);
            }
        }
        heroDesc.setText(desc);
        heroDesc.setVisibility(desc == null || desc.isEmpty() ? View.GONE : View.VISIBLE);
    }

    /**
     * Shouting titles are calmed down: "THE RIBBON HERO" becomes "The Ribbon Hero". A title that
     * already has lower case is left exactly as its studio wrote it, so "LEGO Star Wars" and
     * "WALL·E" keep their capitals instead of being flattened to "Lego" and "Wall·e".
     */
    private static String titleCase(String title) {
        if (title == null || title.isEmpty()) return title;
        for (int i = 0; i < title.length(); i++) {
            if (Character.isLowerCase(title.charAt(i))) return title;
        }
        StringBuilder out = new StringBuilder(title.length());
        boolean start = true;
        for (int i = 0; i < title.length(); i++) {
            char c = title.charAt(i);
            out.append(start ? Character.toUpperCase(c) : Character.toLowerCase(c));
            // A new word begins after a space or any of the marks a title tends to use.
            start = c == ' ' || c == '-' || c == ':' || c == '.' || c == '\'' || c == '(' || c == '/';
        }
        return out.toString();
    }

    /**
     * The largest size at which the name still fits on one line, up to its share of the width.
     * Names run longer than app names ("Wroclaw Market Square"), and a name cut short tells the
     * viewer nothing, so a long one is set smaller rather than clipped.
     */
    private float titleSizeSp(String title) {
        if (title == null || title.isEmpty() || heroNameMaxWidth <= 0) return HERO_TITLE_MAX_SP;
        android.text.TextPaint paint = new android.text.TextPaint(heroName.getPaint());
        float scaled = getResources().getDisplayMetrics().scaledDensity;
        for (float sp = HERO_TITLE_MAX_SP; sp > HERO_TITLE_MIN_SP; sp -= 2f) {
            paint.setTextSize(sp * scaled);
            if (paint.measureText(title) <= heroNameMaxWidth) return sp;
        }
        return HERO_TITLE_MIN_SP;
    }

    /**
     * Follows the rotation: the shelf names whichever place is on screen. Called after the picture
     * changes, so the words and the picture never disagree.
     */
    private void setPlace(File picture) {
        if ((picture != null && picture.getAbsolutePath().contains("custom_wallpapers")) || !isWallpaperDescEnabled()) {
            placeName = null;
            placeText = "";
            placeCountry = "";
            if (heroShelf != null) {
                heroShelf.animate().cancel();
                heroShelf.animate().alpha(0f).setDuration(reducedMotion ? 100 : 250).withEndAction(() -> {
                    if (heroShelf != null) heroShelf.setVisibility(View.GONE);
                }).start();
            }
            return;
        }
        if (heroShelf != null && isWallpaperDescEnabled()) {
            heroShelf.setVisibility(View.VISIBLE);
        }
        String[] about = WallpaperMetadata.describe(picture);
        if (about == null) {
            about = placesMode ? PlaceWallpapers.describe(picture) : OnlineWallpapers.describe(picture);
        }
        String title = about != null ? titleCase(about[0]) : null;
        String overview = about != null ? about[1] : "";
        String country = about != null && about.length > 2 ? about[2] : "";
        if (country != null) {
            country = country.replaceAll("(?i)\\bunsplash\\s*•?\\s*", "").trim();

        }
        if (title != null) {
            title = title.replaceAll("(?i)\\bunsplash\\s*•?\\s*", "").trim();
        }
        if (title == null ? placeName == null : title.equals(placeName)) return;
        placeName = title;
        placeText = overview;
        placeCountry = country;
        // When the shelf is up, the words fade out with the old picture and back in with the new
        // one, inside the picture's own cross-fade: swapped at once, the next place's name sat over
        // the last one's picture while it faded. The layout listener puts the shelf back where it
        // belongs once the new lines have been measured.
        if (heroShown && title != null) {
            final String t = title;
            final String c = country;
            final String o = overview;
            heroShelf.animate().cancel();
            heroShelf.animate().alpha(0f).setDuration(reducedMotion ? 100 : 250).withEndAction(() -> {
                setHeroText(t, c, o);
                if (heroShown) heroShelf.animate().alpha(1f).setDuration(reducedMotion ? 120 : 400).start();
            }).start();
        }
    }

    /**
     * The glow used to be a full-screen radial GradientDrawable. On Android 10 that drawable is
     * drawn on the CPU, so fading it for half a second redrew 1920×1080 every frame and the
     * highlight looked like it was running at a few frames per second. A small bitmap stretched
     * over the screen is one texture; changing apps only uploads it once.
     */
    private void setAmbient(int color) {
        setAmbient(color, false);
    }

    private FadeTopFrame gridWrap;

    /** Turns the top fade on while any row reaches into its band, with room for a focused tile's zoom. */
    private void updateTopFade() {
        if (gridWrap == null) return;
        float band = 56 * density + 24 * density;
        boolean inBand = false;
        for (int i = 0; i < grid.getChildCount() && !inBand; i++) {
            View child = grid.getChildAt(i);
            inBand = grid.getTop() + child.getTop() + child.getTranslationY() < band;
        }
        gridWrap.setFadeStrength(inBand ? 1f : 0f);
    }

    /** Applies independent geometry to the Dock and App Grid without changing the grid span. */
    private boolean parkDock() {
        if (gridWrap == null || grid == null || dockRecycler == null || dockAdapter == null
                || gridWrap.getWidth() <= 0 || gridWrap.getHeight() <= 0) return false;
        applyDockLayout();
        return true;
    }

    /** Applies the 30dp horizontal safe inset while preserving the 29dp top safe edge. */
    private void applyHomeShelfSafeMargins() {
        int inset = DockGeometry.safeHorizontalInsetPx(density);
        if (heroShelf != null && heroShelf.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) heroShelf.getLayoutParams();
            params.setMarginStart(inset);
            heroShelf.setLayoutParams(params);
        }
        if (islamicShelf != null && islamicShelf.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) islamicShelf.getLayoutParams();
            params.setMarginEnd(inset);
            params.topMargin = DockGeometry.safeTopInsetPx(density);
            islamicShelf.setLayoutParams(params);
        }
    }

    private void applyDockLayout() {
        if (gridWrap == null || grid == null || dockRecycler == null || dockAdapter == null) return;
        DockPreferences preferences = DockPreferences.load(this);
        dockVertical = preferences.orientation == DockPreferences.Orientation.VERTICAL;
        dockAdapter.setVertical(dockVertical);
        if (dockVertical) {
            horizontalGridExpanded = false;
            cancelHorizontalGridRowTransition();
        }
        // GridLayoutManager does not support stackFromEnd (it throws during onResume).
        // Keep the Grid's natural adapter order; horizontal overflow is positioned by the
        // surrounding FrameLayout geometry and is expanded only on DPAD Down.
        // In horizontal mode the Grid is an on-demand overflow surface: hidden at rest,
        // revealed by DPAD Down from the Dock, and collapsed again when Dock focus returns.
        int wantedGridVisibility = dockVertical || horizontalGridExpanded ? View.VISIBLE : View.GONE;
        boolean gridVisibilityChanged = grid.getVisibility() != wantedGridVisibility;
        grid.setVisibility(wantedGridVisibility);
        grid.setClipToPadding(!dockVertical && horizontalGridExpanded);
        int orientation = dockVertical
                ? android.widget.LinearLayout.VERTICAL : android.widget.LinearLayout.HORIZONTAL;
        RecyclerView.LayoutManager current = dockRecycler.getLayoutManager();
        if (!(current instanceof androidx.recyclerview.widget.LinearLayoutManager)
                || ((androidx.recyclerview.widget.LinearLayoutManager) current).getOrientation() != orientation) {
            dockRecycler.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this, orientation, false));
        }

        int dockCount = dockAdapter.getItemCount();
        if (tray == null) {
            tray = new DockDecoration(dockCount, density, dockVertical);
            tray.setGlider(glider);
            dockRecycler.addItemDecoration(tray);
            lastDockDecorationCount = dockCount;
            lastDockDecorationVertical = dockVertical;
        } else if (lastDockDecorationCount != dockCount || lastDockDecorationVertical != dockVertical) {
            tray.update(dockCount, dockVertical);
            lastDockDecorationCount = dockCount;
            lastDockDecorationVertical = dockVertical;
            dockRecycler.invalidateItemDecorations();
        }

        FrameLayout.LayoutParams gridParams = new FrameLayout.LayoutParams((FrameLayout.LayoutParams) grid.getLayoutParams());
        FrameLayout.LayoutParams dockParams = new FrameLayout.LayoutParams((FrameLayout.LayoutParams) dockRecycler.getLayoutParams());
        gridParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        gridParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        dockParams.gravity = dockVertical ? Gravity.START : Gravity.BOTTOM;
        boolean hasDock = dockCount > 0;
        int wantedDockVisibility = hasDock ? View.VISIBLE : View.GONE;
        boolean dockVisibilityChanged = dockRecycler.getVisibility() != wantedDockVisibility;
        dockRecycler.setVisibility(wantedDockVisibility);

        dockRecycler.setTranslationX(0f);
        if (dockVertical || !hasDock) {
            dockRecycler.setPadding(0, 0, 0, 0);
        }
        if (dockVertical) {
            gridParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            gridParams.gravity = Gravity.TOP | Gravity.START;
            int dockWidth = hasDock ? Math.round(202 * density) : 0;
            gridParams.leftMargin = hasDock ? dockWidth + Math.round(12 * density) : 0;
            gridParams.topMargin = 0;
            gridParams.rightMargin = 0;
            gridParams.bottomMargin = 0;
            dockParams.width = dockWidth;
            dockParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            dockParams.leftMargin = 0;
            dockParams.topMargin = 0;
            dockParams.rightMargin = 0;
            dockParams.bottomMargin = DockGeometry.safeBottomInsetPx(density);
            if (grid.getPaddingTop() != 0 || grid.getPaddingBottom() != gridBasePaddingBottom) {
                grid.setPadding(grid.getPaddingLeft(), 0, grid.getPaddingRight(), gridBasePaddingBottom);
            }
        } else {
            gridParams.leftMargin = 0;
            gridParams.topMargin = 0;
            gridParams.rightMargin = 0;
            gridParams.bottomMargin = 0;
            if (hasDock) {
                // The Dock host, clock/header and both Home shelves share the 30dp horizontal safe frame.
                // Do not extend the host or apply a second inset: its left and right edges must match.
                int dockViewportWidth = gridWrap.getWidth();
                int inner = DockGeometry.horizontalContentWidthPx(dockViewportWidth, density);
                gridParams.width = DockGeometry.horizontalGridWidthPx(inner, preferences.capacity);
                // Preserve the approved bottom-anchored Dock baseline at rest. When overflow
                // is active, both RecyclerViews share the same bottom coordinate system so the
                // Dock can slide upward while the first overflow row enters from below.
                gridParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                dockParams.gravity = Gravity.BOTTOM | Gravity.START;
                dockParams.width = dockViewportWidth;
                dockAdapter.setHorizontalViewportWidth(dockViewportWidth);
                dockParams.rightMargin = 0;
                // Grid card geometry remains unchanged; Dock uses its own proportional card scale.
                int gridTileWidth = DockGeometry.horizontalTileWidthPx(inner, preferences.capacity, density);
                int dockTileWidth = DockGeometry.horizontalDockTileWidthPx(inner, preferences.capacity, density);
                int dockTileHeight = DockGeometry.tileHeightPx(dockTileWidth);
                int dockHeight = DockGeometry.horizontalDockHeightPx(dockTileHeight, density);

                // RecyclerView's horizontal LinearLayoutManager starts children at the left edge.
                // Keep an even card gap while the glass tray itself remains full width.
                int dockSlotWidth = DockGeometry.horizontalDockSlotWidthPx(inner, preferences.capacity, density);
                int dockCenterOffset = DockGeometry.horizontalCenterOffsetPx(
                        inner, dockSlotWidth, Math.min(dockCount, preferences.capacity));
                // Center the icon group with symmetric content padding. The RecyclerView host
                // stays at the Home shelf origin so its full-width glass tray and aligned shelves
                // do not inherit the icon group's centering offset.
                dockRecycler.setPadding(dockCenterOffset, dockRecycler.getPaddingTop(),
                        dockCenterOffset, dockRecycler.getPaddingBottom());
                dockRecycler.setTranslationX(0f);

                // The Dock's idle geometry is the pre-#115 approved bottom anchor. On expansion,
                // reserve one measured Grid row below it and move the Dock upward by that exact
                // pitch. The Grid's bottom edge lands at the original Dock host bottom.
                int dockBottomMargin = DockGeometry.horizontalDockBottomMarginPx(dockTileHeight, density);
                int gridViewportHeight = DockGeometry.horizontalGridViewportHeightPx(gridTileWidth,
                        density, gridBasePaddingTop);
                int rowGap = Math.round(8f * density);
                dockParams.height = dockHeight;
                dockParams.leftMargin = 0;
                dockParams.topMargin = 0;
                // Permanent idle anchor: the Dock's LayoutParams never change between states.
                // The two-row transition is represented only by translationY.
                dockParams.bottomMargin = dockBottomMargin;

                gridParams.height = horizontalGridExpanded
                        ? gridViewportHeight : ViewGroup.LayoutParams.MATCH_PARENT;
                gridParams.topMargin = 0;
                gridParams.bottomMargin = horizontalGridExpanded ? dockBottomMargin : 0;
                if (grid.getPaddingTop() != gridBasePaddingTop || grid.getPaddingBottom() != 0) {
                    grid.setPadding(grid.getPaddingLeft(), gridBasePaddingTop,
                            grid.getPaddingRight(), 0);
                }
            } else {
                gridParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                gridParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                dockParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                dockParams.height = 0;
                dockParams.leftMargin = 0;
                dockParams.topMargin = 0;
                dockParams.rightMargin = 0;
                dockParams.bottomMargin = 0;
                gridParams.bottomMargin = 0;
                if (grid.getPaddingTop() != gridBasePaddingTop || grid.getPaddingBottom() != gridBasePaddingBottom) {
                    grid.setPadding(grid.getPaddingLeft(), gridBasePaddingTop,
                            grid.getPaddingRight(), gridBasePaddingBottom);
                }
            }
        }

        boolean gridLayoutChanged = !sameLayoutParams(grid.getLayoutParams(), gridParams);
        boolean dockLayoutChanged = !sameLayoutParams(dockRecycler.getLayoutParams(), dockParams);
        if (gridLayoutChanged) grid.setLayoutParams(gridParams);
        if (dockLayoutChanged) dockRecycler.setLayoutParams(dockParams);
        if (gridLayoutChanged || dockLayoutChanged || dockVisibilityChanged || gridVisibilityChanged) {
            grid.requestLayout();
            dockRecycler.requestLayout();
        }
    }

    private boolean sameLayoutParams(ViewGroup.LayoutParams current, ViewGroup.LayoutParams next) {
        if (!(current instanceof FrameLayout.LayoutParams) || !(next instanceof FrameLayout.LayoutParams)) return false;
        FrameLayout.LayoutParams a = (FrameLayout.LayoutParams) current;
        FrameLayout.LayoutParams b = (FrameLayout.LayoutParams) next;
        return a.width == b.width && a.height == b.height && a.gravity == b.gravity
                && a.leftMargin == b.leftMargin && a.topMargin == b.topMargin
                && a.rightMargin == b.rightMargin && a.bottomMargin == b.bottomMargin;
    }

    /**
     * Home's first frame waits until Home is whole: tiles bound and laid out, the dock parked at the
     * bottom and the wallpaper in. Drawn as soon as the window was ready, it showed the glass tray
     * floating empty near the top, then the tiles, then the dock dropping into place. This matters
     * most after a long stay in another app, when Android has reclaimed the launcher and Home starts
     * from nothing. The wait is capped, so Home can never hang on a slow picture.
     */
    private void holdFirstFrame() {
        final long start = android.os.SystemClock.uptimeMillis();
        firstFrameHeld = true;
        final boolean expectsWallpaper = !wallpapers.isEmpty() || (config.wallpaper != null && !placesMode) || config.wallpaperOnline;
        root.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // Parked here rather than by the layout listener alone: while frames are held, the
                // second layout pass that listener relies on does not always come.
                boolean parked = parkDock();
                // The place's name and lines come in the same frame as the picture they describe,
                // whenever focus lands in the dock, where the shelf shows.
                int gridCount = adapter == null ? 0 : adapter.getItemCount();
                int dockCount = dockAdapter == null ? 0 : dockAdapter.getItemCount();
                boolean hasTiles = gridCount > 0 || dockCount > 0;
                boolean gridReady = grid.getVisibility() != View.VISIBLE || gridCount == 0 || grid.getChildCount() > 0;
                boolean dockReady = dockCount == 0 || dockRecycler.getChildCount() > 0;
                boolean focusReady = heroShown || !lastFocusedDock || dockCount == 0;
                boolean whole = hasTiles && gridReady && dockReady && parked
                        && (wallBitmap != null || !expectsWallpaper) && focusReady;
                if (!whole && android.os.SystemClock.uptimeMillis() - start < FIRST_FRAME_WAIT_MS) {
                    return false;   // skip this frame; the next traversal asks again
                }
                root.getViewTreeObserver().removeOnPreDrawListener(this);
                firstFrameHeld = false;
                android.util.Log.i("TvHome.Perf", "first frame after " + (android.os.SystemClock.uptimeMillis() - start)
                        + " ms" + (whole ? "" : " (gave up waiting: tiles=" + adapter.getItemCount()
                        + " gridLaidOut=" + grid.getChildCount() + " dockItems=" + (dockAdapter == null ? 0 : dockAdapter.getItemCount()) + " dockLaidOut=" + (dockRecycler == null ? 0 : dockRecycler.getChildCount()) + " dockParked=" + parked
                        + " wallpaper=" + (wallBitmap != null) + ")"));
                return true;
            }
        });
    }

    /** Tells the glass the glow's colour and strength, so what shows through matches around it. */
    private void syncGlassGlow() {
        Backdrop.setGlow(ambientShape(), glowFrom, ambientColor, glowMix, ambient.getAlpha());
    }

    /** Runs the glass through the same colour blend as the room's TransitionDrawable. */
    private void blendGlassGlow(int from, long ms) {
        if (glowBlend != null) glowBlend.cancel();
        glowFrom = from;
        glowMix = 0f;
        glowBlend = android.animation.ValueAnimator.ofFloat(0f, 1f);
        glowBlend.setDuration(ms);
        glowBlend.addUpdateListener(a -> {
            glowMix = (float) a.getAnimatedValue();
            syncGlassGlow();
        });
        glowBlend.start();
    }

    /** The glow's shape in white, drawn on first use and kept: a tint gives it each app's colour. */
    private Bitmap ambientShape() {
        if (ambientShape != null) return ambientShape;
        int w = 480;
        int h = 270;
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new RadialGradient(
                w * 0.32f, h * 0.42f, w * 0.62f,
                new int[]{0x5CFFFFFF, 0x26FFFFFF, 0x00FFFFFF},
                new float[]{0f, 0.45f, 1f},
                Shader.TileMode.CLAMP));
        new Canvas(bmp).drawRect(0, 0, w, h, paint);
        ambientShape = bmp;
        return bmp;
    }

    /** With crossFade, the old glow fades into the new one (two small textures, one blend). */
    private void setAmbient(int color, boolean crossFade) {
        if (color == ambientColor && ambient.getBackground() != null) return;
        android.graphics.drawable.Drawable old = ambient.getBackground();
        if (old instanceof android.graphics.drawable.TransitionDrawable) {
            android.graphics.drawable.TransitionDrawable t = (android.graphics.drawable.TransitionDrawable) old;
            old = t.getDrawable(t.getNumberOfLayers() - 1);   // a blend still running: start from where it was heading
        }
        int previous = ambientColor;
        ambientColor = color;
        // Every app's glow is the same shape in a different colour, so the shape is drawn once and
        // each app only tints it. Drawing a fresh bitmap per move cost a CPU fill, half a megabyte
        // and a texture upload every time focus moved, which is what made moving around feel heavy
        // once every tile, not only the dock, tinted the room.
        BitmapDrawable drawable = new BitmapDrawable(getResources(), ambientShape());
        drawable.setGravity(Gravity.FILL);
        drawable.setColorFilter(new android.graphics.PorterDuffColorFilter(
                0xFF000000 | color, android.graphics.PorterDuff.Mode.SRC_IN));
        if (crossFade && old != null) {
            android.graphics.drawable.TransitionDrawable t = new android.graphics.drawable.TransitionDrawable(
                    new android.graphics.drawable.Drawable[]{old, drawable});
            t.setCrossFadeEnabled(true);
            ambient.setBackground(t);
            t.startTransition(450);
            // The glass blends with the room, over the same 450 ms, instead of jumping ahead of it.
            blendGlassGlow(previous, 450);
        } else {
            ambient.setBackground(drawable);
            if (glowBlend != null) glowBlend.cancel();
            glowFrom = color;
            glowMix = 1f;
            syncGlassGlow();
        }
    }

    /** Focus has left the apps altogether: both the shelf and the glow go. */
    private void hideHero() {
        hideShelf();
        if (!ambientShown) return;
        ambientShown = false;
        ambient.animate().alpha(0f).setDuration(reducedMotion ? 140 : 300)
                .setUpdateListener(a -> syncGlassGlow()).start();
    }

    private void hideShelf() {
        if (!heroShown) return;
        // Visibility is animated independently; the shelf's measured Y anchor is not changed.
        heroShown = false;
        if (heroShelf != null) heroShelf.animate().alpha(0f).setDuration(reducedMotion ? 100 : 160).start();
        if (islamicShelf != null) islamicShelf.animate().alpha(0f).setDuration(reducedMotion ? 100 : 160).start();
    }

    /**
     * Lights the room in this app's colour. From one app to the next it blends straight across,
     * with no dip to plain in between, whether the move is along the dock, down into the grid or
     * back up again.
     */
    private boolean isIconGlowEnabled() {
        return getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getBoolean("icon_glow_effects", true);
    }

    boolean isIconGlowEffectsEnabled() {
        return getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getBoolean("icon_glow_effects", true);
    }

    void setIconGlowEffectsEnabled(boolean enabled) {
        getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).edit().putBoolean("icon_glow_effects", enabled).apply();
        applyIconGlowPreference();
    }

    boolean isDynamicWeatherEffectsEnabled() {
        return getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getBoolean("dynamic_weather_overlay", true);
    }

    void setDynamicWeatherEffectsEnabled(boolean enabled) {
        getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).edit().putBoolean("dynamic_weather_overlay", enabled).apply();
        testWeatherOverride = null;
        updateWeatherOverlay(HomeWeather.last(this));
        if (enabled) updateHomeWeather();
    }

    private void applyIconGlowPreference() {
        if (ambient == null) return;
        if (!isIconGlowEnabled()) {
            if (glowBlend != null) glowBlend.cancel();
            ambient.animate().cancel();
            ambientShown = false;
            ambient.setAlpha(0f);
            syncGlassGlow();
        } else if (grid != null && adapter != null) {
            if (dockRecycler != null && dockRecycler.hasFocus()) {
                View dockFocused = dockRecycler.getFocusedChild();
                int dockPosition = dockFocused == null ? RecyclerView.NO_POSITION : dockRecycler.getChildAdapterPosition(dockFocused);
                if (dockPosition != RecyclerView.NO_POSITION && dockAdapter != null) showAmbient(dockAdapter.ambientColorAt(dockPosition));
            } else {
                View focused = grid.getFocusedChild();
                int position = focused == null ? RecyclerView.NO_POSITION : grid.getChildAdapterPosition(focused);
                if (position != RecyclerView.NO_POSITION) showAmbient(adapter.ambientColorAt(position));
            }
        }
    }

    private void showAmbient(int color) {
        if (!isIconGlowEnabled()) {
            ambientShown = false;
            ambient.animate().cancel();
            ambient.setAlpha(0f);
            syncGlassGlow();
            return;
        }
        if (!ambientShown) {
            setAmbient(color);
            ambient.animate().alpha(1f).setDuration(reducedMotion ? 160 : 600)
                    .setUpdateListener(a -> syncGlassGlow()).start();
            ambientShown = true;
        } else if (color != ambientColor) {
            setAmbient(color, !reducedMotion);
        }
    }

    private void hideGlass() {
        glassShown = false;
    }

    // ---- navigation ------------------------------------------------------------------------------

    // ---- arranging the tiles ---------------------------------------------------------------------

    /** The tile being moved (adapter position), or -1; its card wiggles while it is. */
    private int movingPos = -1;
    private View movingCard;
    private android.animation.ObjectAnimator wiggle;
    /** A key whose release is swallowed: the OK or Back that put a moved tile down must not also open it. */
    private int swallowRelease = -1;
    /**
     * Slides the tiles into their new places while an app is moved. The grid has no item animations
     * otherwise: they would replay on every rebuild and cost frames while scrolling.
     */
    private androidx.recyclerview.widget.DefaultItemAnimator moveSlide;

    /** Holding OK on an app: a glass menu to move it, or to open Android's page for it. */
    @Override
    public boolean onTileHeld(int position, View card) {
        return showTileContextMenu(adapter.keyAt(position), card, false);
    }

    private boolean showTileContextMenu(String key, View card, boolean inDock) {
        if (key == null || card == null || openMenu != null || movingPos >= 0) return true;
        FrameLayout panel = (FrameLayout) getLayoutInflater().inflate(R.layout.popup_inputs, root, false);
        LinearLayout rows = panel.findViewById(R.id.rows);
        boolean canMove = inDock
                ? dockAdapter != null && dockAdapter.canMove(dockAdapter.positionOf(key))
                : adapter != null && adapter.canMove(adapter.positionOf(key));
        if (canMove) {
            addInputRow(rows, () -> {
                glider.hideNow();
                gliderOnPill = false;
                closeMenu(true);
            }, R.drawable.ic_move, getString(R.string.tile_move), -1, () -> startMove(key, inDock));
        }
        addInputRow(rows, () -> closeMenu(false), R.drawable.ic_info, getString(R.string.tile_app_info), -1,
                () -> adapter.openAppInfoByKey(key));
        panel.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width = Math.max((int) (190 * density), panel.getMeasuredWidth());
        panel.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.UNSPECIFIED);
        card.getLocationInWindow(menuAt);
        root.getLocationInWindow(menuGliderAt);
        int below = menuAt[1] - menuGliderAt[1] + card.getHeight() + (int) (18 * density) + panel.getMeasuredHeight();
        showMenu(panel, rows, card, width, below > root.getHeight() - (int) (24 * density));
        return true;
    }

    /** Picks up one tile in its own region; identity remains stable while its position changes. */
    private boolean movingDock;
    private RecyclerView movingRecycler;

    private void startMove(String key, boolean inDock) {
        RecyclerView recycler = inDock ? dockRecycler : grid;
        int pos = inDock ? dockAdapter.positionOf(key) : adapter.positionOf(key);
        boolean canMove = inDock ? dockAdapter.canMove(pos) : adapter.canMove(pos);
        if (recycler == null || pos < 0 || !canMove) return;
        RecyclerView.ViewHolder vh = recycler.findViewHolderForAdapterPosition(pos);
        if (vh == null) {
            recycler.scrollToPosition(pos);
            startMoveWhenLaidOut(key, inDock, pos, 3);
            return;
        }
        movingPos = pos;
        movingDock = inDock;
        movingRecycler = recycler;
        movingCard = vh.itemView.findViewById(R.id.card);
        movingCard.requestFocus();
        glider.setPaneHidden(true);
        if (moveSlide == null) {
            moveSlide = new androidx.recyclerview.widget.DefaultItemAnimator();
            moveSlide.setSupportsChangeAnimations(false);
            moveSlide.setMoveDuration(reducedMotion ? 140 : 220);
        }
        recycler.setItemAnimator(moveSlide);
        startMoveWiggle(movingCard);
    }

    private void startMoveWhenLaidOut(String key, boolean inDock, int position, int attemptsRemaining) {
        if (movingPos >= 0) return;
        RecyclerView recycler = inDock ? dockRecycler : grid;
        int currentPosition = inDock ? dockAdapter.positionOf(key) : adapter.positionOf(key);
        if (recycler == null || currentPosition < 0) return;
        if (recycler.findViewHolderForAdapterPosition(currentPosition) != null) {
            startMove(key, inDock);
            return;
        }
        if (attemptsRemaining > 0) {
            recycler.postOnAnimation(() -> startMoveWhenLaidOut(key, inDock, currentPosition, attemptsRemaining - 1));
        }
    }

    private void startMoveWiggle(View card) {
        if (card == null) return;
        if (wiggle != null) wiggle.cancel();
        movingCard = card;
        float tilt = reducedMotion ? 0.8f : 1.4f;
        wiggle = android.animation.ObjectAnimator.ofFloat(card, View.ROTATION, -tilt, tilt);
        wiggle.setDuration(reducedMotion ? 260 : 140);
        wiggle.setRepeatMode(android.animation.ValueAnimator.REVERSE);
        wiggle.setRepeatCount(android.animation.ValueAnimator.INFINITE);
        wiggle.start();
    }

    private void finishMove() {
        if (movingPos < 0) return;
        if (wiggle != null) wiggle.cancel();
        wiggle = null;
        if (movingCard != null) movingCard.animate().rotation(0f).setDuration(120).start();
        movingCard = null;
        movingPos = -1;
        glider.setPaneHidden(false);
        RecyclerView recycler = movingRecycler;
        movingRecycler = null;
        movingDock = false;
        if (recycler != null) {
            RecyclerView.ItemAnimator slide = recycler.getItemAnimator();
            if (slide != null) slide.isRunning(() -> {
                if (movingPos < 0 && recycler.getItemAnimator() == slide) recycler.setItemAnimator(null);
            });
        }
    }

    private void moveTileAcrossRegion(String key, boolean toDock, int dockPosition) {
        if (key == null) return;
        finishMove();
        boolean changed = toDock ? adapter.pinToDock(key, dockPosition) : adapter.unpinFromDock(key);
        if (!changed) return;
        dockAdapter.refresh();
        applyDockLayout();
        RecyclerView target = toDock ? dockRecycler : grid;
        target.post(() -> startMove(key, toDock));
    }

    private void restoreMovingTile(boolean inDock, String key, int position, View previousCard, int attemptsRemaining) {
        RecyclerView recycler = inDock ? dockRecycler : grid;
        if (recycler == null || movingPos != position || movingDock != inDock || movingRecycler != recycler) return;
        RecyclerView.ViewHolder holder = recycler.findViewHolderForAdapterPosition(position);
        if (holder == null) {
            if (attemptsRemaining > 0) {
                recycler.postOnAnimation(() -> restoreMovingTile(inDock, key, position, previousCard, attemptsRemaining - 1));
            } else {
                finishMove();
            }
            return;
        }
        View nextCard = holder.itemView.findViewById(R.id.card);
        if (previousCard != null && previousCard != nextCard) previousCard.setRotation(0f);
        movingCard = nextCard;
        if (movingCard == null) {
            finishMove();
            return;
        }
        startMoveWiggle(movingCard);
        movingCard.requestFocus();
        if (inDock) onDockTileFocused(key, movingCard);
        else onTileFocused(position, movingCard);
    }

    private void stepMove(int keyCode) {
        if (movingPos < 0 || movingRecycler == null) return;
        int last;
        int to = movingPos;
        if (movingDock) {
            String movingKey = dockAdapter.keyAt(movingPos);
            if ((dockVertical && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
                    || (!dockVertical && keyCode == KeyEvent.KEYCODE_DPAD_DOWN)) {
                moveTileAcrossRegion(movingKey, false, 0);
                return;
            }
            last = dockAdapter.lastMovable();
            if (dockVertical) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP && movingPos > 0) to = movingPos - 1;
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && movingPos < last) to = movingPos + 1;
            } else {
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && movingPos > 0) to = movingPos - 1;
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && movingPos < last) to = movingPos + 1;
            }
            if (to == movingPos || !dockAdapter.move(movingPos, to)) return;
            movingPos = to;
            final int targetPosition = to;
            final String key = dockAdapter.keyAt(targetPosition);
            final View previousCard = movingCard;
            restoreMovingTile(true, key, targetPosition, previousCard, 3);
        } else {
            String movingKey = adapter.keyAt(movingPos);
            if ((!dockVertical && keyCode == KeyEvent.KEYCODE_DPAD_UP && movingPos < COLUMNS)
                    || (dockVertical && keyCode == KeyEvent.KEYCODE_DPAD_LEFT && movingPos % COLUMNS == 0)) {
                int targetDockPosition = dockVertical ? Math.min(dockAdapter.getItemCount(), movingPos / COLUMNS)
                        : movingPos % COLUMNS;
                moveTileAcrossRegion(movingKey, true, targetDockPosition);
                return;
            }
            last = adapter.lastMovable();
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (movingPos % COLUMNS > 0) to = movingPos - 1;
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (movingPos % COLUMNS < COLUMNS - 1) to = Math.min(movingPos + 1, last);
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (movingPos >= COLUMNS) to = movingPos - COLUMNS;
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (movingPos / COLUMNS < last / COLUMNS) to = Math.min(movingPos + COLUMNS, last);
                    break;
                default:
                    return;
            }
            if (to == movingPos || !adapter.move(movingPos, to)) return;
            movingPos = to;
            final int targetPosition = to;
            final String key = adapter.keyAt(targetPosition);
            final View previousCard = movingCard;
            restoreMovingTile(false, key, targetPosition, previousCard, 3);
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (screensaverActive) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                dismissScreensaver();
            }
            return true;
        }
        int code = event.getKeyCode();
        // Capture one semantic intent during collapse. Never replay synthetic KeyEvents through
        // Android focus search: one physical press must produce at most one navigation action.
        if (horizontalDockCollapseInProgress
                && (code == KeyEvent.KEYCODE_DPAD_UP || code == KeyEvent.KEYCODE_DPAD_DOWN)) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                pendingDockCollapseDirection = code;
            }
            return true;
        }
        if (wallpaperFullscreen) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                setWallpaperFullscreen(false);
                if (code == KeyEvent.KEYCODE_BACK || code == KeyEvent.KEYCODE_ESCAPE) {
                    doubleBackPending = true;
                    singleBackRunnable = () -> {
                        doubleBackPending = false;
                        singleBackRunnable = null;
                    };
                    handler.postDelayed(singleBackRunnable, DOUBLE_BACK_DELAY_MS);
                }
            }
            return true;
        }
        if (doubleBackPending && code != KeyEvent.KEYCODE_BACK && code != KeyEvent.KEYCODE_ESCAPE) {
            if (singleBackRunnable != null) {
                handler.removeCallbacks(singleBackRunnable);
                singleBackRunnable = null;
            }
            doubleBackPending = false;
        }
        if (code == swallowRelease && event.getAction() == KeyEvent.ACTION_UP) {
            swallowRelease = -1;
            return true;
        }
        if (code == KeyEvent.KEYCODE_APP_SWITCH) {
            return true; // Live apps disabled per request
        }
        if (code == KeyEvent.KEYCODE_HOME || code == KeyEvent.KEYCODE_ALL_APPS) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (event.isLongPress() || (event.getFlags() & KeyEvent.FLAG_LONG_PRESS) != 0 || event.getRepeatCount() > 0) {
                    if (!isHomeLongPressed) {
                        isHomeLongPressed = true;
                        handler.removeCallbacks(longPressHomeRunnable);
                        longPressHomeRunnable.run();
                    }
                    return true;
                }
                if (event.getRepeatCount() == 0) {
                    isHomeLongPressed = false;
                    handler.removeCallbacks(longPressHomeRunnable);
                    handler.postDelayed(longPressHomeRunnable, 450);
                }
                return true;
            }
            if (event.getAction() == KeyEvent.ACTION_UP) {
                handler.removeCallbacks(longPressHomeRunnable);
                if (isHomeLongPressed) {
                    isHomeLongPressed = false;
                    return true;
                }
                if (screensaverActive) {
                    dismissScreensaver();
                    return true;
                }
                if (cardPrompt != null) cardPrompt.dismiss();
                if (openPopup != null) openPopup.dismiss();
                closeMenu(false);
                if (movingPos >= 0) finishMove();
                focusLastTile();
                return true;
            }
            return true;
        }
        if (movingPos < 0) {
            if (code == KeyEvent.KEYCODE_BACK || code == KeyEvent.KEYCODE_ESCAPE) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    handleBackPress();
                }
                return true;
            }
            return super.dispatchKeyEvent(event);
        }
        boolean down = event.getAction() == KeyEvent.ACTION_DOWN;
        switch (code) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (down) stepMove(code);
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_ESCAPE:
                if (down) {
                    finishMove();
                    swallowRelease = code;
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public boolean onTileVerticalKey(int position, int keyCode) {
        // Activity dispatch owns input capture while collapse is active. Adapter callbacks
        // are consumed defensively and must not enqueue a duplicate intent.
        if (horizontalDockCollapseInProgress) return true;
        int count = adapter.getItemCount();
        if (count <= 0) return true;
        int target;
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (dockVertical && position % COLUMNS == 0 && dockAdapter.getItemCount() > 0) {
                focusDockTile(DockLayoutModel.dockPositionForGrid(
                        position, true, COLUMNS, dockAdapter.getItemCount()));
                return true;
            }
            return position % COLUMNS == 0;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            return position % COLUMNS == COLUMNS - 1 || position == count - 1;
        }
        if (!dockVertical && dockAdapter != null && dockAdapter.getItemCount() > 0) {
            // While a row is moving, advance from the pending target rather than the still-focused
            // card. Rapid DOWN/UP presses therefore replace the destination without stacking motion.
            int basePosition = horizontalGridTransitionRunning
                    && pendingHorizontalGridPosition >= 0
                    ? pendingHorizontalGridPosition : position;
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                if (basePosition < COLUMNS) {
                    // This Grid is the overflow row directly below the primary Dock row.
                    // UP must return to the Dock item in the same column, not jump to the header.
                    cancelHorizontalGridRowTransition();
                    int dockTarget = Math.min(basePosition % COLUMNS, dockAdapter.getItemCount() - 1);
                    focusDockTile(dockTarget);
                    return true;
                } else {
                    requestHorizontalGridRow(basePosition - COLUMNS);
                }
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                int lastRowStart = ((count - 1) / COLUMNS) * COLUMNS;
                if (basePosition < lastRowStart) {
                    requestHorizontalGridRow(Math.min(basePosition + COLUMNS, count - 1));
                }
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (position < COLUMNS) {
                if (btnHome != null && btnHome.isShown() && btnHome.requestFocus()) return true;
                return focusTopBarFallback();
            }
            target = position - COLUMNS;
        } else {
            int lastRowStart = ((count - 1) / COLUMNS) * COLUMNS;
            if (position >= lastRowStart) return true;
            target = Math.min(position + COLUMNS, count - 1);
        }
        focusTile(target);
        return true;
    }

    /** The tile {@link #focusWhenSettled} focuses once the grid stops scrolling to it. */
    private int pendingFocus;
    private final RecyclerView.OnScrollListener focusWhenSettled = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView rv, int newState) {
            if (newState != RecyclerView.SCROLL_STATE_IDLE) return;
            rv.removeOnScrollListener(this);
            if (adapter == null) return;
            if (adapter.getItemCount() == 0) {
                focusTile(0);
                return;
            }
            int targetPosition = NavigationState.resolveHomePosition(-1, pendingFocus, adapter.getItemCount());
            RecyclerView.ViewHolder h = rv.findViewHolderForAdapterPosition(targetPosition);
            View target = h != null ? h.itemView.findViewById(R.id.card) : null;
            if (target != null && target.isShown() && target.isFocusable() && target.requestFocus()) return;
            rv.post(() -> focusTile(targetPosition));
        }
    };

    private boolean focusTopBarFallback() {
        if (btnHome != null && btnHome.isShown() && btnHome.isFocusable() && btnHome.requestFocus()) {
            return true;
        }
        if (btnWifi != null && btnWifi.isShown() && btnWifi.isFocusable() && btnWifi.requestFocus()) {
            return true;
        }
        View settings = findViewById(R.id.btnSettings);
        return settings != null && settings.isShown() && settings.isFocusable() && settings.requestFocus();
    }

    private void focusLastTile() {
        // Capacity changes can move the same component between Dock and Grid. Resolve by identity
        // across both regions before falling back to a default position.
        int dockPosition = dockAdapter == null ? -1 : dockAdapter.positionOf(lastFocusedKey);
        if (dockPosition >= 0) {
            lastFocusedDock = true;
            focusDockTile(dockPosition);
            return;
        }
        int gridPosition = adapter == null ? -1 : adapter.positionOf(lastFocusedKey);
        if (gridPosition >= 0) {
            lastFocusedDock = false;
            focusTile(gridPosition);
            return;
        }
        if (!dockVertical && dockAdapter != null && dockAdapter.getItemCount() > 0) focusDockTile(0);
        else if (adapter != null && adapter.getItemCount() > 0) focusTile(0);
        else if (dockAdapter != null && dockAdapter.getItemCount() > 0) focusDockTile(0);
        else focusTopBarFallback();
    }

    private void focusLastDockTile() {
        if (dockAdapter == null || dockAdapter.getItemCount() == 0) {
            focusTile(0);
            return;
        }
        int position = dockAdapter.positionOf(lastDockFocusedKey);
        focusDockTile(position >= 0 ? position : 0);
    }

    private void focusDockTile(int position) {
        // The collapse animator owns the focus hand-off until its end callback commits idle geometry.
        // Do not let a repeated UP (or a stale focus request) steal focus and invalidate that callback.
        if (horizontalDockCollapseInProgress) return;
        if (dockAdapter == null || dockRecycler == null || dockAdapter.getItemCount() == 0) {
            focusTile(0);
            return;
        }
        if (!dockVertical && horizontalGridExpanded && !horizontalDockCollapseInProgress) {
            collapseHorizontalOverflowToDock(position);
            return;
        }
        final int requestGeneration = ++gridFocusRequestGeneration;
        final int targetPosition = NavigationState.resolveHomePosition(-1, position, dockAdapter.getItemCount());
        RecyclerView.ViewHolder vh = dockRecycler.findViewHolderForAdapterPosition(targetPosition);
        if (vh != null) {
            View card = vh.itemView.findViewById(R.id.card);
            if (card != null && card.isShown() && card.isFocusable()) {
                if (card.isFocused()) onDockTileFocused(dockAdapter.keyAt(targetPosition), card);
                else if (card.requestFocus()) return;
            }
            focusTopBarFallback();
            return;
        }
        dockRecycler.scrollToPosition(targetPosition);
        focusDockTileWhenLaidOut(targetPosition, 3, requestGeneration);
    }

    private void focusDockTileWhenLaidOut(int position, int attemptsRemaining, int requestGeneration) {
        if (requestGeneration != gridFocusRequestGeneration) return;
        if (dockAdapter == null || position < 0 || position >= dockAdapter.getItemCount()) return;
        RecyclerView.ViewHolder next = dockRecycler.findViewHolderForAdapterPosition(position);
        View card = next != null ? next.itemView.findViewById(R.id.card) : null;
        if (card != null && card.isShown() && card.isFocusable()) {
            if (card.isFocused()) onDockTileFocused(dockAdapter.keyAt(position), card);
            else card.requestFocus();
            return;
        }
        if (attemptsRemaining > 0) {
            dockRecycler.postOnAnimation(() -> focusDockTileWhenLaidOut(position, attemptsRemaining - 1, requestGeneration));
        } else {
            focusTopBarFallback();
        }
    }

    /** Expand as one physical vertical transition: Dock rises, overflow row enters from below. */
    private void expandHorizontalDockForOverflow(int targetPosition, int requestGeneration) {
        if (grid == null || dockRecycler == null || horizontalGridExpanded
                || horizontalDockExpansionInProgress) return;
        horizontalDockExpansionInProgress = true;
        cancelHorizontalGridRowTransition();
        int gridTileWidth = DockGeometry.horizontalTileWidthPx(
                DockGeometry.horizontalContentWidthPx(gridWrap.getWidth(), density),
                DockPreferences.load(this).capacity, density);
        int pitch = DockGeometry.horizontalGridViewportHeightPx(
                gridTileWidth, density, gridBasePaddingTop) + Math.round(8f * density);
        // Seed the offsets before applying expanded anchors, preventing a one-frame jump
        // in which the Dock appears already raised and the overflow row flashes in place.
        dockRecycler.setTranslationY(0f);
        grid.setTranslationY(pitch);
        // Keep the Grid fully opaque during the row transition; do not cross-fade the
        // content plane or make the Dock glass appear to change material.
        horizontalGridExpanded = true;
        applyDockLayout();
        grid.setVisibility(View.VISIBLE);
        grid.requestLayout();
        grid.post(() -> {
            if (requestGeneration != gridFocusRequestGeneration || !horizontalGridExpanded) return;
            animateHorizontalDockTransition(pitch, true, () -> {
                if (requestGeneration != gridFocusRequestGeneration) return;
                grid.setTranslationY(0f);
                dockRecycler.setTranslationY(-pitch);
                grid.setAlpha(1f);
                horizontalDockExpansionInProgress = false;
                focusTile(targetPosition);
            });
        });
    }

    /** Return to the exact idle Dock anchor; focus moves only after both rows finish sliding. */
    /**
     * Collapse the overflow row only after the destination Dock card owns Android focus.
     * The old order hid the focused Grid first and tried to recover Dock focus afterwards;
     * on TV RecyclerView timing this could leave the Activity with no focused tile.
     */
    private void collapseHorizontalOverflowToDock(int dockPosition) {
        if (grid == null || dockRecycler == null || !horizontalGridExpanded
                || horizontalDockCollapseInProgress) return;
        horizontalDockCollapseInProgress = true;
        pendingDockCollapseDirection = -1;
        final int transition = ++horizontalDockTransitionGeneration;
        prepareDockFocusBeforeCollapse(dockPosition, transition, 12);
    }

    /**
     * Acquire focus while both rows are still attached and visible. A failed acquisition aborts
     * the collapse and leaves the expanded Grid intact; it must never fall through to the header.
     */
    private void prepareDockFocusBeforeCollapse(int dockPosition, int transition, int attemptsRemaining) {
        if (transition != horizontalDockTransitionGeneration || !horizontalDockCollapseInProgress
                || !horizontalGridExpanded || dockRecycler == null || dockAdapter == null) return;
        if (dockAdapter.getItemCount() <= 0) {
            abortHorizontalDockCollapse("Dock adapter is empty");
            return;
        }
        int target = NavigationState.resolveHomePosition(-1, dockPosition, dockAdapter.getItemCount());
        RecyclerView.ViewHolder holder = dockRecycler.findViewHolderForAdapterPosition(target);
        View card = holder != null ? holder.itemView.findViewById(R.id.card) : null;
        if (card != null && card.isShown() && card.isFocusable()) {
            boolean acquired = card.isFocused() || card.requestFocus();
            if (acquired && card.isFocused()) {
                // onDockTileFocused() is synchronous and increments gridFocusRequestGeneration.
                // Capture the generation only after that authoritative callback has completed.
                startHorizontalDockCollapse(target, transition, gridFocusRequestGeneration);
                return;
            }
        } else {
            dockRecycler.scrollToPosition(target);
        }
        if (attemptsRemaining > 0) {
            dockRecycler.postOnAnimation(() -> prepareDockFocusBeforeCollapse(
                    target, transition, attemptsRemaining - 1));
            return;
        }
        abortHorizontalDockCollapse("Dock destination did not acquire focus; target=" + target);
    }

    private void startHorizontalDockCollapse(int dockPosition, int transition, int focusGeneration) {
        if (transition != horizontalDockTransitionGeneration || !horizontalDockCollapseInProgress
                || focusGeneration != gridFocusRequestGeneration || grid == null || dockRecycler == null) {
            abortHorizontalDockCollapse("Focus/transition generation changed before collapse");
            return;
        }
        int gridTileWidth = DockGeometry.horizontalTileWidthPx(
                DockGeometry.horizontalContentWidthPx(gridWrap.getWidth(), density),
                DockPreferences.load(this).capacity, density);
        final int pitch = DockGeometry.horizontalGridViewportHeightPx(
                gridTileWidth, density, gridBasePaddingTop) + Math.round(8f * density);
        android.animation.ValueAnimator previous = horizontalDockTransitionAnimator;
        if (previous != null) previous.cancel();
        final android.animation.ValueAnimator animator =
                android.animation.ValueAnimator.ofFloat(0f, 1f);
        horizontalDockTransitionAnimator = animator;
        animator.setDuration(reducedMotion ? 140 : 260);
        animator.setInterpolator(new android.view.animation.DecelerateInterpolator());
        animator.addUpdateListener(value -> {
            if (transition != horizontalDockTransitionGeneration) return;
            float progress = (Float) value.getAnimatedValue();
            dockRecycler.setTranslationY(-pitch * (1f - progress));
            grid.setTranslationY(pitch * progress);
        });
        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            private boolean cancelled;

            @Override public void onAnimationCancel(android.animation.Animator animation) {
                cancelled = true;
                if (transition == horizontalDockTransitionGeneration) {
                    // Keep the expanded scene coherent. Dock already owns focus, so no focus
                    // restoration or header fallback is necessary.
                    dockRecycler.setTranslationY(-pitch);
                    grid.setTranslationY(0f);
                    horizontalDockCollapseInProgress = false;
                    pendingDockCollapseDirection = -1;
                }
            }

            @Override public void onAnimationEnd(android.animation.Animator animation) {
                if (horizontalDockTransitionAnimator == animator) horizontalDockTransitionAnimator = null;
                if (cancelled || transition != horizontalDockTransitionGeneration) return;
                if (focusGeneration != gridFocusRequestGeneration) {
                    abortHorizontalDockCollapse("Focus generation changed during collapse");
                    return;
                }
                // Dock already owns focus. Only now remove the overflow row from the view tree.
                horizontalGridExpanded = false;
                grid.setAlpha(1f);
                grid.setVisibility(View.GONE);
                dockRecycler.setTranslationY(0f);
                grid.setTranslationY(0f);
                applyDockLayout();
                horizontalDockCollapseInProgress = false;
                dispatchPendingDockCollapseDirection(dockPosition);
            }
        });
        animator.start();
    }

    private void abortHorizontalDockCollapse(String reason) {
        android.util.Log.e("TvHome", "Horizontal Dock collapse aborted safely: " + reason);
        horizontalDockCollapseInProgress = false;
        pendingDockCollapseDirection = -1;
        if (horizontalGridExpanded) {
            if (dockRecycler != null) dockRecycler.setTranslationY(-getHorizontalDockPitch());
            if (grid != null) {
                grid.setTranslationY(0f);
                grid.setVisibility(View.VISIBLE);
            }
        }
    }

    private int getHorizontalDockPitch() {
        int gridTileWidth = DockGeometry.horizontalTileWidthPx(
                DockGeometry.horizontalContentWidthPx(gridWrap.getWidth(), density),
                DockPreferences.load(this).capacity, density);
        return DockGeometry.horizontalGridViewportHeightPx(
                gridTileWidth, density, gridBasePaddingTop) + Math.round(8f * density);
    }

    /** Apply one captured direction through the launcher's semantic navigation contract. */
    private void dispatchPendingDockCollapseDirection(int dockPosition) {
        int direction = pendingDockCollapseDirection;
        pendingDockCollapseDirection = -1;
        if (direction == KeyEvent.KEYCODE_DPAD_DOWN && adapter != null && adapter.getItemCount() > 0) {
            int target = DockLayoutModel.gridPositionForDock(
                    dockPosition, false, COLUMNS, adapter.getItemCount());
            if (target >= 0) focusTile(target);
        } else if (direction == KeyEvent.KEYCODE_DPAD_UP) {
            focusTopBarFallback();
        }
    }

    private void animateHorizontalDockTransition(int pitch, boolean expanding, Runnable onSettled) {
        android.animation.ValueAnimator previous = horizontalDockTransitionAnimator;
        if (previous != null) previous.cancel();
        final int generation = ++horizontalDockTransitionGeneration;
        final float from = expanding ? pitch : -pitch;
        final android.animation.ValueAnimator animator = android.animation.ValueAnimator.ofFloat(from, 0f);
        horizontalDockTransitionAnimator = animator;
        animator.setDuration(reducedMotion ? 140 : 260);
        animator.setInterpolator(new android.view.animation.DecelerateInterpolator());
        animator.addUpdateListener(value -> {
            if (generation != horizontalDockTransitionGeneration) return;
            float offset = (Float) value.getAnimatedValue();
            float progress = 1f - Math.abs(offset) / Math.max(1f, pitch);
            if (dockRecycler != null) dockRecycler.setTranslationY(-pitch * progress);
            if (grid != null) grid.setTranslationY(offset);
        });
        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            private boolean cancelled;
            @Override public void onAnimationCancel(android.animation.Animator animation) {
                cancelled = true;
                if (expanding) horizontalDockExpansionInProgress = false;
            }
            @Override public void onAnimationEnd(android.animation.Animator animation) {
                if (horizontalDockTransitionAnimator == animator) horizontalDockTransitionAnimator = null;
                if (!cancelled && generation == horizontalDockTransitionGeneration && onSettled != null) {
                    onSettled.run();
                }
            }
        });
        animator.start();
    }

    private void focusTile(int position) {
        if (adapter == null || grid == null) return;
        final int requestGeneration = ++gridFocusRequestGeneration;
        if (!dockVertical && !horizontalGridExpanded) {
            if (adapter.getItemCount() == 0) {
                if (dockAdapter != null && dockAdapter.getItemCount() > 0) focusDockTile(0);
                else focusTopBarFallback();
                return;
            }
            // A hidden RecyclerView cannot receive focus. Reveal it, wait for layout,
            // then resolve the requested item by its current adapter position.
            expandHorizontalDockForOverflow(position, requestGeneration);
            return;
        }
        if (adapter.getItemCount() == 0) {
            if (dockAdapter != null && dockAdapter.getItemCount() > 0) focusDockTile(0);
            else focusTopBarFallback();
            return;
        }
        final int pos = NavigationState.resolveHomePosition(-1, position, adapter.getItemCount());
        if (!dockVertical && dockAdapter != null && dockAdapter.getItemCount() > 0) {
            // Keep the active overflow row top-aligned immediately below the primary Dock row.
            // Default scrollToPosition aligns to the top and breaks the intended row progression.
            alignHorizontalGridRowToGlass(pos);
            grid.postOnAnimation(() -> focusTileWhenLaidOut(pos, requestGeneration, 8));
            return;
        }
        RecyclerView.ViewHolder vh = grid.findViewHolderForAdapterPosition(pos);
        if (vh != null) {
            View card = vh.itemView.findViewById(R.id.card);
            if (card != null && card.isShown() && card.isFocusable()) {
                // Already focused (e.g. back from an app): no focus change fires, so restore the glass by hand.
                if (card.isFocused()) onTileFocused(pos, card);
                else if (card.requestFocus()) return;
            }
            focusTopBarFallback();
            return;
        }
        // Not laid out yet: the Grid may have just been revealed and position 0 may
        // require no scroll at all. A scroll-state listener alone can then wait forever.
        // Retry after layout frames and request scrolling only if the holder is still absent.
        focusTileWhenLaidOut(pos, requestGeneration, 6);
    }

    private void cancelHorizontalGridRowTransition() {
        horizontalGridTransitionGeneration++;
        pendingHorizontalGridPosition = RecyclerView.NO_POSITION;
        pendingHorizontalGridKey = null;
        horizontalGridTransitionRunning = false;
        horizontalGridTransitionRetries = 0;
        android.animation.ValueAnimator previous = horizontalGridScrollAnimator;
        horizontalGridScrollAnimator = null;
        if (previous != null) previous.cancel();
        if (grid != null && grid.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) grid.stopScroll();
    }

    /** Queue one deterministic row target; a newer remote press replaces the pending destination. */
    private void requestHorizontalGridRow(int targetPosition) {
        if (grid == null || adapter == null || targetPosition < 0
                || targetPosition >= adapter.getItemCount()) return;
        pendingHorizontalGridPosition = targetPosition;
        pendingHorizontalGridKey = adapter.keyAt(targetPosition);
        horizontalGridTransitionRunning = true;
        horizontalGridTransitionRetries = 0;
        final int transitionGeneration = ++horizontalGridTransitionGeneration;
        final int focusGeneration = ++gridFocusRequestGeneration;
        android.animation.ValueAnimator previous = horizontalGridScrollAnimator;
        horizontalGridScrollAnimator = null;
        if (previous != null) previous.cancel();
        if (grid.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) grid.stopScroll();
        grid.postOnAnimation(() -> startHorizontalGridRowTransition(
                transitionGeneration, focusGeneration));
    }

    /** Resolve by stable component identity first; clamp only if that app disappeared. */
    private int resolvePendingHorizontalGridPosition() {
        if (adapter == null || adapter.getItemCount() <= 0) return RecyclerView.NO_POSITION;
        int resolved = pendingHorizontalGridKey == null
                ? RecyclerView.NO_POSITION : adapter.positionOf(pendingHorizontalGridKey);
        if (resolved < 0) {
            resolved = Math.max(0, Math.min(pendingHorizontalGridPosition, adapter.getItemCount() - 1));
        }
        pendingHorizontalGridPosition = resolved;
        pendingHorizontalGridKey = adapter.keyAt(resolved);
        return resolved;
    }

    private void startHorizontalGridRowTransition(int transitionGeneration, int focusGeneration) {
        if (!horizontalGridTransitionRunning
                || transitionGeneration != horizontalGridTransitionGeneration
                || focusGeneration != gridFocusRequestGeneration
                || pendingHorizontalGridPosition < 0
                || adapter == null || grid == null) return;
        int target = resolvePendingHorizontalGridPosition();
        if (target == RecyclerView.NO_POSITION) {
            cancelHorizontalGridRowTransition();
            if (dockAdapter != null && dockAdapter.getItemCount() > 0) focusDockTile(0);
            else focusTopBarFallback();
            return;
        }
        GridLayoutManager manager = grid.getLayoutManager() instanceof GridLayoutManager
                ? (GridLayoutManager) grid.getLayoutManager() : null;
        if (manager == null) {
            horizontalGridTransitionRunning = false;
            pendingHorizontalGridPosition = RecyclerView.NO_POSITION;
            pendingHorizontalGridKey = null;
            focusTopBarFallback();
            return;
        }

        int firstVisible = manager.findFirstVisibleItemPosition();
        View firstView = firstVisible == RecyclerView.NO_POSITION
                ? null : manager.findViewByPosition(firstVisible);
        if (firstView == null || firstView.getHeight() <= 0) {
            if (horizontalGridTransitionRetries++ < 4) {
                grid.postOnAnimation(() -> startHorizontalGridRowTransition(
                        transitionGeneration, focusGeneration));
            } else {
                horizontalGridTransitionRunning = false;
                pendingHorizontalGridPosition = RecyclerView.NO_POSITION;
                pendingHorizontalGridKey = null;
                focusTopBarFallback();
            }
            return;
        }

        // Measure the decorated row pitch and its actual top offset. Raw child height/top
        // omit RecyclerView margins/decorations and can leave a row partially visible.
        Rect rowBounds = new Rect();
        manager.getDecoratedBoundsWithMargins(firstView, rowBounds);
        int rowPitch = Math.max(1, rowBounds.height());
        int currentRow = firstVisible / COLUMNS;
        int targetRow = target / COLUMNS;
        int currentTopOffset = rowBounds.top - grid.getPaddingTop();
        int dy = DockGeometry.gridRowScrollDeltaPx(
                currentRow, targetRow, rowPitch, currentTopOffset);
        if (dy == 0) {
            settleHorizontalGridRowTransition(transitionGeneration, focusGeneration);
            return;
        }
        int duration = Math.min(560, Math.max(200, 220 * Math.abs(targetRow - currentRow)));
        animateHorizontalGridRowScroll(dy, duration, transitionGeneration, focusGeneration);
    }

    /**
     * Animates actual RecyclerView content movement. scrollBy() is applied incrementally,
     * so DOWN physically moves rows upward and UP physically moves them downward.
     */
    private void animateHorizontalGridRowScroll(int dy, long duration,
                                                  int transitionGeneration, int focusGeneration) {
        if (grid == null) return;
        if (dy == 0) {
            grid.postOnAnimation(() -> settleHorizontalGridRowTransition(
                    transitionGeneration, focusGeneration));
            return;
        }
        final android.animation.ValueAnimator animator = android.animation.ValueAnimator.ofInt(0, dy);
        horizontalGridScrollAnimator = animator;
        animator.setDuration(duration);
        animator.setInterpolator(new android.view.animation.DecelerateInterpolator());
        final int[] lastValue = {0};
        animator.addUpdateListener(animation -> {
            if (!horizontalGridTransitionRunning
                    || transitionGeneration != horizontalGridTransitionGeneration
                    || focusGeneration != gridFocusRequestGeneration
                    || grid == null) return;
            int value = (Integer) animation.getAnimatedValue();
            int step = value - lastValue[0];
            lastValue[0] = value;
            if (step != 0) grid.scrollBy(0, step);
        });
        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            private boolean cancelled;

            @Override
            public void onAnimationCancel(android.animation.Animator animation) {
                cancelled = true;
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                if (horizontalGridScrollAnimator == animator) horizontalGridScrollAnimator = null;
                if (!cancelled && horizontalGridTransitionRunning
                        && transitionGeneration == horizontalGridTransitionGeneration
                        && focusGeneration == gridFocusRequestGeneration) {
                    grid.postOnAnimation(() -> settleHorizontalGridRowTransition(
                            transitionGeneration, focusGeneration));
                }
            }
        });
        animator.start();
    }

    private void settleHorizontalGridRowTransition(int transitionGeneration, int focusGeneration) {
        if (!horizontalGridTransitionRunning
                || transitionGeneration != horizontalGridTransitionGeneration
                || focusGeneration != gridFocusRequestGeneration
                || pendingHorizontalGridPosition < 0
                || adapter == null || grid == null) return;
        int target = resolvePendingHorizontalGridPosition();
        if (target == RecyclerView.NO_POSITION) {
            cancelHorizontalGridRowTransition();
            if (dockAdapter != null && dockAdapter.getItemCount() > 0) focusDockTile(0);
            else focusTopBarFallback();
            return;
        }
        GridLayoutManager manager = grid.getLayoutManager() instanceof GridLayoutManager
                ? (GridLayoutManager) grid.getLayoutManager() : null;
        View targetView = manager == null ? null : manager.findViewByPosition(target);
        if (targetView == null) {
            if (horizontalGridTransitionRetries++ < 4) {
                grid.postOnAnimation(() -> startHorizontalGridRowTransition(
                        transitionGeneration, focusGeneration));
            } else {
                horizontalGridTransitionRunning = false;
                pendingHorizontalGridPosition = RecyclerView.NO_POSITION;
                pendingHorizontalGridKey = null;
                focusTopBarFallback();
            }
            return;
        }

        Rect targetBounds = new Rect();
        manager.getDecoratedBoundsWithMargins(targetView, targetBounds);
        int correction = targetBounds.top - grid.getPaddingTop();
        if (Math.abs(correction) > 1) {
            if (horizontalGridTransitionRetries++ < 3) {
                animateHorizontalGridRowScroll(correction,
                        Math.min(180, Math.max(90, Math.abs(correction) * 2L)),
                        transitionGeneration, focusGeneration);
                return;
            }
            // Do not silently focus a row whose final geometry is still wrong.
            horizontalGridTransitionRunning = false;
            pendingHorizontalGridPosition = RecyclerView.NO_POSITION;
            pendingHorizontalGridKey = null;
            focusTopBarFallback();
            return;
        }

        String targetKey = pendingHorizontalGridKey;
        pendingHorizontalGridPosition = RecyclerView.NO_POSITION;
        pendingHorizontalGridKey = null;
        horizontalGridTransitionRunning = false;
        horizontalGridTransitionRetries = 0;
        focusHorizontalGridRowTargetWhenLaidOut(targetKey, target, focusGeneration, 8);
    }

    /**
     * Focuses the already-visible row target without any scrollToPosition fallback.
     * If the inventory changed during motion, resolve by stable app key and queue a new
     * measured transition instead of focusing a different adapter item.
     */
    private void focusHorizontalGridRowTargetWhenLaidOut(String key, int fallbackPosition,
                                                          int requestGeneration, int attemptsRemaining) {
        if (requestGeneration != gridFocusRequestGeneration || adapter == null || grid == null) return;
        int count = adapter.getItemCount();
        if (count <= 0) {
            focusTopBarFallback();
            return;
        }
        int position = key == null ? RecyclerView.NO_POSITION : adapter.positionOf(key);
        if (position < 0) {
            position = Math.max(0, Math.min(fallbackPosition, count - 1));
        }
        GridLayoutManager manager = grid.getLayoutManager() instanceof GridLayoutManager
                ? (GridLayoutManager) grid.getLayoutManager() : null;
        int firstComplete = manager == null ? RecyclerView.NO_POSITION
                : manager.findFirstCompletelyVisibleItemPosition();
        if (firstComplete != RecyclerView.NO_POSITION
                && firstComplete / COLUMNS != position / COLUMNS) {
            requestHorizontalGridRow(position);
            return;
        }
        View targetView = manager == null ? null : manager.findViewByPosition(position);
        if (targetView != null) {
            Rect targetBounds = new Rect();
            manager.getDecoratedBoundsWithMargins(targetView, targetBounds);
            if (Math.abs(targetBounds.top - grid.getPaddingTop()) > 1) {
                requestHorizontalGridRow(position);
                return;
            }
        }
        RecyclerView.ViewHolder holder = grid.findViewHolderForAdapterPosition(position);
        View card = holder != null ? holder.itemView.findViewById(R.id.card) : null;
        if (card != null && card.isShown() && card.isFocusable()) {
            if (card.isFocused()) onTileFocused(position, card);
            else if (card.requestFocus()) return;
        }
        if (attemptsRemaining > 0) {
            grid.postOnAnimation(() -> focusHorizontalGridRowTargetWhenLaidOut(
                    key, fallbackPosition, requestGeneration, attemptsRemaining - 1));
        } else {
            focusTopBarFallback();
        }
    }

    /** Aligns a horizontal overflow row to the fixed baseline immediately below the primary Dock row. */
    private void alignHorizontalGridRowToGlass(int position) {
        RecyclerView.LayoutManager manager = grid.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            ((GridLayoutManager) manager).scrollToPositionWithOffset(position, grid.getPaddingTop());
        } else {
            grid.scrollToPosition(position);
        }
    }

    private void focusTileWhenLaidOut(int position, int requestGeneration, int attemptsRemaining) {
        if (requestGeneration != gridFocusRequestGeneration) return;
        if (adapter == null || grid == null || position < 0 || position >= adapter.getItemCount()) return;
        RecyclerView.ViewHolder holder = grid.findViewHolderForAdapterPosition(position);
        View card = holder != null ? holder.itemView.findViewById(R.id.card) : null;
        if (card != null && card.isShown() && card.isFocusable()) {
            // requestFocus() emits no callback when Android already retains this card.
            // Reapply the glass target explicitly so the visual focus matches logical focus.
            if (card.isFocused()) onTileFocused(position, card);
            else card.requestFocus();
            return;
        }
        if (attemptsRemaining > 0) {
            grid.postOnAnimation(() -> focusTileWhenLaidOut(position, requestGeneration, attemptsRemaining - 1));
        } else if (attemptsRemaining == 0) {
            // Align the requested row to the start of the fixed one-row viewport, then
            // keep checking layout completion instead of assuming one frame is sufficient.
            if (!dockVertical && dockAdapter != null && dockAdapter.getItemCount() > 0) {
                alignHorizontalGridRowToGlass(position);
            } else {
                grid.scrollToPosition(position);
            }
            grid.postOnAnimation(() -> focusTileWhenLaidOut(position, requestGeneration, 8));
        } else {
            focusTopBarFallback();
        }
    }

    // ---- top bar ---------------------------------------------------------------------------------

    private void updateShamsiDate() {
        if (shamsi) shamsiDate.setText(Shamsi.today());
    }

    private void updateHeaderTabLabels() {
        boolean indonesian = "id".equals(AppLanguage.getLanguage(this));
        if (btnHome instanceof TextView) {
            ((TextView) btnHome).setText(indonesian ? "Beranda" : "Home");
            ((TextView) btnHome).setContentDescription(indonesian ? "Beranda" : "Home");
        }
        if (btnCandyBox instanceof TextView) {
            ((TextView) btnCandyBox).setText(indonesian ? "Profil" : "Profile");
        }
        View settings = findViewById(R.id.btnSettings);
        if (settings instanceof TextView) {
            ((TextView) settings).setText(indonesian ? "Pengaturan" : "Settings");
        }
        if (root != null) root.post(this::applyHeaderWidths);
    }

    private void updateLocationPill() {
        TextView name = findViewById(R.id.locationName);
        TextView altitude = findViewById(R.id.locationAltitude);
        View pill = findViewById(R.id.locationPill);
        if (name == null || altitude == null || pill == null) return;
        Location.Zone zone = Location.chosen(this);
        if (zone == null || zone.town == null || zone.town.trim().isEmpty()) {
            pill.setVisibility(View.GONE);
            return;
        }
        pill.setVisibility(View.VISIBLE);
        name.setText(zone.town);
        View divider = findViewById(R.id.locationAltitudeDivider);
        View mountain = findViewById(R.id.locationMountainIcon);
        if (zone.altitudeMeters != null && zone.altitudeMeters > 0) {
            altitude.setText(getString(R.string.location_altitude_format, zone.altitudeMeters));
            altitude.setVisibility(View.VISIBLE);
            if (divider != null) divider.setVisibility(View.VISIBLE);
            if (mountain != null) mountain.setVisibility(View.VISIBLE);
        } else {
            altitude.setText("");
            altitude.setVisibility(View.GONE);
            if (divider != null) divider.setVisibility(View.GONE);
            if (mountain != null) mountain.setVisibility(View.GONE);
        }
        if (root != null) root.post(this::applyHeaderWidths);
    }

    private void updateHomeTabIndicator() {
        if (btnHome == null) return;
        // Beranda is a peer navigation tab, not a permanently selected/raised Home badge.
        btnHome.setSelected(false);
        btnHome.setElevation(0f);
        btnHome.invalidate();
    }

    /**
     * Sizes the left information pills and center navigation inside the same measured header frame.
     * The left edge comes from dateBlock's content, not leftBlock's potentially constrained width.
     */
    private void applyHeaderWidths() {
        View left = findViewById(R.id.leftBlock);
        View date = findViewById(R.id.dateBlock);
        View right = findViewById(R.id.topButtons);
        View nav = findViewById(R.id.navPill);
        View location = findViewById(R.id.locationPill);
        View weather = findViewById(R.id.weatherAirQuality);
        if (left == null || date == null || right == null || nav == null
                || !(left.getParent() instanceof ViewGroup)) return;
        ViewGroup header = (ViewGroup) left.getParent();
        int width = header.getWidth();
        if (width <= 0 || left.getHeight() <= 0 || right.getWidth() <= 0 || date.getWidth() <= 0) return;

        // The location pill defines the shared width for both left information pills.
        // Keep this single calculation independent of either view's visibility.
        int locationWidth = Math.round(width * 0.30f);
        if (location != null) {
            ViewGroup.LayoutParams params = location.getLayoutParams();
            if (params.width != locationWidth) {
                params.width = locationWidth;
                location.setLayoutParams(params);
            }
        }
        if (weather != null) {
            // Both information pills share the location pill's measured width. Their XML
            // anchors already share the same parent-start edge, so equal widths produce
            // identical left and right geometry without changing the location reference.
            ViewGroup.LayoutParams params = weather.getLayoutParams();
            if (params.width != locationWidth) {
                params.width = locationWidth;
                weather.setLayoutParams(params);
            }
        }

        int gap = Math.round(12f * density);
        int leftContentEnd = left.getLeft() + date.getRight() + gap;
        int rightContentStart = right.getLeft() - gap;
        int desired = Math.round(width * 0.39f);
        int available = HomeHeaderGeometry.centeredWidthPx(width, leftContentEnd, rightContentStart, desired);
        if (available <= 0) {
            android.util.Log.e("TvHome.Header", "No measured space for centered Home navigation; header geometry needs review");
            return;
        }
        ViewGroup.LayoutParams navParams = nav.getLayoutParams();
        if (navParams.width != available) {
            navParams.width = available;
            nav.setLayoutParams(navParams);
        }
        if (nav.getVisibility() != View.VISIBLE) nav.setVisibility(View.VISIBLE);
        if (nav.getLayoutParams() instanceof android.widget.RelativeLayout.LayoutParams) {
            android.widget.RelativeLayout.LayoutParams params =
                    (android.widget.RelativeLayout.LayoutParams) nav.getLayoutParams();
            int centeredTop = Math.max(0, (left.getHeight() - nav.getHeight()) / 2);
            if (params.topMargin != centeredTop) {
                params.topMargin = centeredTop;
                nav.setLayoutParams(params);
            }
        }
        if (available < Math.round(210f * density)) {
            android.util.Log.w("TvHome.Header", "Centered navigation is compressed to " + available + "px at this resolution");
        }
    }

    private void updatePrayerTimes() {
        if (prayerTimesView != null) {
            prayerTimesView.setText(PrayerTimes.getFormatted(this));
        }
        updateIslamicShelf();
        if (screensaverActive) {
            updateNightstandScreensaver();
        }
    }

    private void updateWeatherAqi() {
        if (weatherAirQualityView == null) return;
        // The Home pill and Weather dashboard must read the same location-scoped cache.
        // Do not let the legacy lightweight HomeWeather cache overwrite the full G3B line:
        // it can contain temperature only while the dashboard already has a valid AQI record.
        weatherAirQualityView.setText(WeatherAirQuality.getFormatted(this));
    }

    private void updateHomeWeather() {
        if (weatherAirQualityView == null || !isDynamicWeatherEffectsEnabled()
                || homeWeatherRunning || !HomeWeather.due(this)) return;
        homeWeatherRunning = true;
        Location.Zone zone = Location.chosen(this);
        final double lat = (zone != null) ? zone.lat : -4.0212;
        final double lon = (zone != null) ? zone.lon : 103.2517;
        new Thread(() -> {
            HomeWeather.Line line = HomeWeather.refresh(getApplicationContext(), lat, lon);
            handler.post(() -> {
                homeWeatherRunning = false;
                if (line != null && !isDestroyed()) {
                    showHomeWeather(line);
                } else if (!isDestroyed() && HomeWeather.due(this)) {
                    // A superseded response is intentionally not cached; immediately fetch the current location.
                    updateHomeWeather();
                }
            });
        }, "home-weather").start();
    }

    private void showHomeWeather(HomeWeather.Line line) {
        if (weatherAirQualityView == null) return;
        // Keep the visible weather/AQI text on the same location-scoped G3B cache as WeatherActivity.
        // The legacy HomeWeather refresh is retained only for its atmospheric wallpaper overlay;
        // it must never overwrite the shared weather/AQI line or hide an available AQI value.
        updateWeatherAqi();
        if (line != null) updateWeatherOverlay(line);
    }

    public void ensureWeatherOverlayView() {
        if (weatherOverlayView == null && root != null) {
            weatherOverlayView = new WeatherBackgroundView(this);
            weatherOverlayView.setOverlayMode(true);
            try {
                android.app.ActivityManager am = (android.app.ActivityManager) getSystemService(ACTIVITY_SERVICE);
                android.app.ActivityManager.MemoryInfo mi = new android.app.ActivityManager.MemoryInfo();
                if (am != null) {
                    am.getMemoryInfo(mi);
                    if (mi.totalMem <= 1610612736L) {
                        weatherOverlayView.setLowSpecMode(true);
                    }
                }
            } catch (Exception ignored) {}
            weatherOverlayView.setReducedMotion(config != null && config.reducedMotion);

            int idx = (wallView != null) ? root.indexOfChild(wallView) + 1 : 0;
            root.addView(weatherOverlayView, idx, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            weatherOverlayView.setVisibility(View.GONE);
        }
    }

    public void updateWeatherOverlay(HomeWeather.Line line) {
        ensureWeatherOverlayView();
        if (weatherOverlayView == null) return;

        boolean enabled = getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getBoolean("dynamic_weather_overlay", true);
        if (!enabled) {
            weatherOverlayView.setCondition(WeatherBackgroundView.ConditionType.NONE);
            weatherOverlayView.stopAnimation();
            weatherOverlayView.setVisibility(View.GONE);
            return;
        }

        String wText = "";
        if (testWeatherOverride != null && !testWeatherOverride.isEmpty()) {
            wText = testWeatherOverride.toLowerCase(Locale.ROOT);
        } else if (line != null && line.weather != null) {
            wText = line.weather.toLowerCase(Locale.ROOT);
        }

        WeatherBackgroundView.ConditionType cond = WeatherBackgroundView.ConditionType.NONE;
        if (wText.contains("thunder") || wText.contains("storm") || wText.contains("ribut") || wText.contains("petir")) {
            cond = WeatherBackgroundView.ConditionType.THUNDERSTORM;
        } else if (wText.contains("heavy rain") || wText.contains("lebat") || wText.contains("showers")) {
            cond = WeatherBackgroundView.ConditionType.RAIN;
        } else if (wText.contains("rain") || wText.contains("drizzle") || wText.contains("hujan") || wText.contains("renyai")) {
            cond = WeatherBackgroundView.ConditionType.DRIZZLE;
        }

        if (cond == WeatherBackgroundView.ConditionType.NONE) {
            weatherOverlayView.setCondition(WeatherBackgroundView.ConditionType.NONE);
            weatherOverlayView.stopAnimation();
            weatherOverlayView.setVisibility(View.GONE);
        } else {
            weatherOverlayView.setVisibility(View.VISIBLE);
            weatherOverlayView.setCondition(cond);
            weatherOverlayView.startAnimation();
        }
    }

    public void setTestWeatherOverride(String condition) {
        if ("reset".equalsIgnoreCase(condition) || "clear".equalsIgnoreCase(condition) || "none".equalsIgnoreCase(condition)) {
            this.testWeatherOverride = null;
        } else {
            this.testWeatherOverride = condition;
        }
        updateWeatherOverlay(HomeWeather.last(this));
    }

    public boolean isIslamicShelfEnabled() {
        return getSharedPreferences("theme_prefs", MODE_PRIVATE).getBoolean("islamic_shelf_enabled", true);
    }

    public void setIslamicShelfEnabled(boolean enabled) {
        getSharedPreferences("theme_prefs", MODE_PRIVATE).edit().putBoolean("islamic_shelf_enabled", enabled).apply();
        if (islamicShelf != null) {
            islamicShelf.setVisibility(enabled ? View.VISIBLE : View.GONE);
            if (enabled) updateIslamicShelf();
        }
    }

    void updateIslamicShelf() {
        if (islamicShelf == null) return;
        if (!isIslamicShelfEnabled()) {
            islamicShelf.setVisibility(View.GONE);
            return;
        }

        if (WeeklyAyah.shown(this)) {
            WeeklyAyah.Ayah a = WeeklyAyah.stored(this);
            if (a == null) a = WeeklyAyah.last(this);
            showAyah(a);
            updateAyah();
        } else if (ayahBlock != null) {
            ayahBlock.setVisibility(View.GONE);
        }

        Location.Zone currentZone = Location.chosen(this);
        String zoneTitle = (currentZone != null && currentZone.town != null) ? currentZone.town.toUpperCase(Locale.ROOT) : PrayerTimes.getShortZoneName(this);
        if (prayerHeader != null) {
            String pPrefix = AppLanguage.getPrayerHeaderPrefix(this);
            prayerHeader.setText(pPrefix + " " + zoneTitle + "  •  " + PrayerTimes.getHijriDate(this).toUpperCase(Locale.ROOT));
            prayerHeader.setTextColor(0xFFF4F7FC);
        }

        PrayerTimes.PrayerItem[] items = PrayerTimes.getPrayerItems(this);
        if (items != null && items.length >= 6) {
            TextView[] labels = {lblSubuh, lblSyuruk, lblZohor, lblAsar, lblMaghrib, lblIsyak};
            TextView[] times = {timeSubuh, timeSyuruk, timeZohor, timeAsar, timeMaghrib, timeIsyak};

            for (int i = 0; i < 6; i++) {
                if (times[i] != null) times[i].setText(items[i].timeStr);
                if (labels[i] != null) {
                    labels[i].setText(items[i].name);
                    if (items[i].isNext) {
                        labels[i].setTextColor(TvUiTokens.ISLAMIC_GOLD);
                        if (times[i] != null) times[i].setTextColor(TvUiTokens.ISLAMIC_GOLD);
                    } else {
                        labels[i].setTextColor(TvUiTokens.TEXT_SECONDARY);
                        if (times[i] != null) times[i].setTextColor(TvUiTokens.TEXT_PRIMARY);
                    }
                    labels[i].setShadowLayer(3f * density, 0f, 1f * density, TvUiTokens.ISLAMIC_TEXT_SHADOW);
                    if (times[i] != null) {
                        times[i].setShadowLayer(3f * density, 0f, 1f * density, TvUiTokens.ISLAMIC_TEXT_SHADOW);
                    }
                }
            }
        }
    }

    void updateAyah() {
        if (ayahBlock == null || ayahRunning || !WeeklyAyah.shown(this) || !WeeklyAyah.shouldAttemptRefresh(this)) return;
        ayahRunning = true;
        new Thread(() -> {
            WeeklyAyah.Ayah a = WeeklyAyah.refresh(getApplicationContext());
            handler.post(() -> {
                ayahRunning = false;
                if (a != null && !isDestroyed()) showAyah(a);
            });
        }, "weekly-ayah").start();
    }

    void showAyah(WeeklyAyah.Ayah a) {
        if (ayahBlock == null) return;
        if (!WeeklyAyah.shown(this) || (nowPlayingCard != null && nowPlayingCard.getVisibility() == View.VISIBLE)) {
            ayahBlock.setVisibility(View.GONE);
            return;
        }
        if (a == null || a.arabic == null || a.arabic.trim().isEmpty()) {
            ayahBlock.setVisibility(View.GONE);
            return;
        }

        int usable = getResources().getDisplayMetrics().widthPixels - Math.round(68 * density);
        int islamicMaxWidth = Math.round(usable * 0.46f);
        boolean is4K = (getResources().getDisplayMetrics().widthPixels >= 3840
                || getResources().getDisplayMetrics().heightPixels >= 2160);

        if (ayahLabel != null) {
            ayahLabel.setText(AppLanguage.getAyahLabel(this, a.reference,
                    WeeklyAyah.stored(this) != null, WeeklyAyah.hasCached(this)));
            ayahLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, is4K ? 13f : 11f);
        }
        if (ayahArabic != null) {
            // A fixed shared line box gives RTL Arabic a stable right edge.
            ViewGroup.LayoutParams arabicParams = ayahArabic.getLayoutParams();
            if (arabicParams != null && arabicParams.width != islamicMaxWidth) {
                arabicParams.width = islamicMaxWidth;
                ayahArabic.setLayoutParams(arabicParams);
            }
            ayahArabic.setMaxWidth(islamicMaxWidth);
            ayahArabic.setSingleLine(false);
            ayahArabic.setMaxLines(2);
            ayahArabic.setEllipsize(TextUtils.TruncateAt.END);
            // Text direction controls Arabic shaping; layout direction controls relative gravity.
            // Keep physical alignment LTR/right while the glyph run itself remains RTL.
            ayahArabic.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            ayahArabic.setTextDirection(View.TEXT_DIRECTION_RTL);
            ayahArabic.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
            ayahArabic.setGravity(Gravity.RIGHT | Gravity.TOP);
            ayahArabic.setTextColor(TvUiTokens.ISLAMIC_GOLD);
            ayahArabic.setShadowLayer(3f * density, 0f, 2f * density, TvUiTokens.ISLAMIC_TEXT_SHADOW);
            ayahArabic.setIncludeFontPadding(true);
            ayahArabic.setBreakStrategy(android.text.Layout.BREAK_STRATEGY_HIGH_QUALITY);
            ayahArabic.setHyphenationFrequency(android.text.Layout.HYPHENATION_FREQUENCY_NONE);
            ayahArabic.setTextSize(TypedValue.COMPLEX_UNIT_SP, is4K ? 22f : 20f);
            ayahArabic.setText(a.arabic.trim());
        }
        if (ayahText != null) {
            // Keep translation in the same measured column as the Arabic preview.
            ViewGroup.LayoutParams translationParams = ayahText.getLayoutParams();
            if (translationParams != null && translationParams.width != islamicMaxWidth) {
                translationParams.width = islamicMaxWidth;
                ayahText.setLayoutParams(translationParams);
            }
            ayahText.setTextDirection(View.TEXT_DIRECTION_LTR);
            ayahText.setGravity(Gravity.START);
            ayahText.setMaxWidth(islamicMaxWidth);
            ayahText.setSingleLine(false);
            ayahText.setMaxLines(2);
            ayahText.setEllipsize(TextUtils.TruncateAt.END);
            ayahText.setBreakStrategy(android.text.Layout.BREAK_STRATEGY_HIGH_QUALITY);
            ayahText.setHyphenationFrequency(android.text.Layout.HYPHENATION_FREQUENCY_NONE);
            ayahText.setLineSpacing(0, 1.04f);
            String tr = a.translation(AppLanguage.getLanguage(this));
            ayahText.setText(tr != null ? tr.trim() : "");
            ayahText.setTextSize(TypedValue.COMPLEX_UNIT_SP, is4K ? 15f : 14f);
            ayahText.setVisibility(tr != null && !tr.trim().isEmpty() ? View.VISIBLE : View.GONE);
        }
        ayahBlock.setVisibility(View.VISIBLE);
    }

    /**
     * Opens the complete daily verse in a centered, bounded glass reader. The Home shelf remains a
     * deliberately short preview, so verse length cannot resize or displace the prayer timetable.
     */
    private void showDailyAyahDialog() {
        if (isFinishing() || isDestroyed()) return;
        View originFocus = getCurrentFocus();
        WeeklyAyah.Ayah ayah = WeeklyAyah.last(this);
        if (ayah == null || ayah.arabic == null || ayah.arabic.trim().isEmpty()) {
            TvFeedback.show(this, R.string.no_daily_ayah, Toast.LENGTH_SHORT);
            return;
        }

        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setGravity(Gravity.CENTER_HORIZONTAL);
        panel.setPadding(Math.round(24 * density), Math.round(18 * density),
                Math.round(24 * density), Math.round(18 * density));
        panel.setClipToPadding(false);
        panel.setClipChildren(false);

        GradientDrawable glassSurface = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{0xE62B3B50, 0xD91B2638, 0xE6222D40});
        glassSurface.setCornerRadius(24 * density);
        glassSurface.setStroke(Math.max(1, Math.round(1.5f * density)), 0x77FFFFFF);
        panel.setBackground(glassSurface);

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout heading = new LinearLayout(this);
        heading.setOrientation(LinearLayout.VERTICAL);
        TextView title = new TextView(this);
        title.setText(R.string.daily_ayah_popup_title);
        title.setTextColor(0xFFFFFFFF);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
        title.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        title.setShadowLayer(3 * density, 0, 1 * density, 0xCC000000);
        heading.addView(title);

        TextView reference = new TextView(this);
        reference.setText(ayah.reference != null ? ayah.reference : "");
        reference.setTextColor(0xD9FFFFFF);
        reference.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f);
        reference.setTypeface(getResources().getFont(R.font.inter), Typeface.NORMAL);
        LinearLayout.LayoutParams refLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        refLp.topMargin = Math.round(3 * density);
        heading.addView(reference, refLp);
        header.addView(heading, new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        AlertDialog[] dialogHolder = new AlertDialog[1];
        TextView close = new TextView(this);
        close.setText("×");
        close.setTextColor(0xFFFFFFFF);
        close.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f);
        close.setGravity(Gravity.CENTER);
        close.setFocusable(true);
        close.setClickable(true);
        close.setContentDescription(getString(R.string.daily_ayah_close));
        close.setBackground(TvFocusIndicator.createOvalStateBackground(
                density, 0x33FFFFFF, TvUiTokens.FOCUS_HALO,
                0x66DCE5ED, TvUiTokens.FOCUS_BORDER));
        close.setOnClickListener(v -> {
            SoundEffects.playSelect();
            if (dialogHolder[0] != null) dialogHolder[0].dismiss();
        });
        close.setOnFocusChangeListener((v, focused) -> {
            if (focused) SoundEffects.playNav();
        });
        header.addView(close, new LinearLayout.LayoutParams(
                Math.round(46 * density), Math.round(46 * density)));
        panel.addView(header);

        View divider = new View(this);
        divider.setBackgroundColor(0x55FFFFFF);
        LinearLayout.LayoutParams dividerLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, Math.max(1, Math.round(density)));
        dividerLp.topMargin = Math.round(12 * density);
        dividerLp.bottomMargin = Math.round(12 * density);
        panel.addView(divider, dividerLp);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);
        scroll.setVerticalScrollBarEnabled(true);
        scroll.setFocusable(true);
        scroll.setFocusableInTouchMode(true);
        scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        LinearLayout body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView arabic = new TextView(this);
        arabic.setText(ayah.arabic.trim());
        arabic.setTextColor(TvUiTokens.TEXT_PRIMARY);
        arabic.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                getResources().getDisplayMetrics().widthPixels >= 3840 ? 34f : 28f);
        arabic.setTypeface(getResources().getFont(R.font.amiri_quran));
        arabic.setTextDirection(View.TEXT_DIRECTION_RTL);
        arabic.setGravity(Gravity.END);
        arabic.setIncludeFontPadding(true);
        arabic.setBreakStrategy(android.text.Layout.BREAK_STRATEGY_HIGH_QUALITY);
        arabic.setHyphenationFrequency(android.text.Layout.HYPHENATION_FREQUENCY_NONE);
        arabic.setLineSpacing(Math.round(7 * density), 1.16f);
        arabic.setMaxLines(Integer.MAX_VALUE);
        arabic.setEllipsize(null);
        body.addView(arabic, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView meaningLabel = new TextView(this);
        meaningLabel.setText(R.string.daily_ayah_meaning_label);
        meaningLabel.setTextColor(TvUiTokens.TEXT_SECONDARY);
        meaningLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f);
        meaningLabel.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        LinearLayout.LayoutParams meaningLabelLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        meaningLabelLp.topMargin = Math.round(18 * density);
        body.addView(meaningLabel, meaningLabelLp);

        TextView translation = new TextView(this);
        String translated = ayah.translation(AppLanguage.getLanguage(this));
        translation.setText(translated != null && !translated.trim().isEmpty()
                ? translated.trim() : getString(R.string.daily_ayah_translation_unavailable));
        translation.setTextColor(0xF2FFFFFF);
        translation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        translation.setTypeface(getResources().getFont(R.font.inter), Typeface.NORMAL);
        translation.setTextDirection(View.TEXT_DIRECTION_LTR);
        translation.setGravity(Gravity.START);
        translation.setLineSpacing(Math.round(5 * density), 1.18f);
        translation.setMaxLines(Integer.MAX_VALUE);
        translation.setEllipsize(null);
        LinearLayout.LayoutParams translationLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        translationLp.topMargin = Math.round(6 * density);
        body.addView(translation, translationLp);

        TextView footerHint = new TextView(this);
        footerHint.setText(R.string.daily_ayah_navigation_hint);
        footerHint.setTextColor(0xBFFFFFFF);
        footerHint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
        footerHint.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams hintLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        hintLp.topMargin = Math.round(10 * density);
        body.addView(footerHint, hintLp);

        scroll.addView(body, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        panel.addView(scroll, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        LinearLayout.LayoutParams closeLp = new LinearLayout.LayoutParams(
                Math.round(150 * density), Math.round(42 * density));
        closeLp.gravity = Gravity.CENTER_HORIZONTAL;
        closeLp.topMargin = Math.round(12 * density);
        TextView closeAction = new TextView(this);
        closeAction.setText(R.string.daily_ayah_close);
        closeAction.setTextColor(0xFFFFFFFF);
        closeAction.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        closeAction.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        closeAction.setGravity(Gravity.CENTER);
        closeAction.setFocusable(true);
        closeAction.setClickable(true);
        closeAction.setBackground(TvFocusIndicator.createRoundedStateBackground(
                density, 10f, 0x223F4A55, TvUiTokens.FOCUS_HALO,
                0x55DCE5ED, TvUiTokens.FOCUS_BORDER));
        closeAction.setOnClickListener(v -> dialogHolder[0].dismiss());
        closeAction.setOnFocusChangeListener((v, focused) -> {
            if (focused) SoundEffects.playNav();
        });
        panel.addView(closeAction, closeLp);

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog)
                .setView(panel)
                .create();
        dialogHolder[0] = dialog;
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnDismissListener(d -> restoreFocusTo(originFocus));
        dialog.setOnKeyListener((d, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN) return false;
            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) {
                d.dismiss();
                return true;
            }
            return false;
        });
        dialog.show();

        int widthDp = Math.max(360, Math.round(
                getResources().getDisplayMetrics().widthPixels * 0.72f / density));
        int heightDp = Math.max(240, Math.round(
                getResources().getDisplayMetrics().heightPixels * 0.63f / density));
        TvDialogStyle.applyCustom(dialog, this, widthDp, heightDp, 0.30f);

        scroll.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN) return false;
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                if (scroll.canScrollVertically(1)) {
                    scroll.smoothScrollBy(0, Math.max(1, scroll.getHeight() * 2 / 3));
                } else {
                    closeAction.requestFocus();
                }
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                if (scroll.canScrollVertically(-1)) {
                    scroll.smoothScrollBy(0, -Math.max(1, scroll.getHeight() * 2 / 3));
                } else {
                    close.requestFocus();
                }
                return true;
            }
            return false;
        });
        close.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                scroll.requestFocus();
                return true;
            }
            return false;
        });
        closeAction.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                scroll.requestFocus();
                return true;
            }
            return false;
        });
        scroll.requestFocus();
    }

    private void updateDateAndPeriod() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        String period = AppLanguage.getPeriodString(this, hour);

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        String lang = AppLanguage.getLanguage(this);
        String[] months;
        switch (lang) {
            case AppLanguage.LANG_EN:
                months = new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
                break;
            case AppLanguage.LANG_ID:
                months = new String[]{"JAN", "FEB", "MAR", "APR", "MEI", "JUN", "JUL", "AGU", "SEP", "OKT", "NOV", "DES"};
                break;
            case AppLanguage.LANG_MS:
            default:
                months = new String[]{"JAN", "FEB", "MAC", "APR", "MEI", "JUN", "JUL", "OGO", "SEP", "OKT", "NOV", "DIS"};
                break;
        }
        String mStr = (month >= 0 && month < 12) ? months[month] : "";
        String dateStr = day + " " + mStr + " " + year;

        if (ampmView != null) ampmView.setText(period);
        if (dateView != null) dateView.setText(dateStr);
    }

    /** Shows the Wi-Fi network name on the button when we can read it, otherwise the connection type. */
    /** Reads network state independently of the optional top-bar network pill. */
    String getGlobalSettingsNetworkSummary() {
        return getQuickSettingsNetworkSummary();
    }

    private String getQuickSettingsNetworkSummary() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean online = false;
            boolean wifi = false;
            boolean ethernet = false;
            for (Network network : cm != null ? cm.getAllNetworks() : new Network[0]) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
                if (capabilities == null
                        || !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                    continue;
                }
                online = true;
                wifi |= capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
                ethernet |= capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
            }
            if (!online) return getString(R.string.offline);
            if (ethernet) return getString(R.string.ethernet);
            if (wifi) {
                WifiManager manager = (WifiManager) getApplicationContext()
                        .getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager != null ? manager.getConnectionInfo() : null;
                String ssid = info != null ? info.getSSID() : null;
                if (ssid != null) {
                    ssid = ssid.replace("\\", "").replace("\"", "").trim();
                    if (!ssid.isEmpty() && !ssid.toLowerCase(Locale.ROOT).contains("unknown")
                            && !ssid.equals("0x")) {
                        return ssid;
                    }
                }
            }
            return getString(R.string.connected);
        } catch (Exception unavailable) {
            return getString(R.string.quick_settings_wifi_summary);
        }
    }

    private void updateWifiLabel() {
        WifiSettingsDialog.onNetworkStateChanged();
        ModernSettingsDialog.refreshNetworkStatus(this);
        if (btnWifi.getVisibility() == View.GONE) return;   // show_network_pill off
        String text = getString(R.string.wifi);
        boolean vpn = false, online = false;
        int icon = R.drawable.ic_wifi;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean wifi = false, ethernet = false;
            // All networks, not just the active one: with a VPN up the active network is the VPN itself.
            for (Network net : cm != null ? cm.getAllNetworks() : new Network[0]) {
                NetworkCapabilities caps = cm.getNetworkCapabilities(net);
                if (caps == null) continue;
                if (caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                    vpn = true;
                    continue;
                }
                if (!caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) continue;
                online = true;
                if (caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) ethernet = true;
                if (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) wifi = true;
            }
            if (!online) {
                text = getString(R.string.offline);
                vpn = false;
            } else if (ethernet) {
                text = getString(R.string.ethernet);
                icon = R.drawable.ic_ethernet;
            } else if (wifi) {
                WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wm != null ? wm.getConnectionInfo() : null;
                String ssid = info != null ? info.getSSID() : null;
                if (ssid != null) {
                    ssid = ssid.replace("\"", "").trim();
                    if (!ssid.isEmpty() && !ssid.contains("unknown") && !ssid.equals("0x")) text = ssid;
                }
            }
        } catch (Exception ignored) {
        }
        btnWifi.setText(text);
        btnWifi.setCompoundDrawablesRelativeWithIntrinsicBounds(icon, 0, 0, 0);
    }

    /** Shows an Apple TV-style frosted glass Wi-Fi control center popup. */
    private void showWifiPopup() {
        showWifiPopup(null);
    }

    /** Optional parent return keeps nested Quick Settings navigation deterministic. */
    private void showWifiPopup(Runnable returnToParent) {
        if (openMenu != null || isFinishing() || isDestroyed()) return;
        SoundEffects.playSelect();
        View originFocus = getCurrentFocus();
        Runnable onReturn = returnToParent != null ? returnToParent : () -> restoreFocusTo(originFocus);
        WifiSettingsDialog.show(this, () -> {
            if (!tryOpen(systemIntent(Settings.ACTION_WIFI_SETTINGS))) {
                WifiSettingsDialog.onExternalSettingsUnavailable();
            }
        }, onReturn);
    }

    /** Restores the exact Home control that launched a modal or popup. */
    private void restoreFocusTo(View origin) {
        if (origin == null) return;
        origin.post(() -> {
            if (!isFinishing() && !isDestroyed() && origin.isAttachedToWindow()
                    && origin.isShown() && origin.isFocusable()) {
                origin.requestFocus();
            }
        });
    }

    /** Shows the User Profile & Child Mode quick dialog (formerly Candy Box). */
    public void showCandyBoxPopup() {
        if (openMenu != null) {
            closeMenu(false);
        }
        SoundEffects.playSelect();
        View originFocus = getCurrentFocus();
        UserSwitchDialog.show(this, () -> restoreFocusTo(originFocus));
    }

    void updateTopBarUserIcon() {
        if (btnCandyBox instanceof TextView) {
            TextView tv = (TextView) btnCandyBox;
            ProfileManager.UserProfile active = ProfileManager.getActiveProfile(this);
            boolean isChild = ChildModeManager.isChildModeEnabled(this);
            int size = Math.round(26 * density);
            android.graphics.drawable.Drawable avDrawable = ProfileManager.getAvatarDrawable(this, active, size, isChild);
            if (avDrawable != null) {
                tv.setCompoundDrawablesWithIntrinsicBounds(avDrawable, null, null, null);
                tv.setCompoundDrawableTintList(null);
            } else {
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_shield_child, 0, 0, 0);
                tv.setCompoundDrawableTintList(android.content.res.ColorStateList.valueOf(isChild ? 0xFFF59E0B : 0xFFFFFFFF));
            }
            tv.setContentDescription(getString(R.string.current_user_content_description, active.name, isChild ? getString(R.string.child_mode_suffix) : ""));
        }
    }

    /** Opens the centered V04 Quick Settings modal using the shared G2 dialog/focus contract. */
    private void showQuickControlPopup() {
        showQuickControlPopup(QuickSettingsFocusGraph.WIFI);
    }

    private void showQuickControlPopup(int initialFocus) {
        if (isFinishing() || isDestroyed()) return;
        if (openMenu != null) closeMenu(false);
        SoundEffects.playSelect();
        View anchor = findViewById(R.id.btnSettings);
        if (anchor == null) return;

        QuickSettingsDialog.show(this, new QuickSettingsDialog.Actions() {
            @Override public String wifiSummary() {
                return getQuickSettingsNetworkSummary();
            }
            @Override public String languageSummary() {
                return AppLanguage.getLanguageBadge(MainActivity.this);
            }
            @Override public String powerTitle() {
                return AppLanguage.strTurnOffTitle(MainActivity.this);
            }
            @Override public String cacheTitle() {
                return AppLanguage.strCleanCacheTitle(MainActivity.this);
            }
            @Override public String wallpaperIntervalSummary() {
                return getWallpaperIntervalSummary();
            }
            @Override public void openWifi() {
                showWifiPopup(() -> showQuickControlPopup(QuickSettingsFocusGraph.WIFI));
            }
            @Override public void restart() {
                showRestartDialog();
            }
            @Override public void chooseLanguage() {
                showLanguageDialog();
            }
            @Override public void openPowerMenu() {
                showTurnOffDialog();
            }
            @Override public void cleanCache() {
                showCleanTvCacheDialog();
            }
            @Override public void nextPhoto() {
                showNextWallpaperFromSettings();
            }
            @Override public void chooseWallpaperInterval() {
                showWallpaperIntervalDialog(false);
            }
            @Override public void manageWallpaper() {
                showWallpaperDialog();
            }
            @Override public void openGlassSettings() {
                showSettingsPopup();
            }
            @Override public void openAndroidSettings() {
                openSystemSettings(systemIntent(Settings.ACTION_SETTINGS));
            }
        }, initialFocus, () -> {
            if (!isFinishing() && !isDestroyed() && anchor.isAttachedToWindow()) {
                anchor.requestFocus();
            }
        });
    }

    public void showSettingsPopup() {
        if (openMenu != null) {
            closeMenu(false);
        }
        ModernSettingsDialog.show(this);
    }

    void showDockOrientationDialog() {
        DockPreferences current = DockPreferences.load(this);
        String[] options = {
                getString(R.string.dock_orientation_horizontal),
                getString(R.string.dock_orientation_vertical)
        };
        int selected = current.orientation == DockPreferences.Orientation.VERTICAL ? 1 : 0;
        new android.app.AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog)
                .setTitle(R.string.dock_orientation_title)
                .setSingleChoiceItems(options, selected, (dialog, which) -> {
                    DockPreferences.Orientation orientation = which == 1
                            ? DockPreferences.Orientation.VERTICAL : DockPreferences.Orientation.HORIZONTAL;
                    if (!DockPreferences.save(this, orientation, current.capacity)) {
                        TvFeedback.show(this, R.string.dock_settings_save_failed, Toast.LENGTH_SHORT);
                        return;
                    }
                    dialog.dismiss();
                    applyDockPreferenceChange();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    void showDockCapacityDialog() {
        DockPreferences current = DockPreferences.load(this);
        String[] options = {"4", "5", "6"};
        int selected = current.capacity == 4 ? 0 : current.capacity == 6 ? 2 : 1;
        new android.app.AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog)
                .setTitle(R.string.dock_capacity_title)
                .setSingleChoiceItems(options, selected, (dialog, which) -> {
                    int capacity = which == 0 ? 4 : which == 2 ? 6 : 5;
                    if (!DockPreferences.save(this, current.orientation, capacity)) {
                        TvFeedback.show(this, R.string.dock_settings_save_failed, Toast.LENGTH_SHORT);
                        return;
                    }
                    dialog.dismiss();
                    applyDockPreferenceChange();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void applyDockPreferenceChange() {
        if (adapter != null) adapter.invalidateDockSnapshot();
        if (dockAdapter != null) dockAdapter.refresh();
        applyDockLayout();
        // The settings surface is modal. Re-layout Home underneath it without stealing focus
        // from the active settings row; the persisted tile identity remains the return target.
        if (gridWrap != null) gridWrap.post(this::applyDockLayout);
    }

    void showSoundEffectsDialog() {
        final String[] soundOptions = new String[]{
                getString(R.string.sound_ps5_option),
                getString(R.string.sound_switch_option),
                getString(R.string.sound_xbox_option),
                getString(R.string.sound_off_option)
        };

        final String[] soundKeys = new String[]{
                SoundEffects.THEME_PS5,
                SoundEffects.THEME_SWITCH,
                SoundEffects.THEME_XBOX,
                SoundEffects.THEME_OFF
        };

        String currentTheme = SoundEffects.getActiveTheme(this);
        int selectedIndex = 0;
        for (int i = 0; i < soundKeys.length; i++) {
            if (soundKeys[i].equalsIgnoreCase(currentTheme)) {
                selectedIndex = i;
                break;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setTitle(R.string.sound_effects_title);
        builder.setSingleChoiceItems(soundOptions, selectedIndex, (dialog, which) -> {
            dialog.dismiss();
            String chosenKey = soundKeys[which];
            SoundEffects.setTheme(MainActivity.this, chosenKey);
            String displayName = SoundEffects.getThemeDisplayName(chosenKey, this);

            if (SoundEffects.THEME_OFF.equalsIgnoreCase(chosenKey)) {
                TvFeedback.show(MainActivity.this, R.string.sound_effects_off, Toast.LENGTH_SHORT);
            } else {
                TvFeedback.show(MainActivity.this, getString(R.string.sound_effects_selected, displayName), Toast.LENGTH_SHORT);
                // Play sample of the selected theme
                handler.postDelayed(() -> SoundEffects.playSelect(), 120);
            }
        });
        builder.setNegativeButton(R.string.close_label, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        styleGlassDialog(dialog);
    }

    void showLanguageDialog() {
        SoundEffects.playSelect();
        final String[] langOptions = new String[]{
                "🇮🇩 Bahasa Indonesia",
                "🇬🇧 English"
        };
        final String[] langKeys = new String[]{
                AppLanguage.LANG_ID,
                AppLanguage.LANG_EN
        };

        String currentLang = AppLanguage.getLanguage(this);
        int selectedIndex = 0;
        for (int i = 0; i < langKeys.length; i++) {
            if (langKeys[i].equalsIgnoreCase(currentLang)) {
                selectedIndex = i;
                break;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setTitle("🌐 " + AppLanguage.strLanguage(this));
        builder.setSingleChoiceItems(langOptions, selectedIndex, (dialog, which) -> {
            dialog.dismiss();
            String chosenLang = langKeys[which];
            boolean languageChanged = !chosenLang.equalsIgnoreCase(currentLang);
            if (languageChanged && getIntent() != null) {
                // Reopen Settings after recreation; its selected tab and sidebar cursor are persisted by B01.
                getIntent().putExtra("open_settings", true);
            }
            AppLanguage.setLanguage(MainActivity.this, chosenLang);
            WeeklyAyah.setEnglish(MainActivity.this, AppLanguage.LANG_EN.equals(chosenLang));
            WeeklyAyah.clearCache(MainActivity.this);
            TvFeedback.show(MainActivity.this, AppLanguage.strLanguageChangedToast(MainActivity.this, chosenLang), Toast.LENGTH_SHORT);
            if (languageChanged && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                recreate();
            }
        });
        builder.setNegativeButton(AppLanguage.strClose(this), (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        styleGlassDialog(dialog);
    }

    void showAzanSettingsDialog() {
        SoundEffects.playSelect();
        Location.Zone chosenPlace = Location.chosen(this);
        String zoneCode = (chosenPlace != null) ? chosenPlace.code : "WLY01";
        java.util.Calendar cal = java.util.Calendar.getInstance();
        PrayerTimes.Day todayTimes = PrayerTimes.day(this, zoneCode, cal);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        AlertDialog[] dialogHolder = new AlertDialog[1];

        LinearLayout rootContainer = new LinearLayout(this);
        rootContainer.setOrientation(LinearLayout.VERTICAL);
        rootContainer.setPadding((int) (22 * density), (int) (16 * density), (int) (22 * density), (int) (16 * density));

        // Header Title Row
        LinearLayout headerRow = new LinearLayout(this);
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout titleCol = new LinearLayout(this);
        titleCol.setOrientation(LinearLayout.VERTICAL);

        TextView titleView = new TextView(this);
        titleView.setText(AppLanguage.strAzanSettingsHeaderTitle(this));
        titleView.setTextSize(19);
        titleView.setTextColor(0xFFFFFFFF);
        titleView.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        titleView.setFontVariationSettings("'wght' 700");
        titleView.setShadowLayer(4 * density, 0, 1 * density, 0xDD000000);
        titleCol.addView(titleView);

        TextView subView = new TextView(this);
        subView.setText(AppLanguage.strAzanSettingsHeaderSub(this));
        subView.setTextSize(12.5f);
        subView.setTextColor(0xD9FFFFFF);
        subView.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        subView.setFontVariationSettings("'wght' 600");
        subView.setShadowLayer(3 * density, 0, 1 * density, 0xCC000000);
        subView.setPadding(0, (int) (2 * density), 0, 0);
        titleCol.addView(subView);

        headerRow.addView(titleCol, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        rootContainer.addView(headerRow);

        // Gap below header
        View headerDivider = new View(this);
        headerDivider.setBackgroundColor(0x22FFFFFF);
        LinearLayout.LayoutParams divLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.max(1, (int) (1 * density)));
        divLp.topMargin = (int) (8 * density);
        divLp.bottomMargin = (int) (8 * density);
        rootContainer.addView(headerDivider, divLp);

        // Content ScrollView with 2 balanced columns (16:9 widescreen layout)
        android.widget.ScrollView scroll = new android.widget.ScrollView(this);
        scroll.setVerticalScrollBarEnabled(false);
        scroll.setFillViewport(true);

        LinearLayout twoCols = new LinearLayout(this);
        twoCols.setOrientation(LinearLayout.HORIZONTAL);

        // --- LEFT COLUMN: A (Suis Utama) & B (Pilihan Waktu Solat) ---
        LinearLayout leftCol = new LinearLayout(this);
        leftCol.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams leftLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.15f);
        leftLp.rightMargin = (int) (12 * density);
        leftCol.setLayoutParams(leftLp);

        // Bahagian A: Suis Utama
        leftCol.addView(createDialogSectionHeader(AppLanguage.strAzanSectionMaster(this)));
        final TextView masterStatusTag = new TextView(this);
        LinearLayout masterRow = createDialogRow(
                AppLanguage.strAzanMasterTitle(this),
                AppLanguage.strAzanMasterDesc(this),
                masterStatusTag,
                null,
                () -> {
                    boolean nowOn = !Azan.isMasterEnabled(MainActivity.this);
                    Azan.setMasterEnabled(MainActivity.this, nowOn);
                    Azan.schedule(MainActivity.this, zoneCode);
                    SoundEffects.playSelect();
                    updateMasterTag(masterStatusTag, nowOn);
                    TvFeedback.show(MainActivity.this, nowOn ? R.string.azan_popup_enabled : R.string.azan_popup_disabled, Toast.LENGTH_SHORT);
                }
        );
        updateMasterTag(masterStatusTag, Azan.isMasterEnabled(this));
        leftCol.addView(masterRow);

        // Bahagian B: Pilihan Waktu Solat
        leftCol.addView(createDialogSectionHeader(AppLanguage.strAzanSectionPrayers(this)));
        int[] prayers = {0, 2, 3, 4, 5};
        String[] localizedPrayerNames = AppLanguage.getPrayerNames(this);
        String[] prayerIcons = {"🌅 ", "☀️ ", "🌤️ ", "🌇 ", "🌙 "};

        for (int i = 0; i < prayers.length; i++) {
            final int pIdx = prayers[i];
            String pName = prayerIcons[i] + localizedPrayerNames[pIdx];
            String pTimeStr = "";
            if (todayTimes != null && pIdx < todayTimes.minutes.length) {
                int min = todayTimes.minutes[pIdx];
                pTimeStr = String.format(Locale.ROOT, " • %02d:%02d", min / 60, min % 60);
            }

            final TextView prayerTag = new TextView(this);
            LinearLayout prayerRow = createDialogRow(
                    pName + pTimeStr,
                    null,
                    prayerTag,
                    null,
                    () -> {
                        boolean current = Azan.on(MainActivity.this, pIdx);
                        Azan.set(MainActivity.this, pIdx, !current);
                        Azan.schedule(MainActivity.this, zoneCode);
                        SoundEffects.playSelect();
                        updatePrayerTag(prayerTag, !current);
                    }
            );
            updatePrayerTag(prayerTag, Azan.on(this, pIdx));
            leftCol.addView(prayerRow);
        }
        twoCols.addView(leftCol);

        // Subtle vertical divider between columns
        View colDivider = new View(this);
        colDivider.setBackgroundColor(0x1FFFFFFF);
        LinearLayout.LayoutParams colDivLp = new LinearLayout.LayoutParams(Math.max(1, (int) (1 * density)), ViewGroup.LayoutParams.MATCH_PARENT);
        colDivLp.leftMargin = (int) (4 * density);
        colDivLp.rightMargin = (int) (12 * density);
        twoCols.addView(colDivider, colDivLp);

        // --- RIGHT COLUMN: C (Audio), D (Uji Popup), Panduan & Tutup ---
        LinearLayout rightCol = new LinearLayout(this);
        rightCol.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams rightLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        rightCol.setLayoutParams(rightLp);

        // Bahagian C: Audio
        rightCol.addView(createDialogSectionHeader(AppLanguage.strAzanSectionAudio(this)));
        final TextView soundTag = new TextView(this);
        final TextView[] soundSubHolder = new TextView[1];
        LinearLayout soundRow = createDialogRow(
                AppLanguage.strAzanAudioCurrent(this),
                Azan.soundLabel(Azan.getSoundChoice(this)),
                soundTag,
                soundSubHolder,
                () -> {
                    SoundEffects.playSelect();
                    showAzanSoundChoiceSubDialog(() -> {
                        if (soundSubHolder[0] != null) {
                            soundSubHolder[0].setText(Azan.soundLabel(Azan.getSoundChoice(MainActivity.this)));
                        }
                    });
                }
        );
        soundTag.setText(AppLanguage.strAzanAudioChangeBtn(this));
        soundTag.setTextSize(12.5f);
        soundTag.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        soundTag.setFontVariationSettings("'wght' 700");
        soundTag.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_focused}, {}}, new int[]{TvUiTokens.TEXT_PRIMARY, TvUiTokens.ACCENT_CYAN}));
        rightCol.addView(soundRow);

        // Bahagian D: Ujian Azan
        rightCol.addView(createDialogSectionHeader(AppLanguage.strAzanSectionTest(this)));
        final TextView testTag = new TextView(this);
        LinearLayout testRow = createDialogRow(
                AppLanguage.strAzanTestTitle(this),
                AppLanguage.strAzanTestDesc(this),
                testTag,
                null,
                () -> {
                    SoundEffects.playSelect();
                    if (dialogHolder[0] != null) dialogHolder[0].dismiss();
                    Intent azanIntent = new Intent(MainActivity.this, AzanActivity.class);
                    azanIntent.putExtra("prayer", 4);
                    azanIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(azanIntent);
                }
        );
        testTag.setText(AppLanguage.strAzanTestBtn(this));
        testTag.setTextSize(13);
        testTag.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        testTag.setFontVariationSettings("'wght' 700");
        testTag.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_focused}, {}}, new int[]{TvUiTokens.TEXT_PRIMARY, TvUiTokens.ACCENT_CYAN}));
        rightCol.addView(testRow);

        // Info Card
        LinearLayout infoCard = new LinearLayout(this);
        infoCard.setOrientation(LinearLayout.VERTICAL);
        infoCard.setPadding((int) (12 * density), (int) (8 * density), (int) (12 * density), (int) (8 * density));
        GradientDrawable infoBg = new GradientDrawable();
        infoBg.setColor(0x12FFFFFF);
        infoBg.setCornerRadius(9 * density);
        infoBg.setStroke(Math.max(1, (int) (1 * density)), 0x22FFFFFF);
        infoCard.setBackground(infoBg);

        TextView infoText = new TextView(this);
        infoText.setText(AppLanguage.strAzanInfoText(this));
        infoText.setTextSize(11f);
        infoText.setTextColor(0xB3FFFFFF);
        infoText.setTypeface(getResources().getFont(R.font.inter), Typeface.NORMAL);
        infoText.setFontVariationSettings("'wght' 500");
        infoText.setLineSpacing(0, 1.25f);
        infoCard.addView(infoText);

        LinearLayout.LayoutParams infoLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        infoLp.topMargin = (int) (4 * density);
        infoLp.bottomMargin = (int) (5 * density);
        infoCard.setLayoutParams(infoLp);
        rightCol.addView(infoCard);

        // Butang Tutup / Selesai
        final TextView closeTag = new TextView(this);
        LinearLayout closeRow = createDialogRow(
                AppLanguage.strAzanCloseBtn(this),
                null,
                closeTag,
                null,
                () -> {
                    SoundEffects.playSelect();
                    if (dialogHolder[0] != null) dialogHolder[0].dismiss();
                }
        );
        closeTag.setText(AppLanguage.strClose(this).toUpperCase(Locale.ROOT));
        closeTag.setTextSize(12.5f);
        closeTag.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        closeTag.setFontVariationSettings("'wght' 700");
        closeTag.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_focused}, {}}, new int[]{TvUiTokens.TEXT_PRIMARY, TvUiTokens.TEXT_SECONDARY}));
        rightCol.addView(closeRow);

        twoCols.addView(rightCol);
        scroll.addView(twoCols);
        rootContainer.addView(scroll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        builder.setView(rootContainer);
        AlertDialog dialog = builder.create();
        dialogHolder[0] = dialog;
        dialog.show();
        styleGlassDialog(dialog);

        // Set true 16:9 popup layout sizing with generous top & bottom margins
        Window window = dialog.getWindow();
        if (window != null) {
            android.util.DisplayMetrics dm = getResources().getDisplayMetrics();
            int screenH = dm.heightPixels;
            int screenW = dm.widthPixels;

            // Target height: ~70% of screen height in 16:9 (e.g. 756px on 1080p, leaving 162px top & bottom margin)
            int targetH = Math.round(screenH * 0.70f / 9f) * 9;
            int targetW = targetH * 16 / 9;
            if (targetW > (int) (screenW * 0.85f)) {
                targetW = Math.round((screenW * 0.85f) / 16f) * 16;
                targetH = targetW * 9 / 16;
            }
            window.setLayout(targetW, targetH);
            window.setGravity(Gravity.CENTER);
        }
    }

    private void showAzanSoundChoiceSubDialog(Runnable onUpdated) {
        final String[] soundKeys = new String[]{
                Azan.SOUND_MAKKAH,
                Azan.SOUND_MADINAH,
                Azan.SOUND_KLCC,
                Azan.SOUND_CHIME,
                Azan.SOUND_SILENT
        };

        final String[] soundOptions = AppLanguage.strAzanSoundOptions(this);

        String currentSound = Azan.getSoundChoice(this);
        int selectedIndex = 0;
        for (int i = 0; i < soundKeys.length; i++) {
            if (soundKeys[i].equalsIgnoreCase(currentSound)) {
                selectedIndex = i;
                break;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setTitle(AppLanguage.strAzanChooseAudioTitle(this));
        builder.setSingleChoiceItems(soundOptions, selectedIndex, (dialog, which) -> {
            dialog.dismiss();
            String chosen = soundKeys[which];
            Azan.setSoundChoice(MainActivity.this, chosen);
            SoundEffects.playSelect();
            Azan.fetch(MainActivity.this, chosen);
            TvFeedback.show(MainActivity.this, getString(R.string.azan_audio_selected, Azan.soundLabel(chosen)), Toast.LENGTH_SHORT);
            if (onUpdated != null) onUpdated.run();
        });
        builder.setNegativeButton(AppLanguage.strBack(this), (dialog, which) -> dialog.dismiss());
        AlertDialog d = builder.create();
        d.show();
        styleGlassDialog(d);

        if (d.getWindow() != null) {
            android.util.DisplayMetrics dm = getResources().getDisplayMetrics();
            int w = Math.min((int) (540 * density), (int) (dm.widthPixels * 0.75f));
            int h = Math.min((int) (400 * density), (int) (dm.heightPixels * 0.72f));
            d.getWindow().setLayout(w, h);
            d.getWindow().setGravity(Gravity.CENTER);
        }
    }

    private void updateMasterTag(TextView tag, boolean on) {
        tag.setText(on ? "ON" : "OFF");
        tag.setTextSize(13);
        tag.setTypeface(getResources().getFont(R.font.inter), Typeface.NORMAL);
        tag.setFontVariationSettings(on ? "'wght' 700" : "'wght' 600");
        int color = on ? TvUiTokens.ACCENT_CYAN : TvUiTokens.TEXT_SECONDARY;
        tag.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_focused}, {}}, new int[]{TvUiTokens.TEXT_PRIMARY, color}));
    }

    private void updatePrayerTag(TextView tag, boolean on) {
        tag.setText(on ? "ON" : "OFF");
        tag.setTextSize(13);
        tag.setTypeface(getResources().getFont(R.font.inter), Typeface.NORMAL);
        tag.setFontVariationSettings(on ? "'wght' 700" : "'wght' 600");
        int color = on ? TvUiTokens.ACCENT_CYAN : TvUiTokens.TEXT_SECONDARY;
        tag.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_focused}, {}}, new int[]{TvUiTokens.TEXT_PRIMARY, color}));
    }

    private TextView createDialogSectionHeader(String title) {
        TextView tv = new TextView(this);
        tv.setText(title);
        tv.setTextSize(11.5f);
        tv.setTextColor(TvUiTokens.ACCENT_SECTION);
        tv.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        tv.setFontVariationSettings("'wght' 700");
        tv.setLetterSpacing(0.08f);
        tv.setPadding(0, (int) (6 * density), 0, (int) (3 * density));
        return tv;
    }

    private LinearLayout createDialogRow(String title, String subtitle, TextView tagView, TextView[] subHolderOut, Runnable onClick) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setFocusable(true);
        row.setClickable(true);
        boolean hasSub = (subtitle != null && !subtitle.isEmpty());
        int vPad = hasSub ? (int) (8 * density) : (int) (5 * density);
        row.setPadding((int) (14 * density), vPad, (int) (14 * density), vPad);

        StateListDrawable bg = new StateListDrawable();
        GradientDrawable on = new GradientDrawable();
        on.setColor(TvUiTokens.SURFACE_FOCUSED);
        on.setCornerRadius(9 * density);
        on.setStroke(Math.max(1, (int) (2 * density)), TvUiTokens.FOCUS_BORDER);
        GradientDrawable off = new GradientDrawable();
        off.setColor(0x223F4B56);
        off.setCornerRadius(9 * density);
        off.setStroke(Math.max(1, (int) density), 0x55DCE5ED);
        bg.addState(new int[]{android.R.attr.state_focused}, on);
        bg.addState(new int[]{}, off);
        row.setBackground(bg);

        LinearLayout topRow = new LinearLayout(this);
        topRow.setOrientation(LinearLayout.HORIZONTAL);
        topRow.setGravity(Gravity.CENTER_VERTICAL);
        topRow.setDuplicateParentStateEnabled(true);

        TextView rowTitle = new TextView(this);
        rowTitle.setText(title);
        rowTitle.setTextSize(15);
        rowTitle.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_focused}, {}}, new int[]{TvUiTokens.TEXT_PRIMARY, TvUiTokens.TEXT_PRIMARY}));
        rowTitle.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        rowTitle.setFontVariationSettings("'wght' 700");
        rowTitle.setShadowLayer(3 * density, 0, 1 * density, 0xCC000000);
        rowTitle.setDuplicateParentStateEnabled(true);
        topRow.addView(rowTitle, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        if (tagView != null) {
            tagView.setDuplicateParentStateEnabled(true);
            topRow.addView(tagView);
        }
        row.addView(topRow);

        final TextView finalRowSub;
        if (subtitle != null && !subtitle.isEmpty()) {
            TextView rowSub = new TextView(this);
            rowSub.setText(subtitle);
            rowSub.setTextSize(12.5f);
            rowSub.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_focused}, {}}, new int[]{TvUiTokens.TEXT_SECONDARY, TvUiTokens.TEXT_SECONDARY}));
            rowSub.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
            rowSub.setFontVariationSettings("'wght' 600");
            rowSub.setShadowLayer(2 * density, 0, 1 * density, 0xAA000000);
            rowSub.setPadding(0, (int) (2 * density), 0, 0);
            rowSub.setDuplicateParentStateEnabled(true);
            row.addView(rowSub);
            finalRowSub = rowSub;
            if (subHolderOut != null) {
                subHolderOut[0] = rowSub;
            }
        } else {
            finalRowSub = null;
        }

        row.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                rowTitle.setShadowLayer(0, 0, 0, 0);
                if (finalRowSub != null) finalRowSub.setShadowLayer(0, 0, 0, 0);
            } else {
                rowTitle.setShadowLayer(3 * density, 0, 1 * density, 0xCC000000);
                if (finalRowSub != null) finalRowSub.setShadowLayer(2 * density, 0, 1 * density, 0xAA000000);
            }
        });

        row.setOnClickListener(v -> {
            if (onClick != null) onClick.run();
        });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = hasSub ? (int) (5 * density) : (int) (3 * density);
        row.setLayoutParams(lp);

        return row;
    }

    void showRestartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setTitle(AppLanguage.strRestartTitle(this));
        builder.setMessage(AppLanguage.strRestartMsg(this));
        builder.setPositiveButton(AppLanguage.strRestartConfirm(this), (dialog, which) -> {
            dialog.dismiss();
            handler.postDelayed(() -> {
                boolean requested = HomeKeyService.restartTv(MainActivity.this);
                String message = requested
                        ? getString(R.string.restart_tv_requested)
                        : getString(R.string.restart_tv_failed);
                TvFeedback.show(MainActivity.this, message, Toast.LENGTH_LONG);
            }, 300);
        });
        builder.setNegativeButton(AppLanguage.strCancel(this), (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        styleGlassDialog(dialog);
        Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (positive != null) positive.requestFocus();
    }

    void showTurnOffDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setTitle(AppLanguage.strTurnOffTitle(this));
        builder.setMessage(AppLanguage.strTurnOffMsg(this));
        builder.setPositiveButton(AppLanguage.strTurnOffConfirm(this), (dialog, which) -> {
            dialog.dismiss();
            boolean requested = HomeKeyService.turnOffTv(MainActivity.this);
            TvFeedback.show(MainActivity.this, requested
                    ? getString(R.string.power_menu_opened)
                    : getString(R.string.power_menu_failed),
                    Toast.LENGTH_LONG);
        });
        builder.setNegativeButton(AppLanguage.strCancel(this), (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        styleGlassDialog(dialog);
        Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (positive != null) positive.requestFocus();
    }

    void showCleanTvCacheDialog() {
        SoundEffects.playSelect();
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setTitle(AppLanguage.strCleanCacheTitle(this));

        long freeBytes = CacheCleaner.getFreeStorageBytes();
        long totalBytes = CacheCleaner.getTotalStorageBytes();
        int usedPct = CacheCleaner.usedPercent();
        String freeStr = android.text.format.Formatter.formatFileSize(this, freeBytes);
        String totalStr = android.text.format.Formatter.formatFileSize(this, totalBytes);

        String msg = AppLanguage.strCleanCacheMsg(this, freeStr, totalStr, 100 - usedPct, usedPct);
        builder.setMessage(msg);

        builder.setPositiveButton(AppLanguage.strCleanCacheStart(this), (dialog, which) -> {
            dialog.dismiss();
            TvFeedback.show(MainActivity.this, AppLanguage.strCleanCacheToast(MainActivity.this), Toast.LENGTH_SHORT);

            java.lang.ref.WeakReference<MainActivity> activityRef =
                    new java.lang.ref.WeakReference<>(MainActivity.this);
            CacheCleaner.cleanNow(MainActivity.this, (bytesFreed, percentBefore, percentAfter, systemTrimmed) -> {
                MainActivity activity = activityRef.get();
                if (activity == null || activity.isFinishing() || activity.isDestroyed() || !activity.hasWindowFocus()) return;

                SoundEffects.playSelect();
                String freedStr = android.text.format.Formatter.formatFileSize(activity, bytesFreed);
                String resultMsg = AppLanguage.strCleanCacheDoneMsg(activity, freedStr, bytesFreed > 0);
                String systemStatus = activity.getString(systemTrimmed
                        ? R.string.cache_system_trim_success : R.string.cache_system_trim_skipped);

                AlertDialog resDialog = new AlertDialog.Builder(activity, R.style.Theme_TvHome_GlassDialog)
                        .setTitle(AppLanguage.strCleanCacheDoneTitle(activity))
                        .setMessage(resultMsg + "\n\n" + systemStatus
                                + AppLanguage.strCleanCacheUsageLine(activity, percentBefore, percentAfter))
                        .setPositiveButton(AppLanguage.strDone(activity), (d, w) -> d.dismiss())
                        .create();
                resDialog.show();
                activity.styleDeviceSettingsPopup(resDialog, 600, 0);
                Button btnOk = resDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                if (btnOk != null) btnOk.requestFocus();
            });
        });
        builder.setNegativeButton(AppLanguage.strCancel(this), (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        styleDeviceSettingsPopup(dialog, 600, 0);
        Button btnPos = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (btnPos != null) btnPos.requestFocus();
    }

    String getAppVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "4.0.0";
        }
    }

    void showManualUpdateDialog() {
        SoundEffects.playSelect();
        View view = getLayoutInflater().inflate(R.layout.dialog_ota_coffee, null);

        TextView tvVersionSubtitle = view.findViewById(R.id.tvVersionSubtitle);
        TextView tvManualStatus = view.findViewById(R.id.tvManualStatus);
        TextView btnCheckNow = view.findViewById(R.id.btnCheckNow);
        TextView btnClose = view.findViewById(R.id.btnClose);
        ImageView imgQrCoffee = view.findViewById(R.id.imgQrCoffee);

        if (tvVersionSubtitle != null) {
            tvVersionSubtitle.setText(getString(R.string.ota_version_subtitle_dynamic, getAppVersionName()));
        }
        if (tvManualStatus != null) {
            tvManualStatus.setText(R.string.ota_manual_update_status);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        View.OnFocusChangeListener focusListener = (v, hasFocus) -> {
            if (hasFocus) SoundEffects.playNav();
        };
        if (btnCheckNow != null) {
            btnCheckNow.setTextColor(TvUiTokens.TEXT_PRIMARY);
            btnCheckNow.setOnFocusChangeListener(focusListener);
            btnCheckNow.setOnClickListener(v -> {
                SoundEffects.playSelect();
                dialog.dismiss();
                AppUpdateManager.checkForUpdates(MainActivity.this, true);
            });
        }
        if (btnClose != null) {
            btnClose.setTextColor(TvUiTokens.TEXT_PRIMARY);
            btnClose.setOnFocusChangeListener(focusListener);
            btnClose.setOnClickListener(v -> {
                SoundEffects.playSelect();
                dialog.dismiss();
            });
        }
        if (imgQrCoffee != null) {
            imgQrCoffee.setOnClickListener(v -> {
                SoundEffects.playSelect();
                TvFeedback.show(this, R.string.coffee_thanks, Toast.LENGTH_SHORT);
            });
        }

        dialog.show();
        styleDeviceSettingsPopup(dialog, 680, 0);
        if (btnCheckNow != null) btnCheckNow.requestFocus();
    }

    private void updateNowPlayingUi(NowPlayingManager.MediaInfo info) {
        if (nowPlayingCard == null || cardTitle == null || cardSubtitle == null) return;

        boolean hasActiveMusic = info != null && info.hasMusic();

        if (!hasActiveMusic) {
            hideNowPlayingCard();
            nowPlayingDisabledByUser = false;
            lastPlayedTrackKey = "";
            return;
        }

        String currentTrackKey = info.packageName + ":" + info.title;
        if (!currentTrackKey.equals(lastPlayedTrackKey)) {
            lastPlayedTrackKey = currentTrackKey;
            nowPlayingDisabledByUser = false; // New song started playing -> show
        }

        if (nowPlayingDisabledByUser) {
            hideNowPlayingCard();
            return;
        }

        if (ayahBlock != null) {
            ayahBlock.setVisibility(View.GONE);
        }

        // Active playback from Spotify or YouTube Music
        cardTitle.setText(info.title.isEmpty() ? getString(R.string.now_playing_fallback_title) : info.title);
        cardTitle.setSelected(true);
        cardSubtitle.setText(getString(R.string.now_playing_subtitle, info.appName.isEmpty() ? "SPOTIFY" : info.appName.toUpperCase(Locale.ROOT)));

        if (info.albumArt != null && cardAlbumArt != null) {
            cardAlbumArt.setImageBitmap(info.albumArt);
            cardAlbumArt.setPadding(0, 0, 0, 0);
        } else if (cardAlbumArt != null) {
            try {
                Drawable appIcon = getPackageManager().getApplicationIcon(info.packageName);
                cardAlbumArt.setImageDrawable(appIcon);
                cardAlbumArt.setPadding(6, 6, 6, 6);
            } catch (Exception e) {
                cardAlbumArt.setImageResource(R.drawable.ic_music);
                cardAlbumArt.setPadding(8, 8, 8, 8);
            }
        }

        if (btnInlinePlayPause != null) {
            btnInlinePlayPause.setImageResource(info.isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
        }

        if (nowPlayingCard.getVisibility() != View.VISIBLE) {
            nowPlayingCard.setVisibility(View.VISIBLE);
            nowPlayingCard.setEnabled(true);
            nowPlayingCard.setAlpha(0f);
            nowPlayingCard.setScaleX(0.9f);
            nowPlayingCard.setScaleY(0.9f);
            nowPlayingCard.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(220)
                    .setInterpolator(new android.view.animation.DecelerateInterpolator())
                    .withLayer()
                    .start();
            if (weatherAirQualityView != null) {
                weatherAirQualityView.setNextFocusDownId(R.id.cardTrackInfo);
            }
        }
    }

    private void disableNowPlaying() {
        nowPlayingDisabledByUser = true;
        NowPlayingManager.pausePlayback();
        hideNowPlayingCard();
    }

    private void hideNowPlayingCard() {
        if (nowPlayingCard == null) return;
        if (nowPlayingCard.getVisibility() != View.GONE) {
            nowPlayingCard.animate()
                    .alpha(0f)
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(180)
                    .setInterpolator(new android.view.animation.DecelerateInterpolator())
                    .withLayer()
                    .withEndAction(() -> {
                        nowPlayingCard.setVisibility(View.GONE);
                        nowPlayingCard.setEnabled(false);
                        if (weatherAirQualityView != null) {
                            weatherAirQualityView.setNextFocusDownId(View.NO_ID);
                        }
                        showAyah(WeeklyAyah.stored(MainActivity.this) != null ? WeeklyAyah.stored(MainActivity.this) : WeeklyAyah.last(MainActivity.this));
                    })
                    .start();
        } else {
            nowPlayingCard.setVisibility(View.GONE);
            nowPlayingCard.setEnabled(false);
            if (weatherAirQualityView != null) {
                weatherAirQualityView.setNextFocusDownId(View.NO_ID);
            }
            showAyah(WeeklyAyah.stored(this) != null ? WeeklyAyah.stored(this) : WeeklyAyah.last(this));
        }
    }

    private void showNowPlayingDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View panel = getLayoutInflater().inflate(R.layout.dialog_media_hub, root, false);
        dialog.setContentView(panel);

        Window w = dialog.getWindow();
        if (w != null) {
            w.setFlags(android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }

        ImageView btnPopupPrev = panel.findViewById(R.id.btnPopupPrev);
        ImageView btnPlayPause = panel.findViewById(R.id.btnPlayPause);
        ImageView btnPopupNext = panel.findViewById(R.id.btnPopupNext);
        TextView btnOpenApp = panel.findViewById(R.id.btnOpenApp);
        TextView btnCloseDialog = panel.findViewById(R.id.btnCloseDialog);

        updatePopupMediaInfo(panel, dialog);

        if (btnPopupPrev != null) {
            btnPopupPrev.setOnClickListener(v -> {
                SoundEffects.playSelect();
                NowPlayingManager.skipToPrevious();
                handler.postDelayed(() -> {
                    NowPlayingManager.updateCurrentMedia(MainActivity.this);
                    updatePopupMediaInfo(panel, dialog);
                }, 300);
            });
        }

        if (btnPlayPause != null) {
            btnPlayPause.setOnClickListener(v -> {
                SoundEffects.playSelect();
                NowPlayingManager.togglePlayPause();
                handler.postDelayed(() -> {
                    NowPlayingManager.updateCurrentMedia(MainActivity.this);
                    updatePopupMediaInfo(panel, dialog);
                }, 300);
            });
        }

        if (btnPopupNext != null) {
            btnPopupNext.setOnClickListener(v -> {
                SoundEffects.playSelect();
                NowPlayingManager.skipToNext();
                handler.postDelayed(() -> {
                    NowPlayingManager.updateCurrentMedia(MainActivity.this);
                    updatePopupMediaInfo(panel, dialog);
                }, 300);
            });
        }

        if (btnOpenApp != null) {
            btnOpenApp.setOnClickListener(v -> {
                SoundEffects.playSelect();
                dialog.dismiss();
                NowPlayingManager.openMediaApp(MainActivity.this);
            });
        }

        if (btnCloseDialog != null) {
            btnCloseDialog.setOnClickListener(v -> {
                SoundEffects.playSelect();
                dialog.dismiss();
                disableNowPlaying();
            });
        }

        View.OnFocusChangeListener btnFocus = (v, hasFocus) -> {
            if (hasFocus) SoundEffects.playNav();
            // The existing button drawables provide the cyan focus outline; keep geometry unchanged.
        };

        if (btnPopupPrev != null) btnPopupPrev.setOnFocusChangeListener(btnFocus);
        if (btnPlayPause != null) btnPlayPause.setOnFocusChangeListener(btnFocus);
        if (btnPopupNext != null) btnPopupNext.setOnFocusChangeListener(btnFocus);
        if (btnOpenApp != null) btnOpenApp.setOnFocusChangeListener(btnFocus);
        if (btnCloseDialog != null) btnCloseDialog.setOnFocusChangeListener(btnFocus);

        dialog.show();
        TvDialogStyle.applyCustom(dialog, this, 480, 0, 0.65f);
        if (btnPlayPause != null) {
            btnPlayPause.requestFocus();
        }
    }

    private void updatePopupMediaInfo(View panel, Dialog dialog) {
        if (panel == null || (dialog != null && !dialog.isShowing())) return;
        NowPlayingManager.MediaInfo info = NowPlayingManager.getCurrentInfo();
        boolean hasMedia = info != null && info.hasMusic();

        ImageView headerIcon = panel.findViewById(R.id.headerIcon);
        TextView badgeSource = panel.findViewById(R.id.badgeSource);
        ImageView imgAlbumArt = panel.findViewById(R.id.imgAlbumArt);
        TextView txtTrackTitle = panel.findViewById(R.id.txtTrackTitle);
        TextView txtTrackArtist = panel.findViewById(R.id.txtTrackArtist);
        TextView txtPlaybackStatus = panel.findViewById(R.id.txtPlaybackStatus);
        ImageView btnPlayPause = panel.findViewById(R.id.btnPlayPause);
        TextView btnOpenApp = panel.findViewById(R.id.btnOpenApp);

        if (hasMedia) {
            if (txtTrackTitle != null) {
                txtTrackTitle.setText(info.title.isEmpty() ? getString(R.string.now_playing_fallback_title) : info.title);
                txtTrackTitle.setSelected(true);
            }
            if (txtTrackArtist != null) {
                txtTrackArtist.setText(info.artist.isEmpty() ? (info.appName.isEmpty() ? getString(R.string.now_playing_fallback_artist) : info.appName) : info.artist);
            }
            if (txtPlaybackStatus != null) {
                txtPlaybackStatus.setText(info.isPlaying ? R.string.playback_status_playing : R.string.playback_status_paused);
                txtPlaybackStatus.setTextColor(info.isPlaying ? Color.parseColor("#1DB954") : Color.parseColor("#FFC107"));
            }

            if (imgAlbumArt != null) {
                if (info.albumArt != null) {
                    imgAlbumArt.setImageBitmap(info.albumArt);
                    imgAlbumArt.setPadding(0, 0, 0, 0);
                } else if (!info.packageName.isEmpty()) {
                    try {
                        Drawable appIcon = getPackageManager().getApplicationIcon(info.packageName);
                        imgAlbumArt.setImageDrawable(appIcon);
                        imgAlbumArt.setPadding(16, 16, 16, 16);
                    } catch (Exception e) {
                        imgAlbumArt.setImageResource(R.drawable.ic_music);
                        imgAlbumArt.setPadding(18, 18, 18, 18);
                    }
                }
            }

            if (badgeSource != null) {
                badgeSource.setText(info.appName.isEmpty() ? "SPOTIFY" : info.appName.toUpperCase(Locale.ROOT));
                if (info.packageName.contains("spotify")) {
                    badgeSource.setTextColor(Color.parseColor("#1DB954"));
                    if (headerIcon != null) headerIcon.setColorFilter(Color.parseColor("#1DB954"));
                } else {
                    badgeSource.setTextColor(Color.parseColor("#FF0033"));
                    if (headerIcon != null) headerIcon.setColorFilter(Color.parseColor("#FF0033"));
                }
            }

            if (btnPlayPause != null) {
                btnPlayPause.setImageResource(info.isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
            }

            if (btnOpenApp != null) {
                btnOpenApp.setText(getString(R.string.open_app_action, info.appName.isEmpty() ? getString(R.string.app_name) : info.appName));
            }
        } else {
            if (txtTrackTitle != null) txtTrackTitle.setText(R.string.no_song_playing);
            if (txtTrackArtist != null) txtTrackArtist.setText(R.string.now_playing_help);
            if (txtPlaybackStatus != null) {
                txtPlaybackStatus.setText(R.string.playback_idle);
                txtPlaybackStatus.setTextColor(Color.parseColor("#80FFFFFF"));
            }
            if (btnPlayPause != null) {
                btnPlayPause.setImageResource(R.drawable.ic_play);
            }
        }
    }

    private Dialog qrUploadDialog;
    private WallpaperUploadServer uploadServer;
    private AvatarUploadServer avatarUploadServer;
    private ImageView qrImageView;
    private TextView qrStatusText;
    private TextView qrUrlText;

    /** Keeps retired or unknown wallpaper selections compatible with the current catalog. */
    private String getActiveWallpaperCategory() {
        android.content.SharedPreferences prefs = getSharedPreferences("wallpaper_prefs", MODE_PRIVATE);
        String category = prefs.getString("active_category", "Default");
        if (!isSupportedWallpaperCategory(category)) {
            category = "Default";
            prefs.edit().putString("active_category", category).apply();
        }
        return category;
    }

    /**
     * Reconciles durable Custom-wallpaper preferences with the private files that survived
     * process death or an interrupted delete before the collection is rendered.
     */
    private String reconcileActiveWallpaperCategoryForStartup() {
        android.content.SharedPreferences prefs =
                getSharedPreferences("wallpaper_prefs", MODE_PRIVATE);
        String category = getActiveWallpaperCategory();
        if (!"Custom".equalsIgnoreCase(category)) return category;

        File customDir = new File(getFilesDir(), "custom_wallpapers");
        File[] files = customDir.exists()
                ? customDir.listFiles((dir, name) -> UploadFilePolicy.isSafeStoredImageName(name))
                : new File[0];
        // A null listing from an existing directory is an I/O/permission error, not proof that
        // the collection is empty. Preserve the user's saved selection until it can be inspected.
        if (customDir.exists() && files == null) return category;

        if (files == null || files.length == 0) {
            prefs.edit().putString("active_category", "Natural")
                    .putBoolean("custom_single_mode", false)
                    .remove("custom_single_file")
                    .apply();
            return "Natural";
        }

        boolean singleMode = prefs.getBoolean("custom_single_mode", false);
        String singleFile = prefs.getString("custom_single_file", null);
        if (singleMode) {
            File selected = singleFile != null && UploadFilePolicy.isSafeStoredImageName(singleFile)
                    ? new File(customDir, singleFile) : null;
            if (selected == null || !selected.isFile() || selected.length() <= 0) {
                prefs.edit().putBoolean("custom_single_mode", false)
                        .remove("custom_single_file")
                        .apply();
            }
        } else if (singleFile != null) {
            prefs.edit().remove("custom_single_file").apply();
        }
        return category;
    }

    private boolean isSupportedWallpaperCategory(String category) {
        return "Default".equalsIgnoreCase(category)
                || "Natural".equalsIgnoreCase(category)
                || "Skyscraper".equalsIgnoreCase(category)
                || "Space".equalsIgnoreCase(category)
                || "Ocean".equalsIgnoreCase(category)
                || "Minimalist".equalsIgnoreCase(category)
                || "Custom".equalsIgnoreCase(category);
    }

    /** Compact three-action control center used by Quick Control. */
    void showWallpaperDialog() {
        WallpaperSettingsDialog.show(this, new WallpaperSettingsDialog.Actions() {
            @Override public void openCollection() { showWallpaperCollectionDialog(); }
            @Override public void openInterval() { showWallpaperIntervalDialog(); }
            @Override public void showNext() { showNextWallpaperFromSettings(); }
            @Override public boolean isShuffleEnabled() {
                return getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getBoolean("shuffle_mode", true);
            }
            @Override public String intervalSummary() { return getWallpaperIntervalSummary(); }
            @Override public void toggleShuffle() { toggleWallpaperShuffleFromSettings(); }
        });
    }

    /** Full Appearance > Wallpaper management entry points; they never reopen Quick Control. */
    void showWallpaperCollectionManagementDialog() {
        showWallpaperCollectionDialog(false);
    }

    void showWallpaperIntervalManagementDialog() {
        showWallpaperIntervalDialog(false);
    }

    void showCustomWallpaperManagerManagementDialog() {
        showCustomWallpaperManagerDialog(false);
    }

    private void toggleWallpaperShuffleFromSettings() {
        android.content.SharedPreferences prefs = getSharedPreferences("wallpaper_prefs", MODE_PRIVATE);
        boolean enabled = !prefs.getBoolean("shuffle_mode", true);
        prefs.edit().putBoolean("shuffle_mode", enabled).remove("shuffle_cycle")
                .remove("shuffle_position").apply();
        TvFeedback.show(this, enabled ? R.string.wallpaper_shuffle_random : R.string.wallpaper_shuffle_ordered,
                Toast.LENGTH_SHORT);
    }

    private void showNextWallpaperFromSettings() {
        if (wallpapers == null || wallpapers.isEmpty()) {
            TvFeedback.show(this, R.string.default_wallpaper_missing, Toast.LENGTH_SHORT);
            return;
        }
        if (wallpapers.size() < 2) {
            String language = AppLanguage.getLanguage(this);
            String message = AppLanguage.LANG_EN.equals(language) ? "Only one wallpaper is available in this collection."
                    : AppLanguage.LANG_ID.equals(language) ? "Koleksi ini hanya memiliki satu wallpaper."
                    : "Koleksi ini hanya mempunyai satu wallpaper.";
            TvFeedback.show(this, message, Toast.LENGTH_SHORT);
            return;
        }
        showNextWallpaper(true);
        if (wallpaperIntervalMs > 0 && wallpapers.size() >= 2) {
            handler.removeCallbacks(wallTick);
            handler.postDelayed(wallTick, wallpaperIntervalMs);
        }
        TvFeedback.show(this, R.string.next_wallpaper_toast, Toast.LENGTH_SHORT);
    }

    private void showWallpaperCollectionDialog() {
        showWallpaperCollectionDialog(true);
    }

    private void showWallpaperCollectionDialog(boolean returnToQuickControl) {
        File customDir = new File(getFilesDir(), "custom_wallpapers");
        File[] customFiles = customDir.listFiles((dir, name) -> UploadFilePolicy.isSafeStoredImageName(name));
        int customCount = (customFiles != null) ? customFiles.length : 0;

        String currentKey = getActiveWallpaperCategory();

        class WallpaperOption {
            final String key;
            final String title;
            final String subtitle;
            WallpaperOption(String key, String title, String subtitle) {
                this.key = key;
                this.title = title;
                this.subtitle = subtitle;
            }
        }

        String curLang = AppLanguage.getLanguage(this);
        List<WallpaperOption> options = new ArrayList<>();

        // Bundled default wallpaper: available offline and separate from downloadable collections.
        String tDef = AppLanguage.LANG_EN.equals(curLang) ? "🌄 Default Wallpaper" : "🌄 Wallpaper Default";
        String sDef = AppLanguage.LANG_EN.equals(curLang) ? "Kabut Pagi Dempo • Built-in launcher wallpaper" : "Kabut Pagi Dempo • Wallpaper bawaan launcher";
        options.add(new WallpaperOption("Default", tDef, sDef));

        // Category 2: Natural
        String tNat = AppLanguage.LANG_EN.equals(curLang) ? "🌿 Nature & Landscapes" : (AppLanguage.LANG_ID.equals(curLang) ? "🌿 Alam Semesta & Lanskap" : "🌿 Alam Semula Jadi & Landskap");
        String sNat = AppLanguage.LANG_EN.equals(curLang) ? "Mountains, Forests & Aurora 4K" : (AppLanguage.LANG_ID.equals(curLang) ? "Pegunungan, Hutan & Aurora 4K" : "Pergunungan, Rimba & Aurora 4K");
        options.add(new WallpaperOption("Natural", tNat, sNat));

        // Category 3: Skyscraper
        String tSky = AppLanguage.LANG_EN.equals(curLang) ? "🏙️ Skyscrapers & Cities" : (AppLanguage.LANG_ID.equals(curLang) ? "🏙️ Pencakar Langit & Kota" : "🏙️ Pencakar Langit & Bandar Raya");
        String sSky = AppLanguage.LANG_EN.equals(curLang) ? "Modern Metropolis & Architecture" : (AppLanguage.LANG_ID.equals(curLang) ? "Metropolis Modern & Arsitektur" : "Metropolis Moden & Seni Bina");
        options.add(new WallpaperOption("Skyscraper", tSky, sSky));

        // Category 4: Space
        String tSpc = AppLanguage.LANG_EN.equals(curLang) ? "🌌 Space & Cosmos" : (AppLanguage.LANG_ID.equals(curLang) ? "🌌 Luar Angkasa & Kosmos" : "🌌 Angkasa & Kosmos");
        String sSpc = AppLanguage.LANG_EN.equals(curLang) ? "Galaxies, Nebulae & Stars (Deep Space)" : (AppLanguage.LANG_ID.equals(curLang) ? "Galaksi, Nebula & Bintang (Deep Space)" : "Galaksi, Nebula & Bintang (Deep Space)");
        options.add(new WallpaperOption("Space", tSpc, sSpc));

        // Category 5: Ocean
        String tOcn = AppLanguage.LANG_EN.equals(curLang) ? "🌊 Oceans & Coastlines" : (AppLanguage.LANG_ID.equals(curLang) ? "🌊 Lautan & Pesisir Pantai" : "🌊 Lautan & Pesisir Pantai");
        String sOcn = AppLanguage.LANG_EN.equals(curLang) ? "Crystal Clear Waters, Reefs & Beaches" : (AppLanguage.LANG_ID.equals(curLang) ? "Air Jernih, Terumbu Karang & Pantai" : "Air Jernih, Terumbu Karang & Pantai");
        options.add(new WallpaperOption("Ocean", tOcn, sOcn));

        // Category 6: Minimalist
        String tMin = AppLanguage.LANG_EN.equals(curLang) ? "🔲 Dark Minimalist (OLED)" : (AppLanguage.LANG_ID.equals(curLang) ? "🔲 Minimalis Gelap (OLED)" : "🔲 Minimalis Gelap (OLED)");
        String sMin = AppLanguage.LANG_EN.equals(curLang) ? "20 Dark Wallpaper Images • Optimized for OLED" : (AppLanguage.LANG_ID.equals(curLang) ? "20 Koleksi Wallpaper Gelap • Khusus Layar OLED" : "20 Koleksi Wallpaper Gelap • Khas Skrin OLED");
        options.add(new WallpaperOption("Minimalist", tMin, sMin));

        // Category 7: Manage Custom
        String tCst = AppLanguage.LANG_EN.equals(curLang) ? "📁 Manage Uploaded Photos" : (AppLanguage.LANG_ID.equals(curLang) ? "📁 Kelola Foto yang Diunggah" : "📁 Urus Gambar Dimuat Naik");
        String sCst = customCount > 0
                ? (AppLanguage.LANG_EN.equals(curLang) ? (customCount + " uploaded photos • QR / Slideshow") : (AppLanguage.LANG_ID.equals(curLang) ? (customCount + " foto diunggah • QR / Slideshow") : (customCount + " foto dimuat naik • QR / Slideshow")))
                : (AppLanguage.LANG_EN.equals(curLang) ? "Upload personal photos via phone" : (AppLanguage.LANG_ID.equals(curLang) ? "Unggah foto pribadi lewat ponsel" : "Muat naik foto peribadi melalui telefon"));
        options.add(new WallpaperOption("MANAGE_CUSTOM", tCst, sCst));

        boolean isDescOn = isWallpaperDescEnabled();

        String tDsc = (AppLanguage.LANG_EN.equals(curLang) ? "🏷️ Image Title & Info: " : (AppLanguage.LANG_ID.equals(curLang) ? "🏷️ Info & Deskripsi Gambar: " : "🏷️ Info & Deskripsi Gambar: ")) + (isDescOn ? (AppLanguage.LANG_EN.equals(curLang) ? "ON" : (AppLanguage.LANG_ID.equals(curLang) ? "Aktif (ON)" : "Dihidupkan (ON)")) : (AppLanguage.LANG_EN.equals(curLang) ? "OFF" : (AppLanguage.LANG_ID.equals(curLang) ? "Mati (OFF)" : "Dimatikan (OFF)")));
        String sDsc = isDescOn
                ? (AppLanguage.LANG_EN.equals(curLang) ? "Display title and description on home screen" : (AppLanguage.LANG_ID.equals(curLang) ? "Tampilkan judul dan deskripsi gambar di layar utama" : "Paparkan tajuk dan penerangan gambar di skrin utama"))
                : (AppLanguage.LANG_EN.equals(curLang) ? "Hide text for a clean screen display" : (AppLanguage.LANG_ID.equals(curLang) ? "Sembunyikan teks untuk tampilan layar bersih" : "Sembunyikan teks untuk paparan skrin bersih (Clean Look)"));
        options.add(new WallpaperOption("TOGGLE_DESC", tDsc, sDsc));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding((int) (20 * density), (int) (16 * density), (int) (20 * density), (int) (12 * density));

        // Header Title
        TextView titleView = new TextView(this);
        titleView.setText(AppLanguage.strWallpaperDialogTitle(this));
        titleView.setTextSize(19);
        titleView.setTextColor(0xFFFFFFFF);
        titleView.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        titleView.setFontVariationSettings("'wght' 700");
        titleView.setShadowLayer(4 * density, 0, 1 * density, 0xDD000000);
        layout.addView(titleView);

        ScrollView scroll = new ScrollView(this);
        scroll.setVerticalScrollBarEnabled(false);
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);

        AlertDialog[] dialogHolder = new AlertDialog[1];
        View[] viewToFocus = new View[1];

        for (int i = 0; i < options.size(); i++) {
            WallpaperOption opt = options.get(i);
            boolean isActive = opt.key.equalsIgnoreCase(currentKey);

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.VERTICAL);
            row.setFocusable(true);
            row.setClickable(true);
            row.setPadding((int) (TvUiTokens.CARD_PADDING_HORIZONTAL_DP * density),
                    (int) (8 * density), (int) (TvUiTokens.CARD_PADDING_HORIZONTAL_DP * density),
                    (int) (8 * density));
            row.setSelected(isActive);

            row.setBackground(TvFocusIndicator.createSelectableRoundedStateBackground(
                    density, TvUiTokens.CARD_RADIUS_DP, TvUiTokens.SURFACE,
                    TvUiTokens.NAV_ITEM_SELECTED, TvUiTokens.SURFACE_FOCUSED,
                    TvUiTokens.BORDER, TvUiTokens.NAV_SELECTION_BORDER, TvUiTokens.FOCUS_BORDER));

            // Row horizontal container for Title + Active tag
            LinearLayout topRow = new LinearLayout(this);
            topRow.setOrientation(LinearLayout.HORIZONTAL);
            topRow.setGravity(Gravity.CENTER_VERTICAL);

            TextView rowTitle = new TextView(this);
            rowTitle.setText(opt.title);
            rowTitle.setTextSize(TvUiTokens.CARD_TITLE_SP);
            rowTitle.setSingleLine(true);
            rowTitle.setEllipsize(android.text.TextUtils.TruncateAt.END);
            rowTitle.setTextColor(TvUiTokens.TEXT_PRIMARY);
            rowTitle.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
            rowTitle.setFontVariationSettings("'wght' 700");
            
            topRow.addView(rowTitle, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            TextView activeTag = null;
            if (isActive) {
                activeTag = new TextView(this);
                activeTag.setText("✓ " + AppLanguage.strNowBadge(this));
                activeTag.setTextSize(13);
                activeTag.setTextColor(TvUiTokens.ACCENT_SECTION);
                activeTag.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
                activeTag.setFontVariationSettings("'wght' 700");
                topRow.addView(activeTag);
            }
            row.addView(topRow);

            final String chosenKey = opt.key;
            row.setOnClickListener(v -> {
                if (dialogHolder[0] != null) dialogHolder[0].dismiss();
                SoundEffects.playSelect();
                if ("TOGGLE_DESC".equals(chosenKey)) {
                    boolean current = isWallpaperDescEnabled();
                    boolean newMode = !current;
                    setWallpaperDescEnabled(newMode);
                    if (!newMode) {
                        if (heroShelf != null) {
                            heroShelf.animate().cancel();
                            heroShelf.setAlpha(0f);
                            heroShelf.setVisibility(View.GONE);
                        }
                        TvFeedback.show(MainActivity.this, R.string.wallpaper_description_disabled, Toast.LENGTH_SHORT);
                    } else {
                        if (heroShelf != null && !isCustomWallpaper()) {
                            heroShelf.setVisibility(View.VISIBLE);
                            heroShelf.setAlpha(1f);
                            if (wallpapers != null && !wallpapers.isEmpty()) {
                                int curIdx = getSharedPreferences("wallpaper", MODE_PRIVATE).getInt("index", 0);
                                if (curIdx >= 0 && curIdx < wallpapers.size()) {
                                    setPlace(wallpapers.get(curIdx));
                                }
                            }
                        }
                        TvFeedback.show(MainActivity.this, R.string.wallpaper_description_enabled, Toast.LENGTH_SHORT);
                    }
                    if (returnToQuickControl) showWallpaperDialog();
                    else showWallpaperCollectionDialog(false);
                } else if ("MANAGE_CUSTOM".equals(chosenKey)) {
                    showCustomWallpaperManagerDialog(returnToQuickControl);
                } else {
                    switchWallpaperCategory(chosenKey);
                }
            });

            LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rlp.bottomMargin = (int) (5 * density);
            list.addView(row, rlp);

            if (isActive && viewToFocus[0] == null) {
                viewToFocus[0] = row;

            }
        }

        scroll.addView(list);
        layout.addView(scroll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setView(layout);
        builder.setNegativeButton(AppLanguage.strClose(this), (d, w) -> d.dismiss());
        AlertDialog dialog = builder.create();
        dialogHolder[0] = dialog;
        dialog.show();
        styleGlassDialog(dialog, 560, 500);

        if (viewToFocus[0] != null) {
            viewToFocus[0].requestFocus();
        } else if (list.getChildCount() > 0) {
            list.getChildAt(0).requestFocus();
        }
    }

    void showWallpaperIntervalDialog() {
        showWallpaperIntervalDialog(true);
    }

    private void showWallpaperIntervalDialog(boolean returnToQuickControl) {
        SoundEffects.playSelect();
        class IntervalOption {
            final int minutes;
            final String title;
            final String subtitle;
            IntervalOption(int minutes, String title, String subtitle) {
                this.minutes = minutes;
                this.title = title;
                this.subtitle = subtitle;
            }
        }

        String curLang = AppLanguage.getLanguage(this);
        List<IntervalOption> options = new ArrayList<>();
        options.add(new IntervalOption(1,
                AppLanguage.LANG_EN.equals(curLang) ? "⚡ 1 Minute" : (AppLanguage.LANG_ID.equals(curLang) ? "⚡ 1 Menit" : "⚡ 1 Minit"),
                AppLanguage.LANG_EN.equals(curLang) ? "Fast • Ideal for demonstration & testing" : (AppLanguage.LANG_ID.equals(curLang) ? "Cepat • Cocok untuk demonstrasi & pengujian" : "Pantas • Sesuai untuk demonstrasi & ujian")));
        options.add(new IntervalOption(5,
                AppLanguage.LANG_EN.equals(curLang) ? "⭐ 5 Minutes (Recommended)" : (AppLanguage.LANG_ID.equals(curLang) ? "⭐ 5 Menit (Disarankan)" : "⭐ 5 Minit (Disyorkan)"),
                AppLanguage.LANG_EN.equals(curLang) ? "Dynamic display, changes fresh every 5 minutes" : (AppLanguage.LANG_ID.equals(curLang) ? "Tampilan dinamis, berganti segar tiap 5 menit" : "Paparan dinamik, bertukar segar setiap 5 minit")));
        options.add(new IntervalOption(10,
                AppLanguage.LANG_EN.equals(curLang) ? "⭐ 10 Minutes (Recommended)" : (AppLanguage.LANG_ID.equals(curLang) ? "⭐ 10 Menit (Disarankan)" : "⭐ 10 Minit (Disyorkan)"),
                AppLanguage.LANG_EN.equals(curLang) ? "Balanced cadence for relaxed viewing" : (AppLanguage.LANG_ID.equals(curLang) ? "Keseimbangan pas untuk kenyamanan santai" : "Keseimbangan sempurna untuk tontonan santai")));
        options.add(new IntervalOption(15,
                AppLanguage.LANG_EN.equals(curLang) ? "⏱️ 15 Minutes" : (AppLanguage.LANG_ID.equals(curLang) ? "⏱️ 15 Menit" : "⏱️ 15 Minit"),
                AppLanguage.LANG_EN.equals(curLang) ? "Quarter-hour rotation" : (AppLanguage.LANG_ID.equals(curLang) ? "Rotasi santai setiap seperempat jam" : "Pertukaran santai suku jam")));
        options.add(new IntervalOption(30,
                AppLanguage.LANG_EN.equals(curLang) ? "⏱️ 30 Minutes" : (AppLanguage.LANG_ID.equals(curLang) ? "⏱️ 30 Menit" : "⏱️ 30 Minit"),
                AppLanguage.LANG_EN.equals(curLang) ? "Half-hour rotation" : (AppLanguage.LANG_ID.equals(curLang) ? "Rotasi setiap setengah jam" : "Pertukaran perlahan setengah jam")));
        options.add(new IntervalOption(60,
                AppLanguage.LANG_EN.equals(curLang) ? "⏱️ 1 Hour (60 Minutes)" : (AppLanguage.LANG_ID.equals(curLang) ? "⏱️ 1 Jam (60 Menit)" : "⏱️ 1 Jam (60 Minit)"),
                AppLanguage.LANG_EN.equals(curLang) ? "One wallpaper per hour" : (AppLanguage.LANG_ID.equals(curLang) ? "Satu wallpaper setiap jam" : "Satu gambar bagi setiap jam")));
        options.add(new IntervalOption(0,
                AppLanguage.LANG_EN.equals(curLang) ? "⏸️ Manual Only (No Auto Change)" : (AppLanguage.LANG_ID.equals(curLang) ? "⏸️ Manual Saja (Tanpa Pergantian Otomatis)" : "⏸️ Manual Sahaja (Tanpa Pertukaran Automatik)"),
                AppLanguage.LANG_EN.equals(curLang) ? "Keep current wallpaper • No automatic change" : (AppLanguage.LANG_ID.equals(curLang) ? "Pertahankan gambar saat ini • Tidak berganti otomatis" : "Kekalkan gambar semasa • Tiada pertukaran automatik")));

        int currentMin = getWallpaperIntervalMinutes();

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding((int) (20 * density), (int) (16 * density), (int) (20 * density), (int) (12 * density));

        // Header Title
        TextView titleView = new TextView(this);
        titleView.setText(AppLanguage.strWallpaperIntervalTitle(this));
        titleView.setTextSize(19);
        titleView.setTextColor(0xFFFFFFFF);
        titleView.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        titleView.setFontVariationSettings("'wght' 700");
        titleView.setShadowLayer(4 * density, 0, 1 * density, 0xDD000000);
        layout.addView(titleView);

        ScrollView scroll = new ScrollView(this);
        scroll.setVerticalScrollBarEnabled(false);
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);

        AlertDialog[] dialogHolder = new AlertDialog[1];
        View[] viewToFocus = new View[1];

        for (int i = 0; i < options.size(); i++) {
            IntervalOption opt = options.get(i);
            boolean isActive = (opt.minutes == currentMin);

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.VERTICAL);
            row.setFocusable(true);
            row.setClickable(true);
            row.setPadding((int) (TvUiTokens.CARD_PADDING_HORIZONTAL_DP * density),
                    (int) (8 * density), (int) (TvUiTokens.CARD_PADDING_HORIZONTAL_DP * density),
                    (int) (8 * density));
            row.setSelected(isActive);

            row.setBackground(TvFocusIndicator.createSelectableRoundedStateBackground(
                    density, TvUiTokens.CARD_RADIUS_DP, TvUiTokens.SURFACE,
                    TvUiTokens.NAV_ITEM_SELECTED, TvUiTokens.SURFACE_FOCUSED,
                    TvUiTokens.BORDER, TvUiTokens.NAV_SELECTION_BORDER, TvUiTokens.FOCUS_BORDER));

            LinearLayout topRow = new LinearLayout(this);
            topRow.setOrientation(LinearLayout.HORIZONTAL);
            topRow.setGravity(Gravity.CENTER_VERTICAL);

            TextView rowTitle = new TextView(this);
            rowTitle.setText(opt.title);
            rowTitle.setTextSize(TvUiTokens.CARD_TITLE_SP);
            rowTitle.setSingleLine(true);
            rowTitle.setEllipsize(android.text.TextUtils.TruncateAt.END);
            rowTitle.setTextColor(TvUiTokens.TEXT_PRIMARY);
            rowTitle.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
            rowTitle.setFontVariationSettings("'wght' 700");
            
            topRow.addView(rowTitle, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            TextView activeTag = null;
            if (isActive) {
                activeTag = new TextView(this);
                activeTag.setText("✓ " + AppLanguage.strNowBadge(this));
                activeTag.setTextSize(13);
                activeTag.setTextColor(TvUiTokens.ACCENT_SECTION);
                activeTag.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
                activeTag.setFontVariationSettings("'wght' 700");
                topRow.addView(activeTag);
            }
            row.addView(topRow);

            final int chosenMin = opt.minutes;
            row.setOnClickListener(v -> {
                if (dialogHolder[0] != null) dialogHolder[0].dismiss();
                SoundEffects.playSelect();
                getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).edit()
                        .putInt("wallpaper_interval_minutes", chosenMin)
                        .apply();
                wallpaperIntervalMs = getWallpaperIntervalMs();
                handler.removeCallbacks(wallTick);
                if (wallpaperIntervalMs > 0 && wallpapers != null && wallpapers.size() >= 2) {
                    wallShownAt = android.os.SystemClock.elapsedRealtime();
                    handler.postDelayed(wallTick, wallpaperIntervalMs);
                }
                String msg = (chosenMin == 0)
                        ? getString(R.string.wallpaper_auto_change_off)
                        : getString(R.string.wallpaper_auto_change_interval, chosenMin == 60 ? getString(R.string.one_hour_label) : getString(R.string.minutes_label, chosenMin));
                TvFeedback.show(MainActivity.this, msg, Toast.LENGTH_SHORT);
                if (returnToQuickControl) showWallpaperDialog();
            });

            LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rlp.bottomMargin = (int) (5 * density);
            list.addView(row, rlp);

            if (isActive && viewToFocus[0] == null) {
                viewToFocus[0] = row;

            }
        }

        scroll.addView(list);
        layout.addView(scroll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setView(layout);
        builder.setNegativeButton(AppLanguage.strBack(this), (d, w) -> {
            d.dismiss();
            if (returnToQuickControl) showWallpaperDialog();
        });
        AlertDialog dialog = builder.create();
        dialogHolder[0] = dialog;
        dialog.show();
        styleGlassDialog(dialog, 560, 500);

        if (viewToFocus[0] != null) {
            viewToFocus[0].requestFocus();
        } else if (list.getChildCount() > 0) {
            list.getChildAt(0).requestFocus();
        }
    }

    private void triggerGoogleAssistant() {
        SoundEffects.playSelect();
        // 1. Primary: System Google Assistant (same as Assistant key on remote)
        try {
            Intent intent = new Intent(Intent.ACTION_ASSIST);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        } catch (Exception ignored) {}

        // 2. Direct Katniss Voice Search (bypasses intent disambiguation chooser)
        try {
            Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
            intent.setPackage("com.google.android.katniss");
            intent.putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        } catch (Exception ignored) {}

        // 3. Voice Command Action
        try {
            Intent intent = new Intent(Intent.ACTION_VOICE_COMMAND);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        } catch (Exception ignored) {}

        // 4. Web Search Action
        try {
            Intent intent = new Intent("android.speech.action.WEB_SEARCH");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        } catch (Exception ignored) {}

        // 5. System Search Dialog
        try {
            startSearch(null, false, null, true);
            return;
        } catch (Exception ignored) {}

        // 6. Generic Speech Recognizer Fallback
        try {
            Intent intent = new Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            TvFeedback.show(this, R.string.assistant_unavailable, Toast.LENGTH_SHORT);
        }
    }

    void styleGlassDialog(AlertDialog dialog) {
        styleGlassDialog(dialog, 520, 0);
    }

    /** Applies the B01 modal contract before the existing typography/focus styling. */
    void styleGlassDialog(AlertDialog dialog, int maxWidthDp, int maxHeightDp) {
        if (dialog == null) return;
        try {
            TvDialogStyle.apply(dialog, this, maxWidthDp, maxHeightDp);
        } catch (RuntimeException e) {
            android.util.Log.w("TvHome.Dialog", "Unable to apply shared modal layout; keeping theme fallback", e);
        }
        try {
            Typeface outfit = getResources().getFont(R.font.inter);
            TextView titleView = dialog.findViewById(android.R.id.title);
            if (titleView != null) {
                titleView.setTypeface(outfit, Typeface.BOLD);
                titleView.setFontVariationSettings("'wght' 700");
                titleView.setTextSize(20f);
                titleView.setTextColor(0xFFFFFFFF);
                titleView.setShadowLayer(4 * density, 0, 1 * density, 0xDD000000);
            }
            TextView msgView = dialog.findViewById(android.R.id.message);
            if (msgView != null) {
                msgView.setTypeface(outfit, Typeface.BOLD);
                msgView.setFontVariationSettings("'wght' 600");
                msgView.setTextSize(15f);
                msgView.setTextColor(0xF0FFFFFF);
                msgView.setLineSpacing(0, 1.35f);
                msgView.setShadowLayer(3 * density, 0, 1 * density, 0xCC000000);
            }
            Button btnPos = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if (btnPos != null) {
                btnPos.setTypeface(outfit, Typeface.BOLD);
                btnPos.setFontVariationSettings("'wght' 700");
                btnPos.setTextSize(15f);
                btnPos.setTextColor(0xFFFFFFFF);
            }
            Button btnNeg = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (btnNeg != null) {
                btnNeg.setTypeface(outfit, Typeface.BOLD);
                btnNeg.setFontVariationSettings("'wght' 650");
                btnNeg.setTextSize(15f);
                btnNeg.setTextColor(0xCCFFFFFF);
            }
            Button btnNeu = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
            if (btnNeu != null) {
                btnNeu.setTypeface(outfit, Typeface.BOLD);
                btnNeu.setFontVariationSettings("'wght' 650");
                btnNeu.setTextSize(15f);
                btnNeu.setTextColor(0xCCFFFFFF);
            }
            ListView listView = dialog.getListView();
            if (listView != null) {
                listView.setSelector(R.drawable.selector_glass_dialog_list_item_bg);
                listView.setDrawSelectorOnTop(false);
                listView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
                    @Override
                    public void onChildViewAdded(View parent, View child) {
                        if (child instanceof TextView) {
                            TextView tv = (TextView) child;
                            tv.setTypeface(outfit, Typeface.BOLD);
                            tv.setFontVariationSettings("'wght' 650");
                            tv.setTextSize(15.5f);
                            tv.setTextColor(0xFFFFFFFF);
                            tv.setShadowLayer(3 * density, 0, 1 * density, 0xAA000000);
                            tv.setPadding((int) (18 * density), (int) (12 * density), (int) (18 * density), (int) (12 * density));
                        }
                    }

                    @Override
                    public void onChildViewRemoved(View parent, View child) {}
                });
            }
        } catch (Exception ignored) {}
    }



    /**
     * Shared visual contract for app-owned popups opened from the Device Settings tab.
     * Keep the surface opaque and consistent across confirmation, result, list and custom-view dialogs.
     */
    void styleDeviceSettingsPopup(AlertDialog dialog, int maxWidthDp, int maxHeightDp) {
        styleGlassDialog(dialog, maxWidthDp, maxHeightDp);
    }

    private File getBundledDefaultWallpaper() {
        // The APK asset is the single source of truth. Never reuse an unverified legacy cache.
        File target = new File(getFilesDir(), "kabut-pagi-dempo-default.jpg");
        File staged = new File(getFilesDir(), "kabut-pagi-dempo-default.jpg.tmp");
        File backup = new File(getFilesDir(), "kabut-pagi-dempo-default.jpg.bak");
        try (java.io.InputStream in = getAssets().open("kabut-pagi-dempo.jpg");
             java.io.FileOutputStream out = new java.io.FileOutputStream(staged, false)) {
            byte[] buffer = new byte[8192];
            int count;
            while ((count = in.read(buffer)) != -1) out.write(buffer, 0, count);
            out.flush();
            out.getFD().sync();
            if (!staged.isFile() || staged.length() == 0) {
                staged.delete();
                return null;
            }

            if (backup.exists() && !backup.delete()) {
                staged.delete();
                android.util.Log.e("TvHome", "Cannot clear previous Default wallpaper backup");
                return null;
            }
            boolean hadTarget = target.isFile();
            if (hadTarget && !target.renameTo(backup)) {
                staged.delete();
                android.util.Log.e("TvHome", "Cannot stage existing Default wallpaper for replacement");
                return null;
            }
            if (!staged.renameTo(target)) {
                staged.delete();
                if (hadTarget) backup.renameTo(target);
                android.util.Log.e("TvHome", "Cannot finalize bundled Default wallpaper asset");
                return null;
            }
            if (backup.exists() && !backup.delete()) {
                android.util.Log.w("TvHome", "Could not remove previous Default wallpaper backup");
            }
            return target;
        } catch (Exception e) {
            android.util.Log.e("TvHome", "Bundled Default wallpaper unavailable", e);
            staged.delete();
            // Do not display an unverified cache: it may contain an obsolete wallpaper.
            return null;
        }
    }

    private void switchWallpaperCategory(String category) {
        getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).edit().putString("active_category", category).apply();
        String displayLabel;
        if ("Minimalist".equalsIgnoreCase(category)) {
            displayLabel = "Minimalis Gelap (OLED)";
        } else {
            displayLabel = category;
        }
        TvFeedback.show(this, getString(R.string.wallpaper_category_selected, displayLabel), Toast.LENGTH_SHORT);
        if ("Default".equalsIgnoreCase(category)) {
            placesMode = false;
            // Keep the existing hero shelf state while the bundled image is re-applied.
            // setPlace() will update it when the metadata changes; hiding it here first
            // can leave the shelf invisible when Default is selected again with the same title.
            File bundled = getBundledDefaultWallpaper();
            if (bundled != null) {
                wallpapers = Collections.singletonList(bundled);
                getSharedPreferences("wallpaper", MODE_PRIVATE).edit().putInt("index", -1).apply();
                showNextWallpaper(true);
            } else {
                // Do not leave the previous category visible or fall back to a stale image.
                root.setBackgroundColor(android.graphics.Color.BLACK);
                if (wallView != null) {
                    wallView.setImageDrawable(null);
                    wallView.setBackgroundColor(android.graphics.Color.BLACK);
                }
                TvFeedback.show(this, R.string.default_wallpaper_missing, Toast.LENGTH_LONG);
            }
            handler.removeCallbacks(wallTick);
            return;
        }
        if ("Minimalist".equalsIgnoreCase(category)) {
            root.setBackgroundColor(android.graphics.Color.BLACK);
            if (wallView != null) {
                wallView.setImageDrawable(null);
                wallView.setBackgroundColor(android.graphics.Color.BLACK);
            }
        }

        if ("Custom".equalsIgnoreCase(category)) {
            placesMode = false;
            if (heroShelf != null) {
                heroShelf.animate().cancel();
                heroShelf.setAlpha(0f);
                heroShelf.setVisibility(View.GONE);
            }
            File customDir = new File(getFilesDir(), "custom_wallpapers");
            File[] files = customDir.listFiles((dir, name) -> UploadFilePolicy.isSafeStoredImageName(name));
            if (files != null && files.length > 0) {
                Arrays.sort(files, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
                wallpapers = new ArrayList<>(Arrays.asList(files));
                getSharedPreferences("wallpaper", MODE_PRIVATE).edit().putInt("index", -1).apply();
                showNextWallpaper(true);
                startWallpaperRotation();
                return;
            }
        } else {
            placesMode = false;
            if (heroShelf != null) {
                if (isWallpaperDescEnabled()) {
                    heroShelf.setVisibility(View.VISIBLE);
                } else {
                    heroShelf.setVisibility(View.GONE);
                    heroShelf.setAlpha(0f);
                }
            }
        }

        List<File> matching = OnlineWallpapers.listByCategory(this, category);
        if (!matching.isEmpty()) {
            wallpapers = matching;
            showNextWallpaper(true);
            startWallpaperRotation();
        } else {
            TvFeedback.show(this, getString(R.string.wallpaper_download_started, displayLabel), Toast.LENGTH_SHORT);
            new Thread(() -> {
                boolean downloaded;
                try {
                    downloaded = OnlineWallpapers.syncCategory(getApplicationContext(), category);
                } catch (Exception e) {
                    downloaded = false;
                }
                final boolean syncSucceeded = downloaded;
                handler.post(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    List<File> fresh = OnlineWallpapers.listByCategory(MainActivity.this, category);
                    if (!fresh.isEmpty()) {
                        wallpapers = fresh;
                        showNextWallpaper(true);
                        startWallpaperRotation();
                    }
                    if (syncSucceeded && !fresh.isEmpty()) {
                        TvFeedback.show(MainActivity.this,
                                getString(R.string.wallpaper_download_complete, displayLabel, fresh.size()),
                                Toast.LENGTH_LONG);
                    } else {
                        TvFeedback.show(MainActivity.this,
                                getString(R.string.wallpaper_download_failed, displayLabel),
                                Toast.LENGTH_LONG);
                    }
                });
            }, "wall-cat-sync").start();
        }
    }

    void showInfoActionsDialog() {
        SoundEffects.playSelect();
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding((int) (26 * density), (int) (20 * density), (int) (26 * density), (int) (20 * density));

        // Header Title
        TextView title = new TextView(this);
        title.setText(R.string.info_center_title);
        title.setTextSize(22);
        title.setTextColor(0xFFFFFFFF);
        title.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        title.setFontVariationSettings("'wght' 700");
        title.setShadowLayer(4 * density, 0, 1 * density, 0xDD000000);
        title.setPadding(0, 0, 0, (int) (4 * density));
        layout.addView(title);

        TextView sub = new TextView(this);
        sub.setText(R.string.info_center_subtitle);
        sub.setTextSize(14f);
        sub.setTextColor(0xE6FFFFFF);
        sub.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        sub.setFontVariationSettings("'wght' 600");
        sub.setShadowLayer(3 * density, 0, 1 * density, 0xCC000000);
        sub.setPadding(0, 0, 0, (int) (14 * density));
        layout.addView(sub);

        // Status Card: System status overview
        LinearLayout statusCard = new LinearLayout(this);
        statusCard.setOrientation(LinearLayout.VERTICAL);
        statusCard.setPadding((int) (16 * density), (int) (12 * density), (int) (16 * density), (int) (12 * density));
        GradientDrawable cardBg = new GradientDrawable();
        cardBg.setColor(0x24FFFFFF);
        cardBg.setCornerRadius(14 * density);
        cardBg.setStroke((int) (1.8f * density), 0x80DCE5ED);
        statusCard.setBackground(cardBg);

        Location.Zone zone = Location.chosen(this);
        String zoneTown = (zone != null) ? zone.town : "Pagar Alam";
        String nextPrayer = PrayerTimes.getNextPrayerSummary(this);
        HomeWeather.Line wx = HomeWeather.last(this);
        String wxStr = (wx != null && !wx.weather.isEmpty()) ? (wx.weather + (wx.aqi.isEmpty() ? "" : " • " + wx.aqi)) : getString(R.string.weather_live_active);

        TextView statusText = new TextView(this);
        statusText.setText(getString(R.string.info_system_summary, nextPrayer, zoneTown, wxStr));
        statusText.setTextSize(14.5f);
        statusText.setTextColor(0xFFFFFFFF);
        statusText.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        statusText.setFontVariationSettings("'wght' 600");
        statusText.setShadowLayer(3 * density, 0, 1 * density, 0xCC000000);
        statusText.setLineSpacing(0, 1.3f);
        statusCard.addView(statusText);
        layout.addView(statusCard);

        TextView actionsTitle = new TextView(this);
        actionsTitle.setText(R.string.info_actions_title);
        actionsTitle.setTextSize(14.5f);
        actionsTitle.setTextColor(0xE6FFFFFF);
        actionsTitle.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        actionsTitle.setFontVariationSettings("'wght' 700");
        actionsTitle.setShadowLayer(3 * density, 0, 1 * density, 0xCC000000);
        actionsTitle.setPadding(0, (int) (14 * density), 0, (int) (8 * density));
        layout.addView(actionsTitle);

        ScrollView scroll = new ScrollView(this);
        scroll.setVerticalScrollBarEnabled(false);
        LinearLayout actionList = new LinearLayout(this);
        actionList.setOrientation(LinearLayout.VERTICAL);

        AlertDialog[] dialogHolder = new AlertDialog[1];

        // Action 1: Smart Boost
        addInfoActionItem(actionList, getString(R.string.info_action_smart_boost), () -> {
            if (dialogHolder[0] != null) dialogHolder[0].dismiss();
            showCleanTvCacheDialog();
        });

        // Action 2: Cuaca & Kualitas Udara
        addInfoActionItem(actionList, getString(R.string.info_action_weather), () -> {
            if (dialogHolder[0] != null) dialogHolder[0].dismiss();
            SoundEffects.playSelect();
            showWeatherSummaryDialog(zoneTown, wxStr);
        });

        // Action 3: Jadwal Salat & Ayat Pilihan
        addInfoActionItem(actionList, getString(R.string.info_action_prayer), () -> {
            if (dialogHolder[0] != null) dialogHolder[0].dismiss();
            SoundEffects.playSelect();
            showPrayerAndAyahDialog(zoneTown);
        });

        // Action 4: Tips & Panduan Kontrol TV
        addInfoActionItem(actionList, getString(R.string.info_action_tips), () -> {
            if (dialogHolder[0] != null) dialogHolder[0].dismiss();
            SoundEffects.playSelect();
            showTvTipsDialog();
        });

        scroll.addView(actionList);
        layout.addView(scroll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        builder.setView(layout);
        builder.setNegativeButton(R.string.close_label, (d, w) -> d.dismiss());
        AlertDialog dialog = builder.create();
        dialogHolder[0] = dialog;
        dialog.show();
        styleGlassDialog(dialog);

        if (dialog.getWindow() != null) {
            int w = Math.min((int) (520 * density), getResources().getDisplayMetrics().widthPixels - (int) (120 * density));
            int h = Math.min((int) (480 * density), (int) (getResources().getDisplayMetrics().heightPixels * 0.75f));
            dialog.getWindow().setLayout(w, h);
        }
        if (actionList.getChildCount() > 0) {
            actionList.getChildAt(0).requestFocus();
        }
    }

    private void addInfoActionItem(LinearLayout container, String title, Runnable onClick) {
        TextView btn = new TextView(this);
        btn.setText(title);
        btn.setTextSize(15.5f);
        btn.setTextColor(0xFFFFFFFF);
        btn.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        btn.setFontVariationSettings("'wght' 650");
        btn.setShadowLayer(3 * density, 0, 1 * density, 0xAA000000);
        btn.setPadding((int) (16 * density), (int) (12 * density), (int) (16 * density), (int) (12 * density));
        btn.setFocusable(true);
        btn.setClickable(true);

        StateListDrawable bg = new StateListDrawable();
        GradientDrawable on = new GradientDrawable();
        on.setColor(TvUiTokens.SURFACE_FOCUSED);
        on.setCornerRadius(12 * density);
        on.setStroke(Math.max(1, (int) (2 * density)), TvUiTokens.FOCUS_BORDER);
        GradientDrawable off = new GradientDrawable();
        off.setColor(0x223F4B56);
        off.setCornerRadius(12 * density);
        off.setStroke(Math.max(1, (int) density), 0x55DCE5ED);
        bg.addState(new int[]{android.R.attr.state_focused}, on);
        bg.addState(new int[]{}, off);
        btn.setBackground(bg);
        btn.setTextColor(new ColorStateList(new int[][]{{android.R.attr.state_focused}, {}}, new int[]{TvUiTokens.TEXT_PRIMARY, TvUiTokens.TEXT_PRIMARY}));

        btn.setOnClickListener(v -> onClick.run());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = (int) (6 * density);
        container.addView(btn, lp);
    }

    private void showTvTipsDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog)
                .setTitle(R.string.tv_tips_title)
                .setMessage(R.string.tv_tips_body)
                .setPositiveButton(R.string.understand_action, null)
                .show();
        styleGlassDialog(dialog);
        if (dialog.getWindow() != null) {
            int w = Math.min((int) (520 * density), getResources().getDisplayMetrics().widthPixels - (int) (120 * density));
            int h = Math.min((int) (430 * density), (int) (getResources().getDisplayMetrics().heightPixels * 0.68f));
            dialog.getWindow().setLayout(w, h);
        }
    }

    private void showWeatherSummaryDialog(String town, String wxStr) {
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog)
                .setTitle(R.string.weather_summary_title)
                .setMessage(getString(R.string.weather_summary_body, town, wxStr))
                .setPositiveButton(R.string.ok_action, null)
                .show();
        styleGlassDialog(dialog);
        if (dialog.getWindow() != null) {
            int w = Math.min((int) (520 * density), getResources().getDisplayMetrics().widthPixels - (int) (120 * density));
            int h = Math.min((int) (430 * density), (int) (getResources().getDisplayMetrics().heightPixels * 0.68f));
            dialog.getWindow().setLayout(w, h);
        }
    }

    private void showPrayerAndAyahDialog(String town) {
        WeeklyAyah.Ayah ayah = WeeklyAyah.last(this);
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);
        scroll.setVerticalScrollBarEnabled(true);
        scroll.setFocusable(true);
        scroll.setFocusableInTouchMode(true);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(Math.round(24 * density), Math.round(18 * density),
                Math.round(24 * density), Math.round(18 * density));

        TextView summary = new TextView(this);
        summary.setText(getString(R.string.prayer_summary_body, town, PrayerTimes.getHijriDate(),
                PrayerTimes.getNextPrayerSummary(this), ""));
        summary.setTextColor(0xFFEFEFEF);
        summary.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
        summary.setTypeface(getResources().getFont(R.font.inter));
        summary.setLineSpacing(Math.round(3 * density), 1.12f);
        content.addView(summary, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (ayah != null && ayah.arabic != null && !ayah.arabic.trim().isEmpty()) {
            TextView arabic = new TextView(this);
            arabic.setText(ayah.arabic.trim());
            arabic.setTextColor(TvUiTokens.TEXT_PRIMARY);
            arabic.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
            arabic.setTypeface(getResources().getFont(R.font.amiri_quran));
            arabic.setTextDirection(View.TEXT_DIRECTION_RTL);
            arabic.setGravity(Gravity.END);
            arabic.setIncludeFontPadding(true);
            arabic.setBreakStrategy(android.text.Layout.BREAK_STRATEGY_HIGH_QUALITY);
            arabic.setHyphenationFrequency(android.text.Layout.HYPHENATION_FREQUENCY_NONE);
            arabic.setLineSpacing(Math.round(5 * density), 1.12f);
            arabic.setMaxLines(Integer.MAX_VALUE);
            arabic.setEllipsize(null);
            LinearLayout.LayoutParams arabicLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            arabicLp.topMargin = Math.round(12 * density);
            content.addView(arabic, arabicLp);

            TextView translation = new TextView(this);
            String translated = ayah.translation(AppLanguage.getLanguage(this));
            translation.setText(translated != null ? translated.trim() : "");
            translation.setTextColor(0xFFF0F0F0);
            translation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
            translation.setTypeface(getResources().getFont(R.font.inter));
            translation.setGravity(Gravity.START);
            translation.setLineSpacing(Math.round(3 * density), 1.15f);
            translation.setMaxLines(Integer.MAX_VALUE);
            translation.setEllipsize(null);
            LinearLayout.LayoutParams translationLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            translationLp.topMargin = Math.round(12 * density);
            content.addView(translation, translationLp);

            TextView reference = new TextView(this);
            reference.setText(ayah.reference);
            reference.setTextColor(0xFFB9C5D3);
            reference.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
            reference.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
            LinearLayout.LayoutParams referenceLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            referenceLp.topMargin = Math.round(10 * density);
            content.addView(reference, referenceLp);
        } else {
            TextView empty = new TextView(this);
            empty.setText(R.string.no_daily_ayah);
            empty.setTextColor(0xFFEFEFEF);
            empty.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
            content.addView(empty);
        }

        scroll.addView(content);
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog)
                .setTitle(R.string.prayer_summary_title)
                .setView(scroll)
                .setPositiveButton(R.string.close_label, null)
                .create();
        dialog.show();
        styleGlassDialog(dialog);
        if (dialog.getWindow() != null) {
            int w = Math.min((int) (620 * density), getResources().getDisplayMetrics().widthPixels - (int) (120 * density));
            int h = Math.min((int) (560 * density), (int) (getResources().getDisplayMetrics().heightPixels * 0.72f));
            dialog.getWindow().setLayout(w, h);
        }
        scroll.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                    && !scroll.canScrollVertically(1)) {
                Button close = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                if (close != null) close.requestFocus();
                return true;
            }
            return false;
        });
        scroll.requestFocus();
    }

    /** Starts a short-lived, profile-scoped QR upload session for exactly one avatar photo. */
    void showAvatarUploadQrDialog(String profileId, Runnable onUpdated) {
        if (profileId == null || profileId.trim().isEmpty() || isFinishing() || isDestroyed()) return;
        if (qrUploadDialog != null && qrUploadDialog.isShowing()) qrUploadDialog.dismiss();
        if (uploadServer != null) { uploadServer.stop(); uploadServer = null; }
        if (avatarUploadServer != null) { avatarUploadServer.stop(); avatarUploadServer = null; }

        String ip = AvatarUploadServer.getLocalIpAddress(this);
        if (ip == null || "127.0.0.1".equals(ip)) {
            TvFeedback.show(this, R.string.tv_network_required, Toast.LENGTH_LONG);
            return;
        }

        avatarUploadServer = new AvatarUploadServer(this, profileId, file -> {
            if (isFinishing() || isDestroyed()) return false;
            boolean saved = ProfileManager.replaceCustomAvatar(this, profileId, file);
            if (saved) runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                updateTopBarUserIcon();
                if (onUpdated != null) onUpdated.run();
                if (qrStatusText != null) {
                    qrStatusText.setText(R.string.avatar_photo_updated);
                    qrStatusText.setTextColor(0xFF4ADE80);
                }
                if (qrUrlText != null) qrUrlText.setText(R.string.avatar_photo_saved);
                TvFeedback.show(this, R.string.avatar_photo_replaced, Toast.LENGTH_LONG);
            });
            return saved;
        });

        if (!avatarUploadServer.start()) {
            avatarUploadServer = null;
            TvFeedback.show(this, R.string.avatar_server_start_failed, Toast.LENGTH_LONG);
            return;
        }
        showPhoneUploadQrDialog(getString(R.string.avatar_qr_title),
                getString(R.string.avatar_qr_instructions),
                avatarUploadServer.getUrl());
    }

    void showWallpaperUploadQrDialog() {
        if (qrUploadDialog != null && qrUploadDialog.isShowing()) qrUploadDialog.dismiss();
        if (uploadServer != null) {
            uploadServer.stop();
            uploadServer = null;
        }

        String localIp = WallpaperUploadServer.getLocalIpAddress(this);
        if (localIp == null || "127.0.0.1".equals(localIp)) {
            TvFeedback.show(this, R.string.tv_network_required, Toast.LENGTH_LONG);
            return;
        }
        uploadServer = new WallpaperUploadServer(this, 8080, createUploadListener());
        if (!uploadServer.start()) {
            uploadServer = null;
            TvFeedback.show(this, R.string.local_upload_server_start_failed, Toast.LENGTH_LONG);
            return;
        }
        String uploadUrl = "http://" + uploadServer.getBoundIpAddress() + ":" + uploadServer.getPort() + uploadServer.getWallpaperPath();
        showPhoneUploadQrDialog(getString(R.string.wallpaper_qr_title),
                getString(R.string.wallpaper_qr_instructions),
                uploadUrl);
    }

    void showStandbyUploadQrDialog() {
        if (qrUploadDialog != null && qrUploadDialog.isShowing()) qrUploadDialog.dismiss();
        if (uploadServer != null) {
            uploadServer.stop();
            uploadServer = null;
        }

        String localIp = WallpaperUploadServer.getLocalIpAddress(this);
        if (localIp == null || "127.0.0.1".equals(localIp)) {
            TvFeedback.show(this, R.string.tv_network_required, Toast.LENGTH_LONG);
            return;
        }
        WallpaperUploadServer.UploadListener listener = new WallpaperUploadServer.UploadListener() {
            @Override public void onUploaded(File imageFile) {
                // Wallpaper uploads are intentionally not handled by the StandBy endpoint.
            }
            @Override public void onStandbyUploaded(File imageFile) {
                onStandbyPhotoUploaded(imageFile);
            }
        };
        uploadServer = new WallpaperUploadServer(this, 8080, listener, true);
        if (!uploadServer.start()) {
            uploadServer = null;
            TvFeedback.show(this, R.string.local_upload_server_start_failed, Toast.LENGTH_LONG);
            return;
        }
        String uploadUrl = "http://" + uploadServer.getBoundIpAddress() + ":" + uploadServer.getPort() + uploadServer.getStandbyPath();
        showPhoneUploadQrDialog(getString(R.string.standby_qr_title),
                getString(R.string.standby_qr_instructions),
                uploadUrl);
    }

    private void showPhoneUploadQrDialog(String titleText, String subtitleText, String uploadUrl) {
        Bitmap qrBmp = QrBitmapUtil.createQrBitmap(uploadUrl, Math.round(260 * density));
        if (qrBmp == null) {
            if (uploadServer != null) {
                uploadServer.stop();
                uploadServer = null;
            }
            if (avatarUploadServer != null) {
                avatarUploadServer.stop();
                avatarUploadServer = null;
            }
            TvFeedback.show(this, R.string.qr_create_failed, Toast.LENGTH_LONG);
            return;
        }

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        int padH = Math.round(36 * density);
        int padV = Math.round(28 * density);
        rootLayout.setPadding(padH, padV, padH, padV);
        // The shared B01 modal window owns the opaque glass surface.
        rootLayout.setBackgroundColor(Color.TRANSPARENT);

        TextView title = new TextView(this);
        title.setText(titleText);
        title.setTextColor(0xFFFFFFFF);
        title.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        title.setFontVariationSettings("'wght' 700");
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
        title.setShadowLayer(4 * density, 0, 1 * density, 0xDD000000);
        title.setGravity(Gravity.CENTER);
        rootLayout.addView(title);

        TextView sub = new TextView(this);
        sub.setText(subtitleText);
        sub.setTextColor(0xFFE2E8F0);
        sub.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        sub.setFontVariationSettings("'wght' 600");
        sub.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.5f);
        sub.setShadowLayer(3 * density, 0, 1 * density, 0xCC000000);
        sub.setGravity(Gravity.CENTER);
        sub.setPadding(0, Math.round(6 * density), 0, Math.round(18 * density));
        rootLayout.addView(sub);

        FrameLayout qrContainer = new FrameLayout(this);
        qrContainer.setBackgroundColor(0xFFFFFFFF);
        int qrPad = Math.round(12 * density);
        qrContainer.setPadding(qrPad, qrPad, qrPad, qrPad);
        LinearLayout.LayoutParams qrLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        qrLp.gravity = Gravity.CENTER_HORIZONTAL;
        qrContainer.setLayoutParams(qrLp);

        qrImageView = new ImageView(this);
        qrImageView.setImageBitmap(qrBmp);
        int qrDim = Math.round(220 * density);
        FrameLayout.LayoutParams imgLp = new FrameLayout.LayoutParams(qrDim, qrDim);
        imgLp.gravity = Gravity.CENTER;
        qrImageView.setLayoutParams(imgLp);
        qrContainer.addView(qrImageView);
        rootLayout.addView(qrContainer);

        qrUrlText = new TextView(this);
        qrUrlText.setText("🌐 " + uploadUrl);
        qrUrlText.setTextColor(TvUiTokens.ACCENT_BLUE);
        qrUrlText.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        qrUrlText.setFontVariationSettings("'wght' 700");
        qrUrlText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        qrUrlText.setShadowLayer(3 * density, 0, 1 * density, 0xCC000000);
        qrUrlText.setGravity(Gravity.CENTER);
        qrUrlText.setPadding(0, Math.round(14 * density), 0, Math.round(4 * density));
        rootLayout.addView(qrUrlText);

        qrStatusText = new TextView(this);
        qrStatusText.setText(R.string.qr_waiting_for_photo);
        qrStatusText.setTextColor(0xFFF1F5F9);
        qrStatusText.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        qrStatusText.setFontVariationSettings("'wght' 600");
        qrStatusText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        qrStatusText.setShadowLayer(3 * density, 0, 1 * density, 0xAA000000);
        qrStatusText.setGravity(Gravity.CENTER);
        qrStatusText.setPadding(0, 0, 0, Math.round(18 * density));
        rootLayout.addView(qrStatusText);

        Button btnClose = new Button(this);
        btnClose.setText(R.string.close_label);
        btnClose.setTextColor(0xFFFFFFFF);
        btnClose.setBackgroundResource(R.drawable.pill_bg);
        btnClose.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        btnClose.setFontVariationSettings("'wght' 700");
        btnClose.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        btnClose.setFocusable(true);
        btnClose.setFocusableInTouchMode(true);
        LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(
                Math.round(180 * density), Math.round(42 * density));
        btnLp.gravity = Gravity.CENTER_HORIZONTAL;
        btnClose.setLayoutParams(btnLp);
        rootLayout.addView(btnClose);

        TextView credit = new TextView(this);
        credit.setText("Glass TV • Pagar Alam");
        credit.setTextColor(0xFFCBD5E1);
        credit.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        credit.setFontVariationSettings("'wght' 600");
        credit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        credit.setGravity(Gravity.CENTER);
        credit.setPadding(0, Math.round(14 * density), 0, 0);
        rootLayout.addView(credit);

        ScrollView qrScroll = new ScrollView(this);
        qrScroll.setFillViewport(true);
        qrScroll.setClipToPadding(false);
        qrScroll.setFocusable(false);
        qrScroll.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        qrScroll.addView(rootLayout, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        qrUploadDialog = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog)
                .setView(qrScroll).create();
        btnClose.setOnKeyListener((view, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN) return false;
            int scrollStep = Math.max(1, qrScroll.getHeight() / 2);
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP && qrScroll.canScrollVertically(-1)) {
                qrScroll.smoothScrollBy(0, -scrollStep);
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && qrScroll.canScrollVertically(1)) {
                qrScroll.smoothScrollBy(0, scrollStep);
                return true;
            }
            return false;
        });
        btnClose.setOnClickListener(v -> qrUploadDialog.dismiss());
        qrUploadDialog.setOnDismissListener(dialog -> {
            wallpaperThumbnailGeneration.incrementAndGet();
            if (uploadServer != null) {
                uploadServer.stop();
                uploadServer = null;
            }
            if (avatarUploadServer != null) {
                avatarUploadServer.stop();
                avatarUploadServer = null;
            }
            qrUploadDialog = null;
            qrImageView = null;
            qrStatusText = null;
            qrUrlText = null;
        });
        qrUploadDialog.show();
        TvDialogStyle.apply(qrUploadDialog, this, 520, 620);
        btnClose.requestFocus();
    }

    private WallpaperUploadServer.UploadListener createUploadListener() {
        return new WallpaperUploadServer.UploadListener() {
            @Override
            public void onUploaded(File imageFile) {
                onCustomWallpaperUploaded(imageFile);
            }

            @Override
            public void onDeleted(File file) {
                onCustomWallpaperDeleted(file);
            }

            @Override
            public void onSelected(File file) {
                onCustomWallpaperSelected(file);
            }

            @Override
            public void onSlideshowMode() {
                onCustomWallpaperSlideshow();
            }

            @Override
            public void onCleared() {
                onCustomWallpaperCleared();
            }

            @Override
            public void onClearFailed() {
                onCustomWallpaperDeleteFailed();
            }
        };
    }

    private void onStandbyPhotoUploaded(File file) {
        runOnUiThread(() -> {
            if (file == null || !StandbyPhotoStore.getUris(this).contains(Uri.fromFile(file))) {
                if (file != null) file.delete();
                if (qrStatusText != null) qrStatusText.setText(R.string.standby_photo_save_failed);
                return;
            }
            standbyPhotoUriShown = "";
            if (screensaverActive) updateNightstandScreensaver();
            int count = StandbyPhotoStore.getPhotoCount(this);
            if (qrStatusText != null) qrStatusText.setText(getString(R.string.standby_photo_saved_count, count));
            if (qrUrlText != null) qrUrlText.setText(getString(R.string.standby_collection_count, count, StandbyPhotoStore.MAX_PHOTO_COUNT));
        });
    }

    private void onCustomWallpaperUploaded(File file) {
        runOnUiThread(() -> {
            SoundEffects.playSelect();
            TvFeedback.show(this, R.string.wallpaper_uploaded_from_phone, Toast.LENGTH_LONG);

            getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).edit()
                    .putString("active_category", "Custom")
                    .putBoolean("custom_single_mode", false)
                    .remove("custom_single_file")
                    .apply();

            placesMode = false;
            if (heroShelf != null) {
                heroShelf.animate().cancel();
                heroShelf.setAlpha(0f);
                heroShelf.setVisibility(View.GONE);
            }
            File customDir = new File(getFilesDir(), "custom_wallpapers");
            File[] files = customDir.listFiles((dir, name) -> UploadFilePolicy.isSafeStoredImageName(name));
            if (files != null && files.length > 0) {
                Arrays.sort(files, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
                wallpapers = new ArrayList<>(Arrays.asList(files));
            } else {
                wallpapers = Collections.singletonList(file);
            }

            getSharedPreferences("wallpaper", MODE_PRIVATE).edit().putInt("index", -1).apply();
            showNextWallpaper(true);

            if (qrStatusText != null) {
                qrStatusText.setText(R.string.wallpaper_upload_success);
                qrStatusText.setTextColor(0xFF4ADE80);
            }
            if (qrUrlText != null) {
                qrUrlText.setText(getString(R.string.wallpaper_active_file, file.getName()));
                qrUrlText.setTextColor(0xFF4ADE80);
            }
            queueWallpaperThumbnail(file);

            handler.postDelayed(() -> {
                if (qrUploadDialog != null && qrUploadDialog.isShowing()) {
                    qrUploadDialog.dismiss();
                }
            }, 3000);
        });
    }

    private void onCustomWallpaperSelected(File file) {
        runOnUiThread(() -> {
            SoundEffects.playSelect();
            getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).edit()
                    .putString("active_category", "Custom")
                    .putBoolean("custom_single_mode", true)
                    .putString("custom_single_file", file.getName())
                    .apply();

            placesMode = false;
            if (heroShelf != null) {
                heroShelf.animate().cancel();
                heroShelf.setAlpha(0f);
                heroShelf.setVisibility(View.GONE);
            }
            wallpapers = Collections.singletonList(file);
            getSharedPreferences("wallpaper", MODE_PRIVATE).edit().putInt("index", -1).apply();
            showNextWallpaper(true);
            TvFeedback.show(this, getString(R.string.wallpaper_pinned, file.getName()), Toast.LENGTH_SHORT);
        });
    }

    private void onCustomWallpaperSlideshow() {
        runOnUiThread(() -> {
            SoundEffects.playSelect();
            getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).edit()
                    .putString("active_category", "Custom")
                    .putBoolean("custom_single_mode", false)
                    .remove("custom_single_file")
                    .apply();

            placesMode = false;
            if (heroShelf != null) {
                heroShelf.animate().cancel();
                heroShelf.setAlpha(0f);
                heroShelf.setVisibility(View.GONE);
            }

            File customDir = new File(getFilesDir(), "custom_wallpapers");
            File[] files = customDir.listFiles((dir, name) -> UploadFilePolicy.isSafeStoredImageName(name));
            if (files != null && files.length > 0) {
                Arrays.sort(files, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
                wallpapers = new ArrayList<>(Arrays.asList(files));
                getSharedPreferences("wallpaper", MODE_PRIVATE).edit().putInt("index", -1).apply();
                showNextWallpaper(true);
                startWallpaperRotation();
                TvFeedback.show(this, getString(R.string.wallpaper_shuffle_enabled, files.length), Toast.LENGTH_SHORT);
            } else {
                switchWallpaperCategory("Natural");
            }
        });
    }

    private void onCustomWallpaperDeleted(File file) {
        runOnUiThread(() -> {
            String singleFile = getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getString("custom_single_file", null);
            if (singleFile != null && singleFile.equals(file.getName())) {
                getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).edit()
                        .putBoolean("custom_single_mode", false)
                        .remove("custom_single_file")
                        .apply();
            }

            File customDir = new File(getFilesDir(), "custom_wallpapers");
            File[] files = customDir.listFiles((dir, name) -> UploadFilePolicy.isSafeStoredImageName(name));
            String activeCat = getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getString("active_category", "Natural");

            if ("Custom".equalsIgnoreCase(activeCat)) {
                if (files != null && files.length > 0) {
                    Arrays.sort(files, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
                    wallpapers = new ArrayList<>(Arrays.asList(files));
                    getSharedPreferences("wallpaper", MODE_PRIVATE).edit().putInt("index", -1).apply();
                    showNextWallpaper(true);
                    TvFeedback.show(this, getString(R.string.wallpaper_deleted_remaining, files.length), Toast.LENGTH_SHORT);
                } else {
                    TvFeedback.show(this, R.string.wallpaper_all_deleted_default, Toast.LENGTH_SHORT);
                    switchWallpaperCategory("Natural");
                }
            } else {
                TvFeedback.show(this, R.string.wallpaper_removed_storage, Toast.LENGTH_SHORT);
            }
        });
    }

    /** Reconcile the active custom-wallpaper view when a bulk delete only partly succeeds. */
    private void onCustomWallpaperDeleteFailed() {
        runOnUiThread(() -> {
            android.content.SharedPreferences prefs = getSharedPreferences("wallpaper_prefs", MODE_PRIVATE);
            prefs.edit().putBoolean("custom_single_mode", false).remove("custom_single_file").apply();

            String activeCat = prefs.getString("active_category", "Natural");
            if ("Custom".equalsIgnoreCase(activeCat)) {
                File customDir = new File(getFilesDir(), "custom_wallpapers");
                File[] remaining = customDir.listFiles(
                        (dir, name) -> UploadFilePolicy.isSafeStoredImageName(name));
                if (remaining == null) {
                    switchWallpaperCategory("Natural");
                } else if (remaining.length == 0) {
                    onCustomWallpaperCleared();
                    return;
                } else {
                    Arrays.sort(remaining, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
                    wallpapers = new ArrayList<>(Arrays.asList(remaining));
                    placesMode = false;
                    if (heroShelf != null) {
                        heroShelf.animate().cancel();
                        heroShelf.setAlpha(0f);
                        heroShelf.setVisibility(View.GONE);
                    }
                    getSharedPreferences("wallpaper", MODE_PRIVATE).edit().putInt("index", -1).apply();
                    showNextWallpaper(true);
                }
            }
            TvFeedback.show(this, R.string.wallpaper_delete_failed, Toast.LENGTH_LONG);
        });
    }

    private void onCustomWallpaperCleared() {
        runOnUiThread(() -> {
            getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).edit()
                    .putBoolean("custom_single_mode", false)
                    .remove("custom_single_file")
                    .apply();

            String activeCat = getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getString("active_category", "Natural");
            if ("Custom".equalsIgnoreCase(activeCat)) {
                switchWallpaperCategory("Natural");
            }
            TvFeedback.show(this, R.string.wallpaper_all_uploaded_deleted, Toast.LENGTH_SHORT);
        });
    }

    void showCustomWallpaperManagerDialog() {
        showCustomWallpaperManagerDialog(true);
    }

    private void showCustomWallpaperManagerDialog(boolean returnToQuickControl) {
        File customDir = new File(getFilesDir(), "custom_wallpapers");
        File[] files = customDir.listFiles((dir, name) -> UploadFilePolicy.isSafeStoredImageName(name));
        if (files == null || files.length == 0) {
            AlertDialog dialog = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog)
                    .setTitle(R.string.wallpaper_manage_title)
                    .setMessage(R.string.wallpaper_manage_empty)
                    .setPositiveButton(R.string.wallpaper_open_qr_action, (d, w) -> showWallpaperUploadQrDialog())
                    .setNegativeButton(R.string.back_action, (d, w) -> {
                        if (returnToQuickControl) showWallpaperDialog();
                    })
                    .show();
            styleGlassDialog(dialog);
            return;
        }

        Arrays.sort(files, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));

        boolean isSingleMode = getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getBoolean("custom_single_mode", false);
        String singleFile = getSharedPreferences("wallpaper_prefs", MODE_PRIVATE).getString("custom_single_file", "");

        List<String> items = new ArrayList<>();
        List<Runnable> actions = new ArrayList<>();

        // Quick Mode Actions at top
        items.add(getString(R.string.wallpaper_upload_new_phone));
        actions.add(this::showWallpaperUploadQrDialog);

        items.add(getString(R.string.wallpaper_slideshow_all));
        actions.add(this::onCustomWallpaperSlideshow);

        items.add(getString(R.string.wallpaper_delete_all_uploaded));
        actions.add(() -> {
            AlertDialog dialog = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog)
                    .setTitle(R.string.delete_all_wallpapers_title)
                    .setMessage(getString(R.string.delete_all_wallpapers_message, files.length))
                    .setPositiveButton(R.string.delete_all_action, (d, w) -> {
                        boolean allDeleted = true;
                        for (File f : files) {
                            if (f.exists() && !f.delete()) allDeleted = false;
                        }
                        if (allDeleted) onCustomWallpaperCleared();
                        else onCustomWallpaperDeleteFailed();
                    })
                    .setNegativeButton(R.string.cancel_action, null)
                    .show();
            styleGlassDialog(dialog);
        });

        SimpleDateFormat sdf = new SimpleDateFormat("d MMM, HH:mm", Locale.getDefault());
        for (File f : files) {
            double kb = f.length() / 1024.0;
            String sizeStr = kb > 1024 ? String.format(Locale.ROOT, "%.1f MB", kb / 1024.0) : String.format(Locale.ROOT, "%.0f KB", kb);
            boolean isCurrent = isSingleMode && f.getName().equals(singleFile);
            String prefix = isCurrent ? getString(R.string.wallpaper_pinned_prefix) : "🖼️ ";
            items.add(prefix + f.getName() + " (" + sizeStr + " • " + sdf.format(new Date(f.lastModified())) + ")");
            actions.add(() -> showSingleCustomActionDialog(f, returnToQuickControl));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setTitle(getString(R.string.tv_wallpaper_manager_title, files.length));
        builder.setItems(items.toArray(new String[0]), (dialog, which) -> {
            if (which >= 0 && which < actions.size()) {
                actions.get(which).run();
            }
        });
        builder.setNegativeButton(R.string.close_label, (dialog, which) -> {
            dialog.dismiss();
            if (returnToQuickControl) showWallpaperDialog();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        styleGlassDialog(dialog, 560, 460);
    }

    private void showSingleCustomActionDialog(File file, boolean returnToQuickControl) {
        String[] actions = {
                getString(R.string.wallpaper_pin_single),
                getString(R.string.wallpaper_delete_one),
                getString(R.string.wallpaper_keep_only_one),
                getString(R.string.back_action)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setTitle("🖼️ " + file.getName());
        builder.setItems(actions, (dialog, which) -> {
            switch (which) {
                case 0: // Lock single
                    onCustomWallpaperSelected(file);
                    break;
                case 1: // Delete this
                    if (!file.exists() || file.delete()) {
                        onCustomWallpaperDeleted(file);
                    } else {
                        TvFeedback.show(this, R.string.wallpaper_delete_failed, Toast.LENGTH_LONG);
                    }
                    break;
                case 2: // Keep this, delete others
                    File customDir = new File(getFilesDir(), "custom_wallpapers");
                    File[] allFiles = customDir.listFiles((dir, name) -> UploadFilePolicy.isSafeStoredImageName(name));
                    boolean allDeleted = allFiles != null;
                    if (allFiles != null) {
                        for (File f : allFiles) {
                            if (!f.getName().equals(file.getName()) && f.exists() && !f.delete()) {
                                allDeleted = false;
                            }
                        }
                    }
                    onCustomWallpaperSelected(file);
                    TvFeedback.show(this, allDeleted
                            ? R.string.wallpaper_other_deleted
                            : R.string.wallpaper_delete_failed, Toast.LENGTH_LONG);
                    break;
                case 3: // Back
                    showCustomWallpaperManagerDialog(returnToQuickControl);
                    break;
            }
        });
        builder.setNegativeButton(R.string.cancel_action, null);
        AlertDialog dialog = builder.create();
        dialog.show();
        styleGlassDialog(dialog);
    }

    private void showHazeLocationDialog() {
        startActivity(new Intent(this, LocationActivity.class));
    }

    private void showBraviaLocationDialog() {
        startActivity(new Intent(this, LocationActivity.class));
    }

    private void showAsiaCountryDialog() {
        final List<String> regions = AppLocationManager.getAvailableRegions();
        String activeRegion = AppLocationManager.getActiveRegion(this);
        int selectedIndex = 0;
        CharSequence[] items = new CharSequence[regions.size()];
        for (int i = 0; i < regions.size(); i++) {
            String r = regions.get(i);
            items[i] = r;
            if (r.equalsIgnoreCase(activeRegion)) {
                selectedIndex = i;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setTitle(R.string.weather_region_picker_title);
        builder.setSingleChoiceItems(items, selectedIndex, (dialog, which) -> {
            dialog.dismiss();
            String chosenRegion = regions.get(which);
            AppLocationManager.setActiveRegion(MainActivity.this, chosenRegion);
            TvFeedback.show(MainActivity.this, getString(R.string.weather_region_selected, chosenRegion), Toast.LENGTH_SHORT);
            showStationListDialog(chosenRegion);
        });
        builder.setNegativeButton(R.string.back_action, (dialog, which) -> {
            dialog.dismiss();
            showHazeLocationDialog();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        styleGlassDialog(dialog);
    }

    private void showStationListDialog(String region) {
        final List<AppLocationManager.Station> stations = AppLocationManager.getStationsForRegion(region);
        String currentId = AppLocationManager.getStationId(this);
        int selectedIndex = 0;
        CharSequence[] items = new CharSequence[stations.size()];
        for (int i = 0; i < stations.size(); i++) {
            AppLocationManager.Station s = stations.get(i);
            items[i] = s.getDisplayName() + "\n" + s.place;
            if (s.id.equalsIgnoreCase(currentId)) {
                selectedIndex = i;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setTitle(getString(R.string.weather_station_picker_title, region));
        builder.setSingleChoiceItems(items, selectedIndex, (dialog, which) -> {
            dialog.dismiss();
            AppLocationManager.Station chosen = stations.get(which);
            AppLocationManager.setStation(MainActivity.this, chosen);
            TvFeedback.show(MainActivity.this, getString(R.string.weather_location_selected, chosen.name, chosen.country), Toast.LENGTH_SHORT);
            refreshLocationDependentData();
        });
        builder.setNegativeButton(R.string.back_action, (dialog, which) -> {
            dialog.dismiss();
            showHazeLocationDialog();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        styleGlassDialog(dialog);
    }

    private void showPrayerZoneDialog() {
        startActivity(new Intent(this, LocationActivity.class));
    }

    /** Shows an Apple TV-style frosted glass Putrajaya prayer timetable popup. */
    private void showPrayerPopup() {
        if (openMenu != null) return;
        SoundEffects.playSelect();
        View anchor = findViewById(R.id.prayerTable);
        if (anchor == null || !anchor.isShown()) {
            anchor = prayerTimesView != null ? prayerTimesView : findViewById(R.id.leftBlock);
        }
        FrameLayout panel = (FrameLayout) getLayoutInflater().inflate(R.layout.popup_inputs, root, false);
        LinearLayout rows = panel.findViewById(R.id.rows);
        Runnable close = () -> closeMenu(false);

        // Header: Active Prayer Zone
        addInputRow(rows, close, R.drawable.ic_settings, "Waktu Sholat " + PrayerTimes.getShortZoneName(this) + " (Tukar Zon ▶)", TvInputManager.INPUT_STATE_CONNECTED, () -> {
            SoundEffects.playSelect();
            closeMenu(false);
            startActivity(new Intent(MainActivity.this, LocationActivity.class));
        });

        // Date row: Islamic Hijri
        addInputRow(rows, close, R.drawable.ic_input, PrayerTimes.getHijriDate(), -1, () -> {});

        addSeparator(rows);

        // Prayer times list
        java.util.List<PrayerTimes.ScheduleItem> schedule = PrayerTimes.getFullSchedule(this);
        for (PrayerTimes.ScheduleItem item : schedule) {
            addPrayerRow(rows, close, item.name, item.timeFormatted, item.isNext);
        }

        addSeparator(rows);

        // Tetapan Azan shortcut
        addInputRow(rows, close, R.drawable.ic_azan, getString(R.string.azan_settings_menu_label), -1, () -> {
            SoundEffects.playSelect();
            closeMenu(false);
            showAzanSettingsDialog();
        });

        // Tutup
        addInputRow(rows, close, R.drawable.ic_close, getString(R.string.close_label), -1, () -> {
            SoundEffects.playSelect();
            closeMenu(true);
        });

        int width = (int) (270 * density);
        panel.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        width = Math.max(width, panel.getMeasuredWidth());

        showMenu(panel, rows, anchor, width, false);
    }

    private void addPrayerRow(LinearLayout rows, Runnable close, String name, String time, boolean isNext) {
        View row = getLayoutInflater().inflate(R.layout.item_input, rows, false);
        ((ImageView) row.findViewById(R.id.icon)).setImageResource(R.drawable.ic_input);
        TextView title = row.findViewById(R.id.title);
        title.setText(name + (isNext ? getString(R.string.next_prayer_suffix) : ""));
        title.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        title.setFontVariationSettings(isNext ? "'wght' 700" : "'wght' 650");
        title.setShadowLayer(3 * density, 0, 1 * density, 0xCC000000);

        TextView status = row.findViewById(R.id.status);
        View dot = row.findViewById(R.id.dot);
        status.setText(time);
        status.setVisibility(View.VISIBLE);
        status.setTypeface(getResources().getFont(R.font.inter), Typeface.BOLD);
        status.setFontVariationSettings(isNext ? "'wght' 700" : "'wght' 600");
        status.setShadowLayer(3 * density, 0, 1 * density, 0xCC000000);

        if (isNext) {
            dot.setVisibility(View.VISIBLE);
        } else {
            dot.setVisibility(View.GONE);
        }
        row.setOnClickListener(v -> {
            SoundEffects.playSelect();
            close.run();
        });
        rows.addView(row);
    }

    /** Refreshes only the manually opened in-launcher nightstand; Android owns idle transitions. */
    private void refreshManualNightstand() {
        if (screensaverActive && screensaverView != null && !isFinishing() && !isDestroyed()) {
            updateNightstandScreensaver();
        }
    }

    void showNightstandScreensaver() {
        if (screensaverActive || isFinishing() || isDestroyed()) return;
        if (wallpaperFullscreen) {
            setWallpaperFullscreen(false);
        }
        screensaverActive = true;
        updateHomeTabIndicator();
        if (screensaverView == null) {
            screensaverView = getLayoutInflater().inflate(R.layout.view_nightstand, root, false);
            screensaverView.setOnClickListener(v -> dismissScreensaver());
            root.addView(screensaverView, new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        updateNightstandScreensaver();
        handler.removeCallbacks(standbyPhotoTick);
        handler.postDelayed(standbyPhotoTick, StandbyPhotoStore.ROTATION_INTERVAL_MS);

        screensaverView.setVisibility(View.VISIBLE);
        screensaverView.setAlpha(0f);
        screensaverView.animate().alpha(1f).setDuration(500).start();
        android.util.Log.i("TvHome", "Nightstand screensaver activated");
    }

    private void updateNightstandScreensaver() {
        if (screensaverView == null) return;
        updateNightstandPhoto();
        TextView hijri = screensaverView.findViewById(R.id.nightstandHijri);
        if (hijri != null) hijri.setText("🌙 " + PrayerTimes.getHijriDate());

        TextView prayer = screensaverView.findViewById(R.id.nightstandPrayer);
        if (prayer != null) prayer.setText("🕌 " + PrayerTimes.getNextPrayerSummary(this));

        // Center locked strictly with no movement
        View content = screensaverView.findViewById(R.id.nightstandContent);
        if (content != null) {
            content.animate().cancel();
            content.setTranslationX(0f);
            content.setTranslationY(0f);
        }

        // iPhone StandBy Wind Down Mode: 10:00 PM (22:00) to 6:00 AM (06:00) -> Red Monochrome
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int hour = cal.get(java.util.Calendar.HOUR_OF_DAY);
        boolean isNightMode = (hour >= 22 || hour < 6);

        TextView clock = screensaverView.findViewById(R.id.nightstandClock);
        TextView ampm = screensaverView.findViewById(R.id.nightstandAmPm);
        TextView date = screensaverView.findViewById(R.id.nightstandDate);
        TextView hint = screensaverView.findViewById(R.id.nightstandHint);

        if (isNightMode) {
            int red = 0xFFFF453A;
            if (clock != null) clock.setTextColor(red);
            if (ampm != null) ampm.setTextColor(0xCCFF453A);
            if (date != null) date.setTextColor(0xE6FF453A);
            if (hijri != null) hijri.setTextColor(0xE6FF453A);
            if (prayer != null) {
                prayer.setTextColor(red);
                android.graphics.drawable.GradientDrawable redPill = new android.graphics.drawable.GradientDrawable();
                redPill.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
                redPill.setColor(0x26FF453A);
                redPill.setStroke((int) (1 * density), 0x59FF453A);
                redPill.setCornerRadius(21 * density);
                prayer.setBackground(redPill);
            }
            if (hint != null) hint.setTextColor(0x55FF453A);
        } else {
            if (clock != null) clock.setTextColor(0xFFFFFFFF);
            if (ampm != null) ampm.setTextColor(0xB3FFFFFF);
            if (date != null) date.setTextColor(0xE6FFFFFF);
            if (hijri != null) hijri.setTextColor(TvUiTokens.TEXT_SECONDARY);
            if (prayer != null) {
                prayer.setTextColor(0xFFFFFFFF);
                prayer.setBackgroundResource(R.drawable.pill_bg);
            }
            if (hint != null) hint.setTextColor(0x4DFFFFFF);
        }
    }

    private void updateNightstandPhoto() {
        if (screensaverView == null) return;
        ImageView photo = screensaverView.findViewById(R.id.nightstandPhoto);
        View scrim = screensaverView.findViewById(R.id.nightstandPhotoScrim);
        if (photo == null || scrim == null) return;

        List<Uri> uris = StandbyPhotoStore.getUris(this);
        if (!StandbyPhotoStore.isPhotoMode(this) || uris.isEmpty()) {
            photo.setImageDrawable(null);
            photo.setVisibility(View.GONE);
            scrim.setVisibility(View.GONE);
            standbyPhotoUriShown = "";
            return;
        }

        int index = StandbyPhotoStore.getCurrentPhotoIndex(uris.size());
        if (index < 0 || index >= uris.size()) return;
        Uri uri = uris.get(index);
        photo.setVisibility(View.VISIBLE);
        scrim.setVisibility(View.VISIBLE);
        if (!uri.toString().equals(standbyPhotoUriShown)) {
            standbyPhotoUriShown = uri.toString();
            StandbyPhotoStore.loadBitmapAsync(this, uri, bitmap -> {
                if (bitmap == null) {
                    if (uri.toString().equals(standbyPhotoUriShown)) {
                        photo.setImageDrawable(null);
                        photo.setVisibility(View.GONE);
                        scrim.setVisibility(View.GONE);
                        android.util.Log.w("TvHome.StandBy", "Skipping unreadable StandBy photo: " + uri);
                    }
                    return;
                }
                if (!screensaverActive || screensaverView == null
                        || !StandbyPhotoStore.isPhotoMode(this)
                        || !uri.toString().equals(standbyPhotoUriShown)) {
                    bitmap.recycle();
                    return;
                }
                photo.setImageBitmap(bitmap);
            });
        }
    }

    private void dismissScreensaver() {
        if (!screensaverActive || screensaverView == null) return;
        screensaverActive = false;
        updateHomeTabIndicator();
        handler.removeCallbacks(standbyPhotoTick);
        screensaverView.animate().alpha(0f).setDuration(350).withEndAction(() -> {
            if (!screensaverActive && screensaverView != null) {
                screensaverView.setVisibility(View.GONE);
            }
        }).start();
        android.util.Log.i("TvHome", "Nightstand screensaver dismissed");
    }

    /**
     * China-market MIUI TV firmware answers no standard Settings intents at all; open Xiaomi's own
     * settings app (com.xiaomi.mitv.settings) there, so the Settings and Wi-Fi pills keep working.
     * On firmware with a standard settings handler the behaviour is unchanged.
     */
    Intent systemIntent(String standardAction) {
        Intent i = new Intent(standardAction);
        if (i.resolveActivity(getPackageManager()) != null) return i;
        i = new Intent(standardAction).setComponent(new ComponentName(
                "com.xiaomi.mitv.settings", "com.xiaomi.mitv.settings.entry.MainActivity"));
        if (i.resolveActivity(getPackageManager()) != null) return i;
        return new Intent(standardAction);   // nothing found: let open() show the can't-open toast
    }

    /** Hides the Inputs pill when it would have nothing to list: streaming boxes have no HDMI-in, AV or tuner. */
    private void updateInputsButton() {
        boolean any = liveTvIntent() != null || !sourcePanelService.isEmpty();
        TvInputManager tim = (TvInputManager) getSystemService(TV_INPUT_SERVICE);
        if (!any && tim != null) {
            for (TvInputInfo info : tim.getTvInputList()) {
                if (info.isPassthroughInput()) {
                    any = true;
                    break;
                }
            }
        }
        btnInputs.setVisibility(any ? View.VISIBLE : View.GONE);
    }

    /** The Live TV app's launch intent: its leanback entry point, or the configured action if it has none. */
    private Intent liveTvIntent() {
        if (liveTvPkg.isEmpty()) return null;
        Intent i = getPackageManager().getLeanbackLaunchIntentForPackage(liveTvPkg);
        if (i != null || liveTvAction.isEmpty()) return i;
        Intent byAction = new Intent(liveTvAction).setPackage(liveTvPkg);
        return getPackageManager().resolveActivity(byAction, 0) != null ? byAction : null;
    }

    /** The TV's own source-selection panel, for TVs that have no TvInputManager (TCL Android TV 9). */
    private void openSourcePanel() {
        Intent i = new Intent(sourcePanelAction);
        ComponentName c = ComponentName.unflattenFromString(sourcePanelService);
        if (c != null) i.setComponent(c);
        try {
            startService(i);
        } catch (Exception e) {
            TvFeedback.show(this, R.string.not_available, Toast.LENGTH_SHORT);
        }
    }

    private void openSystemSettings(Intent intent) {
        SystemSettingsAccess.open(this, intent);
    }

    private void open(Intent i) {
        tryOpen(i);
    }

    private boolean tryOpen(Intent i) {
        SoundEffects.playSelect();
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(i);
            return true;
        } catch (ActivityNotFoundException e) {
            TvFeedback.show(this, R.string.not_available, Toast.LENGTH_SHORT);
            return false;
        }
    }

    /** Switches to a passthrough input (HDMI, AV). The standard way is ACTION_VIEW on its channel URI,
     *  which the Live TV app handles; where nothing takes that, the configured `input_action` is sent
     *  with the same URI instead (XGIMI's HDMI player only answers com.xgimi.action.hdmiPlayer). */
    private void openInput(String inputId) {
        Uri uri = TvContract.buildChannelUriForPassthroughInput(inputId);
        Intent view = new Intent(Intent.ACTION_VIEW).setDataAndType(uri, "vnd.android.cursor.item/channel")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(view);
            return;
        } catch (ActivityNotFoundException e) {
            if (inputAction.isEmpty()) {
                TvFeedback.show(this, R.string.not_available, Toast.LENGTH_SHORT);
                return;
            }
        }
        open(new Intent(inputAction).setDataAndType(uri, "vnd.android.cursor.item/channel"));
    }

    /** The glass menu while one is open (Inputs, or an app's), and what it opened from. */
    private View openMenu;
    private View menuAnchor;
    private Runnable menuReturnAction;
    private final int[] menuAt = new int[2], menuGliderAt = new int[2];

    /**
     * Menu under the Inputs pill: HDMI ports (with connection state), AV and Live TV. It is drawn in
     * Home's own window rather than a popup window of its own: a new window takes about a quarter of
     * a second to reach the screen on this TV, so the selection could not glide out of the pill into
     * it. Here Home's own focus shape glides from the pill to the first row, the way it glides into
     * the dock's tray, and the menu's glass paints it over itself.
     */
    private void showInputs() {
        if (openMenu != null) return;
        SoundEffects.playSelect();
        View anchor = btnInputs;
        FrameLayout panel = (FrameLayout) getLayoutInflater().inflate(R.layout.popup_inputs, root, false);
        LinearLayout rows = panel.findViewById(R.id.rows);
        Runnable close = () -> closeMenu(false);

        // Compact: as wide as the Inputs pill (a touch wider so "HDMI 1" fits), hanging right below it.
        int width = Math.max(anchor.getWidth(), (int) (150 * density));
        TvInputManager tim = (TvInputManager) getSystemService(TV_INPUT_SERVICE);
        boolean sawNonHdmi = false;
        String tunerId = null;
        boolean tunerIsHw = false;
        if (tim != null) {
            List<TvInputInfo> inputs = new ArrayList<>(tim.getTvInputList());
            // HDMI ports first in port order, then AV and the rest.
            Collections.sort(inputs, (a, b) -> {
                boolean ha = a.getType() == TvInputInfo.TYPE_HDMI, hb = b.getType() == TvInputInfo.TYPE_HDMI;
                if (ha != hb) return ha ? -1 : 1;
                return String.valueOf(a.loadLabel(this)).compareToIgnoreCase(String.valueOf(b.loadLabel(this)));
            });
            // Some TVs (Sony) report every HDMI port as connected whether or not anything is plugged in,
            // so drop the HDMI status then. A TV with every port really in use loses it too.
            int hdmiPorts = 0, hdmiConnected = 0;
            for (TvInputInfo info : inputs) {
                if (!info.isPassthroughInput() || info.getType() != TvInputInfo.TYPE_HDMI) continue;
                hdmiPorts++;
                if (tim.getInputState(info.getId()) == TvInputManager.INPUT_STATE_CONNECTED) hdmiConnected++;
            }
            boolean hdmiStateBogus = hdmiPorts > 1 && hdmiConnected == hdmiPorts;
            for (TvInputInfo info : inputs) {
                if (info.getType() == TvInputInfo.TYPE_TUNER
                        && (info.getServiceInfo().applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    // Prefer a hardware tuner: the framework names those ".../HW<n>". System apps such as
                    // AirPlay also register software inputs of type TUNER and may sort first (seen on TCL G05).
                    boolean hw = info.getId().matches(".*/HW\\d+$");
                    if (tunerId == null || (hw && !tunerIsHw) || (hw == tunerIsHw && info.getId().compareTo(tunerId) < 0)) {
                        tunerId = info.getId();
                        tunerIsHw = hw;
                    }
                }
                if (!info.isPassthroughInput()) continue;
                CharSequence l = info.loadLabel(this);
                String label = l == null ? info.getId() : l.toString();
                boolean hdmi = info.getType() == TvInputInfo.TYPE_HDMI;
                if (hdmi && !label.toUpperCase(Locale.ROOT).contains("HDMI")) label = "HDMI " + label;
                final String id = info.getId();
                int state = hdmi && hdmiStateBogus ? -1 : tim.getInputState(id);
                if (!hdmi && rows.getChildCount() > 0 && !sawNonHdmi) addSeparator(rows);
                if (!hdmi) sawNonHdmi = true;
                addInputRow(rows, close, hdmi ? R.drawable.ic_hdmi : R.drawable.ic_av, label, state,
                        () -> openInput(id));
            }
        }
        // Nothing to list where there is no TvInputManager: the TV's own source panel switches inputs.
        if (rows.getChildCount() == 0 && !sourcePanelService.isEmpty()) {
            addInputRow(rows, close, R.drawable.ic_input, getString(R.string.inputs), -1, this::openSourcePanel);
        }
        Intent liveTv = liveTvIntent();
        if (liveTv != null && tunerId != null) {
            liveTv = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(TvContract.buildChannelUriForPassthroughInput(tunerId), "vnd.android.cursor.item/channel")
                    .setPackage(liveTvPkg);
        }
        if (liveTv != null && config.showLiveTv) {
            if (rows.getChildCount() > 0) addSeparator(rows);
            final Intent liveTvIntent = liveTv;
            addInputRow(rows, close, R.drawable.ic_livetv, getString(R.string.live_tv), -1, () -> open(liveTvIntent));
        }
        if (rows.getChildCount() == 0) {
            TvFeedback.show(this, R.string.no_inputs, Toast.LENGTH_SHORT);
            return;
        }

        // Fit the widest row: TVs that report a status on every port (Sony) would otherwise squeeze the names out.
        panel.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        width = Math.max(width, panel.getMeasuredWidth());

        showMenu(panel, rows, anchor, width, false);
    }

    /**
     * Shows a glass menu in Home's own window, under {@code anchor} or, with {@code above}, over it:
     * Home's focus shape glides from the anchor to the first row, the menu's glass paints it over
     * itself, and the glass and rows fade in around it. Up from the first row (Down from the last when
     * the menu is above), or Back, closes it and the shape glides back to the anchor.
     */
    private void showMenu(FrameLayout panel, LinearLayout rows, View anchor, int width, boolean above) {
        menuReturnAction = null;
        GlassDrawable glass = new GlassDrawable(panel, 20 * density, density, glider, 0.5f, true);
        panel.setBackground(glass);
        Backdrop.watch(panel);
        glider.addCompanion(panel);
        anchor.getLocationInWindow(menuAt);
        root.getLocationInWindow(menuGliderAt);

        int safeX = TvResponsiveLayout.safeInsetPx(root.getWidth());
        int safeY = TvResponsiveLayout.safeInsetPx(root.getHeight());
        width = Math.max(1, Math.min(width, root.getWidth() - 2 * safeX));
        int gap = (int) (10 * density);
        int anchorTop = menuAt[1] - menuGliderAt[1];
        panel.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.UNSPECIFIED);
        int contentHeight = Math.max(1, panel.getMeasuredHeight());
        int safeHeight = Math.max(1, root.getHeight() - 2 * safeY);
        int anchorBottom = anchorTop + anchor.getHeight();
        int aboveSpace = Math.max(0, anchorTop - gap - safeY);
        int belowSpace = Math.max(0, root.getHeight() - safeY - anchorBottom - gap);
        boolean placeAbove = TvResponsiveLayout.shouldPlacePopupAbove(
                aboveSpace, belowSpace, contentHeight, safeHeight, above);
        int availableHeight = placeAbove ? aboveSpace : belowSpace;
        int height = Math.max(1, Math.min(contentHeight, availableHeight));
        int left = TvResponsiveLayout.popupLeftPx(
                menuAt[0] - menuGliderAt[0], width, root.getWidth());
        int popupTop = placeAbove ? anchorTop - gap - height : anchorBottom + gap;
        popupTop = Math.max(safeY, Math.min(popupTop, root.getHeight() - safeY - height));
        final boolean opensAbove = placeAbove;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
        lp.leftMargin = left;
        lp.topMargin = popupTop;
        root.addView(panel, lp);
        openMenu = panel;
        menuAnchor = anchor;

        View first = null, last = null;
        for (int i = 0; i < rows.getChildCount(); i++) {
            View row = rows.getChildAt(i);
            if (!row.isFocusable()) continue;
            if (first == null) first = row;
            last = row;
            row.setOnFocusChangeListener((v, has) -> {
                if (has) glideToMenuRow(panel, rows, v);
            });
        }
        final View top = first, bottom = last;
        for (int i = 0; i < rows.getChildCount(); i++) {
            View row = rows.getChildAt(i);
            if (!row.isFocusable()) continue;
            row.setOnKeyListener((v, keyCode, event) -> {
                boolean down = event.getAction() == KeyEvent.ACTION_DOWN;
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                        if (v != top) return false;
                        if (down && !opensAbove) closeMenu(true);
                        return true;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        if (v != bottom) return false;
                        if (down && opensAbove) closeMenu(true);
                        return true;
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        return true;
                    case KeyEvent.KEYCODE_BACK:
                        if (down) closeMenu(true);
                        return true;
                    default:
                        return false;
                }
            });
        }

        glass.setAlpha(0);
        rows.setAlpha(0f);
        final float slide = (opensAbove ? 8 : -8) * density;
        rows.setTranslationY(slide);
        android.animation.ValueAnimator in = android.animation.ValueAnimator.ofFloat(0f, 1f);
        in.setDuration(reducedMotion ? 120 : 220);
        in.setInterpolator(new android.view.animation.DecelerateInterpolator(1.6f));
        in.addUpdateListener(an -> {
            float f = (float) an.getAnimatedValue();
            glass.setAlpha(Math.round(255 * f));
            rows.setAlpha(f);
            rows.setTranslationY(slide * (1f - f));
        });
        in.start();
        if (top != null) {
            top.requestFocus();
            panel.post(() -> {
                if (openMenu == panel && top.isAttachedToWindow()) top.requestFocus();
            });
        }
    }

    /**
     * Glides Home's focus shape onto a row of the Inputs menu. From layout positions, not on-screen
     * ones: the rows slide into place as the menu opens, and the shape must land where the row ends
     * up, not where it was when focus arrived. The first row takes focus before the menu has been
     * laid out, so then it waits for the layout.
     */
    private void glideToMenuRow(View panel, View rows, View row) {
        SoundEffects.playNav();
        if (!panel.isLaidOut() || row.getHeight() == 0) {
            panel.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int l, int t, int r, int b, int ol, int ot, int or, int ob) {
                    panel.removeOnLayoutChangeListener(this);
                    // Still inside the layout pass here, where the panel does not count as laid
                    // out yet: glide once the pass is over.
                    panel.post(() -> {
                        if (row.hasFocus()) glideToMenuRow(panel, rows, row);
                    });
                }
            });
            return;
        }
        float l = panel.getLeft() + rows.getLeft() + row.getLeft() - glider.getLeft();
        float t = panel.getTop() + rows.getTop() + row.getTop() - glider.getTop();
        gliderOnPill = true;
        glider.setStyle(Glider.DARK);
        glider.moveTo(l, t, l + row.getWidth(), t + row.getHeight(), 13 * density);
    }

    /** Closes the open menu; with {@code toAnchor}, focus and the selection go back to what it opened from. */
    private void closeMenu(boolean toAnchor) {
        View panel = openMenu;
        if (panel == null) return;
        SoundEffects.playNav();
        View returnAnchor = menuAnchor;
        Runnable returnAction = toAnchor ? menuReturnAction : null;
        openMenu = null;
        menuAnchor = null;
        menuReturnAction = null;
        if (toAnchor && returnAnchor != null) returnAnchor.requestFocus();
        panel.animate().alpha(0f).setDuration(reducedMotion ? 80 : 150).withEndAction(() -> {
            glider.removeCompanion(panel);
            root.removeView(panel);
            if (returnAction != null && !isFinishing() && !isDestroyed()) returnAction.run();
        }).start();
    }

    private boolean inMenu(View v) {
        for (View p = v; p != null && openMenu != null; p = p.getParent() instanceof View ? (View) p.getParent() : null) {
            if (p == openMenu) return true;
        }
        return false;
    }

    private PopupWindow glassPopup(View panel, int width) {
        // The same glass as the pills and the tray: what is behind it, softened, with a shining edge.
        panel.setBackground(new GlassDrawable(panel, 20 * density, density, null, 0.5f));
        Backdrop.watch(panel);
        final PopupWindow popup = new PopupWindow(panel, width, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));   // needed for Back to dismiss
        popup.setOutsideTouchable(true);
        popup.setAnimationStyle(0);
        popup.setElevation(0f);
        popup.setOnDismissListener(() -> {
            if (openPopup == popup) openPopup = null;
        });
        openPopup = popup;
        return popup;
    }

    /** Drop in: fade and unfold from the top edge, hanging under the anchor pill. */
    private void dropIn(PopupWindow popup, View panel, View anchor, int gravity) {
        panel.setAlpha(0f);
        panel.setPivotY(0f);
        panel.setScaleY(0.92f);
        panel.setTranslationY(-8 * density);
        popup.showAsDropDown(anchor, 0, (int) (10 * density), gravity);
        panel.animate().alpha(1f).scaleY(1f).translationY(0f).setDuration(220)
                .setInterpolator(new android.view.animation.DecelerateInterpolator(1.6f)).start();
    }

    private void addSeparator(LinearLayout rows) {
        View line = new View(this);
        line.setBackgroundColor(0x1FFFFFFF);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) density);
        int m = (int) (6 * density), h = (int) (12 * density);
        lp.setMargins(h, m, h, m);
        rows.addView(line, lp);
    }

    private void addInputRow(LinearLayout rows, Runnable close, int iconRes, String title, int state, Runnable action) {
        View row = getLayoutInflater().inflate(R.layout.item_input, rows, false);
        ((ImageView) row.findViewById(R.id.icon)).setImageResource(iconRes);
        ((TextView) row.findViewById(R.id.title)).setText(title);
        TextView status = row.findViewById(R.id.status);
        View dot = row.findViewById(R.id.dot);
        // This TV reports "standby" for every port whether or not something is plugged in, so only
        // definite states are shown.
        if (state == TvInputManager.INPUT_STATE_CONNECTED) {
            status.setText(R.string.connected);
            dot.setVisibility(View.VISIBLE);
        } else if (state == TvInputManager.INPUT_STATE_DISCONNECTED) {
            status.setText(R.string.not_connected);
            dot.setVisibility(View.GONE);
        } else {
            status.setVisibility(View.GONE);
            dot.setVisibility(View.GONE);
        }
        row.setOnClickListener(v -> {
            close.run();
            action.run();
        });
        rows.addView(row);
    }

    // ---- module cards ----------------------------------------------------------------------------

    /**
     * Asks about the next card that has no decision for its current signing key. One prompt at a
     * time, only over a laid out grid with window focus (not under a popup or another app), and
     * the next one follows as soon as this one is answered. Nothing is shown until the owner says Show.
     */
    private void showCardPrompt() {
        if (!moduleCards || !tilesReady || promptsPaused || cardPrompt != null || !hasWindowFocus() || isFinishing()) return;
        if (cards.isEmpty() || !CardFeature.working()) return;
        CardFeature.run(() -> {
            showCardPromptNow();
            return null;
        }, null);
    }

    private void showCardPromptNow() {
        final Cards.Card card = Cards.firstPending(cards);
        if (card == null) return;

        View panel = getLayoutInflater().inflate(R.layout.dialog_card_prompt, root, false);
        ((TextView) panel.findViewById(R.id.title)).setText(getString(R.string.card_prompt_title, card.appLabel));
        ((TextView) panel.findViewById(R.id.body)).setText(card.state == Cards.State.RESIGNED
                ? getString(R.string.card_prompt_resigned, card.appLabel)
                : card.state == Cards.State.RELABELLED
                ? getString(R.string.card_prompt_relabelled, card.appLabel)
                : getString(R.string.card_prompt_body, card.appLabel));
        TextView names = panel.findViewById(R.id.names);
        if (Cards.fold(card.label).equals(Cards.fold(card.appLabel))) names.setVisibility(View.GONE);
        else names.setText(getString(R.string.card_prompt_names, card.label, card.appLabel));
        ((TextView) panel.findViewById(R.id.identity)).setText(
                getString(R.string.card_prompt_identity, card.pkg, Cards.shortDigest(card.digest)));
        TextView keep = panel.findViewById(R.id.btnKeep);
        TextView show = panel.findViewById(R.id.btnShow);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(panel);
        dialog.setCanceledOnTouchOutside(false);

        // One decision per prompt: the buttons and Back (cancel) all end here.
        final boolean[] decided = {false};
        View.OnClickListener choose = v -> {
            if (decided[0]) return;
            decided[0] = true;
            cardPrompt = null;
            dialog.dismiss();
            decideCard(card, v.getId() == R.id.btnShow);   // guards its own card calls; the tile reload stays outside
            restoreFocus();   // the tiles changed under the prompt; this also chains the next prompt
        };
        keep.setOnClickListener(choose);
        show.setOnClickListener(choose);
        dialog.setOnCancelListener(d -> choose.onClick(keep));
        dialog.setOnDismissListener(d -> {
            if (cardPrompt == dialog) cardPrompt = null;
        });
        // Left and Right move between the two buttons; Up and Down stay put; the ends don't wrap.
        View.OnKeyListener keys = (v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN) return false;
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) return true;
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) return v.getId() == R.id.btnKeep;
            return keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && v.getId() == R.id.btnShow;
        };
        keep.setOnKeyListener(keys);
        show.setOnKeyListener(keys);
        cardPrompt = dialog;
        dialog.show();
        TvDialogStyle.applyCustom(dialog, this, 440, 0, 0.6f);
        keep.requestFocus();
    }

    /** Writes the owner's decision and rebuilds the tiles; focus is left where it is. */
    private void decideCard(Cards.Card card, boolean show) {
        boolean saved = CardFeature.run(() -> Cards.decide(Cards.storeFile(this), card, show), false);
        if (!saved) {
            TvFeedback.show(this, R.string.card_not_saved, Toast.LENGTH_SHORT);
            promptsPaused = true;
        }
        reloadTiles();
    }

    /** Dropdown under the Settings pill (hold OK): every discovered card, its package and key; OK toggles Shown and Hidden. */
    private void showModuleCards() {
        if (cards.isEmpty() || !CardFeature.working()) {
            TvFeedback.show(this, R.string.no_module_cards, Toast.LENGTH_SHORT);
            return;
        }
        CardFeature.run(() -> {
            showModuleCardsNow();
            return null;
        }, null);
    }

    private void showModuleCardsNow() {
        View anchor = findViewById(R.id.btnSettings);
        View panel = getLayoutInflater().inflate(R.layout.popup_cards, root, false);
        LinearLayout rows = panel.findViewById(R.id.rows);
        final PopupWindow popup = glassPopup(panel, (int) (400 * density));
        List<Cards.Card> listed = new ArrayList<>(cards);
        Collections.sort(listed, Cards::byLabel);
        for (Cards.Card card : listed) {
            View row = getLayoutInflater().inflate(R.layout.item_card_row, rows, false);
            row.setTag(card.pkg);
            ((TextView) row.findViewById(R.id.title)).setText(card.label);
            TextView app = row.findViewById(R.id.app);
            if (Cards.fold(card.label).equals(Cards.fold(card.appLabel))) app.setVisibility(View.GONE);
            else app.setText(getString(R.string.card_app_name, card.appLabel));
            ((TextView) row.findViewById(R.id.pkg)).setText(card.pkg);
            ((TextView) row.findViewById(R.id.digest)).setText(Cards.longDigest(card.digest));
            final String pkg = card.pkg;
            row.setOnClickListener(v -> CardFeature.run(() -> {
                // The list stays open; the decision is for the card as it is now, not as it was listed.
                Cards.Card now = cardFor(pkg);
                if (now == null) return null;
                decideCard(now, now.state != Cards.State.SHOWN);
                // Every row, not only this one: a toggle can move another card across the limit of 8.
                updateCardStates(rows);
                return null;
            }, null));
            rows.addView(row);
        }
        updateCardStates(rows);
        // Scroll inside the panel instead of running off the bottom of a 720p screen.
        panel.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int[] at = new int[2];
        anchor.getLocationInWindow(at);
        int room = root.getHeight() - (at[1] + anchor.getHeight()) - (int) (34 * density);
        popup.setHeight(Math.min(panel.getMeasuredHeight(), Math.max(room, (int) (120 * density))));
        dropIn(popup, panel, anchor, Gravity.END);
        rows.getChildAt(0).requestFocus();
    }

    private Cards.Card cardFor(String pkg) {
        for (Cards.Card c : cards) if (c.pkg.equals(pkg)) return c;
        return null;
    }

    /** Writes the state of every row in the Module cards list from the current card list. */
    private void updateCardStates(LinearLayout rows) {
        List<Cards.Card> drawn = Cards.shown(cards);
        for (int i = 0; i < rows.getChildCount(); i++) {
            View row = rows.getChildAt(i);
            Cards.Card card = row.getTag() instanceof String ? cardFor((String) row.getTag()) : null;
            ((TextView) row.findViewById(R.id.state)).setText(card != null ? stateText(card, drawn.contains(card)) : "");
        }
    }

    /** The row's state: a card the owner set to Shown but which is beyond the eight drawn says so. */
    private String stateText(Cards.Card card, boolean drawn) {
        switch (card.state) {
            case SHOWN:
                return getString(drawn ? R.string.card_state_shown : R.string.card_state_over_limit);
            case HIDDEN:
                return getString(R.string.card_state_hidden);
            case RESIGNED:
                return getString(R.string.card_state_new_key);
            case RELABELLED:
                return getString(R.string.card_state_new_label);
            default:
                return getString(R.string.card_state_new);
        }
    }
}
