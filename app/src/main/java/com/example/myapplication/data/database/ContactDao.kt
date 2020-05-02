package com.example.myapplication.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.model.Contact

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(vararg contact: Contact)

    @Query("SELECT * FROM Contact")
    fun getContact(): Contact

    @Query("DELETE FROM Contact")
    fun deleteContact()
}