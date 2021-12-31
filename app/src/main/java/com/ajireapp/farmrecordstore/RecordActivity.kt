package com.ajireapp.farmrecordstore

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.appbar.MaterialToolbar

class RecordActivity : AppCompatActivity() {

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


        txvLastUpdated.visibility = View.INVISIBLE
        buttonSave.setOnClickListener {
            saveRecord()
            finish()
        }

        buttonCancel.setOnClickListener {
            finish()
        }
    }

   ////Record will also be saved when the back button of the device is pressed.
    override fun onBackPressed() {
        saveRecord()
        super.onBackPressed()
    }

    private fun saveRecord(){
        val resultIntent = Intent()
        if (textRecordTitle.text.toString().trim() == "" &&
            textRecordText.text.toString().trim() == "" ){
            setResult(Activity.RESULT_CANCELED, resultIntent)
        }else{
            val category = recordCategory.text.toString()
            val title = textRecordTitle.text.toString()
            val details = textRecordText.text.toString()

            resultIntent.putExtra(NEW_CATEGORY, category)
            resultIntent.putExtra(NEW_TITLE, title)
            resultIntent.putExtra(NEW_DETAILS, details)
            setResult(Activity.RESULT_OK, resultIntent)
        }
    }

    companion object{
        const val NEW_CATEGORY = "new_category"
        const val NEW_TITLE = "new_title"
        const val NEW_DETAILS ="new_details"
    }
}