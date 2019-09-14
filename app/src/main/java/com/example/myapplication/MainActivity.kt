package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.facebook.accountkit.AccountKit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase?.invoke(this)
        // deleteuser()
        //insertUser()
        findUser()
    }

    private fun insertUser() {
        var user = User("Gabriel", "48")

        GlobalScope.launch {
            db?.userDao()?.insertUser(user)
        }
    }

    private fun deleteuser() {
        GlobalScope.launch {
            db?.userDao()?.deleteUser()
        }
    }

    private fun findUser() {
        GlobalScope.launch {
            val user = withContext(Dispatchers.Default) {
                db?.userDao()?.getUser()
            }

            if (user != null) {
                Log.d("totalU", user.toString())
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
