package com.zegoggles.smssync.contacts;

import androidx.annotation.NonNull;

import com.zegoggles.smssync.mail.PersonRecord;

import java.util.HashSet;
import java.util.Set;

public class ContactGroupIds {
    private final Set<Long> ids = new HashSet<>();
    private final Set<Long> rawIds = new HashSet<>();

    public void add(long id, long rawId) {
        this.ids.add(id);
        this.rawIds.add(rawId);
    }

    public boolean contains(PersonRecord personRecord) {
        return ids.contains(personRecord.getContactId());
    }

    /** @noinspection unused*/
    public boolean isEmpty() {
        return ids.isEmpty() && rawIds.isEmpty();
    }

    /** @noinspection unused*/
    public Set<Long> getIds() {
        return new HashSet<>(ids);
    }

    public Set<Long> getRawIds() {
        return new HashSet<>(rawIds);
    }

    @NonNull
    public String toString() {
        return getClass().getSimpleName() + "[ids: " + ids + " rawIds: " + rawIds + "]";
    }
}
