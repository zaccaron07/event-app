package com.example.myapplication.injection

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.myapplication.model.database.AppDatabase
import com.example.myapplication.ui.group.GroupViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupViewModel::class.java)) {
            val db =
                Room.databaseBuilder(context, AppDatabase::class.java, "app-evento")
                    .build()
            @Suppress("UNCHECKED_CAST")
            return GroupViewModel(db.contactDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}