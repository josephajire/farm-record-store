package com.ajireapp.farmrecordstore

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class SearchResultActivity : AppCompatActivity(), RecordRecyclerAdapter.OnDeleteClickListener {

    private lateinit var searchViewModel: SearchViewModel  //Code for initializing the search view model (a)
    lateinit var recyclerView: RecyclerView  //for recycler view (a)
    lateinit var toolbar: MaterialToolbar   //To initialize the toolbar
    private var recordRecyclerAdapter: RecordRecyclerAdapter? = null   //for recordRecyclerAdapter (a)
    lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)               //for initializing toolbar
        fab = findViewById(R.id.fab)                      //for initializing fab
        recyclerView = findViewById(R.id.recordItems)           //for recycler view (b)

        //code for initializing the searchViewModel  (b)
        searchViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(SearchViewModel::class.java
        )

        setSupportActionBar(toolbar)

        fab.visibility = View.INVISIBLE   // this was added to make the fab button in content_main.xml invisible in SearchResultActivity

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        //Codes related to adding functionality to Switchpreference located in the mySettings() fucntion
        mySettings()


        //Code for inflating the recyclerView using the RecyclerViewAdapter
        recordRecyclerAdapter = RecordRecyclerAdapter(this, this) //for recordRecyclerAdapter (b)
        recyclerView.adapter = recordRecyclerAdapter

        //Code for adding layout Manager to the recycler View and making the latest record to be on the top of the recyclerview.
        val myLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        myLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = myLayoutManager

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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar  and then activates the search menu to work
        menuInflater.inflate(R.menu.search_menu, menu)
        val search = menu.findItem(R.id.searchItems)
        val searchView = search.actionView as androidx.appcompat.widget.SearchView
        searchView.isSubmitButtonEnabled = true

        //After entering the line next to this comment, I pressed Alt + Enter on 'object' and then selected to implement its members.
        //Then, the onQueryTextSubmit and onQueryTextChange overrides were added. I then added some codes into them.
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
               if (query != null){
                   getItemsFromDb(query)
               }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null){
                    getItemsFromDb(newText)
                }
                return true
            }
        }
        )
        return true
    }
//The getItemFromDb function below was called inside the onCreateOptionsmenu above
    //The function is part of the code to handle the search funtionality

    private fun getItemsFromDb(searchText: String) {
        var searchText = searchText
        searchText = "%$searchText%"

        searchViewModel.searchForItems(desc = searchText)?.observe(this, Observer { records ->
        records?.let { recordRecyclerAdapter!!.setRecords(records) }
        })
    }

// The codes below are to handle the updating of the records directly from this SearchResultActivity.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UPDATE_RECORD_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val id = data?.getStringExtra(EditRecordActivity.ID)
            val categoryName = data?.getStringExtra(EditRecordActivity.UPDATED_CATEGORY)
            val titleName = data?.getStringExtra(EditRecordActivity.UPDATED_TITLE)
            val details = data?.getStringExtra(EditRecordActivity.UPDATED_DETAILS)
            val currentTime = Calendar.getInstance().time

            val record = Record(id!!, categoryName!!, titleName!!, details!!, currentTime)

            //code to update
            searchViewModel.update(record)
            Toast.makeText(applicationContext, R.string.record_updated, Toast.LENGTH_LONG).show()

        }else{
            Toast.makeText(applicationContext, R.string.not_saved, Toast.LENGTH_LONG).show()
        }
    }

    //The codes in the onDeleteClickListener below handles the deletion of records right from the SearchResultActivity
    //Alert Dialog was activated to ensure that users confirm a record before deletion
    override fun onDeleteClickListener(myRecord: Record) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.confirmation_delete))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) {
                    dialog, id ->
                searchViewModel.delete(myRecord)   //This is the actual code that deletes records.
                Toast.makeText(applicationContext, R.string.record_deleted, Toast.LENGTH_LONG).show()
            }
            .setNegativeButton(getString(R.string.no)) {
                    dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()

    }

    companion object{
        const val UPDATE_RECORD_ACTIVITY_REQUEST_CODE = 2
    }
}