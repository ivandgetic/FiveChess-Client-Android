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
    private SharedPreferences preferences;
    private Context context;
    private EditTextPreference editTextPreferenceServerAddress, editTextPreferenceUsername;
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        addPreferencesFromResource(R.xml.preferences);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editTextPreferenceServerAddress = (EditTextPreference) findPreference("server_address");
        editTextPreferenceUsername = (EditTextPreference) findPreference("name");

        editTextPreferenceServerAddress.setSummary(editTextPreferenceServerAddress.getText());
        editTextPreferenceUsername.setSummary(editTextPreferenceUsername.getText());
//        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
//            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
//                if (key.equals("server_address")) {
//                    editTextPreferenceServerAddress.setSummary(editTextPreferenceServerAddress.getText());
//                    // TODO: 2016/3/6 0006 重启服务
//                    System.out.println("change");
//                    intent = new Intent(context, MyService.class);
//                    context.stopService(intent);
//                    context.startService(intent);
//                }
//                if (key.equals("name")) {
//                    editTextPreferenceUsername.setSummary(editTextPreferenceUsername.getText());
//                }
//            }
//        };
//        preferences.registerOnSharedPreferenceChangeListener(listener);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("server_address")) {
            editTextPreferenceServerAddress.setSummary(editTextPreferenceServerAddress.getText());
            // TODO: 2016/3/6 0006 重启服务
            System.out.println("change");
            intent = new Intent(context, MyService.class);
            context.stopService(intent);
            context.startService(intent);
        }
        if (key.equals("name")) {
            editTextPreferenceUsername.setSummary(editTextPreferenceUsername.getText());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
