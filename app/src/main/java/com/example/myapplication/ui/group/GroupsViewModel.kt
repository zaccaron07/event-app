package com.example.myapplication.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.base.BaseViewModel
import com.example.myapplication.data.model.Contact
import com.example.myapplication.data.model.Group
import com.example.myapplication.data.model.GroupDTO
import com.example.myapplication.data.repositories.ContactRepository
import com.example.myapplication.data.repositories.GroupRepository
import com.example.myapplication.utils.Coroutines
import com.example.myapplication.utils.GroupUtils
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

    var timePickerDialogData = MutableLiveData<Boolean>()
    var datePickerDialogData = MutableLiveData<Boolean>()
    var addGroupData = MutableLiveData<Boolean>()

    var selectedGroup = Group()
    var groupDetailSaved = MutableLiveData<Boolean>()

    var group = Group()

    init {
        groupDetailSaved.value = false

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

    fun onAddGroupClick() {
        addGroupData.value = true
    }

    fun saveGroupDetail() {
        jobSaveGroupDetail = Coroutines.ioThenMain(
            { groupRepository.updateGroupDetail(contact.id, selectedGroup) },
            { groupDetailSaved.value = true }
        )
    }
}