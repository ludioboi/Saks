package com.example.saks.presence_list;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.Date;

public class PresenceUser {

    private String name, class_;
    long present_from, present_until, date;
    int id;

    public PresenceUser(String name, int id, String class_, long date, long present_from, long present_until) {
        this.name = name;
        this.id = id;
        this.class_ = class_;
        this.date = date;
        this.present_from = present_from;
        this.present_until = present_until;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getClass_() {
        return class_;
    }

    public long getPresent_from() {
        return present_from;
    }

    public void setPresent_from(long present_from) {
        this.present_from = present_from;
    }

    public long getPresent_until() {
        return present_until;
    }

    public void setPresent_until(long present_until) {
        this.present_until = present_until;
    }

    public long getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClass_(String class_) {
        this.class_ = class_;
    }

    public boolean isPresent(){
        LocalDateTime instant = LocalDateTime.now(ZoneId.of(ZoneId.systemDefault().getId()));
        int currHour = instant.getHour();
        int minutes = instant.getMinute();
        long currTime = 1000L * 60 * 60 * currHour + 1000L *60* minutes;
        if (present_from == 0) {
            return false;
        }
        return currTime >= present_from && currTime <= present_until;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "PresenceUser{" +
                "name='" + name + '\'' +
                ", class_='" + class_ + '\'' +
                ", present_from=" + present_from +
                ", present_until=" + present_until +
                ", date=" + date +
                ", id=" + id +
                '}';
    }
}
