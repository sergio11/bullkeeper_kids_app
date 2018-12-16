package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.app.Activity
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IContactsService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.ContactRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.contacts.SynchronizeTerminalContactsInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Contacts Module
 */
@Module
class ContactsModule {

    /**
     * Provide Contacts Service
     */
    @Provides
    @PerActivity
    fun provideContactsService(retrofit: Retrofit) : IContactsService
            = retrofit.create(IContactsService::class.java)

    /**
     * Provide Contact Repository
     */
    @Provides
    @PerActivity
    fun provideContactRepository(): ContactRepositoryImpl
            = ContactRepositoryImpl()

    /**
     * Provide Synchronize Terminal Contacts Interact
     */
    @Provides
    @PerActivity
    fun provideSynchronizeTerminalContactsInteract(
            contactsService: IContactsService,
            contactRepositoryImpl: ContactRepositoryImpl,
            activity: Activity,
            preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit)
            = SynchronizeTerminalContactsInteract(activity, contactsService, contactRepositoryImpl, preferenceRepository, retrofit)
}