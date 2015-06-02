package com.cstewart.android.muzeigram;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;

public class MuzeiGramApplication extends Application {

    private ObjectGraph mObjectGraph;

    public static MuzeiGramApplication get(Context context) {
        return (MuzeiGramApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        mObjectGraph = ObjectGraph.create(new MuzeiGramModule(this));
    }

    public void inject(Object object) {
        mObjectGraph.inject(object);
    }
}
