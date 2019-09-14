package com.example.myapplication

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(vararg user: User)

    @Query("SELECT * FROM User")
    fun getUser(): User

    @Query("DELETE FROM User")
    fun deleteUser()
}