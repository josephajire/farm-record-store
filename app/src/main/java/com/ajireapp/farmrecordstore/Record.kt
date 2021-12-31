package com.ajireapp.farmrecordstore


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity (tableName = "records")
class Record (@PrimaryKey
            val id: String,

            val category: String,

            val title: String,

            val details: String,

            @ColumnInfo(name = "last_updated")
            val lastUpdated: Date?)
