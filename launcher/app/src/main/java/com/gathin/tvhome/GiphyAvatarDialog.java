package com.gathin.tvhome;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/** Glass TV presentation shell around the upstream GIPHY catalog/data contract. */
final class GiphyAvatarDialog {
    private GiphyAvatarDialog() {}

    static void show(MainActivity activity,String profileId,Runnable onUpdated) {
        if(activity==null || profileId==null || profileId.trim().isEmpty()
                || activity.isFinishing() || activity.isDestroyed()) return;
        float density=activity.getResources().getDisplayMetrics().density;
        LinearLayout root=new LinearLayout(activity);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(Math.round(20*density),Math.round(16*density),Math.round(20*density),Math.round(12*density));

        TextView intro=new TextView(activity);
        intro.setText(R.string.giphy_avatar_intro);
        intro.setTextColor(0xFFD5DCE7); intro.setTextSize(13); intro.setLineSpacing(0,1.15f);
        root.addView(intro);

        LinearLayout searchRow=new LinearLayout(activity);
        searchRow.setOrientation(LinearLayout.HORIZONTAL); searchRow.setGravity(Gravity.CENTER_VERTICAL);
        EditText query=new EditText(activity);
        query.setSingleLine(true); query.setInputType(InputType.TYPE_CLASS_TEXT);
        query.setHint(R.string.giphy_search_hint); query.setTextColor(0xFFFFFFFF);
        query.setHintTextColor(0xFF9AA4B2); query.setTextSize(15);
        query.setPadding(Math.round(12*density),0,Math.round(12*density),0);
        query.setBackground(makeBackground(0x222F3A4A,0x556B7C91,10*density));
        searchRow.addView(query,new LinearLayout.LayoutParams(0,Math.round(46*density),1f));
        Button search=new Button(activity); search.setText(R.string.giphy_search_action);
        search.setAllCaps(false); search.setTextSize(13); search.setFocusable(true);
        LinearLayout.LayoutParams searchLp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,Math.round(46*density));
        searchLp.setMarginStart(Math.round(8*density)); searchRow.addView(search,searchLp);
        root.addView(searchRow);

        TextView status=new TextView(activity);
        status.setText(GiphyCatalogClient.isConfigured()?R.string.giphy_search_prompt:R.string.giphy_key_missing);
        status.setTextColor(0xFFB6C2D2); status.setTextSize(12);
        status.setPadding(0,Math.round(8*density),0,Math.round(6*density)); root.addView(status);
        ProgressBar progress=new ProgressBar(activity); progress.setIndeterminate(true); progress.setVisibility(View.GONE);
        root.addView(progress,new LinearLayout.LayoutParams(Math.round(30*density),Math.round(30*density)));

        RecyclerView results=new RecyclerView(activity);
        results.setLayoutManager(new GridLayoutManager(activity,4));
        results.setFocusable(true); results.setClipToPadding(false);
        final Runnable[] closeDialog={()->{}};
        GifAdapter adapter=new GifAdapter(activity,profileId,onUpdated,status,()->closeDialog[0].run());
        results.setAdapter(adapter);
        root.addView(results,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1f));

        TextView attribution=new TextView(activity);
        attribution.setText(R.string.giphy_powered_by);
        attribution.setTextColor(0xFFDCE4EE); attribution.setTextSize(11);
        attribution.setGravity(Gravity.END); attribution.setPadding(0,Math.round(8*density),0,0);
        root.addView(attribution);

        AlertDialog dialog=new AlertDialog.Builder(activity,R.style.Theme_TvHome_GlassDialog)
                .setTitle(R.string.giphy_avatar_title).setView(root)
                .setNegativeButton(R.string.cancel_action,(d,w)->d.dismiss()).create();
        closeDialog[0]=dialog::dismiss;
        ExecutorService worker=Executors.newSingleThreadExecutor();
        AtomicInteger generation=new AtomicInteger();
        dialog.setOnDismissListener(d->{generation.incrementAndGet();worker.shutdownNow();adapter.close();});
        search.setOnClickListener(v->{
            if(!GiphyCatalogClient.isConfigured()){status.setText(R.string.giphy_key_missing);return;}
            int request=generation.incrementAndGet();
            progress.setVisibility(View.VISIBLE); status.setText(R.string.giphy_search_loading);
            search.setEnabled(false); adapter.clear();
            String term=query.getText().toString();
            worker.execute(()->{
                try {
                    List<GiphyCatalogClient.GifAsset> found=GiphyCatalogClient.search(term);
                    activity.runOnUiThread(()->{
                        if(!dialog.isShowing()||generation.get()!=request)return;
                        progress.setVisibility(View.GONE); search.setEnabled(true);
                        adapter.submit(found);
                        status.setText(found.isEmpty()?activity.getString(R.string.giphy_search_empty):activity.getString(R.string.giphy_search_count,found.size()));
                        if(!found.isEmpty()) results.requestFocus();
                    });
                } catch(Exception error) {
                    activity.runOnUiThread(()->{
                        if(!dialog.isShowing()||generation.get()!=request)return;
                        progress.setVisibility(View.GONE); search.setEnabled(true);
                        status.setText(error.getMessage()!=null && error.getMessage().contains("429")
                                ?R.string.giphy_rate_limited:R.string.giphy_search_failed);
                    });
                }
            });
        });
        dialog.setOnShowListener(d->{
            TvDialogStyle.apply(dialog,activity,980,0);
            query.requestFocus();
            query.setOnEditorActionListener((v,id,event)->{search.performClick();return true;});
        });
        dialog.show();
        Window window=dialog.getWindow();
        if(window!=null) window.setLayout(Math.min((int)(980*density),(int)(activity.getResources().getDisplayMetrics().widthPixels*.88f)),
                Math.min((int)(680*density),(int)(activity.getResources().getDisplayMetrics().heightPixels*.82f)));
    }

    private static GradientDrawable makeBackground(int fill,int stroke,float radius){
        GradientDrawable d=new GradientDrawable(); d.setColor(fill); d.setCornerRadius(radius); d.setStroke(1,stroke); return d;
    }

    private static Bitmap loadPreview(String raw) {
        HttpURLConnection c=null;
        try {
            URL url=new URL(raw);
            String host=url.getHost().toLowerCase(java.util.Locale.ROOT);
            if(!"https".equalsIgnoreCase(url.getProtocol()) || !(host.equals("giphy.com") || host.endsWith(".giphy.com"))) return null;
            c=(HttpURLConnection)url.openConnection();
            c.setConnectTimeout(3500); c.setReadTimeout(4500); c.setInstanceFollowRedirects(false);
            c.setRequestProperty("Accept","image/*");
            if(c.getResponseCode()!=200) return null;
            String type=c.getContentType();
            if(type==null || !type.toLowerCase(java.util.Locale.ROOT).startsWith("image/")) return null;
            try(InputStream in=c.getInputStream(); ByteArrayOutputStream out=new ByteArrayOutputStream()) {
                byte[] buffer=new byte[4096]; int n,total=0;
                while((n=in.read(buffer))!=-1) { total+=n; if(total>1024*1024) return null; out.write(buffer,0,n); }
                byte[] bytes=out.toByteArray();
                BitmapFactory.Options bounds=new BitmapFactory.Options(); bounds.inJustDecodeBounds=true;
                BitmapFactory.decodeByteArray(bytes,0,bytes.length,bounds);
                if(bounds.outWidth<=0 || bounds.outHeight<=0 || bounds.outWidth>2048 || bounds.outHeight>2048) return null;
                BitmapFactory.Options decode=new BitmapFactory.Options(); decode.inSampleSize=Math.max(1,Math.max(bounds.outWidth/320,bounds.outHeight/240));
                return BitmapFactory.decodeByteArray(bytes,0,bytes.length,decode);
            }
        } catch(Exception ignored) { return null; }
        finally { if(c!=null)c.disconnect(); }
    }

    private static final class GifAdapter extends RecyclerView.Adapter<GifHolder> {
        private final MainActivity activity; private final String profileId; private final Runnable onUpdated;
        private final TextView status; private final Runnable closeDialog;
        private final List<GiphyCatalogClient.GifAsset> items=new ArrayList<>();
        private final ExecutorService previews=Executors.newFixedThreadPool(3);
        private final ExecutorService chooser=Executors.newSingleThreadExecutor();
        private final AtomicInteger generation=new AtomicInteger();
        private volatile boolean closed;
        GifAdapter(MainActivity a,String p,Runnable u,TextView s,Runnable close){activity=a;profileId=p;onUpdated=u;status=s;closeDialog=close;}
        void submit(List<GiphyCatalogClient.GifAsset> list){items.clear();items.addAll(list);notifyDataSetChanged();}
        void clear(){generation.incrementAndGet();items.clear();notifyDataSetChanged();}
        void close(){closed=true;generation.incrementAndGet();previews.shutdownNow();chooser.shutdownNow();}
        @Override public GifHolder onCreateViewHolder(ViewGroup parent,int type){
            float d=activity.getResources().getDisplayMetrics().density;
            FrameLayout tile=new FrameLayout(activity);
            tile.setFocusable(true); tile.setClickable(true); tile.setBackground(makeBackground(0x222F3A4A,0x336B7C91,12*d));
            ImageView image=new ImageView(activity); image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            tile.addView(image,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,Math.round(104*d)));
            TextView label=new TextView(activity); label.setGravity(Gravity.CENTER); label.setTextColor(0xFFFFFFFF);
            label.setTextSize(10); label.setTypeface(Typeface.DEFAULT,Typeface.BOLD); label.setMaxLines(2);
            label.setPadding(Math.round(4*d),Math.round(3*d),Math.round(4*d),Math.round(3*d));
            label.setBackgroundColor(0x990B1018);
            FrameLayout.LayoutParams labelLp=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,Gravity.BOTTOM);
            tile.addView(label,labelLp);
            RecyclerView.LayoutParams lp=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,Math.round(108*d));
            lp.setMargins(Math.round(4*d),Math.round(4*d),Math.round(4*d),Math.round(4*d)); tile.setLayoutParams(lp);
            return new GifHolder(tile,image,label);
        }
        @Override public void onBindViewHolder(GifHolder holder,int position){
            GiphyCatalogClient.GifAsset asset=items.get(position);
            int request=generation.get(); holder.cancelPreview();
            holder.image.setImageDrawable(null);
            holder.label.setText(asset.title==null||asset.title.trim().isEmpty()?activity.getString(R.string.giphy_select_gif):asset.title);
            holder.tile.setContentDescription(holder.label.getText());
            holder.tile.setEnabled(true);
            holder.tile.setOnClickListener(v->{
                if(closed || !holder.tile.isEnabled())return;
                holder.tile.setEnabled(false); status.setText(R.string.giphy_avatar_saving);
                chooser.execute(()->{
                    File staged=null; boolean saved=false;
                    try {
                        if(closed || Thread.currentThread().isInterrupted())return;
                        staged=GiphyCatalogClient.downloadToPrivateStaging(activity,asset,profileId);
                        if(!closed && !Thread.currentThread().isInterrupted()
                                && profileId.equals(ProfileManager.getActiveProfile(activity).id))
                            saved=ProfileManager.replaceCustomAvatar(activity,profileId,staged);
                    } catch(Exception ignored) {}
                    final boolean result=saved; final File temp=staged;
                    activity.runOnUiThread(()->{
                        if(temp!=null && temp.exists() && !result) temp.delete();
                        if(closed || activity.isFinishing() || activity.isDestroyed())return;
                        if(result){activity.updateTopBarUserIcon();if(onUpdated!=null)onUpdated.run();
                            TvFeedback.show(activity,R.string.giphy_avatar_saved,Toast.LENGTH_SHORT);closeDialog.run();}
                        else {holder.tile.setEnabled(true);status.setText(R.string.giphy_avatar_save_failed);
                            TvFeedback.show(activity,R.string.giphy_avatar_save_failed,Toast.LENGTH_LONG);}
                    });
                });
            });
            holder.preview=previews.submit(()->{
                Bitmap bitmap=loadPreview(asset.previewUrl);
                activity.runOnUiThread(()->{
                    if(closed || holder.getBindingAdapterPosition()==RecyclerView.NO_POSITION
                            || holder.boundId==null || !holder.boundId.equals(asset.id)
                            || request!=generation.get()) { if(bitmap!=null)bitmap.recycle(); return; }
                    if(bitmap!=null)holder.image.setImageBitmap(bitmap);
                });
            });
            holder.boundId=asset.id;
        }
        @Override public void onViewRecycled(GifHolder holder){holder.cancelPreview();holder.image.setImageDrawable(null);holder.boundId=null;super.onViewRecycled(holder);}
        @Override public int getItemCount(){return items.size();}
    }
    private static final class GifHolder extends RecyclerView.ViewHolder {
        final View tile; final ImageView image; final TextView label; Future<?> preview; String boundId;
        GifHolder(View v,ImageView i,TextView t){super(v);tile=v;image=i;label=t;}
        void cancelPreview(){if(preview!=null){preview.cancel(true);preview=null;}}
    }
}
