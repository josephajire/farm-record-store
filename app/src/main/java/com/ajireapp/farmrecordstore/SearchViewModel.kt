package com.ajireapp.farmrecordstore

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class SearchViewModel(application: Application): AndroidViewModel(application) {

    val allRecords: LiveData<List<Record>>
    private val recordRepository = RecordRepository(application)


    init {
        allRecords = recordRepository.allRecords
    }

    fun update(record: Record){
        recordRepository.update(record)
    }

    fun delete(record: Record){
        recordRepository.delete(record)
    }

    fun searchForItems(desc: String) : LiveData<List<Record>>?{
        return recordRepository.search(desc)
    }
}
