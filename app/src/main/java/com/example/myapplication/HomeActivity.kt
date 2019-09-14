package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class HomeActivity : AppCompatActivity(),
    UserProfileFragment.OnFragmentInteractionListener,
    GroupsFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val firstLogin = intent.getBooleanExtra("firstLogin", false)

        if (firstLogin) {
            this.replaceFragment(UserProfileFragment.newInstance())
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

    fun startActivity(activityClass: Class<*>, data: String = "") {
        val intent = Intent(this, activityClass)
        intent.putExtra("group", data)
        this.startActivity(intent)
    }

}
