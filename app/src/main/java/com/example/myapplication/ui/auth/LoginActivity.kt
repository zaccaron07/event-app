package com.example.myapplication.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.data.model.Contact
import com.example.myapplication.ui.HomeActivity
import com.example.myapplication.utils.Coroutines
import com.example.myapplication.utils.extension.kodeinViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.PhoneBuilder
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class LoginActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()

    private val viewModel: AuthViewModel by kodeinViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        decideNextActivity()
    }

    private fun insertContact() {
        var contact = Contact("Gabriel", "+5548998176127")
        contact.id = "5d8678bbcb6e470017c9bd95"

        Coroutines.io {
            viewModel.insertContact(contact)
        }
    }

    private fun deleteContact() {
        Coroutines.io {
            viewModel.deleteContact()
        }

        AuthUI.getInstance()
            .signOut(this)
    }

    private fun decideNextActivity() {
        Coroutines.io {
            val auth = viewModel.getFirebaseAuth()
            val contact = viewModel.getContact()

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
                .build(),
            RC_SIGN_IN
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
