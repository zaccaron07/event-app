package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.ui.group.GroupsFragment
import com.example.myapplication.ui.profile.ContactProfileFragment

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val firstLogin = intent.getBooleanExtra("firstLogin", false)

        if (firstLogin) {
            this.replaceFragment(ContactProfileFragment.newInstance())
        } else {
            this.replaceFragment(GroupsFragment.newInstance())
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(fragment.tag)
        fragmentTransaction.replace(R.id.container, fragment)

        fragmentTransaction.commit()
    }

    fun startActivity(activityClass: Class<*>, data: String = "", contactId: String = "") {
        val intent = Intent(this, activityClass)
        intent.putExtra("group", data)

        if (contactId != "") {
            intent.putExtra("contactId", contactId)
        }

        this.startActivity(intent)
    }
}
