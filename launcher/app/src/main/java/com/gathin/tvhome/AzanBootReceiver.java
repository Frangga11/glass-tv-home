package com.gathin.tvhome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/** Rebuilds the next prayer alarm after Android clears scheduled alarms during a TV reboot. */
public final class AzanBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) return;

        Location.Zone zone = Location.chosen(context);
        String zoneCode = zone != null ? zone.code : null;
        AzanBootRecoveryPolicy.restore(intent.getAction(), zoneCode,
                selectedZone -> Azan.schedule(context, selectedZone));
    }
}
