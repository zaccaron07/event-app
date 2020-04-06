package com.example.myapplication.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.model.Contact
import com.example.myapplication.model.ContactDao

@Database(
    entities = [Contact::class],
    version = 5
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}