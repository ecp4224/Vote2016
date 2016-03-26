package me.eddiep.android.voting.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Election implements Parcelable {
    private String name;
    private DateTime date;
    private boolean isPinned;
    private int background;
    private String description;

    private ArrayList<Candidate> candidates = new ArrayList<>();

    public Election(String name, Date date, int background) {
        this.name = name;
        this.date = new DateTime(date);
        this.background = background;
    }

    protected Election(Parcel in) {
        name = in.readString();
        isPinned = in.readByte() != 0;
        date = new DateTime(in.readLong());
        background = in.readInt();
        description = in.readString();
        in.readTypedList(candidates, Candidate.CREATOR);
    }

    public ArrayList<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(ArrayList<Candidate> candidates) {
        this.candidates = candidates;
    }

    public int getBackground() {
        return background;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static final Creator<Election> CREATOR = new Creator<Election>() {
        @Override
        public Election createFromParcel(Parcel in) {
            return new Election(in);
        }

        @Override
        public Election[] newArray(int size) {
            return new Election[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setIsPinned(boolean isPinned) {
        this.isPinned = isPinned;
    }

    public int getDaysRemaining() {
        DateTime today = DateTime.now();

        return Days.daysBetween(today.toLocalDate(), date.toLocalDate()).getDays();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByte((byte) (isPinned ? 1 : 0));
        dest.writeLong(date.getMillis());
        dest.writeInt(background);
        dest.writeString(description);
        dest.writeTypedList(candidates);
    }
}
