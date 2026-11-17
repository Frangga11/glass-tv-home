package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UploadDeadlinePolicyTest {
    @Test
    public void readTimeoutIsBoundedByIdleAndAbsoluteDeadlines() {
        assertEquals(30000, NetworkFeedbackPolicy.boundedReadTimeoutMs(120000, 30000, 90000));
        assertEquals(5000, NetworkFeedbackPolicy.boundedReadTimeoutMs(95000, 30000, 90000));
        assertEquals(1, NetworkFeedbackPolicy.boundedReadTimeoutMs(90001, 30000, 90000));
        assertEquals(0, NetworkFeedbackPolicy.boundedReadTimeoutMs(90000, 30000, 90000));
        assertEquals(0, NetworkFeedbackPolicy.boundedReadTimeoutMs(80000, 30000, 90000));
        assertEquals(0, NetworkFeedbackPolicy.boundedReadTimeoutMs(120000, 0, 90000));
    }

    @Test
    public void sessionRequiresRunningServerAndUnexpiredDeadline() {
        assertTrue(NetworkFeedbackPolicy.isSessionActive(true, 1001, 1000));
        assertFalse(NetworkFeedbackPolicy.isSessionActive(false, 1001, 1000));
        assertFalse(NetworkFeedbackPolicy.isSessionActive(true, 1000, 1000));
        assertFalse(NetworkFeedbackPolicy.isSessionActive(true, 0, 0));
    }
}
