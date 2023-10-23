package com.zegoggles.smssync.contacts;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Group {
    final String title;
    /** @noinspection unused*/
    final int _id;
    final int count;

    Group(int id, String title, int count) {
        this._id = id;
        this.title = title;
        this.count = count;
    }

    @NonNull
    public String toString() {
        return count > 0 ? String.format(Locale.ENGLISH, "%s (%d)", title, count) : title;
    }
}
