package com.gathin.tvhome;

/**
 * Strict parser for the single HTTP request line accepted by the local QR upload servers.
 * Only origin-form targets and HTTP/1.0 or HTTP/1.1 are supported.
 */
final class UploadHttpRequest {
    final String method;
    final String path;
    final String query;
    final String version;

    private UploadHttpRequest(String method, String path, String query, String version) {
        this.method = method;
        this.path = path;
        this.query = query;
        this.version = version;
    }

    static UploadHttpRequest parse(String line) {
        if (line == null || line.isEmpty()) return null;
        int first = line.indexOf(' ');
        if (first <= 0) return null;
        int second = line.indexOf(' ', first + 1);
        if (second <= first + 1 || second == line.length() - 1) return null;
        if (line.indexOf(' ', second + 1) >= 0) return null;

        String method = line.substring(0, first);
        String target = line.substring(first + 1, second);
        String version = line.substring(second + 1);
        if (!isMethodToken(method) || !("HTTP/1.0".equals(version) || "HTTP/1.1".equals(version))) {
            return null;
        }
        if (!target.startsWith("/") || target.indexOf('#') >= 0) return null;
        for (int i = 0; i < target.length(); i++) {
            char ch = target.charAt(i);
            if (ch <= 0x20 || ch == 0x7f) return null;
        }

        int queryIndex = target.indexOf('?');
        String path = queryIndex >= 0 ? target.substring(0, queryIndex) : target;
        String query = queryIndex >= 0 ? target.substring(queryIndex + 1) : "";
        if (path.isEmpty()) return null;
        return new UploadHttpRequest(method, path, query, version);
    }

    static boolean hasValidHeaders(String[] lines) {
        if (lines == null || lines.length < 1) return false;
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if (line == null || line.isEmpty()) continue;
            if (line.charAt(0) == ' ' || line.charAt(0) == '\t') return false;
            int colon = line.indexOf(':');
            if (colon <= 0) return false;
            for (int j = 0; j < colon; j++) {
                char ch = line.charAt(j);
                if (!isHeaderNameCharacter(ch)) return false;
            }
            for (int j = colon + 1; j < line.length(); j++) {
                char ch = line.charAt(j);
                if ((ch < 0x20 && ch != '\t') || ch == 0x7f) return false;
            }
        }
        return true;
    }

    private static boolean isHeaderNameCharacter(char ch) {
        return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')
                || (ch >= '0' && ch <= '9') || "!#$%&'*+-.^_|~".indexOf(ch) >= 0 || ch == 96;
    }

    private static boolean isMethodToken(String method) {
        if (method.isEmpty()) return false;
        for (int i = 0; i < method.length(); i++) {
            char ch = method.charAt(i);
            if (ch < 'A' || ch > 'Z') return false;
        }
        return true;
    }

    boolean isPath(String expectedPath) {
        return expectedPath != null && expectedPath.equals(path);
    }

    boolean isWithinPath(String sessionPath) {
        return sessionPath != null
                && (path.equals(sessionPath) || path.startsWith(sessionPath + "/"));
    }
}
