package com.zegoggles.smssync.compat;

import static com.zegoggles.smssync.App.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MmsReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive("+intent+")");
    }
}
