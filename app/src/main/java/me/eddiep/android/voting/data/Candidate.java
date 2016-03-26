package me.eddiep.android.voting.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Candidate implements Parcelable {
    private String name;
    private String party;
    private int background;
    private int profile;
    private String description;

    public Candidate(String name, String party, int background, int profile, String description) {
        this.name = name;
        this.party = party;
        this.background = background;
        this.profile = profile;
        this.description = description;
    }

    protected Candidate(Parcel in) {
        name = in.readString();
        party = in.readString();
        background = in.readInt();
        profile = in.readInt();
        description = in.readString();
    }

    public static final Creator<Candidate> CREATOR = new Creator<Candidate>() {
        @Override
        public Candidate createFromParcel(Parcel in) {
            return new Candidate(in);
        }

        @Override
        public Candidate[] newArray(int size) {
            return new Candidate[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public int getBackground() {
        return background;
    }

    public int getProfile() {
        return profile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(party);
        dest.writeInt(background);
        dest.writeInt(profile);
        dest.writeString(description);
    }
}
