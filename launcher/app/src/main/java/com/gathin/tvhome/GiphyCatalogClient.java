package com.gathin.tvhome;

import android.content.Context;
import android.net.Uri;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/** Thin, bounded adapter for the upstream GIPHY catalog API. Call only from a worker thread. */
final class GiphyCatalogClient {
    static final int LIMIT = 14, MAX_JSON_BYTES = 512 * 1024, MAX_GIF_BYTES = 8 * 1024 * 1024;
    private static final int CONNECT_MS = 6000, READ_MS = 8000;
    static final class GifAsset {
        final String id, title, previewUrl, downloadUrl;
        GifAsset(String id, String title, String previewUrl, String downloadUrl) {
            this.id=id; this.title=title; this.previewUrl=previewUrl; this.downloadUrl=downloadUrl;
        }
    }
    static final class CatalogException extends IOException {
        final int status;
        CatalogException(String message, int status) { super(message); this.status=status; }
    }
    private GiphyCatalogClient() {}
    static boolean isConfigured() { return BuildConfig.GIPHY_API_KEY != null && !BuildConfig.GIPHY_API_KEY.trim().isEmpty(); }

    static List<GifAsset> search(String query) throws Exception {
        String key=BuildConfig.GIPHY_API_KEY;
        if (key==null || key.trim().isEmpty()) throw new CatalogException("GIPHY_NOT_CONFIGURED",0);
        String q=query==null?"":query.trim();
        if(q.isEmpty()) q="cute avatar";
        if(q.length()>50) q=q.substring(0,50);
        Uri uri=Uri.parse("https://api.giphy.com/v1/gifs/search").buildUpon()
            .appendQueryParameter("api_key",key).appendQueryParameter("q",q)
            .appendQueryParameter("limit",String.valueOf(LIMIT)).appendQueryParameter("rating","g")
            .appendQueryParameter("lang","en").build();
        HttpURLConnection c=open(uri.toString(),"application/json");
        try {
            int status=c.getResponseCode();
            if(status<200 || status>=300) throw new CatalogException("GIPHY_HTTP_"+status,status);
            JSONObject root=new JSONObject(new String(readBounded(c.getInputStream(),MAX_JSON_BYTES),StandardCharsets.UTF_8));
            JSONArray data=root.optJSONArray("data");
            if(data==null) return Collections.emptyList();
            List<GifAsset> out=new ArrayList<>();
            for(int i=0;i<data.length() && out.size()<LIMIT;i++) {
                JSONObject item=data.optJSONObject(i), images=item==null?null:item.optJSONObject("images");
                JSONObject preview=images==null?null:images.optJSONObject("fixed_width_small");
                JSONObject original=images==null?null:images.optJSONObject("original");
                String p=preview==null?null:preview.optString("url",null);
                String d=original==null?null:original.optString("url",null);
                String id=item==null?"":item.optString("id","");
                if(!id.isEmpty() && isGiphyUrl(p) && isGiphyUrl(d))
                    out.add(new GifAsset(id,item.optString("title",""),p,d));
            }
            return out;
        } finally { c.disconnect(); }
    }

    /** Stage only; caller commits with ProfileManager.replaceCustomAvatar(profileId, file). */
    static File downloadToPrivateStaging(Context context,GifAsset asset,String profileId) throws Exception {
        if(context==null || asset==null || !isGiphyUrl(asset.downloadUrl)
            || profileId==null || !profileId.matches("[A-Za-z0-9_-]{1,64}"))
            throw new IllegalArgumentException("Invalid GIF selection");
        File dir=new File(context.getFilesDir(),"profile_avatars").getCanonicalFile();
        if(!dir.exists() && !dir.mkdirs()) throw new IOException("Avatar storage unavailable");
        File staged=File.createTempFile("giphy_"+profileId+"_",".gif",dir).getCanonicalFile();
        if(!ProfileManager.isPrivateAvatarFile(dir,staged)) throw new IOException("Invalid avatar path");
        HttpURLConnection c=open(asset.downloadUrl,"image/gif,application/octet-stream;q=0.8");
        boolean ok=false;
        try {
            int status=c.getResponseCode();
            if(status<200 || status>=300) throw new CatalogException("GIPHY_MEDIA_HTTP_"+status,status);
            String type=c.getContentType();
            if(type==null || !(type.toLowerCase(Locale.ROOT).startsWith("image/gif")
                || type.toLowerCase(Locale.ROOT).startsWith("application/octet-stream")))
                throw new IOException("Unsupported GIF media type");
            try(InputStream in=c.getInputStream(); FileOutputStream out=new FileOutputStream(staged)) {
                byte[] b=new byte[8192]; int n,total=0;
                while((n=in.read(b))!=-1) { total+=n; if(total>MAX_GIF_BYTES) throw new IOException("GIF exceeds 8 MiB"); out.write(b,0,n); }
                out.getFD().sync();
            }
            byte[] sig=new byte[6];
            try(InputStream in=new FileInputStream(staged)) { if(in.read(sig)!=6) throw new IOException("Truncated GIF"); }
            String magic=new String(sig,StandardCharsets.US_ASCII);
            if(!"GIF87a".equals(magic) && !"GIF89a".equals(magic)) throw new IOException("Downloaded media is not GIF");
            ok=true; return staged;
        } finally { c.disconnect(); if(!ok && staged.exists()) staged.delete(); }
    }
    private static HttpURLConnection open(String raw,String accept) throws Exception {
        URL u=new URL(raw);
        if(!"https".equalsIgnoreCase(u.getProtocol())) throw new IOException("HTTPS required");
        HttpURLConnection c=(HttpURLConnection)u.openConnection();
        c.setConnectTimeout(CONNECT_MS); c.setReadTimeout(READ_MS); c.setRequestMethod("GET");
        c.setRequestProperty("Accept",accept); c.setRequestProperty("User-Agent","GlassTVHome/4");
        c.setInstanceFollowRedirects(false); return c;
    }
    private static boolean isGiphyUrl(String raw) {
        if(raw==null || raw.length()>2048) return false;
        try { URL u=new URL(raw); String h=u.getHost().toLowerCase(Locale.ROOT);
            return "https".equalsIgnoreCase(u.getProtocol()) && (h.equals("giphy.com") || h.endsWith(".giphy.com"));
        } catch(Exception e) { return false; }
    }
    private static byte[] readBounded(InputStream input,int max) throws Exception {
        try(InputStream in=input; ByteArrayOutputStream out=new ByteArrayOutputStream()) {
            byte[] b=new byte[8192]; int n,total=0;
            while((n=in.read(b))!=-1) { total+=n; if(total>max) throw new IOException("GIPHY response too large"); out.write(b,0,n); }
            return out.toByteArray();
        }
    }
}
