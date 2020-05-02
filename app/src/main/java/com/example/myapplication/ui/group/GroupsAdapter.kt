package com.example.myapplication.ui.group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Group
import kotlinx.android.synthetic.main.adapter_item_layout.view.*

typealias ClickListener = (Group) -> Unit

class RvAdapter(private val clickListener: ClickListener) :
    RecyclerView.Adapter<RvAdapter.ViewHolder>() {
    private lateinit var userList: List<Group>

    fun updateGroupList(groupList: List<Group>) {
        this.userList = groupList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val itemContainer =
            LayoutInflater.from(p0.context).inflate(R.layout.adapter_item_layout, p0, false)
        val viewHolder = ViewHolder(itemContainer)
        itemContainer.setOnClickListener {
            clickListener(userList[viewHolder.adapterPosition])
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return if (::userList.isInitialized) userList.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(group: Group) {
            itemView.tvName.text = group.name
        }
    }
}