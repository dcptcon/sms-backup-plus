/* Copyright (c) 2009 Christoph Studer <chstuder@gmail.com>
 * Copyright (c) 2010 Jan Berkel <jan.berkel@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zegoggles.smssync.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.util.Log;
import com.zegoggles.smssync.preferences.AuthPreferences;
import com.zegoggles.smssync.preferences.Preferences;
import com.zegoggles.smssync.service.BackupJobs;
import com.zegoggles.smssync.utils.AppLog;

import static com.zegoggles.smssync.App.LOCAL_LOGV;
import static com.zegoggles.smssync.App.TAG;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    //public static final String SMS_RECEIVED_1 = "android.provider.Telephony.SMS_RECEIVED";
    public static final String SMS_RECEIVED = Telephony.Sms.Intents.SMS_RECEIVED_ACTION;
    public static final String MMS_RECEIVED = Telephony.Sms.Intents.WAP_PUSH_RECEIVED_ACTION;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (LOCAL_LOGV) Log.e("[LOG] SmsBroadcastReceiver.onReceive", "onReceive(" + context + "," + intent + ")");

        String action = intent.getAction();
        if (SMS_RECEIVED.equals(action) || MMS_RECEIVED.equals(action)) {
            incomingSMS(context);
        } else {
            Log.w(TAG, "unhandled intent: "+intent);
        }
    }

    private void incomingSMS(Context context) {
        if (shouldSchedule(context)) {
            getBackupJobs(context).scheduleIncoming();
        } else {
            Log.e("[LOG] SmsBroadcastReceiver.incomingSMS", "Received SMS but not set up to back up.");
        }
    }

    private boolean shouldSchedule(Context context) {
        final Preferences preferences = getPreferences(context);

        final boolean autoBackupEnabled = preferences.isAutoBackupEnabled();
        final boolean loginInformationSet = getAuthPreferences(context).isLoginInformationSet();
        final boolean firstBackup = preferences.isFirstBackup();

        final boolean schedule = (autoBackupEnabled && loginInformationSet && !firstBackup);

        if (!schedule) {
            final String message = "Not set up to back up. " +
                    "autoBackup=" + autoBackupEnabled +
                    ", loginInfoSet=" + loginInformationSet +
                    ", firstBackup=" + firstBackup;

            log(context, message, preferences.isAppLogDebug());
        }
        return schedule;
    }

    private void log(Context context, String message, boolean appLog) {
        Log.e("[LOG] SmsBroadcastReceiver.log", message);
        if (appLog) {
            new AppLog(context).appendAndClose(message);
        }
    }

    protected BackupJobs getBackupJobs(Context context) {
        return new BackupJobs(context);
    }

    protected Preferences getPreferences(Context context) {
        return new Preferences(context);
    }

    protected AuthPreferences getAuthPreferences(Context context) {
        return new AuthPreferences(context);
    }
}
