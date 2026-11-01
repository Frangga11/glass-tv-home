package com.gathin.tvhome;

/**
 * Encodes a validated child-index path into the Settings content tree.
 * Kept Android-free so malformed or stale persisted paths can be tested on the JVM.
 */
final class SettingsFocusPath {
    private static final int MAX_DEPTH = 32;
    private static final int MAX_INDEX = 4096;

    private SettingsFocusPath() { }

    static String encode(int[] indexes) {
        if (indexes == null || indexes.length == 0 || indexes.length > MAX_DEPTH) return null;
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < indexes.length; i++) {
            int index = indexes[i];
            if (index < 0 || index > MAX_INDEX) return null;
            if (i > 0) value.append('.');
            value.append(index);
        }
        return value.toString();
    }

    static int[] decode(String encoded) {
        if (encoded == null || encoded.isEmpty() || encoded.length() > 160) return null;
        String[] parts = encoded.split("\\.", -1);
        if (parts.length == 0 || parts.length > MAX_DEPTH) return null;
        int[] indexes = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.isEmpty() || part.length() > 4) return null;
            try {
                int index = Integer.parseInt(part);
                if (index < 0 || index > MAX_INDEX) return null;
                indexes[i] = index;
            } catch (NumberFormatException invalid) {
                return null;
            }
        }
        return indexes;
    }
}
