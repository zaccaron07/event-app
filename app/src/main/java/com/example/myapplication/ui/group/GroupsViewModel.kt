package com.example.myapplication.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.base.BaseViewModel
import com.example.myapplication.data.model.Contact
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.model.GroupDTO
import com.example.myapplication.data.repositories.ContactRepository
import com.example.myapplication.data.repositories.GroupRepository
import com.example.myapplication.utils.Coroutines
import com.example.myapplication.utils.GroupUtils
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Job

class GroupsViewModel(
    private val contactRepository: ContactRepository,
    private val groupRepository: GroupRepository
) : BaseViewModel() {
    lateinit var contact: Contact

    private lateinit var job: Job
    private lateinit var jobSaveGroupDetail: Job

    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>>
        get() = _groups

    var timePickerDialogData: LiveEvent<Boolean> = LiveEvent()
    var datePickerDialogData: LiveEvent<Boolean> = LiveEvent()
    val locationData: LiveEvent<Boolean> = LiveEvent()

    var selectedGroup = Group()
    var groupDetailSaved: LiveEvent<Boolean> = LiveEvent()

    var group = Group()

    init {
        getUserGroups()

    }

    override fun onCleared() {
        super.onCleared()

        if (::job.isInitialized) job.cancel()
        if (::jobSaveGroupDetail.isInitialized) jobSaveGroupDetail.cancel()
    }

    private fun getUserGroups() {
        Coroutines.io {
            val contact = contactRepository.getContact()

            onRetrieveContactSuccess(contact)
        }
    }

    private fun onRetrieveContactSuccess(contact: Contact) {
        job = Coroutines.ioThenMain(
            { groupRepository.getGroups(contact.id) },
            { onRetrieveGroupsSuccess(it!!) }
        )

        this.contact = contact
    }

    private fun onRetrieveGroupsSuccess(groupListDTO: List<GroupDTO>) {
        val groupUtils = GroupUtils()
        val groupList = groupUtils.formatJsonGroup(groupListDTO)

        _groups.value = groupList
    }

    fun onTimePickerDialogClick() {
        timePickerDialogData.value = true
    }

    fun onDatePickerDialogClick() {
        datePickerDialogData.value = true
    }

    fun onAddContactClick() {
        navigate(R.id.action_groupFragment_to_contactFragment)
    }

    fun onAddGroupClick() {
        navigate(R.id.action_groupsFragment_to_groupFragment)
    }

    fun onLocationClick() {
        locationData.value = true
    }

    fun saveGroupDetail() {
        jobSaveGroupDetail = Coroutines.ioThenMain(
            { groupRepository.updateGroupDetail(contact.id, selectedGroup) },
            {
                groupDetailSaved.value = true
                popBackStack()
            }
        )
    }
}