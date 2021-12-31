package com.ajireapp.farmrecordstore

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), RecordRecyclerAdapter.OnDeleteClickListener {

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var recordViewModel: RecordViewModel
    lateinit var recyclerView: RecyclerView  //for recycler view (a)
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var toolbar: MaterialToolbar   //To initialize the toolbar
    lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)     //for initializing drawerLayout
        navView = findViewById(R.id.navView)               //for initializing navView
        toolbar = findViewById(R.id.toolbar)               //for initializing toolbar
        fab = findViewById(R.id.fab)                      //for initializing fab
        recyclerView = findViewById(R.id.recordItems)           //for recycler view (b)

        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        //Codes related to adding functionality to Switchpreference located in the mySettings() fucntion
        mySettings()

        //Code for inflating the recyclerView using the RecyclerViewAdapter
        val recordRecyclerAdapter = RecordRecyclerAdapter(this, this)
        recyclerView.adapter = recordRecyclerAdapter

        //Code for adding layout Manager to the recycler View and making the latest record to be on the top of the recyclerview.
        val myLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        myLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = myLayoutManager


        fab.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            startActivityForResult(intent, NEW_RECORD_ACTIVITY_REQUEST_CODE)
        }

        recordViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(RecordViewModel::class.java)
        recordViewModel.allRecords.observe(this, Observer{records ->
            records?.let{
                recordRecyclerAdapter.setRecords(records)
            }
        })

    navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navHome -> Toast.makeText(applicationContext, getString(R.string.toast_you_are_here), Toast.LENGTH_LONG).show()
                R.id.navNewRecord-> goToNewRecord()
                R.id.navSearch-> goToSearchResultActivity()
                R.id.navSettings -> goToSettingsActivity()
            }
            drawerLayout.closeDrawer(GravityCompat.START) //This will close the drawer after the click of the Nav menu item
            true
        }
    }

    private fun mySettings() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

//I initialized each of the preference items in the preference_settings.xml that needs user's action and set their default values.
        val keyStatus = prefs.getString("key_status", getString(R.string.status_default))
        val keyWhiteBackground = prefs.getBoolean("key_white_background", false) //the default value is set to false.


// If the White Home Background switch is ON, then the background should be white but if it is OFF, then
//the background should be dark green. As in the codes that follows.
        if (keyWhiteBackground){
            rvCoordinatorLayout.setBackgroundColor(getResources().getColor(android.R.color.white))
        }else{
            rvCoordinatorLayout.setBackgroundColor(getResources().getColor(R.color.my_primary_dark_color))
        }
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        if (item.itemId == R.id.action_settings){
            goToSettingsActivity()
        }

        if (item.itemId == R.id.action_new_record){
            goToNewRecord()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToNewRecord() {
        val intent = Intent(this, RecordActivity::class.java)
        startActivityForResult(intent, NEW_RECORD_ACTIVITY_REQUEST_CODE)
    }

    private fun goToSettingsActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun goToSearchResultActivity() {
        startActivity(Intent(this, SearchResultActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == NEW_RECORD_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val id = UUID.randomUUID().toString()
            val categoryName = data?.getStringExtra(RecordActivity.NEW_CATEGORY)
            val titleName = data?.getStringExtra(RecordActivity.NEW_TITLE)
            val details = data?.getStringExtra(RecordActivity.NEW_DETAILS)
            val currentTime = Calendar.getInstance().time

            val record = Record(id, categoryName!!, titleName!!, details!!, currentTime)

            recordViewModel.insert(record)
            Toast.makeText(applicationContext, R.string.record_saved, Toast.LENGTH_LONG).show()

        } else if (requestCode == UPDATE_RECORD_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val id = data?.getStringExtra(EditRecordActivity.ID)
            val categoryName = data?.getStringExtra(EditRecordActivity.UPDATED_CATEGORY)
            val titleName = data?.getStringExtra(EditRecordActivity.UPDATED_TITLE)
            val details = data?.getStringExtra(EditRecordActivity.UPDATED_DETAILS)
            val currentTime = Calendar.getInstance().time

            val record = Record(id!!, categoryName!!, titleName!!, details!!, currentTime)

            //code to update
            recordViewModel.update(record)
            Toast.makeText(applicationContext, R.string.record_updated, Toast.LENGTH_LONG).show()

        }else{
            Toast.makeText(applicationContext, R.string.not_saved, Toast.LENGTH_LONG).show()
        }
    }

//Alert Dialog was activated to ensure that users confirm a record before deletion
    override fun onDeleteClickListener(myRecord: Record) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage(getString(R.string.confirm_record_delete))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) {
                dialog, id ->
                recordViewModel.delete(myRecord)
                Toast.makeText(applicationContext, R.string.record_deleted, Toast.LENGTH_LONG).show()
            }
            .setNegativeButton(getString(R.string.no)) {
                dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()

    }

    override fun onResume() {
        super.onResume()
        recyclerView.adapter?.notifyDataSetChanged()
    }

    companion object{
        private const val NEW_RECORD_ACTIVITY_REQUEST_CODE = 1
        const val UPDATE_RECORD_ACTIVITY_REQUEST_CODE = 2
    }
}