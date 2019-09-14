package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact_detail_adapter_item_layout.view.tvName
import kotlinx.android.synthetic.main.group_participants_adapter_item_layout.view.*

class GroupParticipantsRvAdapter(
    private val userGroupDetailList: ArrayList<UserGroupDetail>,
    private val clickListener: (UserGroupDetail, Switch) -> Unit
) :
    RecyclerView.Adapter<GroupParticipantsRvAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.group_participants_adapter_item_layout, p0, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userGroupDetailList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userGroupDetailList[position], clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            userGroupDetail: UserGroupDetail,
            clickListener: (UserGroupDetail, Switch) -> Unit
        ) {
            itemView.tvName.text = userGroupDetail.name
            itemView.switchParticipate.isEnabled = (userGroupDetail.permission == 1)
            itemView.switchParticipate.isChecked = userGroupDetail.participate
            itemView.setOnClickListener {
                clickListener(
                    userGroupDetail,
                    itemView.switchParticipate
                )
            }
            itemView.switchParticipate.setOnClickListener {
                clickListener(
                    userGroupDetail,
                    itemView.switchParticipate
                )
            }
        }
    }
}