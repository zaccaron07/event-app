package com.example.myapplication.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse

class LoginFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        openAuthenticationActivity()
    }

    private fun openAuthenticationActivity() {
        val phoneConfigWithDefaultNumber = AuthUI.IdpConfig.PhoneBuilder()
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

    private fun navigateToContactProfile() {
        findNavController().navigate(R.id.action_loginFragment_to_contactProfileFragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode === Activity.RESULT_OK) {
                navigateToContactProfile()
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