package com.example.myapplication.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.base.BaseViewModel
import com.example.myapplication.model.Contact
import com.example.myapplication.model.ContactDao
import com.example.myapplication.model.Group
import com.example.myapplication.model.GroupDTO
import com.example.myapplication.network.GroupApi
import com.example.myapplication.utils.GroupUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GroupViewModel(private val contactDao: ContactDao) : BaseViewModel() {
    @Inject
    lateinit var groupApi: GroupApi
    lateinit var contact: Contact
    private val groupList = MutableLiveData<List<Group>>()

    private lateinit var subscriptionGroup: Disposable
    private lateinit var subscriptionContact: Disposable

    init {
        getUserGroups()
    }

    override fun onCleared() {
        super.onCleared()

        subscriptionGroup.dispose()
        subscriptionContact.dispose()
    }

    fun getGroupList(): LiveData<List<Group>> {
        return groupList
    }

    private fun getUserGroups() {
        subscriptionContact = contactDao.getContact()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onRetrieveContactSuccess(it) },
                { onRetrieveContactError(it) }
            )
    }

    private fun onRetrieveContactSuccess(contact: Contact) {
        this.contact = contact
        subscriptionGroup = groupApi.getGroups(contact.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onRetrieveGroupsSuccess(it) },
                { onRetrieveGroupsError(it) }
            )
    }

    private fun onRetrieveContactError(ex: Throwable) {
    }

    private fun onRetrieveGroupsSuccess(groupListDTO: List<GroupDTO>) {
        val groupUtils = GroupUtils()
        val groupList = groupUtils.formatJsonGroup(groupListDTO)

        this.groupList.value = groupList
    }

    private fun onRetrieveGroupsError(ex: Throwable) {
    }
}