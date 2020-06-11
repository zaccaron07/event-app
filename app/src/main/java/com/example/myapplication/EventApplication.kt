package com.example.myapplication

import android.app.Application
import android.content.ContentResolver
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.network.ContactApi
import com.example.myapplication.data.network.GroupApi
import com.example.myapplication.data.repositories.ContactRepository
import com.example.myapplication.data.repositories.GroupRepository
import com.example.myapplication.injection.ViewModelFactory
import com.example.myapplication.utils.interceptor.AuthInterceptor
import com.example.myapplication.ui.auth.AuthViewModel
import com.example.myapplication.ui.contact.ContactViewModel
import com.example.myapplication.ui.group.GroupsViewModel
import com.example.myapplication.ui.profile.ContactProfileViewModel
import com.example.myapplication.utils.APIConstants
import com.example.myapplication.utils.extension.bindViewModel
import okhttp3.OkHttpClient
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class EventApplication : Application(), DIAware {

    override val di = DI.lazy {
        import(androidXModule(this@EventApplication))

        bind() from singleton { AppDatabase(instance()) }
        bind<Retrofit.Builder>() with provider { Retrofit.Builder() }
        bind<ContentResolver>() with singleton { contentResolver }
        bind<Retrofit>() with singleton {
            instance<Retrofit.Builder>()
                .client(
                    OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build()
                )
                .baseUrl(APIConstants.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }
        bind<GroupApi>() with singleton {
            instance<Retrofit>().create(GroupApi::class.java)
        }
        bind<ContactApi>() with singleton {
            instance<Retrofit>().create(ContactApi::class.java)
        }
        bind() from singleton { GroupRepository(instance()) }
        bind() from singleton { ContactRepository(instance(), instance(), instance()) }
        bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(di.direct) }
        bindViewModel<AuthViewModel>() with provider {
            AuthViewModel(instance())
        }
        bindViewModel<GroupsViewModel>() with singleton { GroupsViewModel(instance(), instance()) }
        bindViewModel<ContactProfileViewModel>() with provider {
            ContactProfileViewModel(instance())
        }
        bindViewModel<ContactViewModel>() with provider {
            ContactViewModel(instance(), instance())
        }
    }

}