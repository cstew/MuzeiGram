package com.cstewart.android.muzeigram.controller.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cstewart.android.muzeigram.R;
import com.cstewart.android.muzeigram.data.instagram.InstagramUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends ArrayAdapter<InstagramUser> {

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