package com.example.myapplication.ui.group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Group

class GroupsListViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    private val tvName: TextView = view.findViewById(R.id.tvName)
    private val tvEventTime: TextView = view.findViewById(R.id.tvEventTime)

    fun bind(group: Group) {
        tvName.text = group.name
        tvEventTime.text = group.date
    }
}

class GroupsListAdapter(
    private val listener: RecyclerViewGroupsClickListener
) : ListAdapter<Group, GroupsListViewHolder>(DIFF_CONFIG) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_groups, parent, false)
        return GroupsListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GroupsListViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            listener.onRecyclerViewItemClick(getItem(position))
        }
    }

    companion object {
        val DIFF_CONFIG = object : DiffUtil.ItemCallback<Group>() {
            override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: Group,
                newItem: Group
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}