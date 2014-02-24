package com.cstewart.android.muzeigram.controller.settings;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cstewart.android.muzeigram.R;

abstract class SettingAdapter<T> extends ArrayAdapter<T> {

    protected abstract void updateTitle(TextView textView, T item);

    public SettingAdapter(Context context, T[] items) {
        super(context, R.layout.view_spinner, items);
        setDropDownViewResource(R.layout.view_spinner);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        return updateText(view, position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        return updateText(view, position);
    }

    private View updateText(View view, int position) {
        T item = getItem(position);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        updateTitle(textView, item);
        return view;
    }
}