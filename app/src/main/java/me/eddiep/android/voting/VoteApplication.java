package me.eddiep.android.voting;

import android.app.Application;
import android.content.Intent;

import net.danlew.android.joda.JodaTimeAndroid;

public class VoteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
