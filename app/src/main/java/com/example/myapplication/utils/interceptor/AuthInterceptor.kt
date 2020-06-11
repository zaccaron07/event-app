package com.example.myapplication.utils.interceptor

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val result = Tasks.await(FirebaseAuth.getInstance().currentUser!!.getIdToken(false))
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", result!!.token)
            .build()

        return chain.proceed(newRequest)
    }
}