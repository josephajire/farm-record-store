package com.ajireapp.farmrecordstore

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.google.android.material.appbar.MaterialToolbar

class EditRecordActivity : AppCompatActivity() {
    var id: String? = null

    lateinit var recordCategory : EditText
    lateinit var textRecordTitle: EditText
    lateinit var textRecordText: EditText
    lateinit var buttonSave: Button
    lateinit var buttonCancel: Button
    lateinit var txvLastUpdated: TextView
    lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        recordCategory = findViewById(R.id.recordCategory)
        textRecordTitle = findViewById(R.id.textRecordTitle)
        textRecordText = findViewById(R.id.textRecordText)
        buttonSave = findViewById(R.id.buttonSave)
        buttonCancel = findViewById(R.id.buttonCancel)
        txvLastUpdated = findViewById(R.id.txvLastUpdated)

        val bundle: Bundle? = intent.extras
        bundle?.let {
            id = bundle.getString("id")
            val title = bundle.getString("title")
            val category = bundle.getString("category")
            val details = bundle.getString("details")
            val lastUpdated = bundle.getString("lastUpdated")

            recordCategory.setText(category)
            textRecordTitle.setText(title)
            textRecordText.setText(details)
            txvLastUpdated.text = lastUpdated
        }

        buttonSave.setOnClickListener{
            updateRecord()
            finish()
        }

        buttonCancel.setOnClickListener {
            finish()
        }
    }

    //Record will also be updated when the back button of the device is pressed.
    override fun onBackPressed() {
        updateRecord()
        super.onBackPressed()
    }

    private fun updateRecord(){
        val updatedCategory = recordCategory.text.toString()
        val updatedTitle = textRecordTitle.text.toString()
        val updatedDetails = textRecordText.text.toString()

        val resultIntent = Intent()
        resultIntent.putExtra(ID, id)
        resultIntent.putExtra(UPDATED_CATEGORY, updatedCategory)
        resultIntent.putExtra(UPDATED_TITLE, updatedTitle)
        resultIntent.putExtra(UPDATED_DETAILS, updatedDetails)
        setResult(Activity.RESULT_OK, resultIntent)
    }

    companion object{
        const val ID = "record_id"
        const val UPDATED_CATEGORY = "category_name"
        const val UPDATED_TITLE = "title_name"
        const val UPDATED_DETAILS = "details"
    }
}
