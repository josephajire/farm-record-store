package com.ajireapp.farmrecordstore

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Record::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)

abstract class RecordRoomDatabase: RoomDatabase() {

    abstract fun recordDao(): RecordDao

    companion object{
        private var recordRoomInstance: RecordRoomDatabase? = null
        fun getDatabase(context: Context): RecordRoomDatabase?{
            if (recordRoomInstance == null) {
                synchronized(RecordRoomDatabase::class.java){
                    if (recordRoomInstance == null){
                        recordRoomInstance = Room.databaseBuilder(context.applicationContext,
                            RecordRoomDatabase::class.java, "record_database").build()
                    }
                }
            }
            return recordRoomInstance
        }
    }
}

