package com.example.myapplication

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.injection.ViewModelFactory
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.repositories.ContactRepository
import com.example.myapplication.data.repositories.GroupRepository
import com.example.myapplication.network.ContactApi
import com.example.myapplication.network.GroupApi
import com.example.myapplication.ui.auth.AuthViewModel
import com.example.myapplication.ui.contact.ContactViewModel
import com.example.myapplication.ui.group.GroupsViewModel
import com.example.myapplication.ui.profile.ContactProfileViewModel
import com.example.myapplication.utils.APIConstants
import com.example.myapplication.utils.extension.bindViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class EventApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@EventApplication))

        bind() from singleton { AppDatabase(instance()) }
        bind<Retrofit.Builder>() with provider { Retrofit.Builder() }
        bind<Retrofit>() with singleton {
            instance<Retrofit.Builder>()
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
        bind() from singleton { ContactRepository(instance(), instance()) }
        bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(kodein.direct) }
        bindViewModel<AuthViewModel>() with provider {
            AuthViewModel(instance())
        }
        bindViewModel<GroupsViewModel>() with provider {
            GroupsViewModel(instance(), instance())
        }
        bindViewModel<ContactProfileViewModel>() with provider {
            ContactProfileViewModel(instance())
        }
        bindViewModel<ContactViewModel>() with provider {
            ContactViewModel(instance())
        }
    }

}