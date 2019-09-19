package com.example.myapplication

import androidx.room.*

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(vararg contact: Contact)

    @Query("SELECT * FROM Contact")
    fun getContact(): Contact

    @Query("DELETE FROM Contact")
    fun deleteContact()
}