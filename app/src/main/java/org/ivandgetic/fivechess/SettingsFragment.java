package org.ivandgetic.fivechess;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by ivandgetic on 14/7/9.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Context context;
    SharedPreferences preferences;
    EditTextPreference editTextPreferenceServerAddress = null;
    EditTextPreference editTextPreferenceName = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        addPreferencesFromResource(R.xml.fragment_settings);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editTextPreferenceServerAddress = (EditTextPreference) findPreference("server_address");
        editTextPreferenceServerAddress.setSummary(editTextPreferenceServerAddress.getText());
        editTextPreferenceName = (EditTextPreference) findPreference("name");
        editTextPreferenceName.setSummary(editTextPreferenceName.getText());
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle(getString(R.string.action_settings));
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("server_address")) {
            editTextPreferenceServerAddress.setSummary(editTextPreferenceServerAddress.getText());
        }
        if (key.equals("name")) {
            editTextPreferenceName.setSummary(editTextPreferenceName.getText());
        }
    }
}
