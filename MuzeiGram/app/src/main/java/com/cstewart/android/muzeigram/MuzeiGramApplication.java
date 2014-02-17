package com.cstewart.android.muzeigram;

import android.app.Application;
import android.content.Context;

import dagger.ObjectGraph;

public class MuzeiGramApplication extends Application {

    private ObjectGraph mObjectGraph;

    public static MuzeiGramApplication get(Context context) {
        return (MuzeiGramApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mObjectGraph = ObjectGraph.create(new MuzeiGramModule(this));

    }

    public void inject(Object object) {
        mObjectGraph.inject(object);
    }
}
