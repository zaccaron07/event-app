package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact_detail_adapter_item_layout.view.*

class ContactRvAdapter(
    public val contactList: ArrayList<ContactDetail>,
    private val clickListener: (ContactDetail, CheckBox) -> Unit
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
        fun bind(contactDetail: ContactDetail, clickListener: (ContactDetail, CheckBox) -> Unit) {
            itemView.tvName.text = contactDetail.name
            itemView.checkBoxContact.isChecked = contactDetail.checked
            itemView.setOnClickListener { clickListener(contactDetail, itemView.checkBoxContact) }
            itemView.checkBoxContact.setOnClickListener {
                clickListener(
                    contactDetail,
                    itemView.checkBoxContact
                )
            }
        }
    }
}