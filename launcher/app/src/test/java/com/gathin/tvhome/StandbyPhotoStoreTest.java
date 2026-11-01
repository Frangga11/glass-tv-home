package com.gathin.tvhome;

import android.content.SharedPreferences;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StandbyPhotoStoreTest {

    @Test
    public void downsamplesFourKPhotoToTvResolution() {
        assertEquals(2, StandbyPhotoStore.calculateInSampleSize(3840, 2160, 1920, 1080));
    }

    @Test
    public void downsamplesPortraitPhotoWithoutKeepingFullPhoneResolution() {
        assertEquals(2, StandbyPhotoStore.calculateInSampleSize(3000, 4000, 1920, 1080));
    }

    @Test
    public void keepsSmallPhotoAtOriginalResolution() {
        assertEquals(1, StandbyPhotoStore.calculateInSampleSize(1280, 720, 1920, 1080));
    }

    @Test
    public void downsamplesEightKPhotoByFour() {
        assertEquals(4, StandbyPhotoStore.calculateInSampleSize(7680, 4320, 1920, 1080));
    }

    @Test
    public void rotatesOnSharedTwentySecondBoundaries() {
        assertEquals(0, StandbyPhotoStore.getPhotoIndexAtElapsedTime(3, 0));
        assertEquals(1, StandbyPhotoStore.getPhotoIndexAtElapsedTime(3, 20_000));
        assertEquals(2, StandbyPhotoStore.getPhotoIndexAtElapsedTime(3, 40_000));
        assertEquals(0, StandbyPhotoStore.getPhotoIndexAtElapsedTime(3, 60_000));
    }

    @Test
    public void emptyPhotoCollectionHasNoCurrentIndex() {
        assertEquals(-1, StandbyPhotoStore.getPhotoIndexAtElapsedTime(0, 20_000));
        assertEquals(-1, StandbyPhotoStore.getPhotoIndexAtElapsedTime(3, -1));
    }

    @Test
    public void invalidDimensionsFallBackToSafeSampleSize() {
        assertEquals(1, StandbyPhotoStore.calculateInSampleSize(0, 4000, 1920, 1080));
        assertEquals(1, StandbyPhotoStore.calculateInSampleSize(4000, 3000, 0, 1080));
    }

    @Test
    public void uploadedCollectionAndPhotoModeUseOnePreferenceCommit() {
        Map<String, Object> values = new HashMap<>();
        int[] commits = {0};
        SharedPreferences preferences = recordingPreferences(true, values, commits);

        assertTrue(StandbyPhotoStore.commitUploadedState(preferences, "file:///standby_photos/photo.jpg"));

        assertEquals("file:///standby_photos/photo.jpg", values.get("photo_uris"));
        assertEquals(Boolean.TRUE, values.get("display_mode"));
        assertEquals(1, commits[0]);
    }

    @Test
    public void uploadedStateReportsPreferenceCommitFailure() {
        Map<String, Object> values = new HashMap<>();
        int[] commits = {0};
        SharedPreferences preferences = recordingPreferences(false, values, commits);

        assertFalse(StandbyPhotoStore.commitUploadedState(preferences, "file:///standby_photos/photo.jpg"));
        assertEquals("file:///standby_photos/photo.jpg", values.get("photo_uris"));
        assertEquals(Boolean.TRUE, values.get("display_mode"));
        assertEquals(1, commits[0]);
    }

    private static SharedPreferences recordingPreferences(
            boolean commitResult, Map<String, Object> values, int[] commits) {
        final SharedPreferences.Editor[] editorHolder = new SharedPreferences.Editor[1];
        InvocationHandler editorHandler = (proxy, method, args) -> {
            String name = method.getName();
            if ("putString".equals(name) || "putBoolean".equals(name)) {
                values.put((String) args[0], args[1]);
                return editorHolder[0];
            }
            if ("commit".equals(name)) {
                commits[0]++;
                return commitResult;
            }
            if ("apply".equals(name) || "clear".equals(name) || "remove".equals(name)
                    || name.startsWith("put")) return editorHolder[0];
            return null;
        };
        editorHolder[0] = (SharedPreferences.Editor) Proxy.newProxyInstance(
                SharedPreferences.Editor.class.getClassLoader(),
                new Class<?>[]{SharedPreferences.Editor.class}, editorHandler);
        InvocationHandler preferencesHandler = (proxy, method, args) ->
                "edit".equals(method.getName()) ? editorHolder[0] : null;
        return (SharedPreferences) Proxy.newProxyInstance(
                SharedPreferences.class.getClassLoader(),
                new Class<?>[]{SharedPreferences.class}, preferencesHandler);
    }

}
