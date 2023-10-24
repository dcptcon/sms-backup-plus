package com.zegoggles.smssync.compat;

import static com.zegoggles.smssync.App.TAG;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MmsReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive("+intent+")");
    }
}
