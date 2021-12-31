package com.ajireapp.farmrecordstore

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.android.material.appbar.MaterialToolbar


class SettingsActivity : AppCompatActivity(),  SharedPreferences.OnSharedPreferenceChangeListener{

    lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar = findViewById(R.id.toolbar)               //for initializing toolbar
        setSupportActionBar(toolbar)                       //for setting and supporting the toolbar

        if(savedInstanceState == null){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.preference_content, SettingsPreference()) //The preference_content is located in activity_settings.xml while the SettingsPreference is the class newly created below.
                .commit()
        }

        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)

        setUpToolbar() //This function is called to setup the toolbar
    }

    private fun setUpToolbar() {
        supportActionBar?.setTitle(R.string.settings)   //This adds title to the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   //This adds the back icon to the toolbar
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    class SettingsPreference : PreferenceFragmentCompat(){
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preference_settings, rootKey)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }
}