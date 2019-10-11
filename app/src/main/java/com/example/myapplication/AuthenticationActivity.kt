package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.facebook.accountkit.AccountKitLoginResult
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType

class AuthenticationActivity : AppCompatActivity() {

    val APP_REQUEST_CODE = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        val intent = Intent(this, AccountKitActivity::class.java)
        val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
            LoginType.PHONE,
            AccountKitActivity.ResponseType.TOKEN
        )
        configurationBuilder.setDefaultCountryCode("BR")

        intent.putExtra(
            AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
            configurationBuilder.build()
        )
        startActivityForResult(intent, APP_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val loginResult =
            data?.getParcelableExtra<AccountKitLoginResult>(AccountKitLoginResult.RESULT_KEY)

        if (loginResult?.error != null) {
            Log.d("errorL", loginResult.error?.errorType?.message)
        } else {
            Log.d("sucessoL", loginResult?.toString())
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("firstLogin", true)

            this.startActivity(intent)
        }
    }
}
