package com.example.myapplication.ui.contact

import com.example.myapplication.data.model.Contact

interface RecyclerViewContactClickListener {
    fun onRecyclerViewItemClick(contact: Contact)
}