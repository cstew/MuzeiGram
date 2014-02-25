package com.cstewart.android.muzeigram;

import android.content.Context;

import com.cstewart.android.muzeigram.controller.InstagramAuthorizeActivity;
import com.cstewart.android.muzeigram.controller.InstagramRemoteArtSource;
import com.cstewart.android.muzeigram.controller.settings.InstagramSettingsActivity;
import com.cstewart.android.muzeigram.controller.settings.InstagramUserChooserActivity;
import com.cstewart.android.muzeigram.data.instagram.InstagramService;
import com.cstewart.android.muzeigram.data.settings.Settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

@Module (
        injects = {
                InstagramRemoteArtSource.class,
                InstagramAuthorizeActivity.class,
                InstagramSettingsActivity.class,
                InstagramUserChooserActivity.class
        }
)
public class MuzeiGramModule {

    private Context mContext;

    public MuzeiGramModule(Context context) {
        mContext  = context.getApplicationContext();
    }

    @Provides @Singleton Settings provideSettings(Context context) {
        return new Settings(context);
    }

    @Provides InstagramService provideInstagramService(RestAdapter restAdapter) {
        return restAdapter.create(InstagramService.class);
    }

    @Provides RestAdapter provideRestAdapter(Settings settings) {
        final String accessToken = settings.getInstagramToken();

        return new RestAdapter.Builder()
                .setEndpoint("https://api.instagram.com")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade requestFacade) {
                        requestFacade.addEncodedQueryParam("access_token", accessToken);
                    }
                })
                .build();
    }

    @Provides Context provideContext() {
        return mContext;
    }

}
