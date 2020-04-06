package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.Contact
import kotlinx.android.synthetic.main.contact_detail_adapter_item_layout.view.*

class ContactRvAdapter(
    private val contactList: ArrayList<Contact>,
    private val clickListener: (Contact, CheckBox) -> Unit
) :
    RecyclerView.Adapter<ContactRvAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.contact_detail_adapter_item_layout, p0, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contactList[position], clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(Contact: Contact, clickListener: (Contact, CheckBox) -> Unit) {
            itemView.tvName.text = Contact.name
            itemView.checkBoxContact.isChecked = Contact.checked
            itemView.setOnClickListener { clickListener(Contact, itemView.checkBoxContact) }
            itemView.checkBoxContact.setOnClickListener {
                clickListener(
                    Contact,
                    itemView.checkBoxContact
                )
            }
        }
    }
}