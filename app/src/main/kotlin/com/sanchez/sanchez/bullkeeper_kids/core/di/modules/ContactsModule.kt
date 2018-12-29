package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import android.os.Handler
import com.sanchez.sanchez.bullkeeper_kids.data.net.service.IContactsService
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.ContactRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.contacts.SynchronizeTerminalContactsInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.observers.ContactsObserver
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Contacts Module
 */
@Module
class ContactsModule {

    /**
     * Provide Contacts Service
     */
    @Provides
    @Singleton
    fun provideContactsService(retrofit: Retrofit) : IContactsService
            = retrofit.create(IContactsService::class.java)

    /**
     * Provide Contact Repository
     */
    @Provides
    @Singleton
    fun provideContactRepository(): ContactRepositoryImpl
            = ContactRepositoryImpl()

    /**
     * Provide Synchronize Terminal Contacts Interact
     */
    @Provides
    @Singleton
    fun provideSynchronizeTerminalContactsInteract(
            contactsService: IContactsService,
            contactRepositoryImpl: ContactRepositoryImpl,
            context: Context,
            preferenceRepository: IPreferenceRepository,
            retrofit: Retrofit)
            = SynchronizeTerminalContactsInteract(context, contactsService, contactRepositoryImpl, preferenceRepository, retrofit)

    /**
     * Provide Contacts Observer
     */
    @Provides
    @Singleton
    fun provideContactsObserver(
            handler: Handler,
            synchronizeTerminalContactsInteract: SynchronizeTerminalContactsInteract
    ) = ContactsObserver(handler, synchronizeTerminalContactsInteract)

}