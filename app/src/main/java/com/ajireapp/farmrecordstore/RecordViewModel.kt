package com.ajireapp.farmrecordstore

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class RecordViewModel(application: Application): AndroidViewModel(application) {

    val allRecords: LiveData<List<Record>>
    private val recordRepository = RecordRepository(application)

    init {
        allRecords = recordRepository.allRecords
    }

    fun insert(record: Record) {
        recordRepository.insert(record)
    }

    fun update(record: Record){
        recordRepository.update(record)
    }

    fun delete(record: Record){
        recordRepository.delete(record)
    }
}
