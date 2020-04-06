package com.example.myapplication.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(vararg contact: Contact)

    @Query("SELECT * FROM Contact")
    fun getContact(): Single<Contact>

    @Query("DELETE FROM Contact")
    fun deleteContact()
}