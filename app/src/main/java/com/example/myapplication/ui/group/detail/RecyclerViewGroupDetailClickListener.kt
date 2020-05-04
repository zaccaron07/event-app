package com.example.myapplication.ui.group.detail

import com.example.myapplication.data.model.Contact

interface RecyclerViewGroupDetailClickListener {
    fun onRecyclerViewItemClick(contact: Contact)
}