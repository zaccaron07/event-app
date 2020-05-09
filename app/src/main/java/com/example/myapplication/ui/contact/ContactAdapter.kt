package com.example.myapplication.ui.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Contact
import kotlinx.android.synthetic.main.contact_detail_adapter_item_layout.view.*

class ContactAdapter(
    private val contactList: MutableList<Contact>,
    private val listener: RecyclerViewContactClickListener
) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.contact_detail_adapter_item_layout, p0, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contactList[position], listener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact, listener: RecyclerViewContactClickListener) {
            itemView.tvName.text = contact.name
            itemView.checkBoxContact.isChecked = contact.checked

            itemView.checkBoxContact.setOnClickListener {
                listener.onRecyclerViewItemClick(contact)
                itemView.checkBoxContact.isChecked = contact.checked
            }
        }
    }
}