package com.ajireapp.farmrecordstore


import android.app.Application
import android.os.AsyncTask
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class RecordRepository(application: Application) {

    val allRecords: LiveData<List<Record>>
    private val recordDao : RecordDao

    init {
        val recordDb = RecordRoomDatabase.getDatabase(application)
        recordDao = recordDb!!.recordDao()
        allRecords = recordDao.allRecords
    }

    //The next two lines are for implementing the search functionality
    @WorkerThread
    fun search(desc: String): LiveData<List<Record>>?{
        return recordDao.getSearchResults(desc)
    }

    fun insert(record: Record) {
        InsertAsyncTask(recordDao).execute(record)
    }

    fun update(record: Record){
        UpdateAsyncTask(recordDao).execute(record)
    }

    fun delete(record: Record){
        DeleteAsyncTask(recordDao).execute(record)
    }

    companion object {
        private class InsertAsyncTask(private val recordDao: RecordDao) : AsyncTask<Record, Void, Void>(){

            override fun doInBackground(vararg records: Record): Void? {
                recordDao.insert(records[0])
                return null
            }
        }

        private class UpdateAsyncTask(private val recordDao: RecordDao) : AsyncTask<Record, Void, Void>(){

            override fun doInBackground(vararg records: Record): Void? {
                recordDao.update(records[0])
                return null
            }
        }

        private class DeleteAsyncTask(private val recordDao: RecordDao) : AsyncTask<Record, Void, Void>(){

            override fun doInBackground(vararg records: Record): Void? {
                recordDao.delete(records[0])
                return null
            }
        }

    }
}
