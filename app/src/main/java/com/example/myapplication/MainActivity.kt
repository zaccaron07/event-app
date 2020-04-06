package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.model.Contact
import com.example.myapplication.model.database.AppDatabase
import com.example.myapplication.ui.HomeActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.PhoneBuilder
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.invoke(this)

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

        AuthUI.getInstance()
            .signOut(this)
    }

    private fun findUser() {
        GlobalScope.launch {
            val auth = FirebaseAuth.getInstance()
            val contact = withContext(Dispatchers.Default) {
                db?.contactDao()?.getContact()
            }

            if (contact != null) {
                goToHome()
            } else {
                if (auth.currentUser != null) {
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
        val phoneConfigWithDefaultNumber = PhoneBuilder()
            .setDefaultCountryIso("br")
            .build()

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(
                    listOf(phoneConfigWithDefaultNumber)
                )
                .build(), RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode === Activity.RESULT_OK) {
                goToHome(true)
                finish()
            } else {
                if (response == null) {
                    // User pressed back button
                    return
                }
                if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                    return
                }
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 45776
    }
}
