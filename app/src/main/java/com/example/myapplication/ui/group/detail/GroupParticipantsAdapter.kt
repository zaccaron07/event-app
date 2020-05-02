package com.example.myapplication.ui.group.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Contact
import kotlinx.android.synthetic.main.contact_detail_adapter_item_layout.view.tvName
import kotlinx.android.synthetic.main.group_participants_adapter_item_layout.view.*

class GroupParticipantsAdapter(
    private val contactList: MutableList<Contact>,
    private val clickListener: (Contact, Switch) -> Unit,
    private val contactId: String
) :
    RecyclerView.Adapter<GroupParticipantsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.group_participants_adapter_item_layout, p0, false)

        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contactList[position], clickListener, contactId)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            contact: Contact,
            clickListener: (Contact, Switch) -> Unit,
            contactId: String
        ) {
            itemView.tvName.text = contact.name
            itemView.switchParticipate.isEnabled = (contact.id == contactId)
            itemView.switchParticipate.isChecked = contact.participate
            contact.contact = contact.id
            itemView.setOnClickListener {
                clickListener(
                    contact,
                    itemView.switchParticipate
                )
            }
            itemView.switchParticipate.setOnClickListener {
                clickListener(
                    contact,
                    itemView.switchParticipate
                )
            }
        }
    }
}