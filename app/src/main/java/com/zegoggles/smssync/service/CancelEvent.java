package com.zegoggles.smssync.service;

import static com.zegoggles.smssync.service.CancelEvent.Origin.SYSTEM;

import androidx.annotation.NonNull;

public class CancelEvent {
    enum Origin { USER, SYSTEM }
    private final Origin origin;

    public CancelEvent() {
        this(Origin.USER);
    }

    CancelEvent(Origin origin) {
        this.origin = origin;
    }

    public boolean mayInterruptIfRunning() {
        return origin == SYSTEM;
    }

    @NonNull
    @Override
    public String toString() {
        return "CancelEvent{" +
                "origin=" + origin +
                '}';
    }
}
