package com.cstewart.android.muzeigram.controller.settings;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;

import com.cstewart.android.muzeigram.MuzeiGramApplication;
import com.cstewart.android.muzeigram.data.settings.InstagramUserCollection;
import com.cstewart.android.muzeigram.data.settings.Settings;

import javax.inject.Inject;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class UserAccountsAlertDialogFragment extends DialogFragment {

    @Inject Settings mSettings;

    private InstagramUserCollection mUsers;

    public static UserAccountsAlertDialogFragment newInstance() {
        return new UserAccountsAlertDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MuzeiGramApplication.get(getActivity()).inject(this);

        mUsers = mSettings.getUserCollection();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

//        Dialog dialog = new AlertDialog.Builder(getActivity())
//                .setTitle(R.string.user_accounts_dialog_title)
//                .setMultiChoiceItems(mUsers.toCharSequence(), mUsers.toCheckedItems(), new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
//                        mUsers.setChecked(i, b);
//                    }
//                })
//                .setPositiveButton(android.R.string.ok, null)
//                .create();

//        return dialog;
    }
}
