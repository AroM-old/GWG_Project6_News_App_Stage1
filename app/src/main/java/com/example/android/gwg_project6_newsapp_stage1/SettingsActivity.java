package com.example.android.gwg_project6_newsapp_stage1;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SettingsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class NewsPreferencesFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        private static final String LOG_ID = NewsPreferencesFragment.class.getSimpleName();

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference sectionName = findPreference(getString(R.string.settings_section_key));
            bindPreferenceSummaryToValue(sectionName);

            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);

            Log.i(LOG_ID, "TEST Initial prefs section....." + sectionName + "Order By...." + orderBy);

        }

        private void bindPreferenceSummaryToValue(Preference sectionName) {
            sectionName.setOnPreferenceChangeListener(this);
            SharedPreferences sectionNames = PreferenceManager.getDefaultSharedPreferences(sectionName.getContext());
            String sectionNameString = sectionNames.getString(sectionName.getKey(), "");
            onPreferenceChange(sectionName, sectionNameString);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            preference.setSummary(stringValue);
            if (preference instanceof ListPreference){
                ListPreference listPreference = (ListPreference) preference;
                int predIndex = listPreference.findIndexOfValue(stringValue);
                if (predIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[predIndex]);
                }
            }else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    }
}
