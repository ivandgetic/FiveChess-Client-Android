package org.ivandgetic.fivechess.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import org.ivandgetic.fivechess.MyService;
import org.ivandgetic.fivechess.R;

/**
 * Created by ivandgetic on 2016/3/6 0006.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    private SharedPreferences sharedPreferences;
    private Context context;
    private EditTextPreference editTextPreferenceServerAddress;
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        intent = new Intent(context, MyService.class);
        addPreferencesFromResource(R.xml.preferences);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editTextPreferenceServerAddress = (EditTextPreference) findPreference("server_address");

        editTextPreferenceServerAddress.setSummary(editTextPreferenceServerAddress.getText());
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("server_address")) {
            editTextPreferenceServerAddress.setSummary(editTextPreferenceServerAddress.getText());
            context.stopService(intent);
            context.startService(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
