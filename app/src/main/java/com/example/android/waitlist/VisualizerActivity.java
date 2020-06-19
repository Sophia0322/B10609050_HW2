package com.example.android.waitlist;

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class VisualizerActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final int MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE = 88;
    private LinearLayout mMainView;
    // Will display the party size number
    TextView partySizeTextView;

    public VisualizerActivity(View itemView) {

        partySizeTextView = (TextView) itemView.findViewById(R.id.party_size_text_view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainView = (LinearLayout) findViewById(R.id.activity_list);

        //defaultSetup();
        setupSharedPreferences();
    }

    // COMPLETED (4) Update setupSharedPreferences and onSharedPreferenceChanged to load the color
    // from shared preferences. Call setColor, passing in the color you got
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        loadColorFromPreferences(sharedPreferences);
        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener) this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void loadColorFromPreferences(SharedPreferences sharedPreferences) {
        partySizeTextView.setBackground(Drawable.createFromPath(sharedPreferences.getString(getString(R.string.pref_color_key),
                getString(R.string.pref_color_red_value))));
        System.out.println(sharedPreferences.getString(getString(R.string.pref_color_key),
                getString(R.string.pref_color_red_value)));
    }
    /*private void defaultSetup() {
        mMainView.setColor(getString(R.string.pref_color_red_value));
    }*/

    /**
     * Methods for setting up the menu
     **/
    // COMPLETED (5) Add the menu to the menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our visualizer_menu layout to this menu */
        inflater.inflate(R.menu.menu_item, menu);
        /* Return true so that the visualizer_menu is displayed in the Toolbar */
        return true;
    }

    // COMPLETED (6) When the "Settings" menu item is pressed, open SettingsActivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Updates the screen if the shared preferences change. This method is required when you make a
    // class implement OnSharedPreferenceChangedListener
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_color_key))) {
            loadColorFromPreferences(sharedPreferences);
        }
    }


}