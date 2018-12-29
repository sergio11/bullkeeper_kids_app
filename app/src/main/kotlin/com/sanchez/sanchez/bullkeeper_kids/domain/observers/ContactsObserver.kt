package com.sanchez.sanchez.bullkeeper_kids.domain.observers

import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.contacts.SynchronizeTerminalContactsInteract
import timber.log.Timber
import javax.inject.Inject

/**
 * Contacts Observer
 */
class ContactsObserver
    @Inject constructor(
            handler: Handler,
            private val synchronizeTerminalContactsInteract: SynchronizeTerminalContactsInteract
    ): ContentObserver(handler) {

    /**
     * On Change
     */
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        if (!selfChange) {
            Timber.d("Sync Contacts")
            synchronizeTerminalContactsInteract(UseCase.None()){
                it.either(fnL = fun(_: Failure) {
                    Timber.d("Contact Sync failed")
                }, fnR = fun(total: Int){
                    Timber.d("Contact sync total -> %d", total)
                })
            }
        }
    }
}