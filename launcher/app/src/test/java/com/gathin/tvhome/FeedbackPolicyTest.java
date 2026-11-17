package com.gathin.tvhome;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Pure policy checks used by the shared TV feedback delivery path. */
public class FeedbackPolicyTest {
    @Test
    public void preservesSupportedToastDurationsAndNormalizesUnknownValues() {
        assertEquals(0, FeedbackPolicy.normalizeToastDuration(0));
        assertEquals(1, FeedbackPolicy.normalizeToastDuration(1));
        assertEquals(0, FeedbackPolicy.normalizeToastDuration(-1));
        assertEquals(0, FeedbackPolicy.normalizeToastDuration(99));
    }

    @Test
    public void rejectsEmptyFeedbackMessages() {
        assertFalse(FeedbackPolicy.hasMessage(null));
        assertFalse(FeedbackPolicy.hasMessage(""));
        assertFalse(FeedbackPolicy.hasMessage("   "));
        assertTrue(FeedbackPolicy.hasMessage("Saved"));
    }

    @Test
    public void rejectsFeedbackForFinishedOrDestroyedActivities() {
        assertTrue(FeedbackPolicy.isActivityUsable(false, false));
        assertFalse(FeedbackPolicy.isActivityUsable(true, false));
        assertFalse(FeedbackPolicy.isActivityUsable(false, true));
        assertFalse(FeedbackPolicy.isActivityUsable(true, true));
    }
}
