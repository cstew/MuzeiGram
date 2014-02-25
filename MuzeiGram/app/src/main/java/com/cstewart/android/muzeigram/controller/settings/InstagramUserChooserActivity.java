package com.cstewart.android.muzeigram.controller.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cstewart.android.muzeigram.R;
import com.cstewart.android.muzeigram.controller.MuzeiGramActivity;
import com.cstewart.android.muzeigram.data.instagram.InstagramUser;
import com.cstewart.android.muzeigram.data.instagram.InstagramUserCollection;
import com.cstewart.android.muzeigram.data.settings.Settings;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

public class InstagramUserChooserActivity extends MuzeiGramActivity {

    @Inject Settings mSettings;

    private ListView mUserList;

    public static Intent newIntent(Context context) {
        return new Intent(context, InstagramUserChooserActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_user_chooser);

        mUserList = (ListView) findViewById(R.id.activity_instagram_user_chooser_list);

        setupAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_instagram_user_chooser, menu);
        return true;
    }

    private void setupAdapter() {
        InstagramUserCollection userCollection = mSettings.getUserCollection();
        List<InstagramUser> users = userCollection.getInstagramUsers();
        UserAdapter adapter = new UserAdapter(this, users);
        mUserList.setAdapter(adapter);
    }

    private class UserAdapter extends ArrayAdapter<InstagramUser> {

        public UserAdapter(Context context, List<InstagramUser> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_instagram_user, parent, false);
            }

            InstagramUser user = getItem(position);

            TextView usernameText = (TextView) convertView.findViewById(R.id.view_instagram_user_name);
            usernameText.setText(user.getUsername());

            ImageView userImageView = (ImageView) convertView.findViewById(R.id.view_instagram_user_image);
            Picasso.with(getContext()).load(user.getProfilePicture()).into(userImageView);

            return convertView;
        }
    }
}
