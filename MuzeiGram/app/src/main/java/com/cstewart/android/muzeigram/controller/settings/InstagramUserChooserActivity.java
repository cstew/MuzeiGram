package com.cstewart.android.muzeigram.controller.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.cstewart.android.muzeigram.R;
import com.cstewart.android.muzeigram.controller.MuzeiGramActivity;
import com.cstewart.android.muzeigram.controller.settings.swipe.SwipeDismissListViewTouchListener;
import com.cstewart.android.muzeigram.data.instagram.InstagramUser;
import com.cstewart.android.muzeigram.data.instagram.InstagramUserCollection;
import com.cstewart.android.muzeigram.data.settings.Settings;

import java.util.List;

import javax.inject.Inject;

public class InstagramUserChooserActivity extends MuzeiGramActivity {

    @Inject Settings mSettings;

    private InstagramUserCollection mUserCollection;

    private ListView mUserList;
    private UserAdapter mAdapter;

    public static Intent newIntent(Context context) {
        return new Intent(context, InstagramUserChooserActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_user_chooser);

        mUserList = (ListView) findViewById(R.id.activity_instagram_user_chooser_list);
        SwipeTouchListener swipeTouchListener = new SwipeTouchListener(mUserList);
        mUserList.setOnTouchListener(swipeTouchListener);
        mUserList.setOnScrollListener(swipeTouchListener.makeScrollListener());

        setupAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_instagram_user_chooser, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.activity_instagram_user_chooser_searchview:
                startActivity(InstagramUserSearchActivity.newIntent(this));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupAdapter() {
        mUserCollection = mSettings.getUserCollection();
        List<InstagramUser> users = mUserCollection.getInstagramUsers();
        mAdapter = new UserAdapter(this, users);
        mUserList.setAdapter(mAdapter);
    }

    private class SwipeTouchListener extends SwipeDismissListViewTouchListener {

        public SwipeTouchListener(ListView listView) {
            super(listView, new SwipeDismissListViewTouchListener.DismissCallbacks() {
                @Override
                public boolean canDismiss(int position) {
                    return true;
                }

                @Override
                public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                    for (int position : reverseSortedPositions) {
                        InstagramUser user = mAdapter.getItem(position);
                        mAdapter.remove(user);
                        mUserCollection.removeUser(user);
                    }
                    mSettings.saveUserCollection(mUserCollection);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

}
