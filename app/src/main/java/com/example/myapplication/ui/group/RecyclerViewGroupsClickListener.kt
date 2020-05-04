package com.example.myapplication.ui.group

import com.example.myapplication.data.model.Group

interface RecyclerViewGroupsClickListener {
    fun onRecyclerViewItemClick(group: Group)
}