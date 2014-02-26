package com.cstewart.android.muzeigram.controller.settings;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SearchView;

import com.cstewart.android.muzeigram.R;
import com.cstewart.android.muzeigram.controller.MuzeiGramActivity;
import com.cstewart.android.muzeigram.data.instagram.InstagramService;
import com.cstewart.android.muzeigram.data.instagram.InstagramUser;
import com.cstewart.android.muzeigram.data.instagram.UserSearchResponse;

import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class InstagramUserSearchActivity extends MuzeiGramActivity {

    @Inject InstagramService mInstagramService;

    private ListView mUserList;

    public static Intent newIntent(Context context) {
        return new Intent(context, InstagramUserSearchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_user_search);
        mUserList = (ListView) findViewById(R.id.activity_instagram_user_search_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_instagram_user_search, menu);

        // Set up SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.activity_instagram_user_search_searchview).getActionView();
        searchView.setIconified(false);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(mQueryTextListener);

        return true;
    }

    private void onQueryChange(String query) {

        mInstagramService.getUserAccount(query, new Callback<UserSearchResponse>() {
            @Override
            public void success(UserSearchResponse userSearchResponse, Response response) {
                setupAdapter(userSearchResponse.getUsers());
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    private void setupAdapter(List<InstagramUser> users) {
        UserAdapter adapter = new UserAdapter(this, users);
        mUserList.setAdapter(adapter);
    }

    private SearchView.OnQueryTextListener mQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            onQueryChange(s);
            return true;
        }
    };
}
