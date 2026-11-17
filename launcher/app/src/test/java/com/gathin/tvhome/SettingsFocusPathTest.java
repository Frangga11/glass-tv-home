package com.gathin.tvhome;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SettingsFocusPathTest {

    @Test
    public void roundTripsNestedChildIndexes() {
        int[] path = {0, 2, 1, 14};
        assertEquals("0.2.1.14", SettingsFocusPath.encode(path));
        assertArrayEquals(path, SettingsFocusPath.decode("0.2.1.14"));
    }

    @Test
    public void rejectsEmptyOrExcessivePaths() {
        assertNull(SettingsFocusPath.encode(null));
        assertNull(SettingsFocusPath.encode(new int[0]));
        assertNull(SettingsFocusPath.decode(null));
        assertNull(SettingsFocusPath.decode(""));
        int[] tooDeep = new int[33];
        assertNull(SettingsFocusPath.encode(tooDeep));
        assertNull(SettingsFocusPath.decode("0.1.2.3.4.5.6.7.8.9.10.11.12.13.14.15.16.17.18.19.20.21.22.23.24.25.26.27.28.29.30.31.32"));
    }

    @Test
    public void rejectsMalformedAndOutOfRangeIndexes() {
        assertNull(SettingsFocusPath.decode("1..2"));
        assertNull(SettingsFocusPath.decode("1.-2"));
        assertNull(SettingsFocusPath.decode("1.x"));
        assertNull(SettingsFocusPath.decode("4097"));
        assertNull(SettingsFocusPath.encode(new int[]{-1}));
        assertNull(SettingsFocusPath.encode(new int[]{4097}));
    }

    @Test
    public void acceptsZeroIndex() {
        assertArrayEquals(new int[]{0}, SettingsFocusPath.decode("0"));
    }
}