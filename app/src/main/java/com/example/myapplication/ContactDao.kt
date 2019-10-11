package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(vararg contact: Contact)

    @Query("SELECT * FROM Contact")
    fun getContact(): Contact

    @Query("DELETE FROM Contact")
    fun deleteContact()
}