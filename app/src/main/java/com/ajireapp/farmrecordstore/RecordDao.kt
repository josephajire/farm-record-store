package com.ajireapp.farmrecordstore

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecordDao {
    @Insert
    fun insert(record: Record)


//    @Query("SELECT * FROM books")
//    fun getAllBooks(): LiveData<List<Book>>
    //The above two lines are alternatives to the two lines below.

    @get:Query("SELECT * FROM records")
    val allRecords: LiveData<List<Record>>

    //The next two lines implemented search query. NB: title, category and details are from Record.kt
    @Query("SELECT * FROM records WHERE title LIKE :desc OR category LIKE :desc OR details LIKE :desc")
    fun getSearchResults(desc: String): LiveData<List<Record>>

    @Update
    fun update(record: Record)

    @Delete
    fun delete(record: Record)
}
