package com.fs3d.pete_andrews.panicalarm;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class PasswordActivity extends DialogPreference {

    String mCurrentValue, mNewValue;
    Dialog here;
    private EditText passwd1, passwd2;
    private Context mContext;

    public PasswordActivity(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);
        mContext = ctxt;
        setDialogLayoutResource(R.layout.activity_password);
        setPositiveButtonText("Set Password");
        setNegativeButtonText("Cancel");
        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View vw) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String prefStr = prefs.getString(getKey(), "error");
        Log.i("PasswordString", "Retrieved " + prefStr);
        try {
            passwd1 = (EditText) vw.findViewById(R.id.et_password1);
            passwd2 = (EditText) vw.findViewById(R.id.et_password2);
        } catch (NullPointerException err) {
            // Something is wrong. The reference to the password fields could not be retrieved.
        }
        if (prefStr.equals("error") || prefStr.equals("null") || prefStr.equals("") || prefStr.equals("0000")) {
            Log.i("PasswordString", "Retrieved one of the error condition values. Stored password not valid. A new one can be accepted.");
        } else {
            Log.i("PasswordString", "Valid password stored. It will need to be entered before it can be changed.");
        }
        super.onBindDialogView(vw);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (!positiveResult)
            return;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        if (passwd1 != null && passwd2 != null) {
            Log.i("PasswordString", "Return String 1 <" + passwd1.getText().toString() + "> Return String 2 <" + passwd2.getText().toString() + ">");
            if (passwd1.getText().toString().equals(passwd2.getText().toString())) {
                Log.i("PasswordString", "Strings match.");
                editor.putString(getKey(), passwd1.getText().toString());
                editor.commit();
            } else {
                Log.w("PasswordString", "String mismatch. Password will not be saved.");
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }
        final SavedState myState = new SavedState(superState);
        myState.value = mNewValue;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        // mPasswordWidget.setValue(myState.value);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defValue) {
        if (restorePersistedValue) {
            mCurrentValue = this.getPersistedString("0000");
        } else {
            mCurrentValue = (String) defValue;
        }
    }

    private static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        String value;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            value = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
        }
    }
}
