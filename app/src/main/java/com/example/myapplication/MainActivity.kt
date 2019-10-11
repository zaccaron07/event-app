package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.accountkit.AccountKit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase?.invoke(this)

        findUser()
    }

    private fun insertContact() {
        var contact = Contact("Gabriel", "+5548998176127")
        contact.id = "5d8678bbcb6e470017c9bd95"

        GlobalScope.launch {
            db?.contactDao()?.insertContact(contact)
        }
    }

    private fun deleteContact() {
        GlobalScope.launch {
            db?.contactDao()?.deleteContact()
        }
        AccountKit.logOut()
    }

    private fun findUser() {
        GlobalScope.launch {
            val contact = withContext(Dispatchers.Default) {
                db?.contactDao()?.getContact()
            }

            if (contact != null) {
                goToHome()
            } else {
                if (AccountKit.getCurrentAccessToken() != null) {
                    goToHome(true)
                } else {
                    openAuthenticationActivity()
                }
            }
        }
    }

    private fun goToHome(aFirstLogin: Boolean = false) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("firstLogin", aFirstLogin)

        this.startActivity(intent)
    }

    private fun openAuthenticationActivity() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        this.startActivity(intent)
    }
}
