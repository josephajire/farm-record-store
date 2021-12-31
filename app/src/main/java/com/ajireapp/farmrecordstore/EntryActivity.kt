package com.ajireapp.farmrecordstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class EntryActivity : AppCompatActivity() {

    private lateinit var btnEntry : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        /*With these, when the user clicks the button_entry, he will be redirected to MainActivity screen*/
        btnEntry =  findViewById(R.id.btnEntry)

        btnEntry.setOnClickListener {
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}